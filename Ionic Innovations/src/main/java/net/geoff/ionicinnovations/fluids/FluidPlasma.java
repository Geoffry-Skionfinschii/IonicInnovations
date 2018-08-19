package net.geoff.ionicinnovations.fluids;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidPlasma extends Fluid {

	public FluidPlasma() {
		super("plasma", new ResourceLocation("blocks/plasma"), new ResourceLocation("blocks/plasma"));
		
		this.setLuminosity(15);
		this.setDensity(-1000);
		this.setTemperature(10000000);
		this.setGaseous(true);
	}
	
}
