package com.github.kxmischesdomi.tetris.window.game;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class FallingBlock {

	private final Shape shape;
	private final Color color;
	private BlockPos blockPos;
	private int rotation;

	public FallingBlock(Shape shape, BlockPos blockPos, Color color) {
		this.shape = shape;
		this.blockPos = blockPos;
		this.color = color;
		rotation = 0;
	}

	public BlockPos pos() {
		return blockPos;
	}

	public List<BlockPos> translatedPositions(BlockPos origin) {
		return translatedPositions(origin, rotation);
	}


	public List<BlockPos> translatedPositions(BlockPos origin, int rotation) {
		int[][] positions = shape.getBlockPositions(rotation);

		List<BlockPos> list = new LinkedList<>();

		int centerY = positions.length / 2;

		for (int y = 0; y < positions.length; y++) {
			int[] position = positions[y];
			int centerX = position.length / 2;

			for (int x = 0; x < position.length; x++) {
				int val = position[x];

				if (val == 0) continue;

				BlockPos pos = origin.add(x - centerX, y - centerY);
				list.add(pos);

			}

		}

		return list;
	}

	public Shape shape() {
		return shape;
	}

	public int rotation() {
		return rotation;
	}

	public Color color() {
		return color;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public void setPos(BlockPos blockPos) {
		this.blockPos = blockPos;
	}

}
