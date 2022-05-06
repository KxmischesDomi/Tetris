package com.github.kxmischesdomi.tetris.types;

import processing.core.PApplet;
import processing.data.JSONObject;
import processing.event.KeyEvent;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public abstract class AbstractGame extends PApplet {

	private final List<IGameField> fields = new LinkedList<>();

	@Override
	public void settings() {
		loadGameData();

		Runtime.getRuntime().addShutdownHook(new Thread(this::saveGameData));
	}

	@Override
	public void draw() {
		for (IGameField field : fields) {
			pushMatrix();
			field.draw(this);
			popMatrix();
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		super.keyPressed(event);
		for (IGameField field : fields) {
			field.keyPressed(event, this);
		}
	}

	protected void addField(IGameField field) {
		field.init(this);
		fields.add(field);
	}

	private void loadGameData() {
		try {
			JSONObject jsonObject = loadJSONObject("data.json");

			if (jsonObject == null) return;

			for (IGameField field : fields) {
				field.loadGameData(jsonObject);
			}
		} catch (Exception ignored) {
			// No data
		}

	}

	private void saveGameData() {
		JSONObject object = new JSONObject();

		for (IGameField field : fields) {
			field.saveGameData(object);
		}

		saveJSONObject(object, "data.json");
	}

	@Override
	public void exit() {
		saveGameData();
		super.exit();
	}
}
