package pgdp.security.oracles;

import static pgdp.security.TestHelper.*;

public class LightPanelOracle extends SignalPostOracle {

	public LightPanelOracle(int postNumber) {
		super(postNumber);
	}

	@Override
	public String toString() {
		return "Signal post " + getPostNumber() + " of type light panel is in level " + getLevel() + " and is "
				+ (getLevel() == 0 ? "switched off" : "blinking " + getDepiction());
	}

	public String toStringColored() {
		return "Signal post " + getPostNumber() + " of type light panel is in level " + getLevel() + " and is "
				+ (getLevel() == 0 ? "switched off" : "blinking " + changeColors(getDepiction()));
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

		if (newLevel == getLevel() && !getDepiction().contains(type)) {
			String current = getDepiction();
			if (current.contains("green") || current.contains("doubleYellow")) {
				setDepiction(type);
			}
			return !current.contains(getDepiction());
		} else if (newLevel > getLevel()) {
			setDepiction(type.equals("end") ? "yellow" : type);
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
			if (getDepiction().contains("green")) {
				setDepiction("");
				setLevel(0);
				yield true;
			}
			yield false;
		}
		case "blue" -> {
			if (getDepiction().contains("blue")) {
				setDepiction("");
				setLevel(0);
				yield true;
			}
			yield false;
		}
		case "danger" -> {
			String d = getDepiction();
			if (getLevel() != 5 && (d.contains("yellow") || d.contains("doubleYellow") || d.contains("red")
					|| d.contains("[SC]"))) {
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
