
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BusinessId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="MessageTemplateId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ReBackCall" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LoginName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Mobile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "businessId",
    "messageTemplateId",
    "reBackCall",
    "loginName",
    "mobile"
})
@XmlRootElement(name = "SendMessageAusXin")
public class SendMessageAusXin {

    @XmlElement(name = "BusinessId")
    protected int businessId;
    @XmlElement(name = "MessageTemplateId")
    protected String messageTemplateId;
    @XmlElement(name = "ReBackCall")
    protected String reBackCall;
    @XmlElement(name = "LoginName")
    protected String loginName;
    @XmlElement(name = "Mobile")
    protected String mobile;

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
     * 获取loginName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * 设置loginName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoginName(String value) {
        this.loginName = value;
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

}
