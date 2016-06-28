/**
 * CreateCampaign2SerializeResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package services.webservice.rspread;

public class CreateCampaign2SerializeResponse  implements java.io.Serializable {
    private int createCampaign2SerializeResult;

    public CreateCampaign2SerializeResponse() {
    }

    public CreateCampaign2SerializeResponse(
           int createCampaign2SerializeResult) {
           this.createCampaign2SerializeResult = createCampaign2SerializeResult;
    }


    /**
     * Gets the createCampaign2SerializeResult value for this CreateCampaign2SerializeResponse.
     * 
     * @return createCampaign2SerializeResult
     */
    public int getCreateCampaign2SerializeResult() {
        return createCampaign2SerializeResult;
    }


    /**
     * Sets the createCampaign2SerializeResult value for this CreateCampaign2SerializeResponse.
     * 
     * @param createCampaign2SerializeResult
     */
    public void setCreateCampaign2SerializeResult(int createCampaign2SerializeResult) {
        this.createCampaign2SerializeResult = createCampaign2SerializeResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateCampaign2SerializeResponse)) return false;
        CreateCampaign2SerializeResponse other = (CreateCampaign2SerializeResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.createCampaign2SerializeResult == other.getCreateCampaign2SerializeResult();
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
        _hashCode += getCreateCampaign2SerializeResult();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateCampaign2SerializeResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">createCampaign2SerializeResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createCampaign2SerializeResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "createCampaign2SerializeResult"));
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
