
public class Response {

	private final String url;
	private final String parameter;
	private final String method;
	private final int responseCode;
	private final String response;

	public static boolean DEBUG_FLAG = false;
	
	public Response(String url, String parameter, String method, int responseCode, String response) {
		this.url = url;
		this.parameter = parameter;
		this.method = method;
		this.responseCode = responseCode;
		this.response = response;
		
		if (DEBUG_FLAG){
			System.out.println(this);
		}
	}

	public String getUrl() {
		return url;
	}

	public String getParameter() {
		return parameter;
	}

	public String getMethod() {
		return method;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public String getResponse() {
		return response;
	}

	public String toString(){
		return String.format("[URL: %s\nMETHOD: %s\nRESPONSE CODE: %d\nPARAMETERS: %s\nRESPONSE: %s]", url, method, responseCode, parameter, response);
		
	}
	
}
