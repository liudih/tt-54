package dto.mobile;

import utils.ValidataUtils;

public class MessageList {

	private Integer id;//	消息ID
	private String from;//来源
	private String subject;//主题
	private String content;//内容
	private Long createDate;//	时间
	private Integer status;//状态
	private String table;// 该条消息属于哪个表
	
	public Integer getId() {
		return ValidataUtils.validataInt(id);
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFrom() {
		return ValidataUtils.validataStr(from);
	}
	public void setFrom(String from) {
		this.from = from;
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
	public String getTable() {
		return ValidataUtils.validataStr(table);
	}
	public void setTable(String table) {
		this.table = table;
	}
	
	
	
}
