package xml;

public class Parser {

	String xml;

	public Parser(String xml) {
		this.xml = xml;
	}

	public String getNodeValue_AsString(String nodename) {
		int start = xml.indexOf("<" + nodename);
		start = xml.indexOf('>', start);
		int end = xml.indexOf("</" + nodename + ">");
		
		String substring = "";
		if (start+1 < end){
			substring = xml.substring(start + 1, end);
		}
		return substring;
	}

	public Double getDivisor(String nodename) throws NumberFormatException {
		int start = xml.indexOf("<" + nodename);
		int end = xml.indexOf('>', start);
		String tag = xml.substring(start + 1 + nodename.length(), end);

		start = tag.indexOf("div=\"") + ("div=\n").length();
		end = tag.indexOf('"', start);
		String val = tag.substring(start, end);
		Double result = Double.parseDouble(val);
		if (result == 0) {
			result = 1.0;
		}
		return result;
	}

	public Integer getNodeValue_AsInteger(String nodename) throws NumberFormatException {
		String val = getNodeValue_AsString(nodename);
		int result = Integer.parseInt(val);
		return result;
	}

	public Boolean getNodeValue_AsBoolean(String nodename) throws NumberFormatException {
		try {
			Integer num = getNodeValue_AsInteger(nodename);
			return num == 1;
		} catch (NumberFormatException e) {
			String str = getNodeValue_AsString(nodename);
			return str.toLowerCase().equals("true");
		}
	}

	public Double getNodeValue_AsDouble(String nodename) throws NumberFormatException {
		String val = getNodeValue_AsString(nodename);
		Double res = Double.parseDouble(val);
		Double div = getDivisor(nodename);		
		if (div > 0) {
			res = res / div;
		}		
		return res;
	}
}
