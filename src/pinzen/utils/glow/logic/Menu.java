package pinzen.utils.glow.logic;

import java.util.ArrayList;

import pinzen.utils.glow.ShaderProgram;
import pinzen.utils.glow.inputs.IMouseListener;
import pinzen.utils.glow.inputs.InputManager;
import pinzen.utils.glow.inputs.InputManager.MouseButton;
import pinzen.utils.glow.inputs.InputManager.MouseEvent;
import pinzen.utils.mathsfog.Matrix4f;
import pinzen.utils.mathsfog.Vertex2f;

public abstract class Menu implements IMouseListener{

	private ApplicationWindow window;
	
	private ArrayList<GUI> GUIs;
		
	public Menu(ApplicationWindow win) {
		this.window = win;
		this.GUIs = new ArrayList<GUI>();
		
		init();
	}
	
		/** ----- Functions to override ----- **/
	
	public abstract void init();
	
	public abstract void onShow();
	
	public abstract void update(long delta);
	
	public abstract void render(ShaderProgram s);
	
	public abstract void onHide();
	
	public abstract void dispose();

		/** --- End Functions to override --- **/
		
	
	public void setCurrent() {
		this.window.setMenu(this);
	}
	
	public void closeWindow() {
		this.window.close();
	}
	
	public void closeAllGUI() {
		for(GUI g : GUIs) {
			g.setVisible(false);
		}
	}
	
	public void addGUI(GUI g) {
		this.GUIs.add(g);
	}
	
	public void removeGUI(GUI g) {
		this.GUIs.remove(g);
	}
	
	/**
	 * Check if a click was processed inside a GUI
	 */
	public boolean processClickOnGUIs(Vertex2f pos, MouseButton button, MouseEvent event) {
		boolean onGUI = false;
		//Revert loop so last GUI is on top
		for(int i = GUIs.size()-1; i>=0; i--) {
			GUI currentGUI = GUIs.get(i);
			onGUI = onGUI || currentGUI.processClickOnComponents(pos, button, event);
			
			//Check if click is inside GUI and process if true
			if(!onGUI && currentGUI.isPointInsideGUI(pos)) {
				currentGUI.onMouseEvent(pos, button, event);
				onGUI = true;
			}
			
			//If click was consumed by a GUI or one of its components, stop looping
			if(onGUI)
				break;
		}
		
		return onGUI;
	}
	
	/**
	 * Render all GUIs of this menu
	 */
	protected void renderGUIs(ShaderProgram shaderGUI) {
		shaderGUI.setUniformMatrix4f("view", new Matrix4f());
		for(GUI g : this.GUIs) {
			g.render(shaderGUI);
			g.renderComponents(shaderGUI);
		}
	}
	
	/**
	 * Update all GUIs of this menu
	 */
	protected void updateGUIs(long delta) {		
		for(GUI g : this.GUIs) {
			g.updateComponents(delta);
			g.update(delta);
		}
	}
	
		/* ----- ----- Getters ----- ----- **/
	
	/**
	 * Get InputManager of the window container
	 * @return InputManager
	 */
	public InputManager inputs() {
		return this.window.getInputs();
	}
	
	public int getWidth() {
		return window.getWidth();
	}
	public int getHeight() {
		return window.getHeight();
	}
}
