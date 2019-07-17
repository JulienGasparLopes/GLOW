package pinzen.utils.glow.logic;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import pinzen.utils.glow.ShaderProgram;
import pinzen.utils.glow.Window;
import pinzen.utils.glow.inputs.IMouseListener;
import pinzen.utils.glow.inputs.InputManager;
import pinzen.utils.glow.inputs.InputManager.MouseButton;
import pinzen.utils.glow.inputs.InputManager.MouseEvent;
import pinzen.utils.mathsfog.Matrix4f;
import pinzen.utils.mathsfog.Vertex2f;

public abstract class ApplicationWindow extends Window implements IMouseListener{

	private String title;

	//Menu
	protected Menu currentMenu;
	
	//Shader variables
	protected ShaderProgram shader, shaderGUI;
	protected Matrix4f projection;
	
	//InputManager
	private InputManager inputs;
	
	//FPS variables
	private int maxFPS;
	private boolean showFPSOnTitle;
	private int actualFPS, counterFPS;
	private long lastFrame, delta, counterSecond, now;
				
	public ApplicationWindow(String title, int width, int height, boolean resizable) {
		super(title, width, height, resizable);
		this.title = title;
		
		//Default FPS value
		this.maxFPS = 50;
		this.lastFrame = System.currentTimeMillis();
		this.counterSecond = 0;
		
		//Default shader and matrices values
		this.shader = ShaderProgram.DEFAULT_SHADER;
		
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
					if(this.currentMenu != null) {
						this.currentMenu.updateGUIs(delta);
						this.currentMenu.update(delta);
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
		
	protected abstract void render();
	
	protected abstract void windowResizeCallback(long winId, int width, int height);
	
	public void setMenu(Menu m) {
		if(this.currentMenu != m) {
			//Remove old menu
			if(this.currentMenu != null) {
				this.currentMenu.onHide();
			}
						
			//Set the new menu
			this.currentMenu = m;
		}
	}
	
	
	public void setMainShader(ShaderProgram s) {
		this.shader = s;
	}
	
	public void setGUIShader(ShaderProgram s) {
		this.shaderGUI = s;
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
	public void onMouseEvent(Vertex2f pos, MouseButton button, MouseEvent event) {
		boolean wasProcessed = this.currentMenu.processClickOnGUIs(pos, button, event);
		if(!wasProcessed) {
			this.currentMenu.onMouseEvent(pos, button, event);
		}
	}
}
