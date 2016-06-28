/**
 * GetAccessTokenResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package services.webservice.rspread;

public class GetAccessTokenResponse  implements java.io.Serializable {
    private java.lang.String getAccessTokenResult;

    public GetAccessTokenResponse() {
    }

    public GetAccessTokenResponse(
           java.lang.String getAccessTokenResult) {
           this.getAccessTokenResult = getAccessTokenResult;
    }


    /**
     * Gets the getAccessTokenResult value for this GetAccessTokenResponse.
     * 
     * @return getAccessTokenResult
     */
    public java.lang.String getGetAccessTokenResult() {
        return getAccessTokenResult;
    }


    /**
     * Sets the getAccessTokenResult value for this GetAccessTokenResponse.
     * 
     * @param getAccessTokenResult
     */
    public void setGetAccessTokenResult(java.lang.String getAccessTokenResult) {
        this.getAccessTokenResult = getAccessTokenResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetAccessTokenResponse)) return false;
        GetAccessTokenResponse other = (GetAccessTokenResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getAccessTokenResult==null && other.getGetAccessTokenResult()==null) || 
             (this.getAccessTokenResult!=null &&
              this.getAccessTokenResult.equals(other.getGetAccessTokenResult())));
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
        if (getGetAccessTokenResult() != null) {
            _hashCode += getGetAccessTokenResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetAccessTokenResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">GetAccessTokenResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getAccessTokenResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "GetAccessTokenResult"));
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
