package valueobjects.member;

public enum ResultType {
	SUCCESS, // success
	MEMBER_NOT_ACTIVATED, // member found, but not activated
	MEMBER_EXISTS, // member already registered, and activated
	MEMBER_EXIST_LINKED // member found, and linked to other login provider
}