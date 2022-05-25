package com.casoncompany.engine.test;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.casoncompany.engine.Camera;
import com.casoncompany.engine.Window;
import com.casoncompany.engine.entity.Entity;
import com.casoncompany.engine.entity.Material;
import com.casoncompany.engine.entity.Model;
import com.casoncompany.engine.entity.ObjectLoader;
import com.casoncompany.engine.entity.Texture;
import com.casoncompany.engine.input.InputController;
import com.casoncompany.engine.lighting.DirectionalLight;
import com.casoncompany.engine.lighting.PointLight;
import com.casoncompany.engine.lighting.SpotLight;
import com.casoncompany.engine.renderer.GameLogic;
import com.casoncompany.engine.renderer.Renderer;
import com.casoncompany.engine.renderer.Terrain;

import java.util.ArrayList;
import java.util.List;

import org.joml.Random;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TestGame implements GameLogic {
	
	private static final float CAMERA_MOVE_SPEED = 0.05f;
	private static final float MOUSE_SENSITIVITY = 0.05f;
	private static final float SCALE = 10.0f;
	
	private final Window window;
	private final Renderer renderer;
	private final InputController inputController;
	private final ObjectLoader objectLoader;
	
	private List<Entity> entities;
	private List<Terrain> terrains;
	private Camera camera;
	
	private Vector3f cameraInc;
	
	private float lightAngle;
	private float spotAngle;
	private DirectionalLight directionalLight;
	private PointLight[] pointLights;
	private SpotLight[] spotLights;
		
	public TestGame(Window window) {
		this.window = window;
		
		renderer = new Renderer(this.window);
		inputController = new InputController(this.window);
		objectLoader = new ObjectLoader();
		camera = new Camera();
		
		cameraInc = new Vector3f(0,0,0);
		
		lightAngle = -90;
	}
	
	@Override
	public void init() throws Exception {
		renderer.init();
		inputController.init();
		
		Model model = objectLoader.loadOBJModel("/models/bunny.obj");
		model.setTexture(new Texture(objectLoader.loadTexture("textures/grassBlock.png")), 1f);
		
		terrains = new ArrayList<>();
		Terrain terrain = new Terrain(new Vector3f(0,-1,-800), objectLoader, new Material(new Texture(objectLoader.loadTexture("textures/terrain.png")), 0.1f));
		//Terrain terrain2 = new Terrain(new Vector3f(-800,-1,-800), objectLoader, new Material(new Texture(objectLoader.loadTexture("textures/flowers.png")), 0.1f));
		terrains.add(terrain);
		//terrains.add(terrain2);
		
		entities = new ArrayList<Entity>();
		Random rand = new Random();
		for(int i = 0; i < 100; i++) {
			float x = rand.nextFloat()*100 - 50;
			float y = rand.nextFloat()*100 - 50;
			float z = rand.nextFloat()*-200;
			entities.add(new Entity(model, new Vector3f(x,y,z), new Vector3f(rand.nextFloat()*180, rand.nextFloat()*180, 0), SCALE));
		}
		entities.add(new Entity(model, new Vector3f(0,0,-2f), new Vector3f(0,0,0), SCALE));

		//point light
		float lightIntensity = 1.0f;
		Vector3f lightPosition = new Vector3f(-0.5f, -0.5f,-3.2f);
		Vector3f lightColor = new Vector3f(1,1,1);
		PointLight pointLight = new PointLight(lightColor, lightPosition, lightIntensity, 0, 0, 1);
		
		//spot light
		Vector3f conedir = new Vector3f(0,0,1);
		float cutoff = (float) Math.cos(Math.toRadians(180));
		SpotLight spotLight = new SpotLight(new PointLight(lightColor, new Vector3f(0,0,1f), lightIntensity, 0, 0, 1), conedir, cutoff);
		SpotLight spotLight1 = new SpotLight(new PointLight(lightColor, lightPosition, lightIntensity, 0, 0, 1), conedir, cutoff);
		spotLight1.getPointLight().setPosition(new Vector3f(0.5f, 0.5f, -3.6f));
		
		//directional light
		lightPosition = new Vector3f(-1,-10,0);
		lightColor = new Vector3f(1,1,1);
		directionalLight = new DirectionalLight(lightColor, lightPosition, lightIntensity);		
	
		pointLights = new PointLight[] {pointLight};
		spotLights = new SpotLight[] {spotLight, spotLight1};
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
		
		//testing point light
		if(window.isKeyPressed(GLFW.GLFW_KEY_O))
			pointLights[0].getPosition().x -= 0.1f;
		if(window.isKeyPressed(GLFW.GLFW_KEY_P))
			pointLights[0].getPosition().x += 0.1f;
		
		//testing spot light
		float lightPos = spotLights[0].getPointLight().getPosition().z;
		if(window.isKeyPressed(GLFW.GLFW_KEY_N))
			spotLights[0].getPointLight().getPosition().z = lightPos - 0.1f;
		if(window.isKeyPressed(GLFW.GLFW_KEY_M))
			spotLights[0].getPointLight().getPosition().z = lightPos + 0.1f;
		
		//entity.incrementRotation(0.0f, 0.25f, 0.0f);
		
		
		//random day night cycle using directional light
//		lightAngle+=1.05f;
//		if(lightAngle > 90) {
//			directionalLight.setIntensity(0);
//			
//			if(lightAngle >= 360)
//				lightAngle = -90;
//		} else if(lightAngle <= -80 || lightAngle >= 80) {
//			float factor = 1 - (Math.abs(lightAngle) - 80) / 10.0f;
//			directionalLight.setIntensity(factor);
//			directionalLight.getColor().y = Math.max(factor, 0.9f);
//			directionalLight.getColor().z = Math.max(factor, 0.5f);
//		} else {
//			directionalLight.setIntensity(1);
//			directionalLight.getColor().x = 1;
//			directionalLight.getColor().y = 1;
//			directionalLight.getColor().z = 1;
//		}
//		double angRad = Math.toRadians(lightAngle);
//		directionalLight.getDirection().x = (float) Math.sin(angRad);
//		directionalLight.getDirection().y = (float) Math.cos(angRad);
		
		for(Entity entity : entities) {
			renderer.processEntities(entity);
		}
		
		for(Terrain terrain : terrains) {
			renderer.processTerrain(terrain);
		}
	}
	
	@Override
	public void render() {	
		renderer.render(camera, directionalLight, pointLights, spotLights);
	}

	@Override
	public void cleanup() {
		renderer.cleanup();
		objectLoader.cleanup();
	}

}
