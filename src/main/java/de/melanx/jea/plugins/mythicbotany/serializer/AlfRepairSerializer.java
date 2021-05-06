package de.melanx.jea.plugins.mythicbotany.serializer;

import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.network.PacketUtil;
import de.melanx.jea.plugins.mythicbotany.MythicBotanyCriteriaIds;
import mythicbotany.advancement.AlfRepairTrigger;
import net.minecraft.network.PacketBuffer;

public class AlfRepairSerializer extends CriterionSerializer<AlfRepairTrigger.Instance> {

    public AlfRepairSerializer() {
        super(AlfRepairTrigger.Instance.class);
        this.setRegistryName(MythicBotanyCriteriaIds.ALF_REPAIR);
    }

    @Override
    public void write(AlfRepairTrigger.Instance instance, PacketBuffer buffer) {
        PacketUtil.writeItemPredicate(instance.item, buffer);
    }

    @Override
    public AlfRepairTrigger.Instance read(PacketBuffer buffer) {
        return new AlfRepairTrigger.Instance(PacketUtil.readItemPredicate(buffer));
    }
}
