package valueobjects.product;

/**
 * 
 * @author lijun
 *
 */
public class ImageBo {

	private final byte[] data;
	private final String type;

	public ImageBo(byte[] data, String type) {
		this.data = data;
		this.type = type;
	}

	public byte[] getData() {
		return data;
	}

	public String getType() {
		return type;
	}

}
