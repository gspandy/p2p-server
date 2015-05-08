
package org.tempuri;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfSmsMessageData complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayOfSmsMessageData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SmsMessageData" type="{http://tempuri.org/}SmsMessageData" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfSmsMessageData", propOrder = {
    "smsMessageData"
})
public class ArrayOfSmsMessageData {

    @XmlElement(name = "SmsMessageData", nillable = true)
    protected List<SmsMessageData> smsMessageData;

    /**
     * Gets the value of the smsMessageData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the smsMessageData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSmsMessageData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SmsMessageData }
     * 
     * 
     */
    public List<SmsMessageData> getSmsMessageData() {
        if (smsMessageData == null) {
            smsMessageData = new ArrayList<SmsMessageData>();
        }
        return this.smsMessageData;
    }

}
