package engine.client.renderer.rendering;


import engine.client.object.GameObject;
import engine.client.rendering.scene.Scene;
import engine.client.rendering.shader.Shader;
import engine.client.rendering.texture.Texture;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.List;

public class RenderBatch {
	//Vertex
	//Pos			Color						Texture Coords	 Texture ID
	//float, float  float, float, float, float  float, float     float
	public static final int VERTEX_POSITION_SIZE = 2;
	public static final int VERTEX_COLOR_SIZE = 4;
	public static final int VERTEX_UV_SIZE = 2;
	public static final int VERTEX_TEXTURE_ID_SIZE = 1;
	public static final int VERTEX_POSITION_OFFSET = 0;
	public static final int VERTEX_POSITION_SIZE_RAW = VERTEX_POSITION_SIZE * Float.BYTES;
	public static final int VERTEX_COLOR_SIZE_RAW = VERTEX_COLOR_SIZE * Float.BYTES;
	public static final int VERTEX_UV_SIZE_RAW = VERTEX_UV_SIZE * Float.BYTES;
	public static final int VERTEX_COLOR_OFFSET = VERTEX_POSITION_OFFSET + VERTEX_POSITION_SIZE_RAW;
	public static final int VERTEX_UV_OFFSET = VERTEX_COLOR_OFFSET + VERTEX_COLOR_SIZE_RAW;
	public static final int VERTEX_TEXTURE_ID_SIZE_RAW = VERTEX_TEXTURE_ID_SIZE * Float.BYTES;
	public static final int VERTEX_TEXTURE_ID_OFFSET = VERTEX_UV_OFFSET + VERTEX_UV_SIZE_RAW;
	public static final int VERTEX_SIZE = VERTEX_POSITION_SIZE + VERTEX_COLOR_SIZE + VERTEX_UV_SIZE + VERTEX_TEXTURE_ID_SIZE;
	public static final int VERTEX_SIZE_RAW = VERTEX_SIZE * Float.BYTES;
	
	private final int MAX_MESH_COUNT;
	private final int VAO_ID, VBO_ID;
	private final GameObject[] objects;
	private final float[] vertices;
	
