package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.component.CustomData;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.entity.CraftTropicalFish;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.meta.TropicalFishBucketMeta;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaTropicalFishBucket extends CraftMetaItem implements TropicalFishBucketMeta {
    static final ItemMetaKey VARIANT = new ItemMetaKey("BucketVariantTag", "fish-variant");
    static final ItemMetaKeyType<CustomData> ENTITY_TAG = new ItemMetaKeyType<>(DataComponents.ENTITY_DATA, "entity-tag");

    private Integer variant;
    private CompoundTag entityTag;

    CraftMetaTropicalFishBucket(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaTropicalFishBucket)) {
            return;
        }

        CraftMetaTropicalFishBucket bucket = (CraftMetaTropicalFishBucket) meta;
        this.variant = bucket.variant;
        this.entityTag = bucket.entityTag;
    }

    CraftMetaTropicalFishBucket(DataComponentPatch tag) {
        super(tag);

        getOrEmpty(tag, CraftMetaTropicalFishBucket.ENTITY_TAG).ifPresent((nbt) -> {
            this.entityTag = nbt.copyTag();

            if (this.entityTag.contains(CraftMetaTropicalFishBucket.VARIANT.NBT, CraftMagicNumbers.NBT.TAG_INT)) {
                this.variant = this.entityTag.getInt(CraftMetaTropicalFishBucket.VARIANT.NBT);
            }
        });
    }

    CraftMetaTropicalFishBucket(Map<String, Object> map) {
        super(map);

        Integer variant = SerializableMeta.getObject(Integer.class, map, CraftMetaTropicalFishBucket.VARIANT.BUKKIT, true);
        if (variant != null) {
            this.variant = variant;
        }
    }

    @Override
    void deserializeInternal(CompoundTag tag, Object context) {
        super.deserializeInternal(tag, context);

        if (tag.contains(CraftMetaTropicalFishBucket.ENTITY_TAG.NBT)) {
            this.entityTag = tag.getCompound(CraftMetaTropicalFishBucket.ENTITY_TAG.NBT);
        }
    }

    @Override
    void serializeInternal(Map<String, Tag> internalTags) {
        if (this.entityTag != null && !this.entityTag.isEmpty()) {
            internalTags.put(CraftMetaTropicalFishBucket.ENTITY_TAG.NBT, this.entityTag);
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.hasVariant()) {
            if (this.entityTag == null) {
                this.entityTag = new CompoundTag();
            }
            this.entityTag.putInt(CraftMetaTropicalFishBucket.VARIANT.NBT, this.variant);
        }

        if (this.entityTag != null) {
            tag.put(CraftMetaTropicalFishBucket.ENTITY_TAG, CustomData.of(this.entityTag));
        }
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.TROPICAL_FISH_BUCKET;
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isBucketEmpty();
    }

    boolean isBucketEmpty() {
        return !(this.hasVariant() || this.entityTag != null);
    }

    @Override
    public DyeColor getPatternColor() {
        return CraftTropicalFish.getPatternColor(this.variant);
    }

    @Override
    public void setPatternColor(DyeColor color) {
        if (this.variant == null) {
            this.variant = 0;
        }
        this.variant = CraftTropicalFish.getData(color, this.getPatternColor(), this.getPattern());
    }

    @Override
    public DyeColor getBodyColor() {
        return CraftTropicalFish.getBodyColor(this.variant);
    }

    @Override
    public void setBodyColor(DyeColor color) {
        if (this.variant == null) {
            this.variant = 0;
        }
        this.variant = CraftTropicalFish.getData(this.getPatternColor(), color, this.getPattern());
    }

    @Override
    public TropicalFish.Pattern getPattern() {
        return CraftTropicalFish.getPattern(this.variant);
    }

    @Override
    public void setPattern(TropicalFish.Pattern pattern) {
        if (this.variant == null) {
            this.variant = 0;
        }
        this.variant = CraftTropicalFish.getData(this.getPatternColor(), this.getBodyColor(), pattern);
    }

    @Override
    public boolean hasVariant() {
        return this.variant != null;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaTropicalFishBucket) {
            CraftMetaTropicalFishBucket that = (CraftMetaTropicalFishBucket) meta;

            return (this.hasVariant() ? that.hasVariant() && this.variant.equals(that.variant) : !that.hasVariant())
                    && (this.entityTag != null ? that.entityTag != null && this.entityTag.equals(that.entityTag) : that.entityTag == null);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaTropicalFishBucket || this.isBucketEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (this.hasVariant()) {
            hash = 61 * hash + this.variant;
        }
        if (this.entityTag != null) {
            hash = 61 * hash + this.entityTag.hashCode();
        }

        return original != hash ? CraftMetaTropicalFishBucket.class.hashCode() ^ hash : hash;
    }


    @Override
    public CraftMetaTropicalFishBucket clone() {
        CraftMetaTropicalFishBucket clone = (CraftMetaTropicalFishBucket) super.clone();

        if (this.entityTag != null) {
            clone.entityTag = this.entityTag.copy();
        }

        return clone;
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        if (this.hasVariant()) {
            builder.put(CraftMetaTropicalFishBucket.VARIANT.BUKKIT, this.variant);
        }

        return builder;
    }
}
