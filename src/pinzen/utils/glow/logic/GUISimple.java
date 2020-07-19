package pinzen.utils.glow.logic;

import pinzen.utils.glow.ShaderProgram;
import pinzen.utils.glow.inputs.InputManager.MouseButton;
import pinzen.utils.glow.inputs.InputManager.MouseEvent;
import pinzen.utils.mathsfog.Vertex2f;

public class GUISimple extends GUI{

	public GUISimple(Menu m, float x, float y, float width, float height) {
		super(m, x, y, width, height);
		this.setCatchClickBehavior(false);
	}
	
	public GUISimple(Menu m, Vertex2f pos, Vertex2f bounds) {
		super(m, pos, bounds);
		this.setCatchClickBehavior(false);
	}
	
	public GUISimple(Menu m) {
		super(m);
		this.setCatchClickBehavior(false);
	}

	@Override
	public void onMouseEvent(Vertex2f pos, MouseButton button, MouseEvent event) {

	}

	@Override
	protected void init() {
		
	}

	@Override
	protected void onShow() {
		
	}

	@Override
	public void render(ShaderProgram s) {
		
	}

	@Override
	public void update(long delta) {
		
	}

	@Override
	protected void onHide() {
		
	}
}
