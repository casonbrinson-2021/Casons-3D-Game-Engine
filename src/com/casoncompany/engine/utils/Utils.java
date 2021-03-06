package com.casoncompany.engine.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.lwjgl.system.MemoryUtil;

public class Utils {
	
	public static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
		buffer.put(data).flip();
		return buffer;
	}
	
	public static IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
		buffer.put(data).flip();
		return buffer;
	}
	
	public static String loadResource(String filename) throws Exception {
		String result;
		
		try(InputStream in = Utils.class.getResourceAsStream(filename)) {
			Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name());
			result = scanner.useDelimiter("\\A").next();
			//scanner.close();
		}
		
		return result;
	}
	
	public static List<String> readAllLines(String filename) {
		List<String> list = new ArrayList<>();
		
		try(BufferedReader buffer = new BufferedReader(new InputStreamReader(Class.forName(Utils.class.getName()).getResourceAsStream(filename)))) {
			String line;
			while((line = buffer.readLine()) != null) {
				list.add(line);
			}
		} catch(IOException | ClassNotFoundException e) {
			System.err.println("Error reading all lines from file");
			e.printStackTrace();
		}
		
		return list;
	}

}
