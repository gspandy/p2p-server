
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SendMessageData complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="SendMessageData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BusinessId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="MessageTemplateId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ReBackCall" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SendMobile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SmsSource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SendMessageData", propOrder = {
    "businessId",
    "messageTemplateId",
    "reBackCall",
    "userId",
    "sendMobile",
    "smsSource"
})
public class SendMessageData {

    @XmlElement(name = "BusinessId")
    protected int businessId;
    @XmlElement(name = "MessageTemplateId")
    protected String messageTemplateId;
    @XmlElement(name = "ReBackCall")
    protected String reBackCall;
    @XmlElement(name = "UserId")
    protected int userId;
    @XmlElement(name = "SendMobile")
    protected String sendMobile;
    @XmlElement(name = "SmsSource")
    protected String smsSource;

    /**
     * 获取businessId属性的值。
     * 
     */
    public int getBusinessId() {
        return businessId;
    }

    /**
     * 设置businessId属性的值。
     * 
     */
    public void setBusinessId(int value) {
        this.businessId = value;
    }

    /**
     * 获取messageTemplateId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageTemplateId() {
        return messageTemplateId;
    }

    /**
     * 设置messageTemplateId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageTemplateId(String value) {
        this.messageTemplateId = value;
    }

    /**
     * 获取reBackCall属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReBackCall() {
        return reBackCall;
    }

    /**
     * 设置reBackCall属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReBackCall(String value) {
        this.reBackCall = value;
    }

    /**
     * 获取userId属性的值。
     * 
     */
    public int getUserId() {
        return userId;
    }

    /**
     * 设置userId属性的值。
     * 
     */
    public void setUserId(int value) {
        this.userId = value;
    }

    /**
     * 获取sendMobile属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendMobile() {
        return sendMobile;
    }

    /**
     * 设置sendMobile属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendMobile(String value) {
        this.sendMobile = value;
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

}
