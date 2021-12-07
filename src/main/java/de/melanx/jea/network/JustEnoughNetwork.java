package de.melanx.jea.network;

import de.melanx.jea.AdvancementInfo;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.network.NetworkX;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JustEnoughNetwork extends NetworkX {
    
    public JustEnoughNetwork(ModX mod) {
        super(mod);
    }

    @Override
    protected Protocol getProtocol() {
        return Protocol.of("6");
    }

    @Override
    protected void registerPackets() {
        this.register(new AdvancementInfoUpdateSerializer(), () -> AdvancementInfoUpdateHandler::handle, NetworkDirection.PLAY_TO_CLIENT);
    }

    public void syncAdvancements(MinecraftServer server) {
        this.channel.send(PacketDistributor.ALL.noArg(), collectAdvancements(server));
    }

    public void syncAdvancements(MinecraftServer server, ServerPlayer player) {
        this.channel.send(PacketDistributor.PLAYER.with(() -> player), collectAdvancements(server));
    }

    private static AdvancementInfoUpdateSerializer.AdvancementInfoUpdateMessage collectAdvancements(MinecraftServer server) {
        Set<AdvancementInfo> advancements = server.getAdvancements().getAllAdvancements().stream().map(AdvancementInfo::create).flatMap(Optional::stream).collect(Collectors.toSet());
        return new AdvancementInfoUpdateSerializer.AdvancementInfoUpdateMessage(advancements);
    }
}
