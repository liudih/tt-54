/**
 * ActivationCampaignByFtpFileResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class ActivationCampaignByFtpFileResponse  implements java.io.Serializable {
    private java.lang.String activationCampaignByFtpFileResult;

    public ActivationCampaignByFtpFileResponse() {
    }

    public ActivationCampaignByFtpFileResponse(
           java.lang.String activationCampaignByFtpFileResult) {
           this.activationCampaignByFtpFileResult = activationCampaignByFtpFileResult;
    }


    /**
     * Gets the activationCampaignByFtpFileResult value for this ActivationCampaignByFtpFileResponse.
     * 
     * @return activationCampaignByFtpFileResult
     */
    public java.lang.String getActivationCampaignByFtpFileResult() {
        return activationCampaignByFtpFileResult;
    }


    /**
     * Sets the activationCampaignByFtpFileResult value for this ActivationCampaignByFtpFileResponse.
     * 
     * @param activationCampaignByFtpFileResult
     */
    public void setActivationCampaignByFtpFileResult(java.lang.String activationCampaignByFtpFileResult) {
        this.activationCampaignByFtpFileResult = activationCampaignByFtpFileResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ActivationCampaignByFtpFileResponse)) return false;
        ActivationCampaignByFtpFileResponse other = (ActivationCampaignByFtpFileResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.activationCampaignByFtpFileResult==null && other.getActivationCampaignByFtpFileResult()==null) || 
             (this.activationCampaignByFtpFileResult!=null &&
              this.activationCampaignByFtpFileResult.equals(other.getActivationCampaignByFtpFileResult())));
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
        if (getActivationCampaignByFtpFileResult() != null) {
            _hashCode += getActivationCampaignByFtpFileResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ActivationCampaignByFtpFileResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">ActivationCampaignByFtpFileResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activationCampaignByFtpFileResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "ActivationCampaignByFtpFileResult"));
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
