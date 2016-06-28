/**
 * AddSenderEmailResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package services.webservice.rspread;

public class AddSenderEmailResponse  implements java.io.Serializable {
    private boolean addSenderEmailResult;

    public AddSenderEmailResponse() {
    }

    public AddSenderEmailResponse(
           boolean addSenderEmailResult) {
           this.addSenderEmailResult = addSenderEmailResult;
    }


    /**
     * Gets the addSenderEmailResult value for this AddSenderEmailResponse.
     * 
     * @return addSenderEmailResult
     */
    public boolean isAddSenderEmailResult() {
        return addSenderEmailResult;
    }


    /**
     * Sets the addSenderEmailResult value for this AddSenderEmailResponse.
     * 
     * @param addSenderEmailResult
     */
    public void setAddSenderEmailResult(boolean addSenderEmailResult) {
        this.addSenderEmailResult = addSenderEmailResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AddSenderEmailResponse)) return false;
        AddSenderEmailResponse other = (AddSenderEmailResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.addSenderEmailResult == other.isAddSenderEmailResult();
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
        _hashCode += (isAddSenderEmailResult() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AddSenderEmailResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">AddSenderEmailResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addSenderEmailResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "AddSenderEmailResult"));
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
