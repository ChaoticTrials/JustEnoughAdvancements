package de.melanx.jea.network;

import de.melanx.jea.AdvancementInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.network.NetworkX;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JustEnoughNetwork extends NetworkX {
    
    public JustEnoughNetwork(ModX mod) {
        super(mod);

        this.register(new AdvancementInfoUpdateHandler());
    }

    @Override
    protected String getVersion() {
        return "7";
    }

    public void syncAdvancements(MinecraftServer server) {
        PacketDistributor.sendToAllPlayers(JustEnoughNetwork.collectAdvancements(server));
    }

    public void syncAdvancements(MinecraftServer server, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, JustEnoughNetwork.collectAdvancements(server));
    }

    private static AdvancementInfoUpdateHandler.Message collectAdvancements(MinecraftServer server) {
        Set<AdvancementInfo> advancements = server.getAdvancements().getAllAdvancements().stream().map(AdvancementInfo::create).flatMap(Optional::stream).collect(Collectors.toSet());
        return new AdvancementInfoUpdateHandler.Message(advancements);
    }
}
