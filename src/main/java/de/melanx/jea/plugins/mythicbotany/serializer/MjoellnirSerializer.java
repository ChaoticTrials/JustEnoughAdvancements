package de.melanx.jea.plugins.mythicbotany.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.mythicbotany.MythicBotanyCriteriaIds;
import de.melanx.jea.util.LootUtil;
import mythicbotany.advancement.MjoellnirTrigger;
import net.minecraft.network.PacketBuffer;

public class MjoellnirSerializer extends CriterionSerializer<MjoellnirTrigger.Instance> {

    public MjoellnirSerializer() {
        super(MjoellnirTrigger.Instance.class);
        this.setRegistryName(MythicBotanyCriteriaIds.MJOELLNIR);
    }

    @Override
    public void write(MjoellnirTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeItemPredicate(instance.item, buffer);
        PacketUtil.writeEntityPredicate(LootUtil.asEntity(instance.entity), buffer);
    }

    @Override
    public MjoellnirTrigger.Instance read(PacketBuffer buffer) {
        return new MjoellnirTrigger.Instance(PacketUtil.readItemPredicate(buffer), LootUtil.asLootPredicate(PacketUtil.readEntityPredicate(buffer)));
    }
}
