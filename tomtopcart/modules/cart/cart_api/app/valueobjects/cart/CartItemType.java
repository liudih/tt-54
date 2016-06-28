package valueobjects.cart;

import java.io.Serializable;

public enum CartItemType implements Serializable {
	
	Single(1), Bundle(2);

	// 定义私有变量
	private int ntype;

	// 构造函数，枚举类型只能为私有
	private CartItemType(int nType) {
		this.ntype = nType;
	}

	public int value() {
		return this.ntype;
	}

	public static CartItemType valueOf(int nType) {
		switch (nType) {
		case 1:
			return Single;
		case 2:
			return Bundle;
		default:
			return null;
		}
	}

	@Override
	public String toString() {
		return String.valueOf(this.ntype);
	}

}
