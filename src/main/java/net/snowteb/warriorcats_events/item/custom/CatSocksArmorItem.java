package net.snowteb.warriorcats_events.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class CatSocksArmorItem extends ArmorItem {

    private static final UUID ARMOR_UUID =
            UUID.fromString("a1b2c3d4-aaaa-cbc3-edbe-1a1b2c1144a6");

    public CatSocksArmorItem() {
        super(ArmorMaterials.IRON, Type.BOOTS, new Properties().stacksTo(1).durability(120));
    }


    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.FEET;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.FEET) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder =
                    ImmutableMultimap.builder();

            builder.put(
                    Attributes.ARMOR,
                    new AttributeModifier(ARMOR_UUID, "socks_armor_attribute", 2.0, AttributeModifier.Operation.ADDITION)
            );

            return builder.build();
        }



        return super.getDefaultAttributeModifiers(slot);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        EquipmentSlot slot = this.getEquipmentSlot();

        ItemStack equipped = player.getItemBySlot(slot);

        if (equipped.isEmpty()) {
            player.setItemSlot(slot, stack.copy());
            stack.shrink(1);
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        return InteractionResultHolder.fail(stack);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "warriorcats_events:textures/empty.png";
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }



}
