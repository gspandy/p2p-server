
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
 *         &lt;element name="lstSendMessageData" type="{http://tempuri.org/}ArrayOfSendMessageData" minOccurs="0"/>
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
    "lstSendMessageData"
})
@XmlRootElement(name = "SendMessageList")
public class SendMessageList {

    protected ArrayOfSendMessageData lstSendMessageData;

    /**
     * 获取lstSendMessageData属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSendMessageData }
     *     
     */
    public ArrayOfSendMessageData getLstSendMessageData() {
        return lstSendMessageData;
    }

    /**
     * 设置lstSendMessageData属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSendMessageData }
     *     
     */
    public void setLstSendMessageData(ArrayOfSendMessageData value) {
        this.lstSendMessageData = value;
    }

}
