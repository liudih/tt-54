package extensions.loyalty.campaign.action.discount;

public enum DiscountType {

	// 全单打折 (物流费用等不能打折)
	WHOLE,

	// 部分SKU打折
	SKU_INCLUSIVE,

	// 除了部分SKU不打折
	SKU_EXCLUSIVE
}
