/**
 * SubscriberStatus.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package SpreadWS.javapackage;

public class SubscriberStatus implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected SubscriberStatus(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Any = "Any";
    public static final java.lang.String _Deleted = "Deleted";
    public static final java.lang.String _Active = "Active";
    public static final java.lang.String _Unsubscribed = "Unsubscribed";
    public static final java.lang.String _Unconfirmed = "Unconfirmed";
    public static final java.lang.String _Undeliverable = "Undeliverable";
    public static final java.lang.String _SpamReporter = "SpamReporter";
    public static final java.lang.String _DoNotMail = "DoNotMail";
    public static final java.lang.String _Blacklist = "Blacklist";
    public static final SubscriberStatus Any = new SubscriberStatus(_Any);
    public static final SubscriberStatus Deleted = new SubscriberStatus(_Deleted);
    public static final SubscriberStatus Active = new SubscriberStatus(_Active);
    public static final SubscriberStatus Unsubscribed = new SubscriberStatus(_Unsubscribed);
    public static final SubscriberStatus Unconfirmed = new SubscriberStatus(_Unconfirmed);
    public static final SubscriberStatus Undeliverable = new SubscriberStatus(_Undeliverable);
    public static final SubscriberStatus SpamReporter = new SubscriberStatus(_SpamReporter);
    public static final SubscriberStatus DoNotMail = new SubscriberStatus(_DoNotMail);
    public static final SubscriberStatus Blacklist = new SubscriberStatus(_Blacklist);
    public java.lang.String getValue() { return _value_;}
    public static SubscriberStatus fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        SubscriberStatus enumeration = (SubscriberStatus)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static SubscriberStatus fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(SubscriberStatus.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.reasonablespread.com/", "SubscriberStatus"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
