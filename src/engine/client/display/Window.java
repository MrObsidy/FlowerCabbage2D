package engine.client.display;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryUtil;

import engine.client.input.KeyboardHandler;
import engine.client.input.MouseHandler;

public class Window {
	private final long ID;
	private static int WIDTH;
	private static int HEIGHT;
	private static long MONITOR;
	private static String TITLE;
	private static Window INSTANCE;
	private final GLCapabilities CAPABILITIES;
	
	public static void setWidth(int width) {
		WIDTH = width;
	}
	
	public static void setHeight(int height) {
		HEIGHT = height;
	}
	
	public static void setMonitor(long monitor) {
		MONITOR = monitor;
	}
	
	public static void setTitle(String title) {
		TITLE = title;
	}
	
	public static Window forceNewWindow() {
		INSTANCE = null;
		INSTANCE = new Window();
		return INSTANCE;
	}
	
	public static Window getWindow() {
		if(INSTANCE == null) {
			INSTANCE = new Window();
		}
		return INSTANCE;
	}
	/**
	 * 
	 * 
	 * @param width Width of the Window
	 * @param height Height of the Window
	 * @param title Title of the Window
	 * @param monitor Monitor for fullscreen window, null for windowed mode
	 */
	private Window() {
		if(!GLFW.glfwInit()) throw new RuntimeException("GLFW could not be initialized.");
		
		System.out.println("FlowerCabbage2D is starting or a new window is created, LWJGL Version: " + Version.getVersion() + " (" + Version.BUILD_TYPE + ")");
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
		
		GLFW.glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
		
		this.ID = GLFW.glfwCreateWindow(WIDTH, HEIGHT, TITLE, MONITOR, MemoryUtil.NULL);
		if(this.ID == MemoryUtil.NULL) {
			throw new RuntimeException("Could not create Window.");
		}
		
		GLFW.glfwSetCursorPosCallback(this.ID, MouseHandler::mousePositionCallback);
		GLFW.glfwSetMouseButtonCallback(this.ID, MouseHandler::mouseButtonCallback);
		GLFW.glfwSetScrollCallback(this.ID, MouseHandler::mouseScrollCallback);
		GLFW.glfwSetKeyCallback(this.ID, KeyboardHandler::keyCallback);
		
		GLFW.glfwMakeContextCurrent(this.ID);
		GLFW.glfwSwapInterval(1);
		this.CAPABILITIES = GL.createCapabilities();
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GLFW.glfwShowWindow(this.ID);
	}
	
	public void destroy() throws Throwable {
		GLFW.glfwDestroyWindow(this.ID);
		
		this.finalize();
	}
	
	public long getID() {
		return this.ID;
	}
	
	public static int getWidth() {
		return WIDTH;
	}
	
	public static int getHeight() {
		return HEIGHT;
	}
	
	public static String getTitle() {
		return TITLE;
	}
	
	public static long getMonitor() {
		return MONITOR;
	}
	
	public GLCapabilities getCapabilities() {
		return this.CAPABILITIES;
	}
}