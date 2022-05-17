package com.casoncompany.engine.renderer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.casoncompany.engine.Window;

public class Renderer {
	
	private final Window window;
	
	//test
	float color;
	
	public Renderer(Window window) {
		this.window = window;
	}
	
	public void init() {
		
	}
	
	public void update() {
		if(window.isKeyPressed(GLFW.GLFW_KEY_W))
			color+=0.01;
		if(window.isKeyPressed(GLFW.GLFW_KEY_S))
			color-=0.01;
	}
	
	public void render() {
		window.setClearColor(color);
		clear();
	}
	
	public void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void cleanup() {
		
	}

}
