/**
 * ExcludeContactListResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class ExcludeContactListResponse  implements java.io.Serializable {
    private int excludeContactListResult;

    public ExcludeContactListResponse() {
    }

    public ExcludeContactListResponse(
           int excludeContactListResult) {
           this.excludeContactListResult = excludeContactListResult;
    }


    /**
     * Gets the excludeContactListResult value for this ExcludeContactListResponse.
     * 
     * @return excludeContactListResult
     */
    public int getExcludeContactListResult() {
        return excludeContactListResult;
    }


    /**
     * Sets the excludeContactListResult value for this ExcludeContactListResponse.
     * 
     * @param excludeContactListResult
     */
    public void setExcludeContactListResult(int excludeContactListResult) {
        this.excludeContactListResult = excludeContactListResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ExcludeContactListResponse)) return false;
        ExcludeContactListResponse other = (ExcludeContactListResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.excludeContactListResult == other.getExcludeContactListResult();
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
        _hashCode += getExcludeContactListResult();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExcludeContactListResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">ExcludeContactListResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("excludeContactListResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "ExcludeContactListResult"));
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
