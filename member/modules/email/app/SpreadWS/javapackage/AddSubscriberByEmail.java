/**
 * AddSubscriberByEmail.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class AddSubscriberByEmail  implements java.io.Serializable {
    private java.lang.String loginEmail;

    private java.lang.String password;

    private java.lang.String subscriberEmail;

    private java.lang.String subscription;

    private SpreadWS.javapackage.DoubleOptIn optInType;

    public AddSubscriberByEmail() {
    }

    public AddSubscriberByEmail(
           java.lang.String loginEmail,
           java.lang.String password,
           java.lang.String subscriberEmail,
           java.lang.String subscription,
           SpreadWS.javapackage.DoubleOptIn optInType) {
           this.loginEmail = loginEmail;
           this.password = password;
           this.subscriberEmail = subscriberEmail;
           this.subscription = subscription;
           this.optInType = optInType;
    }


    /**
     * Gets the loginEmail value for this AddSubscriberByEmail.
     * 
     * @return loginEmail
     */
    public java.lang.String getLoginEmail() {
        return loginEmail;
    }


    /**
     * Sets the loginEmail value for this AddSubscriberByEmail.
     * 
     * @param loginEmail
     */
    public void setLoginEmail(java.lang.String loginEmail) {
        this.loginEmail = loginEmail;
    }


    /**
     * Gets the password value for this AddSubscriberByEmail.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this AddSubscriberByEmail.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }


    /**
     * Gets the subscriberEmail value for this AddSubscriberByEmail.
     * 
     * @return subscriberEmail
     */
    public java.lang.String getSubscriberEmail() {
        return subscriberEmail;
    }


    /**
     * Sets the subscriberEmail value for this AddSubscriberByEmail.
     * 
     * @param subscriberEmail
     */
    public void setSubscriberEmail(java.lang.String subscriberEmail) {
        this.subscriberEmail = subscriberEmail;
    }


    /**
     * Gets the subscription value for this AddSubscriberByEmail.
     * 
     * @return subscription
     */
    public java.lang.String getSubscription() {
        return subscription;
    }


    /**
     * Sets the subscription value for this AddSubscriberByEmail.
     * 
     * @param subscription
     */
    public void setSubscription(java.lang.String subscription) {
        this.subscription = subscription;
    }


    /**
     * Gets the optInType value for this AddSubscriberByEmail.
     * 
     * @return optInType
     */
    public SpreadWS.javapackage.DoubleOptIn getOptInType() {
        return optInType;
    }


    /**
     * Sets the optInType value for this AddSubscriberByEmail.
     * 
     * @param optInType
     */
    public void setOptInType(SpreadWS.javapackage.DoubleOptIn optInType) {
        this.optInType = optInType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AddSubscriberByEmail)) return false;
        AddSubscriberByEmail other = (AddSubscriberByEmail) obj;
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
            ((this.subscriberEmail==null && other.getSubscriberEmail()==null) || 
             (this.subscriberEmail!=null &&
              this.subscriberEmail.equals(other.getSubscriberEmail()))) &&
            ((this.subscription==null && other.getSubscription()==null) || 
             (this.subscription!=null &&
              this.subscription.equals(other.getSubscription()))) &&
            ((this.optInType==null && other.getOptInType()==null) || 
             (this.optInType!=null &&
              this.optInType.equals(other.getOptInType())));
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
        if (getSubscriberEmail() != null) {
            _hashCode += getSubscriberEmail().hashCode();
        }
        if (getSubscription() != null) {
            _hashCode += getSubscription().hashCode();
        }
        if (getOptInType() != null) {
            _hashCode += getOptInType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AddSubscriberByEmail.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">addSubscriberByEmail"));
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
        elemField.setFieldName("subscriberEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "subscriberEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subscription");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "subscription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("optInType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "optInType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "DoubleOptIn"));
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
