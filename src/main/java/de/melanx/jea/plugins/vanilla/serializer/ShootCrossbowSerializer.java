package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ShotCrossbowTrigger;
import net.minecraft.network.PacketBuffer;

public class ShootCrossbowSerializer extends CriterionSerializer<ShotCrossbowTrigger.Instance> {

    public ShootCrossbowSerializer() {
        super(ShotCrossbowTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.SHOOT_CROSSBOW);
    }

    @Override
    public void write(ShotCrossbowTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeItemPredicate(instance.itemPredicate, buffer);
    }

    @Override
    public ShotCrossbowTrigger.Instance read(PacketBuffer buffer) {
        return new ShotCrossbowTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, PacketUtil.readItemPredicate(buffer));
    }
}
