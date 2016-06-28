package entity.messaging;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import enums.messaging.SendMethod;
import enums.messaging.Type;

/**
 * 对应表t_message_broadcast实体
 * 
 * @author lijun
 *
 */
public class Broadcast implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int websiteId;
	// 发送人ID
	private int sendId;
	// 消息来源
	private String from;
	// 主题
	private String subject;
	// 内容
	private String content;
	// 类型（订单状态变更 ，积分获取等等）
	private int type;
	private Type typeEnum;
	// 发送方式（系统发送，人工发送）
	private int sendMethod;
	private SendMethod sendMethodEnum;
	// 创建时间
	private Date createDate;
	private String createDateStr;
	// 该条消息属于哪个表
	private String table;
	// 修改人
	private int modifierId;
	// 创建人
	private String creater;
	// 最后修改人
	private String modifier;
	// 状态
	private int status;
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public SendMethod getSendMethodEnum() {
		return sendMethodEnum;
	}

	public void setSendMethodEnum(SendMethod sendMethodEnum) {
		this.sendMethodEnum = sendMethodEnum;
	}

	public int getModifierId() {
		return modifierId;
	}

	public void setModifierId(int modifierId) {
		this.modifierId = modifierId;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Type getTypeEnum() {
		return typeEnum;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWebsiteId() {
		return websiteId;
	}

	public void setWebsiteId(int websiteId) {
		this.websiteId = websiteId;
	}

	public int getSendId() {
		return sendId;
	}

	public void setSendId(int sendId) {
		this.sendId = sendId;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
		this.typeEnum = Type.getType(type);
	}

	public int getSendMethod() {
		return sendMethod;
	}

	public void setSendMethod(int sendMethod) {
		this.sendMethod = sendMethod;
		this.sendMethodEnum = SendMethod.getSendMethod(sendMethod);
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
		if (createDate != null) {
			SimpleDateFormat formater = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			this.createDateStr = formater.format(createDate);
		}
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

}
