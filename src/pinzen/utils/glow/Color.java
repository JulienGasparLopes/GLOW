package pinzen.utils.glow;

import pinzen.utils.mathsfog.Vertex3f;
import pinzen.utils.mathsfog.Vertex4f;

public class Color{
	
	public static final Color RED = new Color(1,0,0),
							  GREEN = new Color(0,1,0),
							  BLUE = new Color(0,0,1),
							  
							  YELLOW = new Color(1,1,0),
							  PURPLE = new Color(1,0,1),
							  CYAN = new Color(0,1,1),
							  
							  WHITE = new Color(1,1,1),
							  BLACK = new Color(0,0,0),
							  GRAY = new Color(.5f,.5f,.5f);
	
			/** ----- ----- Constructors ----- ----- **/
	
	private float r, g, b, a;
	
	/**
	 * Create a color using r,g,b and alpha components
	 * @param r : red component between 0 and 1
	 * @param g : green component between 0 and 1
	 * @param b : blue component between 0 and 1
	 * @param a : alpha component between 0 and 1
	 */
	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	/**
	 * Create a color using r,g,b (alpha set to 1)
	 * @param r : red component between 0 and 1
	 * @param g : green component between 0 and 1
	 * @param b : blue component between 0 and 1
	 */
	public Color(float r, float g, float b) {
		this(r,g,b,1f);
	}
	
	/**
	 * Create a gray scale color (r,g and b components equals, alpha to 1)
	 * @param c : gray component between 0 and 1
	 */
	public Color(float c) {
		this(c,c,c);
	}
	
			/** ----- ----- Functions ----- ----- **/
	
	/**
	 * Convert Color to Vertex4f
	 * @return Vertex4f(r,g,b,a)
	 */
	public Vertex4f toVertex4f() {
		return new Vertex4f(r,g,b,a);
	}
	/**
	 * Convert Color to Vertex3f
	 * @return Vertex3f(r,g,b)
	 */
	public Vertex3f toVertex3f() {
		return new Vertex3f(r,g,b);
	}
	
			/** ----- ----- Static Functions ----- ----- **/
	
	/**
	 * Create an array of float with components of a unique color
	 * Useful to send to OpenGL an array of the same color (for rendering)
	 * @param c : Color to populate the array with
	 * @param nbVertices : number of time color should be added
	 * @return an array of float [r,g,b,a,r,g,b,a,r...a]
	 */
	public static float[] createColorArray(Color c, int nbVertices) {
		float[] array = new float[nbVertices*4];
		for(int i = 0; i<nbVertices; i++) {
			array[i*4 + 0] = c.r;
			array[i*4 + 1] = c.g;
			array[i*4 + 2] = c.b;
			array[i*4 + 3] = c.a;
		}
		return array;
	}
	
			/** ----- ----- Getters ----- ----- **/
	
	/**
	 * Get red component (between 0 and 1)
	 * @return red component
	 */
	public float getRed() {
		return r;
	}
	/**
	 * Get green component (between 0 and 1)
	 * @return green component
	 */
	public float getGreen() {
		return g;
	}
	/**
	 * Get blue component (between 0 and 1)
	 * @return blue component
	 */
	public float getBlue() {
		return b;
	}
	/**
	 * Get alpha component (between 0 and 1)
	 * @return alpha component
	 */
	public float getAlpha() {
		return a;
	}
}
