package pinzen.utils.glow.textures;

public class TextureMap extends Texture {

	private int nbTileWidth, nbTileHeight;
	
	public TextureMap(String path, int nbTileWidth, int nbTileHeight) {
		super(path);
		this.nbTileWidth = nbTileWidth;
		this.nbTileHeight = nbTileHeight;
		
		if(this.width % nbTileWidth != 0 || this.height % nbTileHeight != 0) {
			throw new RuntimeException("File dimensions not compatible with TextureMap dimensions");
		}
	}
	
	public float[] getTexCoord(int x, int y) {
		float dx = 1f/(float)nbTileWidth;
		float dy = 1f/(float)nbTileHeight;
		
		return new float[] {
				x*dx, y*dy,
				x*dx, (y+1)*dy,
				(x+1)*dx, (y+1)*dy,
				(x+1)*dx, y*dy
		};
	}
}
