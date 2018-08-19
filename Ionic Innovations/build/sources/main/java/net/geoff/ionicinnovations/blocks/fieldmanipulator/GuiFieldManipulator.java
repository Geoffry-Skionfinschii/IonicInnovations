package net.geoff.ionicinnovations.blocks.fieldmanipulator;

import java.io.IOException;

import net.geoff.ionicinnovations.IonicInnovations;
import net.geoff.ionicinnovations.network.MessageFieldManipulator;
import net.geoff.ionicinnovations.network.NetworkHandler;
import net.geoff.ionicinnovations.util.gui.GuiNumberWang;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.math.BlockPos;

public class GuiFieldManipulator extends GuiScreen {
	
	private int xRad = 0;
	private int yRad = 0;
	private int zRad = 0;
	private BlockPos pos;
	
	private GuiNumberWang xRadius;
	private GuiNumberWang yRadius;
	private GuiNumberWang zRadius;
	
	private static final int BUTTON_ID_CONFIRM = 0;
	private static final int BUTTON_ID_CANCEL = 1;
	
	public GuiFieldManipulator(int xSize, int ySize, int zSize, BlockPos pos) {
		super();
		xRad = xSize;
		yRad = ySize;
		zRad = zSize;
		this.pos = pos;
	}
	
	@Override
	public void initGui() {
		this.xRadius = new GuiNumberWang(0, this.fontRenderer, this.width / 2 - 68, this.height / 2 - 20, 100, 20);
		this.xRadius.setMaxStringLength(3);
		this.xRadius.setText(Integer.toString(xRad));
		
		this.yRadius = new GuiNumberWang(0, this.fontRenderer, this.width / 2 - 68, this.height / 2 - 0, 100, 20);
		this.yRadius.setMaxStringLength(3);
		this.yRadius.setText(Integer.toString(yRad));
		
		this.zRadius = new GuiNumberWang(0, this.fontRenderer, this.width / 2 - 68, this.height / 2 + 20, 100, 20);
		this.zRadius.setMaxStringLength(3);
		this.zRadius.setText(Integer.toString(zRad));
		
		this.buttonList.add(new GuiButton(BUTTON_ID_CONFIRM, this.width / 2 - 68, this.height / 2 + 40, 45, 20, "Apply"));
		this.buttonList.add(new GuiButton(BUTTON_ID_CANCEL, this.width / 2 + 68, this.height / 2 + 40, 45, 20, "Cancel"));
		
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
		this.drawDefaultBackground();
		this.xRadius.drawTextBox();
		this.yRadius.drawTextBox();
		this.zRadius.drawTextBox();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void actionPerformed(GuiButton but) {
		switch(but.id) {
		case BUTTON_ID_CONFIRM:
			int x = Integer.parseInt(xRadius.getText());
			int y = Integer.parseInt(yRadius.getText());
			int z = Integer.parseInt(zRadius.getText());
			IonicInnovations.logger.info("Updated FF to: " + x + ", " + y + ", " + z);
			NetworkHandler.INSTANCE.sendToServer(new MessageFieldManipulator(x,y,z,pos));
			Minecraft.getMinecraft().displayGuiScreen(null);
			break;
			
		case BUTTON_ID_CANCEL:
			Minecraft.getMinecraft().displayGuiScreen(null);
			break;
		}
	}
	
}
