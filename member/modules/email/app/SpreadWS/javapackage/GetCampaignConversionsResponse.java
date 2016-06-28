/**
 * GetCampaignConversionsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class GetCampaignConversionsResponse  implements java.io.Serializable {
    private SpreadWS.javapackage.GetCampaignConversionsResponseGetCampaignConversionsResult getCampaignConversionsResult;

    public GetCampaignConversionsResponse() {
    }

    public GetCampaignConversionsResponse(
           SpreadWS.javapackage.GetCampaignConversionsResponseGetCampaignConversionsResult getCampaignConversionsResult) {
           this.getCampaignConversionsResult = getCampaignConversionsResult;
    }


    /**
     * Gets the getCampaignConversionsResult value for this GetCampaignConversionsResponse.
     * 
     * @return getCampaignConversionsResult
     */
    public SpreadWS.javapackage.GetCampaignConversionsResponseGetCampaignConversionsResult getGetCampaignConversionsResult() {
        return getCampaignConversionsResult;
    }


    /**
     * Sets the getCampaignConversionsResult value for this GetCampaignConversionsResponse.
     * 
     * @param getCampaignConversionsResult
     */
    public void setGetCampaignConversionsResult(SpreadWS.javapackage.GetCampaignConversionsResponseGetCampaignConversionsResult getCampaignConversionsResult) {
        this.getCampaignConversionsResult = getCampaignConversionsResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetCampaignConversionsResponse)) return false;
        GetCampaignConversionsResponse other = (GetCampaignConversionsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getCampaignConversionsResult==null && other.getGetCampaignConversionsResult()==null) || 
             (this.getCampaignConversionsResult!=null &&
              this.getCampaignConversionsResult.equals(other.getGetCampaignConversionsResult())));
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
        if (getGetCampaignConversionsResult() != null) {
            _hashCode += getGetCampaignConversionsResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetCampaignConversionsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">GetCampaignConversionsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getCampaignConversionsResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "GetCampaignConversionsResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">>GetCampaignConversionsResponse>GetCampaignConversionsResult"));
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
