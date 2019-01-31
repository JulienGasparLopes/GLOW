package pinzen.utils.glow;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import pinzen.utils.mathsfog.Matrix4f;

public class Shader {

	/**
	 * Default Vertex Shader (mostly used by internal logic)
	 * Will be deported in a specific file in next versions
	 */
	private static final String DEFAULT_VERTEX_SHADER = "#version 330 core\r\n" + 
			"layout (location = 0) in vec3 position;\r\n" + 
			"layout (location = 1) in vec4 color;\r\n" + 
			"layout (location = 2) in vec2 textureCoord;\r\n" + 
			"\r\n" + 
			"uniform mat4 model;\r\n" +
			"uniform mat4 view;\r\n" +
			"uniform mat4 projection;\r\n" +
			"\r\n" + 
			"out vec2 TexCoord;\r\n" +
			"out vec4 Color;\r\n" +
			"\r\n" + 
			"void main()\r\n" + 
			"{\r\n" + 
			"    gl_Position = projection * view * model * vec4(position, 1.0);\r\n" + 
			"    TexCoord = textureCoord;\r\n" + 
			"    Color = color;\r\n" + 
			"}";
	
	/**
	 * Default Fragment Shader (mostly used by internal logic)
	 * Will be deported in a specific file in next versions
	 */
	private static final String DEFAULT_FRAGMENT_SHADER = "#version 330 core\r\n" + 
			"out vec4 FragColor;\r\n" + 
			"\r\n" + 
			"in vec2 TexCoord;\r\n" + 
			"in vec4 Color;\r\n" +
			"\r\n" + 
			"uniform sampler2D ourTexture;\r\n" + 
			"\r\n" +
			"void main()\r\n" + 
			"{\r\n" + 
			"    FragColor = Color * texture(ourTexture, TexCoord);\r\n" + 
			"}";
	
	/**
	 * Default Shader Program
	 * It uses colors, textures and position to render
	 * It uses view, model and projection matrices for coordinate system
	 */
	public static final Shader DEFAULT_SHADER = new Shader();
	
	private int programId;
	private int fragmentId, vertexId;
	
	/**
	 * Create default Shader
	 */
	private Shader() {
		//Create Vertex Shader from sources
		vertexId = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexId, Shader.DEFAULT_VERTEX_SHADER);
		glCompileShader(vertexId);
		
		if(glGetShaderi(vertexId, GL_COMPILE_STATUS) != 1)
			throw new RuntimeException("Unable to compile Vertex Shader");
				
		//Create Fragment Shader from sources
		fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentId, Shader.DEFAULT_FRAGMENT_SHADER);
		glCompileShader(fragmentId);
		
		if(glGetShaderi(fragmentId, GL_COMPILE_STATUS) != 1)
			throw new RuntimeException("Unable to compile Fragment Shader");
		
		
		//Create Program and link Shaders
		programId = glCreateProgram();
		glAttachShader(programId, vertexId);
		glAttachShader(programId, fragmentId);
		glLinkProgram(programId);
		
		if(glGetProgrami(programId, GL_LINK_STATUS) != 1)
			throw new RuntimeException("Unable to link Fragment and Vertex Shader");
	
		glDeleteShader(this.fragmentId);
		glDeleteShader(this.vertexId);
	}
	
	/**
	 * Create a Shader program using external files
	 * @param vertexPath : path to the Vertex Shader file
	 * @param fragmentPath : path to the Fragment Shader file
	 */
	public Shader(String vertexPath, String fragmentPath) {
		String vertexSource = "";
		try {
			BufferedReader buffVert = new BufferedReader(new FileReader(new File(vertexPath)));
			String st;
			while((st = buffVert.readLine()) != null)
				vertexSource += st;
			buffVert.close();
		}
		catch(Exception e) {
			System.out.println("Unable to find vertex file <" + vertexPath + ">");
		}
		
		String fragmentSource = "";
		try {
			BufferedReader buffFrag = new BufferedReader(new FileReader(new File(fragmentPath)));
			String st;
			while((st = buffFrag.readLine()) != null)
				fragmentSource += st;
			buffFrag.close();
		}
		catch(Exception e) {
			System.out.println("Unable to find fragment file <" + vertexPath + ">");
		}
		
		//Create Vertex Shader from sources
		vertexId = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexId, vertexSource);
		glCompileShader(vertexId);
		
		if(glGetShaderi(vertexId, GL_COMPILE_STATUS) != 1)
			throw new RuntimeException("Unable to compile Vertex Shader");
				
		//Create Fragment Shader from sources
		fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentId, fragmentSource);
		glCompileShader(fragmentId);
		
		if(glGetShaderi(fragmentId, GL_COMPILE_STATUS) != 1)
			throw new RuntimeException("Unable to compile Fragment Shader");
		
		
		//Create Program and link Shaders
		programId = glCreateProgram();
		glAttachShader(programId, vertexId);
		glAttachShader(programId, fragmentId);
		glLinkProgram(programId);
		
		if(glGetProgrami(programId, GL_LINK_STATUS) != 1)
			throw new RuntimeException("Unable to link Fragment and Vertex Shader");
	
		glDeleteShader(this.fragmentId);
		glDeleteShader(this.vertexId);
	}
	
	/**
	 * Bind this Shader Program in order to use it for incoming renderings
	 */
	public void use() {
		glUseProgram(this.programId);
	}
	
	/**
	 * Dispose Shader Program (detach ressources from OpenGL)
	 */
	public void dispose() {
		glDeleteProgram(this.programId);
	}
	
	/**
	 * Send Matrix4f as uniform to Shader
	 * @param name : name of the uniform in the Shader files
	 * @param m : Matrix4f to send
	 */
	public void setUniformMatrix4f(String name, Matrix4f m) {
		int uniformId = glGetUniformLocation(this.programId, name);
		glUniformMatrix4fv(uniformId, false, m.toArray());
	}
}
