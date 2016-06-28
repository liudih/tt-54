/**
 * GetCampaignUnsubscribesResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class GetCampaignUnsubscribesResponse  implements java.io.Serializable {
    private SpreadWS.javapackage.GetCampaignUnsubscribesResponseGetCampaignUnsubscribesResult getCampaignUnsubscribesResult;

    public GetCampaignUnsubscribesResponse() {
    }

    public GetCampaignUnsubscribesResponse(
           SpreadWS.javapackage.GetCampaignUnsubscribesResponseGetCampaignUnsubscribesResult getCampaignUnsubscribesResult) {
           this.getCampaignUnsubscribesResult = getCampaignUnsubscribesResult;
    }


    /**
     * Gets the getCampaignUnsubscribesResult value for this GetCampaignUnsubscribesResponse.
     * 
     * @return getCampaignUnsubscribesResult
     */
    public SpreadWS.javapackage.GetCampaignUnsubscribesResponseGetCampaignUnsubscribesResult getGetCampaignUnsubscribesResult() {
        return getCampaignUnsubscribesResult;
    }


    /**
     * Sets the getCampaignUnsubscribesResult value for this GetCampaignUnsubscribesResponse.
     * 
     * @param getCampaignUnsubscribesResult
     */
    public void setGetCampaignUnsubscribesResult(SpreadWS.javapackage.GetCampaignUnsubscribesResponseGetCampaignUnsubscribesResult getCampaignUnsubscribesResult) {
        this.getCampaignUnsubscribesResult = getCampaignUnsubscribesResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetCampaignUnsubscribesResponse)) return false;
        GetCampaignUnsubscribesResponse other = (GetCampaignUnsubscribesResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getCampaignUnsubscribesResult==null && other.getGetCampaignUnsubscribesResult()==null) || 
             (this.getCampaignUnsubscribesResult!=null &&
              this.getCampaignUnsubscribesResult.equals(other.getGetCampaignUnsubscribesResult())));
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
        if (getGetCampaignUnsubscribesResult() != null) {
            _hashCode += getGetCampaignUnsubscribesResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetCampaignUnsubscribesResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">GetCampaignUnsubscribesResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getCampaignUnsubscribesResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "GetCampaignUnsubscribesResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">>GetCampaignUnsubscribesResponse>GetCampaignUnsubscribesResult"));
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
