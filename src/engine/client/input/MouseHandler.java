package engine.client.input;

import java.lang.reflect.InvocationTargetException;

import org.lwjgl.glfw.GLFW;

public class MouseHandler {
	private static MouseHandler INSTANCE;
	
	private static Class<? extends MouseHandler> MouseHandlerClass = MouseHandler.class;
	
	public double scrollX;
	public double scrollY;
	public double posX, posY, lastX, lastY;
	public boolean[] buttonPressed = new boolean[3];
	public boolean isDragged;
	
	public MouseHandler() {
		if(INSTANCE != null) {
			throw new IllegalStateException("A suitable Mouse Handler already exists!");
		}
		this.scrollX = 0.0;
		this.scrollY = 0.0;
		this.posX = 0.0;
		this.posY = 0.0;
		this.lastX = 0.0;
		this.lastY = 0.0;
	}
	
	public double getDx() {
		return this.lastX - this.posX;
	}
	
	public double getDy() {
		return this.lastY - this.posY;
	}
	
	public static void registerMouseHandler(Class <? extends MouseHandler> handler) {
		MouseHandlerClass = handler;
	}
	
	public static void mousePositionCallback(long window, double xpos, double ypos) {
		MouseHandler.getInstance().lastX = MouseHandler.getInstance().posX;
		MouseHandler.getInstance().lastY = MouseHandler.getInstance().posY;
		MouseHandler.getInstance().posX = xpos;
		MouseHandler.getInstance().posY = ypos;
	}
	
	public static void mouseButtonCallback(long window, int button, int action, int mods) {
		if(action == GLFW.GLFW_PRESS) {
			if(button < MouseHandler.getInstance().buttonPressed.length) MouseHandler.getInstance().buttonPressed[button] = true;
		} else if(action == GLFW.GLFW_RELEASE) {
			if(button < MouseHandler.getInstance().buttonPressed.length) {
				MouseHandler.getInstance().buttonPressed[button] = false;
				MouseHandler.getInstance().isDragged = false;
			}
		}
	}
	
	public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
		MouseHandler.getInstance().scrollX = xOffset;
		MouseHandler.getInstance().scrollY = yOffset;
	}
	
	public static void done() {
		MouseHandler.getInstance().scrollX = 0;
		MouseHandler.getInstance().scrollY = 0;
		MouseHandler.getInstance().lastX = MouseHandler.getInstance().posX;
		MouseHandler.getInstance().lastY = MouseHandler.getInstance().posY;
	}
	
	public static MouseHandler getInstance() {
		return INSTANCE;
	}
	
	public static void createInstance() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		INSTANCE = (MouseHandler) MouseHandlerClass.getConstructor().newInstance();
	}
}