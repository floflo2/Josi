package pgdp.security.oracles;

import static pgdp.security.TestHelper.*;

public class FinishPostOracle extends FlagPostOracle {

	public FinishPostOracle(int postNumber) {
		super(postNumber);
	}

	@Override
	public boolean up(String type) {
		if (type.contains("end") && getLevel() != 5) {
			setDepiction("chequered");
			setLevel(5);
			return true;
		}
		return super.up(type);
	}

	@Override
	public String toString() {
		return "Signal post " + getPostNumber() + " of type finish post is in level " + getLevel() + " and is "
				+ (getLevel() == 0 ? " doing nothing" : " waving  " + getDepiction());
	}

	public String toStringColored() {
		return "Signal post " + getPostNumber() + " of type finish post is in level " + getLevel() + " and is "
				+ (getLevel() == 0 ? " doing nothing" : " waving  " + changeColors(getDepiction()));
	}
}
