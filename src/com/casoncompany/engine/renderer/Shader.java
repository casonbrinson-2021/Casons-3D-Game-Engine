package com.casoncompany.engine.renderer;

import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

public class Shader {

	private final int programID;
	private int vertexShaderID, fragmentShaderID;
	
	private final Map<String, Integer> uniforms;
	
	public Shader() throws Exception {
		programID = GL20.glCreateProgram();
		if(programID == 0)
			throw new Exception("Could not create the shader");
		
		uniforms = new HashMap<>();
	}
	
	public void createUniform(String uniformName) throws Exception {
		int uniformLocation = GL20.glGetUniformLocation(programID, uniformName);
		
		if(uniformLocation < 0)
			throw new Exception("Could not find uniform " + uniformName);
		
		uniforms.put(uniformName, uniformLocation);
	}
	
	public void setUniform(String uniformName, Matrix4f value) {
		try(MemoryStack stack = MemoryStack.stackPush()) {
			GL20.glUniformMatrix4fv(uniforms.get(uniformName), false, value.get(stack.mallocFloat(16)));
		}
	}
	
	public void setUniform(String uniformName, int vlaue) {
		GL20.glUniform1i(uniforms.get(uniformName), vlaue);
	}
	
	public void setUniform(String uniformName, float vlaue) {
		GL20.glUniform1f(uniforms.get(uniformName), vlaue);
	}
	
	public void setUniform(String uniformName, Vector3f value) {
		GL20.glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
	}
	
	public void setUniform(String uniformName, Vector4f value) {
		GL20.glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
	}
	
	public void setUniform(String uniformName, boolean value) {
		float res = 0;
		if(value)res = 1;
		
		GL20.glUniform1f(uniforms.get(uniformName), res);
	}
	
	public void createVertexShader(String shaderCode) throws Exception {
		vertexShaderID = createShader(shaderCode, GL20.GL_VERTEX_SHADER);
	}
	
	public void createFragmentShader(String shaderCode) throws Exception {
		fragmentShaderID = createShader(shaderCode, GL20.GL_FRAGMENT_SHADER);
	}
	
	public int createShader(String shaderCode, int shaderType) throws Exception {
		int shaderID = GL20.glCreateShader(shaderType);
		if(shaderID == 0)
			throw new Exception("Error creting shader - Type: " + shaderType);
		
		GL20.glShaderSource(shaderID, shaderCode);
		GL20.glCompileShader(shaderID);
		
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == 0)
			throw new Exception("Error compiling shader code - Type: " + shaderType + "\nInfo: " + GL20.glGetShaderInfoLog(shaderID, 1024));
		
		GL20.glAttachShader(programID, shaderID);
		
		return shaderID;
	}
	
	public void link() throws Exception {
		GL20.glLinkProgram(programID);
		if(GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == 0)
			throw new Exception("Error linking shader code - " + "\nInfo: " + GL20.glGetProgramInfoLog(programID, 1024));
		
		if(vertexShaderID != 0)
			GL20.glDetachShader(programID, vertexShaderID);
		
		if(fragmentShaderID != 0)
			GL20.glDetachShader(programID, fragmentShaderID);
		
		GL20.glValidateProgram(programID);
		
		if(GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == 0)
			throw new Exception("Unable to validate shader code " + GL20.glGetProgramInfoLog(programID, 1024));
	}
	
	public void bind() {
		GL20.glUseProgram(programID);
	}
	
	public void unbind() {
		GL20.glUseProgram(0);
	}
	
	public void cleanup() {
		unbind();
		if(programID != 0)
			GL20.glDeleteProgram(programID);
	}
}