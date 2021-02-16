package de.melanx.jea.network;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import de.melanx.jea.api.CriterionSerializer;
import de.melanx.jea.api.JeaRegistries;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.criterion.*;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
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
    
    public static void writeBlockPredicate(BlockPredicate predicate, PacketBuffer buffer) {
        writeJSON(buffer, predicate.serialize());
    }
    
    public static BlockPredicate readBlockPredicate(PacketBuffer buffer) {
        return BlockPredicate.deserialize(readJSON(buffer));
    }

    public static void writeDamagePredicate(DamagePredicate predicate, PacketBuffer buffer) {
        writeJSON(buffer, predicate.serialize());
    }

    public static DamagePredicate readDamagePredicate(PacketBuffer buffer) {
        return DamagePredicate.deserialize(readJSON(buffer));
    }
    
    public static void writeDamageSourcePredicate(DamageSourcePredicate predicate, PacketBuffer buffer) {
        writeJSON(buffer, predicate.serialize());
    }

    public static DamageSourcePredicate readDamageSourcePredicate(PacketBuffer buffer) {
        return DamageSourcePredicate.deserialize(readJSON(buffer));
    }

    public static void writeDistancePredicate(DistancePredicate predicate, PacketBuffer buffer) {
        writeJSON(buffer, predicate.serialize());
    }

    public static DistancePredicate readDistancePredicate(PacketBuffer buffer) {
        return DistancePredicate.deserialize(readJSON(buffer));
    }
    
    public static void writeEnchantmentPredicate(EnchantmentPredicate predicate, PacketBuffer buffer) {
        writeJSON(buffer, predicate.serialize());
    }

    public static EnchantmentPredicate readEnchantmentPredicate(PacketBuffer buffer) {
        return EnchantmentPredicate.deserialize(readJSON(buffer));
    }
    
    public static void writeEntityEquipmentPredicate(EntityEquipmentPredicate predicate, PacketBuffer buffer) {
        writeJSON(buffer, predicate.serialize());
    }

    public static EntityEquipmentPredicate readEntityEquipmentPredicate(PacketBuffer buffer) {
        return EntityEquipmentPredicate.deserialize(readJSON(buffer));
    }

    public static void writeEntityFlagsPredicate(EntityFlagsPredicate predicate, PacketBuffer buffer) {
        writeJSON(buffer, predicate.serialize());
    }

    public static EntityFlagsPredicate readEntityFlagsPredicate(PacketBuffer buffer) {
        return EntityFlagsPredicate.deserialize(readJSON(buffer));
    }

    public static void writeEntityPredicate(EntityPredicate predicate, PacketBuffer buffer) {
        writeJSON(buffer, predicate.serialize());
    }

    public static EntityPredicate readEntityPredicate(PacketBuffer buffer) {
        return EntityPredicate.deserialize(readJSON(buffer));
    }

    public static void writeEntityTypePredicate(EntityTypePredicate predicate, PacketBuffer buffer) {
        writeJSON(buffer, predicate.serialize());
    }

    public static EntityTypePredicate readEntityTypePredicate(PacketBuffer buffer) {
        return EntityTypePredicate.deserialize(readJSON(buffer));
    }

    public static void writeFluidPredicate(FluidPredicate predicate, PacketBuffer buffer) {
        writeJSON(buffer, predicate.serialize());
    }

    public static FluidPredicate readFluidPredicate(PacketBuffer buffer) {
        return FluidPredicate.deserialize(readJSON(buffer));
    }

    public static void writeLightPredicate(LightPredicate predicate, PacketBuffer buffer) {
        writeJSON(buffer, predicate.serialize());
    }

    public static LightPredicate readLightPredicate(PacketBuffer buffer) {
        return LightPredicate.deserialize(readJSON(buffer));
    }
    
    public static void writeItemPredicate(ItemPredicate predicate, PacketBuffer buffer) {
        writeJSON(buffer, predicate.serialize());
    }

    public static ItemPredicate readItemPredicate(PacketBuffer buffer) {
        return ItemPredicate.deserialize(readJSON(buffer));
    }

    public static void writeLocationPredicate(LocationPredicate predicate, PacketBuffer buffer) {
        writeJSON(buffer, predicate.serialize());
    }

    public static LocationPredicate readLocationPredicate(PacketBuffer buffer) {
        return LocationPredicate.deserialize(readJSON(buffer));
    }

    public static void writeMobEffectsPredicate(MobEffectsPredicate predicate, PacketBuffer buffer) {
        writeJSON(buffer, predicate.serialize());
    }

    public static MobEffectsPredicate readMobEffectsPredicate(PacketBuffer buffer) {
        return MobEffectsPredicate.deserialize(readJSON(buffer));
    }

    public static void writeNBTPredicate(NBTPredicate predicate, PacketBuffer buffer) {
        writeJSON(buffer, predicate.serialize());
    }

    public static NBTPredicate readNBTPredicate(PacketBuffer buffer) {
        return NBTPredicate.deserialize(readJSON(buffer));
    }

    public static void writePlayerPredicate(PlayerPredicate predicate, PacketBuffer buffer) {
        writeJSON(buffer, predicate.serialize());
    }

    public static PlayerPredicate readPlayerPredicate(PacketBuffer buffer) {
        return PlayerPredicate.deserialize(readJSON(buffer));
    }

    public static void writeStatePropertiesPredicate(StatePropertiesPredicate predicate, PacketBuffer buffer) {
        writeJSON(buffer, predicate.toJsonElement());
    }

    public static StatePropertiesPredicate readStatePropertiesPredicate(PacketBuffer buffer) {
        return StatePropertiesPredicate.deserializeProperties(readJSON(buffer));
    }

    public static void writeIntRange(MinMaxBounds.IntBound range, PacketBuffer buffer) {
        byte mask = 0;
        if (range.getMin() != null) mask |= (1);
        if (range.getMax() != null) mask |= (1 << 1);
        buffer.writeByte(mask);
        if (range.getMin() != null) buffer.writeVarInt(range.getMin());
        if (range.getMax() != null) buffer.writeVarInt(range.getMax());
    }
    
    public static MinMaxBounds.IntBound readIntRange(PacketBuffer buffer) {
        byte mask = buffer.readByte();
        Integer min = null;
        Integer max = null;
        if ((mask & 1) != 0) {
            min = buffer.readVarInt();
        }
        if ((mask & (1 << 1)) != 0) {
            max = buffer.readVarInt();
        }
        return new MinMaxBounds.IntBound(min, max);
    }
    
    public static void writeFloatRange(MinMaxBounds.FloatBound range, PacketBuffer buffer) {
        byte mask = 0;
        if (range.getMin() != null) mask |= (1);
        if (range.getMax() != null) mask |= (1 << 1);
        buffer.writeByte(mask);
        if (range.getMin() != null) buffer.writeFloat(range.getMin());
        if (range.getMax() != null) buffer.writeFloat(range.getMax());
    }
    
    public static MinMaxBounds.FloatBound readFloatRange(PacketBuffer buffer) {
        byte mask = buffer.readByte();
        Float min = null;
        Float max = null;
        if ((mask & 1) != 0) {
            min = buffer.readFloat();
        }
        if ((mask & (1 << 1)) != 0) {
            max = buffer.readFloat();
        }
        return new MinMaxBounds.FloatBound(min, max);
    }
    
    public static void writeKey(RegistryKey<?> registryKey, PacketBuffer buffer) {
        buffer.writeResourceLocation(registryKey.getRegistryName());
        buffer.writeResourceLocation(registryKey.getLocation());
    }
    
    public static <T> RegistryKey<T> readKey(PacketBuffer buffer, @SuppressWarnings("unused") Class<T> clazz) {
        ResourceLocation registryId = buffer.readResourceLocation();
        RegistryKey<Registry<Object>> registry = RegistryKey.getOrCreateRootKey(registryId);
        //noinspection unchecked
        return (RegistryKey<T>) RegistryKey.getOrCreateKey(registry, buffer.readResourceLocation());
        
    }
}
