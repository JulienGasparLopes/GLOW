package pinzen.utils.glow.inputs;

import static org.lwjgl.glfw.GLFW.*;

public enum Key {

	A(GLFW_KEY_A, GLFW_KEY_Q),
	B(GLFW_KEY_B, GLFW_KEY_B),
	C(GLFW_KEY_C, GLFW_KEY_C),
	D(GLFW_KEY_D, GLFW_KEY_D),
	E(GLFW_KEY_E, GLFW_KEY_E),
	F(GLFW_KEY_F, GLFW_KEY_F),
	G(GLFW_KEY_G, GLFW_KEY_G),
	H(GLFW_KEY_H, GLFW_KEY_H),
	I(GLFW_KEY_I, GLFW_KEY_I),
	J(GLFW_KEY_J, GLFW_KEY_J),
	K(GLFW_KEY_K, GLFW_KEY_K),
	L(GLFW_KEY_L, GLFW_KEY_L),
	M(GLFW_KEY_M, GLFW_KEY_SEMICOLON),
	N(GLFW_KEY_N, GLFW_KEY_N),
	O(GLFW_KEY_O, GLFW_KEY_O),
	P(GLFW_KEY_P, GLFW_KEY_P),
	Q(GLFW_KEY_Q, GLFW_KEY_A),
	R(GLFW_KEY_R, GLFW_KEY_R),
	S(GLFW_KEY_S, GLFW_KEY_S),
	T(GLFW_KEY_T, GLFW_KEY_T),
	U(GLFW_KEY_U, GLFW_KEY_U),
	V(GLFW_KEY_V, GLFW_KEY_V),
	W(GLFW_KEY_W, GLFW_KEY_Z),
	X(GLFW_KEY_X, GLFW_KEY_X),
	Y(GLFW_KEY_Y, GLFW_KEY_Y),
	Z(GLFW_KEY_Z, GLFW_KEY_W),
	ONE(GLFW_KEY_1, GLFW_KEY_1),
	TWO(GLFW_KEY_2, GLFW_KEY_2),
	THREE(GLFW_KEY_3, GLFW_KEY_3),
	FOUR(GLFW_KEY_4, GLFW_KEY_4),
	FIVE(GLFW_KEY_5, GLFW_KEY_5),
	SIX(GLFW_KEY_6, GLFW_KEY_6),
	SEVEN(GLFW_KEY_7, GLFW_KEY_7),
	HEIGHT(GLFW_KEY_8, GLFW_KEY_8),
	NINE(GLFW_KEY_9, GLFW_KEY_9),
	ZERO(GLFW_KEY_0, GLFW_KEY_0),
	SPACE(GLFW_KEY_SPACE),
	ESCAPE(GLFW_KEY_ESCAPE),
	LEFT(GLFW_KEY_LEFT),
	RIGHT(GLFW_KEY_RIGHT),
	UP(GLFW_KEY_UP),
	DOWN(GLFW_KEY_DOWN),
	LSHIFT(GLFW_KEY_LEFT_SHIFT),
	F1(GLFW_KEY_F1),
	F2(GLFW_KEY_F2),
	F3(GLFW_KEY_F3),
	F4(GLFW_KEY_F4),
	F5(GLFW_KEY_F5),
	F6(GLFW_KEY_F6),
	F7(GLFW_KEY_F7),
	F8(GLFW_KEY_F8),
	F9(GLFW_KEY_F9),
	F10(GLFW_KEY_F10),
	F11(GLFW_KEY_F11),
	F12(GLFW_KEY_F12);
	
	private int keyQUERTY, keyAZERTY;
	
	Key(int keyQUERTY, int keyAZERTY) {
		this.keyQUERTY = keyQUERTY;
		this.keyAZERTY = keyAZERTY;
	}
	
	Key(int keyBoth){
		this(keyBoth, keyBoth);
	}
	
	public int getKeyCode() {
		return InputManager.IS_QUERTY ? this.keyQUERTY : this.keyAZERTY;
	}
	
	public static Key fromGlfwKey(int key) {
		for(Key k : Key.values()) {
			if(key == k.keyAZERTY)
				return k;
		}
		return null;
	}
}
