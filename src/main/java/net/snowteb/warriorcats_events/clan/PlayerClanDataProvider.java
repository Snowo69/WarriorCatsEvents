package net.snowteb.warriorcats_events.clan;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerClanDataProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerClanData> PLAYER_CLAN_DATA = CapabilityManager.get(new CapabilityToken<PlayerClanData>() {});


    private PlayerClanData clanData  = null;
    private final LazyOptional<PlayerClanData> optional = LazyOptional.of(this::createClanData);

    private PlayerClanData createClanData() {
        if(this.clanData == null) {
            this.clanData = new PlayerClanData();
        }

        return this.clanData;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == PLAYER_CLAN_DATA) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }


    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createClanData().saveNBT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createClanData().loadNBT(nbt);
    }

    public PlayerClanData getOrCreateClanData() {
        PlayerClanData t = createClanData();
        t.reset();
        return t;
    }


}
