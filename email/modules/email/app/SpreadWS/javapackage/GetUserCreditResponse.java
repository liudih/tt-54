/**
 * GetUserCreditResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class GetUserCreditResponse  implements java.io.Serializable {
    private SpreadWS.javapackage.GetUserCreditResponseGetUserCreditResult getUserCreditResult;

    public GetUserCreditResponse() {
    }

    public GetUserCreditResponse(
           SpreadWS.javapackage.GetUserCreditResponseGetUserCreditResult getUserCreditResult) {
           this.getUserCreditResult = getUserCreditResult;
    }


    /**
     * Gets the getUserCreditResult value for this GetUserCreditResponse.
     * 
     * @return getUserCreditResult
     */
    public SpreadWS.javapackage.GetUserCreditResponseGetUserCreditResult getGetUserCreditResult() {
        return getUserCreditResult;
    }


    /**
     * Sets the getUserCreditResult value for this GetUserCreditResponse.
     * 
     * @param getUserCreditResult
     */
    public void setGetUserCreditResult(SpreadWS.javapackage.GetUserCreditResponseGetUserCreditResult getUserCreditResult) {
        this.getUserCreditResult = getUserCreditResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetUserCreditResponse)) return false;
        GetUserCreditResponse other = (GetUserCreditResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getUserCreditResult==null && other.getGetUserCreditResult()==null) || 
             (this.getUserCreditResult!=null &&
              this.getUserCreditResult.equals(other.getGetUserCreditResult())));
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
        if (getGetUserCreditResult() != null) {
            _hashCode += getGetUserCreditResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetUserCreditResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">GetUserCreditResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getUserCreditResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "GetUserCreditResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">>GetUserCreditResponse>GetUserCreditResult"));
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
