/**
 * TransferCredit.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package services.webservice.rspread;

public class TransferCredit  implements java.io.Serializable {
    private java.lang.String loginEmail;

    private java.lang.String loginPassword;

    private java.lang.String transferToEmail;

    private int creditCount;

    public TransferCredit() {
    }

    public TransferCredit(
           java.lang.String loginEmail,
           java.lang.String loginPassword,
           java.lang.String transferToEmail,
           int creditCount) {
           this.loginEmail = loginEmail;
           this.loginPassword = loginPassword;
           this.transferToEmail = transferToEmail;
           this.creditCount = creditCount;
    }


    /**
     * Gets the loginEmail value for this TransferCredit.
     * 
     * @return loginEmail
     */
    public java.lang.String getLoginEmail() {
        return loginEmail;
    }


    /**
     * Sets the loginEmail value for this TransferCredit.
     * 
     * @param loginEmail
     */
    public void setLoginEmail(java.lang.String loginEmail) {
        this.loginEmail = loginEmail;
    }


    /**
     * Gets the loginPassword value for this TransferCredit.
     * 
     * @return loginPassword
     */
    public java.lang.String getLoginPassword() {
        return loginPassword;
    }


    /**
     * Sets the loginPassword value for this TransferCredit.
     * 
     * @param loginPassword
     */
    public void setLoginPassword(java.lang.String loginPassword) {
        this.loginPassword = loginPassword;
    }


    /**
     * Gets the transferToEmail value for this TransferCredit.
     * 
     * @return transferToEmail
     */
    public java.lang.String getTransferToEmail() {
        return transferToEmail;
    }


    /**
     * Sets the transferToEmail value for this TransferCredit.
     * 
     * @param transferToEmail
     */
    public void setTransferToEmail(java.lang.String transferToEmail) {
        this.transferToEmail = transferToEmail;
    }


    /**
     * Gets the creditCount value for this TransferCredit.
     * 
     * @return creditCount
     */
    public int getCreditCount() {
        return creditCount;
    }


    /**
     * Sets the creditCount value for this TransferCredit.
     * 
     * @param creditCount
     */
    public void setCreditCount(int creditCount) {
        this.creditCount = creditCount;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TransferCredit)) return false;
        TransferCredit other = (TransferCredit) obj;
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
            ((this.transferToEmail==null && other.getTransferToEmail()==null) || 
             (this.transferToEmail!=null &&
              this.transferToEmail.equals(other.getTransferToEmail()))) &&
            this.creditCount == other.getCreditCount();
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
        if (getTransferToEmail() != null) {
            _hashCode += getTransferToEmail().hashCode();
        }
        _hashCode += getCreditCount();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TransferCredit.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">TransferCredit"));
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
        elemField.setFieldName("transferToEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "transferToEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creditCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "creditCount"));
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
