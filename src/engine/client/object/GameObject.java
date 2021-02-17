package engine.client.object;

import org.joml.Vector2f;

import configurationutil.type.Configuration;
import engine.client.rendering.mesh.GameObjectTexture;


public class GameObject {
	
	private Vector2f position;
	private Vector2f scale;
	private GameObjectTexture mesh;
	private boolean hasUpdated;
	private int zIndex;
	
	public GameObject(Vector2f position, Vector2f scale, GameObjectTexture mesh, int index) {
		this.position = position;
		this.scale = scale;
		this.mesh = mesh;
		this.hasUpdated = false;
		
		if(zIndex < 0) {
			zIndex = 0;
		} else if(zIndex > 9) {
			zIndex = 9;
		} //clamp this
		
		this.zIndex = index;
	}
	
	public int getZIndex() {
		return this.zIndex;
	}
	
	public void setProcessed() {
		this.hasUpdated = false;
	}
	
	public void setPosition(Vector2f pos) {
		this.position = pos;
		this.hasUpdated = true;
	}
	
	public void setPosition(float x, float y) {
		this.setPosition(new Vector2f(x, y));
	}
	
	public void setScale(Vector2f scale) {
		this.scale = scale;
		this.hasUpdated = true;
	}
	
	public void setScale(float x, float y) {
		this.setScale(new Vector2f(x, y));
	}
	
	public void setMesh(GameObjectTexture texture) {
		this.mesh = null;
		this.mesh = texture;
		this.hasUpdated = true;
	}
	
	public Vector2f getPosition() {
		return this.position;
	}
	
	public Vector2f getScale() {
		return this.scale;
	}
	
	public GameObjectTexture getMesh() {
		return this.mesh;
	}
	
	public boolean hasUpdated() {
		return this.hasUpdated;
	}
	
	public void onClick(int button) {
		
	}

	public void process(Configuration config) {
		
	}	
}
