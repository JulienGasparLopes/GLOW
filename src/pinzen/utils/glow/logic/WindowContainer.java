package pinzen.utils.glow.logic;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glViewport;

import pinzen.utils.glow.Shader;
import pinzen.utils.glow.Window;
import pinzen.utils.glow.inputs.IMouseListener;
import pinzen.utils.glow.inputs.InputManager;
import pinzen.utils.glow.inputs.InputManager.MouseButton;
import pinzen.utils.glow.inputs.InputManager.MouseEvent;
import pinzen.utils.mathsfog.Matrix4f;
import pinzen.utils.mathsfog.Vertex2f;

public final class WindowContainer extends Window implements IMouseListener{

	private String title;

	//Menu
	private Menu actualMenu;
	
	//Shader variables
	private Shader shader;
	private Matrix4f projection, view;
	
	//InputManager
	private InputManager inputs;
	
	//FPS variables
	private int maxFPS;
	private boolean showFPSOnTitle;
	private int actualFPS, counterFPS;
	private long lastFrame, delta, counterSecond, now;
				
	public WindowContainer(String title, int width, int height, boolean resizable) {
		super(title, width, height, resizable);
		this.title = title;
		
		//Default FPS value
		this.maxFPS = 50;
		this.lastFrame = System.currentTimeMillis();
		this.counterSecond = 0;
		
		//Default shader and matrices values
		this.shader = Shader.DEFAULT_SHADER;
		this.projection = Matrix4f.getOrtho(0, 0, width, height, 0, 1);
		this.view = new Matrix4f();
		
		//Default InputManager
		this.inputs = new InputManager(this.windowId);
		inputs.updateWindowSize(width, height);
		inputs.addMouseListener(this);
		
		//Set callback function for window size modification
		glfwSetFramebufferSizeCallback(this.windowId, this::windowResizeCallback);
	}
	
	/**
	 * Update the window (check events and input, render and dispose if needed)
	 * @return true if update has been done, false if window was disposed (are is already disposed)
	 */
	public boolean update() {
		if(!this.isDisposed()) {
			//Check if window should be closed
			if(glfwWindowShouldClose(this.windowId)) {
				this.dispose();
				return false;
			}
			
			//Update events
			glfwPollEvents();
			
			//Update FPS
			{
				this.now = System.currentTimeMillis();
				this.delta = now - lastFrame;
				//10 frames where skipped, we mustn't update nor render
				if(delta >= (1000/maxFPS)*10) {
					this.lastFrame = now;
				}
				//Render only if last render occur more than 1000/FPS millis ago
				else if(delta >= (1000/maxFPS)) {
					//Update FPS counters
					this.counterFPS++;
					this.lastFrame = now;
					this.counterSecond += delta;
					if(this.counterSecond >= 1000) {
						this.actualFPS = this.counterFPS;
						this.counterFPS = 0;
						if(showFPSOnTitle)
							this.setTitle(this.title + " [FPS:" + actualFPS + "]");
					}
					
					glfwSwapBuffers(this.windowId);
					
					
					//Update 
					if(this.actualMenu != null) {
						this.actualMenu._updateMenu(delta);
					}
					else
						throw new RuntimeException("No menu was set for the window");
					
					//Render
					this.render();
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
		
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT);
		
		shader.use();
		shader.setUniformMatrix4f("view",view);
		shader.setUniformMatrix4f("projection", projection);
		
		this.actualMenu._renderMenu(this.shader);
	}
	
	private void windowResizeCallback(long winId, int width, int height) {
		this.width = width;
		this.height = height;
		
		this.inputs.updateWindowSize(width, height);
		this.projection = Matrix4f.getOrtho(0, 0, width, height, 0, 1);
		glViewport(0, 0, width, height);
	}
	
	public void setMenu(Menu m) {
		if(this.actualMenu != m) {
			//Remove mouse listener
			if(this.actualMenu != null) {
				this.inputs.removeMouseListener(actualMenu);
			}
						
			//Set the new menu
			this.actualMenu = m;
			this.inputs.addMouseListener(actualMenu);
		}
	}
	
	
	public void setShader(Shader s) {
		this.shader = s;
	}
	public void sendModelMatrix(Matrix4f model) {
		shader.setUniformMatrix4f("model",model);
	}
	
	
	public InputManager getInputs() {
		return this.inputs;
	}
	
	public void showFPSOnTitle(boolean show) {
		this.showFPSOnTitle = show;
	}
	
	public void close() {
		glfwSetWindowShouldClose(this.windowId, true);
	}
	
	public void dispose() {
		super.dispose();
	}

	@Override
	public void _onMouseEvent(Vertex2f pos, MouseButton button, MouseEvent event) {
		//Actual menu is added on list of mouse listener, no need to do this here
	}
}
