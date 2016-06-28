package valuesobject.mobile;

import utils.LanguageUtil;

public class BaseResultType {

	/** Public **/
	public final static int SUCCESS = 1;
	public final static String SUCCESSMSG = msg("mobile.success");

	public final static int ERROR = 0;
	public final static String NODATA = msg("mobile.nodata");

	public final static int FAILURE = -1;
	public final static String OPERATE_FAIL = msg("mobile.operatefail");

	public final static int REQUIRED_CODE = -6;
	public final static String REQUIRED_MSG = msg("mobile.required");

	public final static int NOTENOUGH = -89;
	public final static String NOT_ENOUGH = msg("mobile.notenough");

	public final static int EXCEPTION = -999;
	public final static String EXCEPTIONMSG = msg("mobile.exception");

	public final static int ADD_EXCEPTION = -9000;
	public final static String ADDORDERERROR = msg("mobile.add.error");

	public final static int UPDATE_EXCEPTION = -9001;
	public final static String UPDATEERROR = msg("mobile.update.error");

	/** Login **/
	public final static int EMAIL_IS_EMPTY_ERROR_CODE = -10001;
	public final static String EMAIL_IS_EMPTY_ERROR_MSG = msg("mobile.login.email.empty");

	public final static int PASSWORD_IS_EMPTY_ERROR_CODE = -10002;
	public final static String PASSWORD_IS_EMPTY_ERROR_MSG = msg("mobile.login.password.empty");

	public final static int EMAIL_NOT_FIND_ERROR_CODE = -10003;
	public final static String EMAIL_NOT_FIND_ERROR_MSG = msg("mobile.login.email.notfind");

	public final static int PASSWORD_ERROR_CODE = -10004;
	public final static String PASSWORD_ERROR_MSG = msg("mobile.login.password.error");

	public final static int LOGIN_ERROR_CODE = -10005;
	public final static String LOGIN_ERROR_MSG = msg("mobile.login.error");
	public final static String LOGIN_SUCCESS_MSG = msg("mobile.login.success");

	/** Password **/
	public final static int PASSWORD_ALERT_INPUT_ERROR_CODE = -10006;
	public final static String PASSWORD_ALERT_INPUT_ERROR_MSG = msg("mobile.password.input.error");

	public final static int PASSORD_CODE_ERROR_CODE = -10007;
	public final static String PASSORD_CODE_ERROR_MSG = msg("mobile.password.code.error");

	public final static int OLD_PASSWORD_ERROR_CODE = -10008;
	public final static String OLD_PASSWORD_ERROR_MSG = msg("mobile.password.old.error");

	public final static int PASSWORD_RESET_INPUT_ERROR_CODE = -10009;
	public final static String PASSWORD_RESET_INPUT_ERROR_MSG = msg("mobile.password.reset.error");

	/** Register **/
	public final static String REGISTER_SUCCESS_MSG = msg("mobile.register.success");
	public final static int REGISTER_INPUT_ERROR_CODE = -10010;
	public final static String REGISTER_INPUT_ERROR_MSG = msg("mobile.register.input.error");

	public final static int REGISTER_USER_EXISTS_ERROR_CODE = -10011;
	public final static String REGISTER_USER_EXISTS_ERROR_MSG = msg("mobile.register.user.exists");

	public final static int REGISTER_USER_UN_ACTIVATED_ERROR_CODE = -10012;
	public final static String REGISTER_USER_UN_ACTIVATED_ERROR_MSG = msg("mobile.register.user.unactivated");

	public final static int REGISTER_SERVICE_ERROR_CODE = -10013;
	public final static String REGISTER_SERVICE_ERROR_MSG = msg("mobile.register.service.error");

	/** UserController **/
	public final static int NICK_NAME_FORMAT_ERROR_CODE = -20001;
	public final static String NICK_NAME_ERROR_MSG = msg("mobile.nickname.format");

	public final static int FNAME_FORMAT_ERROR_CODE = -20002;
	public final static String FIRST_NAME_ERROR_MSG = msg("mobile.fname.format");

	public final static int LNAME_FORMAT_ERROR_CODE = -20003;
	public final static String LAST_NAME_ERROR_MSG = msg("mobile.lname.format");

