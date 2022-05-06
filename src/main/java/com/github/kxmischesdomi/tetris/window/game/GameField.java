package com.github.kxmischesdomi.tetris.window.game;

import com.github.kxmischesdomi.tetris.types.AbstractField;
import com.github.kxmischesdomi.tetris.window.score.ScoreField;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;

import javax.lang.model.element.ElementVisitor;
import java.awt.*;
import java.awt.geom.PathIterator;
import java.util.*;
import java.util.List;

/**
 * @author KxmischesDomi | https://github.com/kxmischesdomi
 * @since 1.0
 */
public class GameField extends AbstractField {

	private static final String tilePath = "tile.png";

	private static final int gridWidth = 10, gridHeight = 20;
	private static final float tileSize = 40;
	private static final int advancedShapesLevel = 3;

	private final Map<BlockPos, Block> landedBlocks = new HashMap<>();
	private final ScoreField scoreField;
	private final Random random = new Random();

	private PImage tileImg;

	private FallingBlock fallingBlock;
	private FallingBlock nextBlock;
	private FallingBlock storageBlock;
	private long lastBlockDown = -1;

	public GameField(int x, int y, ScoreField scoreField) {
		super(x, y);
		this.scoreField = scoreField;
	}

	private Block getBlockAt(int x, int y) {
		return this.getBlockAt(new BlockPos(x, y));
	}

	private int getFirstHeightEmpty(int x, int startY) {

		for (int y = startY; y >= 0; y--) {

			if (getBlockAt(x, y) != null) {
				return y+1;
			}

		}

		return 0;
	}

	private Block getBlockAt(BlockPos pos) {
		return landedBlocks.get(pos);
	}

	private void setBlockAt(int x, int y, Block block) {
		setBlockAt(new BlockPos(x, y), block);
	}
	
	private void setBlockAt(BlockPos pos, Block block) {
		int x = pos.x();
		int y = pos.y();
		if (x < 0 || x >= gridWidth) {
			throw new IllegalArgumentException("Width is out of range 0-" + (gridWidth-1) + " got: " + x);
		}
		if (y < 0 || y >= gridHeight) {
			throw new IllegalArgumentException("Height is out of range 0-" + (gridHeight-1) + " got: " + y);
		}
		if (block == null) {
			landedBlocks.remove(pos);
		} else {
			landedBlocks.put(pos, block);
		}
	}

	private boolean checkBlocksToBreak(int y) {

		for (int x = 0; x < gridWidth; x++) {
			if (getBlockAt(x, y) == null) return false;
		}

		scoreField.addScore();

		for (int x = 0; x < gridWidth; x++) {
			setBlockAt(x, y, null);
		}

		for (int currentY = y; currentY < gridHeight; currentY++) {
			for (int x = 0; x < gridWidth; x++) {
				Block blockAt = getBlockAt(x, currentY);
				if (blockAt != null) {
					setBlockAt(x, currentY, null);
					setBlockAt(x, currentY-1, blockAt);
//					setBlockAt(x, getFirstHeightEmpty(x, currentY-1), blockAt);
				}
			}
		}
		return true;
	}

	private void applyLogic() {

		if (fallingBlock == null) {
			spawnFallingBlock();
		} else if (lastBlockDown != -1) {
			float difference = System.currentTimeMillis() - lastBlockDown;
			if (difference >= getPieceSpeed()) {
				lastBlockDown = System.currentTimeMillis();
				moveFallingBlockDown();
			}
		}

	}

	/**
	 * @return the time between the movement of the falling block. A value between 333 and 1000
	 */
	private int getPieceSpeed() {
//		int level = scoreField.getLevel();
//		return 1000 - (Math.max(333, level * 30));
		return 1000;
	}

	private void spawnFallingBlock() {

		if (fallingBlock != null) {
			List<BlockPos> list = fallingBlock.translatedPositions(fallingBlock.pos());
			for (BlockPos position : list) {
				setBlockAt(position, new Block(fallingBlock.color()));
			}

			for (int y = 0; y < gridHeight; y++) {
				if (checkBlocksToBreak(y)) {
					y--;
				}
			}

			lastBlockDown = System.currentTimeMillis();
		}

		BlockPos spawnPos = new BlockPos(gridWidth / 2, gridHeight - 3);

		fallingBlock = nextBlock;
		nextBlock = new FallingBlock(getRandomShape(), spawnPos, getRandomColor());

	}

	private Shape getRandomShape() {

//		if (scoreField.getLevel() >= advancedShapesLevel) {
//			return Shape.values()[random.nextInt(Shape.values().length)];
//		}
		ArrayList<Shape> list = new ArrayList<>(Arrays.asList(Shape.values()));
		list.removeIf(Shape::isAdvanced);
		return list.get(random.nextInt(list.size()));
	}

	private boolean fallingBlockFits(BlockPos origin, int rotation) {

		for (BlockPos position : fallingBlock.translatedPositions(origin, rotation)) {
			if (position.x() > gridWidth-1 || position.x() < 0) {
				return false;
			}
			if (position.y() < 0) {
				return false;
			}
			if (getBlockAt(position) != null) {
				return false;
			}
		}
		return true;
	}

