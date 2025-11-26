package net.snowteb.warriorcats_events.integration;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import tocraft.walkers.api.variant.TypeProvider;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;

public class WCatTypeProvider extends TypeProvider<WCatEntity> {

    @Override
    public int getVariantData(WCatEntity wCatEntity) {
        return wCatEntity.getVariant();
    }

    @Override
    public WCatEntity create(EntityType<WCatEntity> type, Level level, int data) {
        WCatEntity cat = new WCatEntity(type, level);
        cat.setVariant(data);
        return cat;
    }

    @Override
    public int getFallbackData() {
        return 0;
    }

    @Override
    public int getRange() {
        return 16;
    }

    @Override
    public Component modifyText(WCatEntity wCatEntity, MutableComponent mutableComponent) {
        return mutableComponent.append("Variant " + wCatEntity.getVariant());
    }

    
}
