package net.snowteb.warriorcats_events.network.packet;

import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.item.ModItems;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

public class CtSTeleportToLocationPacket {

    private final int data;

    public CtSTeleportToLocationPacket(int data) {
        this.data = data;
    }

    public static CtSTeleportToLocationPacket decode(FriendlyByteBuf buf) {
        return new CtSTeleportToLocationPacket(buf.readInt());
    }


    public static void encode(CtSTeleportToLocationPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.data);
    }

    public static void handle(CtSTeleportToLocationPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            ServerLevel level = (ServerLevel) player.level();
            BlockPos destination = player.blockPosition();
            int type = packet.data;


            if (type == 32) {
                destination = player.blockPosition();
            }
            if (type == 0){
                destination = player.blockPosition();
            }
            if (type == 1){
                destination = locateBiome(level,
                        level.registryAccess().registryOrThrow(Registries.BIOME).getHolderOrThrow(Biomes.TAIGA),
                        player.blockPosition()
                );
            }
            if (type == 2){
                destination = locateBiome(level,
                        level.registryAccess().registryOrThrow(Registries.BIOME).getHolderOrThrow(Biomes.PLAINS),
                        player.blockPosition()
                );
            }
            if (type == 3){
                destination = locateBiome(level,
                        level.registryAccess().registryOrThrow(Registries.BIOME).getHolderOrThrow(Biomes.SAVANNA),
                        player.blockPosition()
                );
            }
            if (type == 4){
                destination = locateBiome(level,
                        level.registryAccess().registryOrThrow(Registries.BIOME).getHolderOrThrow(Biomes.FOREST),
                        player.blockPosition()
                );
            }
            if (type == 5) {
                destination = locateStructure(
                        level,
                        getStructure(level, "minecraft:village_taiga"),
                        player.blockPosition()
                );
            }
            if (type == 6){
                destination = locateStructure(
                        level,
                        getStructure(level, "minecraft:village_plains"),
                        player.blockPosition()
                );
            }
            if (type == 7){
                destination = locateStructure(
                        level,
                        getStructure(level, "minecraft:village_savanna"),
                        player.blockPosition()
                );
            }
            if (type == 8){
                destination = locateStructure(
                        level,
                        getStructure(level, "minecraft:jungle_pyramid"),
                        player.blockPosition()
                );
            }

            if (destination != null) {
                if (type != 32) {
                    player.sendSystemMessage(Component.literal("Teleporting...").withStyle(ChatFormatting.DARK_GRAY));
                    teleportPlayer(player, destination);
                    player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 300));

                }
            } else {
                player.sendSystemMessage(Component.literal("Couldn't find a nearby appropiate location.").withStyle(ChatFormatting.RED));
            }

            CompoundTag persistent;
            if (player.getPersistentData().contains(Player.PERSISTED_NBT_TAG)) {
                persistent = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
            } else {
                persistent = new CompoundTag();
                player.getPersistentData().put(Player.PERSISTED_NBT_TAG, persistent);
            }

            if (!persistent.getBoolean("warriorcats_events.starting_items")) {
                player.getInventory().add(new ItemStack(ModItems.WARRIORS_GUIDE.get()));
                player.getInventory().add(new ItemStack(ModItems.CLAWS.get()));
                player.sendSystemMessage(Component.literal("You have received your own [Claws] and [A Warrior's Guide]!").withStyle(ChatFormatting.YELLOW));
                player.sendSystemMessage(Component.literal("Get support and stay tuned for mod updates: ").append(
                        Component.literal("[Discord]")
                                .withStyle(style -> style
                                        .withColor(0x579dff)
                                        .withUnderlined(true)
                                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/SkYvZr9DBb"))
                                )
                ));
                persistent.putBoolean("warriorcats_events.starting_items", true);
            }

            MinecraftServer server = level.getServer();
            int delay = 140;

            server.tell(new TickTask(
                    server.getTickCount() + delay,
                    () -> {
                        level.getChunkAt(player.blockPosition());
                        level.playSound(null, player.blockPosition() ,SoundEvents.CAT_STRAY_AMBIENT, SoundSource.MASTER);

                        level.sendParticles(
                                ParticleTypes.CLOUD,
                                player.getX(),
                                player.getY() - 6,
                                player.getZ(),
                                1200,
                                0.1, 1.5, 0.1,
                                0.2
                        );
                        level.sendParticles(
                                ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                                player.getX(),
                                player.getY() - 6,
                                player.getZ(),
                                1000,
                                0.3, 1.5, 0.3,
                                0.2
                        );
                    }
            ));





        });

        ctx.get().setPacketHandled(true);
    }

    public static Holder<Structure> getStructure(ServerLevel level, String id) {
        return level.registryAccess()
                .registryOrThrow(Registries.STRUCTURE)
                .getHolderOrThrow(
                        ResourceKey.create(
                                Registries.STRUCTURE, new ResourceLocation(id)
                        )
                );
    }

    @Nullable
    public static BlockPos locateBiome(ServerLevel level, Holder<Biome> biome, BlockPos origin) {
        Pair<BlockPos, Holder<Biome>> pair =
                level.findClosestBiome3d(
                        b -> b == biome,
                        origin,
                        6400,
                        32,
                        64
                );

        return pair != null ? pair.getFirst() : null;
    }

    @Nullable
    public static BlockPos locateStructure(ServerLevel level, Holder<Structure> structure, BlockPos origin) {
        Pair<BlockPos, Holder<Structure>> pair =
                level.getChunkSource()
                        .getGenerator()
                        .findNearestMapStructure(
                                level,
                                HolderSet.direct(structure),
                                origin,
                                100,
                                false
                        );

        return pair != null ? pair.getFirst() : null;
    }



    public static void teleportPlayer(ServerPlayer player, BlockPos pos) {
        player.teleportTo(
                player.serverLevel(),
                pos.getX() + 0.5,
                player.getY() + 45,
                pos.getZ() + 0.5,
                player.getYRot(),
                player.getXRot()
        );
    }

}
