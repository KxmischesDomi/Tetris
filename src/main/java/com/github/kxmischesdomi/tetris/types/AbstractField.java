package com.github.kxmischesdomi.tetris.types;

import processing.core.PApplet;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public abstract class AbstractField implements IGameField {

	protected static boolean gamePaused, drawControls = true;

	private final int x, y;

	public AbstractField(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public final void draw(PApplet window) {
		window.pushMatrix();
		window.pushStyle();
		window.translate(x, y);
		drawField(window);
		window.popMatrix();
		window.popStyle();
	}

	public abstract void drawField(PApplet window);

}