	public final static int GENDER_VALUE_NULL_ERROR_CODE = -20004;
	public final static String GENDER_IS_NULL_ERROR_MSG = msg("mobile.gender.null");

	public final static int GENDER_VALUE_ERROR_CODE = -20005;
	public final static String GENDER_VALUE_ERROR_MSG = msg("mobile.gender.type.error");

	public final static int BIRTHDAY_VALUE_NULL_ERROR_CODE = -20006;
	public final static String BIRTHDAY_IS_NULL_ERROR_MSG = msg("mobile.birth.null");

	public final static int BIRTHDAY_FORMATE_ERROR_CODE = -20007;
	public final static String BIRTHDAY_FORMAT_ERROR_MSG = msg("mobile.birth.format");

	public final static int COUNTRY_VALUE_NULL_ERROR_CODE = -20008;
	public final static String COUNTRY_IS_NULL_ERROR_MSG = msg("mobile.country.null");

	public final static int COUNTRY_FORMATE_LENGTH_OVER_ERROR_CODE = -20009;
	public final static String COUNTRY_LENGTH_OVER_ERROR_MSG = msg("mobile.country.lengthover");

	public final static int ABOUT_FORMATE_LENGTH_OVER_ERROR_CODE = -20010;
	public final static String ABOUT_LENGTH_OVER_ERROR_MSG = msg("mobile.about.lengthover");

	public final static int MESSAGE_FORMATE_LENGTH_OVER_ERROR_CODE = -20011;
	public final static String MESSAGE_LENGTH_OVER_ERROR_MSG = msg("mobile.msg.lengthover");

	public final static int UPLOAD_USER_IMG_NULL_ERROR_CODE = -20012;
	public final static String UPLOAD_USER_IMG_IS_NULL_ERROR_MSG = msg("moblie.upload.img.null");

	public final static int UPLOAD_USER_IMG_FILE_OVER_ERROR_CODE = -20013;
	public final static String UPLOAD_USER_IMG_FILE_OVER_ERROR_MSG = msg("mobile.upload.img.lenthover");

	public final static int UPLOAD_USER_IMG_NOT_FILE_ERROR_CODE = -20014;
	public final static String UPLOAD_USER_IMG_NOT_FILE_ERROR_MSG = msg("mobile.upload.img.noexists");

	public final static int UPLOAD_USER_IMG_CONTENT_TYPE_ERROR_CODE = -20015;
	public final static String UPLOAD_USER_IMG_CONTENT_TYPE_ERROR_MSG = msg("mobile.upload.img.format");

	public final static int UPLOAD_USER_IMG_FILE_SIZE_OVER_MAX_ERROR_CODE = -20016;
	public final static String UPLOAD_USER_IMG_FILE_SIZE_OVER_MAX_ERROR_MSG = msg("mobile.upload.img.size");

	public final static int MESSAGE_EMIAL_NULL_ERROR_CODE = -20017;

	/** OrderController **/
	public final static int UPDATE_ORDER_STATUS_NOT_DISPATCHED_ERROR_CODE = -30001;
	public final static String UPDATE_ORDER_STATUS_NOT_DISPATCHED_ERROR_MSG = msg("mobile.order.update.status");

	public final static int COMFIR_ORDER_ERROR_CODE = -30002;
	public final static String COMFIR_ORDER_ERROR_MSG = msg("mobile.order.comfir.failure");

	public final static int UPDATE_ORDER_ERROR_CODE = -30003;
	public final static String UPDATE_ORDER_ERROR_MSG = msg("mobile.order.update.failure");

	public final static int ORDER_ADD_CART_IS_NULL_ERROR_CODE = -30004;
	public final static String ORDER_ADD_CART_IS_NULL_ERROR_MSG = msg("mobile.order.add.cart.null");

	public final static int ORDER_ADDRESS_IS_NULL_ERROR_CODE = -30005;
	public final static String ORDER_ADDRESS_IS_NULL_ERROR_MSG = msg("mobile.order.address.null");

	public final static int ORDER_SHIPPING_IS_NULL_ERROR_CODE = -30006;
	public final static String ORDER_SHIPPING_IS_NULL_ERROR_MSG = msg("mobile.order.shipping.null");

	public final static int ORDER_NOT_FIND_ERROR_CODE = -30007;
	public final static String ORDER_NOT_FIND_ERROR_MSG = msg("mobile.order.nofind");

