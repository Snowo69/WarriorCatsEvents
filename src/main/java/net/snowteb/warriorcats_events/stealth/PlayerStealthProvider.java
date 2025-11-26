package net.snowteb.warriorcats_events.stealth;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerStealthProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerStealth> STEALTH_MODE = CapabilityManager.get(new CapabilityToken<>() {});

    private PlayerStealth stealth = null;
    private final LazyOptional<PlayerStealth> optional = LazyOptional.of(this::createPlayerStealth);

    private PlayerStealth createPlayerStealth() {
        if (this.stealth == null) {
            this.stealth = new PlayerStealth();
        }
        return this.stealth;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == STEALTH_MODE ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        createPlayerStealth().saveNBT(tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerStealth().loadNBT(nbt);
    }

    public PlayerStealth getOrCreateStealth() {
        PlayerStealth s = createPlayerStealth();
        return s;
    }
}
