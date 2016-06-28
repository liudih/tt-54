/**
 * GetUploadRequestStatusResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class GetUploadRequestStatusResponse  implements java.io.Serializable {
    private SpreadWS.javapackage.GetUploadRequestStatusResponseGetUploadRequestStatusResult getUploadRequestStatusResult;

    public GetUploadRequestStatusResponse() {
    }

    public GetUploadRequestStatusResponse(
           SpreadWS.javapackage.GetUploadRequestStatusResponseGetUploadRequestStatusResult getUploadRequestStatusResult) {
           this.getUploadRequestStatusResult = getUploadRequestStatusResult;
    }


    /**
     * Gets the getUploadRequestStatusResult value for this GetUploadRequestStatusResponse.
     * 
     * @return getUploadRequestStatusResult
     */
    public SpreadWS.javapackage.GetUploadRequestStatusResponseGetUploadRequestStatusResult getGetUploadRequestStatusResult() {
        return getUploadRequestStatusResult;
    }


    /**
     * Sets the getUploadRequestStatusResult value for this GetUploadRequestStatusResponse.
     * 
     * @param getUploadRequestStatusResult
     */
    public void setGetUploadRequestStatusResult(SpreadWS.javapackage.GetUploadRequestStatusResponseGetUploadRequestStatusResult getUploadRequestStatusResult) {
        this.getUploadRequestStatusResult = getUploadRequestStatusResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetUploadRequestStatusResponse)) return false;
        GetUploadRequestStatusResponse other = (GetUploadRequestStatusResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getUploadRequestStatusResult==null && other.getGetUploadRequestStatusResult()==null) || 
             (this.getUploadRequestStatusResult!=null &&
              this.getUploadRequestStatusResult.equals(other.getGetUploadRequestStatusResult())));
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
        if (getGetUploadRequestStatusResult() != null) {
            _hashCode += getGetUploadRequestStatusResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetUploadRequestStatusResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">GetUploadRequestStatusResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getUploadRequestStatusResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "GetUploadRequestStatusResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">>GetUploadRequestStatusResponse>GetUploadRequestStatusResult"));
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
