package net.snowteb.warriorcats_events.network.packet.c2s.clan;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.network.NetworkEvent;
import net.snowteb.warriorcats_events.block.ModBlocks;
import net.snowteb.warriorcats_events.block.custom.TreeStumpBlock;
import net.snowteb.warriorcats_events.block.entity.TreeStumpBlockEntity;
import net.snowteb.warriorcats_events.clan.ClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanData;
import net.snowteb.warriorcats_events.clan.PlayerClanDataProvider;
import net.snowteb.warriorcats_events.zconfig.WCEServerConfig;

import java.util.UUID;
import java.util.function.Supplier;

public class CtSClaimTerritory {
    private final String name;

    public CtSClaimTerritory(String name) {
        this.name = name;
    }

    public static CtSClaimTerritory decode(FriendlyByteBuf buf) {
        String name = buf.readUtf();
        return new CtSClaimTerritory(name);
    }

    public static void encode(CtSClaimTerritory packet, FriendlyByteBuf buf) {
        buf.writeUtf(packet.name);
    }

    public static void handle(CtSClaimTerritory packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            ServerLevel overworldLevel = player.getServer().overworld();

            UUID uuid = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                    .map(PlayerClanData::getCurrentClanUUID).orElse(ClanData.EMPTY_UUID);

            String morphName = player.getCapability(PlayerClanDataProvider.PLAYER_CLAN_DATA)
                    .map(PlayerClanData::getMorphName).orElse(player.getGameProfile().getName());

            if (player.level().dimension() != Level.OVERWORLD) {
                player.sendSystemMessage(Component.literal("Territory not available in other dimensions.").withStyle(ChatFormatting.RED));
                return;
            }

            if (uuid.equals(ClanData.EMPTY_UUID)) {
                player.sendSystemMessage(Component.literal("You are not in a clan.").withStyle(ChatFormatting.RED));
                return;
            }

            ClanData data = ClanData.get(overworldLevel);
            ClanData.Clan clan = data.getClan(uuid);
            if (clan == null) {
                player.sendSystemMessage(Component.literal("Invalid clan provided.").withStyle(ChatFormatting.RED));
                return;
            }

            ChunkPos currentPosition = player.chunkPosition();

            if (!data.canManage(clan, player.getUUID())) {
                player.sendSystemMessage(Component.literal("You don't have permission to claim territory.").withStyle(ChatFormatting.RED));
                return;
            }

            if (clan.claimedTerritory.containsKey(currentPosition)) {
                player.sendSystemMessage(Component.literal("Territory already claimed. Try renewing scent markers instead.").withStyle(ChatFormatting.RED));
                return;
            }

            if (!overworldLevel.getBlockState(player.blockPosition()).isAir() || !overworldLevel.getBlockState(player.blockPosition().below()).isSolid()) {
                player.sendSystemMessage(Component.literal("Your position is not an empty block or is invalid.").withStyle(ChatFormatting.RED));
                return;
            }

            if (clan.claimedTerritory.size() >= 169) {
                player.sendSystemMessage(Component.literal("Max territory reached. Try unclaiming some chunks to claim new chunks.").withStyle(ChatFormatting.RED));
                return;
            }

            boolean handled = false;

            if (clan.claimedTerritory.isEmpty()) {

                if (!packet.takeXPfromPlayer(player, 300)) return;

                Component log = Component.empty()
                                .append(Component.literal(morphName).withStyle(ChatFormatting.GOLD))
                        .append(Component.literal(" [").withStyle(ChatFormatting.DARK_GRAY))
                        .append(Component.literal(player.getName().getString()).withStyle(ChatFormatting.DARK_GRAY))
                        .append(Component.literal("]").withStyle(ChatFormatting.DARK_GRAY))
                        .append(" has set the core territory ")
                        .append(Component.literal(packet.name).withStyle(ChatFormatting.LIGHT_PURPLE))
                        .append(" at: ")
                        .append(Component.literal(
                                String.format("X=%d, Z=%d", currentPosition.x, currentPosition.z)
                        ).withStyle(ChatFormatting.AQUA));


                boolean success = data.claimChunk(clan.clanUUID, currentPosition, packet.name);
                if (success) {
                    data.registerLog(overworldLevel, clan.clanUUID, log);
                    clan.coreTerritory = currentPosition;
                    player.sendSystemMessage(Component.literal("Core territory successfully claimed."));
                    handled = true;
                } else {
                    player.sendSystemMessage(Component.literal("This chunk is claimed by another clan.").withStyle(ChatFormatting.RED));
                }

            } else {

                if (!packet.takeXPfromPlayer(player, 300)) return;

                boolean isTerritoryConnected = false;

                ChunkPos[] neighbors = new ChunkPos[] {
                        new ChunkPos(currentPosition.x + 1, currentPosition.z),
                        new ChunkPos(currentPosition.x - 1, currentPosition.z),
                        new ChunkPos(currentPosition.x, currentPosition.z + 1),
                        new ChunkPos(currentPosition.x, currentPosition.z - 1)
                };

                for (ChunkPos pos : neighbors) {
                    if (clan.claimedTerritory.containsKey(pos)) {
                        isTerritoryConnected = true;
                        break;
                    }
                }

                if (isTerritoryConnected) {

                    Component log = Component.empty()
                            .append(Component.literal(morphName).withStyle(ChatFormatting.GOLD))
                            .append(Component.literal(" [").withStyle(ChatFormatting.DARK_GRAY))
                            .append(Component.literal(player.getName().getString()).withStyle(ChatFormatting.DARK_GRAY))
                            .append(Component.literal("]").withStyle(ChatFormatting.DARK_GRAY))
                            .append(" has claimed new territory ")
                            .append(Component.literal(packet.name).withStyle(ChatFormatting.LIGHT_PURPLE))
                            .append(" at: ")
                            .append(Component.literal(
                                    String.format("X=%d, Z=%d", currentPosition.x, currentPosition.z)
                            ).withStyle(ChatFormatting.AQUA));


                    boolean result = data.claimChunk(clan.clanUUID, currentPosition, packet.name);
                    if (result) {
                        data.registerLog(overworldLevel, clan.clanUUID, log);
                        player.sendSystemMessage(Component.literal("New territory successfully claimed."));
                        handled = true;
                    } else {
                        player.sendSystemMessage(Component.literal("This chunk is claimed by another clan.").withStyle(ChatFormatting.RED));
                    }

                } else {
                    player.sendSystemMessage(Component.literal("You can't claim territory not connected to the core.").withStyle(ChatFormatting.RED));
                }
            }

            if (handled) {
                data.syncTerritoriesToClients(overworldLevel);

                data.removeMarkersFromChunk(overworldLevel,  currentPosition);

                Direction facing = player.getDirection().getOpposite();

                TreeStumpBlock.StumpVariant[] variants = TreeStumpBlock.StumpVariant.values();

                TreeStumpBlock.StumpVariant randomVariant = variants[overworldLevel.getRandom().nextInt(variants.length)];

                Holder<Biome> biome = overworldLevel.getBiome(player.blockPosition());
                if (biome.is(BiomeTags.IS_FOREST)) {
                    if (overworldLevel.getRandom().nextBoolean()) {
                        randomVariant = TreeStumpBlock.StumpVariant.OAK;
                    } else {
                        randomVariant = TreeStumpBlock.StumpVariant.BIRCH;
                    }
                } else if (biome.is(BiomeTags.IS_TAIGA)) {
                    randomVariant = TreeStumpBlock.StumpVariant.SPRUCE;
                } else if (biome.is(Biomes.CHERRY_GROVE)) {
                    randomVariant = TreeStumpBlock.StumpVariant.CHERRY;
                } else if (biome.is(Biomes.JUNGLE)) {
                    randomVariant = TreeStumpBlock.StumpVariant.JUNGLE;
                } else if (biome.is(Biomes.SAVANNA)) {
                    randomVariant = TreeStumpBlock.StumpVariant.ACACIA;
                } else if (biome.is(Biomes.DARK_FOREST)) {
                    randomVariant = TreeStumpBlock.StumpVariant.DARK_OAK;
                }

                BlockState blockState = ModBlocks.TREE_STUMP.get().defaultBlockState()
                        .setValue(HorizontalDirectionalBlock.FACING, facing)
                        .setValue(TreeStumpBlock.VARIANT, randomVariant);

                overworldLevel.setBlock(player.blockPosition(),  blockState, 3);


                BlockState placedBlock = overworldLevel.getBlockState(player.blockPosition());

                if (placedBlock.getBlock() instanceof TreeStumpBlock) {
                    BlockEntity entity = overworldLevel.getBlockEntity(player.blockPosition());
                    if (entity instanceof TreeStumpBlockEntity tree) {
                        tree.setOwnerClanUUID(clan.clanUUID);
                        tree.setTerritoryPos(currentPosition);
                        tree.setTimeUntilRenewScent((WCEServerConfig.SERVER.MAX_TERRITORY_TIME.get()*20*60)/8);
                        tree.setOwnerClanColor(clan.color);
                        tree.setOwnerClanName(clan.name);
                        tree.setTerritoryName(packet.name);
                    }
                }
            }

        });

        ctx.get().setPacketHandled(true);
    }

    private boolean takeXPfromPlayer(ServerPlayer player, int cost) {

        if (player.getAbilities().instabuild) return true;
        int remaining = cost - player.totalExperience;

        if (player.totalExperience < cost) {
            player.sendSystemMessage(Component.literal("⚠ You need " + remaining + " XP more.")
                    .withStyle(ChatFormatting.RED));
            return false;
        }

        player.giveExperiencePoints(-cost);
        return true;
    }
}
