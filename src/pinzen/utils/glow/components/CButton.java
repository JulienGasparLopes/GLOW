package pinzen.utils.glow.components;

import pinzen.utils.glow.inputs.InputManager;
import pinzen.utils.glow.inputs.InputManager.MouseButton;
import pinzen.utils.glow.inputs.InputManager.MouseEvent;
import pinzen.utils.mathsfog.RectBounds2f;
import pinzen.utils.mathsfog.Vertex2f;

public abstract class CButton extends Component{

	private Runnable callback;
	
	public enum ButtonState{
		IDDLE,
		HOVERED,
		PRESSED,
		DISABLED;
	}
	
	private ButtonState state;
	protected RectBounds2f bounds;
	
	public CButton(Vertex2f pos, Vertex2f dim) {
		this.state = ButtonState.IDDLE;
		this.bounds = new RectBounds2f(pos, dim);
	}
	
	public CButton(float x, float y, float width, float height) {
		this(new Vertex2f(x,y), new Vertex2f(width, height));
	}
	
	public CButton(Vertex2f pos, float width, float height) {
		this(pos, new Vertex2f(width,height));
	}
	
	@Override
	public void update(InputManager inputs, long delta) {
		//Update only if button ins't disabled
		if(this.state != ButtonState.DISABLED) {
			Vertex2f pos = inputs.GetcursorPos();
			if(this.state == ButtonState.IDDLE && bounds.contains(pos)) {
				this.state = ButtonState.HOVERED;
			}
			else if(this.state == ButtonState.HOVERED && !bounds.contains(pos)) {
				this.state = ButtonState.IDDLE;
			}
		}
	}
			
	@Override
	public boolean onMouseEvent(Vertex2f pos, MouseButton button, MouseEvent event) {
		if(this.state != ButtonState.DISABLED && button == MouseButton.LEFT) {
			if(this.bounds.contains(pos)) {
				//Button was pressed
				if(event == MouseEvent.PRESSED) {
					this.state = ButtonState.PRESSED;
				}
				//Button was released, check if it was pressed before
				else if(event == MouseEvent.RELEASED && this.state == ButtonState.PRESSED) {
					this.state = ButtonState.HOVERED;
					if(this.callback != null)
						this.callback.run();
				}
				
				return true;
			}
			else if(event == MouseEvent.RELEASED) {
				this.state = ButtonState.IDDLE;
			}
		}
		return false;
	}
	
	public void setCallback(Runnable callback) {
		this.callback = callback;
	}
	
	public void disable() {
		this.state = ButtonState.DISABLED;
	}
	public void enable() {
		this.state = ButtonState.IDDLE;
	}
	
	public ButtonState getState() {
		return this.state;
	}
}
