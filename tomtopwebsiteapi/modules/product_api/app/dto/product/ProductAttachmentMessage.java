package dto.product;


public class ProductAttachmentMessage {
	private ProductAttachmentMapper productAttachmentMapper;

	private AttachmentDesc attachmentDesc;

	public ProductAttachmentMapper getProductAttachmentMapper() {
		return productAttachmentMapper;
	}

	public void setProductAttachmentMapper(
			ProductAttachmentMapper productAttachmentMapper) {
		this.productAttachmentMapper = productAttachmentMapper;
	}

	public AttachmentDesc getAttachmentDesc() {
		return attachmentDesc;
	}

	public void setAttachmentDesc(AttachmentDesc attachmentDesc) {
		this.attachmentDesc = attachmentDesc;
	}
	
}