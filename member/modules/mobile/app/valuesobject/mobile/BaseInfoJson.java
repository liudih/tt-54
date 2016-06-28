package valuesobject.mobile;

public class BaseInfoJson<T> extends BaseJson {

	private static final long serialVersionUID = 1332008287725136535L;

	private T info;

	public T getInfo() {
		return info;
	}

	public void setInfo(T info) {
		this.info = info;
	}

}