	/** ValidController **/
	public final static int VALID_CART_NULL_EXCEPTION = -31001;
	public final static String VALID_CART_NULL_EXCEPTION_MSG = msg("mobile.valid.cart.null");

	public final static int VALID_RULE_NULL_EXCEPTION = -31002;
	public final static String VALID_RULE_NULL_EXCEPTION_MSG = msg("mobile.valid.rule.null");

	public final static int VALID_POINTS_MONEY_NULL_EXCEPTION = -31003;
	public final static String VALID_POINTS_MONEY_NULL_EXCEPTION_MSG = msg("mobile.valid.points.money.null");

	public final static int VALID_POINTS_TOTAL_ERROR = -31004;
	public final static String VALID_POINTS_TOTAL_ERROR_MSG = msg("mobile.valid.points.total.error");

	/** OrderPayController **/
	public final static int ORDER_PAY_NOT_FIND_ERROR_CODE = -32001;
	public final static String ORDER_PAY_NOT_FIND_ERROR_MSG = msg("mobile.pay.order.notfind");

	public final static int ORDER_PAY_NOT_EQUALS_EMAIL_ERROR_CODE = -32002;
	public final static String ORDER_PAY_NOT_EQUALS_EMAIL_ERROR_MSG = msg("mobile.pay.order.notequals.email");

	public final static int ORDER_PAY_STATUS_NOT_PAYMENT_PENDING_ERROR_CODE = -32003;
	public final static String ORDER_PAY_STATUS_NOT_PAYMENT_PENDING_ERROR_MSG = msg("mobile.pay.order.status.error");

	public final static int ORDER_PAY_BAD_ERROR_CODE = -32004;
	public final static String ORDER_PAY_BAD_ERROR_MSG = msg("mobile.pay.order.bad");

	public final static int ORDER_PAY_CURRENCY_ERROR_CODE = -32005;
	public final static String ORDER_PAY_CURRENCY_ERROR_MSG = msg("mobile.pay.order.currency.null");

	/** ProductReviewController **/
	public final static int REVIEW_ADD_FORM_GET_ERROR_CODE = -60002;
	public final static String REVIEW_ADD_FORM_GET_ERROR_MSG = msg("mobile.review.add.getform.error");

	public final static int REVIEW_ADD_NOT_OREDER_ERROR_CODE = -60003;
	public final static String REVIEW_ADD_NOT_OREDER_ERROR_MSG = msg("mobile.review.add.failure");

	public final static int REVIEW_ADD_PRODUCT_NOT_FIND_ERROR_CODE = -60004;
	public final static String REVIEW_ADD_PRODUCT_NOT_FIND_ERROR_MSG = msg("mobile.review.add.product.not.find");

	public final static int REVIEW_ADD_VIDEO_ERROR_CODE = -60005;
	public final static String REVIEW_ADD_VIDEO_ERROR_MSG = msg("mobile.review.add.video.error");

	public final static int UPLOAD_REVIEW_IMG_FILE_SIZE_OVER_MAX_ERROR_CODE = -60006;
	public final static String UPLOAD_REVIEW_IMG_FILE_SIZE_OVER_MAX_ERROR_MSG = msg("mobile.upload.review.img.size");

	/** ContactService **/
	public final static int CONTACT_TITLE_EMPTY_ERROR_CODE = -61006;
	public final static String CONTACT_TITLE_EMPTY_ERROR_MSG = msg("mobile.contact.title.empty.error");

	public final static int CONTACT_CONTENT_EMPTY_ERROR_CODE = -61007;
	public final static String CONTACT_CONTENT_EMPTY_ERROR_MSG = msg("mobile.contact.content.empty.error");

	public final static int CONTACT_EMAIL_EMPTY_ERROR_CODE = -61008;
	public final static String CONTACT_EMAIL_EMPTY_ERROR_MSG = msg("mobile.contact.email.empty.error");

	public final static int CONTACT_EMAIL_FORMAT_ERROR_CODE = -61009;
	public final static String CONTACT_EMAIL_FORMAT_ERROR_MSG = msg("mobile.contact.email.format.error");

