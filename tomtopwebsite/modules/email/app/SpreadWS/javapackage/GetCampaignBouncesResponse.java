/**
 * GetCampaignBouncesResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class GetCampaignBouncesResponse  implements java.io.Serializable {
    private SpreadWS.javapackage.GetCampaignBouncesResponseGetCampaignBouncesResult getCampaignBouncesResult;

    public GetCampaignBouncesResponse() {
    }

    public GetCampaignBouncesResponse(
           SpreadWS.javapackage.GetCampaignBouncesResponseGetCampaignBouncesResult getCampaignBouncesResult) {
           this.getCampaignBouncesResult = getCampaignBouncesResult;
    }


    /**
     * Gets the getCampaignBouncesResult value for this GetCampaignBouncesResponse.
     * 
     * @return getCampaignBouncesResult
     */
    public SpreadWS.javapackage.GetCampaignBouncesResponseGetCampaignBouncesResult getGetCampaignBouncesResult() {
        return getCampaignBouncesResult;
    }


    /**
     * Sets the getCampaignBouncesResult value for this GetCampaignBouncesResponse.
     * 
     * @param getCampaignBouncesResult
     */
    public void setGetCampaignBouncesResult(SpreadWS.javapackage.GetCampaignBouncesResponseGetCampaignBouncesResult getCampaignBouncesResult) {
        this.getCampaignBouncesResult = getCampaignBouncesResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetCampaignBouncesResponse)) return false;
        GetCampaignBouncesResponse other = (GetCampaignBouncesResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getCampaignBouncesResult==null && other.getGetCampaignBouncesResult()==null) || 
             (this.getCampaignBouncesResult!=null &&
              this.getCampaignBouncesResult.equals(other.getGetCampaignBouncesResult())));
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
        if (getGetCampaignBouncesResult() != null) {
            _hashCode += getGetCampaignBouncesResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetCampaignBouncesResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">GetCampaignBouncesResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getCampaignBouncesResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "GetCampaignBouncesResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">>GetCampaignBouncesResponse>GetCampaignBouncesResult"));
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
