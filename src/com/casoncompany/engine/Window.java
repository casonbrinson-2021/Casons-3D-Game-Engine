package com.casoncompany.engine;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import org.joml.Matrix4f;

public class Window {
	
	private static final float FOV = (float) Math.toRadians(60);
	private static final float Z_NEAR = 0.01f;
	private static final float Z_FAR = 1000f;
	
	private final Matrix4f projectionMatrix;
	
	private int width;
	private int height;
	private String title;
	private boolean vsync;
	
	private boolean resize;
	
	private long window;
	
	public Window(int width, int height, String title, boolean vsync) {
		this.width = width;
		this.height = height;
		this.title = title;
		this.vsync = vsync;
		projectionMatrix = new Matrix4f();
	}
	
	public void init() {
		if(!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); 
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		
		boolean maximized = false;
		if(width == 0 || height == 0) {
			width = 100;
			height = 100;
			GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
			maximized = true;
		}
		
		window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if(window == MemoryUtil.NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		
		GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
			this.width = width;
			this.height = height;
			this.setResize(true);
		});
		
		if(maximized)
			GLFW.glfwMaximizeWindow(window);
		else {
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			GLFW.glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
		}
		
		GLFW.glfwMakeContextCurrent(window);
		
		if(vsync)
			GLFW.glfwSwapInterval(1);
		else GLFW.glfwSwapInterval(0);
		
		GLFW.glfwShowWindow(window);
		
		GL.createCapabilities();
		
		GL11.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);	
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_STENCIL_TEST);
	}
	
	public void update() {
		GLFW.glfwSwapBuffers(window);
		GLFW.glfwPollEvents();
	}
	
	public void cleanup() {	
		GLFW.glfwDestroyWindow(window);
	}
	
	public boolean windowShouldClose() {
		return GLFW.glfwWindowShouldClose(window);
	}
	
	public boolean isResize() {
		return resize;
	}
	
	public void setResize(boolean resize) {
		this.resize = resize;
	}
	
	public boolean isKeyPressed(int keycode) {
		return GLFW.glfwGetKey(window, keycode) == GLFW.GLFW_PRESS;
	}
	
	public void setTitle(String title) {
		GLFW.glfwSetWindowTitle(window, title);
	}
	
	public void setClearColor(float r, float g, float b) {
		GL11.glClearColor(r, g, b, 0.0f);
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public Matrix4f updateProjectionMatrix() {
		float aspectRatio = (float) width / height;
		return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
	}
	
	public Matrix4f updateProjectionMatrix(Matrix4f matrix, int width, int height) {
		float aspectRatio = (float) width / height;
		return matrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public long getWindowHandle() {
		return window;
	}
}
