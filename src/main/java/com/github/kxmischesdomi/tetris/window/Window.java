package com.github.kxmischesdomi.tetris.window;

import com.github.kxmischesdomi.tetris.types.AbstractGame;
import com.github.kxmischesdomi.tetris.window.controls.ControlsField;
import com.github.kxmischesdomi.tetris.window.game.GameField;
import com.github.kxmischesdomi.tetris.window.pause.PauseField;
import com.github.kxmischesdomi.tetris.window.score.ScoreField;
import processing.core.PImage;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class Window extends AbstractGame {

	private int pixelWidth, pixelHeight;

	@Override
	public void settings() {
		pixelWidth = 1920;
		pixelHeight = 1080;
		fullScreen();
//		size(pixelWidth, pixelHeight);

		ScoreField scoreField = new ScoreField(0, 0);
		addField(new GameField(displayWidth / 2, displayHeight / 2, scoreField));
		addField(scoreField);
		addField(new PauseField(0, 0));
		addField(new ControlsField(0, displayHeight - 10));

		super.settings();
	}

	@Override
	public void draw() {
		background(20);
		scale(pixelWidth / 1920f, pixelWidth / 1920f);
		super.draw();
	}
}
