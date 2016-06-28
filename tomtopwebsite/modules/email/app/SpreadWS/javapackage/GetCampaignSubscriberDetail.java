/**
 * GetCampaignSubscriberDetail.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class GetCampaignSubscriberDetail  implements java.io.Serializable {
    private java.lang.String loginEmail;

    private java.lang.String APIKey;

    private java.lang.String subscriber_email;

    private int campaign_id;

    public GetCampaignSubscriberDetail() {
    }

    public GetCampaignSubscriberDetail(
           java.lang.String loginEmail,
           java.lang.String APIKey,
           java.lang.String subscriber_email,
           int campaign_id) {
           this.loginEmail = loginEmail;
           this.APIKey = APIKey;
           this.subscriber_email = subscriber_email;
           this.campaign_id = campaign_id;
    }


    /**
     * Gets the loginEmail value for this GetCampaignSubscriberDetail.
     * 
     * @return loginEmail
     */
    public java.lang.String getLoginEmail() {
        return loginEmail;
    }


    /**
     * Sets the loginEmail value for this GetCampaignSubscriberDetail.
     * 
     * @param loginEmail
     */
    public void setLoginEmail(java.lang.String loginEmail) {
        this.loginEmail = loginEmail;
    }


    /**
     * Gets the APIKey value for this GetCampaignSubscriberDetail.
     * 
     * @return APIKey
     */
    public java.lang.String getAPIKey() {
        return APIKey;
    }


    /**
     * Sets the APIKey value for this GetCampaignSubscriberDetail.
     * 
     * @param APIKey
     */
    public void setAPIKey(java.lang.String APIKey) {
        this.APIKey = APIKey;
    }


    /**
     * Gets the subscriber_email value for this GetCampaignSubscriberDetail.
     * 
     * @return subscriber_email
     */
    public java.lang.String getSubscriber_email() {
        return subscriber_email;
    }


    /**
     * Sets the subscriber_email value for this GetCampaignSubscriberDetail.
     * 
     * @param subscriber_email
     */
    public void setSubscriber_email(java.lang.String subscriber_email) {
        this.subscriber_email = subscriber_email;
    }


    /**
     * Gets the campaign_id value for this GetCampaignSubscriberDetail.
     * 
     * @return campaign_id
     */
    public int getCampaign_id() {
        return campaign_id;
    }


    /**
     * Sets the campaign_id value for this GetCampaignSubscriberDetail.
     * 
     * @param campaign_id
     */
    public void setCampaign_id(int campaign_id) {
        this.campaign_id = campaign_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetCampaignSubscriberDetail)) return false;
        GetCampaignSubscriberDetail other = (GetCampaignSubscriberDetail) obj;
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
            ((this.subscriber_email==null && other.getSubscriber_email()==null) || 
             (this.subscriber_email!=null &&
              this.subscriber_email.equals(other.getSubscriber_email()))) &&
            this.campaign_id == other.getCampaign_id();
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
        if (getSubscriber_email() != null) {
            _hashCode += getSubscriber_email().hashCode();
        }
        _hashCode += getCampaign_id();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetCampaignSubscriberDetail.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">GetCampaignSubscriberDetail"));
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
        elemField.setFieldName("subscriber_email");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "subscriber_email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campaign_id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "campaign_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
