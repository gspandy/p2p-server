package org.tempuri;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.0.0
 * 2015-05-07T12:47:26.954+08:00
 * Generated source version: 3.0.0
 * 
 */
@WebService(targetNamespace = "http://tempuri.org/", name = "VcreditSmsServiceSoap")
@XmlSeeAlso({ObjectFactory.class})
public interface VcreditSmsServiceSoap {

    /**
     * 发送短信
     */
    @WebResult(name = "SendSmsResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "SendSms", targetNamespace = "http://tempuri.org/", className = "org.tempuri.SendSms")
    @WebMethod(operationName = "SendSms", action = "http://tempuri.org/SendSms")
    @ResponseWrapper(localName = "SendSmsResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.SendSmsResponse")
    public org.tempuri.RequestResult sendSms(
        @WebParam(name = "mobile", targetNamespace = "http://tempuri.org/")
        java.lang.String mobile,
        @WebParam(name = "content", targetNamespace = "http://tempuri.org/")
        java.lang.String content,
        @WebParam(name = "type", targetNamespace = "http://tempuri.org/")
        java.lang.String type
    );
}