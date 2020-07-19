package pinzen.utils.glow.components;

import pinzen.utils.glow.ShaderProgram;
import pinzen.utils.glow.inputs.InputManager;
import pinzen.utils.glow.inputs.InputManager.MouseButton;
import pinzen.utils.glow.inputs.InputManager.MouseEvent;
import pinzen.utils.mathsfog.Vertex2f;

public abstract class Component{
	
	public abstract void update(InputManager inputs, long delta);
	
	public abstract void render(ShaderProgram s);
	
	public abstract boolean onMouseEvent(Vertex2f pos, MouseButton button, MouseEvent event);
}
