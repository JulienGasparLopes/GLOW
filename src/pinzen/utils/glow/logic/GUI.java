package pinzen.utils.glow.logic;

import java.util.ArrayList;
import java.util.List;

import pinzen.utils.glow.Shader;
import pinzen.utils.glow.components.Component;
import pinzen.utils.glow.inputs.InputManager.MouseButton;
import pinzen.utils.glow.inputs.InputManager.MouseEvent;
import pinzen.utils.mathsfog.Vertex2f;

public abstract class GUI {

	private static boolean DEFAULT_VISIBILITY = true;
	
	
	private List<Component> components;
	
	private boolean visible;
	private Vertex2f position, bounds;
	private Menu container;
	
	public GUI(Menu m, Vertex2f pos, Vertex2f bounds) {
		this.container = m;
		
		this.position = pos.clone();
		this.bounds = bounds.clone();
		
		this.visible = DEFAULT_VISIBILITY;
		
		this.components = new ArrayList<Component>();
	}
	
	public GUI(Menu m, float x, float y, float width, float height) {
		this(m, new Vertex2f(x,y), new Vertex2f(width,height));
	}

	/**
	 * Function internally called for click logic <br>
	 * Prefer using "processMouseEvent"
	 * If really needed, don't forget to call super._onMouseEvent
	 */
	public boolean _onMouseEvent(Vertex2f pos, MouseButton button, MouseEvent event) {
		if(visible) {
			Vertex2f absPos = Vertex2f.difference(position, pos);
			//Check if event is on GUI bounds
			if(absPos.x >= 0 && absPos.y >= 0 && absPos.x <= bounds.x && absPos.y <= bounds.y) {
				//Call every component's function onMouseEvent
				boolean isOnComp = false;
				for(Component c : components) {
					//If a component already consumed event, stop using it
					if(!isOnComp)
						isOnComp = isOnComp || c.onMouseEvent(pos, button, event);
				}
				
				//If a component already consumed event do not call GUI's function onMouseEvent
				if(!isOnComp)
					this.processMouseEvent(pos, button, event);
				
				return true;
			}
		}
		
		
		return false;
	}

	/**
	 * Function internally called for rendering <br>
	 * Use "render" instead
	 */
	protected void _renderGUI(Shader s) {
		if(this.visible) {
			this.render(s);
			
			for(Component c : components)
				c.render(s);
		}
	}
	
	/**
	 * Function internally called for updating <br>
	 * Use "update" instead
	 */
	protected void _updateGUI(long delta) {
		if(this.visible) {
			this.update(delta);
			
			for(Component c : components) {
				c.update(container.inputs(), delta);
			}
		}
	}
	
	public abstract void render(Shader s);
	
	public abstract void update(long delta);
	
	protected abstract void processMouseEvent(Vertex2f pos, MouseButton button, MouseEvent event);

	protected abstract void onShow();
	
	protected abstract void onHide();
	
	public void addComponent(Component c) {
		this.components.add(c);
	}
	
	public void removeComponent(Component c) {
		this.components.remove(c);
	}
	
	public void setVisible(boolean v) {
		if(!this.visible && v)
			this.onShow();
		
		if(this.visible && !v)
			this.onHide();
		
		this.visible = v;
	}
	
	public boolean isVisible() {
		return this.visible;
	}
	
	public void toggleVisibility() {
		this.visible = !this.visible;
		
		if(this.visible)
			this.onShow();
		
		if(!this.visible)
			this.onHide();
	}
	
	public void translate(Vertex2f trans) {
		this.position = Vertex2f.translate(position, trans);
	}
	
	public void setPosition(Vertex2f pos) {
		this.position = pos.clone();
	}
	
	public Vertex2f getPosition() {
		return this.position.clone();
	}
	
	public Vertex2f getBounds() {
		return this.bounds.clone();
	}
	
	
	
	public static boolean getDefaultVisibility() {
		return GUI.DEFAULT_VISIBILITY;
	}
	
	public static void setDefaultVisibility(boolean vis) {
		GUI.DEFAULT_VISIBILITY = vis;
	}
}
