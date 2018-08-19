package net.geoff.ionicinnovations.fluids;

import net.minecraftforge.fluids.FluidRegistry;

public class IonicFluids {
	
	
	public static final FluidPlasma FLUID_PLASMA = new FluidPlasma();
	
	public static void registerFluids() {
		FluidRegistry.enableUniversalBucket();
		
		FluidRegistry.registerFluid(FLUID_PLASMA);
		FluidRegistry.addBucketForFluid(FLUID_PLASMA);
	}
	
}
