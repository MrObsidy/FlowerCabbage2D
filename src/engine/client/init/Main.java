package engine.client.init;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL20;

import engine.client.display.Window;
import engine.client.input.KeyboardHandler;
import engine.client.rendering.mesh.GameObjectTexture;
import engine.client.rendering.scene.Camera;
import engine.client.rendering.scene.DefaultScene;
import engine.client.rendering.scene.Scene;
import engine.client.rendering.shader.Shader;
import engine.client.rendering.texture.Texture;
import engine.client.resources.ResourceManager;
import engine.client.util.TimeManager;
import engine.client.input.MouseHandler;
import engine.client.input.MouseHelper;
import engine.client.object.GameObject;

public class Main {
	private static Window testWindow;
	
	public static Texture texture;
	
	public static String[] vertex;
	public static String[] fragment;
	
	public static void main(String[] args) {
		
		try {
			vertex = (String[]) Files.readAllLines(new File("assets/shaders/defaultvertex.glsl").toPath()).toArray(new String[Files.readAllLines(new File("assets/shaders/defaultvertex.glsl").toPath()).size()]);
			fragment = (String[]) Files.readAllLines(new File("assets/shaders/defaultfragment.glsl").toPath()).toArray(new String[Files.readAllLines(new File("assets/shaders/defaultfragment.glsl").toPath()).size()]);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				

		
		if(!GLFW.glfwInit()) throw new IllegalStateException("Nope");
		Window.setHeight(GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()).height());
		Window.setWidth(GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()).width());
		Window.setTitle("CoolBeansWindow");
		Window.setMonitor(GLFW.glfwGetPrimaryMonitor());
		try {
			MouseHandler.createInstance();
			KeyboardHandler.createInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		testWindow = Window.getWindow();
		
		loop();
	}
	
	public static void loop() {
		
		Shader shader = ResourceManager.getShader("defaultShader");
		
		GameObjectTexture marioTex = ResourceManager.getSprite("assets/images/spritesheet.png", 0, 16);
		
		Texture red = ResourceManager.getTexture("assets/images/blendImage1.png");
		Texture green = ResourceManager.getTexture("assets/images/blendImage2.png");
		
		Texture goombaTex = ResourceManager.getTexture("assets/images/testImage2.png");	
		
		String vertexS = "";
		String fragmentS = "";
		
		for(String v : vertex) {
			vertexS += v + "\n";
		}	
		
		for(String v : fragment) {
			fragmentS += v + "\n";
		}
				
		shader.addShader(vertexS, GL20.GL_VERTEX_SHADER);
		shader.addShader(fragmentS, GL20.GL_FRAGMENT_SHADER);
		shader.linkShaders();
		
		Scene scene = new DefaultScene();
				
		//scene.addTexturedMesh(tmesh);
		scene.setShader(shader);
		
		boolean triggeredThisScene = false;
		
		Camera camera = new Camera(0f, 0f, 0f);
		//camera.perspective(2, 2, 0.01f, 20f);
		camera.ortho(0f, Window.getWidth(), 0f, Window.getHeight(), 0f, 20f);
		
		scene.setCamera(camera);
		
		scene.setCurrent();
		
		
		GameObjectTexture goombaMesh = new GameObjectTexture(goombaTex);		
		
		GameObjectTexture redSquareMesh = new GameObjectTexture(red);
		GameObjectTexture greenSquareMesh = new GameObjectTexture(green);
		
		GameObject mario = new GameObject(new Vector2f(100, 100), new Vector2f(256, 256), marioTex, 0);
		GameObject goomba = new GameObject(new Vector2f(400, 400), new Vector2f(256, 256), goombaMesh, 0);
		GameObject redSquare = new GameObject(new Vector2f(656, 400), new Vector2f(256, 256), redSquareMesh, 0);
		GameObject greenSquare = new GameObject(new Vector2f(756, 400), new Vector2f(256, 256), greenSquareMesh, 1);
		
		scene.addGameObjct(mario);
		scene.addGameObjct(goomba);
		scene.addGameObjct(redSquare);
		scene.addGameObjct(greenSquare);
		
		TimeManager debouncer = new TimeManager();
		debouncer.setStartTime();
		
		int index = 0;
		while(!GLFW.glfwWindowShouldClose(testWindow.getID())) {
			GLFW.glfwPollEvents();
			
			scene.render();
			
			if(debouncer.hasElapsed(TimeManager.SECOND)) {
				GameObjectTexture tex = ResourceManager.getSprite("assets/images/spritesheet.png", index, 16);
				mario.setMesh(tex);
				index++;
				if(index >= 27) {
					index = 0;
				}
				debouncer.setStartTime();
			}
			
			if(KeyboardHandler.getInstance().keyPressed[GLFW.GLFW_KEY_ESCAPE]) GLFW.glfwSetWindowShouldClose(testWindow.getID(), true);
			if(debouncer.hasElapsed(TimeManager.SECOND) && !triggeredThisScene && KeyboardHandler.getInstance().keyPressed[GLFW.GLFW_KEY_F9] && DefaultScene.drawMode == GL20.GL_TRIANGLES) {
				DefaultScene.drawMode = GL20.GL_LINES;
				triggeredThisScene = true;
				debouncer.setStartTime(System.nanoTime());
			}
			if(debouncer.hasElapsed(TimeManager.SECOND) && !triggeredThisScene && KeyboardHandler.getInstance().keyPressed[GLFW.GLFW_KEY_F9] && DefaultScene.drawMode == GL20.GL_LINES) {
				DefaultScene.drawMode = GL20.GL_TRIANGLES;
				triggeredThisScene = true;
				debouncer.setStartTime(System.nanoTime());
			}
			if(KeyboardHandler.getInstance().keyPressed[GLFW.GLFW_KEY_W]) {
				scene.getCamera().getPosition().y -= 1;
			}
			if(KeyboardHandler.getInstance().keyPressed[GLFW.GLFW_KEY_S]) {
				scene.getCamera().getPosition().y += 1;
			}
			if(KeyboardHandler.getInstance().keyPressed[GLFW.GLFW_KEY_A]) {
				scene.getCamera().getPosition().x += 1;
			}
			if(KeyboardHandler.getInstance().keyPressed[GLFW.GLFW_KEY_D]) {
				scene.getCamera().getPosition().x -= 1;
			}
			
			if(MouseHandler.getInstance().buttonPressed[0]) {
				int x = (int) MouseHandler.getInstance().posX;
				int y = (int) MouseHandler.getInstance().posY;
				
				Vector2f wc = MouseHelper.convertToWorldCoordinates(x, y, scene.getCamera().getViewMatrix(), scene.getCamera().getProjectionMatrix());
				
				scene.onClick(wc, 0);
			} else if(MouseHandler.getInstance().buttonPressed[1]) {
				int x = (int) MouseHandler.getInstance().posX;
				int y = (int) MouseHandler.getInstance().posY;
				
				Vector2f wc = MouseHelper.convertToWorldCoordinates(x, y, scene.getCamera().getViewMatrix(), scene.getCamera().getProjectionMatrix());
				
				scene.onClick(wc, 1);
			}
			
			GLFW.glfwSwapBuffers(testWindow.getID());
			triggeredThisScene = false;
		}
	}
}
