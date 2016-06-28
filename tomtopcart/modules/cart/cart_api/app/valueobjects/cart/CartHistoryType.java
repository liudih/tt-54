package valueobjects.cart;

import java.io.Serializable;

public enum CartHistoryType implements Serializable {
	add(1), update(2), delete(3);

	// 定义私有变量
	private int ntype;

	// 构造函数，枚举类型只能为私有
	private CartHistoryType(int type) {
		this.ntype = type;
	}

	public int value() {
		return this.ntype;
	}
}
