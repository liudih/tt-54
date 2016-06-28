/**
 * GetSubscribersResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package services.webservice.rspread;

public class GetSubscribersResponse  implements java.io.Serializable {
    private services.webservice.rspread.GetSubscribersResponseGetSubscribersResult getSubscribersResult;

    public GetSubscribersResponse() {
    }

    public GetSubscribersResponse(
           services.webservice.rspread.GetSubscribersResponseGetSubscribersResult getSubscribersResult) {
           this.getSubscribersResult = getSubscribersResult;
    }


    /**
     * Gets the getSubscribersResult value for this GetSubscribersResponse.
     * 
     * @return getSubscribersResult
     */
    public services.webservice.rspread.GetSubscribersResponseGetSubscribersResult getGetSubscribersResult() {
        return getSubscribersResult;
    }


    /**
     * Sets the getSubscribersResult value for this GetSubscribersResponse.
     * 
     * @param getSubscribersResult
     */
    public void setGetSubscribersResult(services.webservice.rspread.GetSubscribersResponseGetSubscribersResult getSubscribersResult) {
        this.getSubscribersResult = getSubscribersResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetSubscribersResponse)) return false;
        GetSubscribersResponse other = (GetSubscribersResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getSubscribersResult==null && other.getGetSubscribersResult()==null) || 
             (this.getSubscribersResult!=null &&
              this.getSubscribersResult.equals(other.getGetSubscribersResult())));
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
        if (getGetSubscribersResult() != null) {
            _hashCode += getGetSubscribersResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetSubscribersResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">GetSubscribersResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getSubscribersResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "GetSubscribersResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">>GetSubscribersResponse>GetSubscribersResult"));
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
