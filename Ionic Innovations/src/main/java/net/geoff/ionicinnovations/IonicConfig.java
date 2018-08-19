package net.geoff.ionicinnovations;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;

@Config(modid = IonicInnovations.modID)
public class IonicConfig {
	
	public static FieldManipCategory fieldManipulator = new FieldManipCategory();
	
	public static class FieldManipCategory {
		@Name("Number of blocks to place each update")
		public int blocksPerUpdate = 20;
		@Name("Number of ticks (20 per second) per update")
		public int ticksPerUpdate = 5;
		@Name("Cost per new block (FE)")
		public int costPerPlacement = 1;
		@Name("Cost per tick per block")
		public int fieldBlockUpkeep = 5;
	}
}
