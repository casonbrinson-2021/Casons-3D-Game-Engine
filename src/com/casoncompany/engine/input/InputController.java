package com.casoncompany.engine.input;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.casoncompany.engine.Window;

public class InputController {
	
	private Window window;
	private MouseInput mouseInput;
	
	public InputController(Window window) {
		this.window = window;
		mouseInput = new MouseInput(window);
	}
	
	public void init() {
		mouseInput.init();
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
	
	public boolean getUp() {
		return window.isKeyPressed(GLFW.GLFW_KEY_SPACE);
	}
	
	public boolean getDown() {
		return window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT);
	}
	
	public void update() {
		mouseInput.input();
	}
	
	public boolean getLeftMouseButtonPress() {
		return mouseInput.getLeftButtonPress();
	}
	
	public boolean getRightMouseButtonPress() {
		return mouseInput.getRightButtonPress();
	}
	
	public Vector2f getMouseDisplayVec() {
		return mouseInput.getDisplayVector();
	}

}
