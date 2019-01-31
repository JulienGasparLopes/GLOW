package pinzen.utils.glow.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import pinzen.utils.glow.textures.Texture;
import pinzen.utils.mathsfog.Vertex2f;

public class GString extends GBase{

	private int fontSize;
	private String text;
	private Texture texture;
	private GRectangle rectangle;
	
	public GString(String txt, int size) {
		this.text = txt;
		this.fontSize = size;
		generateTexture();
	}
	
	public void render() {
		this.texture.bind();
		this.rectangle.render();
	}
	
	private void generateTexture() {
	    try {
	    	Font font = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);
			
			BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = img.createGraphics();
			g.setFont(font);
			
			FontMetrics fm = g.getFontMetrics(font);
			int offset = fm.getAscent() - fm.getDescent() - fm.getLeading();
			int height = fm.getAscent();
			int width = (int)fm.getStringBounds(text, g).getWidth();
			
			img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			g = img.createGraphics();
			
			g.setFont(font);
			g.setColor(new Color(1,1,1,0));
			g.fillRect(0, 0, width, height);
			g.setColor(Color.WHITE);
			g.drawString(text, 0, offset);
			
			int[] pixels = new int[width * height];
			img.getRGB(0, 0, width, height, pixels, 0, width);

			ByteBuffer buffer = BufferUtils.createByteBuffer(width*height*4);
			for(int y = height-1; y>=0; y--) {
				for(int x = 0; x<width; x++) {
					int pixel = pixels[x + y*width];
					buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
	                buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green component
	                buffer.put((byte) (pixel & 0xFF));         // Blue component
	                buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component
				}
			}
			this.texture = new Texture(width, height, buffer);
			
			this.rectangle = new GRectangle(width,height);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getWidth() {
		return this.texture.getWidth();
	}
	public int getHeight() {
		return this.texture.getHeight();
	}
	public Vertex2f getDimension() {
		return new Vertex2f(getWidth(), getHeight());
	}
	
	public void setText(String txt) {
		this.text = txt;
		this.texture.dispose();
		this.rectangle.dispose();
		generateTexture();
	}
	public String getText() {
		return text;
	}
	
	public void dispose() {
		this.texture.dispose();
		this.rectangle.dispose();
	}
}
