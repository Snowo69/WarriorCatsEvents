package net.snowteb.warriorcats_events.integration;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.core.jmx.Server;
import tocraft.walkers.api.PlayerShape;
import tocraft.walkers.api.PlayerShapeChanger;
import tocraft.walkers.api.variant.TypeProvider;
import net.snowteb.warriorcats_events.entity.custom.WCatEntity;

/**
 * Provides all the available variants of the wild cat.
 */
public class WCatTypeProvider extends TypeProvider<WCatEntity> {

    @Override
    public int getVariantData(WCatEntity wCatEntity) {
        return wCatEntity.getVariant();
    }

    @Override
    public WCatEntity create(EntityType<WCatEntity> type, Level level, int data) {
        WCatEntity cat = new WCatEntity(type, level);
        cat.setVariant(data);
//        Player player = Minecraft.getInstance().player;
//        cat.setBaby(true);
//        cat.setCustomName(Component.empty().append(Minecraft.getInstance().player.getName()).withStyle(ChatFormatting.GRAY));
//        cat.setCustomNameVisible(true);
//        cat.setAge(-113);
//        cat.setAppScale(true);
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
    public Component modifyText(WCatEntity wCatEntity, MutableComponent mutableComponent) {
        return mutableComponent.append("Variant " + wCatEntity.getVariant());
    }

    
}
