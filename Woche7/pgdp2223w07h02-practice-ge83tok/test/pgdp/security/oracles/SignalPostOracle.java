package pgdp.security.oracles;

import pgdp.security.*;

public abstract class SignalPostOracle extends SignalPost {

	private int postNumberOracle;
	private String depictionOracle = "";
	private int levelOracle = 0;

	public SignalPostOracle(int postNumber) {
		super(postNumber);
		this.postNumberOracle = postNumber;
	}

	public abstract boolean up(String type);

	public abstract boolean down(String type);

	public String toString() {
		return "Signal Post " + postNumberOracle + ": " + levelOracle + " " + depictionOracle;
	}

	public abstract String toStringColored();

	public int getPostNumber() {
		return postNumberOracle;
	}

	public void setPostNumber(int postNumber) {
		this.postNumberOracle = postNumber;
	}

	public String getDepiction() {
		return depictionOracle;
	}

	public void setDepiction(String depiction) {
		this.depictionOracle = depiction;
	}

	public int getLevel() {
		return levelOracle;
	}

	public void setLevel(int level) {
		this.levelOracle = level;
	}
}
