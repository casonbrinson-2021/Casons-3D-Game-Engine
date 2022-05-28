package com.casoncompany.engine.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.casoncompany.engine.Camera;
import com.casoncompany.engine.Window;
import com.casoncompany.engine.entity.Entity;
import com.casoncompany.engine.entity.Model;
import com.casoncompany.engine.entity.SceneManager;
import com.casoncompany.engine.lighting.DirectionalLight;
import com.casoncompany.engine.lighting.PointLight;
import com.casoncompany.engine.lighting.SpotLight;
import com.casoncompany.engine.utils.Transformation;
import com.casoncompany.engine.utils.Utils;

public class Renderer {
	
	public static final Vector3f AMBIENT_LIGHT = new Vector3f(0.5f, 0.5f, 0.5f);
	public static final float SPECULAR_POWER = 10.0f;
	
	private final Window window;
	private EntityRenderer entityRenderer;
	private TerrainRenderer terrainRenderer;
			
	public Renderer(Window window) {
		this.window = window;
	}
	
	public void init() throws Exception {
		entityRenderer = new EntityRenderer(window);
		terrainRenderer = new TerrainRenderer(window);
		entityRenderer.init();
		terrainRenderer.init();
		
	}
	
	public void update() {
	}
	
	public static void renderLights(PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight, Shader shader) {
		shader.setUniform("ambientLight", AMBIENT_LIGHT);
		shader.setUniform("specularPower", SPECULAR_POWER);
		
		int numLights = spotLights != null ? spotLights.length : 0;
		for(int i = 0; i < numLights; i++)
			shader.setUniform("spotLights", spotLights[i], i);
		
		numLights = pointLights != null ? pointLights.length : 0;
		for(int i = 0; i < numLights; i++)
			shader.setUniform("pointLights", pointLights[i], i);
		
		shader.setUniform("directionalLight", directionalLight);
	}
	
	public void render(Camera camera, SceneManager sceneManager) {
		clear();
		
		if(window.isResize()) {
			GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResize(true);
		}
		
		entityRenderer.render(camera, sceneManager.getPointLights(), sceneManager.getSpotLights(), sceneManager.getDirectionalLight());
		terrainRenderer.render(camera, sceneManager.getPointLights(), sceneManager.getSpotLights(), sceneManager.getDirectionalLight());
		
	}
	
	public void processEntities(Entity entity) {
		List<Entity> entityList = entityRenderer.getEntities().get(entity.getModel());
		
		if(entityList != null)
			entityList.add(entity);
		else {
			List<Entity> newEntityList = new ArrayList<>();
			newEntityList.add(entity);
			entityRenderer.getEntities().put(entity.getModel(), newEntityList);
		}
	}
	
	public void processTerrain(Terrain terrain) {
		terrainRenderer.getTerrain().add(terrain);
	}
	
	public void clear() {
		window.setClearColor(0.0f,  0.0f, 0.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void cleanup() {
		entityRenderer.cleanup();
		terrainRenderer.cleanup();
	}

}
