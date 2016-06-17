
public class PostParameter {
	public final String KEY;
	public final String VALUE;

	public PostParameter(String key, String value) {
		this.KEY = key;
		this.VALUE = value;
	}

	@Override
	public String toString() {
		return KEY + "=" + VALUE;
	}

	public static String concat(PostParameter... params) {
		StringBuilder builder = new StringBuilder();
		for (PostParameter param : params) {
			builder.append("&" + param);
		}
		return builder.substring(1);
	}
}
