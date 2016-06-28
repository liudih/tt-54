/**
 * UpdateCampaignStatus.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class UpdateCampaignStatus  implements java.io.Serializable {
    private java.lang.String loginEmail;

    private java.lang.String password;

    private java.lang.String campaignID;

    private SpreadWS.javapackage.CampaignStatus campaignStatus;

    public UpdateCampaignStatus() {
    }

    public UpdateCampaignStatus(
           java.lang.String loginEmail,
           java.lang.String password,
           java.lang.String campaignID,
           SpreadWS.javapackage.CampaignStatus campaignStatus) {
           this.loginEmail = loginEmail;
           this.password = password;
           this.campaignID = campaignID;
           this.campaignStatus = campaignStatus;
    }


    /**
     * Gets the loginEmail value for this UpdateCampaignStatus.
     * 
     * @return loginEmail
     */
    public java.lang.String getLoginEmail() {
        return loginEmail;
    }


    /**
     * Sets the loginEmail value for this UpdateCampaignStatus.
     * 
     * @param loginEmail
     */
    public void setLoginEmail(java.lang.String loginEmail) {
        this.loginEmail = loginEmail;
    }


    /**
     * Gets the password value for this UpdateCampaignStatus.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this UpdateCampaignStatus.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }


    /**
     * Gets the campaignID value for this UpdateCampaignStatus.
     * 
     * @return campaignID
     */
    public java.lang.String getCampaignID() {
        return campaignID;
    }


    /**
     * Sets the campaignID value for this UpdateCampaignStatus.
     * 
     * @param campaignID
     */
    public void setCampaignID(java.lang.String campaignID) {
        this.campaignID = campaignID;
    }


    /**
     * Gets the campaignStatus value for this UpdateCampaignStatus.
     * 
     * @return campaignStatus
     */
    public SpreadWS.javapackage.CampaignStatus getCampaignStatus() {
        return campaignStatus;
    }


    /**
     * Sets the campaignStatus value for this UpdateCampaignStatus.
     * 
     * @param campaignStatus
     */
    public void setCampaignStatus(SpreadWS.javapackage.CampaignStatus campaignStatus) {
        this.campaignStatus = campaignStatus;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UpdateCampaignStatus)) return false;
        UpdateCampaignStatus other = (UpdateCampaignStatus) obj;
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
            ((this.password==null && other.getPassword()==null) || 
             (this.password!=null &&
              this.password.equals(other.getPassword()))) &&
            ((this.campaignID==null && other.getCampaignID()==null) || 
             (this.campaignID!=null &&
              this.campaignID.equals(other.getCampaignID()))) &&
            ((this.campaignStatus==null && other.getCampaignStatus()==null) || 
             (this.campaignStatus!=null &&
              this.campaignStatus.equals(other.getCampaignStatus())));
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
        if (getPassword() != null) {
            _hashCode += getPassword().hashCode();
        }
        if (getCampaignID() != null) {
            _hashCode += getCampaignID().hashCode();
        }
        if (getCampaignStatus() != null) {
            _hashCode += getCampaignStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UpdateCampaignStatus.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">UpdateCampaignStatus"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loginEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "loginEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("password");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campaignID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "campaignID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campaignStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "campaignStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "CampaignStatus"));
        elemField.setNillable(false);
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
