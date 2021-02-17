package engine.client.input;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import engine.client.display.Window;


public class MouseHelper {
	public static Vector2f convertToWorldCoordinates(int parX, int parY, Matrix4f viewMatrixOriginal, Matrix4f projectionMatrixOriginal) {
		float x = ((float) parX / (float) Window.getWidth()) * 2 - 1;
		float y = ((float) (Window.getHeight() - parY) / (float) Window.getHeight()) * 2 - 1;
		
		Matrix4f viewMatrixInverse = new Matrix4f(viewMatrixOriginal).invert();
		Matrix4f projectionMatrixInverse = new Matrix4f(projectionMatrixOriginal).invert();
		
		Vector4f clickedAt = new Vector4f(x, y, 0f, 1.0f);
		
		Vector4f worldCoordinatesRaw = clickedAt.mul(projectionMatrixInverse).mul(viewMatrixInverse);
		
		return new Vector2f(worldCoordinatesRaw.x, worldCoordinatesRaw.y);
	}
	
	public static boolean inRange(Vector2f clickAt, Vector2f position, Vector2f scale) {
		boolean inRange = false;
		
		if(clickAt.x <= position.x + scale.x && clickAt.x >= position.x && clickAt.y <= position.y + scale.y && clickAt.y >= position.y) {
			inRange = true;
		}
		
		return inRange;
	}
}
