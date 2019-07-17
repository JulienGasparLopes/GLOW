package pinzen.utils.glow.logic;

import static org.lwjgl.opengl.GL11.*;

import pinzen.utils.mathsfog.Matrix4f;

public class ApplicationWindow2D extends ApplicationWindow{

	public ApplicationWindow2D(String title, int width, int height, boolean resizable) {
		super(title, width, height, resizable);
		
		this.projection = Matrix4f.getOrtho(0, 0, width, height, 0, 1);
	}

	@Override
	protected void render() {
		glClear(GL_COLOR_BUFFER_BIT);
		
		shader.use();
		shader.setUniformMatrix4f("projection", projection);
		this.currentMenu.render(this.shader);
		
		shaderGUI.use();
		shaderGUI.setUniformMatrix4f("projection", projection);
		this.currentMenu.renderGUIs(this.shaderGUI);
	}

	@Override
	protected void windowResizeCallback(long winId, int width, int height) {
		this.width = width;
		this.height = height;
		
		this.getInputs().updateWindowSize(width, height);
		this.projection = Matrix4f.getOrtho(0, 0, width, height, 0, 1);
		glViewport(0, 0, width, height);
	}

}
