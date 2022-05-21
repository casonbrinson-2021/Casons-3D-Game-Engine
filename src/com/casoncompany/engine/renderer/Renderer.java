package com.casoncompany.engine.renderer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.casoncompany.engine.Window;
import com.casoncompany.engine.entity.Model;
import com.casoncompany.engine.utils.Utils;

public class Renderer {
	
	private final Window window;
	private Shader shader;
	
	//test
	
	public Renderer(Window window) {
		this.window = window;
	}
	
	public void init() throws Exception {
		shader = new Shader();
		shader.createVertexShader(Utils.loadResource("/shader/vertex.vs"));
		shader.createFragmentShader(Utils.loadResource("/shader/fragment.fs"));
		shader.link();
	}
	
	public void update() {
	}
	
	public void render(Model model) {
		clear();
		shader.bind();
		GL30.glBindVertexArray(model.getId());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.unbind();
	}
	
	public void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void cleanup() {
		shader.cleanup();
	}

}
