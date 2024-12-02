package pgdp.security.oracles;

import static pgdp.security.TestHelper.*;

public class PostOracle extends SignalPostOracle {

	public PostOracle(int postNumber) {
		super(postNumber);
	}

	@Override
	public boolean up(String type) {
		return false;
	}

	@Override
	public boolean down(String type) {
		return false;
	}

	@Override
	public String toStringColored() {
		return "Signal Post " + getPostNumber() + ": " + getLevel() + " " + changeColors(getDepiction());
	}
}
