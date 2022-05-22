package com.casoncompany.engine.lighting;

import org.joml.Vector3f;

public class DirectionalLight {
	
	private Vector3f color;
	private Vector3f direction;
	private float intensity;
	
	public DirectionalLight(Vector3f color, Vector3f direction, float intensity) {
		this.setColor(color);
		this.setDirection(direction);
		this.setIntensity(intensity);
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public Vector3f getDirection() {
		return direction;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

}
