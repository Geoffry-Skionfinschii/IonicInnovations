package net.geoff.ionicinnovations.network;

import io.netty.buffer.ByteBuf;
import net.geoff.ionicinnovations.blocks.IonicBlocks;
import net.geoff.ionicinnovations.blocks.fieldmanipulator.GuiFieldManipulator;
import net.geoff.ionicinnovations.blocks.fieldmanipulator.TileFieldManipulator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
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
	private BlockPos pos;
	
	public MessageFieldManipulator(int xSize, int ySize, int zSize, BlockPos pos) {
		// TODO Auto-generated constructor stub
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
		this.pos = pos;
	}

	
	@Override
	public void toBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		buf.writeInt(xSize);
		buf.writeInt(ySize);
		buf.writeInt(zSize);
		buf.writeLong(pos.toLong());
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		xSize = buf.readInt();
		ySize = buf.readInt();
		zSize = buf.readInt();
		pos = BlockPos.fromLong(buf.readLong());
	}

	
	
	public static class Handler implements IMessageHandler<MessageFieldManipulator, IMessage> {

		@Override
		public IMessage onMessage(MessageFieldManipulator message, MessageContext ctx) {
			if(ctx.side == Side.CLIENT) {
				int x = message.xSize;
				int y = message.ySize;
				int z = message.zSize;
				BlockPos pos = message.pos;
				Minecraft.getMinecraft().addScheduledTask(() -> {
					Minecraft.getMinecraft().displayGuiScreen(new GuiFieldManipulator(x,y,z,pos));
				});
				
			} else {
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
				
			}
			return null;
		}
		
	}
}
