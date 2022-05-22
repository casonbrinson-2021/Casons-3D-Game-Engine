package com.casoncompany.engine.input;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.casoncompany.engine.Window;

public class MouseInput {
	
	private Window window;
	
	private final Vector2d previousPosition, currentPosition;
	private final Vector2f displayVector;
	
	private boolean inWindow = false;
	private boolean leftButtonPress = false;
	private boolean rightButtonPress = false;
	
	public MouseInput(Window window) {
		this.window = window;
		this.previousPosition = new Vector2d(-1,-1);
		this.currentPosition = new Vector2d(0,0);
		this.displayVector = new Vector2f();
	}
	
	public void init() {
		GLFW.glfwSetCursorPosCallback(window.getWindowHandle(), (window, xpos, ypos) -> {
			currentPosition.x = xpos;
			currentPosition.y = ypos;
		});
		
		GLFW.glfwSetCursorEnterCallback(window.getWindowHandle(), (window, entered) -> {
			inWindow = entered;
		});
		
		GLFW.glfwSetMouseButtonCallback(window.getWindowHandle(), (window, button, action, mods) -> {
			leftButtonPress = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS;
			rightButtonPress = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS;
		});
	}
	
	public void update() {
		displayVector.x = 0;
		displayVector.y = 0;
		
		if(currentPosition.x > 0 && currentPosition.y > 0 && inWindow) {
			double x = currentPosition.x - previousPosition.x;
			double y = currentPosition.y - previousPosition.y;
			
			boolean rotateX = x != 0;
			boolean rotateY = y != 0;
			
			if(rotateX)
				displayVector.y = (float) x;
			
			if(rotateY)
				displayVector.x = (float) y;
		}
		
		previousPosition.x = currentPosition.x;
		previousPosition.y = currentPosition.y;
	}
	
	public boolean getLeftButtonPress() {
		return leftButtonPress;
	}
	
	public boolean getRightButtonPress() {
		return rightButtonPress;
	}
	
	public Vector2f getDisplayVector() {
		return displayVector;
	}

}
