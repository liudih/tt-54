/**
 * CampaignCreatives.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package services.webservice.rspread;

public class CampaignCreatives  implements java.io.Serializable {
    private java.lang.String subject;

    private java.lang.String displayName;

    private java.lang.String fromAddress;

    private java.lang.String replyTo;

    private java.lang.String creativeContent;

    private java.lang.String target;

    private boolean isCampaignDefault;

    public CampaignCreatives() {
    }

    public CampaignCreatives(
           java.lang.String subject,
           java.lang.String displayName,
           java.lang.String fromAddress,
           java.lang.String replyTo,
           java.lang.String creativeContent,
           java.lang.String target,
           boolean isCampaignDefault) {
           this.subject = subject;
           this.displayName = displayName;
           this.fromAddress = fromAddress;
           this.replyTo = replyTo;
           this.creativeContent = creativeContent;
           this.target = target;
           this.isCampaignDefault = isCampaignDefault;
    }


    /**
     * Gets the subject value for this CampaignCreatives.
     * 
     * @return subject
     */
    public java.lang.String getSubject() {
        return subject;
    }


    /**
     * Sets the subject value for this CampaignCreatives.
     * 
     * @param subject
     */
    public void setSubject(java.lang.String subject) {
        this.subject = subject;
    }


    /**
     * Gets the displayName value for this CampaignCreatives.
     * 
     * @return displayName
     */
    public java.lang.String getDisplayName() {
        return displayName;
    }


    /**
     * Sets the displayName value for this CampaignCreatives.
     * 
     * @param displayName
     */
    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }


    /**
     * Gets the fromAddress value for this CampaignCreatives.
     * 
     * @return fromAddress
     */
    public java.lang.String getFromAddress() {
        return fromAddress;
    }


    /**
     * Sets the fromAddress value for this CampaignCreatives.
     * 
     * @param fromAddress
     */
    public void setFromAddress(java.lang.String fromAddress) {
        this.fromAddress = fromAddress;
    }


    /**
     * Gets the replyTo value for this CampaignCreatives.
     * 
     * @return replyTo
     */
    public java.lang.String getReplyTo() {
        return replyTo;
    }


    /**
     * Sets the replyTo value for this CampaignCreatives.
     * 
     * @param replyTo
     */
    public void setReplyTo(java.lang.String replyTo) {
        this.replyTo = replyTo;
    }


    /**
     * Gets the creativeContent value for this CampaignCreatives.
     * 
     * @return creativeContent
     */
    public java.lang.String getCreativeContent() {
        return creativeContent;
    }


    /**
     * Sets the creativeContent value for this CampaignCreatives.
     * 
     * @param creativeContent
     */
    public void setCreativeContent(java.lang.String creativeContent) {
        this.creativeContent = creativeContent;
    }


    /**
     * Gets the target value for this CampaignCreatives.
     * 
     * @return target
     */
    public java.lang.String getTarget() {
        return target;
    }


    /**
     * Sets the target value for this CampaignCreatives.
     * 
     * @param target
     */
    public void setTarget(java.lang.String target) {
        this.target = target;
    }


    /**
     * Gets the isCampaignDefault value for this CampaignCreatives.
     * 
     * @return isCampaignDefault
     */
    public boolean isIsCampaignDefault() {
        return isCampaignDefault;
    }


    /**
     * Sets the isCampaignDefault value for this CampaignCreatives.
     * 
     * @param isCampaignDefault
     */
    public void setIsCampaignDefault(boolean isCampaignDefault) {
        this.isCampaignDefault = isCampaignDefault;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CampaignCreatives)) return false;
        CampaignCreatives other = (CampaignCreatives) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.subject==null && other.getSubject()==null) || 
             (this.subject!=null &&
              this.subject.equals(other.getSubject()))) &&
            ((this.displayName==null && other.getDisplayName()==null) || 
             (this.displayName!=null &&
              this.displayName.equals(other.getDisplayName()))) &&
            ((this.fromAddress==null && other.getFromAddress()==null) || 
             (this.fromAddress!=null &&
              this.fromAddress.equals(other.getFromAddress()))) &&
            ((this.replyTo==null && other.getReplyTo()==null) || 
             (this.replyTo!=null &&
              this.replyTo.equals(other.getReplyTo()))) &&
            ((this.creativeContent==null && other.getCreativeContent()==null) || 
             (this.creativeContent!=null &&
              this.creativeContent.equals(other.getCreativeContent()))) &&
            ((this.target==null && other.getTarget()==null) || 
             (this.target!=null &&
              this.target.equals(other.getTarget()))) &&
            this.isCampaignDefault == other.isIsCampaignDefault();
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
        if (getSubject() != null) {
            _hashCode += getSubject().hashCode();
        }
        if (getDisplayName() != null) {
            _hashCode += getDisplayName().hashCode();
        }
        if (getFromAddress() != null) {
            _hashCode += getFromAddress().hashCode();
        }
        if (getReplyTo() != null) {
            _hashCode += getReplyTo().hashCode();
        }
        if (getCreativeContent() != null) {
            _hashCode += getCreativeContent().hashCode();
        }
        if (getTarget() != null) {
            _hashCode += getTarget().hashCode();
        }
        _hashCode += (isIsCampaignDefault() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CampaignCreatives.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "CampaignCreatives"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subject");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "subject"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("displayName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "displayName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fromAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "fromAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("replyTo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "replyTo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creativeContent");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "creativeContent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("target");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "target"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isCampaignDefault");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "isCampaignDefault"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
