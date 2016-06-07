import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args)
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		TCUIVKeywordLibrary tcu = new TCUIVKeywordLibrary();		
		tcu.setResetAlarms();
		System.out.println("done");
	}

	/**
	 * Der Kalibriervorgang ist nicht ganz so einfach. Die Methode beschreibt,
	 * was beachtet werden muss.
	 * 
	 * @param c
	 * @param tcu
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private static void calibrate(Connection c, TCUIVKeywordLibrary tcu) throws IOException {
		PostParameter ran = new PostParameter("ran", "1"); // Wert ist egal
		PostParameter lowU = new PostParameter("lowU", "1"); // Wert bestimmt
																// den Kanal (1,
																// 2, 3)

		PostParameter[] params = new PostParameter[] { ran, lowU }; // Reihenfolge
																	// ist
																	// wichtig:
																	// 1. ran 2.
																	// lowU
		c.sendPOST("response/calibA.xml", params);
		tcu.setCalibrateSpannungsPlatine("2016-5-5", 1.0, -1.0, 1.1, -1.1, 1.2, -1.2, 1.3, -1.3);
	}

	@SuppressWarnings("unused")
	private static void test_get(TCUIVKeywordLibrary tcu) {
		ArrayList<Object> results = new ArrayList<>();
		Class<?> c = TCUIVKeywordLibrary.class;
		Method[] ms = c.getMethods();
		for (Method m : ms) {
			if (m.getName().startsWith("get")) {
				try {
					Object o = m.invoke(tcu);
					System.out.println("# " + m.getName() + " -> " + o);
					results.add(o);
				} catch (Exception e) {
					System.out.println("# " + m.getName() + " -> " + e.getMessage());
					results.add("ERR");
				}
			}
		}

		System.out.println("### ### ### ### ### ### ### ###");
		for (Object o : results) {
			if (o instanceof Object[]) {
				Object[] arr = (Object[]) o;
				System.out.print("#");
				for (Object tmp : arr) {
					System.out.print(" " + tmp);
				}
			} else
				System.out.println("# " + o);
		}
	}

}
