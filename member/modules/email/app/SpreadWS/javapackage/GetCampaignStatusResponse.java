/**
 * GetCampaignStatusResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class GetCampaignStatusResponse  implements java.io.Serializable {
    private SpreadWS.javapackage.CampaignStatus getCampaignStatusResult;

    public GetCampaignStatusResponse() {
    }

    public GetCampaignStatusResponse(
           SpreadWS.javapackage.CampaignStatus getCampaignStatusResult) {
           this.getCampaignStatusResult = getCampaignStatusResult;
    }


    /**
     * Gets the getCampaignStatusResult value for this GetCampaignStatusResponse.
     * 
     * @return getCampaignStatusResult
     */
    public SpreadWS.javapackage.CampaignStatus getGetCampaignStatusResult() {
        return getCampaignStatusResult;
    }


    /**
     * Sets the getCampaignStatusResult value for this GetCampaignStatusResponse.
     * 
     * @param getCampaignStatusResult
     */
    public void setGetCampaignStatusResult(SpreadWS.javapackage.CampaignStatus getCampaignStatusResult) {
        this.getCampaignStatusResult = getCampaignStatusResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetCampaignStatusResponse)) return false;
        GetCampaignStatusResponse other = (GetCampaignStatusResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getCampaignStatusResult==null && other.getGetCampaignStatusResult()==null) || 
             (this.getCampaignStatusResult!=null &&
              this.getCampaignStatusResult.equals(other.getGetCampaignStatusResult())));
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
        if (getGetCampaignStatusResult() != null) {
            _hashCode += getGetCampaignStatusResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetCampaignStatusResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">getCampaignStatusResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getCampaignStatusResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "getCampaignStatusResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "CampaignStatus"));
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
