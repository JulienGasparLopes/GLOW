package pinzen.utils.glow.logic;

import java.util.ArrayList;

import pinzen.utils.glow.Shader;
import pinzen.utils.glow.inputs.IMouseListener;
import pinzen.utils.glow.inputs.InputManager;
import pinzen.utils.glow.inputs.InputManager.MouseButton;
import pinzen.utils.glow.inputs.InputManager.MouseEvent;
import pinzen.utils.mathsfog.Matrix4f;
import pinzen.utils.mathsfog.Vertex2f;

public abstract class Menu implements IMouseListener{

	private WindowContainer window;
	
	private ArrayList<GUI> GUIs;
	
	public Menu(WindowContainer win) {
		this.window = win;
		this.GUIs = new ArrayList<GUI>();
		
		init();
	}
	
	public abstract void init();
	
	public abstract void update(long delta);
	
	public abstract void render(Shader s);
	
	public abstract void dispose();
	
	protected abstract void processMouseEvent(Vertex2f pos,
											  MouseButton button,
											  MouseEvent event);

	
	public void setActual() {
		this.window.setMenu(this);
	}
	
	public void closeWindow() {
		this.window.close();
	}
	
	public void sendModelMatrix(Matrix4f model) {
		this.window.sendModelMatrix(model);
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
	 * Function internally called for click logic <br>
	 * Prefer using "processMouseEvent"
	 * If really needed, don't forget to call super.onMouseEvent
	 */
	public void _onMouseEvent(Vertex2f pos, MouseButton button, MouseEvent event) {
		boolean onGUI = false;
		//Revert loop so last GUI is on top
		for(int i = GUIs.size()-1; i>=0; i--) {
			onGUI = onGUI || GUIs.get(i)._onMouseEvent(pos, button, event);
			
			//If click was consumed by a GUI, stop loop
			if(onGUI)
				break;
		}
		
		//If click isn't in any GUI then process click
		if(!onGUI) {
			this.processMouseEvent(pos, button, event);
		}
	}
	
	/**
	 * Function internally called for rendering <br>
	 * Use "render"
	 */
	protected void _renderMenu(Shader s) {
		this.render(s);
		
		s.setUniformMatrix4f("view", new Matrix4f());
		for(GUI g : this.GUIs)
			g._renderGUI(s);
	}
	
	/**
	 * Function internally called for updating <br>
	 * Use "update"
	 */
	protected void _updateMenu(long delta) {
		this.update(delta);
		
		for(GUI g : this.GUIs)
			g._updateGUI(delta);;
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
