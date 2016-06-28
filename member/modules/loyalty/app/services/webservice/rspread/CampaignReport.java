/**
 * CampaignReport.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package services.webservice.rspread;

public class CampaignReport  implements java.io.Serializable {
    private int campaignId;

    private java.lang.String campaignName;

    private int sentCount;

    private int failCount;

    private int openCount;

    private int allClickCount;

    private int uniqueClicksCount;

    private int conversionCount;

    private int bounceCount;

    private int spamCount;

    private int unsubscribeCount;

    private services.webservice.rspread.CampaignStatus status;

    private java.util.Calendar dateSent;

    public CampaignReport() {
    }

    public CampaignReport(
           int campaignId,
           java.lang.String campaignName,
           int sentCount,
           int failCount,
           int openCount,
           int allClickCount,
           int uniqueClicksCount,
           int conversionCount,
           int bounceCount,
           int spamCount,
           int unsubscribeCount,
           services.webservice.rspread.CampaignStatus status,
           java.util.Calendar dateSent) {
           this.campaignId = campaignId;
           this.campaignName = campaignName;
           this.sentCount = sentCount;
           this.failCount = failCount;
           this.openCount = openCount;
           this.allClickCount = allClickCount;
           this.uniqueClicksCount = uniqueClicksCount;
           this.conversionCount = conversionCount;
           this.bounceCount = bounceCount;
           this.spamCount = spamCount;
           this.unsubscribeCount = unsubscribeCount;
           this.status = status;
           this.dateSent = dateSent;
    }


    /**
     * Gets the campaignId value for this CampaignReport.
     * 
     * @return campaignId
     */
    public int getCampaignId() {
        return campaignId;
    }


    /**
     * Sets the campaignId value for this CampaignReport.
     * 
     * @param campaignId
     */
    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }


    /**
     * Gets the campaignName value for this CampaignReport.
     * 
     * @return campaignName
     */
    public java.lang.String getCampaignName() {
        return campaignName;
    }


    /**
     * Sets the campaignName value for this CampaignReport.
     * 
     * @param campaignName
     */
    public void setCampaignName(java.lang.String campaignName) {
        this.campaignName = campaignName;
    }


    /**
     * Gets the sentCount value for this CampaignReport.
     * 
     * @return sentCount
     */
    public int getSentCount() {
        return sentCount;
    }


    /**
     * Sets the sentCount value for this CampaignReport.
     * 
     * @param sentCount
     */
    public void setSentCount(int sentCount) {
        this.sentCount = sentCount;
    }


    /**
     * Gets the failCount value for this CampaignReport.
     * 
     * @return failCount
     */
    public int getFailCount() {
        return failCount;
    }


    /**
     * Sets the failCount value for this CampaignReport.
     * 
     * @param failCount
     */
    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }


    /**
     * Gets the openCount value for this CampaignReport.
     * 
     * @return openCount
     */
    public int getOpenCount() {
        return openCount;
    }


    /**
     * Sets the openCount value for this CampaignReport.
     * 
     * @param openCount
     */
    public void setOpenCount(int openCount) {
        this.openCount = openCount;
    }


    /**
     * Gets the allClickCount value for this CampaignReport.
     * 
     * @return allClickCount
     */
    public int getAllClickCount() {
        return allClickCount;
    }


    /**
     * Sets the allClickCount value for this CampaignReport.
     * 
     * @param allClickCount
     */
    public void setAllClickCount(int allClickCount) {
        this.allClickCount = allClickCount;
    }


    /**
     * Gets the uniqueClicksCount value for this CampaignReport.
     * 
     * @return uniqueClicksCount
     */
    public int getUniqueClicksCount() {
        return uniqueClicksCount;
    }


    /**
     * Sets the uniqueClicksCount value for this CampaignReport.
     * 
     * @param uniqueClicksCount
     */
    public void setUniqueClicksCount(int uniqueClicksCount) {
        this.uniqueClicksCount = uniqueClicksCount;
    }


    /**
     * Gets the conversionCount value for this CampaignReport.
     * 
     * @return conversionCount
     */
    public int getConversionCount() {
        return conversionCount;
    }


    /**
     * Sets the conversionCount value for this CampaignReport.
     * 
     * @param conversionCount
     */
    public void setConversionCount(int conversionCount) {
        this.conversionCount = conversionCount;
    }


    /**
     * Gets the bounceCount value for this CampaignReport.
     * 
     * @return bounceCount
     */
    public int getBounceCount() {
        return bounceCount;
    }


    /**
     * Sets the bounceCount value for this CampaignReport.
     * 
     * @param bounceCount
     */
    public void setBounceCount(int bounceCount) {
        this.bounceCount = bounceCount;
    }


    /**
     * Gets the spamCount value for this CampaignReport.
     * 
     * @return spamCount
     */
    public int getSpamCount() {
        return spamCount;
    }


    /**
     * Sets the spamCount value for this CampaignReport.
     * 
     * @param spamCount
     */
    public void setSpamCount(int spamCount) {
        this.spamCount = spamCount;
    }


    /**
     * Gets the unsubscribeCount value for this CampaignReport.
     * 
     * @return unsubscribeCount
     */
    public int getUnsubscribeCount() {
        return unsubscribeCount;
    }


    /**
     * Sets the unsubscribeCount value for this CampaignReport.
     * 
     * @param unsubscribeCount
     */
    public void setUnsubscribeCount(int unsubscribeCount) {
        this.unsubscribeCount = unsubscribeCount;
    }


    /**
     * Gets the status value for this CampaignReport.
     * 
     * @return status
     */
    public services.webservice.rspread.CampaignStatus getStatus() {
        return status;
    }


    /**
     * Sets the status value for this CampaignReport.
     * 
     * @param status
     */
    public void setStatus(services.webservice.rspread.CampaignStatus status) {
        this.status = status;
    }


    /**
     * Gets the dateSent value for this CampaignReport.
     * 
     * @return dateSent
     */
    public java.util.Calendar getDateSent() {
        return dateSent;
    }


    /**
     * Sets the dateSent value for this CampaignReport.
     * 
     * @param dateSent
     */
    public void setDateSent(java.util.Calendar dateSent) {
        this.dateSent = dateSent;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CampaignReport)) return false;
        CampaignReport other = (CampaignReport) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.campaignId == other.getCampaignId() &&
            ((this.campaignName==null && other.getCampaignName()==null) || 
             (this.campaignName!=null &&
              this.campaignName.equals(other.getCampaignName()))) &&
            this.sentCount == other.getSentCount() &&
            this.failCount == other.getFailCount() &&
            this.openCount == other.getOpenCount() &&
            this.allClickCount == other.getAllClickCount() &&
            this.uniqueClicksCount == other.getUniqueClicksCount() &&
            this.conversionCount == other.getConversionCount() &&
            this.bounceCount == other.getBounceCount() &&
            this.spamCount == other.getSpamCount() &&
            this.unsubscribeCount == other.getUnsubscribeCount() &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.dateSent==null && other.getDateSent()==null) || 
             (this.dateSent!=null &&
              this.dateSent.equals(other.getDateSent())));
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
        _hashCode += getCampaignId();
        if (getCampaignName() != null) {
            _hashCode += getCampaignName().hashCode();
        }
        _hashCode += getSentCount();
        _hashCode += getFailCount();
        _hashCode += getOpenCount();
        _hashCode += getAllClickCount();
        _hashCode += getUniqueClicksCount();
        _hashCode += getConversionCount();
        _hashCode += getBounceCount();
        _hashCode += getSpamCount();
        _hashCode += getUnsubscribeCount();
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getDateSent() != null) {
            _hashCode += getDateSent().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CampaignReport.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "CampaignReport"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campaignId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "CampaignId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campaignName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "CampaignName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sentCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "SentCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("failCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "FailCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("openCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "OpenCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("allClickCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "AllClickCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uniqueClicksCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "UniqueClicksCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("conversionCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "ConversionCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bounceCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "BounceCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spamCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "SpamCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unsubscribeCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "UnsubscribeCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "CampaignStatus"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateSent");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "DateSent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
