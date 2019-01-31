package pinzen.utils.glow.textures;

import java.nio.ByteBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;

public class TextureManager {

	private static final String DEFAULT_TEXTURE_NAME = "GLOW_DEFAULT_TEXTURE";
	
	
	private boolean isDisposed;
	private String root;
	private HashMap<String, Texture> textures;
	
	public TextureManager(String pathToRoot) {
		this.isDisposed = false;
		this.root = pathToRoot;
		this.textures = new HashMap<String, Texture>();
		
		//Verify if root ends with '/' or '\'
		if(! (root.endsWith("/") || root.endsWith("\\")))
				root += "/";
		
		//Add default texture (white square 1x1)
		ByteBuffer buff = BufferUtils.createByteBuffer(4).put((byte)-1)
							 .put((byte)-1).put((byte)-1).put((byte)-1);
		this.textures.put(DEFAULT_TEXTURE_NAME, new Texture(1,1,buff));
	}
	
	public void bindTexture(String name) {
		this.bindTexture("", name);
	}
	
	public void bindTexture(String subFolder, String name) {
		if(isDisposed)
			throw new RuntimeException("Texture Manager is already disposed");
		
		//Verify if subFolder ends with '/' or '\\' or is empty
		if(!(subFolder.endsWith("/") || subFolder.endsWith("\\")) && subFolder != "")
			subFolder += "/";
		
		//Texture already exists
		if(textures.containsKey(name)) {
			textures.get(name).bind();
		}
		//Texture doesn't exists, try creation
		else {
			try {
				//Create Texture and return it
				Texture t = new Texture(root + subFolder + name + ".png");
				textures.put(name, t);
				t.bind();
			}
			catch(Exception e) {
				//Return default texture
				this.bindDefaultTexture();
			}
		}
	}
	
	public void bindDefaultTexture() {
		if(isDisposed)
			throw new RuntimeException("Texture Manager is already disposed");
		
		textures.get(DEFAULT_TEXTURE_NAME).bind();
	}
	
	public void dispose() {
		for(Texture t : textures.values()) {
			t.dispose();
		}
	}
	
	
		/** ----- ----- Getters ----- ----- **/
	
	public boolean isDisposed() {
		return this.isDisposed;
	}
	
	public int getTextureTotal() {
		return this.textures.size();
	}
}
