/**
 * UploadContactListFileResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class UploadContactListFileResponse  implements java.io.Serializable {
    private java.lang.String uploadContactListFileResult;

    public UploadContactListFileResponse() {
    }

    public UploadContactListFileResponse(
           java.lang.String uploadContactListFileResult) {
           this.uploadContactListFileResult = uploadContactListFileResult;
    }


    /**
     * Gets the uploadContactListFileResult value for this UploadContactListFileResponse.
     * 
     * @return uploadContactListFileResult
     */
    public java.lang.String getUploadContactListFileResult() {
        return uploadContactListFileResult;
    }


    /**
     * Sets the uploadContactListFileResult value for this UploadContactListFileResponse.
     * 
     * @param uploadContactListFileResult
     */
    public void setUploadContactListFileResult(java.lang.String uploadContactListFileResult) {
        this.uploadContactListFileResult = uploadContactListFileResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UploadContactListFileResponse)) return false;
        UploadContactListFileResponse other = (UploadContactListFileResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.uploadContactListFileResult==null && other.getUploadContactListFileResult()==null) || 
             (this.uploadContactListFileResult!=null &&
              this.uploadContactListFileResult.equals(other.getUploadContactListFileResult())));
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
        if (getUploadContactListFileResult() != null) {
            _hashCode += getUploadContactListFileResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UploadContactListFileResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">UploadContactListFileResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uploadContactListFileResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "UploadContactListFileResult"));
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
