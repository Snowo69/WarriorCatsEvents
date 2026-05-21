package net.snowteb.warriorcats_events.diseases;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.network.ModPackets;
import net.snowteb.warriorcats_events.network.packet.s2c.others.SyncDiseasesPacket;
import net.snowteb.warriorcats_events.zconfig.WCEServerConfig;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Disease System
 * <p>
 * The interface leads the NBT. The NBT methods must be put into the Saved data methods of the entity.
 * getList is overriden, the variable must be added into the entity.
 * <p>
 * diseaseTick(), addDisease, etc, all stay the same.
 */
public interface Diseaseable<T extends LivingEntity> {

    default T getEntity() {
        return (T) this;
    }

    default void writeDiseasesNBT(CompoundTag tag) {

        ListTag diseaseList = new ListTag();
        for (Disease<?> disease : getList()) {
            CompoundTag diseaseTag = new CompoundTag();

            disease.saveNBT(diseaseTag);

            diseaseList.add(diseaseTag);
        }
        tag.put("Diseases", diseaseList);
    }

    default void loadDiseasesNBT(CompoundTag tag) {
        getList().clear();

        if (!tag.contains("Diseases")) return;

        ListTag diseaseList = tag.getList("Diseases", Tag.TAG_COMPOUND);

        for (int i = 0; i < diseaseList.size(); i++) {

            CompoundTag diseaseTag = diseaseList.getCompound(i);

            Disease<?> disease = reCreateDiseaseNBT(diseaseTag);
            if (disease == null) continue;

            disease.loadNBT(diseaseTag);

            getList().add(disease);
        }
    }

    private static @Nullable Disease<?> reCreateDiseaseNBT(CompoundTag diseaseTag) {
        String diseaseID = diseaseTag.getString("DiseaseID");
        DiseaseType<? extends Disease<?>> type = DiseaseRegistry.getByID(diseaseID);
        if (type == null) return null;
        return type.create();
    }

    default CompoundTag diseaseData() {
        CompoundTag tag = new CompoundTag();
        writeDiseasesNBT(tag);

        return tag;
    }


    default boolean hasDisease(DiseaseType<? extends Disease<?>> type) {
        if (!WCEServerConfig.SERVER.DISEASES.get()) return false;

        return getList().stream().anyMatch(disease -> disease.getType() == type);
    }

    List<Disease<?>> getList();

    default void diseaseTick() {
        if (!WCEServerConfig.SERVER.DISEASES.get()) return;

        Iterator<Disease<?>> iterator = getList().iterator();
        while (iterator.hasNext()) {
            Disease<?> disease = iterator.next();
            disease.tick(((LivingEntity) this));
            disease.defaultTickEvent(this, false);
            if (disease.shouldRemove()) {
                iterator.remove();
                onChange();
            }
        }

        if (!getEntity().level().isClientSide()) {
            List<DiseaseManager.DiseaseInstance> toAdd = new ArrayList<>(DiseaseManager.pendingDiseases(getEntity()));
            if (!toAdd.isEmpty()) {
                for (DiseaseManager.DiseaseInstance instance : toAdd) {
                    addDiseaseAndSync(instance.disease(), instance.organic());
                    DiseaseManager.pendingDiseases(getEntity()).remove(instance);
                }
                if (DiseaseManager.pendingDiseases(getEntity()).isEmpty()) {
                    DiseaseManager.clearPendingDiseases(getEntity());
                }
            }
        }

        DiseaseManager.tick(this);
    }

    /**
     * The method used for adding diseases to a Diseaseable entity
     * @param type The DiseaseType to add.
     * @param organic True when the disease is caused naturally. False if it's added through commands or such.
     * @return True when the disease can be applied.
     */
    default boolean addDisease(DiseaseType<? extends Disease<?>> type, boolean organic) {
        if (!WCEServerConfig.SERVER.DISEASES.get()) return false;
        if (hasDisease(type)) return false;
        Disease<?> newDisease = type.create();
        if (!newDisease.canAdd(this)) return false;

        return addDisease(newDisease, organic);
    }

    default boolean addDisease(Disease<?> newDisease, boolean organic) {
        DiseaseManager.diseaseAdd(getEntity(), newDisease, organic);
        return true;
    }

    default void addDiseaseAndSync(Disease<?> disease, boolean organic) {
        getList().add(disease);
        disease.onAdd(this, organic);
        onChange();
    }

    default void remove(DiseaseType<? extends Disease<?>> type) {
        Disease<?> disease = getDisease(type);
        if (disease == null) return;

        disease.setForRemove();
    }

    @ApiStatus.Internal
    default boolean removeDisease(DiseaseType<? extends Disease<?>> type) {
        Disease<?> disease = getDisease(type);
        if (disease == null) return false;
        disease.onRemove(this);

        getList().removeIf(d -> d.getType() == type);

        onChange();

        return true;
    }

    @Nullable
    default Disease<?> getDisease(DiseaseType<? extends Disease<?>> type) {
        return getList().stream()
                .filter(disease -> disease.getType() == type)
                .findFirst()
                .orElse(null);
    }

    default void onChange() {
        if (!WCEServerConfig.SERVER.DISEASES.get()) getList().clear();

        if (getEntity() instanceof ServerPlayer player) {
            if (player instanceof Diseaseable<?> diseaseable) {
                int id = player.getId();
                CompoundTag tag = diseaseable.diseaseData();

                ModPackets.sendToPlayer(new SyncDiseasesPacket(id, tag), player);
            }
        }
    }


    default boolean canClimb() {
        if (!WCEServerConfig.SERVER.DISEASES.get()) return true;

        for (Disease<?> disease : getList()) {
            if (!disease.allowClimb(this)) {
                return false;
            }
        }
        return true;
    }

    default boolean canLeap() {
        if (!WCEServerConfig.SERVER.DISEASES.get()) return true;

        for (Disease<?> disease : getList()) {
            if (!disease.allowLeap(this)) {
                return false;
            }
        }
        return true;
    }

    default boolean allowReducedFallDamage() {
        if (!WCEServerConfig.SERVER.DISEASES.get()) return true;

        for (Disease<?> disease : getList()) {
            if (!disease.allowReducedFallDamage(this)) {
                return false;
            }
        }
        return true;
    }

    default void tryHurt(DamageSource damageSource, float amount, float minHealth) {
        LivingEntity livingEntity = getEntity();
        if (livingEntity.getHealth() - minHealth > amount) {
            getEntity().hurt(damageSource, amount);
        }
    }
}