	private final int[] textureSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 99};
	
	private final List<Texture> textures = new ArrayList<Texture>();
	
	private int meshCount = 0;
	private Shader shader;

	
	public RenderBatch(int max, Shader shader) {
		this.MAX_MESH_COUNT = max;
		this.shader = shader;
		this.objects = new GameObject[max];
		this.vertices = new float[this.MAX_MESH_COUNT * VERTEX_SIZE * 4 /* a quad has 4 vertices */];
		
		this.VAO_ID = glGenVertexArrays();
		glBindVertexArray(this.VAO_ID);
		
		this.VBO_ID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, this.VBO_ID);
		glBufferData(GL_ARRAY_BUFFER, this.vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);
		
		int eboID = glGenBuffers();
		int[] indices = new int[MAX_MESH_COUNT * 6];
		
		int i = 0;
		int offset = 0;
		int indexOffset = 0;
		while(i < MAX_MESH_COUNT) {
			//3, 2, 0, 0, 2, 1
			offset = 4 * i;
			indexOffset = 6 * i;
			
			indices[indexOffset] = offset + 3;
			indices[indexOffset + 1] = offset + 2;
			indices[indexOffset + 2] = offset + 0;
			indices[indexOffset + 3] = offset + 0;
			indices[indexOffset + 4] = offset + 2;
			indices[indexOffset + 5] = offset + 1;
			
			i++;
		}
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		
		glVertexAttribPointer(0, VERTEX_POSITION_SIZE, GL_FLOAT, false, VERTEX_SIZE_RAW, VERTEX_POSITION_OFFSET);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, VERTEX_COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_RAW, VERTEX_COLOR_OFFSET);
		glEnableVertexAttribArray(1);
		
		glVertexAttribPointer(2, VERTEX_UV_SIZE, GL_FLOAT, false, VERTEX_SIZE_RAW, VERTEX_UV_OFFSET);
		glEnableVertexAttribArray(2);
		
		glVertexAttribPointer(3, VERTEX_TEXTURE_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_RAW, VERTEX_TEXTURE_ID_OFFSET);
		glEnableVertexAttribArray(3);
	}
	
	public boolean addMesh(GameObject object) {		
		if(this.meshCount >= this.MAX_MESH_COUNT) return false;
		if(this.textures.size() >= 16) return false;
		
		int index = this.meshCount;
		this.objects[index] = object;
		this.meshCount++;
		
		if(object.getMesh().isTextured()) {
			if(!this.textures.contains(object.getMesh().getTexture())) {
				this.textures.add(object.getMesh().getTexture());
			}
		}
		
		int offset = index * 4 * VERTEX_SIZE;
		
		float xCoord = 1f;
		float yCoord = 1f;
		
		int textureID = 0;
		
		if(object.getMesh().isTextured()) {
			for(int i = 0; i < this.textures.size(); i++) {
				if(textures.get(i) == object.getMesh().getTexture()) {
					textureID = i;
					break;
				}
			}
		} else {
			textureID = 99;
		}
		
		int vertex = 0;
		while(vertex < 4) {
			if(vertex == 1) yCoord = 0f;
			if(vertex == 2) xCoord = 0f;
			if(vertex == 3) yCoord = 1f;
			
			vertices[offset] = object.getPosition().x + (xCoord * object.getScale().x);
			vertices[offset + 1] = object.getPosition().y + (yCoord * object.getScale().y);
			
			vertices[offset + 2] = object.getMesh().getColor().x;
			vertices[offset + 3] = object.getMesh().getColor().y;
			vertices[offset + 4] = object.getMesh().getColor().z;
			vertices[offset + 5] = object.getMesh().getColor().w;
			
			vertices[offset + 6] = object.getMesh().getTextureCoordinates()[vertex].x;
			vertices[offset + 7] = object.getMesh().getTextureCoordinates()[vertex].y;
			
			vertices[offset + 8] = textureID;
					
			offset += VERTEX_SIZE;
			
			vertex++;
		}
		return true;
	}
	
	private void updateObject(GameObject object, int index) {
		if(object.getMesh().isTextured()) {
			if(!this.textures.contains(object.getMesh().getTexture())) {
				this.textures.add(object.getMesh().getTexture());
			}
		}
		
		int offset = index * 4 * VERTEX_SIZE;
		
		float xCoord = 1f;
		float yCoord = 1f;
		
		int textureID = 0;
		
		if(object.getMesh().isTextured()) {
			for(int i = 0; i < this.textures.size(); i++) {
				if(textures.get(i) == object.getMesh().getTexture()) {
					textureID = i;
					break;
				}
			}
		} else {
			textureID = 99;
		}
		
		int vertex = 0;
		while(vertex < 4) {
			if(vertex == 1) yCoord = 0f;
			if(vertex == 2) xCoord = 0f;
			if(vertex == 3) yCoord = 1f;
			
			vertices[offset] = object.getPosition().x + (xCoord * object.getScale().x);
			vertices[offset + 1] = object.getPosition().y + (yCoord * object.getScale().y);
			
			vertices[offset + 2] = object.getMesh().getColor().x;
			vertices[offset + 3] = object.getMesh().getColor().y;
			vertices[offset + 4] = object.getMesh().getColor().z;
			vertices[offset + 5] = object.getMesh().getColor().w;
			
			vertices[offset + 6] = object.getMesh().getTextureCoordinates()[vertex].x;
			vertices[offset + 7] = object.getMesh().getTextureCoordinates()[vertex].y;
			
			vertices[offset + 8] = textureID;
					
			offset += VERTEX_SIZE;
			
			vertex++;
		}
	}
	
	public void render() {
		
		int index = 0;
		for(GameObject object : this.objects) {
			if(object != null) {
				if(object.hasUpdated()) {
					this.updateObject(object, index);
					object.setProcessed();
				}
			}
			index++;
		}
				
		glBindBuffer(GL_ARRAY_BUFFER, VBO_ID);
		glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
		
		glUseProgram(shader.getProgramID());
		shader.uniformMatrix4f("uProjMatrix", Scene.getScene().getCamera().getProjectionMatrix());
		shader.uniformMatrix4f("uViewMatrix", Scene.getScene().getCamera().getViewMatrix());
		
		for(int i = 0; i < this.textures.size(); i++) {
			glActiveTexture(GL_TEXTURE0 + i);
			glBindTexture(GL_TEXTURE_2D, this.textures.get(i).getID());
		}
		
		shader.uniformIntArray("uTextures", textureSlots);
		
		glBindVertexArray(this.VAO_ID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDrawElements(Scene.drawMode, this.meshCount * 6, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		for(int i = 0; i < this.textures.size(); i++) {
			glActiveTexture(GL_TEXTURE0 + i);
			glBindTexture(GL_TEXTURE_2D, 0);
		}
		
		glBindVertexArray(0);
		
		glUseProgram(0);
	}
}