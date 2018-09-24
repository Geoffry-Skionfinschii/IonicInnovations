package net.geoff.ionicinnovations.network;

import net.geoff.ionicinnovations.IonicInnovations;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
	public static SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(IonicInnovations.modID);
	
	private static int netID = 0;
	
	public static void initMessages() {
		INSTANCE.registerMessage(MessageFieldManipulator.Handler.class, MessageFieldManipulator.class, netID++, Side.CLIENT);
		INSTANCE.registerMessage(MessageSetFieldManipulator.Handler.class, MessageSetFieldManipulator.class, netID++, Side.SERVER);
		INSTANCE.registerMessage(MessageUpdateFieldManipulator.Handler.class, MessageUpdateFieldManipulator.class, netID++, Side.CLIENT);
	}
}
