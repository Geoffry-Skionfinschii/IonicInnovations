package net.geoff.ionicinnovations.proxy;

import net.geoff.ionicinnovations.IonicInnovations;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {
	
	
	public void registerItemRenderer(Item item, int meta, String id) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(IonicInnovations.modID + ":" + id, "inventory"));
	}
}
