/**
 * UnsubscribeEmailInContactList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class UnsubscribeEmailInContactList  implements java.io.Serializable {
    private java.lang.String loginEmail;

    private java.lang.String APIKey;

    private java.lang.String subscriberEmail;

    private java.lang.String contactListName;

    public UnsubscribeEmailInContactList() {
    }

    public UnsubscribeEmailInContactList(
           java.lang.String loginEmail,
           java.lang.String APIKey,
           java.lang.String subscriberEmail,
           java.lang.String contactListName) {
           this.loginEmail = loginEmail;
           this.APIKey = APIKey;
           this.subscriberEmail = subscriberEmail;
           this.contactListName = contactListName;
    }


    /**
     * Gets the loginEmail value for this UnsubscribeEmailInContactList.
     * 
     * @return loginEmail
     */
    public java.lang.String getLoginEmail() {
        return loginEmail;
    }


    /**
     * Sets the loginEmail value for this UnsubscribeEmailInContactList.
     * 
     * @param loginEmail
     */
    public void setLoginEmail(java.lang.String loginEmail) {
        this.loginEmail = loginEmail;
    }


    /**
     * Gets the APIKey value for this UnsubscribeEmailInContactList.
     * 
     * @return APIKey
     */
    public java.lang.String getAPIKey() {
        return APIKey;
    }


    /**
     * Sets the APIKey value for this UnsubscribeEmailInContactList.
     * 
     * @param APIKey
     */
    public void setAPIKey(java.lang.String APIKey) {
        this.APIKey = APIKey;
    }


    /**
     * Gets the subscriberEmail value for this UnsubscribeEmailInContactList.
     * 
     * @return subscriberEmail
     */
    public java.lang.String getSubscriberEmail() {
        return subscriberEmail;
    }


    /**
     * Sets the subscriberEmail value for this UnsubscribeEmailInContactList.
     * 
     * @param subscriberEmail
     */
    public void setSubscriberEmail(java.lang.String subscriberEmail) {
        this.subscriberEmail = subscriberEmail;
    }


    /**
     * Gets the contactListName value for this UnsubscribeEmailInContactList.
     * 
     * @return contactListName
     */
    public java.lang.String getContactListName() {
        return contactListName;
    }


    /**
     * Sets the contactListName value for this UnsubscribeEmailInContactList.
     * 
     * @param contactListName
     */
    public void setContactListName(java.lang.String contactListName) {
        this.contactListName = contactListName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UnsubscribeEmailInContactList)) return false;
        UnsubscribeEmailInContactList other = (UnsubscribeEmailInContactList) obj;
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
            ((this.subscriberEmail==null && other.getSubscriberEmail()==null) || 
             (this.subscriberEmail!=null &&
              this.subscriberEmail.equals(other.getSubscriberEmail()))) &&
            ((this.contactListName==null && other.getContactListName()==null) || 
             (this.contactListName!=null &&
              this.contactListName.equals(other.getContactListName())));
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
        if (getSubscriberEmail() != null) {
            _hashCode += getSubscriberEmail().hashCode();
        }
        if (getContactListName() != null) {
            _hashCode += getContactListName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UnsubscribeEmailInContactList.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">UnsubscribeEmailInContactList"));
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
        elemField.setFieldName("subscriberEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "subscriberEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contactListName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "contactListName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
