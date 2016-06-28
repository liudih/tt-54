/**
 * GetSubscriptionResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package services.webservice.rspread;

public class GetSubscriptionResponse  implements java.io.Serializable {
    private services.webservice.rspread.GetSubscriptionResponseGetSubscriptionResult getSubscriptionResult;

    public GetSubscriptionResponse() {
    }

    public GetSubscriptionResponse(
           services.webservice.rspread.GetSubscriptionResponseGetSubscriptionResult getSubscriptionResult) {
           this.getSubscriptionResult = getSubscriptionResult;
    }


    /**
     * Gets the getSubscriptionResult value for this GetSubscriptionResponse.
     * 
     * @return getSubscriptionResult
     */
    public services.webservice.rspread.GetSubscriptionResponseGetSubscriptionResult getGetSubscriptionResult() {
        return getSubscriptionResult;
    }


    /**
     * Sets the getSubscriptionResult value for this GetSubscriptionResponse.
     * 
     * @param getSubscriptionResult
     */
    public void setGetSubscriptionResult(services.webservice.rspread.GetSubscriptionResponseGetSubscriptionResult getSubscriptionResult) {
        this.getSubscriptionResult = getSubscriptionResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetSubscriptionResponse)) return false;
        GetSubscriptionResponse other = (GetSubscriptionResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getSubscriptionResult==null && other.getGetSubscriptionResult()==null) || 
             (this.getSubscriptionResult!=null &&
              this.getSubscriptionResult.equals(other.getGetSubscriptionResult())));
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
        if (getGetSubscriptionResult() != null) {
            _hashCode += getGetSubscriptionResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetSubscriptionResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">getSubscriptionResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getSubscriptionResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "getSubscriptionResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">>getSubscriptionResponse>getSubscriptionResult"));
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
