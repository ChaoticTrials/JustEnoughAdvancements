package de.melanx.jea.plugins.vanilla.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.vanilla.VanillaCriteriaIds;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.UsedTotemTrigger;
import net.minecraft.network.PacketBuffer;

public class UseTotemSerializer extends CriterionSerializer<UsedTotemTrigger.Instance> {

    public UseTotemSerializer() {
        super(UsedTotemTrigger.Instance.class);
        this.setRegistryName(VanillaCriteriaIds.USE_TOTEM);
    }

    @Override
    public void write(UsedTotemTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeItemPredicate(instance.item, buffer);
    }

    @Override
    public UsedTotemTrigger.Instance read(PacketBuffer buffer) {
        return new UsedTotemTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, PacketUtil.readItemPredicate(buffer));
    }
}
