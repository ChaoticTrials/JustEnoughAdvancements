package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.UsedTotemTrigger;
import net.minecraft.network.FriendlyByteBuf;

public class UseTotemSerializer extends CriterionSerializer<UsedTotemTrigger.TriggerInstance> {

    public UseTotemSerializer() {
        super(UsedTotemTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.USE_TOTEM);
    }

    @Override
    public void write(UsedTotemTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeItemPredicate(instance.item, buffer);
    }

    @Override
    public UsedTotemTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        return new UsedTotemTrigger.TriggerInstance(EntityPredicate.Composite.ANY, PacketUtil.readItemPredicate(buffer));
    }
}
