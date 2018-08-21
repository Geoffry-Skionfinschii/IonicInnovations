package net.geoff.ionicinnovations.fluids;

import net.geoff.ionicinnovations.IonicInnovations;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidPlasma extends Fluid {

	public FluidPlasma() {
		super("plasma", new ResourceLocation(IonicInnovations.modID, "plasma_still"), new ResourceLocation(IonicInnovations.modID, "plasma_flow"));
		
		this.setLuminosity(15);
		this.setDensity(-1000);
		this.setTemperature(10000000);
		this.setGaseous(true);
	}
	
}
