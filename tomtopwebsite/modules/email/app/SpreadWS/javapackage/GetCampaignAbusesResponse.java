/**
 * GetCampaignAbusesResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class GetCampaignAbusesResponse  implements java.io.Serializable {
    private SpreadWS.javapackage.GetCampaignAbusesResponseGetCampaignAbusesResult getCampaignAbusesResult;

    public GetCampaignAbusesResponse() {
    }

    public GetCampaignAbusesResponse(
           SpreadWS.javapackage.GetCampaignAbusesResponseGetCampaignAbusesResult getCampaignAbusesResult) {
           this.getCampaignAbusesResult = getCampaignAbusesResult;
    }


    /**
     * Gets the getCampaignAbusesResult value for this GetCampaignAbusesResponse.
     * 
     * @return getCampaignAbusesResult
     */
    public SpreadWS.javapackage.GetCampaignAbusesResponseGetCampaignAbusesResult getGetCampaignAbusesResult() {
        return getCampaignAbusesResult;
    }


    /**
     * Sets the getCampaignAbusesResult value for this GetCampaignAbusesResponse.
     * 
     * @param getCampaignAbusesResult
     */
    public void setGetCampaignAbusesResult(SpreadWS.javapackage.GetCampaignAbusesResponseGetCampaignAbusesResult getCampaignAbusesResult) {
        this.getCampaignAbusesResult = getCampaignAbusesResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetCampaignAbusesResponse)) return false;
        GetCampaignAbusesResponse other = (GetCampaignAbusesResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getCampaignAbusesResult==null && other.getGetCampaignAbusesResult()==null) || 
             (this.getCampaignAbusesResult!=null &&
              this.getCampaignAbusesResult.equals(other.getGetCampaignAbusesResult())));
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
        if (getGetCampaignAbusesResult() != null) {
            _hashCode += getGetCampaignAbusesResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetCampaignAbusesResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">GetCampaignAbusesResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getCampaignAbusesResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "GetCampaignAbusesResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">>GetCampaignAbusesResponse>GetCampaignAbusesResult"));
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
