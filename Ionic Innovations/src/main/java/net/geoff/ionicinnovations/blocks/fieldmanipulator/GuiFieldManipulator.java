package net.geoff.ionicinnovations.blocks.fieldmanipulator;

import java.io.IOException;

import net.geoff.ionicinnovations.IonicInnovations;
import net.geoff.ionicinnovations.network.MessageSetFieldManipulator;
import net.geoff.ionicinnovations.network.NetworkHandler;
import net.geoff.ionicinnovations.util.gui.GuiNumberWang;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class GuiFieldManipulator extends GuiScreen {
	
	private int xRad = 0;
	private int yRad = 0;
	private int zRad = 0;
	private BlockPos pos;
	private int energyStored;
	
	private TileFieldManipulator te;
	
	private GuiNumberWang xRadius;
	private GuiNumberWang yRadius;
	private GuiNumberWang zRadius;
	
	private static final int BUTTON_ID_CONFIRM = 0;
	private static final int BUTTON_ID_CANCEL = 1;
	
	
	private int xL;
	private int yT;
	
	private int xR;
	private int yB;
	
	public GuiFieldManipulator(BlockPos pos, TileEntity tileEntity) {
		super();
		te = (TileFieldManipulator) tileEntity;
		xRad = te.xSize;
		yRad = te.ySize;
		zRad = te.zSize;
		energyStored = te.clientEnergy;
		this.pos = pos;
		((EntityPlayer) Minecraft.getMinecraft().player).sendMessage(new TextComponentString("GUI: " + energyStored));
	}
	
	@Override
	public void initGui() {
		
		xL = this.width / 2 - 60;
		yT = this.height / 2 - 50;
		
		xR = this.width / 2 + 60;
		yB = this.height / 2 + 50;
		
		this.xRadius = new GuiNumberWang(0, this.fontRenderer, xR - 50, yT + 20, 100, 20);
		this.xRadius.setMaxStringLength(3);
		this.xRadius.setText(Integer.toString(xRad));
		
		this.yRadius = new GuiNumberWang(0, this.fontRenderer, xR - 50, yT + 40, 100, 20);
		this.yRadius.setMaxStringLength(3);
		this.yRadius.setText(Integer.toString(yRad));
		
		this.zRadius = new GuiNumberWang(0, this.fontRenderer, xR - 50, yT + 60, 100, 20);
		this.zRadius.setMaxStringLength(3);
		this.zRadius.setText(Integer.toString(zRad));
		
		this.buttonList.add(new GuiButton(BUTTON_ID_CONFIRM, xL + 10, yB - 10, 45, 20, "Apply"));
		this.buttonList.add(new GuiButton(BUTTON_ID_CANCEL, xR - 10, yB - 10, 45, 20, "Cancel"));
		
	}
	
	@Override
	protected void keyTyped(char par1, int par2) {
		try {
			super.keyTyped(par1, par2);
			this.xRadius.textboxKeyTyped(par1, par2);
			this.yRadius.textboxKeyTyped(par1, par2);
			this.zRadius.textboxKeyTyped(par1, par2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		this.xRadius.updateCursorCounter();
		this.yRadius.updateCursorCounter();
		this.zRadius.updateCursorCounter();
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if(te != null) {
			energyStored = te.clientEnergy;
		} else {
			Minecraft.getMinecraft().displayGuiScreen(null);
		}
		
		this.drawDefaultBackground();
		this.xRadius.drawTextBox();
		this.yRadius.drawTextBox();
		this.zRadius.drawTextBox();
		drawString(this.fontRenderer, "North/South Radius", xL + 10, yT + 20, 0xFFFFFF);
		drawString(this.fontRenderer, "Top/Bottom Radius", xL + 10, yT + 40, 0xFFFFFF);
		drawString(this.fontRenderer, "East/West Radius", xL + 10, yT + 60, 0xFFFFFF);
		drawRect(20,(int) (200 - (200 * ((float) energyStored / 1000000))),40,200, 0xFFFFFFFF);
		super.drawScreen(mouseX,mouseY,partialTicks);
	}
	
	@Override
	protected void mouseClicked(int x, int y, int btn) {
		try {
			super.mouseClicked(x, y, btn);
			this.xRadius.mouseClicked(x, y, btn);
			this.yRadius.mouseClicked(x, y, btn);
			this.zRadius.mouseClicked(x, y, btn);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	protected void actionPerformed(GuiButton but) {
		switch(but.id) {
		case BUTTON_ID_CONFIRM:
			int x = Integer.parseInt(xRadius.getText());
			int y = Integer.parseInt(yRadius.getText());
			int z = Integer.parseInt(zRadius.getText());
			IonicInnovations.logger.info("Updated FF to: " + x + ", " + y + ", " + z);
			NetworkHandler.INSTANCE.sendToServer(new MessageSetFieldManipulator(x,y,z,pos));
			Minecraft.getMinecraft().displayGuiScreen(null);
			break;
			
		case BUTTON_ID_CANCEL:
			Minecraft.getMinecraft().displayGuiScreen(null);
			break;
		}
	}
	
}
