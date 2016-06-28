/**
 * AddSubscribersByInfoResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class AddSubscribersByInfoResponse  implements java.io.Serializable {
    private SpreadWS.javapackage.AddSubscribersByInfoResponseAddSubscribersByInfoResult addSubscribersByInfoResult;

    public AddSubscribersByInfoResponse() {
    }

    public AddSubscribersByInfoResponse(
           SpreadWS.javapackage.AddSubscribersByInfoResponseAddSubscribersByInfoResult addSubscribersByInfoResult) {
           this.addSubscribersByInfoResult = addSubscribersByInfoResult;
    }


    /**
     * Gets the addSubscribersByInfoResult value for this AddSubscribersByInfoResponse.
     * 
     * @return addSubscribersByInfoResult
     */
    public SpreadWS.javapackage.AddSubscribersByInfoResponseAddSubscribersByInfoResult getAddSubscribersByInfoResult() {
        return addSubscribersByInfoResult;
    }


    /**
     * Sets the addSubscribersByInfoResult value for this AddSubscribersByInfoResponse.
     * 
     * @param addSubscribersByInfoResult
     */
    public void setAddSubscribersByInfoResult(SpreadWS.javapackage.AddSubscribersByInfoResponseAddSubscribersByInfoResult addSubscribersByInfoResult) {
        this.addSubscribersByInfoResult = addSubscribersByInfoResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AddSubscribersByInfoResponse)) return false;
        AddSubscribersByInfoResponse other = (AddSubscribersByInfoResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.addSubscribersByInfoResult==null && other.getAddSubscribersByInfoResult()==null) || 
             (this.addSubscribersByInfoResult!=null &&
              this.addSubscribersByInfoResult.equals(other.getAddSubscribersByInfoResult())));
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
        if (getAddSubscribersByInfoResult() != null) {
            _hashCode += getAddSubscribersByInfoResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AddSubscribersByInfoResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">addSubscribersByInfoResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addSubscribersByInfoResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "addSubscribersByInfoResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">>addSubscribersByInfoResponse>addSubscribersByInfoResult"));
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
