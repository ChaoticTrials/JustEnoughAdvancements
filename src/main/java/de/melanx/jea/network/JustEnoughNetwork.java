package de.melanx.jea.network;

import de.melanx.jea.AdvancementInfo;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.network.NetworkX;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Set;
import java.util.stream.Collectors;

public class JustEnoughNetwork extends NetworkX {
    
    // Increment when sth changed with the base networking.
    public static final int BASE_PROTOCOL_VERSION = 4;
    
    // Increment when sth changed with the plugins networking.
    // Set to 0 when BASE_PROTOCOL_VERSION is incremented.
    public static final int PLUGIN_PROTOCOL_VERSION = 0;
    
    public JustEnoughNetwork(ModX mod) {
        super(mod);
    }

    @Override
    protected String getProtocolVersion() {
        return BASE_PROTOCOL_VERSION + "." + PLUGIN_PROTOCOL_VERSION;
    }

    @Override
    protected void registerPackets() {
        this.register(new AdvancementInfoUpdateSerializer(), () -> AdvancementInfoUpdateHandler::handle, NetworkDirection.PLAY_TO_CLIENT);
    }

    public void syncAdvancements(MinecraftServer server) {
        this.instance.send(PacketDistributor.ALL.noArg(), collectAdvancements(server));
    }

    public void syncAdvancements(MinecraftServer server, ServerPlayerEntity player) {
        this.instance.send(PacketDistributor.PLAYER.with(() -> player), collectAdvancements(server));
    }

    private static AdvancementInfoUpdateSerializer.AdvancementInfoUpdateMessage collectAdvancements(MinecraftServer server) {
        Set<AdvancementInfo> advancements = server.getAdvancementManager().getAllAdvancements().stream().flatMap(AdvancementInfo::createAsStream).collect(Collectors.toSet());
        return new AdvancementInfoUpdateSerializer.AdvancementInfoUpdateMessage(advancements);
    }
}
