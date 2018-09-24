package net.geoff.ionicinnovations.blocks.field;

import net.geoff.ionicinnovations.IonicInnovations;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockField extends net.geoff.ionicinnovations.blocks.BlockTileEntity<TileField> {
	
	protected static final AxisAlignedBB FIELD_COLLISION_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.9375D, 0.9375D);
	
	public BlockField() {
		super(Material.ROCK, "forcefield");
		this.setBlockUnbreakable();
		this.setLightOpacity(0);
		this.setResistance(0.0F);
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
		
		this.getTileEntity(world, pos).useEmergencyPower((int) (dist == 0 ? 100000 : Math.pow(dist, 1.5) * 10), true);
		IonicInnovations.logger.debug("emergencypower: " + (dist == 0 ? 100000 : Math.pow(dist, 1.5) * 10));
	}
	
	@Override
	public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
		return true;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return FIELD_COLLISION_AABB;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		//super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
		double totalMotion = Math.sqrt(Math.pow(entityIn.motionX, 2) + Math.pow(entityIn.motionY, 2) + Math.pow(entityIn.motionZ, 2));
		IonicInnovations.logger.debug("Entity Impacted");
		TileField field = (TileField) worldIn.getTileEntity(pos);
		if(field != null) {
			field.useEmergencyPower((int) (totalMotion * 50), false);
			IonicInnovations.logger.debug("Entity impacted and used: " + totalMotion * 50 + " RF");
		}
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
        return false;
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
