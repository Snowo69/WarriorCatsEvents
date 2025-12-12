package net.snowteb.warriorcats_events.integration;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
//import net.snowteb.warriorcats_events.entity.custom.VanillaWCatEntity;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;
import tocraft.walkers.api.variant.TypeProvider;
/*
public class VanillaWCatTypeProvider extends TypeProvider<VanillaWCatEntity> {

    @Override
    public int getVariantData(VanillaWCatEntity VanillaWCatEntity) {
        return VanillaWCatEntity.getVariant();
    }

    @Override
    public VanillaWCatEntity create(EntityType<VanillaWCatEntity> type, Level level, int data) {
        VanillaWCatEntity cat = new VanillaWCatEntity(type, level);
        cat.setVariant(data);
        return cat;
    }

    @Override
    public int getFallbackData() {
        return 0;
    }

    @Override
    public int getRange() {
        return 19;
    }

    @Override
    public Component modifyText(VanillaWCatEntity VanillaWCatEntity, MutableComponent mutableComponent) {
        return mutableComponent.append("Variant " + VanillaWCatEntity.getVariant());
    }

    
}


 */