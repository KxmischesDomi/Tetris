package com.github.kxmischesdomi.tetris.types;

import processing.core.PApplet;
import processing.data.JSONObject;
import processing.event.KeyEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public interface IGameField {

	void init(PApplet window);

	void draw(PApplet window);

	default void keyPressed(KeyEvent event, PApplet window) { }

	default void saveGameData(JSONObject jsonObject) { }

	default void loadGameData(JSONObject jsonObject) { }

}
