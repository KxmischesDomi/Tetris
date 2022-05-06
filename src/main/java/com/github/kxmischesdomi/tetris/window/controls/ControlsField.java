package com.github.kxmischesdomi.tetris.window.controls;

import com.github.kxmischesdomi.tetris.types.AbstractField;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ControlsField extends AbstractField {

	private PImage wKey, aKey, sKey, dKey, shiftKey, escKey, rKey,
			plusKey, slashKey, upKey, downKey, leftKey, rightKey, spaceKey;

	private static final float tileSize = 100;

	public ControlsField(int x, int y) {
		super(x, y);
	}

	@Override
	public void drawField(PApplet window) {
		if (!gamePaused) {
			if (!drawControls) {
				return;
			}
		}

		window.textAlign(PConstants.LEFT);
		window.textSize(50);
		window.scale(0.5f, 0.5f);

		try {
			drawKeyImage(window, wKey, 0, 8, "");
			drawKeyImage(window, slashKey, 1, 8, "");
			drawKeyImage(window, upKey, 2, 8, "Rotate");

			drawKeyImage(window, aKey, 0, 7, "");
			drawKeyImage(window, slashKey, 1, 7, "");
			drawKeyImage(window, leftKey, 2, 7, "Move Left");

			drawKeyImage(window, dKey, 0, 6, "");
			drawKeyImage(window, slashKey, 1, 6, "");
			drawKeyImage(window, rightKey, 2, 6, "Move Right");

			drawKeyImage(window, sKey, 0, 5, "");
			drawKeyImage(window, slashKey, 1, 5, "");
			drawKeyImage(window, downKey, 2, 5, "Move Down");

			drawKeyImage(window, shiftKey, 0, 4, "Finish Tile");
			drawKeyImage(window, rKey, 0, 3, "Reset");
			drawKeyImage(window, escKey, 0, 2, "Pause");

			drawKeyImage(window, escKey, 0, 1, "");
			drawKeyImage(window, plusKey, 1, 1, "");
			drawKeyImage(window, shiftKey, 2, 1, "Quit");
		} catch (Exception exception) {
			System.err.println("A control image is missing");
			exception.printStackTrace();
		}
	}

	@Override
	public void init(PApplet window) {
		wKey = window.loadImage("w_key.png");
		aKey = window.loadImage("a_key.png");
		sKey = window.loadImage("s_key.png");
		dKey = window.loadImage("d_key.png");
		rKey = window.loadImage("r_key.png");
		shiftKey = window.loadImage("shift_key.png");
		escKey = window.loadImage("esc_key.png");
		plusKey = window.loadImage("plus_key.png");
		slashKey = window.loadImage("slash_key.png");
		upKey = window.loadImage("arrow_up_key.png");
		downKey = window.loadImage("arrow_down_key.png");
		leftKey = window.loadImage("arrow_left_key.png");
		rightKey = window.loadImage("arrow_right_key.png");
		spaceKey = window.loadImage("space_key.png");
	}

	public void drawKeyImage(PApplet window, PImage image, int x, int y, String text) {
		if (!text.isEmpty()) {
			window.text(text, x*tileSize + tileSize + 10, -y*tileSize / 1.2f + 65);
		}
		window.image(image, x*tileSize, -y*tileSize / 1.2f, tileSize, tileSize);
	}

}
