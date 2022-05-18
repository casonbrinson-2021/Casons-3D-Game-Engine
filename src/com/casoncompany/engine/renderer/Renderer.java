package com.casoncompany.engine.renderer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.casoncompany.engine.Window;
import com.casoncompany.engine.entity.Model;

public class Renderer {
	
	private final Window window;
	
	//test
	
	public Renderer(Window window) {
		this.window = window;
	}
	
	public void init() {
		
	}
	
	public void update() {
	}
	
	public void render(Model model) {
		clear();
		GL30.glBindVertexArray(model.getId());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
	public void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void cleanup() {
		
	}

}
