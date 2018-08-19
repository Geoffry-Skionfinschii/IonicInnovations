package net.geoff.ionicinnovations.blocks.creativeenergy;

import net.geoff.ionicinnovations.energy.EnergyUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileCreativeEnergy extends TileEntity implements ITickable {

	@Override
	public void update() {
		EnergyUtil.giveEnergyToAllFaces(this.world, this.pos, Integer.MAX_VALUE);
	}

}
