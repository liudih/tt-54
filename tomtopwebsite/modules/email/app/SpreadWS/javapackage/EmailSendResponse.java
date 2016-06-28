/**
 * EmailSendResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class EmailSendResponse  implements java.io.Serializable {
    private java.lang.String emailSendResult;

    public EmailSendResponse() {
    }

    public EmailSendResponse(
           java.lang.String emailSendResult) {
           this.emailSendResult = emailSendResult;
    }


    /**
     * Gets the emailSendResult value for this EmailSendResponse.
     * 
     * @return emailSendResult
     */
    public java.lang.String getEmailSendResult() {
        return emailSendResult;
    }


    /**
     * Sets the emailSendResult value for this EmailSendResponse.
     * 
     * @param emailSendResult
     */
    public void setEmailSendResult(java.lang.String emailSendResult) {
        this.emailSendResult = emailSendResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EmailSendResponse)) return false;
        EmailSendResponse other = (EmailSendResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.emailSendResult==null && other.getEmailSendResult()==null) || 
             (this.emailSendResult!=null &&
              this.emailSendResult.equals(other.getEmailSendResult())));
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
        if (getEmailSendResult() != null) {
            _hashCode += getEmailSendResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EmailSendResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">EmailSendResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("emailSendResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "EmailSendResult"));
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
