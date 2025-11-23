package net.snowteb.warriorcats_events.item.custom;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ClawsTooltip extends ShearsItem {
    private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID ATTACK_SPEED_UUID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private final String tooltipKey;

    public ClawsTooltip(Properties properties, String tooltipKey) {
        super(properties);
        this.tooltipKey = tooltipKey;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();

        if (slot == EquipmentSlot.MAINHAND) {
            modifiers.put(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(ATTACK_DAMAGE_UUID, "Weapon modifier",
                            2.0, AttributeModifier.Operation.ADDITION));
            modifiers.put(Attributes.ATTACK_SPEED,
                    new AttributeModifier(ATTACK_SPEED_UUID, "Weapon speed",
                            1, AttributeModifier.Operation.ADDITION));
        }

        return modifiers;
    }





    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        String raw = (Component.translatable(tooltipKey).getString());

        String[] lines = raw.split("\\\\n");

        for (String line : lines) {
            tooltip.add(Component.literal(line).withStyle(ChatFormatting.GRAY));
        }

    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return 1.0f;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState state) {
        return false;
    }

}

