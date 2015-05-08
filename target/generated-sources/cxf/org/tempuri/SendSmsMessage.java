
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="messageData" type="{http://tempuri.org/}SmsMessageData" minOccurs="0"/>
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
    "messageData"
})
@XmlRootElement(name = "SendSmsMessage")
public class SendSmsMessage {

    protected SmsMessageData messageData;

    /**
     * 获取messageData属性的值。
     * 
     * @return
     *     possible object is
     *     {@link SmsMessageData }
     *     
     */
    public SmsMessageData getMessageData() {
        return messageData;
    }

    /**
     * 设置messageData属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link SmsMessageData }
     *     
     */
    public void setMessageData(SmsMessageData value) {
        this.messageData = value;
    }

}
