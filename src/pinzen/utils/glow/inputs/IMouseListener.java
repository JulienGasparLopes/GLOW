package pinzen.utils.glow.inputs;

import pinzen.utils.glow.inputs.InputManager.MouseButton;
import pinzen.utils.glow.inputs.InputManager.MouseEvent;
import pinzen.utils.mathsfog.Vertex2f;

public interface IMouseListener {

	public void onMouseEvent(Vertex2f pos,
							 MouseButton button,
							 MouseEvent event);
}
