import java.io.IOException;

import testfile.Testfile;
import testfile.TestfileReader;

public class Application {

	public static void main(String[] args){
		try {
			Testfile tf = TestfileReader.read("test.tst");
			System.out.println(tf);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
	}
	
}
