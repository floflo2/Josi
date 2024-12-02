package pgdp.security.oracles;

public class TrackOracle {

	private SignalPostOracle[] posts;

	public TrackOracle(int postCount) {
		posts = new SignalPostOracle[postCount > 0 ? postCount : 10];
		for (int i = 0; i < posts.length - 1; i++) {
			posts[i] = i % 3 == 0 ? new LightPanelOracle(i) : new FlagPostOracle(i);
		}
		posts[posts.length - 1] = new FinishPostOracle(posts.length - 1);
	}

	public void setAll(String type, boolean up) {
		for (SignalPostOracle s : posts) {
			if (up) {
				s.up(type);
			} else {
				s.down(type);
			}
		}
	}

	public void setRange(String type, boolean up, int start, int end) {
		for (int i = start; i != end; i = (i + 1) % posts.length) {
			if (up) {
				posts[i].up(type);
			} else {
				posts[i].down(type);
			}
		}
		if (up) {
			posts[end].up(type);
		} else {
			posts[end].down(type);
		}
	}

	public void createHazardAt(int start, int end) {
		for (int i = start; i != end; i = (i + 1) % posts.length) {
			posts[i].up("yellow");
		}
		posts[end].up("green");
	}

	public void removeHazardAt(int start, int end) {
		for (int i = start; i != end; i = (i + 1) % posts.length) {
			posts[i].down("danger");
		}
	}

	public void createLappedCarAt(int post) {
		for (int i = 0; i < 4; i++) {
			posts[(post + i) % posts.length].up("blue");
		}
	}

	public void removeLappedCarAt(int post) {
		for (int i = 0; i < 4; i++) {
			posts[(post + i) % posts.length].down("blue");
		}
	}

	public String printStatus() {
		StringBuilder sb = new StringBuilder();
		for (SignalPostOracle p : posts) {
			sb.append(p + "\n");
		}
		return sb.toString();
	}

	public String printStatusColored() {
		StringBuilder sb = new StringBuilder();
		for (SignalPostOracle p : posts) {
			sb.append(p.toStringColored() + "\n");
		}
		return sb.toString();
	}

	public SignalPostOracle[] getPosts() {
		return posts;
	}

	public void setPosts(SignalPostOracle[] posts) {
		this.posts = posts;
	}
}
