package execution;

import java.util.function.Consumer;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class TestSuiteExecuterThread extends Thread {

	private TestSuiteExecuter executer = null;
	private static Consumer<TestSuiteProtocol> onFinish = (p) -> {
	};

	private static Runnable onAbort = () -> {
	};

	public TestSuiteExecuterThread(TestSuiteExecuter executer) {
		super(() -> {
			ObjectProperty<TestSuiteProtocol> protocol = new SimpleObjectProperty<>();
			try {
				protocol.set(executer.execute());
				onFinish.accept(protocol.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
				onAbort.run();
			}
		});
		setDaemon(true);
		this.executer = executer;
	}

	public void setOnFinish(Consumer<TestSuiteProtocol> onFinish) {
		TestSuiteExecuterThread.onFinish = onFinish;
	}

	public void setOnAbort(Runnable onAbort) {
		TestSuiteExecuterThread.onAbort = onAbort;
	}

	@Override
	public void start() {
		super.start();
	}

	public void abort() {
		if (this.isAlive()) {
			executer.abort();
		}
	}

}
