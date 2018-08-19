package net.geoff.ionicinnovations.blocks.fieldmanipulator;

import net.geoff.ionicinnovations.blocks.BlockTileEntity;
import net.geoff.ionicinnovations.network.MessageFieldManipulator;
import net.geoff.ionicinnovations.network.NetworkHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFieldManipulator extends BlockTileEntity<TileFieldManipulator> {

	public BlockFieldManipulator() {
		super(Material.ROCK, "field_manipulator");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Class<TileFieldManipulator> getTileEntityClass() {
		return TileFieldManipulator.class;
	}

	@Override
	public TileFieldManipulator createTileEntity(World world, IBlockState state) {
		return new TileFieldManipulator();
	}

	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, 
			EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			TileFieldManipulator tile = getTileEntity(world, pos);
			if(player.isSneaking()) {
				//Toggle field
				tile.setForceField(!tile.isGenerated);
			} else {
				NetworkHandler.INSTANCE.sendTo(new MessageFieldManipulator(tile.xSize,tile.ySize,tile.zSize,pos), (EntityPlayerMP) player);
			}
		}
		return true;
	}
}
