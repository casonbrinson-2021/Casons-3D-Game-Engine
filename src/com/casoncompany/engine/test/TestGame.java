package com.casoncompany.engine.test;

import org.lwjgl.opengl.GL11;

import com.casoncompany.engine.Camera;
import com.casoncompany.engine.Window;
import com.casoncompany.engine.entity.Entity;
import com.casoncompany.engine.entity.Model;
import com.casoncompany.engine.entity.ObjectLoader;
import com.casoncompany.engine.entity.Texture;
import com.casoncompany.engine.input.InputController;
import com.casoncompany.engine.renderer.GameLogic;
import com.casoncompany.engine.renderer.Renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class TestGame implements GameLogic {
	
	private static final float CAMERA_MOVE_SPEED = 0.05f;
	private static final float MOUSE_SENSITIVITY = 0.05f;
	
	private final Window window;
	private final Renderer renderer;
	private final InputController inputController;
	private final ObjectLoader objectLoader;
	
	private Entity entity;
	private Camera camera;
	
	private Vector3f cameraInc;
		
	public TestGame(Window window) {
		this.window = window;
		
		renderer = new Renderer(this.window);
		inputController = new InputController(this.window);
		objectLoader = new ObjectLoader();
		camera = new Camera();
		
		cameraInc = new Vector3f(0,0,0);
	}
	
	@Override
	public void init() {
		try {
			renderer.init();
			inputController.init();
		} catch(Exception e) {
			System.err.println("Erorr initializing somehting in the test game");
			e.printStackTrace();
			System.exit(0);
		}
		
		Model model;
		
		try {
			model = objectLoader.loadOBJModel("/models/bunny.obj");
			model.setTexture(new Texture(objectLoader.loadTexture("textures/grassBlock.png")));
			entity = new Entity(model, new Vector3f(0,0,-5), new Vector3f(0,0,0), 1);
		} catch (Exception e) {
			System.err.println("Error with the textures");
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public void update() {
		cameraInc.set(0,0,0);
		inputController.update();
		
		if(inputController.getForward())
			cameraInc.z = -1;
		if(inputController.getBackward())
			cameraInc.z = 1;
		
		if(inputController.getLeft())
			cameraInc.x = -1;
		if(inputController.getRight())
			cameraInc.x = 1;
		
		if(inputController.getUp())
			cameraInc.y = 1;
		if(inputController.getDown())
			cameraInc.y = -1;
		
		camera.movePosition(cameraInc.x * CAMERA_MOVE_SPEED, cameraInc.y * CAMERA_MOVE_SPEED, cameraInc.z * CAMERA_MOVE_SPEED);
		
		if(inputController.getLeftMouseButtonPress()) {
			Vector2f rotateVector = inputController.getMouseDisplayVec();
			camera.moveRotation(rotateVector.x * MOUSE_SENSITIVITY, rotateVector.y * MOUSE_SENSITIVITY, 0);
		}
		
		entity.incrementRotation(0.0f, 0.5f, 0.0f);
	}
	
	@Override
	public void render() {
		if(window.isResize()) {
			GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResize(true);
		}
		
		window.setClearColor(0.0f, 0.0f, 0.0f);
		renderer.render(entity, camera);
	}

	@Override
	public void cleanup() {
		renderer.cleanup();
		objectLoader.cleanup();
	}

}
