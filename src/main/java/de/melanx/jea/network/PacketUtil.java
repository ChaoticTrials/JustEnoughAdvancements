package de.melanx.jea.network;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.api.JeaRegistries;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    
    public static Map<String, ResourceLocation> getCriteriaSerializers(Map<String, Criterion> criteria) {
        //noinspection UnstableApiUsage
        return criteria.entrySet().stream()
                .filter(entry -> entry.getValue().getCriterionInstance() != null)
                .flatMap(entry -> CriterionSerializer.getSerializerAsStream(entry.getValue().getCriterionInstance())
                        .map(serializer -> Pair.of(entry.getKey(), serializer.getRegistryName()))
                )
                .collect(ImmutableMap.toImmutableMap(Pair::getKey, Pair::getValue));
    }
    
    public static void writeCriteria(PacketBuffer buffer, Map<String, Criterion> criteria, Map<String, ResourceLocation> serializers) {
        List<Triple<String, Criterion, ? extends CriterionSerializer<?>>> writeableCriteria = criteria.entrySet().stream()
                .filter(entry -> entry.getValue().getCriterionInstance() != null)
                .filter(entry -> serializers.containsKey(entry.getKey()))
                .map(entry -> Triple.of(entry.getKey(), entry.getValue(), JeaRegistries.CRITERION_SERIALIZER.getValue(serializers.get(entry.getKey()))))
                .filter(entry -> entry.getRight() != null)
                .collect(Collectors.toList());
        buffer.writeVarInt(writeableCriteria.size());
        for (Triple<String, Criterion, ? extends CriterionSerializer<?>> entry : writeableCriteria) {
            buffer.writeString(entry.getLeft(), 32767);
            //noinspection ConstantConditions
            buffer.writeResourceLocation(entry.getRight().getRegistryName());
            //noinspection unchecked
            ((CriterionSerializer<ICriterionInstance>) entry.getRight()).write(entry.getMiddle().getCriterionInstance(), buffer);
        }
    }
    
    public static Pair<Map<String, Criterion>, Map<String, ResourceLocation>> readCriteria(PacketBuffer buffer) {
        int size = buffer.readVarInt();
        Map<String, Criterion> criteria = new HashMap<>(size);
        Map<String, ResourceLocation> criteriaSerializers = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            String key = buffer.readString(32767);
            ResourceLocation serializerId = buffer.readResourceLocation();
            CriterionSerializer<?> serializer = JeaRegistries.CRITERION_SERIALIZER.getValue(serializerId);
            if (serializer == null) {
                throw new IllegalStateException("Server sent unknown advancement criterion instance. Can't deserialise: serialiser unknown: '" + serializerId + "'");
            }
            ICriterionInstance criterion = serializer.read(buffer);
            criteria.put(key, new Criterion(criterion));
            criteriaSerializers.put(key, serializerId);
        }
        return Pair.of(ImmutableMap.copyOf(criteria), ImmutableMap.copyOf(criteriaSerializers));
    }
}
