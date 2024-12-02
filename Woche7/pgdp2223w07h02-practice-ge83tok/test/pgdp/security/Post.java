package pgdp.security;

public class Post extends SignalPost {

	public Post(int postNumber) {
		super(postNumber);
	}

	public boolean up(String type) {
		return false;
	}

	public boolean down(String type) {
		return false;
	}

}
