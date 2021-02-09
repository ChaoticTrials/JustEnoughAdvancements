package de.melanx.jea.network;

import de.melanx.jea.ingredient.AdvancementInfo;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.network.NetworkX;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class JustEnoughNetwork extends NetworkX {
    public JustEnoughNetwork(ModX mod) {
        super(mod);
    }

    @Override
    protected String getProtocolVersion() {
        return "1";
    }

    @Override
    protected void registerPackets() {
        this.register(new AdvancementInfoUpdateSerializer(), () -> AdvancementInfoUpdateHandler::handle, NetworkDirection.PLAY_TO_CLIENT);
    }

    public void syncAdvancement(Collection<Advancement> advancements) {
        if (!advancements.isEmpty()) {
            this.instance.send(PacketDistributor.ALL.noArg(), getAdvancementMessage(advancements));
        }
    }

    public void syncAdvancementTo(Collection<Advancement> advancements, ServerPlayerEntity playerEntity) {
        if (!advancements.isEmpty()) {
            this.instance.send(PacketDistributor.PLAYER.with(() -> playerEntity), getAdvancementMessage(advancements));
        }
    }

    private static AdvancementInfoUpdateSerializer.AdvancementInfoUpdateMessage getAdvancementMessage(Collection<Advancement> advancements) {
        Set<AdvancementInfo> infos = new HashSet<>();

        advancements.forEach(advancement -> {
            if (advancement.getDisplay() != null) {
                ItemPredicate tooltip = null;
                for (Criterion criterion : advancement.getCriteria().values()) {
                    ICriterionInstance inst = criterion.getCriterionInstance();
                    if (inst instanceof InventoryChangeTrigger.Instance) {
                        if (tooltip != null) {
                            tooltip = null;
                            break;
                        }
                        ItemPredicate[] predicates = ((InventoryChangeTrigger.Instance) inst).items;
                        if (predicates.length == 1) {
                            tooltip = predicates[0];
                        } else {
                            tooltip = null;
                            break;
                        }
                    }
                }

                infos.add(new AdvancementInfo(advancement.getId(), advancement.getDisplay().icon, advancement.getDisplay().getTitle(), tooltip));
            }
        });

        return new AdvancementInfoUpdateSerializer.AdvancementInfoUpdateMessage(infos);
    }
}
