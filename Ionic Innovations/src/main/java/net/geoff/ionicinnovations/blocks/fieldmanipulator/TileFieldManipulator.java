package net.geoff.ionicinnovations.blocks.fieldmanipulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import net.geoff.ionicinnovations.IonicConfig;
import net.geoff.ionicinnovations.IonicInnovations;
import net.geoff.ionicinnovations.blocks.IonicBlocks;
import net.geoff.ionicinnovations.blocks.field.TileField;
import net.geoff.ionicinnovations.energy.NBTEnergyStorage;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileFieldManipulator extends TileEntity implements ITickable {
	
	private NBTEnergyStorage energyStorage;
	
	public boolean isGenerated = false;
	public int xSize = 10;
	public int ySize = 10;
	public int zSize = 10;
	public int forcefieldKey = 0;
	
	public static int maxBlocksPerUpdate = IonicConfig.fieldManipulator.blocksPerUpdate;
	public static int ticksPerUpdate = IonicConfig.fieldManipulator.ticksPerUpdate;
	
	//Costs 1 FE per block placed
	public static int costPerBlockPlaced = IonicConfig.fieldManipulator.costPerPlacement;
	
	public ArrayList<BlockPos> blockList = new ArrayList<>();
	
	public TileFieldManipulator() {
		//Store 1,000,000FE with intake of 1,000,000FE
		this.energyStorage = new NBTEnergyStorage(1000000,1000000,1000000);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		energyStorage.writeToNBT(compound);
		compound.setInteger("xSize", xSize);
		compound.setInteger("ySize", ySize);
		compound.setInteger("zSize", zSize);
		compound.setInteger("ffKey", forcefieldKey);
		compound.setBoolean("isActive", isGenerated);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		energyStorage.readFromNBT(compound);
		xSize = compound.getInteger("xSize");
		ySize = compound.getInteger("ySize");
		zSize = compound.getInteger("zSize");
		forcefieldKey = compound.getInteger("ffKey");
		isGenerated = compound.getBoolean("isActive");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY) {
			return (T) this.energyStorage;
		}
		return super.getCapability(capability, facing);
	}
	
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	public void setForceField(boolean val) {
		if(val) {
			generateForceFieldClean();
		} else {
			destroyForceField();
		}
	}
	
	//Forces field refresh, if it is generated, otherwise does nothing
	public void forceReloadForceField() {
		if(isGenerated) {
			generateForceFieldClean();
		}
	}
	
	//Remove old blocks, fresh forcefield
	private void generateForceFieldClean() {
		forcefieldKey = new Random().nextInt();
		generateForceField();
		
	}
	
	private void generateForceField() {
		
		blockList.clear();
		for(int x = -xSize; x <= xSize; x++) {
			for(int y = -ySize; y <= ySize; y++) {
				for(int z = -zSize; z <= zSize; z++) {
					//Check y is floor or ceiling
					if(y == -ySize || y == ySize) {
						//Fill area
						blockList.add(new BlockPos(x,y,z).add(pos));
					} else {
						//Hollow area
						if(x == -xSize || x == xSize) {
							//Fill z
							blockList.add(new BlockPos(x,y,z).add(pos));
						}
						if(z == -zSize || z == zSize) {
							//Fill x
							blockList.add(new BlockPos(x,y,z).add(pos));
						}
					}
				}
			}
		}
		Collections.shuffle(blockList);
		IonicInnovations.logger.info("Placing: " + blockList.size() + " blocks");
		//Offset by position and save to list
		
		isGenerated = true;
	}
	
	private void destroyForceField() {
		forcefieldKey = 0;		
		isGenerated = false;
	}

	@Override
	public void update() {
		if(!world.isRemote && world != null) {
			//this.energyStorage.receiveEnergy(EnergyUtil.receiveEnergyFromAll(world, pos, this.energyStorage.getMaxReceive()), false);
			if(world.getTotalWorldTime() % ticksPerUpdate == 0 && isGenerated) {
				if(isGenerated && blockList.size() == 0) {
					//World reload, don't clean old blocks, just update cube shape.
					generateForceField();
				}
				IonicInnovations.logger.info(this.energyStorage.getEnergyStored());
				int numChosen = 0;
				Collections.shuffle(blockList);
				for(BlockPos block : blockList) {

					if(world.getBlockState(block).getBlock() == IonicBlocks.BLOCK_UTIL_FIELD) {

						continue;
					} else {
						if(world.getBlockState(block).getBlock() == Blocks.AIR) {
							
							if(this.energyStorage.getEnergyStored() < costPerBlockPlaced) {
								break;
							}
							this.energyStorage.extractEnergy(costPerBlockPlaced, false);
							world.setBlockState(block, IonicBlocks.BLOCK_UTIL_FIELD.getDefaultState());
							
							//ForcefieldMod.logger.info("Placing block at: " + block.toString());
							TileField tl = (TileField) this.world.getTileEntity(block);
							if(tl != null) {
								tl.generator = this.pos;
								tl.forcefieldKey = this.forcefieldKey;
								//ForcefieldMod.logger.info("Block Placed - END");
								numChosen++;
							}
						}
					}
					if(numChosen >= 20) {
						//ForcefieldMod.logger.info("Max blocks reached - KILL");
						break;
					}
				}
			}
		}
	}
}
