/**
 * GetUserSentReportResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class GetUserSentReportResponse  implements java.io.Serializable {
    private SpreadWS.javapackage.GetUserSentReportResponseGetUserSentReportResult getUserSentReportResult;

    public GetUserSentReportResponse() {
    }

    public GetUserSentReportResponse(
           SpreadWS.javapackage.GetUserSentReportResponseGetUserSentReportResult getUserSentReportResult) {
           this.getUserSentReportResult = getUserSentReportResult;
    }


    /**
     * Gets the getUserSentReportResult value for this GetUserSentReportResponse.
     * 
     * @return getUserSentReportResult
     */
    public SpreadWS.javapackage.GetUserSentReportResponseGetUserSentReportResult getGetUserSentReportResult() {
        return getUserSentReportResult;
    }


    /**
     * Sets the getUserSentReportResult value for this GetUserSentReportResponse.
     * 
     * @param getUserSentReportResult
     */
    public void setGetUserSentReportResult(SpreadWS.javapackage.GetUserSentReportResponseGetUserSentReportResult getUserSentReportResult) {
        this.getUserSentReportResult = getUserSentReportResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetUserSentReportResponse)) return false;
        GetUserSentReportResponse other = (GetUserSentReportResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getUserSentReportResult==null && other.getGetUserSentReportResult()==null) || 
             (this.getUserSentReportResult!=null &&
              this.getUserSentReportResult.equals(other.getGetUserSentReportResult())));
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
        if (getGetUserSentReportResult() != null) {
            _hashCode += getGetUserSentReportResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetUserSentReportResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">getUserSentReportResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getUserSentReportResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "getUserSentReportResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">>getUserSentReportResponse>getUserSentReportResult"));
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
