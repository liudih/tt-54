/**
 * ActivationCampaignByFtpFile.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class ActivationCampaignByFtpFile  implements java.io.Serializable {
    private java.lang.String loginEmail;

    private java.lang.String loginPassword;

    private java.lang.String fileType;

    private java.lang.String contactListName;

    private int campaignId;

    private java.lang.String ftpIp;

    private java.lang.String ftpUser;

    private java.lang.String ftpPassword;

    private java.lang.String ftpFilePath;

    public ActivationCampaignByFtpFile() {
    }

    public ActivationCampaignByFtpFile(
           java.lang.String loginEmail,
           java.lang.String loginPassword,
           java.lang.String fileType,
           java.lang.String contactListName,
           int campaignId,
           java.lang.String ftpIp,
           java.lang.String ftpUser,
           java.lang.String ftpPassword,
           java.lang.String ftpFilePath) {
           this.loginEmail = loginEmail;
           this.loginPassword = loginPassword;
           this.fileType = fileType;
           this.contactListName = contactListName;
           this.campaignId = campaignId;
           this.ftpIp = ftpIp;
           this.ftpUser = ftpUser;
           this.ftpPassword = ftpPassword;
           this.ftpFilePath = ftpFilePath;
    }


    /**
     * Gets the loginEmail value for this ActivationCampaignByFtpFile.
     * 
     * @return loginEmail
     */
    public java.lang.String getLoginEmail() {
        return loginEmail;
    }


    /**
     * Sets the loginEmail value for this ActivationCampaignByFtpFile.
     * 
     * @param loginEmail
     */
    public void setLoginEmail(java.lang.String loginEmail) {
        this.loginEmail = loginEmail;
    }


    /**
     * Gets the loginPassword value for this ActivationCampaignByFtpFile.
     * 
     * @return loginPassword
     */
    public java.lang.String getLoginPassword() {
        return loginPassword;
    }


    /**
     * Sets the loginPassword value for this ActivationCampaignByFtpFile.
     * 
     * @param loginPassword
     */
    public void setLoginPassword(java.lang.String loginPassword) {
        this.loginPassword = loginPassword;
    }


    /**
     * Gets the fileType value for this ActivationCampaignByFtpFile.
     * 
     * @return fileType
     */
    public java.lang.String getFileType() {
        return fileType;
    }


    /**
     * Sets the fileType value for this ActivationCampaignByFtpFile.
     * 
     * @param fileType
     */
    public void setFileType(java.lang.String fileType) {
        this.fileType = fileType;
    }


    /**
     * Gets the contactListName value for this ActivationCampaignByFtpFile.
     * 
     * @return contactListName
     */
    public java.lang.String getContactListName() {
        return contactListName;
    }


    /**
     * Sets the contactListName value for this ActivationCampaignByFtpFile.
     * 
     * @param contactListName
     */
    public void setContactListName(java.lang.String contactListName) {
        this.contactListName = contactListName;
    }


    /**
     * Gets the campaignId value for this ActivationCampaignByFtpFile.
     * 
     * @return campaignId
     */
    public int getCampaignId() {
        return campaignId;
    }


    /**
     * Sets the campaignId value for this ActivationCampaignByFtpFile.
     * 
     * @param campaignId
     */
    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }


    /**
     * Gets the ftpIp value for this ActivationCampaignByFtpFile.
     * 
     * @return ftpIp
     */
    public java.lang.String getFtpIp() {
        return ftpIp;
    }


    /**
     * Sets the ftpIp value for this ActivationCampaignByFtpFile.
     * 
     * @param ftpIp
     */
    public void setFtpIp(java.lang.String ftpIp) {
        this.ftpIp = ftpIp;
    }


    /**
     * Gets the ftpUser value for this ActivationCampaignByFtpFile.
     * 
     * @return ftpUser
     */
    public java.lang.String getFtpUser() {
        return ftpUser;
    }


    /**
     * Sets the ftpUser value for this ActivationCampaignByFtpFile.
     * 
     * @param ftpUser
     */
    public void setFtpUser(java.lang.String ftpUser) {
        this.ftpUser = ftpUser;
    }


    /**
     * Gets the ftpPassword value for this ActivationCampaignByFtpFile.
     * 
     * @return ftpPassword
     */
    public java.lang.String getFtpPassword() {
        return ftpPassword;
    }


    /**
     * Sets the ftpPassword value for this ActivationCampaignByFtpFile.
     * 
     * @param ftpPassword
     */
    public void setFtpPassword(java.lang.String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }


    /**
     * Gets the ftpFilePath value for this ActivationCampaignByFtpFile.
     * 
     * @return ftpFilePath
     */
    public java.lang.String getFtpFilePath() {
        return ftpFilePath;
    }


    /**
     * Sets the ftpFilePath value for this ActivationCampaignByFtpFile.
     * 
     * @param ftpFilePath
     */
    public void setFtpFilePath(java.lang.String ftpFilePath) {
        this.ftpFilePath = ftpFilePath;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ActivationCampaignByFtpFile)) return false;
        ActivationCampaignByFtpFile other = (ActivationCampaignByFtpFile) obj;
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
            ((this.loginPassword==null && other.getLoginPassword()==null) || 
             (this.loginPassword!=null &&
              this.loginPassword.equals(other.getLoginPassword()))) &&
            ((this.fileType==null && other.getFileType()==null) || 
             (this.fileType!=null &&
              this.fileType.equals(other.getFileType()))) &&
            ((this.contactListName==null && other.getContactListName()==null) || 
             (this.contactListName!=null &&
              this.contactListName.equals(other.getContactListName()))) &&
            this.campaignId == other.getCampaignId() &&
            ((this.ftpIp==null && other.getFtpIp()==null) || 
             (this.ftpIp!=null &&
              this.ftpIp.equals(other.getFtpIp()))) &&
            ((this.ftpUser==null && other.getFtpUser()==null) || 
             (this.ftpUser!=null &&
              this.ftpUser.equals(other.getFtpUser()))) &&
            ((this.ftpPassword==null && other.getFtpPassword()==null) || 
             (this.ftpPassword!=null &&
              this.ftpPassword.equals(other.getFtpPassword()))) &&
            ((this.ftpFilePath==null && other.getFtpFilePath()==null) || 
             (this.ftpFilePath!=null &&
              this.ftpFilePath.equals(other.getFtpFilePath())));
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
        if (getLoginPassword() != null) {
            _hashCode += getLoginPassword().hashCode();
        }
        if (getFileType() != null) {
            _hashCode += getFileType().hashCode();
        }
        if (getContactListName() != null) {
            _hashCode += getContactListName().hashCode();
        }
        _hashCode += getCampaignId();
        if (getFtpIp() != null) {
            _hashCode += getFtpIp().hashCode();
        }
        if (getFtpUser() != null) {
            _hashCode += getFtpUser().hashCode();
        }
        if (getFtpPassword() != null) {
            _hashCode += getFtpPassword().hashCode();
        }
        if (getFtpFilePath() != null) {
            _hashCode += getFtpFilePath().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ActivationCampaignByFtpFile.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">ActivationCampaignByFtpFile"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loginEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "loginEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loginPassword");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "loginPassword"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fileType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "fileType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contactListName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "contactListName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("campaignId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "campaignId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ftpIp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "ftpIp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ftpUser");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "ftpUser"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ftpPassword");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "ftpPassword"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ftpFilePath");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "ftpFilePath"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
