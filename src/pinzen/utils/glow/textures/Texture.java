package pinzen.utils.glow.textures;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

public class Texture {

	public static final Texture DEFAULT = new Texture(1,1,BufferUtils.createByteBuffer(4).put((byte)-1).put((byte)-1).put((byte)-1).put((byte)-1));
	
	private int textureId;
	private String file;

	protected int height, width;
	
	private Texture() {
		this.textureId = glGenTextures();
		
		glBindTexture(GL_TEXTURE_2D, this.textureId);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);	
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	}
	
	public Texture(String imgPath) {
		this();
		
		this.file = imgPath;
		
		ByteBuffer data = getDataFromImage(imgPath);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
	    glGenerateMipmap(GL_TEXTURE_2D);

		stbi_image_free(data);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public Texture(int w, int h, ByteBuffer data) {
		this();
		this.width = w;
		this.height = h;
		this.file = "from data";
		
		data.flip();
				
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
	    glGenerateMipmap(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public String toString() {
		return "Texture[file:" + file + ",width:" + width + ",height:" + height + "]";
	}
	
	private ByteBuffer getDataFromImage(String path) {
		ByteBuffer image;
        
		try (MemoryStack stack = stackPush()) {
			
			/* Prepare image buffers */
	        IntBuffer w = stack.mallocInt(1);
	        IntBuffer h = stack.mallocInt(1);
	        IntBuffer comp = stack.mallocInt(1);
	        
	        /* Load image */
	        stbi_set_flip_vertically_on_load(true);
	        image = stbi_load(path, w, h, comp, 4);
	        
	        if (image == null) {
	            throw new RuntimeException("Failed to load texture from <" + path
	            						 + "> | Reason : " + stbi_failure_reason());
	        }	        
	
	        /* Get width and height of image */
	        width = w.get();
	        height = h.get();
		}
		catch(Exception e) {
            throw new RuntimeException("Failed to load texture from <" + path
					 + "> | Reason : " + stbi_failure_reason());
		}
		
		return image;
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, this.textureId);
	}
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	
	public void dispose() {
		this.unbind();
		GL11.glDeleteTextures(textureId);
	}
}
