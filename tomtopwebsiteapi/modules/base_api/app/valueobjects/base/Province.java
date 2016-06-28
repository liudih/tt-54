package valueobjects.base;

import java.util.List;

public class Province {

	final List<dto.Province> provinces;

	public Province(List<dto.Province> provinces) {
		this.provinces = provinces;
	}

	public List<dto.Province> getProvinces() {
		return provinces;
	}

}
