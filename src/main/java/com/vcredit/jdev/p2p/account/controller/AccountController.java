package com.vcredit.jdev.p2p.account.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vcredit.jdev.p2p.account.modal.AccountInfoManager;
import com.vcredit.jdev.p2p.account.modal.CommonManager;
import com.vcredit.jdev.p2p.account.modal.EmailManager;
import com.vcredit.jdev.p2p.account.modal.LoginManager;
import com.vcredit.jdev.p2p.account.modal.MobileManager;
import com.vcredit.jdev.p2p.account.modal.PasswordManager;
import com.vcredit.jdev.p2p.account.modal.RegisterManager;
import com.vcredit.jdev.p2p.account.modal.TestDao;
import com.vcredit.jdev.p2p.base.P2pSessionContext;
import com.vcredit.jdev.p2p.base.Response;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.base.util.ConstantsUtil;
import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.SystemQuestion;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON+ ";charset=UTF-8")
@Consumes(MediaType.APPLICATION_JSON) 
public class AccountController {

	private static Logger logger = Logger.getLogger(AccountController.class);
	private static org.slf4j.Logger logger2 = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private P2pSessionContext p2pSessionContext;

	@Autowired
	private LoginManager loginManager;

	@Autowired
	private RegisterManager registerManager;

	@Autowired
	private MobileManager mobileManager;

	@Autowired
	private EmailManager emailManager;

	@Autowired
	private PasswordManager passwordManager;

	@Autowired
	private AccountInfoManager accountInfoManager;

	@Autowired
	private CommonManager commonManager;

	//	@Autowired
	//	private CapitalManager capitalManager;



	@POST
	@Path("/login")
	public Response login(Map<String, Object> paramMap,@Context HttpServletRequest request){
		String userName = (String) paramMap.get("userName");		
		String password = (String) paramMap.get("password");		
		//String verificationCode = (String) paramMap.get("verificationCode");
		String ip = request.getHeader("X-FORWARDED-FOR");  
		if (ip == null) {  
			ip = request.getRemoteAddr();  
		}
		if(StringUtils.isBlank(userName)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_202, ResponseConstants.CommonMessage.RESPONSE_MSG_202);
		}
		if(StringUtils.isBlank(password)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_203, ResponseConstants.CommonMessage.RESPONSE_MSG_203);
		}
