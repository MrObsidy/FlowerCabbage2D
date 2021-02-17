package engine.client.rendering.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector2f;

import engine.client.input.MouseHelper;
import engine.client.object.GameObject;
import engine.client.renderer.rendering.Renderer;
import engine.client.rendering.shader.Shader;

import static org.lwjgl.opengl.GL20.*;

public abstract class Scene {
	
	private static Scene INSTANCE;
	
	public static Scene getScene() {
		return INSTANCE;
	}
	
	private Renderer renderer;
	
	private Camera camera;
	
	public static int drawMode = GL_TRIANGLES;
	
	private Map<Integer, List<GameObject>> gameObjects = new HashMap<Integer, List<GameObject>>();
	
	public Scene() {
		for(int i = 0; i <= 9; i++) {
			gameObjects.put(i, new ArrayList<GameObject>());
		}
	}
	
	public void onClick(Vector2f coords, int mouseButton) {
		for(int i = 9; i >= 0; i--) {
			for(GameObject object : this.gameObjects.get(i)) {
				if(MouseHelper.inRange(coords, object.getPosition(), object.getScale())) {
					object.onClick(mouseButton);
					return;
				}
			}
		}
	}
	
	public void setCurrent() {
		INSTANCE = this;
	}
	
	public Camera getCamera() {
		return this.camera;
	}
	
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}
	
	public void setShader(Shader shader) {
		if(renderer == null) renderer = new Renderer(shader);
		this.renderer.setShader(shader);
	}
	
	public void addGameObjct(GameObject gameObject) {
		this.gameObjects.get(gameObject.getZIndex()).add(gameObject);
		this.renderer.addObject(gameObject);
	}
	
	public void render() {
		this.renderer.render();
	}
}
