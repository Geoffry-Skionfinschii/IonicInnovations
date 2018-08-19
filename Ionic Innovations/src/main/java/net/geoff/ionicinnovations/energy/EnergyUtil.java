package net.geoff.ionicinnovations.energy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyUtil {
	public static int fieldUseEnergy(World world, BlockPos pos, int energy, EnumFacing face) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile == null) {
			return 0;
		}
		return fieldUseEnergy(tile,energy,face);
	}
	
	public static int fieldUseEnergy(TileEntity tile, int energy, EnumFacing face) {
		if(checkCapable(tile,face)) {
			IEnergyStorage energyStorage = getFixedCapability(tile, face);
			if(energyStorage.getEnergyStored() > energy) {
				int val = energyStorage.extractEnergy(energy, false);
				//IonicInnovations.logger.info(val);
				return val;
			} else {
				int val = energyStorage.extractEnergy(energyStorage.getEnergyStored(), false);
				//IonicInnovations.logger.info("All en" + val);
				return val;
			}
		}
		return 0;
	}
	
	
	public static boolean checkCapable(TileEntity tile, EnumFacing face) {
		//If not null, check capable, else false
		return tile != null ? tile.hasCapability(CapabilityEnergy.ENERGY, face != null ? face.getOpposite() : null) : false;
	}
	
	public static IEnergyStorage getFixedCapability(TileEntity tile, EnumFacing face) {
		return tile.getCapability(CapabilityEnergy.ENERGY, face != null ? face.getOpposite() : null);
	}
	
	public static int giveEnergy(World world, BlockPos pos, int amount, EnumFacing face) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile == null) {
			return 0;
		}
		return giveEnergy(tile,amount,face);
	}
	
	public static int giveEnergy(TileEntity tile, int amount, EnumFacing face) {
		if(checkCapable(tile,face)) {
			IEnergyStorage energyStorage = getFixedCapability(tile, face);
			
			if(energyStorage.canReceive()) {
				int received = energyStorage.receiveEnergy(amount, false);
				return received;
			}
		}
		return 0;
	}
	
	public static int giveEnergyToAllFaces(World world, BlockPos pos, int maxSend) {
		
		for(EnumFacing side : EnumFacing.VALUES) {
			TileEntity tile = world.getTileEntity(pos.offset(side));
			if(checkCapable(tile, side)) {
				getFixedCapability(tile,side).receiveEnergy(maxSend, false);
			}
		}
		return 0;
	}
}
