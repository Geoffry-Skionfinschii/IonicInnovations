package net.geoff.ionicinnovations.network;

import io.netty.buffer.ByteBuf;
import net.geoff.ionicinnovations.blocks.fieldmanipulator.GuiFieldManipulator;
import net.geoff.ionicinnovations.blocks.fieldmanipulator.TileFieldManipulator;
import net.geoff.ionicinnovations.energy.EnergyUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageFieldManipulator implements IMessage {
	
	public MessageFieldManipulator() {}
	
	private int xSize;
	private int ySize;
	private int zSize;
	private int energyStored;
	private BlockPos pos;
	
	public MessageFieldManipulator(TileFieldManipulator tile, BlockPos pos) {
		// TODO Auto-generated constructor stub
		this.xSize = tile.xSize;
		this.ySize = tile.ySize;
		this.zSize = tile.zSize;
		this.pos = pos;
		this.energyStored = EnergyUtil.getFixedCapability(tile, null).getEnergyStored();
	}

	
	@Override
	public void toBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		buf.writeInt(xSize);
		buf.writeInt(ySize);
		buf.writeInt(zSize);
		buf.writeLong(pos.toLong());
		buf.writeInt(energyStored);
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		xSize = buf.readInt();
		ySize = buf.readInt();
		zSize = buf.readInt();
		pos = BlockPos.fromLong(buf.readLong());
		energyStored = buf.readInt();
	}

	
	
	public static class Handler implements IMessageHandler<MessageFieldManipulator, IMessage> {

		@Override
		public IMessage onMessage(MessageFieldManipulator message, MessageContext ctx) {
			if(ctx.side == Side.CLIENT) {
				int x = message.xSize;
				int y = message.ySize;
				int z = message.zSize;
				BlockPos pos = message.pos;
				int energy = message.energyStored;
				Minecraft.getMinecraft().addScheduledTask(() -> {
					TileFieldManipulator te = (TileFieldManipulator) Minecraft.getMinecraft().world.getTileEntity(pos);
					te.xSize = x;
					te.ySize = y;
					te.zSize = z;
					te.clientEnergy = energy;
					Minecraft.getMinecraft().displayGuiScreen(new GuiFieldManipulator(pos, te));
				});
				
			} /*else {  //Removed this field and replaced with new MessageSetFieldManipulator
				int x = Math.max(0,Math.min(50,message.xSize));
				int y = Math.max(0,Math.min(50,message.ySize));
				int z = Math.max(0,Math.min(50,message.zSize));
				BlockPos pos = message.pos;
				EntityPlayerMP pl = ctx.getServerHandler().player;
				pl.getServer().addScheduledTask(() -> {
					if(pl.world.isBlockLoaded(pos)) {
						if(pl.world.getBlockState(pos).getBlock() == IonicBlocks.BLOCK_MACHINE_FIELDMANIP) {
							TileFieldManipulator ent = (TileFieldManipulator) pl.world.getTileEntity(pos);
							ent.xSize = x;
							ent.ySize = y;
							ent.zSize = z;
							ent.forceReloadForceField();
							ent.markDirty();
						}
					}
				});
				
			}*/
			return null;
		}
		
	}
}
