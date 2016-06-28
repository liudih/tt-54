package services.manager;

import play.Logger;
import extensions.InjectorInstance;

public class CountService {
	private int i;

	public CountService() {
		this.i = 0;
	}

	public CountService(int i) {
		this.i = i;
	}

	public void add() {
		this.i += 1;
	}

	public void add(int i) {
		this.i += i;
	}

	public int get() {
		return this.i;
	}

	public static CountService getInstance() {
		return InjectorInstance.getInstance(CountService.class);
	}

}
