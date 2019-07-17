package pinzen.utils.glow.inputs;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_HIDDEN;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

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
	
	@SuppressWarnings("unused")
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
		
		//Set callback function for cursor click
		glfwSetMouseButtonCallback(window, this::mouseClickCallback);
		
		glfwSetKeyCallback(window, this::keyCallback);
	}
	
	private void cursorPosCallback(long winId, double x, double y) {
		this.cursorPosition.x = (float)x;
		this.cursorPosition.y = (float)(height == 0 ? y : height - y);
	}
	
	private void mouseClickCallback(long winId, int button, int action, int mods){		
		this.cursorPosition = getcursorPos();
				
		this.listeners.addAll(listenersToAdd);
		this.listeners.removeAll(listenersToRemove);
		this.listenersToAdd.clear();
		this.listenersToRemove.clear();
		
		if(button == MOUSE_BUTTON_LEFT) {
			if(action == ACTION_PRESSED) {
				//Update last press event position
				this.leftClickPosition = this.cursorPosition.clone();
				for(IMouseListener listen : listeners)
					listen.onMouseEvent(this.cursorPosition, MouseButton.LEFT, MouseEvent.PRESSED);
			}
			else {
				//If release happened next to press event fire click event
				if(leftClickPosition != null && Vertex2f.difference(cursorPosition, leftClickPosition).getNorm() <= MAX_CLICK_DIST) {
					for(IMouseListener listen : listeners)
						listen.onMouseEvent(this.leftClickPosition, MouseButton.LEFT, MouseEvent.CLICKED);
				}
				//Send release event
				for(IMouseListener listen : listeners)
					listen.onMouseEvent(this.cursorPosition, MouseButton.LEFT, MouseEvent.RELEASED);

				//remove button pressed position
				this.leftClickPosition = null;
			}
		}
		else {
			if(action == ACTION_PRESSED) {
				//Update last press event position
				this.rightClickPosition = this.cursorPosition.clone();
				for(IMouseListener listen : listeners)
					listen.onMouseEvent(this.cursorPosition, MouseButton.RIGHT, MouseEvent.PRESSED);
			}
			else {
				//If release happened next to press event fire click event
				if(Vertex2f.difference(cursorPosition, rightClickPosition).getNorm() <= MAX_CLICK_DIST) {
					for(IMouseListener listen : listeners)
						listen.onMouseEvent(this.rightClickPosition, MouseButton.RIGHT, MouseEvent.CLICKED);
				}
				//Send release event
				for(IMouseListener listen : listeners)
					listen.onMouseEvent(this.cursorPosition, MouseButton.RIGHT, MouseEvent.RELEASED);
				
				//Remove button pressed position
				this.rightClickPosition = null;
			}
		}
	}
	
	private void keyCallback(long winId, int key, int scancode, int action, int mods) {
		if(action == GLFW_PRESS) {
			if(this.keysState.containsKey(Key.fromGlfwKey(key))) {
				this.keysState.get(Key.fromGlfwKey(key)).keyDown();
			}
		}
		else if(action == GLFW_RELEASE) {
			if(this.keysState.containsKey(Key.fromGlfwKey(key))) {
				this.keysState.get(Key.fromGlfwKey(key)).keyUp();
			}
		}
	}
	
	public void addMouseListener(IMouseListener listen) {
		this.listenersToAdd.add(listen);
	}
	
	public void removeMouseListener(IMouseListener listen) {
		this.listenersToRemove.add(listen);
	}
	
	public void updateWindowSize(int w, int h) {
		this.width = w;
		this.height = h;
	}
	
	public Vertex2f getcursorPos() {
		DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(this.windowId, xBuffer, yBuffer);
		float x = (float)xBuffer.get(0);
		float y = (float)(height == 0 ? yBuffer.get(0) : height - yBuffer.get(0));
		return new Vertex2f(x, y);
	}
	
	public Vertex2f centerCursor() {
		glfwSetCursorPos(this.windowId, this.width/2f, this.height/2f);
		return new Vertex2f(this.width/2, this.height/2);
	}
	
	public void showCursor() {
		glfwSetInputMode(this.windowId, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
	}
	public void hideCursor() {
		glfwSetInputMode(this.windowId, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
	}
	
	public boolean isLeftDown() {
		return isKeyDown(Key.Q) || isKeyDown(Key.LEFT);
	}
	
	public boolean isRightDown() {
		return isKeyDown(Key.D) || isKeyDown(Key.RIGHT);
	}
	
	public boolean isUpDown() {
		return isKeyDown(Key.Z) || isKeyDown(Key.UP);
	}
	
	public boolean isDownDown() {
		return isKeyDown(Key.S) || isKeyDown(Key.DOWN);
	}
	
	public boolean isSpaceDown() {
		return isKeyDown(Key.SPACE);
	}
	
	public boolean isKeyDown(Key k) {
		return this.keysState.get(k).isDown;
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
