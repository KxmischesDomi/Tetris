package com.github.kxmischesdomi.tetris.window.game;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public enum Shape {

	L(new int[][] {
		{0, 1, 0},
		{0, 1, 0},
		{0, 1, 1}
	}, false),
	REVERSE(new int[][] {
		{0, 1, 0},
		{0, 1, 0},
		{1, 1, 0}
	}, false),
	STRAIGHT(new int[][] {
			{0, 0, 0, 0},
			{1, 1, 1, 1},
			{0, 0, 0, 0},
			{0, 0, 0, 0}
	}, false, false),
	SQUARE(new int[][] {
			{0, 0, 0, 0},
			{0, 1, 1, 0},
			{0, 1, 1, 0},
			{0, 0, 0, 0}
	}, false, false),
	T(new int[][] {
			{0, 0, 0},
			{1, 1, 1},
			{0, 1, 0}
	}, false),
	Z(new int[][] {
			{0, 1, 1},
			{1, 1, 0},
			{0, 0, 0}
	}, true),
	X(new int[][] {
			{0, 1, 0},
			{1, 1, 1},
			{0, 1, 0}
	}, true, false),
	LONG_L(new int[][] {
			{0, 0, 0, 0},
			{0, 0, 0, 1},
			{1, 1, 1, 1},
			{0, 0, 0, 0}
	}, true),
	U(new int[][] {
			{0, 0, 0},
			{1, 0, 1},
			{1, 1, 1}
	}, true),
	LONG_T(new int[][] {
			{1, 1, 1},
			{0, 1, 0},
			{0, 1, 0}
	}, true)
	;

	private final int[][] blockPositions;
	private final boolean advanced;
	private boolean doubleRotation = true;

	Shape(int[][] blockPositions, boolean advanced) {
		this.blockPositions = blockPositions;
		this.advanced = advanced;
	}

	Shape(int[][] blockPositions, boolean advanced, boolean doubleRotation) {
		this.blockPositions = blockPositions;
		this.advanced = advanced;
		this.doubleRotation = doubleRotation;
	}


	public int[][] getBlockPositions(int rotation) {
		int[][] positions = getBlockPositions();

		if ((rotation == 3 || rotation == 2) && !doubleRotation) {
			if (rotation == 3) {
				positions = rotateClockwise(positions);
			}
			return positions;
		}

		switch (rotation) {
			case 3: positions = rotateClockwise(positions);
			case 2: positions = rotateClockwise(positions);
			case 1: positions = rotateClockwise(positions);
		}

		return positions;
	}

	static int[][] rotateClockwise(int[][] matrix) {
		int rowNum = matrix.length;
		int colNum = matrix[0].length;
		int[][] temp = new int[rowNum][colNum];
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				temp[i][j] = matrix[rowNum - j - 1][i];
			}
		}
		return temp;
	}

	public int[][] getBlockPositions() {
		return blockPositions;
	}

	public boolean isAdvanced() {
		return advanced;
	}

}
