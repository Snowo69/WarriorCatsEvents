package net.snowteb.warriorcats_events.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.block.Block;
import net.snowteb.warriorcats_events.block.custom.MossbedBlock;

public class MossbBlocks {
    public static final DeferredRegister<Block> REGISTRY =
            DeferredRegister.create(ForgeRegistries.BLOCKS, "warriorcats_events");

    public static final RegistryObject<Block> MOSSBED = REGISTRY.register("mossbed", MossbedBlock::new);
}
