/**
 * GetCampaignReportResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class GetCampaignReportResponse  implements java.io.Serializable {
    private SpreadWS.javapackage.CampaignReport getCampaignReportResult;

    public GetCampaignReportResponse() {
    }

    public GetCampaignReportResponse(
           SpreadWS.javapackage.CampaignReport getCampaignReportResult) {
           this.getCampaignReportResult = getCampaignReportResult;
    }


    /**
     * Gets the getCampaignReportResult value for this GetCampaignReportResponse.
     * 
     * @return getCampaignReportResult
     */
    public SpreadWS.javapackage.CampaignReport getGetCampaignReportResult() {
        return getCampaignReportResult;
    }


    /**
     * Sets the getCampaignReportResult value for this GetCampaignReportResponse.
     * 
     * @param getCampaignReportResult
     */
    public void setGetCampaignReportResult(SpreadWS.javapackage.CampaignReport getCampaignReportResult) {
        this.getCampaignReportResult = getCampaignReportResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetCampaignReportResponse)) return false;
        GetCampaignReportResponse other = (GetCampaignReportResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getCampaignReportResult==null && other.getGetCampaignReportResult()==null) || 
             (this.getCampaignReportResult!=null &&
              this.getCampaignReportResult.equals(other.getGetCampaignReportResult())));
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
        if (getGetCampaignReportResult() != null) {
            _hashCode += getGetCampaignReportResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetCampaignReportResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">getCampaignReportResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getCampaignReportResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "getCampaignReportResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "CampaignReport"));
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
