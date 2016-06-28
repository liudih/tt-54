/**
 * SplitContactListByRange.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class SplitContactListByRange  implements java.io.Serializable {
    private java.lang.String loginEmail;

    private java.lang.String APIKey;

    private java.lang.String sourceContactListName;

    private java.lang.String range;

    private java.lang.String targetContactListName;

    public SplitContactListByRange() {
    }

    public SplitContactListByRange(
           java.lang.String loginEmail,
           java.lang.String APIKey,
           java.lang.String sourceContactListName,
           java.lang.String range,
           java.lang.String targetContactListName) {
           this.loginEmail = loginEmail;
           this.APIKey = APIKey;
           this.sourceContactListName = sourceContactListName;
           this.range = range;
           this.targetContactListName = targetContactListName;
    }


    /**
     * Gets the loginEmail value for this SplitContactListByRange.
     * 
     * @return loginEmail
     */
    public java.lang.String getLoginEmail() {
        return loginEmail;
    }


    /**
     * Sets the loginEmail value for this SplitContactListByRange.
     * 
     * @param loginEmail
     */
    public void setLoginEmail(java.lang.String loginEmail) {
        this.loginEmail = loginEmail;
    }


    /**
     * Gets the APIKey value for this SplitContactListByRange.
     * 
     * @return APIKey
     */
    public java.lang.String getAPIKey() {
        return APIKey;
    }


    /**
     * Sets the APIKey value for this SplitContactListByRange.
     * 
     * @param APIKey
     */
    public void setAPIKey(java.lang.String APIKey) {
        this.APIKey = APIKey;
    }


    /**
     * Gets the sourceContactListName value for this SplitContactListByRange.
     * 
     * @return sourceContactListName
     */
    public java.lang.String getSourceContactListName() {
        return sourceContactListName;
    }


    /**
     * Sets the sourceContactListName value for this SplitContactListByRange.
     * 
     * @param sourceContactListName
     */
    public void setSourceContactListName(java.lang.String sourceContactListName) {
        this.sourceContactListName = sourceContactListName;
    }


    /**
     * Gets the range value for this SplitContactListByRange.
     * 
     * @return range
     */
    public java.lang.String getRange() {
        return range;
    }


    /**
     * Sets the range value for this SplitContactListByRange.
     * 
     * @param range
     */
    public void setRange(java.lang.String range) {
        this.range = range;
    }


    /**
     * Gets the targetContactListName value for this SplitContactListByRange.
     * 
     * @return targetContactListName
     */
    public java.lang.String getTargetContactListName() {
        return targetContactListName;
    }


    /**
     * Sets the targetContactListName value for this SplitContactListByRange.
     * 
     * @param targetContactListName
     */
    public void setTargetContactListName(java.lang.String targetContactListName) {
        this.targetContactListName = targetContactListName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SplitContactListByRange)) return false;
        SplitContactListByRange other = (SplitContactListByRange) obj;
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
            ((this.APIKey==null && other.getAPIKey()==null) || 
             (this.APIKey!=null &&
              this.APIKey.equals(other.getAPIKey()))) &&
            ((this.sourceContactListName==null && other.getSourceContactListName()==null) || 
             (this.sourceContactListName!=null &&
              this.sourceContactListName.equals(other.getSourceContactListName()))) &&
            ((this.range==null && other.getRange()==null) || 
             (this.range!=null &&
              this.range.equals(other.getRange()))) &&
            ((this.targetContactListName==null && other.getTargetContactListName()==null) || 
             (this.targetContactListName!=null &&
              this.targetContactListName.equals(other.getTargetContactListName())));
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
        if (getAPIKey() != null) {
            _hashCode += getAPIKey().hashCode();
        }
        if (getSourceContactListName() != null) {
            _hashCode += getSourceContactListName().hashCode();
        }
        if (getRange() != null) {
            _hashCode += getRange().hashCode();
        }
        if (getTargetContactListName() != null) {
            _hashCode += getTargetContactListName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SplitContactListByRange.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">SplitContactListByRange"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loginEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "loginEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("APIKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "APIKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceContactListName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "SourceContactListName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("range");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "Range"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("targetContactListName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "TargetContactListName"));
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
