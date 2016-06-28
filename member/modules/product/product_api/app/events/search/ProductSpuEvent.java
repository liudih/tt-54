package events.search;

public class ProductSpuEvent {

	public ProductSpuEvent(String spu) {
		super();
		this.spu = spu;
	}

	String spu;

	public String getSpu() {
		return spu;
	}
}
