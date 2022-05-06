package com.github.kxmischesdomi.tetris.window.game;

import java.util.Objects;

public record BlockPos(int x, int y) {

	public BlockPos add(int x, int y) {
		return new BlockPos(this.x + x, this.y + y);
	}

	public BlockPos subtract(int x, int y) {
		return new BlockPos(this.x - x, this.y - y);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BlockPos blockPos = (BlockPos) o;
		return x == blockPos.x && y == blockPos.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

}
