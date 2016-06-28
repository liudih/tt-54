package valueobjects.order_api;

import java.io.Serializable;

import valueobjects.order_api.cart.ExtraLine;

public class ExtraSaveInfo implements Serializable {
	boolean isSuccessful;
	Object identifier;
	Double parValue;
	final ExtraLine cartLine;

	public ExtraSaveInfo(boolean isSuccessful, Object identifier,
			Double parValue, ExtraLine cartLine) {
		this.isSuccessful = isSuccessful;
		this.identifier = identifier;
		this.parValue = parValue;
		this.cartLine = cartLine;
	}

	public ExtraSaveInfo(boolean isSuccessful, ExtraLine cartLine) {
		this.isSuccessful = isSuccessful;
		this.cartLine = cartLine;
	}

	public ExtraLine getCartLine() {
		return cartLine;
	}

	public boolean isSuccessful() {
		return isSuccessful;
	}

	public void setSuccessful(boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}

	public Object getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Object identifier) {
		this.identifier = identifier;
	}

	public Double getParValue() {
		return parValue;
	}

	public void setParValue(Double parValue) {
		this.parValue = parValue;
	}

	@Override
	public String toString() {
		return "ExtraSaveInfo [isSuccessful=" + isSuccessful + ", identifier="
				+ identifier + ", parValue=" + parValue + "]";
	}

}
