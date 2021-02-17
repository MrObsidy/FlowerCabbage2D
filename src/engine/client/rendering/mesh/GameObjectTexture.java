package engine.client.rendering.mesh;

import org.joml.Vector2f;
import org.joml.Vector4f;

import engine.client.rendering.texture.Texture;

public class GameObjectTexture {
	private final Vector2f[] textureCoordinates;
	private final Vector4f color;
	private final Texture texture;
	private final boolean hasTexture;
	
	
	public GameObjectTexture(Texture texture, Vector2f[] textureCoordinates) {
		this.color = new Vector4f(1f, 1f, 1f, 1f);
		this.texture = texture;
		this.textureCoordinates = textureCoordinates;
		this.hasTexture = true;
	}
	
	public GameObjectTexture(Texture texture) {
		this.color = new Vector4f(1f, 1f, 1f, 1f);
		this.texture = texture;
		this.textureCoordinates = new Vector2f[] {
				new Vector2f(1, 1),
				new Vector2f(1, 0),	
				new Vector2f(0, 0),
				new	Vector2f(0, 1),
						
			};
		this.hasTexture = true;
	}
	
	public GameObjectTexture(Vector4f color) {
		this.color = color;
		this.texture = new Texture(99);
		this.textureCoordinates = new Vector2f[] {
				new Vector2f(1, 1),
				new Vector2f(0, 1),
				new Vector2f(0, 0),
				new	Vector2f(1, 0),
			};
		this.hasTexture = false;
	}
	
	public Vector2f[] getTextureCoordinates() {
		return this.textureCoordinates;
	}
	
	public boolean isTextured() {
		return this.hasTexture;
	}
	
	public Vector4f getColor() {
		return this.color;
	}
	
	public Texture getTexture() {
		return this.texture;
	}
}
