package pgdp.game.mock;

import static org.junit.jupiter.api.Assertions.fail;

import pgdp.game.PinguGame;

public class PinguGameRollDiceMock extends PinguGame {

	public PinguGameRollDiceMock() {
		super(false);
	}

	public PinguGameRollDiceMock(boolean renderBW) {
		super(renderBW);
	}

	public PinguGameRollDiceMock(boolean renderBW, long randomSeed, int renderDelay) {
		super(renderBW, randomSeed, renderDelay);
	}

	private int[] diceValues;
	private int idx = 0;

	/**
	 * rolls a dice
	 * @return an integer in [1,6]
	 */
	@Override
	protected int rollDice() {
		if (idx >= diceValues.length) {
			fail("Trying to roll dice more often than expceted by tests.");
		}
		return diceValues[idx++];
	}

	public void setDiceValues(int[] values) {
		diceValues = values;
	}
}
