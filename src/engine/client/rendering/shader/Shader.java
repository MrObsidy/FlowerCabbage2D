package engine.client.rendering.shader;

import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

public class Shader {
	private final ArrayList<Integer> shaderIDs = new ArrayList<Integer>();
	private int programID;
	
	public void addShader(String source, int shaderType) {
		int id = glCreateShader(shaderType);
		glShaderSource(id, source);
		glCompileShader(id);
		if(glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
			int len = glGetShaderi(id, GL_INFO_LOG_LENGTH);
			
			System.err.println(glGetShaderInfoLog(id, len));
			
			throw new RuntimeException("Could not compile shader " + id);
		}
		shaderIDs.add(id);
	}
	
	public void linkShaders() {
		this.programID = glCreateProgram();
		for(int i : this.shaderIDs) {
			glAttachShader(this.programID, i);
		}
		glLinkProgram(this.programID);
		
		if(glGetProgrami(this.programID, GL_LINK_STATUS) == GL_FALSE) {
			throw new RuntimeException("Could not link shader program" + this.programID);
		}
	}
	
	public void uniformMatrix4f(String uniformName, Matrix4f mat) {
		int loc = glGetUniformLocation(this.programID, uniformName);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		mat.get(buffer);
		glUniformMatrix4fv(loc, false, buffer);
	}
	
	public void uniformMatrix3f(String uniformName, Matrix3f mat) {
		int loc = glGetUniformLocation(this.programID, uniformName);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(9);
		mat.get(buffer);
		glUniformMatrix3fv(loc, false, buffer);
	}
	
	public void uniformVector4f(String uniformName, Vector4f mat) {
		int loc = glGetUniformLocation(this.programID, uniformName);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
		mat.get(buffer);
		glUniform4fv(loc, buffer);
	}
	
	public void uniformVector3f(String uniformName, Vector3f mat) {
		int loc = glGetUniformLocation(this.programID, uniformName);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(3);
		mat.get(buffer);
		glUniform3fv(loc, buffer);
	}
	
	public void uniformVector2f(String uniformName, Vector2f mat) {
		int loc = glGetUniformLocation(this.programID, uniformName);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
		mat.get(buffer);
		glUniform2fv(loc, buffer);
	}
	
	public void uniformFloat(String uniformName, float mat) {
		int loc = glGetUniformLocation(this.programID, uniformName);
		glUniform1f(loc, mat);
	}
	
	public void uniformInt(String uniformName, int mat) {
		int loc = glGetUniformLocation(this.programID, uniformName);
		glUniform1i(loc, mat);
	}
	
	public void texture(String textureName, int slot) {
		int loc = glGetUniformLocation(this.programID, textureName);
		glUniform1i(loc, slot);
	}
	
	public void uniformIntArray(String uniformName, int[] array) {
		int loc = glGetUniformLocation(this.programID, uniformName);
		glUniform1iv(loc, array);
	}
	
	public int getProgramID() {
		return this.programID;
	}


}
