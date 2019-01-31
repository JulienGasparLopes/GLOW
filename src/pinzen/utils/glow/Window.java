package pinzen.utils.glow;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.opengl.GL;


public class Window {
	
	/**
	 * Window id in OpenGL
	 */
	protected long windowId;
	
	protected int width, height;

	/**
	 * Create a new GLFW window
	 * @param title : String shown on top of the window
	 * @param width : width of window
	 * @param height : height of window
	 * @param resizable : can the window be resized ?
	 */
	public Window(String title, int width, int height, boolean resizable) {
		this.width = width;
		this.height = height;
		
		//Init GLFW and check result
		if(!glfwInit())
			throw new IllegalStateException("Unable to initialize glfw");
		
		//Set resizable
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);
		
		//Create window and store its Id
		this.windowId = glfwCreateWindow(width, height, title, NULL, NULL);
        if (this.windowId == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        
        //Set callback for resize event
        glfwSetFramebufferSizeCallback(this.windowId, this::ResizeCallBack);
                
        //Set this window as actual context and create its capabilities
        //(add OpenGL functionalities)
        glfwMakeContextCurrent(this.windowId);
        GL.createCapabilities();
        
		//Enable alpha
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        //Set background color
        this.setBackground(Color.BLACK);
	}
	
	/**
	 * Delete this window from OpenGL
	 */
	public void dispose() {
		glfwDestroyWindow(this.windowId);
		this.windowId = NULL;
	}
	
	/**
	 * Center this window in user screen
	 */
	public void center() {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	/**
	 * Set window opacity
	 * @param opacity : opacity between 0 and 1
	 */
	public void setOpacity(float opacity) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	/**
	 * Show or hide window decoration (top bar with close icon)
	 * @param showDecoration : false if decoration should be hidden
	 */
	public void showDecoration(boolean showDecoration) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	/**
	 * Set this window title (Text to show on top of the window)
	 * @param title : text to show
	 */
	public void setTitle(String title) {
        glfwSetWindowTitle(this.windowId, title);
	}
	
	/**
	 * Set background Color <br>
	 * (window should be bound first using "setActual")
	 * @param c : color of the background
	 */
	public void setBackground(Color c) {
		glClearColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}
	
	/**
	 * Set this window the current one in OpenGL<br>
	 * The next OpenGL functions will now affect this window
	 */
	public void setCurrent() {
        glfwMakeContextCurrent(this.windowId);
	}
	
	/**
	 * Tells if this windows has been disposed
	 * @return : true if window is disposed
	 */
	public boolean isDisposed() {
		return this.windowId == NULL;
	}
	
	/**
	 * Get OpenGL Id
	 * @return : OpenGL Id
	 */
	public long getId() {
		return this.windowId;
	}
		
	/**
	 * Function called on window resizing
	 * @param window : OpenGL id of the window
	 * @param width : new width of window
	 * @param height : new height of window
	 */
    protected void ResizeCallBack(long window, int width, int height) {
    	/*
        if (width == 0 || height == 0) {
            return;
        }

        float f = height / (float)width;
        
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0, width, height, 0.0, -1.0, 1.0);
        glMatrixMode(GL_MODELVIEW);
        */
    	this.width = width;
    	this.height = height;
        glViewport(0, 0, width, height);
    }
    
    /**
     * Get window width
     * @return : window width
     */
    public int getWidth() {
    	return this.width;
    }
    /**
     * Get window height
     * @return : window Height
     */
    public int getHeight() {
    	return this.height;
    }
}
