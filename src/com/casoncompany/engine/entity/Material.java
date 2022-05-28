package com.casoncompany.engine.entity;

import org.joml.Vector4f;

public class Material {
	
	public static final Vector4f DEFAULT_COLOR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
	
	private Vector4f ambientColor;
	private Vector4f diffuseColor;
	private Vector4f specularColor;
	
	private float reflectance;
	private Texture texture;
	
	private boolean disableCulling;
	
	public Material() {
		this.setAmbientColor(DEFAULT_COLOR);
		this.setDiffuseColor(DEFAULT_COLOR);
		this.setSpecularColor(DEFAULT_COLOR);
		this.setTexture(null);
		this.setReflectance(0);
		this.setDisableCulling(false);
	}
	
	public Material(Vector4f color, float reflectance) {
		this(color, color, color, reflectance, null);
	}
	
	public Material(Vector4f color, float reflectance, Texture texture) {
		this(color, color, color, reflectance, texture);
	}
	
	public Material(Texture texture) {
		this(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR, 0, texture);
	}
	
	public Material(Texture texture, float reflectance) {
		this(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR, reflectance, texture);
	}
	
	public Material(Vector4f ambientColor, Vector4f diffuseColor, Vector4f specularColor, float reflectance, Texture texture) {
		this.setAmbientColor(ambientColor);
		this.setDiffuseColor(diffuseColor);
		this.setSpecularColor(specularColor);
		this.setReflectance(reflectance);
		this.setTexture(texture);
	}

	public Vector4f getAmbientColor() {
		return ambientColor;
	}

	public void setAmbientColor(Vector4f ambientColor) {
		this.ambientColor = ambientColor;
	}

	public Vector4f getDiffuseColor() {
		return diffuseColor;
	}

	public void setDiffuseColor(Vector4f diffuseColor) {
		this.diffuseColor = diffuseColor;
	}

	public Vector4f getSpecularColor() {
		return specularColor;
	}

	public void setSpecularColor(Vector4f specularColor) {
		this.specularColor = specularColor;
	}

	public float getReflectance() {
		return reflectance;
	}

	public void setReflectance(float reflectance) {
		this.reflectance = reflectance;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public boolean hasTexture() {
		return texture != null;
	}

	public boolean getDisableCulling() {
		return disableCulling;
	}

	public void setDisableCulling(boolean disableCulling) {
		this.disableCulling = disableCulling;
	}


}
