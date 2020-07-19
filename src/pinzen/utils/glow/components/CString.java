package pinzen.utils.glow.components;

import pinzen.utils.glow.ShaderProgram;
import pinzen.utils.glow.graphics.GString;
import pinzen.utils.glow.inputs.InputManager;
import pinzen.utils.glow.inputs.InputManager.MouseButton;
import pinzen.utils.glow.inputs.InputManager.MouseEvent;
import pinzen.utils.mathsfog.Matrix4f;
import pinzen.utils.mathsfog.Vertex2f;

public class CString extends Component{

	private GString gString;
	private Vertex2f position;
	private Matrix4f model;
	private boolean centerHoriz, centerVert;
	
	public CString(String text, int size, float x, float y) {
		this.gString = new GString(text, size);
		this.position = new Vertex2f(x, y);
		this.centerHoriz = false;
		this.centerVert = false;
		
		this.updateModelMatrix();
	}
	
	private void updateModelMatrix() {
		float x = position.x - (this.centerHoriz ? gString.getWidth()/2f : 0f);
		float y = position.y - (this.centerVert ? gString.getHeight()/2f : 0f);
		
		this.model = Matrix4f.getTranslationMatrix(new Vertex2f(x, y));
	}
	
	public void setText(String text) {
		this.gString.setText(text);
		
		this.updateModelMatrix();
	}
	
	@Override
	public void update(InputManager inputs, long delta) {}

	@Override
	public void render(ShaderProgram s) {
		s.setUniformMatrix4f("model", model);
		gString.render();
	}

	@Override
	public boolean onMouseEvent(Vertex2f pos, MouseButton button, MouseEvent event) {
		return false;
	}
	
	public void center(boolean horizontaly, boolean verticaly) {
		this.centerHoriz = horizontaly;
		this.centerVert = verticaly;
		this.updateModelMatrix();
	}
	
	public void setPosition(Vertex2f pos) {
		this.position = pos.clone();
		this.updateModelMatrix();
	}
	
	public void setPosition(float x, float y) {
		this.position = new Vertex2f(x, y);
		this.updateModelMatrix();
	}
	
	public Vertex2f getBounds() {
		return this.gString.getDimension();
	}
}
