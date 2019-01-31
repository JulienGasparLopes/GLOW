package pinzen.utils.glow.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;


public class GObject extends GBase{

	private int nbVertices;
	private int vaoId, vboId, eboId;
	
	private boolean useIndices;
	private int nbIndices;
	
	public GObject() {

	}
	
	public void initialize(float[] vertices, float[] colors, float[] texCoord, int[] indices) {
		this.useIndices = indices != null;
		generateVAO(vertices, colors, texCoord, indices);
	}
	
	public void initialize(float[] vertices, float[] colors, float[] texCoord) {
		this.initialize(vertices, colors, texCoord, null);
	}
	
	
	public void render() {
		glBindVertexArray(this.vaoId);
		
		//Active Texture0
		glActiveTexture(GL_TEXTURE0);
		
		//Draw elements or just arrays
		if(useIndices)
			glDrawElements(GL_TRIANGLES, nbIndices, GL_UNSIGNED_INT, 0);
		else
			glDrawArrays(GL_TRIANGLES, 0, nbVertices);
	}
	
	private void generateVAO(float[] vertices, float[] colors, float[] texCoord, int[] indices) {		
		//Generate data array that will be passed to the VBO
		nbVertices = vertices.length/3;
		float[] combinedVertices = new float[(3 + 4 + 2) * nbVertices];
		for(int i = 0; i<nbVertices; i++) {
			combinedVertices[i*9] = vertices[i*3];
			combinedVertices[i*9+1] = vertices[i*3 + 1];
			combinedVertices[i*9+2] = vertices[i*3 + 2];
			
			combinedVertices[i*9+3] = colors[i*4];
			combinedVertices[i*9+4] = colors[i*4 + 1];
			combinedVertices[i*9+5] = colors[i*4 + 2];
			combinedVertices[i*9+6] = colors[i*4 + 3];
			
			combinedVertices[i*9+7] = texCoord[i*2];
			combinedVertices[i*9+8] = texCoord[i*2 + 1];
		}
		
		//Generate VAO
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);
		
		//Create a VBO to store data (VBO will be stored in VAO)
		vboId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, combinedVertices, GL_STATIC_DRAW);
		
		//Create EBO if needed
		if(useIndices) {
			this.nbIndices = indices.length;
			
			eboId = glGenBuffers();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW); 
		}
		
		//Configure attrib pointers (layout = 0 for position)
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 9*Float.BYTES, 0);
		glEnableVertexAttribArray(0);
		
		//Configure attrib pointers (layout = 1 for color)
		glVertexAttribPointer(1, 4, GL_FLOAT, false, 9*Float.BYTES, 3*Float.BYTES);
		glEnableVertexAttribArray(1);
		
		//Configure attrib pointers (layout = 2 for texture)
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 9*Float.BYTES, 7*Float.BYTES);
		glEnableVertexAttribArray(2);
		
		
		//Unbind buffers
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	public void dispose() {
		glDeleteVertexArrays(vaoId);
		glDeleteBuffers(vboId);
		if(useIndices)
			glDeleteBuffers(eboId);
	}
}
