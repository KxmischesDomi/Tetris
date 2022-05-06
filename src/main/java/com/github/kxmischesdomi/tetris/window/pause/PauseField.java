package com.github.kxmischesdomi.tetris.window.pause;

import com.github.kxmischesdomi.tetris.types.AbstractField;
import com.github.kxmischesdomi.tetris.types.AbstractGame;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.event.KeyEvent;

import java.awt.*;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class PauseField extends AbstractField {

	public PauseField(int x, int y) {
		super(x, y);
	}

	@Override
	public void drawField(PApplet window) {

		if (gamePaused) {
			window.pushStyle();
			window.fill(Color.BLACK.brighter().getRGB(), 127);
			window.rect(0, 0, window.displayWidth, window.displayHeight);
			window.popStyle();

			window.textAlign(PConstants.CENTER, PConstants.CENTER);
			window.textSize(100);
			window.text("Paused [ESC]", window.displayWidth / 2f, window.displayHeight / 2f);
		}

	}

	@Override
	public void init(PApplet window) {

	}

	@Override
	public void keyPressed(KeyEvent event, PApplet window) {

		if (event.isShiftDown() && gamePaused) {
			window.exit();
			return;
		}

		if (event.getKeyCode() == 27) {
			gamePaused = !gamePaused;
			window.key = 0;
		}

	}

}
