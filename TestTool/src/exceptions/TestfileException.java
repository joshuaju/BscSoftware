package exceptions;

import java.io.IOException;

public class TestfileException extends IOException{
	 
	private static final long serialVersionUID = 1L;

	public TestfileException(){
		super();
	}
	
	public TestfileException(String msg){
		super(msg);
	}
	
}
