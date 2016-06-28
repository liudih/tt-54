package  com.rabbit.dto.monitor;

import java.io.Serializable;
/**
 * @author Administrator
 *
 */
public class RabbitMonitorDto implements Serializable {
	private static final long serialVersionUID = 868591398710684992L;
	private String moniterId;		//记录ID
	private String recordKey;		//能够唯一标识的记录（通过此记录能够获得记录标识）
	private String optType;			//操作类型 ：eg：p_push(类型_操作) 
	//private String extendsField;	//辅助属性，当唯一标识不能确定记录的唯一性时，辅助属性存json以帮助确定唯一 eg:sku+语言+站点
	private String recordState;		//记录状态 1：成功  2:失败
	private String nodeData;		//存放失败原因和报文{reason:XXX,node:xxx}
	private String failReason;		//失败原因
	private String createdOn;			// 创建时间 UTC
	private String createdBy;		//  创建人
	private String lastUpdatedOn;		//  最后更新时间 UTC
	private String lastUpdatedBy;	//  最后更新人
	private String isDeleted;		//是否已逻辑删除 1:是 0:否 默认:0
	
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	public String getMoniterId() {
		return moniterId;
	}
	public void setMoniterId(String moniterId) {
		this.moniterId = moniterId;
	}
	public String getRecordKey() {
		return recordKey;
	}
	public void setRecordKey(String recordKey) {
		this.recordKey = recordKey;
	}
	public String getOptType() {
		return optType;
	}
	public void setOptType(String optType) {
		this.optType = optType;
	}
	public String getRecordState() {
		return recordState;
	}
	public void setRecordState(String recordState) {
		this.recordState = recordState;
	}
	
	public String getNodeData() {
		return nodeData;
	}
	public void setNodeData(String nodeData) {
		this.nodeData = nodeData;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getLastUpdatedOn() {
		return lastUpdatedOn;
	}
	public void setLastUpdatedOn(String lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}
	
	
}
