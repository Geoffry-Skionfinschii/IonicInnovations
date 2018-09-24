package net.geoff.ionicinnovations.network;

import io.netty.buffer.ByteBuf;
import net.geoff.ionicinnovations.blocks.IonicBlocks;
import net.geoff.ionicinnovations.blocks.fieldmanipulator.TileFieldManipulator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSetFieldManipulator implements IMessage {
	
	public MessageSetFieldManipulator() {}
	
	private int xSize;
	private int ySize;
	private int zSize;
	private BlockPos pos;
	
	public MessageSetFieldManipulator(int x, int y, int z, BlockPos pos) {
		this.xSize = x;
		this.ySize = y;
		this.zSize = z;
		this.pos = pos;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		xSize = buf.readInt();
		ySize = buf.readInt();
		zSize = buf.readInt();
		pos = BlockPos.fromLong(buf.readLong());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(xSize);
		buf.writeInt(ySize);
		buf.writeInt(zSize);
		buf.writeLong(pos.toLong());
	}
	
	public static class Handler implements IMessageHandler<MessageSetFieldManipulator, IMessage> {

		@Override
		public IMessage onMessage(MessageSetFieldManipulator message, MessageContext ctx) {
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
			return null;
		}
		
	}

}
