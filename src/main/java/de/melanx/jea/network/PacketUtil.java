package de.melanx.jea.network;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.api.JeaRegistries;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PacketUtil {

    public static final Gson GSON = net.minecraft.Util.make(() -> {
        GsonBuilder gsonbuilder = new GsonBuilder();
        gsonbuilder.disableHtmlEscaping();
        return gsonbuilder.create();
    });
    
    public static void writeJSON(FriendlyByteBuf buffer, JsonElement json) {
        buffer.writeUtf(GSON.toJson(json), 0x40000);
    }

    public static JsonElement readJSON(FriendlyByteBuf buffer) {
        return readJSON(buffer, JsonElement.class);
    }
    
    public static <T extends JsonElement> T readJSON(FriendlyByteBuf buffer, Class<T> clazz) {
        return GSON.fromJson(buffer.readUtf(0x40000), clazz);
    }
    
    public static Map<String, ResourceLocation> getCriteriaSerializers(Map<String, Criterion> criteria) {
        //noinspection UnstableApiUsage
        return criteria.entrySet().stream()
                .filter(entry -> entry.getValue().getTrigger() != null)
                .flatMap(entry -> CriterionSerializer.getSerializer(entry.getValue().getTrigger()).stream()
                        .map(serializer -> Pair.of(entry.getKey(), serializer.getRegistryName()))
                )
                .collect(ImmutableMap.toImmutableMap(Pair::getKey, Pair::getValue));
    }
    
    public static void writeCriteria(FriendlyByteBuf buffer, Map<String, Criterion> criteria, Map<String, ResourceLocation> serializers) {
        List<Triple<String, Criterion, ? extends CriterionSerializer<?>>> writeableCriteria = criteria.entrySet().stream()
                .filter(entry -> entry.getValue().getTrigger() != null)
                .filter(entry -> serializers.containsKey(entry.getKey()))
                .map(entry -> Triple.of(entry.getKey(), entry.getValue(), JeaRegistries.CRITERION_SERIALIZER.getValue(serializers.get(entry.getKey()))))
                .filter(entry -> entry.getRight() != null)
                .collect(Collectors.toList());
        buffer.writeVarInt(writeableCriteria.size());
        for (Triple<String, Criterion, ? extends CriterionSerializer<?>> entry : writeableCriteria) {
            buffer.writeUtf(entry.getLeft(), 32767);
            //noinspection ConstantConditions
            buffer.writeResourceLocation(entry.getRight().getRegistryName());
            //noinspection unchecked
            ((CriterionSerializer<CriterionTriggerInstance>) entry.getRight()).write(entry.getMiddle().getTrigger(), buffer);
        }
    }
    
    public static Pair<Map<String, Criterion>, Map<String, ResourceLocation>> readCriteria(FriendlyByteBuf buffer) {
        int size = buffer.readVarInt();
        Map<String, Criterion> criteria = new HashMap<>(size);
        Map<String, ResourceLocation> criteriaSerializers = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            String key = buffer.readUtf(32767);
            ResourceLocation serializerId = buffer.readResourceLocation();
            CriterionSerializer<?> serializer = JeaRegistries.CRITERION_SERIALIZER.getValue(serializerId);
            if (serializer == null) {
                throw new IllegalStateException("Server sent unknown advancement criterion instance. Can't deserialize: serializer unknown: '" + serializerId + "'");
            }
            CriterionTriggerInstance criterion = serializer.read(buffer);
            criteria.put(key, new Criterion(criterion));
            criteriaSerializers.put(key, serializerId);
        }
        return Pair.of(ImmutableMap.copyOf(criteria), ImmutableMap.copyOf(criteriaSerializers));
    }
    
    public static void writeCompletion(FriendlyByteBuf buffer, List<List<String>> completion) {
        buffer.writeVarInt(completion.size());
        for (List<String> entry : completion) {
            buffer.writeVarInt(entry.size());
            for (String value : entry) {
                buffer.writeUtf(value, 0x40000);
            }
        }
    }
    
    public static List<List<String>> readCompletion(FriendlyByteBuf buffer) {
        ImmutableList.Builder<List<String>> builder = new ImmutableList.Builder<>();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            int length = buffer.readVarInt();
            ImmutableList.Builder<String> sublist = new ImmutableList.Builder<>();
            for (int j = 0; j < length; j++) {
                sublist.add(buffer.readUtf(0x40000));
            }
            builder.add(sublist.build());
        }
        return builder.build();
    }
    
    public static void writeBlockPredicate(BlockPredicate predicate, FriendlyByteBuf buffer) {
        writeJSON(buffer, predicate.serializeToJson());
    }
    
    public static BlockPredicate readBlockPredicate(FriendlyByteBuf buffer) {
        return BlockPredicate.fromJson(readJSON(buffer));
    }

    public static void writeDamagePredicate(DamagePredicate predicate, FriendlyByteBuf buffer) {
        writeJSON(buffer, predicate.serializeToJson());
    }

    public static DamagePredicate readDamagePredicate(FriendlyByteBuf buffer) {
        return DamagePredicate.fromJson(readJSON(buffer));
    }
    
    public static void writeDamageSourcePredicate(DamageSourcePredicate predicate, FriendlyByteBuf buffer) {
        writeJSON(buffer, predicate.serializeToJson());
    }

    public static DamageSourcePredicate readDamageSourcePredicate(FriendlyByteBuf buffer) {
        return DamageSourcePredicate.fromJson(readJSON(buffer));
    }

    public static void writeDistancePredicate(DistancePredicate predicate, FriendlyByteBuf buffer) {
        writeJSON(buffer, predicate.serializeToJson());
    }

    public static DistancePredicate readDistancePredicate(FriendlyByteBuf buffer) {
        return DistancePredicate.fromJson(readJSON(buffer));
    }
    
    public static void writeEnchantmentPredicate(EnchantmentPredicate predicate, FriendlyByteBuf buffer) {
        writeJSON(buffer, predicate.serializeToJson());
    }

    public static EnchantmentPredicate readEnchantmentPredicate(FriendlyByteBuf buffer) {
        return EnchantmentPredicate.fromJson(readJSON(buffer));
    }
    
    public static void writeEntityEquipmentPredicate(EntityEquipmentPredicate predicate, FriendlyByteBuf buffer) {
        writeJSON(buffer, predicate.serializeToJson());
    }

    public static EntityEquipmentPredicate readEntityEquipmentPredicate(FriendlyByteBuf buffer) {
        return EntityEquipmentPredicate.fromJson(readJSON(buffer));
    }

    public static void writeEntityFlagsPredicate(EntityFlagsPredicate predicate, FriendlyByteBuf buffer) {
        writeJSON(buffer, predicate.serializeToJson());
    }

    public static EntityFlagsPredicate readEntityFlagsPredicate(FriendlyByteBuf buffer) {
        return EntityFlagsPredicate.fromJson(readJSON(buffer));
    }

    public static void writeEntityPredicate(EntityPredicate predicate, FriendlyByteBuf buffer) {
        writeJSON(buffer, predicate.serializeToJson());
    }

    public static EntityPredicate readEntityPredicate(FriendlyByteBuf buffer) {
        return EntityPredicate.fromJson(readJSON(buffer));
    }

    public static void writeEntityTypePredicate(EntityTypePredicate predicate, FriendlyByteBuf buffer) {
        writeJSON(buffer, predicate.serializeToJson());
    }

    public static EntityTypePredicate readEntityTypePredicate(FriendlyByteBuf buffer) {
        return EntityTypePredicate.fromJson(readJSON(buffer));
    }

    public static void writeFluidPredicate(FluidPredicate predicate, FriendlyByteBuf buffer) {
        writeJSON(buffer, predicate.serializeToJson());
    }

    public static FluidPredicate readFluidPredicate(FriendlyByteBuf buffer) {
        return FluidPredicate.fromJson(readJSON(buffer));
    }

    public static void writeLightPredicate(LightPredicate predicate, FriendlyByteBuf buffer) {
        writeJSON(buffer, predicate.serializeToJson());
    }

    public static LightPredicate readLightPredicate(FriendlyByteBuf buffer) {
        return LightPredicate.fromJson(readJSON(buffer));
    }
    
    public static void writeItemPredicate(ItemPredicate predicate, FriendlyByteBuf buffer) {
        writeJSON(buffer, predicate.serializeToJson());
    }

    public static ItemPredicate readItemPredicate(FriendlyByteBuf buffer) {
        return ItemPredicate.fromJson(readJSON(buffer));
    }

    public static void writeLocationPredicate(LocationPredicate predicate, FriendlyByteBuf buffer) {
        writeJSON(buffer, predicate.serializeToJson());
    }

    public static LocationPredicate readLocationPredicate(FriendlyByteBuf buffer) {
        return LocationPredicate.fromJson(readJSON(buffer));
    }

    public static void writeMobEffectsPredicate(MobEffectsPredicate predicate, FriendlyByteBuf buffer) {
        writeJSON(buffer, predicate.serializeToJson());
    }

    public static MobEffectsPredicate readMobEffectsPredicate(FriendlyByteBuf buffer) {
        return MobEffectsPredicate.fromJson(readJSON(buffer));
    }

    public static void writeNBTPredicate(NbtPredicate predicate, FriendlyByteBuf buffer) {
        writeJSON(buffer, predicate.serializeToJson());
    }

    public static NbtPredicate readNBTPredicate(FriendlyByteBuf buffer) {
        return NbtPredicate.fromJson(readJSON(buffer));
    }

    public static void writePlayerPredicate(PlayerPredicate predicate, FriendlyByteBuf buffer) {
        writeJSON(buffer, predicate.serializeToJson());
    }

    public static PlayerPredicate readPlayerPredicate(FriendlyByteBuf buffer) {
        return PlayerPredicate.fromJson(readJSON(buffer));
    }

    public static void writeStatePropertiesPredicate(StatePropertiesPredicate predicate, FriendlyByteBuf buffer) {
        writeJSON(buffer, predicate.serializeToJson());
    }

    public static StatePropertiesPredicate readStatePropertiesPredicate(FriendlyByteBuf buffer) {
        return StatePropertiesPredicate.fromJson(readJSON(buffer));
    }

    public static void writeIntRange(MinMaxBounds.Ints range, FriendlyByteBuf buffer) {
        byte mask = 0;
        if (range.getMin() != null) mask |= (1);
        if (range.getMax() != null) mask |= (1 << 1);
        buffer.writeByte(mask);
        if (range.getMin() != null) buffer.writeVarInt(range.getMin());
        if (range.getMax() != null) buffer.writeVarInt(range.getMax());
    }
    
    public static MinMaxBounds.Ints readIntRange(FriendlyByteBuf buffer) {
        byte mask = buffer.readByte();
        Integer min = null;
        Integer max = null;
        if ((mask & 1) != 0) {
            min = buffer.readVarInt();
        }
        if ((mask & (1 << 1)) != 0) {
            max = buffer.readVarInt();
        }
        return new MinMaxBounds.Ints(min, max);
    }
    
    public static void writeDoubleRange(MinMaxBounds.Doubles range, FriendlyByteBuf buffer) {
        byte mask = 0;
        if (range.getMin() != null) mask |= (1);
        if (range.getMax() != null) mask |= (1 << 1);
        buffer.writeByte(mask);
        if (range.getMin() != null) buffer.writeDouble(range.getMin());
        if (range.getMax() != null) buffer.writeDouble(range.getMax());
    }
    
    public static MinMaxBounds.Doubles readDoubleRange(FriendlyByteBuf buffer) {
        byte mask = buffer.readByte();
        Double min = null;
        Double max = null;
        if ((mask & 1) != 0) {
            min = buffer.readDouble();
        }
        if ((mask & (1 << 1)) != 0) {
            max = buffer.readDouble();
        }
        return new MinMaxBounds.Doubles(min, max);
    }
    
    public static void writeKey(ResourceKey<?> registryKey, FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(registryKey.getRegistryName());
        buffer.writeResourceLocation(registryKey.location());
    }
    
    public static <T> ResourceKey<T> readKey(FriendlyByteBuf buffer, @SuppressWarnings("unused") Class<T> clazz) {
        ResourceLocation registryId = buffer.readResourceLocation();
        ResourceKey<Registry<Object>> registry = ResourceKey.createRegistryKey(registryId);
        //noinspection unchecked
        return (ResourceKey<T>) ResourceKey.create(registry, buffer.readResourceLocation());
    }
    
    
}