	public final static int CONTACT_TITLE_LENGTH_OVER_ERROR_CODE = -61010;
	public final static String CONTACT_TITLE_LENGTH_OVER_ERROR_MSG = msg("mobile.contact.title.lengthover");

	public final static int CONTACT_CONTENT_LENGTH_OVER_ERROR_CODE = -61011;
	public final static String CONTACT_CONTENT_LENGTH_OVER_ERROR_MSG = msg("mobile.contact.content.lengthover");

	public final static int CONTACT_EMAIL_LENGTH_OVER_ERROR_CODE = -61012;
	public final static String CONTACT_EMAIL_LENGTH_OVER_ERROR_MSG = msg("mobile.contact.email.lengthover");

	/** AddressController **/
	public final static int ADDRESS_INPUT_FROM_ERROR_CODE = -62013;
	public final static String ADDRESS_INPUT_FROM_ERROR_MSG = msg("mobile.address.input.error");

	public final static int ADDRESS_FIRST_NAME_EMPTY_ERROR_CODE = -62014;
	public final static String ADDRESS_FIRST_NAME_EMPTY_ERROR_MSG = msg("mobile.address.fname.empty.error");

	public final static int ADDRESS_STREET_IS_EMPTY_ERROR_CODE = -62015;
	public final static String ADDRESS_STREET_IS_EMPTY_ERROR_MSG = msg("mobile.address.street.empty.error");

	public final static int ADDRESS_COUNTRY_IS_EMPTY_ERROR_CODE = -62016;
	public final static String ADDRESS_COUNTRY_IS_EMPTY_ERROR_MSG = msg("mobile.address.country.empty.error");

	public final static int ADDRESS_PROVINCE_IS_EMPTY_ERROR_CODE = -62017;
	public final static String ADDRESS_PROVINCE_IS_EMPTY_ERROR_MSG = msg("mobile.address.province.empty.error");

	public final static int ADDRESS_CITY_IS_EMPTY_ERROR_CODE = -62018;
	public final static String ADDRESS_CITY_IS_EMPTY_ERROR_MSG = msg("mobile.address.city.empty.error");

	public final static int ADDRESS_POSTAL_IS_EMPTY_ERROR_CODE = -62019;
	public final static String ADDRESS_POSTAL_IS_EMPTY_ERROR_MSG = msg("mobile.address.postal.empty.error");

	public final static int ADDRESS_PHONE_IS_EMPTY_ERROR_CODE = -62020;
	public final static String ADDRESS_PHONE_IS_EMPTY_ERROR_MSG = msg("mobile.address.phone.empty.error");

	public final static int ADDRESS_FIRST_NAME_LENGTH_OVER_ERROR_CODE = -62021;
	public final static String ADDRESS_FIRST_NAME_LENGTH_OVER_ERROR_MSG = msg("mobile.address.fname.lengthover");

	public final static int ADDRESS_LAST_NAME_LENGTH_OVER_ERROR_CODE = -62022;
	public final static String ADDRESS_LAST_NAME_LENGTH_OVER_ERROR_MSG = msg("mobile.address.lname.lengthover");

	public final static int ADDRESS_STREET_LENGTH_OVER_ERROR_CODE = -62023;
	public final static String ADDRESS_STREET_LENGTH_OVER_ERROR_MSG = msg("mobile.address.street.lengthover");

	public final static int ADDRESS_PROVINCE_LENGTH_OVER_ERROR_CODE = -62024;
	public final static String ADDRESS_PROVINCE_LENGTH_OVER_ERROR_MSG = msg("mobile.address.province.lengthover");

	public final static int ADDRESS_CITY_LENGTH_OVER_ERROR_CODE = -62025;
	public final static String ADDRESS_CITY_LENGTH_OVER_ERROR_MSG = msg("mobile.address.city.lengthover");

	public final static int ADDRESS_POSTAL_LENGTH_OVER_ERROR_CODE = -62026;
	public final static String ADDRESS_POSTAL_LENGTH_OVER_ERROR_MSG = msg("mobile.address.postal.lengthover");

	public final static int ADDRESS_PHONE_LENGTH_OVER_ERROR_CODE = -62027;
	public final static String ADDRESS_PHONE_LENGTH_OVER_ERROR_MSG = msg("mobile.address.phone.lengthover");

	private static String msg(String key) {
		return LanguageUtil.getMessage(key);
	}
}
