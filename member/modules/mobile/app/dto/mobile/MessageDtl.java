package dto.mobile;

import utils.ValidataUtils;

public class MessageDtl {

	private String id;//消息ID
	private String from;//来源
	private String type;//	订单状态变更 ，积分获取等等
	private String subject;//主题
	private String content;//内容
	private Long createDate;//	时间
	private Integer status;//状态
	private Integer sendMethod;//发送方式（系统发送，人工发送）
	
	
	public String getId() {
		return ValidataUtils.validataStr(id);
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFrom() {
		return ValidataUtils.validataStr(from);
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getType() {
		return ValidataUtils.validataStr(type);
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubject() {
		return ValidataUtils.validataStr(subject);
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return ValidataUtils.validataStr(content);
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getCdate() {
		return ValidataUtils.validataLong(createDate);
	}
	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}
	public Integer getStatus() {
		return ValidataUtils.validataInt(status);
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSendmethod() {
		return ValidataUtils.validataInt(sendMethod);
	}
	public void setSendMethod(Integer sendMethod) {
		this.sendMethod = sendMethod;
	}
	
	
	
}