//		if(StringUtils.isBlank(verificationCode)){
//			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_204, ResponseConstants.CommonMessage.RESPONSE_MSG_204);
//		}
//		String ip = cxfUtil.getClientIpCxfRest();
		try {
			int result = loginManager.login(userName, password,ip);
			if(result == 1){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_200, ResponseConstants.CommonMessage.RESPONSE_MSG_200);
			}else if(result == 2){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_205, ResponseConstants.CommonMessage.RESPONSE_MSG_205);
			}else if(result == 3){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_206, ResponseConstants.CommonMessage.RESPONSE_MSG_206);
			}else if(result == 4){
				return Response.successResponse();
			}
		} catch (Exception e) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE,e.getMessage());
		}
		return Response.successResponse();
	}


	/** 
	 * @Title: accountIsExist 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param paramMap
	 * @param @return    设定文件 
	 * @return Response    返回类型 
	 * @throws 
	 */
	@POST
	@Path("/accountisexist")
	public Response accountIsExist(Map<String, Object> paramMap){
		String userName = (String) paramMap.get("userName");		
		if(StringUtils.isBlank(userName)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_202, ResponseConstants.CommonMessage.RESPONSE_MSG_202);
		}
		if(commonManager.isExistUserName(userName)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_201, ResponseConstants.CommonMessage.RESPONSE_MSG_201,true);
		}else{
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_200, ResponseConstants.CommonMessage.RESPONSE_MSG_200,false);
		}
	}




	//*************************************************register******************************
	/** 
	* @Title: register 
	* @Description: 用户在注册完成后，默认登录
	*  @param paramMap
	*  @return
	* @return Response    返回类型 
	* @throws 
	*/
	@POST
	@Path("/register")
	public Response register(Map<String, Object> paramMap,@Context HttpServletRequest request){
		String userName = (String) paramMap.get("userName");		
		String password = (String) paramMap.get("password");		
		String mobile = (String) paramMap.get("mobile");		
		if(StringUtils.isBlank(userName)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_202, ResponseConstants.CommonMessage.RESPONSE_MSG_202);
		}
		if(StringUtils.isBlank(password)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_203, ResponseConstants.CommonMessage.RESPONSE_MSG_203);
		}
		if(StringUtils.isBlank(mobile)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_207, ResponseConstants.CommonMessage.RESPONSE_MSG_207);
		}
		//1.取调用rest的IP
		String ip = request.getHeader("X-FORWARDED-FOR");  
		if (ip == null) {  
			ip = request.getRemoteAddr();  
		}
		Account account = null;
		//2.注册用户
		try {
			account = registerManager.register(userName, password, 1, mobile, ip);
		} catch (Exception e) {
			e.printStackTrace();
			if("1".equals(e.getMessage())){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_201, ResponseConstants.CommonMessage.RESPONSE_MSG_201);
			}else if("2".equals(e.getMessage())){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_208, ResponseConstants.CommonMessage.RESPONSE_MSG_208);
			}else{
				logger.error("用户注册失败 user register error:"+e.getMessage());
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_230, ResponseConstants.CommonMessage.RESPONSE_MSG_230);
			}
		}
		logger.debug("register finish..........................&&&&&&&&&&&");
		//3.登录
		try {
			int result = loginManager.login(userName, password,ip);
			if(result == 1){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_200, ResponseConstants.CommonMessage.RESPONSE_MSG_200);
			}else if(result == 2){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_205, ResponseConstants.CommonMessage.RESPONSE_MSG_205);
			}else if(result == 3){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_206, ResponseConstants.CommonMessage.RESPONSE_MSG_206);
			}
		} catch (Exception e) {
			logger.error("用户注册完本登录失败 user register after login error:"+e.getMessage());
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE,e.getMessage());
		}
		logger.debug("register after login finish..........................&&&&&&&&&&&");
		return Response.successResponse(account.getAccountSequence());
	}




	//*************************************************mobile******************************
	@POST
	@Path("/sendMobileVerificationCode")
	public Response sendMobileVerificationCode(Map<String, Object> paramMap){
		String mobile = (String) paramMap.get("mobile");	
		String sendKind = (String) paramMap.get("sendKind");	
		if(StringUtils.isBlank(mobile)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_207, ResponseConstants.CommonMessage.RESPONSE_MSG_207);
		}
		try {
			mobileManager.sendMobileVerificationCode(mobile,sendKind);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_213, ResponseConstants.CommonMessage.RESPONSE_MSG_213);
		}
		return Response.successResponse();
	}

	@POST
	@Path("/validMobileVerificationCode")
	public Response validMobileVerificationCode(Map<String, Object> paramMap){
		String mobile = (String) paramMap.get("mobile");		
		String mobileVerificationCode = (String) paramMap.get("mobileVerificationCode");
		if(StringUtils.isBlank(mobile)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_207, ResponseConstants.CommonMessage.RESPONSE_MSG_207);
		}
		if(StringUtils.isBlank(mobileVerificationCode)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_210, ResponseConstants.CommonMessage.RESPONSE_MSG_210);
		}
		int result = mobileManager.mobileCodeValidate(mobile, mobileVerificationCode);

		if(result == 1){//true
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_211, ResponseConstants.CommonMessage.RESPONSE_MSG_211);
		}else if(result == 2){//time out
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_244, ResponseConstants.CommonMessage.RESPONSE_MSG_244);
		}else{//false
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_212, ResponseConstants.CommonMessage.RESPONSE_MSG_212);
		}
	}

	@POST
	@Path("/modifyAccountMobile")
	public Response modifyAccountMobile(Map<String, Object> paramMap){
		String mobile = (String) paramMap.get("mobile");	
		if(StringUtils.isBlank(mobile)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_207, ResponseConstants.CommonMessage.RESPONSE_MSG_207);
		}
		//TODO JUnit Test not get aid
		Long aid = p2pSessionContext.getCurrentAid();
		if(aid == null){
			return Response.noLoginResponse();
		}
		//Long aid = 1L;
		try {
			int result = mobileManager.editAccountMobile(aid, mobile);
			if(result == -1){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_208, ResponseConstants.CommonMessage.RESPONSE_MSG_208);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_209, ResponseConstants.CommonMessage.RESPONSE_MSG_209);
		}
		return Response.successResponse();
	}

	@POST
	@Path("/checkMobileAndPwd")
	public Response checkMobileAndPwd(Map<String, Object> paramMap){
		String mobile = (String) paramMap.get("mobile");		
		String password = (String) paramMap.get("password");		
		if(StringUtils.isBlank(mobile)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_207, ResponseConstants.CommonMessage.RESPONSE_MSG_207);
		}
		if(StringUtils.isBlank(password)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_203, ResponseConstants.CommonMessage.RESPONSE_MSG_203);
		}
		try {
			int result = loginManager.checkMobileAndPwd(mobile, password);
			if(result == 1){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_200, ResponseConstants.CommonMessage.RESPONSE_MSG_200);
			}else if(result == 2){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_205, ResponseConstants.CommonMessage.RESPONSE_MSG_205);
			}else if(result == 4){
				return Response.successResponse();
			}
		} catch (Exception e) {
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE,e.getMessage());
		}
		return Response.successResponse();
	}

	@POST
	@Path("/checkMobile")
	public Response checkMobile(Map<String, Object> paramMap){
		String mobile = (String) paramMap.get("mobile");		
		if(StringUtils.isBlank(mobile)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_207, ResponseConstants.CommonMessage.RESPONSE_MSG_207);
		}
		Long aid = p2pSessionContext.getCurrentAid();
		if(aid == null){
			return Response.noLoginResponse();
		}
		//Long aid = 1L;

		boolean result = commonManager.checkMobile(aid, mobile);
		if(result){
			return Response.successResponse();
		}else{
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_238, ResponseConstants.CommonMessage.RESPONSE_MSG_238);
		}
	}
	
	
	/** 
	* @Title: checkMobileIsExist 
	* @Description: 判断手机号是否存在 
	*  @param paramMap
	*  @return
	* @return Response    返回类型 
	* @throws 
	*/
	@POST
	@Path("/checkMobileIsExist")
	public Response checkMobileIsExist(Map<String, Object> paramMap){
		String mobile = (String) paramMap.get("mobile");		
		if(StringUtils.isBlank(mobile)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_207, ResponseConstants.CommonMessage.RESPONSE_MSG_207);
		}
		boolean result = commonManager.isExistMobile(mobile);
		if(result){
			return Response.successResponse();
		}else{
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_238, ResponseConstants.CommonMessage.RESPONSE_MSG_238);
		}
	}

	@POST
	@Path("/getAccountById")
	public Response getAccountById(){
		//TODO JUnit Test not get aid
		Long aid = p2pSessionContext.getCurrentAid();
		if(aid == null){
			return Response.noLoginResponse();
		}
		//Long aid = 1L;
		Account account = null;
		//ThirdPaymentAccount third = null;
		//Map<String, Object> map = new HashMap<String, Object>();
		try {
			account = commonManager.getAcccontById(aid);
			//third = capitalManager.queryCapitalInfo(aid);
			//map.put("account", account);
			//map.put("third", third);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_209, ResponseConstants.CommonMessage.RESPONSE_MSG_209);
		}
		return Response.successResponse(account);
		//return Response.successResponse(map);
	}



	//*************************************************email******************************
	@POST
	@Path("/modifyAccountEmail")
	public Response modifyAccountEmail(Map<String, Object> paramMap){
		String email = (String) paramMap.get("email");
		String type = (String) paramMap.get("type");
		if(StringUtils.isBlank(email)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_231, ResponseConstants.CommonMessage.RESPONSE_MSG_231);
		}
		//TODO JUnit Test not get aid
		Long aid = p2pSessionContext.getCurrentAid();
		if(aid == null){
			return Response.noLoginResponse();
		}
		//Long aid = 99L;
		try {
			emailManager.editEmailAndSend(aid, email,type);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_217, ResponseConstants.CommonMessage.RESPONSE_MSG_217);
		}
		return Response.successResponse();
	}
	

	//*************************************************password******************************
	@POST
	@Path("/findPassword")
	public Response findPassword(Map<String, Object> paramMap){
		String userName = (String) paramMap.get("userName");		
		String password = (String) paramMap.get("password");
		if(StringUtils.isBlank(userName)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_202, ResponseConstants.CommonMessage.RESPONSE_MSG_202);
		}
		if(StringUtils.isBlank(password)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_203, ResponseConstants.CommonMessage.RESPONSE_MSG_203);
		}
		try {
			passwordManager.findPassword(userName, password);
		} catch (Exception e) {
			if("1".equals(e.getMessage())){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_200, ResponseConstants.CommonMessage.RESPONSE_MSG_200);
			}else if("2".equals(e.getMessage())){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_218, ResponseConstants.CommonMessage.RESPONSE_MSG_218);
			}else{
				return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, e.getMessage());
			}
		}
		return Response.successResponse();
	}

	@POST
	@Path("/isUserNameMatchMobile")
	public Response isUserNameMatchMobile(Map<String, Object> paramMap){
		String userName = (String) paramMap.get("userName");		
		String mobile = (String) paramMap.get("mobile");	
		if(StringUtils.isBlank(userName)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_202, ResponseConstants.CommonMessage.RESPONSE_MSG_202);
		}
		if(StringUtils.isBlank(mobile)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_207, ResponseConstants.CommonMessage.RESPONSE_MSG_207);
		}
		if(commonManager.isUserNameMatchMobile(userName, mobile)){
			return Response.successResponse();
		}else{
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_239, ResponseConstants.CommonMessage.RESPONSE_MSG_239);
		}
	}

	@POST
	@Path("/resetPassword")
	public Response resetPassword(Map<String, Object> paramMap){
		String oldpwd = (String) paramMap.get("oldpwd");		
		String newpwd = (String) paramMap.get("newpwd");	
		if(StringUtils.isBlank(oldpwd)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_219, ResponseConstants.CommonMessage.RESPONSE_MSG_219);
		}
		if(StringUtils.isBlank(newpwd)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_220, ResponseConstants.CommonMessage.RESPONSE_MSG_220);
		}
		//TODO JUnit Test not get aid
		Long aid = p2pSessionContext.getCurrentAid();
		if(aid == null){
			return Response.noLoginResponse();
		}
		//Long aid = 1L;
		try {
			passwordManager.resetPassword(aid, oldpwd, newpwd);
		} catch (Exception e) {
			if("1".equals(e.getMessage())){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_200, ResponseConstants.CommonMessage.RESPONSE_MSG_200);
			}else if("2".equals(e.getMessage())){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_205, ResponseConstants.CommonMessage.RESPONSE_MSG_205);
			}else if("3".equals(e.getMessage())){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_221, ResponseConstants.CommonMessage.RESPONSE_MSG_221);
			}else if("4".equals(e.getMessage())){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_243, ResponseConstants.CommonMessage.RESPONSE_MSG_243);
			}else{
				return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, e.getMessage());
			}
		}
		return Response.successResponse();
	}

	//***********************************安全问题****************************************
	@SuppressWarnings("unchecked")
	@POST
	@Path("/saveAccountQuestionAnswer")
	public Response saveAccountQuestionAnswer(Map<String, Object> paramMap) throws Exception{
		String password = (String) paramMap.get("password");
		System.out.println(paramMap.get("data"));
		List<Map<String, String>> list = (List<Map<String, String>>) paramMap.get("data");
		if(StringUtils.isBlank(password)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_203, ResponseConstants.CommonMessage.RESPONSE_MSG_203);
		}
		//List<TestDto> list= P2pUtil.getBeansFromJson(P2pUtil.getJosnByKey(dto, "data"), TestDto.class);
		//System.out.println(list);
		//TODO JUnit Test not get aid
		Long aid = p2pSessionContext.getCurrentAid();
		if(aid == null){
			return Response.noLoginResponse();
		}
		//Long aid = 10L;
		int result = loginManager.checkPwd(aid, password);
		if(result == 0){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_205, ResponseConstants.CommonMessage.RESPONSE_MSG_205);
		}else if(result == 1){
			try {
				passwordManager.saveAccountQuestionAnswerList(list, aid);
			} catch (Exception e) {
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_224, ResponseConstants.CommonMessage.RESPONSE_MSG_224);
			}
		}

		return Response.successResponse();
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("/updateAccountQuestionAnswer")
	public Response updateAccountQuestionAnswer(Map<String, Object> paramMap) throws Exception{
		System.out.println(paramMap.get("data"));
		List<Map<String, String>> list = (List<Map<String, String>>) paramMap.get("data");
		//TODO JUnit Test not get aid
		Long aid = p2pSessionContext.getCurrentAid();
		if(aid == null){
			return Response.noLoginResponse();
		}
		//Long aid = 10L;
		try {
			passwordManager.saveAccountQuestionAnswerList(list, aid);
		} catch (Exception e) {
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_224, ResponseConstants.CommonMessage.RESPONSE_MSG_224);
		}

		return Response.successResponse();
	}

	@POST
	@Path("/getQuestionList")
	public Response getQuestionList(){
		List<SystemQuestion> list = null;
		try {
			list = passwordManager.getSystemQuestionList(ConstantsUtil.EntityStatus.STATUS_ENABLE);
		} catch (Exception e) {
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_225, ResponseConstants.CommonMessage.RESPONSE_MSG_225);
		}
		return Response.successResponse(list);
	}

	@POST
	@Path("/getQuestionListByUserName")
	public Response getQuestionListByUserName(Map<String, Object> paramMap){
		String userName = (String) paramMap.get("userName");
		if(StringUtils.isBlank(userName)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_202, ResponseConstants.CommonMessage.RESPONSE_MSG_202);
		}
		List<SystemQuestion> list = null;
		try {
			list = passwordManager.getSystemQuestionListByAid(userName, ConstantsUtil.EntityStatus.STATUS_ENABLE);
		} catch (Exception e) {
			if("1".equals(e.getMessage())){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_200, ResponseConstants.CommonMessage.RESPONSE_MSG_200);
			}else{
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_237, ResponseConstants.CommonMessage.RESPONSE_MSG_237);
			}
		}
		return Response.successResponse(list);
	}

	@POST
	@Path("/checkAccountIsSetQuestion")
	public Response checkAccountIsSetQuestion(Map<String, Object> paramMap){
		String userName = (String) paramMap.get("userName");
		if(StringUtils.isBlank(userName)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_202, ResponseConstants.CommonMessage.RESPONSE_MSG_202);
		}
		try {
			if(passwordManager.checkAccountIsSetQuestion(userName)){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_226, ResponseConstants.CommonMessage.RESPONSE_MSG_226);
			}else{
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_227, ResponseConstants.CommonMessage.RESPONSE_MSG_227);
			}
		} catch (Exception e) {
			if("1".equals(e.getMessage())){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_200, ResponseConstants.CommonMessage.RESPONSE_MSG_200);
			}else{
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_236, ResponseConstants.CommonMessage.RESPONSE_MSG_236);
			}
		}
	}

	@POST
	@Path("/checkAccountQuestionAnswer")
	public Response checkAccountQuestionAnswer(Map<String, Object> paramMap){
		String qid = (String) paramMap.get("systemQuestionSequence");		
		String answer = (String) paramMap.get("answer");
		String userName = (String) paramMap.get("userName");
		if(StringUtils.isBlank(userName)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_202, ResponseConstants.CommonMessage.RESPONSE_MSG_202);
		}
		if(StringUtils.isBlank(qid)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_222, ResponseConstants.CommonMessage.RESPONSE_MSG_222);
		}
		if(StringUtils.isBlank(answer)){
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_223, ResponseConstants.CommonMessage.RESPONSE_MSG_223);
		}
		try {
			int result = passwordManager.checkAccountQuestionAnswer(userName, Long.valueOf(qid), answer);
			if(result == 1){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_200, ResponseConstants.CommonMessage.RESPONSE_MSG_200);
			}else if(result==2){
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_228, ResponseConstants.CommonMessage.RESPONSE_MSG_228);
			}else{
				return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_229, ResponseConstants.CommonMessage.RESPONSE_MSG_229);
			}
		} catch (Exception e) {
			logger.error("checkAccountQuestionAnswer error:"+e.getMessage());
			return Response.response(ResponseConstants.CommonCode.NOT_DEFINE_CODE, e.getMessage());
		}
	}
		


	//*************************************************Account info******************************

	@POST
	@Path("/modifyAddress")
	public Response modifyAddress(Map<String, Object> paramMap) {	
		String value = (String) paramMap.get("value");
		//TODO JUnit Test not get aid
		Long aid = p2pSessionContext.getCurrentAid();
		if(aid == null){
			return Response.noLoginResponse();
		}
		//Long aid = 1L;
		try {
			accountInfoManager.updateAccountAddress(value, aid);
		} catch (Exception e) {
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_232, ResponseConstants.CommonMessage.RESPONSE_MSG_232);
		}
		return Response.successResponse();
	}

	@POST
	@Path("/modifyEducationDegree")
	public Response modifyEducationDegree(Map<String, Object> paramMap) {	
		String value = (String) paramMap.get("value");
		//TODO JUnit Test not get aid
		Long aid = p2pSessionContext.getCurrentAid();
		if(aid == null){
			return Response.noLoginResponse();
		}
		//Long aid = 1L;
		try {
			accountInfoManager.updateAccountEducationDegree(value, aid);
		} catch (Exception e) {
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_233, ResponseConstants.CommonMessage.RESPONSE_MSG_233);
		}
		return Response.successResponse();
	}

	@POST
	@Path("/modifyIncome")
	public Response modifyIncome(Map<String, Object> paramMap) {
		String value = (String) paramMap.get("value");
		//TODO JUnit Test not get aid
		Long aid = p2pSessionContext.getCurrentAid();
		if(aid == null){
			return Response.noLoginResponse();
		}
		//Long aid = 1L;
		try {
			accountInfoManager.updateAccountIncome(value, aid);
		} catch (Exception e) {
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_234, ResponseConstants.CommonMessage.RESPONSE_MSG_234);
		}
		return Response.successResponse();
	}
	
	@POST
	@Path("/logout")
	public void logout(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}

		SecurityContext context = SecurityContextHolder.getContext();
		if(context != null){
			context.setAuthentication(null);
		}
		SecurityContextHolder.clearContext();
	}

	@POST
	@Path("/modifyIndustry")
	public Response modifyIndustry(Map<String, Object> paramMap) {
		String value = (String) paramMap.get("value");
		//TODO JUnit Test not get aid
		Long aid = p2pSessionContext.getCurrentAid();
		if(aid == null){
			return Response.noLoginResponse();
		}
		//Long aid = 1L;
		try {
			accountInfoManager.updateAccountIndustry(value, aid);
		} catch (Exception e) {
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_235, ResponseConstants.CommonMessage.RESPONSE_MSG_235);
		}
		return Response.successResponse();
	}
	
	
	@POST
	@Path("/modifyProvinceAndCity")
	public Response modifyProvinceAndCity(Map<String, Object> paramMap) {
		String provinceCode  = (String) paramMap.get("provinceCode");
		String cityCode  = (String) paramMap.get("cityCode");
		//TODO JUnit Test not get aid
		Long aid = p2pSessionContext.getCurrentAid();
		if(aid == null){
			return Response.noLoginResponse();
		}
		//Long aid = 1L;
		try {
			accountInfoManager.updateAccountProvinceAndCity(aid, provinceCode, cityCode);
		} catch (Exception e) {
			return Response.response(ResponseConstants.CommonCode.RESPONSE_CODE_241, ResponseConstants.CommonMessage.RESPONSE_MSG_241);
		}
		return Response.successResponse();
	}


	//*************************************************Test******************************
	@GET
	@Path("/login3")
	@Produces(MediaType.APPLICATION_JSON+ ";charset=UTF-8")
	public Response login3(@Context Request request){
		logger.debug("logger-----------------------------------------------");
		logger2.debug("logger2-----------------------------------------------");
		List<Teacher> teachers = new ArrayList<Teacher>();
		for (int i = 0; i < 10; i++) {
			teachers.add(new Teacher(10L, "MISS GAO", 23+i));
		}
		return Response.successResponse(teachers) ;
	}

	@Autowired
	private TestDao testdao;
	@GET
	@Path("/login4")
	@Produces(MediaType.APPLICATION_JSON+ ";charset=UTF-8")
	public Teacher login4() throws Exception{
		testdao.test();
		System.out.println(p2pSessionContext.getAttribute("aid"));
		return new Teacher(10L, "MISS GAO", 23);
	}

}
