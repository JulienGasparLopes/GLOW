package pinzen.utils.glow.inputs;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.BufferUtils;

import pinzen.utils.mathsfog.Vertex2f;

public class InputManager {

	protected static boolean IS_QUERTY = true;
	
	public static enum MouseEvent {PRESSED, RELEASED, CLICKED};
	public static enum MouseButton {LEFT, RIGHT};
		
	private static final int MAX_CLICK_DIST = 0;
	
	private static final int MOUSE_BUTTON_LEFT = GLFW_MOUSE_BUTTON_LEFT,
							MOUSE_BUTTON_RIGHT = GLFW_MOUSE_BUTTON_RIGHT,
							ACTION_RELEASED = GLFW_RELEASE,
							ACTION_PRESSED = GLFW_PRESS;
	
	private class KeyPushState{
		public boolean isDown, wasConsumed;
		
		public KeyPushState() {
			this.isDown = false;
			this.wasConsumed = false;
		}
		
		public boolean consume() {
			if(isDown && !wasConsumed) {
				wasConsumed = true;
				return true;
			}
			else
				return false;
		}
		
		public void keyDown() {
			if(!isDown) {
				isDown = true;
				wasConsumed = false;
			}
		}
		
		public void keyUp() {
			isDown = false;
			wasConsumed = false;
		}
	}
	
	private long windowId;
	private int width, height;
	private Vertex2f cursorPosition, rightClickPosition, leftClickPosition;
	
	private ArrayList<IMouseListener> listeners, listenersToAdd, listenersToRemove;
	
	private HashMap<Key,KeyPushState> keysState;
	
	public InputManager(long window) {
		this.windowId = window;
		this.cursorPosition = new Vertex2f();
		this.width = 0;
		this.height = 0;
		
		this.listeners = new ArrayList<IMouseListener>();
		this.listenersToAdd = new ArrayList<IMouseListener>();
		this.listenersToRemove = new ArrayList<IMouseListener>();
		
		this.leftClickPosition = null;
		this.rightClickPosition = null;
		
		this.keysState = new HashMap<Key,KeyPushState>();
		for(Key k : Key.values()) {
			keysState.put(k, new KeyPushState());
		}
		
		//Set callback function for cursor position modification
		//glfwSetCursorPosCallback(window, this::cursorPosCallback);
		
		//Set callback function for cursor position modification
		glfwSetMouseButtonCallback(window, this::mouseClickCallback);
	}
	
	private void cursorPosCallback(long winId, double x, double y) {
		this.cursorPosition.x = (float)x;
		this.cursorPosition.y = (float)(height == 0 ? y : height - y);
	}
	
	private void mouseClickCallback(long winId, int button, int action, int mods){		
		this.cursorPosition = GetcursorPos();
				
		this.listeners.addAll(listenersToAdd);
		this.listeners.removeAll(listenersToRemove);
		this.listenersToAdd.clear();
		this.listenersToRemove.clear();
		
		if(button == MOUSE_BUTTON_LEFT) {
			if(action == ACTION_PRESSED) {
				//Update last press event position
				this.leftClickPosition = this.cursorPosition.clone();
				for(IMouseListener listen : listeners)
					listen._onMouseEvent(this.cursorPosition, MouseButton.LEFT, MouseEvent.PRESSED);
			}
			else {
				//If release happened next to press event fire click event
				if(leftClickPosition != null && Vertex2f.difference(cursorPosition, leftClickPosition).getNorm() <= MAX_CLICK_DIST) {
					for(IMouseListener listen : listeners)
						listen._onMouseEvent(this.leftClickPosition, MouseButton.LEFT, MouseEvent.CLICKED);
				}
				//Send release event
				for(IMouseListener listen : listeners)
					listen._onMouseEvent(this.cursorPosition, MouseButton.LEFT, MouseEvent.RELEASED);

				//remove button pressed position
				this.leftClickPosition = null;
			}
		}
		else {
			if(action == ACTION_PRESSED) {
				//Update last press event position
				this.rightClickPosition = this.cursorPosition.clone();
				for(IMouseListener listen : listeners)
					listen._onMouseEvent(this.cursorPosition, MouseButton.RIGHT, MouseEvent.PRESSED);
			}
			else {
				//If release happened next to press event fire click event
				if(Vertex2f.difference(cursorPosition, rightClickPosition).getNorm() <= MAX_CLICK_DIST) {
					for(IMouseListener listen : listeners)
						listen._onMouseEvent(this.rightClickPosition, MouseButton.RIGHT, MouseEvent.CLICKED);
				}
				//Send release event
				for(IMouseListener listen : listeners)
					listen._onMouseEvent(this.cursorPosition, MouseButton.RIGHT, MouseEvent.RELEASED);
				
				//Remove button pressed position
				this.rightClickPosition = null;
			}
		}
	}
	
	public void addMouseListener(IMouseListener listen) {
		this.listenersToAdd.add(listen);
	}
	
	public void removeMouseListener(IMouseListener listen) {
		this.listenersToRemove.remove(listen);
	}
	
	public void updateWindowSize(int w, int h) {
		this.width = w;
		this.height = h;
	}
	
	public Vertex2f GetcursorPos() {
		DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(this.windowId, xBuffer, yBuffer);
		float x = (float)xBuffer.get(0);
		float y = (float)(height == 0 ? yBuffer.get(0) : height - yBuffer.get(0));
		return new Vertex2f(x, y);
	}
	
	public boolean isLeftDown() {
		return isKeyDown(GLFW_KEY_A) || isKeyDown(GLFW_KEY_LEFT);
	}
	
	public boolean isRightDown() {
		return isKeyDown(GLFW_KEY_D) || isKeyDown(GLFW_KEY_RIGHT);
	}
	
	public boolean isUpDown() {
		return isKeyDown(GLFW_KEY_W) || isKeyDown(GLFW_KEY_UP);
	}
	
	public boolean isDownDown() {
		return isKeyDown(GLFW_KEY_S) || isKeyDown(GLFW_KEY_DOWN);
	}
	
	public boolean isSpaceDown() {
		return isKeyDown(GLFW_KEY_SPACE);
	}
	
	private boolean isKeyDown(int glwfKey) {
		return glfwGetKey(this.windowId, glwfKey) == GLFW_PRESS;
	}
	
	public boolean isKeyDown(Key k) {
		return isKeyDown(k.getKeyCode());
	}
	
	public boolean consumeKey(Key k) {
		KeyPushState state = this.keysState.get(k);
		if(isKeyDown(k))
			state.keyDown();
		else
			state.keyUp();
			
		return state.consume();
	}
	
	public boolean isMouseLeftDown() {
		return leftClickPosition != null;
	}
	
	public boolean isMouseRightDown() {
		return rightClickPosition != null;
	}
	
	public static void setAZERTYMode() {
		IS_QUERTY = false;
	}
	
	public static void setQUERTYMode() {
		IS_QUERTY = true;
	}
}
