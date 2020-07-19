package pinzen.utils.glow.logic;

import pinzen.utils.glow.components.Component;

public abstract class MenuSimple extends Menu{

	private GUISimple gui;
	
	public MenuSimple(ApplicationWindow win) {
		super(win);

		this.gui = new GUISimple(this);
		this.addGUI(gui);
	}

	public void addComponent(Component c) {
		this.gui.addComponent(c);
	}
}
