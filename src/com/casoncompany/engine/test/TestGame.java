package com.casoncompany.engine.test;

import org.lwjgl.opengl.GL11;

import com.casoncompany.engine.Window;
import com.casoncompany.engine.entity.Model;
import com.casoncompany.engine.entity.ObjectLoader;
import com.casoncompany.engine.entity.Texture;
import com.casoncompany.engine.input.InputController;
import com.casoncompany.engine.renderer.GameLogic;
import com.casoncompany.engine.renderer.Renderer;

public class TestGame implements GameLogic {
	
	private final Window window;
	private final Renderer renderer;
	private final InputController inputController;
	private final ObjectLoader objectLoader;
	
	private Model model;
	
	private float color = 0.0f;
	
	public TestGame(Window window) {
		this.window = window;
		renderer = new Renderer(this.window);
		inputController = new InputController(this.window);
		objectLoader = new ObjectLoader();
	}
	
	@Override
	public void init() {
		try {
			renderer.init();
		} catch(Exception e) {
			System.err.println("Erorr initializing somehting in the test game");
			e.printStackTrace();
			System.exit(0);
		}
		
		float[] vertices = {
			-0.5f,  0.5f, 0f,
		    -0.5f, -0.5f, 0f,
		     0.5f, -0.5f, 0f,
		     0.5f,  0.5f, 0f
		};
		
		int[] indices = {
			0,1,3,
			3,1,2
		};
		
		float[] textureCoords = {
			0,0,
			0,1,
			1,1,
			1,0
		};
		
		try {
			model = objectLoader.loadModel(vertices, textureCoords, indices);
			model.setTexture(new Texture(objectLoader.loadTexture("textures/grassBlock.png")));
		} catch (Exception e) {
			System.err.println("Error with the textures");
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public void update() {
		if(inputController.getForward())
			color+=0.01;
		if(inputController.getBackward())
			color-=0.01;
	}
	
	@Override
	public void render() {
		if(window.isResize()) {
			GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResize(true);
		}
		
		
		window.setClearColor(color);
		renderer.render(model);
	}

	@Override
	public void cleanup() {
		renderer.cleanup();
		objectLoader.cleanup();
	}

}
