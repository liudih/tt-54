/**
 * AddSubscriberByInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class AddSubscriberByInfo  implements java.io.Serializable {
    private java.lang.String loginEmail;

    private java.lang.String password;

    private SpreadWS.javapackage.Subscriber subscriberArgs;

    private java.lang.String subscription;

    private SpreadWS.javapackage.DoubleOptIn optInType;

    public AddSubscriberByInfo() {
    }

    public AddSubscriberByInfo(
           java.lang.String loginEmail,
           java.lang.String password,
           SpreadWS.javapackage.Subscriber subscriberArgs,
           java.lang.String subscription,
           SpreadWS.javapackage.DoubleOptIn optInType) {
           this.loginEmail = loginEmail;
           this.password = password;
           this.subscriberArgs = subscriberArgs;
           this.subscription = subscription;
           this.optInType = optInType;
    }


    /**
     * Gets the loginEmail value for this AddSubscriberByInfo.
     * 
     * @return loginEmail
     */
    public java.lang.String getLoginEmail() {
        return loginEmail;
    }


    /**
     * Sets the loginEmail value for this AddSubscriberByInfo.
     * 
     * @param loginEmail
     */
    public void setLoginEmail(java.lang.String loginEmail) {
        this.loginEmail = loginEmail;
    }


    /**
     * Gets the password value for this AddSubscriberByInfo.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this AddSubscriberByInfo.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }


    /**
     * Gets the subscriberArgs value for this AddSubscriberByInfo.
     * 
     * @return subscriberArgs
     */
    public SpreadWS.javapackage.Subscriber getSubscriberArgs() {
        return subscriberArgs;
    }


    /**
     * Sets the subscriberArgs value for this AddSubscriberByInfo.
     * 
     * @param subscriberArgs
     */
    public void setSubscriberArgs(SpreadWS.javapackage.Subscriber subscriberArgs) {
        this.subscriberArgs = subscriberArgs;
    }


    /**
     * Gets the subscription value for this AddSubscriberByInfo.
     * 
     * @return subscription
     */
    public java.lang.String getSubscription() {
        return subscription;
    }


    /**
     * Sets the subscription value for this AddSubscriberByInfo.
     * 
     * @param subscription
     */
    public void setSubscription(java.lang.String subscription) {
        this.subscription = subscription;
    }


    /**
     * Gets the optInType value for this AddSubscriberByInfo.
     * 
     * @return optInType
     */
    public SpreadWS.javapackage.DoubleOptIn getOptInType() {
        return optInType;
    }


    /**
     * Sets the optInType value for this AddSubscriberByInfo.
     * 
     * @param optInType
     */
    public void setOptInType(SpreadWS.javapackage.DoubleOptIn optInType) {
        this.optInType = optInType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AddSubscriberByInfo)) return false;
        AddSubscriberByInfo other = (AddSubscriberByInfo) obj;
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
            ((this.subscriberArgs==null && other.getSubscriberArgs()==null) || 
             (this.subscriberArgs!=null &&
              this.subscriberArgs.equals(other.getSubscriberArgs()))) &&
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
        if (getSubscriberArgs() != null) {
            _hashCode += getSubscriberArgs().hashCode();
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
        new org.apache.axis.description.TypeDesc(AddSubscriberByInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">addSubscriberByInfo"));
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
        elemField.setFieldName("subscriberArgs");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "subscriberArgs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "Subscriber"));
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
