package net.geoff.ionicinnovations.blocks.field;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.geoff.ionicinnovations.IonicConfig;
import net.geoff.ionicinnovations.blocks.IonicBlocks;
import net.geoff.ionicinnovations.blocks.fieldmanipulator.TileFieldManipulator;
import net.geoff.ionicinnovations.energy.EnergyUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileField extends TileEntity implements ITickable {
	
	//private NBTEnergyStorage energyStorage;
	
	public BlockPos generator = new BlockPos(-1,-1,-1);
	public int forcefieldKey = -1;
	
	private int emergencyPowerRequirement = 0;
	private boolean spreadPower = false;
	
	private int updateTickOffset;
	
	//5 FE/s per tick
	private int energyUpkeep = IonicConfig.fieldManipulator.fieldBlockUpkeep;
	private int ticksPerUpdate = 5;
	
	public TileField() {
		//this.energyStorage = new NBTEnergyStorage(100000,100000,0,100000);
		updateTickOffset = new Random().nextInt(ticksPerUpdate);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		//energyStorage.writeToNBT(compound);
		compound.setLong("generatorPos", generator.toLong());
		compound.setInteger("ffKey", forcefieldKey);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		//energyStorage.readFromNBT(compound);
		generator = BlockPos.fromLong(compound.getLong("generatorPos"));
		forcefieldKey = compound.getInteger("ffKey");
		
	}
	
	public void useEmergencyPower(int amount, boolean spread) {
		emergencyPowerRequirement = amount;
		spreadPower = spread;
	}
	
	public void spreadEmergencyPower(int amount) {
		Set<TileField> fields = new HashSet<TileField>();
		for(EnumFacing side : EnumFacing.VALUES) {
			if(world.getBlockState(this.pos.offset(side)) == IonicBlocks.BLOCK_UTIL_FIELD) {
				fields.add((TileField) world.getTileEntity(pos.offset(side)));
			}
		}
		int distribute = amount / fields.size();
		for(TileField field : fields) {
			field.useEmergencyPower(distribute, true);
		}
	}

	@Override
	public void update() {
		if(!world.isRemote) {
			
			if((world.getTotalWorldTime() + updateTickOffset) % ticksPerUpdate == 0) {
				//int removedPower = energyStorage.extractEnergy(energyUpkeep * ticksPerUpdate + emergencyPowerRequirement, false);
				if(world.getBlockState(generator).getBlock() != IonicBlocks.BLOCK_MACHINE_FIELDMANIP) {
					world.setBlockToAir(pos);
				}
				
				if(world.getBlockState(generator).getBlock() == IonicBlocks.BLOCK_MACHINE_FIELDMANIP) {
					TileFieldManipulator tile = (TileFieldManipulator) world.getTileEntity(generator);
					if((tile).forcefieldKey != forcefieldKey) {
						world.setBlockToAir(pos);
					}
					//energyStorage.extractEnergy(energyUpkeep * ticksPerUpdate, false);
					int expectedEnergy = energyUpkeep * ticksPerUpdate + emergencyPowerRequirement;
					int receivedEnergy = EnergyUtil.fieldUseEnergy(tile, expectedEnergy, null);
					//energyStorage.receiveEnergy(receivedEnergy, false);
					
					
					
					if(receivedEnergy < expectedEnergy) {
						if(spreadPower && (expectedEnergy - receivedEnergy) != emergencyPowerRequirement) {
							spreadEmergencyPower(emergencyPowerRequirement - (expectedEnergy - receivedEnergy));
						}
						world.setBlockToAir(pos);
					}
					
					emergencyPowerRequirement = 0;
					spreadPower = false;
				}
			}
		}
	}
	
	
}
