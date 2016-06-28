/**
 * GetEmailStatusResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class GetEmailStatusResponse  implements java.io.Serializable {
    private java.lang.String[] getEmailStatusResult;

    public GetEmailStatusResponse() {
    }

    public GetEmailStatusResponse(
           java.lang.String[] getEmailStatusResult) {
           this.getEmailStatusResult = getEmailStatusResult;
    }


    /**
     * Gets the getEmailStatusResult value for this GetEmailStatusResponse.
     * 
     * @return getEmailStatusResult
     */
    public java.lang.String[] getGetEmailStatusResult() {
        return getEmailStatusResult;
    }


    /**
     * Sets the getEmailStatusResult value for this GetEmailStatusResponse.
     * 
     * @param getEmailStatusResult
     */
    public void setGetEmailStatusResult(java.lang.String[] getEmailStatusResult) {
        this.getEmailStatusResult = getEmailStatusResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetEmailStatusResponse)) return false;
        GetEmailStatusResponse other = (GetEmailStatusResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getEmailStatusResult==null && other.getGetEmailStatusResult()==null) || 
             (this.getEmailStatusResult!=null &&
              java.util.Arrays.equals(this.getEmailStatusResult, other.getGetEmailStatusResult())));
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
        if (getGetEmailStatusResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGetEmailStatusResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGetEmailStatusResult(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetEmailStatusResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">GetEmailStatusResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getEmailStatusResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "GetEmailStatusResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "string"));
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
