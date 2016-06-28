/**
 * SendSMS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class SendSMS  implements java.io.Serializable {
    private java.lang.String loginEmail;

    private java.lang.String APIKey;

    private java.lang.String SMSContent;

    private java.lang.String sender;

    private java.util.Calendar schedule;

    private SpreadWS.javapackage.CampaignStatus campaignStatus;

    private java.lang.String[] phoneSubscribers;

    private java.lang.String[] category;

    public SendSMS() {
    }

    public SendSMS(
           java.lang.String loginEmail,
           java.lang.String APIKey,
           java.lang.String SMSContent,
           java.lang.String sender,
           java.util.Calendar schedule,
           SpreadWS.javapackage.CampaignStatus campaignStatus,
           java.lang.String[] phoneSubscribers,
           java.lang.String[] category) {
           this.loginEmail = loginEmail;
           this.APIKey = APIKey;
           this.SMSContent = SMSContent;
           this.sender = sender;
           this.schedule = schedule;
           this.campaignStatus = campaignStatus;
           this.phoneSubscribers = phoneSubscribers;
           this.category = category;
    }


    /**
     * Gets the loginEmail value for this SendSMS.
     * 
     * @return loginEmail
     */
    public java.lang.String getLoginEmail() {
        return loginEmail;
    }


    /**
     * Sets the loginEmail value for this SendSMS.
     * 
     * @param loginEmail
     */
    public void setLoginEmail(java.lang.String loginEmail) {
        this.loginEmail = loginEmail;
    }


    /**
     * Gets the APIKey value for this SendSMS.
     * 
     * @return APIKey
     */
    public java.lang.String getAPIKey() {
        return APIKey;
    }


    /**
     * Sets the APIKey value for this SendSMS.
     * 
     * @param APIKey
     */
    public void setAPIKey(java.lang.String APIKey) {
        this.APIKey = APIKey;
    }


    /**
     * Gets the SMSContent value for this SendSMS.
     * 
     * @return SMSContent
     */
    public java.lang.String getSMSContent() {
        return SMSContent;
    }


    /**
     * Sets the SMSContent value for this SendSMS.
     * 
     * @param SMSContent
     */
    public void setSMSContent(java.lang.String SMSContent) {
        this.SMSContent = SMSContent;
    }


    /**
     * Gets the sender value for this SendSMS.
     * 
     * @return sender
     */
    public java.lang.String getSender() {
        return sender;
    }


    /**
     * Sets the sender value for this SendSMS.
     * 
     * @param sender
     */
    public void setSender(java.lang.String sender) {
        this.sender = sender;
    }


    /**
     * Gets the schedule value for this SendSMS.
     * 
     * @return schedule
     */
    public java.util.Calendar getSchedule() {
        return schedule;
    }


    /**
     * Sets the schedule value for this SendSMS.
     * 
     * @param schedule
     */
    public void setSchedule(java.util.Calendar schedule) {
        this.schedule = schedule;
    }


    /**
     * Gets the campaignStatus value for this SendSMS.
     * 
     * @return campaignStatus
     */
    public SpreadWS.javapackage.CampaignStatus getCampaignStatus() {
        return campaignStatus;
    }


    /**
     * Sets the campaignStatus value for this SendSMS.
     * 
     * @param campaignStatus
     */
    public void setCampaignStatus(SpreadWS.javapackage.CampaignStatus campaignStatus) {
        this.campaignStatus = campaignStatus;
    }


    /**
     * Gets the phoneSubscribers value for this SendSMS.
     * 
     * @return phoneSubscribers
     */
    public java.lang.String[] getPhoneSubscribers() {
        return phoneSubscribers;
    }


    /**
     * Sets the phoneSubscribers value for this SendSMS.
     * 
     * @param phoneSubscribers
     */
    public void setPhoneSubscribers(java.lang.String[] phoneSubscribers) {
        this.phoneSubscribers = phoneSubscribers;
    }


    /**
     * Gets the category value for this SendSMS.
     * 
     * @return category
     */
    public java.lang.String[] getCategory() {
        return category;
    }


    /**
     * Sets the category value for this SendSMS.
     * 
     * @param category
     */
    public void setCategory(java.lang.String[] category) {
        this.category = category;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SendSMS)) return false;
        SendSMS other = (SendSMS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.loginEmail==null && other.getLoginEmail()==null) || 
             (this.loginEmail!=null &&
              this.loginEmail.equals(other.getLoginEmail()))) &&
            ((this.APIKey==null && other.getAPIKey()==null) || 
             (this.APIKey!=null &&
              this.APIKey.equals(other.getAPIKey()))) &&
            ((this.SMSContent==null && other.getSMSContent()==null) || 
             (this.SMSContent!=null &&
              this.SMSContent.equals(other.getSMSContent()))) &&
            ((this.sender==null && other.getSender()==null) || 
             (this.sender!=null &&
              this.sender.equals(other.getSender()))) &&
            ((this.schedule==null && other.getSchedule()==null) || 
             (this.schedule!=null &&
              this.schedule.equals(other.getSchedule()))) &&
            ((this.campaignStatus==null && other.getCampaignStatus()==null) || 
             (this.campaignStatus!=null &&
              this.campaignStatus.equals(other.getCampaignStatus()))) &&
            ((this.phoneSubscribers==null && other.getPhoneSubscribers()==null) || 
             (this.phoneSubscribers!=null &&
              java.util.Arrays.equals(this.phoneSubscribers, other.getPhoneSubscribers()))) &&
            ((this.category==null && other.getCategory()==null) || 
             (this.category!=null &&
              java.util.Arrays.equals(this.category, other.getCategory())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getLoginEmail() != null) {
            _hashCode += getLoginEmail().hashCode();
        }
        if (getAPIKey() != null) {
            _hashCode += getAPIKey().hashCode();
        }
        if (getSMSContent() != null) {
            _hashCode += getSMSContent().hashCode();
        }
        if (getSender() != null) {
            _hashCode += getSender().hashCode();
        }
        if (getSchedule() != null) {
            _hashCode += getSchedule().hashCode();
        }
        if (getCampaignStatus() != null) {
            _hashCode += getCampaignStatus().hashCode();
        }
        if (getPhoneSubscribers() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPhoneSubscribers());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPhoneSubscribers(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCategory() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCategory());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCategory(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SendSMS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">SendSMS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loginEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "loginEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("APIKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "APIKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SMSContent");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "SMSContent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sender");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "sender"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("schedule");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "schedule"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campaignStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "campaignStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "CampaignStatus"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("phoneSubscribers");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "phoneSubscribers"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("category");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "category"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "string"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
