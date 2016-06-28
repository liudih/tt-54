/**
 * CreateCampaign2Serialize.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class CreateCampaign2Serialize  implements java.io.Serializable {
    private java.lang.String loginEmail;

    private java.lang.String password;

    private java.lang.String campaignName;

    private java.lang.String strcampaignCreatives;

    private java.lang.String[] category;

    private int interval;

    private java.util.Calendar schedule;

    private java.lang.String signature;

    private SpreadWS.javapackage.CampaignStatus campaignStatus;

    public CreateCampaign2Serialize() {
    }

    public CreateCampaign2Serialize(
           java.lang.String loginEmail,
           java.lang.String password,
           java.lang.String campaignName,
           java.lang.String strcampaignCreatives,
           java.lang.String[] category,
           int interval,
           java.util.Calendar schedule,
           java.lang.String signature,
           SpreadWS.javapackage.CampaignStatus campaignStatus) {
           this.loginEmail = loginEmail;
           this.password = password;
           this.campaignName = campaignName;
           this.strcampaignCreatives = strcampaignCreatives;
           this.category = category;
           this.interval = interval;
           this.schedule = schedule;
           this.signature = signature;
           this.campaignStatus = campaignStatus;
    }


    /**
     * Gets the loginEmail value for this CreateCampaign2Serialize.
     * 
     * @return loginEmail
     */
    public java.lang.String getLoginEmail() {
        return loginEmail;
    }


    /**
     * Sets the loginEmail value for this CreateCampaign2Serialize.
     * 
     * @param loginEmail
     */
    public void setLoginEmail(java.lang.String loginEmail) {
        this.loginEmail = loginEmail;
    }


    /**
     * Gets the password value for this CreateCampaign2Serialize.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this CreateCampaign2Serialize.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }


    /**
     * Gets the campaignName value for this CreateCampaign2Serialize.
     * 
     * @return campaignName
     */
    public java.lang.String getCampaignName() {
        return campaignName;
    }


    /**
     * Sets the campaignName value for this CreateCampaign2Serialize.
     * 
     * @param campaignName
     */
    public void setCampaignName(java.lang.String campaignName) {
        this.campaignName = campaignName;
    }


    /**
     * Gets the strcampaignCreatives value for this CreateCampaign2Serialize.
     * 
     * @return strcampaignCreatives
     */
    public java.lang.String getStrcampaignCreatives() {
        return strcampaignCreatives;
    }


    /**
     * Sets the strcampaignCreatives value for this CreateCampaign2Serialize.
     * 
     * @param strcampaignCreatives
     */
    public void setStrcampaignCreatives(java.lang.String strcampaignCreatives) {
        this.strcampaignCreatives = strcampaignCreatives;
    }


    /**
     * Gets the category value for this CreateCampaign2Serialize.
     * 
     * @return category
     */
    public java.lang.String[] getCategory() {
        return category;
    }


    /**
     * Sets the category value for this CreateCampaign2Serialize.
     * 
     * @param category
     */
    public void setCategory(java.lang.String[] category) {
        this.category = category;
    }


    /**
     * Gets the interval value for this CreateCampaign2Serialize.
     * 
     * @return interval
     */
    public int getInterval() {
        return interval;
    }


    /**
     * Sets the interval value for this CreateCampaign2Serialize.
     * 
     * @param interval
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }


    /**
     * Gets the schedule value for this CreateCampaign2Serialize.
     * 
     * @return schedule
     */
    public java.util.Calendar getSchedule() {
        return schedule;
    }


    /**
     * Sets the schedule value for this CreateCampaign2Serialize.
     * 
     * @param schedule
     */
    public void setSchedule(java.util.Calendar schedule) {
        this.schedule = schedule;
    }


    /**
     * Gets the signature value for this CreateCampaign2Serialize.
     * 
     * @return signature
     */
    public java.lang.String getSignature() {
        return signature;
    }


    /**
     * Sets the signature value for this CreateCampaign2Serialize.
     * 
     * @param signature
     */
    public void setSignature(java.lang.String signature) {
        this.signature = signature;
    }


    /**
     * Gets the campaignStatus value for this CreateCampaign2Serialize.
     * 
     * @return campaignStatus
     */
    public SpreadWS.javapackage.CampaignStatus getCampaignStatus() {
        return campaignStatus;
    }


    /**
     * Sets the campaignStatus value for this CreateCampaign2Serialize.
     * 
     * @param campaignStatus
     */
    public void setCampaignStatus(SpreadWS.javapackage.CampaignStatus campaignStatus) {
        this.campaignStatus = campaignStatus;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateCampaign2Serialize)) return false;
        CreateCampaign2Serialize other = (CreateCampaign2Serialize) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.loginEmail==null && other.getLoginEmail()==null) || 
             (this.loginEmail!=null &&
              this.loginEmail.equals(other.getLoginEmail()))) &&
            ((this.password==null && other.getPassword()==null) || 
             (this.password!=null &&
              this.password.equals(other.getPassword()))) &&
            ((this.campaignName==null && other.getCampaignName()==null) || 
             (this.campaignName!=null &&
              this.campaignName.equals(other.getCampaignName()))) &&
            ((this.strcampaignCreatives==null && other.getStrcampaignCreatives()==null) || 
             (this.strcampaignCreatives!=null &&
              this.strcampaignCreatives.equals(other.getStrcampaignCreatives()))) &&
            ((this.category==null && other.getCategory()==null) || 
             (this.category!=null &&
              java.util.Arrays.equals(this.category, other.getCategory()))) &&
            this.interval == other.getInterval() &&
            ((this.schedule==null && other.getSchedule()==null) || 
             (this.schedule!=null &&
              this.schedule.equals(other.getSchedule()))) &&
            ((this.signature==null && other.getSignature()==null) || 
             (this.signature!=null &&
              this.signature.equals(other.getSignature()))) &&
            ((this.campaignStatus==null && other.getCampaignStatus()==null) || 
             (this.campaignStatus!=null &&
              this.campaignStatus.equals(other.getCampaignStatus())));
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
        if (getLoginEmail() != null) {
            _hashCode += getLoginEmail().hashCode();
        }
        if (getPassword() != null) {
            _hashCode += getPassword().hashCode();
        }
        if (getCampaignName() != null) {
            _hashCode += getCampaignName().hashCode();
        }
        if (getStrcampaignCreatives() != null) {
            _hashCode += getStrcampaignCreatives().hashCode();
        }
        if (getCategory() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCategory());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCategory(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getInterval();
        if (getSchedule() != null) {
            _hashCode += getSchedule().hashCode();
        }
        if (getSignature() != null) {
            _hashCode += getSignature().hashCode();
        }
        if (getCampaignStatus() != null) {
            _hashCode += getCampaignStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateCampaign2Serialize.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">createCampaign2Serialize"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loginEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "loginEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("password");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campaignName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "campaignName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("strcampaignCreatives");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "strcampaignCreatives"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("category");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "category"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("interval");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "interval"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("schedule");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "schedule"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("signature");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "signature"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campaignStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "campaignStatus"));
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
