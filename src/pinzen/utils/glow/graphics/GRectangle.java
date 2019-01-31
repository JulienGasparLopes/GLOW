package pinzen.utils.glow.graphics;

import pinzen.utils.glow.Color;
import pinzen.utils.mathsfog.Vertex2f;

public class GRectangle extends GObject{

	protected Vertex2f bounds;
	
	/**
	 * Create a graphical Rectangle
	 * @param bounds : Vertex2f(width, height)
	 * @param c : color
	 */
	public GRectangle(Vertex2f bounds, Color c) {
		super();
		
		this.bounds = bounds.clone();
		float w = bounds.x;
		float h = bounds.y;
		
		float[] vertices = new float[] {
				0, 0, 0,
				0, h, 0,
				w, h, 0,
				w, 0, 0
		};
		float[] colors = Color.createColorArray(c, 4);
		float[] texCoord = new float[] {
				0, 0,
				0, 1,
				1, 1,
				1, 0
		};
		int[] indices = new int[] {
				0, 1, 3,
				1, 2, 3
		};
		
		this.initialize(vertices, colors, texCoord, indices);
	}
	
	/**
	 * Create a graphical Rectangle (Color set to WHITE)
	 * @param bounds : Vertex2f(width, height)
	 */
	public GRectangle(Vertex2f bounds) {
		this(bounds,Color.WHITE);
	}
	
		/** ----- ----- Constructor using width and height ----- ----- **/
	
	/**
	 * Create a graphical Rectangle
	 * @param w : width
	 * @param h : height
	 * @param c : color
	 */
	public GRectangle(float w, float h, Color c) {
		this(new Vertex2f(w,h),c);
	}
	
	/**
	 * Create a graphical Rectangle (Color set to WHITE)
	 * @param w : width
	 * @param h : height
	 */
	public GRectangle(float w, float h) {
		this(w,h,Color.WHITE);
	}
	
	/**
	 * Get a copy of this GRectangle's bounds
	 */
	public Vertex2f getDimension() {
		return this.bounds.clone();
	}
}
