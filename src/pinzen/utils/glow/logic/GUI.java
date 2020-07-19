package pinzen.utils.glow.logic;

import java.util.ArrayList;
import java.util.List;

import pinzen.utils.glow.ShaderProgram;
import pinzen.utils.glow.components.Component;
import pinzen.utils.glow.inputs.IMouseListener;
import pinzen.utils.glow.inputs.InputManager.MouseButton;
import pinzen.utils.glow.inputs.InputManager.MouseEvent;
import pinzen.utils.mathsfog.Vertex2f;

public abstract class GUI implements IMouseListener{

	private static boolean DEFAULT_VISIBILITY = true;
	
	
	private List<Component> components;
	
	private boolean visible;
	private Vertex2f position, bounds;
	private Menu container;
	
	private boolean shouldCatchClick;
	
	public GUI(Menu m, Vertex2f pos, Vertex2f bounds) {
		this.container = m;
		
		this.position = pos.clone();
		this.bounds = bounds.clone();
		
		this.visible = DEFAULT_VISIBILITY;
		this.shouldCatchClick = true;
		
		this.components = new ArrayList<Component>();
		
		this.init();
	}
	
	public GUI(Menu m, float x, float y, float width, float height) {
		this(m, new Vertex2f(x,y), new Vertex2f(width,height));
	}
	
	public GUI(Menu m) {
		this(m, new Vertex2f(0, 0), new Vertex2f(m.getWidth(), m.getHeight()));
	}

	/**
	 * Check if a click was processed inside a Component
	 */
	public boolean processClickOnComponents(Vertex2f pos, MouseButton button, MouseEvent event) {
		boolean isOnComp = false;
		
		if(visible && this.isPointInsideGUI(pos)) {
			//Call every component's function onMouseEvent
			for(Component c : components) {
				//If a component already consumed event, stop using it
				if(!isOnComp) {
					isOnComp = isOnComp || c.onMouseEvent(pos, button, event);
				}
				else {
					break;
				}
			}
		}
		
		return isOnComp;
	}
	
	public boolean isPointInsideGUI(Vertex2f pos) {
		Vertex2f absPos = Vertex2f.difference(position, pos);
		return absPos.x >= 0 && absPos.y >= 0 && absPos.x <= bounds.x && absPos.y <= bounds.y;
	}

	/**
	 * Render all Components of this GUI
	 */
	protected void renderComponents(ShaderProgram s) {
		if(this.visible) {
			for(Component c : components) {
				c.render(s);
			}
		}
	}
	
	/**
	 * Update all Components of this GUI
	 */
	protected void updateComponents(long delta) {
		if(this.visible) {
			for(Component c : components) {
				c.update(container.inputs(), delta);
			}
		}
	}
	
		/** ----- Functions to override ----- **/

	protected abstract void init();
	
	protected abstract void onShow();

	public abstract void render(ShaderProgram s);
	
	public abstract void update(long delta);
	
	protected abstract void onHide();
	
		/** --- End Functions to override --- **/
	
	
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
	
	public void setCatchClickBehavior(boolean shouldCatch) {
		this.shouldCatchClick = shouldCatch;
	}
	
	public Vertex2f getPosition() {
		return this.position.clone();
	}
	
	public Vertex2f getBounds() {
		return this.bounds.clone();
	}
	
	public boolean shouldCatchClick() {
		return this.shouldCatchClick;
	}
	
	
	
	public static boolean getDefaultVisibility() {
		return GUI.DEFAULT_VISIBILITY;
	}
	
	public static void setDefaultVisibility(boolean vis) {
		GUI.DEFAULT_VISIBILITY = vis;
	}
}
