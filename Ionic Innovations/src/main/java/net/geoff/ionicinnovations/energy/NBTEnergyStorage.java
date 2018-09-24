package net.geoff.ionicinnovations.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class NBTEnergyStorage extends EnergyStorage implements IEnergyStorage {

	public NBTEnergyStorage(int capacity) {
		super(capacity);
	}
	
	public NBTEnergyStorage(int capacity, int maxReceive) {
		super(capacity, maxReceive);
	}
	
	public NBTEnergyStorage(int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
	}
	
	public NBTEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
		super(capacity, maxReceive, maxExtract, energy);
	}

	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("energy", this.energy);
		nbt.setInteger("capacity", this.capacity);
		nbt.setInteger("maxExtract", this.maxExtract);
		nbt.setInteger("maxReceive", this.maxReceive);
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		this.energy = nbt.getInteger("energy");
		this.capacity = nbt.getInteger("capacity");
		this.maxExtract = nbt.getInteger("maxExtract");
		this.maxReceive = nbt.getInteger("maxReceive");
	}
	
	@Override
	public int receiveEnergy(int amount, boolean simulated) {
		int acceptedEnergy = Math.min(this.capacity - this.energy, amount);
		this.energy += acceptedEnergy;
		return acceptedEnergy;
	}
	

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		int val = Math.min(this.energy, maxExtract);
		this.energy -= val;
		return val;
	}

	@Override
	public int getEnergyStored() {
		return this.energy;
	}

	@Override
	public int getMaxEnergyStored() {
		return this.capacity;
	}

	//Allows forced setting for clientside updates and such
	public void setEnergyStored(int set) {
		energy = Math.min(set, this.capacity);
	}
	
	@Override
	public boolean canExtract() {
		return true;
	}

	@Override
	public boolean canReceive() {
		return true;
	}
	
	public int getMaxExtract() {
		return this.maxExtract;
	}
	
	public int getMaxReceive() {
		return this.maxReceive;
	}
}
