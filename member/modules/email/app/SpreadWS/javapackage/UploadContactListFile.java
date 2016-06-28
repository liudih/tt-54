/**
 * UploadContactListFile.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class UploadContactListFile  implements java.io.Serializable {
    private java.lang.String loginEmail;

    private java.lang.String loginPassword;

    private java.lang.String fileType;

    private byte[] myFileStream;

    private java.lang.String contactListName;

    public UploadContactListFile() {
    }

    public UploadContactListFile(
           java.lang.String loginEmail,
           java.lang.String loginPassword,
           java.lang.String fileType,
           byte[] myFileStream,
           java.lang.String contactListName) {
           this.loginEmail = loginEmail;
           this.loginPassword = loginPassword;
           this.fileType = fileType;
           this.myFileStream = myFileStream;
           this.contactListName = contactListName;
    }


    /**
     * Gets the loginEmail value for this UploadContactListFile.
     * 
     * @return loginEmail
     */
    public java.lang.String getLoginEmail() {
        return loginEmail;
    }


    /**
     * Sets the loginEmail value for this UploadContactListFile.
     * 
     * @param loginEmail
     */
    public void setLoginEmail(java.lang.String loginEmail) {
        this.loginEmail = loginEmail;
    }


    /**
     * Gets the loginPassword value for this UploadContactListFile.
     * 
     * @return loginPassword
     */
    public java.lang.String getLoginPassword() {
        return loginPassword;
    }


    /**
     * Sets the loginPassword value for this UploadContactListFile.
     * 
     * @param loginPassword
     */
    public void setLoginPassword(java.lang.String loginPassword) {
        this.loginPassword = loginPassword;
    }


    /**
     * Gets the fileType value for this UploadContactListFile.
     * 
     * @return fileType
     */
    public java.lang.String getFileType() {
        return fileType;
    }


    /**
     * Sets the fileType value for this UploadContactListFile.
     * 
     * @param fileType
     */
    public void setFileType(java.lang.String fileType) {
        this.fileType = fileType;
    }


    /**
     * Gets the myFileStream value for this UploadContactListFile.
     * 
     * @return myFileStream
     */
    public byte[] getMyFileStream() {
        return myFileStream;
    }


    /**
     * Sets the myFileStream value for this UploadContactListFile.
     * 
     * @param myFileStream
     */
    public void setMyFileStream(byte[] myFileStream) {
        this.myFileStream = myFileStream;
    }


    /**
     * Gets the contactListName value for this UploadContactListFile.
     * 
     * @return contactListName
     */
    public java.lang.String getContactListName() {
        return contactListName;
    }


    /**
     * Sets the contactListName value for this UploadContactListFile.
     * 
     * @param contactListName
     */
    public void setContactListName(java.lang.String contactListName) {
        this.contactListName = contactListName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UploadContactListFile)) return false;
        UploadContactListFile other = (UploadContactListFile) obj;
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
            ((this.myFileStream==null && other.getMyFileStream()==null) || 
             (this.myFileStream!=null &&
              java.util.Arrays.equals(this.myFileStream, other.getMyFileStream()))) &&
            ((this.contactListName==null && other.getContactListName()==null) || 
             (this.contactListName!=null &&
              this.contactListName.equals(other.getContactListName())));
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
        if (getMyFileStream() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMyFileStream());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMyFileStream(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getContactListName() != null) {
            _hashCode += getContactListName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UploadContactListFile.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">UploadContactListFile"));
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
        elemField.setFieldName("myFileStream");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "myFileStream"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contactListName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "ContactListName"));
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
