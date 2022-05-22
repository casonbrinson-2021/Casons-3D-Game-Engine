package com.casoncompany.engine.renderer;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.casoncompany.engine.Camera;
import com.casoncompany.engine.Window;
import com.casoncompany.engine.entity.Entity;
import com.casoncompany.engine.entity.Model;
import com.casoncompany.engine.utils.Transformation;
import com.casoncompany.engine.utils.Utils;

public class Renderer {
	
	public static final Vector3f AMBIENT_LIGHT = new Vector3f(1.3f, 1.3f, 1.3f);
	
	private final Window window;
	private Shader shader;
	
	//test
	
	public Renderer(Window window) {
		this.window = window;
	}
	
	public void init() throws Exception {
		shader = new Shader();
		shader.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
		shader.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));
		shader.link();
		shader.createUniform("textureSampler");
		shader.createUniform("transformationMatrix");
		shader.createUniform("projectionMatrix");
		shader.createUniform("viewMatrix");
		shader.createUniform("ambientLight");
		shader.createMaterialUniform("material");
	}
	
	public void update() {
	}
	
	public void render(Entity entity, Camera camera) {
		clear();
		
		shader.bind();
		
		shader.setUniform("textureSampler", 0);
		shader.setUniform("transformationMatrix", Transformation.createTransformationMatrix(entity));
		shader.setUniform("projectionMatrix", window.updateProjectionMatrix());
		shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
		shader.setUniform("material", entity.getModel().getMaterial());
		shader.setUniform("ambientLight", AMBIENT_LIGHT);
		
		GL30.glBindVertexArray(entity.getModel().getId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getId());
		GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		
		shader.unbind();
	}
	
	public void clear() {
		window.setClearColor(0.0f,  0.0f, 0.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void cleanup() {
		shader.cleanup();
	}

}
