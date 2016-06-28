package services.order.exception;

public enum ExType {

	IncompleteInformation,

	InvalidShippingMethod,

	InvalidPaymentMethod,

	CartNotReadyForCheckout,

	ExtrasFailed,

	BillDetailFailed,

	OrderDetailFailed,

	DetailSizeError,

	CountryIsEmpty,

	Unknown,
}
