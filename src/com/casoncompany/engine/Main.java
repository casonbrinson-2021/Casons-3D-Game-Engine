package com.casoncompany.engine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import com.casoncompany.engine.input.InputController;
import com.casoncompany.engine.renderer.GameLogic;
import com.casoncompany.engine.renderer.Renderer;
import com.casoncompany.engine.test.TestGame;

public class Main implements Runnable {
	
	private static final long NANOSECOND = 1000000000L;
	private static final int UPDATES_PER_SECOND = 60;
	private static final double NANOSECONDS_PER_UPDATE = NANOSECOND / (double) UPDATES_PER_SECOND;
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	private static final String TITLE = "3D Game Engine";
	
	private Thread thread;
	private boolean running = false;
	
	private Window window;
	private GameLogic gameLogic;
	
	private GLFWErrorCallback errorCallback;
		
	public void start() {
		if(running)return;
		
		running = true;
		
		thread = new Thread(this, "Main Thread");
		thread.start();
	}
	
	public void stop() {
		if(!running)return;
		
		running = false;
		
		try {
			cleanup();
		} catch(Exception e) {
			System.err.println("Error cleaning up...");
			System.exit(0);
		}
		System.out.println("Program terminated");

	}
	
	public void cleanup() throws Exception{
		window.cleanup();
		gameLogic.cleanup();
		errorCallback.free();
		GLFW.glfwTerminate();
	}
	
	public void init() {
		System.out.println("Initializing game");
		
		window = new Window(WIDTH, HEIGHT, TITLE, true);
		window.init();
		
		errorCallback = GLFWErrorCallback.createPrint(System.err);
		GLFW.glfwSetErrorCallback(errorCallback);
		
		gameLogic = new TestGame(window);	//this is where the game is set
		try {
			gameLogic.init();
		} catch(Exception e) {
			System.err.println("Error initializing test game");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void render() {
		gameLogic.render();
		window.update();
	}
	
	//updates the state of things in the game at a consistent rate of UPDATES_PER_SECOND
	public void update() {
		gameLogic.update();
	}
	
	public void loop() {
		System.out.println("Starting game loop");
		
		long prevTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		int frames = 0;
		double delta = 0;
		int updates = 0;
		
		while(running) {
			long currTime = System.nanoTime();
			delta += (currTime - prevTime) / (double) NANOSECONDS_PER_UPDATE;
			prevTime = currTime;
			
			while(delta >= 1) {
				update();

				delta--;
				updates++;
			}
			
			render();
			
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer+=1000;
				//System.out.println(frames + " fps | " + updates + " update per second");
				window.setTitle(TITLE + " | " + frames + " fps");
				frames = 0;
				updates = 0;
			}
			
			if(window.windowShouldClose())
				stop();
		}	
	}

	@Override
	public void run() {
		init();
		loop();
	}
	

	public static void main(String[] args) {
		new Main().start();
	}
}
