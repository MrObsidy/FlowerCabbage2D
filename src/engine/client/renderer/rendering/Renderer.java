package engine.client.renderer.rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import engine.client.object.GameObject;
import engine.client.rendering.shader.Shader;

public class Renderer {
	public static final int BATCH_SIZE = 1000;
	
	private final Map<Integer, List<RenderBatch>> renderBatches = new HashMap<Integer, List<RenderBatch>>();
	
	private Shader shader;
	
	public Renderer(Shader shader) {
		this.shader = shader;
		for(int i = 0; i <= 9; i++) {
			ArrayList<RenderBatch> list = new ArrayList<RenderBatch>();
			this.renderBatches.put(i, list);
		}
	}
	
	public void addObject(GameObject obj) {
		boolean success = false;
		for(int i = 0; i <= 9; i++) {
			for(RenderBatch renderBatch : this.renderBatches.get(i)) {
				if(success) return;
				if(obj.getZIndex() == i) success = renderBatch.addMesh(obj);
			}
		}
		
		if(!success) {
			RenderBatch renderBatch = new RenderBatch(BATCH_SIZE, this.shader);
			renderBatch.addMesh(obj);
			this.renderBatches.get(obj.getZIndex()).add(renderBatch);
		}
	}
	
	public void setShader(Shader shader) {
		this.shader = shader;
	}
	
	public void render() {
		GL11.glClearColor(0.5f, 0.5f, 0.5f, 0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		for(int i = 0; i <= 9; i++){
			for(RenderBatch renderBatch : this.renderBatches.get(i)) {
				renderBatch.render();
			}
		}
	}
}
