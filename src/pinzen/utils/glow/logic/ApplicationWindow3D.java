package pinzen.utils.glow.logic;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glViewport;

import pinzen.utils.mathsfog.Matrix4f;

public class ApplicationWindow3D extends ApplicationWindow{

	private Matrix4f projectionGUI;
	private float fov, zNear, zFar;
	
	public ApplicationWindow3D(String title, int width, int height, boolean resizable, float fieldOfView, float nearDistance, float viewDistance) {
		super(title, width, height, resizable);
		this.fov = fieldOfView;
		this.zNear = nearDistance;
		this.zFar = viewDistance;
		
		this.projection = Matrix4f.getPerspective(fov, (float)width/(float)height, zNear, zFar);
		this.projectionGUI = Matrix4f.getOrtho(0, 0, width, height, -1f, 1f);
	}

	@Override
	protected void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		shader.use();
		shader.setUniformMatrix4f("projection", projection);
		this.currentMenu.render(shader);
		
		shaderGUI.use();
		shaderGUI.setUniformMatrix4f("projection", projectionGUI);
		this.currentMenu.renderGUIs(shaderGUI);
	}

	@Override
	protected void windowResizeCallback(long winId, int newWidth, int newHeight) {
		this.width = newWidth;
		this.height = newHeight;
		
		this.projection = Matrix4f.getPerspective(fov, (float)width/(float)height, zNear, zFar);
		this.projectionGUI = Matrix4f.getOrtho(0, 0, width, height, -1f, 1f);
		glViewport(0, 0, width, height);
		this.getInputs().updateWindowSize(width, height);
	}

}
