package com.github.kxmischesdomi.tetris.window.score;

import com.github.kxmischesdomi.tetris.types.AbstractField;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.data.JSONObject;
import processing.event.KeyEvent;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class ScoreField extends AbstractField {

	private int score;
	private int highscore;

	public ScoreField(int x, int y) {
		super(x, y);
	}

	@Override
	public void drawField(PApplet window) {
		window.pushMatrix();
		window.textSize(80);
		window.textAlign(PConstants.CENTER);
		window.translate(window.displayWidth / 2f, 70);
		window.text(score, 0, 0);
		window.popMatrix();

		window.textSize(60);
		window.textAlign(PConstants.LEFT);
		window.translate(10, 70);
		window.text("Highscore: " + highscore, 0, 0);

//		window.translate(0, 70);
//		window.text("Level: " + (getLevel() + 1), 0, 0);
//
//		window.translate(0, 70);
//		window.text("Next Level: " + getScoreToNextLevel(), 0, 0);
	}

	@Override
	public void init(PApplet window) {

	}

	@Override
	public void saveGameData(JSONObject jsonObject) {
		jsonObject.setInt("score", score);
		jsonObject.setInt("highscore", highscore);
	}

	@Override
	public void loadGameData(JSONObject jsonObject) {
		this.score = jsonObject.getInt("score");
		this.highscore = jsonObject.getInt("highscore");
	}

	public int getScorePerRow() {
		return 100;
	}

	public void addScore() {
		this.score += getScorePerRow();
	}

	public int getScore() {
		return score;
	}

	public int getScoreToNextLevel() {
		return (getLevel()+1) * getScorePerLevel() - getScore();
	}

	public int getLevel() {
		return getScore() / getScorePerLevel();
	}

	public int getScorePerLevel() {
		return getScorePerRow() * 10;
	}

	public void reset() {
		if (this.score > highscore) {
			this.highscore = score;
		}
		this.score = 0;
	}

}
