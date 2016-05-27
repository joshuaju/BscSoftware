
public class PostParameter {
	public final String KEY;
	public final String VALUE;
	
	public PostParameter(String key, String value){
		this.KEY = key;
		this.VALUE = value;
	}
	
	@Override
	public String toString(){
		return KEY + "=" + VALUE;
	}
}
