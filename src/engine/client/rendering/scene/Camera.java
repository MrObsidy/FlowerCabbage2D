package engine.client.rendering.scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
	private Matrix4f projectionMatrix, viewMatrix;
	private Vector3f position;
		
	public Camera(float x, float y, float z) {
		this.projectionMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		this.position = new Vector3f(x, y, z);
	}
		
	public Matrix4f getProjectionMatrix() {
		return this.projectionMatrix;
	}

	public Matrix4f getViewMatrix() {
		Vector3f front = new Vector3f(0.0f, 0.0f, -1.0f);
		Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
		this.viewMatrix.identity();
		viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f), front.add(position.x, position.y, 0.0f), up);
		return this.viewMatrix;
	}
		
	public Vector3f getPosition() {
		return this.position;
	}

	public void perspective(float fov, float aspect, float near, float far) {
		projectionMatrix.identity();
		projectionMatrix.perspective(fov, aspect, near, far);
	}

	public void ortho(float left, float right, float bottom, float top, float near, float far) {
		projectionMatrix.identity();
		projectionMatrix.ortho(left, right, bottom, top, near, far);
	}
}
