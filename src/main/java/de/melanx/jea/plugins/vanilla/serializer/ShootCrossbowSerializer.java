package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ShotCrossbowTrigger;
import net.minecraft.network.FriendlyByteBuf;

public class ShootCrossbowSerializer extends CriterionSerializer<ShotCrossbowTrigger.TriggerInstance> {

    public ShootCrossbowSerializer() {
        super(ShotCrossbowTrigger.TriggerInstance.class);
        this.setRegistryName(VanillaCriteriaIds.SHOOT_CROSSBOW);
    }

    @Override
    public void write(ShotCrossbowTrigger.TriggerInstance instance, FriendlyByteBuf buffer) {
        PacketUtil.writeItemPredicate(instance.item, buffer);
    }

    @Override
    public ShotCrossbowTrigger.TriggerInstance read(FriendlyByteBuf buffer) {
        return new ShotCrossbowTrigger.TriggerInstance(EntityPredicate.Composite.ANY, PacketUtil.readItemPredicate(buffer));
    }
}
