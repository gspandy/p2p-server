
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SmsMessageData complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="SmsMessageData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OperationUserID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="MessageContent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Mobile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SignStr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SmsSource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SmsTypeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CustomerType" type="{http://www.w3.org/2001/XMLSchema}unsignedByte"/>
 *         &lt;element name="Parameters" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Remark" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SmsMessageData", propOrder = {
    "operationUserID",
    "messageContent",
    "mobile",
    "signStr",
    "smsSource",
    "smsTypeName",
    "customerType",
    "parameters",
    "remark"
})
public class SmsMessageData {

    @XmlElement(name = "OperationUserID")
    protected int operationUserID;
    @XmlElement(name = "MessageContent")
    protected String messageContent;
    @XmlElement(name = "Mobile")
    protected String mobile;
    @XmlElement(name = "SignStr")
    protected String signStr;
    @XmlElement(name = "SmsSource")
    protected String smsSource;
    @XmlElement(name = "SmsTypeName")
    protected String smsTypeName;
    @XmlElement(name = "CustomerType")
    @XmlSchemaType(name = "unsignedByte")
    protected short customerType;
    @XmlElement(name = "Parameters")
    protected String parameters;
    @XmlElement(name = "Remark")
    protected String remark;

    /**
     * 获取operationUserID属性的值。
     * 
     */
    public int getOperationUserID() {
        return operationUserID;
    }

    /**
     * 设置operationUserID属性的值。
     * 
     */
    public void setOperationUserID(int value) {
        this.operationUserID = value;
    }

    /**
     * 获取messageContent属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * 设置messageContent属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageContent(String value) {
        this.messageContent = value;
    }

    /**
     * 获取mobile属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置mobile属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobile(String value) {
        this.mobile = value;
    }

    /**
     * 获取signStr属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignStr() {
        return signStr;
    }

    /**
     * 设置signStr属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignStr(String value) {
        this.signStr = value;
    }

    /**
     * 获取smsSource属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSmsSource() {
        return smsSource;
    }

    /**
     * 设置smsSource属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSmsSource(String value) {
        this.smsSource = value;
    }

    /**
     * 获取smsTypeName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSmsTypeName() {
        return smsTypeName;
    }

    /**
     * 设置smsTypeName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSmsTypeName(String value) {
        this.smsTypeName = value;
    }

    /**
     * 获取customerType属性的值。
     * 
     */
    public short getCustomerType() {
        return customerType;
    }

    /**
     * 设置customerType属性的值。
     * 
     */
    public void setCustomerType(short value) {
        this.customerType = value;
    }

    /**
     * 获取parameters属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParameters() {
        return parameters;
    }

    /**
     * 设置parameters属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParameters(String value) {
        this.parameters = value;
    }

    /**
     * 获取remark属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置remark属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemark(String value) {
        this.remark = value;
    }

}
