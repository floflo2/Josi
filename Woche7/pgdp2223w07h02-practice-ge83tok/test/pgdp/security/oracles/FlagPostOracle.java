package pgdp.security.oracles;

import static pgdp.security.TestHelper.*;

public class FlagPostOracle extends SignalPostOracle {

	public FlagPostOracle(int postNumber) {
		super(postNumber);
	}

	@Override
	public String toString() {
		return "Signal post " + getPostNumber() + " of type  flag post  is in level " + getLevel() + " and is "
				+ (getLevel() == 0 ? " doing nothing" : " waving  " + getDepiction());
	}

	public String toStringColored() {
		return "Signal post " + getPostNumber() + " of type  flag post  is in level " + getLevel() + " and is "
				+ (getLevel() == 0 ? " doing nothing" : " waving  " + changeColors(getDepiction()));
	}

	public boolean up(String type) {
		int newLevel = switch (type) {
		case "green", "blue" -> 1;
		case "yellow" -> 2;
		case "doubleYellow", "[SC]" -> 3;
		case "red" -> 4;
		case "end" -> 5;
		default -> -1;
		};

		if (newLevel == getLevel() && !type.equals("end") && !getDepiction().contains(type)) {
			setDepiction(type.equals("green") || type.equals("blue") ? "green/blue" : getDepiction() + "/" + type);
			return true;
		} else if (newLevel > getLevel()) {
			setDepiction(
					type.contains("[SC]") ? "doubleYellow/[SC]" : type.equals("end") ? "green/yellow/red/blue" : type);
			setLevel(newLevel);
			return true;
		}
		return false;
	}

	public boolean down(String type) {
		return switch (type) {
		case "clear" -> {
			if (getLevel() != 0) {
				setDepiction("");
				setLevel(0);
				yield true;
			}
			yield false;
		}
		case "green" -> {
			if (getDepiction().contains("green") && getLevel() != 5) {
				String[] s = getDepiction().split("/");
				if (s.length == 1) {
					setDepiction("");
					setLevel(0);
				} else {
					setDepiction("blue");
				}
				yield true;
			}
			yield false;
		}
		case "blue" -> {
			if (getDepiction().contains("blue") && getLevel() != 5) {
				String[] s = getDepiction().split("/");
				if (s.length == 1) {
					setDepiction("");
					setLevel(0);
				} else {
					setDepiction("green");
				}
				yield true;
			}
			yield false;
		}
		case "danger" -> {
			String d = getDepiction();
			if (getLevel() != 5 && (d.contains("yellow") || d.contains("doubleYellow") || d.contains("red"))) {
				setDepiction("green");
				setLevel(1);
				yield true;
			}
			yield false;
		}
		default -> false;
		};
	}
}
