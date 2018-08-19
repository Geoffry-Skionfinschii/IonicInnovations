package net.geoff.ionicinnovations.blocks.field;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockField extends net.geoff.ionicinnovations.blocks.BlockTileEntity<TileField> {

	public BlockField() {
		super(Material.ROCK, "forcefield");
		this.setBlockUnbreakable();
		this.setLightOpacity(0);
	}

	@Override
	public Class<TileField> getTileEntityClass() {
		return TileField.class;
	}

	@Override
	public TileField createTileEntity(World world, IBlockState state) {
		return new TileField();
	}
	
	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion exp) {
		Vec3d expPos = exp.getPosition();
		double dist = pos.distanceSq(expPos.x,expPos.y,expPos.z);
		
		this.getTileEntity(world, pos).useEmergencyPower(dist == 0 ? 100000 : 1/(int) dist, true);;
	}
	
	
	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isTranslucent(IBlockState state) {
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing face) {
		IBlockState iblock = access.getBlockState(pos.offset(face));
		Block block = iblock.getBlock();
		
		if(block.isOpaqueCube(iblock)) {
			return false;
		}
		return true;
	}
}
