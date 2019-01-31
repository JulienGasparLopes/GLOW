package pinzen.utils.glow.graphics;

/**
 * Graphical base
 */
public abstract class GBase {

	/**
	 * Render (position transformation should be done before)
	 */
	public abstract void render();
	
	/**
	 * Dispose the graphical resources
	 */
	public abstract void dispose();
}
