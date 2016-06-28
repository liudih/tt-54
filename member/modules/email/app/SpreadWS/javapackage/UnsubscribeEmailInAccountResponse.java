/**
 * UnsubscribeEmailInAccountResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class UnsubscribeEmailInAccountResponse  implements java.io.Serializable {
    private boolean unsubscribeEmailInAccountResult;

    public UnsubscribeEmailInAccountResponse() {
    }

    public UnsubscribeEmailInAccountResponse(
           boolean unsubscribeEmailInAccountResult) {
           this.unsubscribeEmailInAccountResult = unsubscribeEmailInAccountResult;
    }


    /**
     * Gets the unsubscribeEmailInAccountResult value for this UnsubscribeEmailInAccountResponse.
     * 
     * @return unsubscribeEmailInAccountResult
     */
    public boolean isUnsubscribeEmailInAccountResult() {
        return unsubscribeEmailInAccountResult;
    }


    /**
     * Sets the unsubscribeEmailInAccountResult value for this UnsubscribeEmailInAccountResponse.
     * 
     * @param unsubscribeEmailInAccountResult
     */
    public void setUnsubscribeEmailInAccountResult(boolean unsubscribeEmailInAccountResult) {
        this.unsubscribeEmailInAccountResult = unsubscribeEmailInAccountResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UnsubscribeEmailInAccountResponse)) return false;
        UnsubscribeEmailInAccountResponse other = (UnsubscribeEmailInAccountResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.unsubscribeEmailInAccountResult == other.isUnsubscribeEmailInAccountResult();
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
        _hashCode += (isUnsubscribeEmailInAccountResult() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UnsubscribeEmailInAccountResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">UnsubscribeEmailInAccountResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unsubscribeEmailInAccountResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "UnsubscribeEmailInAccountResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
