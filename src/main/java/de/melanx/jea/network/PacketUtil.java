package de.melanx.jea.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.network.PacketBuffer;

public class PacketUtil {

    public static final Gson GSON = net.minecraft.util.Util.make(() -> {
        GsonBuilder gsonbuilder = new GsonBuilder();
        gsonbuilder.disableHtmlEscaping();
        return gsonbuilder.create();
    });
    
    public static void writeJSON(PacketBuffer buffer, JsonElement json) {
        buffer.writeString(GSON.toJson(json), 0x40000);
    }

    public static JsonElement readJSON(PacketBuffer buffer) {
        return readJSON(buffer, JsonElement.class);
    }
    
    public static <T extends JsonElement> T readJSON(PacketBuffer buffer, Class<T> clazz) {
        return GSON.fromJson(buffer.readString(0x40000), clazz);
    }
}