	private boolean lost(BlockPos origin) {

		for (BlockPos position : fallingBlock.translatedPositions(origin, fallingBlock.rotation())) {

			if (position.y() >= gridHeight-5) {
				return true;
			}

		}

		return false;
	}

	private void moveFallingBlockSideways(int x) {

		BlockPos newPos = fallingBlock.pos().add(x, 0);

		if (!fallingBlockFits(newPos, fallingBlock.rotation())) {
			return;
		}
		fallingBlock.setPos(newPos);
	}

	private void moveFallingBlockDown() {

		BlockPos newPos = fallingBlock.pos().subtract(0, 1);

		if (!fallingBlockFits(newPos, fallingBlock.rotation())) {
			if (lost(newPos)) {
				reset();
			} else {
				spawnFallingBlock();
			}
			return;
		}

		if (newPos.y() < 0) {
			spawnFallingBlock();
			return;
		}
		fallingBlock.setPos(newPos);

		if (!fallingBlockFits(fallingBlock.pos().subtract(0, 1), fallingBlock.rotation())) {
			spawnFallingBlock();
		}

	}

	public void moveFallingBlockToFloor() {

		FallingBlock fallingBlock = this.fallingBlock;

		for (int i = fallingBlock.pos().y(); i > 0; i++) {
			if (this.fallingBlock != fallingBlock) {
				break;
			}
			moveFallingBlockDown();
		}

	}

	private Color getRandomColor() {
		return Color.getHSBColor(random.nextFloat(), 1, 1);
	}

	@Override
	public void init(PApplet window) {
		tileImg = window.loadImage(tilePath);
	}

	@Override
	public void loadGameData(JSONObject jsonObject) {
		if (!jsonObject.hasKey("fallingBlock")) return;

		try {
			JSONObject fallingBlockData = jsonObject.getJSONObject("fallingBlock");
			int y = fallingBlockData.getInt("y");
			int x = fallingBlockData.getInt("x");
			int colorRGB = fallingBlockData.getInt("color");
			Color color = Color.getColor(null, colorRGB);
			String shapeName = fallingBlockData.getString("shape");
			Shape shape = Shape.valueOf(shapeName);
			fallingBlock = new FallingBlock(shape, new BlockPos(x, y), color);

			JSONObject nextBlockData = jsonObject.getJSONObject("nextBlock");
			y = nextBlockData.getInt("y");
			x = nextBlockData.getInt("x");
			colorRGB = nextBlockData.getInt("color");
			color = Color.getColor(null, colorRGB);
			shapeName = nextBlockData.getString("shape");
			shape = Shape.valueOf(shapeName);
			nextBlock = new FallingBlock(shape, new BlockPos(x, y), color);

			JSONArray blocks = jsonObject.getJSONArray("blocks");
			for (int i = 0; i < blocks.size(); i++) {
				JSONObject o = (JSONObject) blocks.get(i);
				y = o.getInt("y");
				x = o.getInt("x");
				colorRGB = o.getInt("color");
				color = Color.getColor(null, colorRGB);
				setBlockAt(x, y, new Block(color));
			}

		} catch (Exception exception) {
			System.err.println("Error loading game data");
			exception.printStackTrace();
		}

	}

	@Override
	public void saveGameData(JSONObject jsonObject) {

		JSONObject fallingBlockData = createBlockData(fallingBlock.pos(), fallingBlock.color());
		fallingBlockData.setString("shape", fallingBlock.shape().name());
		jsonObject.setJSONObject("fallingBlock", fallingBlockData);

		JSONObject nextBlockData = createBlockData(nextBlock.pos(), nextBlock.color());
		nextBlockData.setString("shape", nextBlock.shape().name());
		jsonObject.setJSONObject("nextBlock", nextBlockData);

		JSONArray blocks = new JSONArray();
		landedBlocks.forEach((pos, block) -> {
			JSONObject currentBlock = createBlockData(pos, block.color());
			blocks.append(currentBlock);
		});
		jsonObject.setJSONArray("blocks", blocks);

	}

	private JSONObject createBlockData(BlockPos pos, Color color) {
		JSONObject object = new JSONObject();
		object.setInt("x", pos.x());
		object.setInt("y", pos.y());
		object.setInt("color", color.getRGB());
		return object;
	}

	private void reset() {
		landedBlocks.clear();
		fallingBlock = null;
		scoreField.reset();
	}

	@Override
	public void drawField(PApplet window) {

		// Logic stuff
		if (!gamePaused) {
			applyLogic();
		}

		// Rendering stuff
		window.translate(-tileSize*gridWidth/2, -gridHeight*tileSize/2);
		drawBackground(window);
		drawLandedBlocks(window);
		drawPredictedLanding(window);
		drawFallingBlock(window);
		drawNextBlock(window);
		drawStoragedBlock(window);
		drawStartText(window);
	}

