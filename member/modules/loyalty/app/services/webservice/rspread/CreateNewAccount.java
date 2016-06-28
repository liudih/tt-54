/**
 * CreateNewAccount.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package services.webservice.rspread;

public class CreateNewAccount  implements java.io.Serializable {
    private java.lang.String loginEmail;

    private java.lang.String loginPassword;

    private java.lang.String newAccountName;

    private java.lang.String newAccountEmail;

    private java.lang.String newAccountPassword;

    public CreateNewAccount() {
    }

    public CreateNewAccount(
           java.lang.String loginEmail,
           java.lang.String loginPassword,
           java.lang.String newAccountName,
           java.lang.String newAccountEmail,
           java.lang.String newAccountPassword) {
           this.loginEmail = loginEmail;
           this.loginPassword = loginPassword;
           this.newAccountName = newAccountName;
           this.newAccountEmail = newAccountEmail;
           this.newAccountPassword = newAccountPassword;
    }


    /**
     * Gets the loginEmail value for this CreateNewAccount.
     * 
     * @return loginEmail
     */
    public java.lang.String getLoginEmail() {
        return loginEmail;
    }


    /**
     * Sets the loginEmail value for this CreateNewAccount.
     * 
     * @param loginEmail
     */
    public void setLoginEmail(java.lang.String loginEmail) {
        this.loginEmail = loginEmail;
    }


    /**
     * Gets the loginPassword value for this CreateNewAccount.
     * 
     * @return loginPassword
     */
    public java.lang.String getLoginPassword() {
        return loginPassword;
    }


    /**
     * Sets the loginPassword value for this CreateNewAccount.
     * 
     * @param loginPassword
     */
    public void setLoginPassword(java.lang.String loginPassword) {
        this.loginPassword = loginPassword;
    }


    /**
     * Gets the newAccountName value for this CreateNewAccount.
     * 
     * @return newAccountName
     */
    public java.lang.String getNewAccountName() {
        return newAccountName;
    }


    /**
     * Sets the newAccountName value for this CreateNewAccount.
     * 
     * @param newAccountName
     */
    public void setNewAccountName(java.lang.String newAccountName) {
        this.newAccountName = newAccountName;
    }


    /**
     * Gets the newAccountEmail value for this CreateNewAccount.
     * 
     * @return newAccountEmail
     */
    public java.lang.String getNewAccountEmail() {
        return newAccountEmail;
    }


    /**
     * Sets the newAccountEmail value for this CreateNewAccount.
     * 
     * @param newAccountEmail
     */
    public void setNewAccountEmail(java.lang.String newAccountEmail) {
        this.newAccountEmail = newAccountEmail;
    }


    /**
     * Gets the newAccountPassword value for this CreateNewAccount.
     * 
     * @return newAccountPassword
     */
    public java.lang.String getNewAccountPassword() {
        return newAccountPassword;
    }


    /**
     * Sets the newAccountPassword value for this CreateNewAccount.
     * 
     * @param newAccountPassword
     */
    public void setNewAccountPassword(java.lang.String newAccountPassword) {
        this.newAccountPassword = newAccountPassword;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateNewAccount)) return false;
        CreateNewAccount other = (CreateNewAccount) obj;
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
            ((this.loginPassword==null && other.getLoginPassword()==null) || 
             (this.loginPassword!=null &&
              this.loginPassword.equals(other.getLoginPassword()))) &&
            ((this.newAccountName==null && other.getNewAccountName()==null) || 
             (this.newAccountName!=null &&
              this.newAccountName.equals(other.getNewAccountName()))) &&
            ((this.newAccountEmail==null && other.getNewAccountEmail()==null) || 
             (this.newAccountEmail!=null &&
              this.newAccountEmail.equals(other.getNewAccountEmail()))) &&
            ((this.newAccountPassword==null && other.getNewAccountPassword()==null) || 
             (this.newAccountPassword!=null &&
              this.newAccountPassword.equals(other.getNewAccountPassword())));
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
        if (getLoginPassword() != null) {
            _hashCode += getLoginPassword().hashCode();
        }
        if (getNewAccountName() != null) {
            _hashCode += getNewAccountName().hashCode();
        }
        if (getNewAccountEmail() != null) {
            _hashCode += getNewAccountEmail().hashCode();
        }
        if (getNewAccountPassword() != null) {
            _hashCode += getNewAccountPassword().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateNewAccount.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">CreateNewAccount"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loginEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "loginEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loginPassword");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "loginPassword"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("newAccountName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "newAccountName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("newAccountEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "newAccountEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("newAccountPassword");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "newAccountPassword"));
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
