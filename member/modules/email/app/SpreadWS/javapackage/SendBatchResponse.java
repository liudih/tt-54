/**
 * SendBatchResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class SendBatchResponse  implements java.io.Serializable {
    private SpreadWS.javapackage.Server sendBatchResult;

    public SendBatchResponse() {
    }

    public SendBatchResponse(
           SpreadWS.javapackage.Server sendBatchResult) {
           this.sendBatchResult = sendBatchResult;
    }


    /**
     * Gets the sendBatchResult value for this SendBatchResponse.
     * 
     * @return sendBatchResult
     */
    public SpreadWS.javapackage.Server getSendBatchResult() {
        return sendBatchResult;
    }


    /**
     * Sets the sendBatchResult value for this SendBatchResponse.
     * 
     * @param sendBatchResult
     */
    public void setSendBatchResult(SpreadWS.javapackage.Server sendBatchResult) {
        this.sendBatchResult = sendBatchResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SendBatchResponse)) return false;
        SendBatchResponse other = (SendBatchResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sendBatchResult==null && other.getSendBatchResult()==null) || 
             (this.sendBatchResult!=null &&
              this.sendBatchResult.equals(other.getSendBatchResult())));
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
        if (getSendBatchResult() != null) {
            _hashCode += getSendBatchResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SendBatchResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">SendBatchResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sendBatchResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "SendBatchResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "server"));
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
