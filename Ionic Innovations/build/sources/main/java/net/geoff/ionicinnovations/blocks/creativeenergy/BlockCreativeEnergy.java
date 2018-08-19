package net.geoff.ionicinnovations.blocks.creativeenergy;

import net.geoff.ionicinnovations.blocks.BlockTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public class BlockCreativeEnergy extends BlockTileEntity<TileCreativeEnergy> {

	public BlockCreativeEnergy() {
		super(Material.ROCK, "creative_energy");
		this.setBlockUnbreakable();
		this.setResistance(Float.MAX_VALUE);
	}

	@Override
	public Class<TileCreativeEnergy> getTileEntityClass() {
		return TileCreativeEnergy.class;
	}

	@Override
	public TileCreativeEnergy createTileEntity(World world, IBlockState state) {
		return new TileCreativeEnergy();
	}
	
}