	@Override
	public void keyPressed(KeyEvent event, PApplet window) {
		if (gamePaused) return;

		boolean startGame = false;
		if (event.getKey() == 'r') {
			reset();

		} else if (event.getKey() == 'w' || event.getKeyCode() == PConstants.UP) {
			startGame = true;
			int newRotation = (fallingBlock.rotation() + 1) % 4;
			if (!fallingBlockFits(fallingBlock.pos(), newRotation)) {
				return;
			}
			fallingBlock.setRotation(newRotation);

		} else if (event.getKey() == 's' || event.getKeyCode() == PConstants.DOWN) {
			startGame = true;
			moveFallingBlockDown();

		} else if (event.getKey() == 'a' || event.getKeyCode() == PConstants.LEFT) {
			startGame = true;
			moveFallingBlockSideways(-1);
		} else if (event.getKey() == 'd' || event.getKeyCode() == PConstants.RIGHT) {
			startGame = true;
			moveFallingBlockSideways(1);
//		} else if (event.getKey() == 'f') {
//			if (storageBlock != null) {
//				fallingBlock = storageBlock;
//				storageBlock = null;
//			} else {
//				BlockPos oldPos = fallingBlock.pos().add(0, 0);
//				storageBlock = fallingBlock;
//				fallingBlock = null;
//				spawnFallingBlock();
//				fallingBlock.setPos(oldPos);
//			}

		} else if (event.isShiftDown()) {
			startGame = true;
			moveFallingBlockToFloor();
		}


		if (startGame) {
			if (lastBlockDown == -1) {
				drawControls = false;
				lastBlockDown = System.currentTimeMillis();
			}
		}

	}

	private void drawStartText(PApplet window) {
		if (lastBlockDown != -1) return;

		window.pushStyle();
		window.textAlign(PConstants.CENTER, PConstants.CENTER);
		window.translate(tileSize*gridWidth/2, tileSize*gridHeight/4);
		window.textSize(50);
		window.text("Press any key to start", 0, 0);
		window.popStyle();

	}

	private void drawPredictedLanding(PApplet window) {
		if (fallingBlock == null) return;

		for (int i = 0; i <= fallingBlock.pos().y()+1; i++) {

			if (!fallingBlockFits(fallingBlock.pos().subtract(0, i), fallingBlock.rotation())) {

				for (BlockPos position : fallingBlock.translatedPositions(fallingBlock.pos().subtract(0, i-1))) {
					drawTileTransparency(window, fallingBlock.color().brighter(), 20, position.x(), position.y());
				}
				break;
			}
		}

	}

	private void drawFallingBlock(PApplet window) {
		if (fallingBlock == null) return;

		List<BlockPos> pos = fallingBlock.translatedPositions(fallingBlock.pos());

		for (BlockPos position : pos) {
			drawTile(window, fallingBlock.color(), position.x(), position.y());
		}

	}

	private void drawStoragedBlock(PApplet window) {
		if (storageBlock == null) return;

		List<BlockPos> pos = storageBlock.translatedPositions(new BlockPos(gridWidth + 4, gridHeight - 7), storageBlock.rotation());

		for (BlockPos position : pos) {
			drawTile(window, storageBlock.color(), position.x(), position.y());
		}

	}

	private void drawNextBlock(PApplet window) {
		if (nextBlock == null) return;

		List<BlockPos> pos = nextBlock.translatedPositions(new BlockPos(gridWidth + 4, gridHeight - 3));

		for (BlockPos position : pos) {
			drawTile(window, nextBlock.color(), position.x(), position.y());
		}

	}

	private void drawLandedBlocks(PApplet window) {

		landedBlocks.forEach((blockPos, block) -> {
			drawTile(window, block.color(), blockPos.x(), blockPos.y());
		});

	}

	private void drawBackground(PApplet window) {

		for (int y = -1; y <= gridHeight; y++) {

			if (y == -1 || y == gridHeight) {
				for (int x = 0; x < gridWidth; x++) {
					drawBgTile(window, x, y);
				}
			}

			drawBgTile(window, gridWidth, y);
			drawBgTile(window, -1, y);
		}

	}

	private void drawBgTile(PApplet window, int x, int y) {
		if (y >= gridHeight - 4) {
			drawTile(window, Color.DARK_GRAY.darker(), x, y);
		} else {
			drawTile(window, Color.DARK_GRAY, x, y);
		}
	}

	private void drawTile(PApplet window, Color color, int x, int y) {
		this.drawTileTransparency(window, color, 255, x, y);
	}

	private void drawTileTransparency(PApplet window, Color color, float transparency, int x, int y) {
		window.pushStyle();
		window.tint(color.getRGB(), transparency);
		window.image(tileImg, x*tileSize, translateYForDrawing(y)*tileSize, tileSize, tileSize);
		window.popStyle();
	}

	/**
	 * Translates the code intern y coordinate to the renderer y
	 */
	private int translateYForDrawing(int y) {
		return gridHeight - 1 - y;
	}

}
