package net.geoff.ionicinnovations.network;

import io.netty.buffer.ByteBuf;
import net.geoff.ionicinnovations.blocks.fieldmanipulator.TileFieldManipulator;
import net.geoff.ionicinnovations.energy.EnergyUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateFieldManipulator implements IMessage {

	public MessageUpdateFieldManipulator() {}
	
	private BlockPos pos;
	private int energyStored;
	
	public MessageUpdateFieldManipulator(TileFieldManipulator tile, BlockPos pos) {
		this.pos = pos;
		this.energyStored = EnergyUtil.getFixedCapability(tile, null).getEnergyStored();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		energyStored = buf.readInt();
		pos = BlockPos.fromLong(buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(energyStored);
		buf.writeLong(pos.toLong());
	}
	
	public static class Handler implements IMessageHandler<MessageUpdateFieldManipulator, IMessage> {

		@Override
		public IMessage onMessage(MessageUpdateFieldManipulator message, MessageContext ctx) {
			BlockPos pos = message.pos;
			int energy = message.energyStored;
			Minecraft.getMinecraft().addScheduledTask(() -> {
				TileFieldManipulator te = (TileFieldManipulator) Minecraft.getMinecraft().world.getTileEntity(pos);
				te.clientEnergy = energy;
				//Minecraft.getMinecraft().player.sendChatMessage("Upd");
			});
			return null;
		}
		
	}
}
