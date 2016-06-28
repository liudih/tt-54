/**
 * GetCampaignConvertionsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package services.webservice.rspread;

public class GetCampaignConvertionsResponse  implements java.io.Serializable {
    private services.webservice.rspread.GetCampaignConvertionsResponseGetCampaignConvertionsResult getCampaignConvertionsResult;

    public GetCampaignConvertionsResponse() {
    }

    public GetCampaignConvertionsResponse(
           services.webservice.rspread.GetCampaignConvertionsResponseGetCampaignConvertionsResult getCampaignConvertionsResult) {
           this.getCampaignConvertionsResult = getCampaignConvertionsResult;
    }


    /**
     * Gets the getCampaignConvertionsResult value for this GetCampaignConvertionsResponse.
     * 
     * @return getCampaignConvertionsResult
     */
    public services.webservice.rspread.GetCampaignConvertionsResponseGetCampaignConvertionsResult getGetCampaignConvertionsResult() {
        return getCampaignConvertionsResult;
    }


    /**
     * Sets the getCampaignConvertionsResult value for this GetCampaignConvertionsResponse.
     * 
     * @param getCampaignConvertionsResult
     */
    public void setGetCampaignConvertionsResult(services.webservice.rspread.GetCampaignConvertionsResponseGetCampaignConvertionsResult getCampaignConvertionsResult) {
        this.getCampaignConvertionsResult = getCampaignConvertionsResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetCampaignConvertionsResponse)) return false;
        GetCampaignConvertionsResponse other = (GetCampaignConvertionsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getCampaignConvertionsResult==null && other.getGetCampaignConvertionsResult()==null) || 
             (this.getCampaignConvertionsResult!=null &&
              this.getCampaignConvertionsResult.equals(other.getGetCampaignConvertionsResult())));
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
        if (getGetCampaignConvertionsResult() != null) {
            _hashCode += getGetCampaignConvertionsResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetCampaignConvertionsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">GetCampaignConvertionsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getCampaignConvertionsResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "GetCampaignConvertionsResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">>GetCampaignConvertionsResponse>GetCampaignConvertionsResult"));
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
