package com.casoncompany.engine.input;

import org.lwjgl.glfw.GLFW;

import com.casoncompany.engine.Window;

public class InputController {
	
	private Window window;
	
	public InputController(Window window) {
		this.window = window;
	}
	
	public boolean getForward() {
		return window.isKeyPressed(GLFW.GLFW_KEY_W);
	}
	
	public boolean getBackward() {
		return window.isKeyPressed(GLFW.GLFW_KEY_S);
	}
	
	public boolean getLeft() {
		return window.isKeyPressed(GLFW.GLFW_KEY_A);
	}
	
	public boolean getRight() {
		return window.isKeyPressed(GLFW.GLFW_KEY_D);
	}

}
