import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Connection {
	
	private final String base;

	public Connection(String protocol, String ip) {
		base = protocol + "://" + ip + "/";
	}

	public Response sendGET(String path) throws IOException {
		HttpURLConnection connection = null;
		URL url = new URL(base + path);

		connection = (HttpURLConnection) url.openConnection();
		// connection.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = connection.getResponseCode();

		String response = getResponse(connection.getInputStream());
		return new Response(url.toString(), "", "GET", responseCode, response);
	}

	public Response sendPOST(String path, String parameter) throws IOException {
		HttpURLConnection connection = null;
		URL url = new URL(base + path);

		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");

		connection.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.writeBytes(parameter);
		wr.flush();
		wr.close();

		int responseCode = connection.getResponseCode();

		String response = getResponse(connection.getInputStream());
		return new Response(url.toString(), parameter, "POST", responseCode, response);
	}

	public Response sendPOST(String path, PostParameter[] params) throws IOException {
		String strParams = getPostParams(params);
		return sendPOST(path, strParams);
	}

	public Response sendPOST(String path, String key, String value) throws IOException {
		return sendPOST(path, key + "=" + value);
	}

	public Response sendPOST(String path, String[] keys, String[] values) throws IOException {
		if (keys.length != values.length) {
			throw new IndexOutOfBoundsException("Array lengths don't match");
		}

		PostParameter[] arr = new PostParameter[keys.length];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = new PostParameter(keys[i], values[i]);
		}
		String strParams = getPostParams(arr);
		return sendPOST(path, strParams);
	}

	private String getPostParams(PostParameter[] params) {
		StringBuilder builder = new StringBuilder();
		for (PostParameter p : params) {
			builder.append("&" + p);
		}
		return builder.substring(1);
	}

	private String getResponse(InputStream is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String inputLine;
		StringBuilder response = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine + "\n");
		}
		in.close();
		return response.toString();
	}

}
