package com.casoncompany.engine.entity;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import com.casoncompany.engine.lighting.DirectionalLight;
import com.casoncompany.engine.lighting.PointLight;
import com.casoncompany.engine.lighting.SpotLight;
import com.casoncompany.engine.renderer.Terrain;

public class SceneManager {
	
	public static final Vector3f AMBIENT_LIGHT = new Vector3f(0.5f, 0.5f, 0.5f);
	
	private List<Terrain> terrains;
	
	private Vector3f ambientLight;
	private SpotLight[] spotLights;
	private PointLight[] pointLights;
	private DirectionalLight directionalLight;
	
	private float lightAngle;
	private float spotAngle = 0;
	private float spotInc = 1;
	
	public SceneManager(float lightAngle) {
		entities = new ArrayList<>();
		terrains = new ArrayList<>();
		ambientLight = AMBIENT_LIGHT;
		this.lightAngle = lightAngle;
	}
	
	private List<Entity> entities;
	public List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	public List<Terrain> getTerrains() {
		return terrains;
	}

	public void setTerrains(List<Terrain> terrains) {
		this.terrains = terrains;
	}
	
	public void addTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public Vector3f getAmbientLight() {
		return ambientLight;
	}

	public void setAmbientLight(Vector3f ambientLight) {
		this.ambientLight = ambientLight;
	}
	
	public void setAmbientLight(float x, float y, float z) {
		ambientLight = new Vector3f(x,y,z);
	}

	public SpotLight[] getSpotLights() {
		return spotLights;
	}

	public void setSpotLights(SpotLight[] spotLights) {
		this.spotLights = spotLights;
	}

	public PointLight[] getPointLights() {
		return pointLights;
	}

	public void setPointLights(PointLight[] pointLights) {
		this.pointLights = pointLights;
	}

	public DirectionalLight getDirectionalLight() {
		return directionalLight;
	}

	public void setDirectionalLight(DirectionalLight directionalLight) {
		this.directionalLight = directionalLight;
	}

	public float getLightAngle() {
		return lightAngle;
	}

	public void setLightAngle(float lightAngle) {
		this.lightAngle = lightAngle;
	}
	
	public void incLightAngle(float inc) {
		this.lightAngle+=inc;
	}

	public float getSpotAngle() {
		return spotAngle;
	}

	public void setSpotAngle(float spotAngle) {
		this.spotAngle = spotAngle;
	}

	public float getSpotInc() {
		return spotInc;
	}

	public void setSpotInc(float spotInc) {
		this.spotInc = spotInc;
	}
	
	public void incSpotAngle(float inc) {
		this.spotAngle*=inc;
	}

}
