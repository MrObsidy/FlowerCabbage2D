package engine.client.resources;

import java.util.HashMap;

import org.joml.Vector2f;

import engine.client.rendering.mesh.GameObjectTexture;
import engine.client.rendering.shader.Shader;
import engine.client.rendering.texture.Texture;

public class ResourceManager {
	private static final HashMap<String, Texture> TEXTURES = new HashMap<String, Texture>();
	private static final HashMap<String, Shader> SHADERS = new HashMap<String, Shader>();
	private static final HashMap<String, Texture> SPRITESHEETS = new HashMap<String, Texture>();
	//private static final Map<String, Sound> SOUNDS = new HashMap<String, Sound>();
	
	/**
	 * Get the sprite from a spritesheet with the ID spriteID. The sprite ID is the nth sprite counted from the bottom left corner.
	 * 
	 * @param name - path of the spritesheet
	 * @param spriteID - the nth sprite on the spritesheet you want to get
	 * @param spriteSize - the size of each sprite in pixels
	 * @return
	 */
	public static GameObjectTexture getSprite(String name, int spriteID, int spriteSize) {
		Texture spritesheet;
		
		if(SPRITESHEETS.get(name) == null) {
			spritesheet = new Texture(name);
			SPRITESHEETS.put(name, spritesheet);
		} else {
			spritesheet = SPRITESHEETS.get(name);
		}
		
		int height = spritesheet.getHeight();
		int width = spritesheet.getWidth();
		
		int widthInSprites = width / spriteSize;
		
		if(height % spriteSize != 0 || width % spriteSize != 0) {
			throw new RuntimeException("Spritesheet dimensions do not align with sprite size");
		}
		
		float x = Math.floorMod(spriteID, widthInSprites);
		float y = Math.floorDiv(spriteID, widthInSprites);
		
		float leftX = (x * spriteSize) / width;
		float rightX = ((x + 1) * spriteSize) / width;
		
		float lowerY = (y * spriteSize) / height;	
		float upperY = ((y + 1) * spriteSize) / height;
		
		return new GameObjectTexture(spritesheet, new Vector2f[] {
				new Vector2f(rightX, upperY),
				new Vector2f(rightX, lowerY),
				new Vector2f(leftX, lowerY),
				new Vector2f(leftX, upperY),
		});
	}
	
	public static Texture getTexture(String name) {
		if(TEXTURES.get(name) == null) {
			Texture texture = new Texture(name);
			TEXTURES.put(name, texture);
			return texture;
		} else {
			return TEXTURES.get(name);
		}
	}
	
	public static Shader getShader(String name) {
		if(SHADERS.get(name) == null) {
			Shader shader = new Shader();
			SHADERS.put(name, shader);
			return shader;
		} else {
			return SHADERS.get(name);
		}
	}
}
