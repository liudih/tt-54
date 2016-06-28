/**
 * GetSubscriberDetailResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class GetSubscriberDetailResponse  implements java.io.Serializable {
    private SpreadWS.javapackage.GetSubscriberDetailResponseGetSubscriberDetailResult getSubscriberDetailResult;

    public GetSubscriberDetailResponse() {
    }

    public GetSubscriberDetailResponse(
           SpreadWS.javapackage.GetSubscriberDetailResponseGetSubscriberDetailResult getSubscriberDetailResult) {
           this.getSubscriberDetailResult = getSubscriberDetailResult;
    }


    /**
     * Gets the getSubscriberDetailResult value for this GetSubscriberDetailResponse.
     * 
     * @return getSubscriberDetailResult
     */
    public SpreadWS.javapackage.GetSubscriberDetailResponseGetSubscriberDetailResult getGetSubscriberDetailResult() {
        return getSubscriberDetailResult;
    }


    /**
     * Sets the getSubscriberDetailResult value for this GetSubscriberDetailResponse.
     * 
     * @param getSubscriberDetailResult
     */
    public void setGetSubscriberDetailResult(SpreadWS.javapackage.GetSubscriberDetailResponseGetSubscriberDetailResult getSubscriberDetailResult) {
        this.getSubscriberDetailResult = getSubscriberDetailResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetSubscriberDetailResponse)) return false;
        GetSubscriberDetailResponse other = (GetSubscriberDetailResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getSubscriberDetailResult==null && other.getGetSubscriberDetailResult()==null) || 
             (this.getSubscriberDetailResult!=null &&
              this.getSubscriberDetailResult.equals(other.getGetSubscriberDetailResult())));
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
        if (getGetSubscriberDetailResult() != null) {
            _hashCode += getGetSubscriberDetailResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetSubscriberDetailResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">GetSubscriberDetailResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getSubscriberDetailResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "GetSubscriberDetailResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">>GetSubscriberDetailResponse>GetSubscriberDetailResult"));
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
