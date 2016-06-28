/**
 * CampaignStatus.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class CampaignStatus implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected CampaignStatus(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Inexistent = "Inexistent";
    public static final java.lang.String _Deleted = "Deleted";
    public static final java.lang.String _Sent = "Sent";
    public static final java.lang.String _Draft = "Draft";
    public static final java.lang.String _Pause = "Pause";
    public static final java.lang.String _Sending = "Sending";
    public static final java.lang.String _Waiting = "Waiting";
    public static final CampaignStatus Inexistent = new CampaignStatus(_Inexistent);
    public static final CampaignStatus Deleted = new CampaignStatus(_Deleted);
    public static final CampaignStatus Sent = new CampaignStatus(_Sent);
    public static final CampaignStatus Draft = new CampaignStatus(_Draft);
    public static final CampaignStatus Pause = new CampaignStatus(_Pause);
    public static final CampaignStatus Sending = new CampaignStatus(_Sending);
    public static final CampaignStatus Waiting = new CampaignStatus(_Waiting);
    public java.lang.String getValue() { return _value_;}
    public static CampaignStatus fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        CampaignStatus enumeration = (CampaignStatus)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static CampaignStatus fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CampaignStatus.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "CampaignStatus"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
