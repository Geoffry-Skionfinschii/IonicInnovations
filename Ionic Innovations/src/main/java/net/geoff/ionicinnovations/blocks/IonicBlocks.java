package net.geoff.ionicinnovations.blocks;

import net.geoff.ionicinnovations.blocks.creativeenergy.BlockCreativeEnergy;
import net.geoff.ionicinnovations.blocks.field.BlockField;
import net.geoff.ionicinnovations.blocks.fieldmanipulator.BlockFieldManipulator;
import net.geoff.ionicinnovations.fluids.IonicFluids;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class IonicBlocks {
	
	public static BlockField BLOCK_UTIL_FIELD = new BlockField();
	public static BlockFieldManipulator BLOCK_MACHINE_FIELDMANIP = new BlockFieldManipulator();
	public static BlockCreativeEnergy BLOCK_UTIL_CREATIVEENERGY = new BlockCreativeEnergy();
	
	public static BlockFluidFinite FLUID_PLASMA_BLOCK = new BlockFluidFinite(IonicFluids.FLUID_PLASMA, Material.LAVA);
	
	public static void register(IForgeRegistry<Block> registry) {
		registry.registerAll(
				BLOCK_UTIL_FIELD,
				BLOCK_MACHINE_FIELDMANIP,
				BLOCK_UTIL_CREATIVEENERGY
			);
		
		registerTile(BLOCK_UTIL_FIELD);
		registerTile(BLOCK_MACHINE_FIELDMANIP);
		registerTile(BLOCK_UTIL_CREATIVEENERGY);
	}
	
	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.registerAll(
				BLOCK_MACHINE_FIELDMANIP.createItemBlock(),
				BLOCK_UTIL_CREATIVEENERGY.createItemBlock()
			);
	}
	
	public static void registerModels() {
		registerItemModel(BLOCK_MACHINE_FIELDMANIP);
		registerItemModel(BLOCK_UTIL_FIELD);
		registerItemModel(BLOCK_UTIL_CREATIVEENERGY);
	}
	
	public static void registerTile(BlockTileEntity<?> block) {
		GameRegistry.registerTileEntity(block.getTileEntityClass(), block.getRegistryName());
	}
	
	public static void registerItemModel(BlockBase block) {
		block.registerItemModel(Item.getItemFromBlock(block));
	}
	
}
