package execution;

public class TestProtocol {

	private final boolean passed;
	private final String testname, testpath, message;
	
	public TestProtocol(boolean passed, String testname, String testpath, String message) {
		this.passed = passed;
		this.testname = testname;
		this.testpath = testpath;
		this.message = message;	
	}

	public String getTestname() {
		return testname;
	}

	public String getTestpath() {
		return testpath;
	}

	public String getMessage() {
		return message;
	}

	public boolean hasPassed() {
		return passed;
	}

	@Override
	public String toString() {
		String strPassed = (passed) ? "[PASS]" : "[FAIL]";
		
		return strPassed + "\t" + testname + "\t" + testpath + "\t" + message;
	}

}
