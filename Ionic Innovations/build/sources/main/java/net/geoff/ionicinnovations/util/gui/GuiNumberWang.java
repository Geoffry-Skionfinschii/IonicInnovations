package net.geoff.ionicinnovations.util.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiNumberWang extends GuiTextField {

	public GuiNumberWang(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
		super(componentId, fontrendererObj, x, y, par5Width, par6Height);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void writeText(String str) {
		super.writeText(str.replaceAll("[^0-9]", ""));
	}
}
