/**
 * SearchContacts.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class SearchContacts  implements java.io.Serializable {
    private java.lang.String loginEmail;

    private java.lang.String password;

    private java.lang.String criteria;

    private int topN;

    private java.lang.String saveAsList;

    private boolean forceCreate;

    public SearchContacts() {
    }

    public SearchContacts(
           java.lang.String loginEmail,
           java.lang.String password,
           java.lang.String criteria,
           int topN,
           java.lang.String saveAsList,
           boolean forceCreate) {
           this.loginEmail = loginEmail;
           this.password = password;
           this.criteria = criteria;
           this.topN = topN;
           this.saveAsList = saveAsList;
           this.forceCreate = forceCreate;
    }


    /**
     * Gets the loginEmail value for this SearchContacts.
     * 
     * @return loginEmail
     */
    public java.lang.String getLoginEmail() {
        return loginEmail;
    }


    /**
     * Sets the loginEmail value for this SearchContacts.
     * 
     * @param loginEmail
     */
    public void setLoginEmail(java.lang.String loginEmail) {
        this.loginEmail = loginEmail;
    }


    /**
     * Gets the password value for this SearchContacts.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this SearchContacts.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }


    /**
     * Gets the criteria value for this SearchContacts.
     * 
     * @return criteria
     */
    public java.lang.String getCriteria() {
        return criteria;
    }


    /**
     * Sets the criteria value for this SearchContacts.
     * 
     * @param criteria
     */
    public void setCriteria(java.lang.String criteria) {
        this.criteria = criteria;
    }


    /**
     * Gets the topN value for this SearchContacts.
     * 
     * @return topN
     */
    public int getTopN() {
        return topN;
    }


    /**
     * Sets the topN value for this SearchContacts.
     * 
     * @param topN
     */
    public void setTopN(int topN) {
        this.topN = topN;
    }


    /**
     * Gets the saveAsList value for this SearchContacts.
     * 
     * @return saveAsList
     */
    public java.lang.String getSaveAsList() {
        return saveAsList;
    }


    /**
     * Sets the saveAsList value for this SearchContacts.
     * 
     * @param saveAsList
     */
    public void setSaveAsList(java.lang.String saveAsList) {
        this.saveAsList = saveAsList;
    }


    /**
     * Gets the forceCreate value for this SearchContacts.
     * 
     * @return forceCreate
     */
    public boolean isForceCreate() {
        return forceCreate;
    }


    /**
     * Sets the forceCreate value for this SearchContacts.
     * 
     * @param forceCreate
     */
    public void setForceCreate(boolean forceCreate) {
        this.forceCreate = forceCreate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SearchContacts)) return false;
        SearchContacts other = (SearchContacts) obj;
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
            ((this.criteria==null && other.getCriteria()==null) || 
             (this.criteria!=null &&
              this.criteria.equals(other.getCriteria()))) &&
            this.topN == other.getTopN() &&
            ((this.saveAsList==null && other.getSaveAsList()==null) || 
             (this.saveAsList!=null &&
              this.saveAsList.equals(other.getSaveAsList()))) &&
            this.forceCreate == other.isForceCreate();
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
        if (getCriteria() != null) {
            _hashCode += getCriteria().hashCode();
        }
        _hashCode += getTopN();
        if (getSaveAsList() != null) {
            _hashCode += getSaveAsList().hashCode();
        }
        _hashCode += (isForceCreate() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SearchContacts.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", ">SearchContacts"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loginEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "LoginEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("password");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "Password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("criteria");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "Criteria"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("topN");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "TopN"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("saveAsList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "SaveAsList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("forceCreate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "ForceCreate"));
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
