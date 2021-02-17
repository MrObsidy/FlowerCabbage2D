package engine.client.input;

import java.lang.reflect.InvocationTargetException;

import org.lwjgl.glfw.GLFW;

public class KeyboardHandler {
	private static KeyboardHandler INSTANCE;
	private static Class<? extends KeyboardHandler> handler = KeyboardHandler.class;
	
	public boolean keyPressed[] = new boolean[350];
	
	public KeyboardHandler() {
		
	}
	
	public static void registerKeyboardHandler(Class<? extends KeyboardHandler> parHandler) {
		handler = parHandler;
	}
	
	public static KeyboardHandler getInstance() {
		return INSTANCE;
	}
	
	public static void keyCallback(long window, int key, int scancode, int action, int mods) {
		if(action == GLFW.GLFW_PRESS) {
			KeyboardHandler.getInstance().keyPressed[key] = true;
		} else if(action == GLFW.GLFW_RELEASE) {
			KeyboardHandler.getInstance().keyPressed[key] = false;
		}
	}
	
	public static void createInstance() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		INSTANCE = (KeyboardHandler) handler.getConstructor().newInstance();
	}
}