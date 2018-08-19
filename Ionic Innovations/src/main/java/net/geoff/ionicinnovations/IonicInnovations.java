package net.geoff.ionicinnovations;

import org.apache.logging.log4j.Logger;

import net.geoff.ionicinnovations.blocks.IonicBlocks;
import net.geoff.ionicinnovations.fluids.IonicFluids;
import net.geoff.ionicinnovations.network.NetworkHandler;
import net.geoff.ionicinnovations.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = IonicInnovations.modID, name = IonicInnovations.name, version = IonicInnovations.version)
public class IonicInnovations {

	public static final String modID = "ionicinnovations";
	public static final String name = "Ionic Innovations";
	public static final String version = "1.0.0";
	
	@SidedProxy(serverSide = "net.geoff.ionicinnovations.proxy.CommonProxy", clientSide = "net.geoff.ionicinnovations.proxy.ClientProxy")
	public static CommonProxy proxy;
	
	@Mod.Instance(modID)
	public static IonicInnovations instance;
	
	public static Logger logger;
	
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		
		NetworkHandler.initMessages();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}
	
	@Mod.EventBusSubscriber
	public static class RegistrationHandler {
		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			IonicBlocks.register(event.getRegistry());
			IonicFluids.registerFluids();
		}
		
		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			IonicBlocks.registerItemBlocks(event.getRegistry());
		}
		
		@SubscribeEvent
		public static void registerModels(ModelRegistryEvent event) {
			IonicBlocks.registerModels();
		}
	}
}
