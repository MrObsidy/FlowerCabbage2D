package engine.client.rendering.texture;

import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

public class Texture {
	private final String filePath;
	private final int ID;
	private final int WIDTH;
	private final int HEIGHT;
	
	public Texture(String filePath) {
		
		this.filePath = filePath;
		this.ID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, this.ID);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		
		STBImage.stbi_set_flip_vertically_on_load(true);
		
		ByteBuffer img = STBImage.stbi_load(this.filePath, width, height, channels, 0);
		
		this.WIDTH = width.get(0);
		this.HEIGHT = height.get(0);
		
		if(img != null) {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, img);
		} else {
			throw new RuntimeException("File not found: " + this.filePath);
		}
		STBImage.stbi_image_free(img);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public Texture(int id) {
		this.ID = id;
		this.WIDTH = 0;
		this.HEIGHT = 0;
		this.filePath = "";
	}
	
	public int getWidth() {
		return this.WIDTH;
	}
	
	public int getHeight() {
		return this.HEIGHT;
	}
	
	public int getID() {
		return this.ID;
	}
}
