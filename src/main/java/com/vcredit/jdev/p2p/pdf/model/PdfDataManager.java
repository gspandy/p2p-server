package com.vcredit.jdev.p2p.pdf.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.vcredit.jdev.p2p.base.P2pSessionContext;
import com.vcredit.jdev.p2p.base.ResponseConstants;
import com.vcredit.jdev.p2p.base.util.DateFormatUtil;
import com.vcredit.jdev.p2p.base.util.DictionaryUtil;
import com.vcredit.jdev.p2p.entity.AccountContract;
import com.vcredit.jdev.p2p.entity.Investment;
import com.vcredit.jdev.p2p.entity.Resource;
import com.vcredit.jdev.p2p.enums.DictionaryEnum;
import com.vcredit.jdev.p2p.enums.ResourceEnum;
import com.vcredit.jdev.p2p.pdf.convert.Lender;
import com.vcredit.jdev.p2p.pdf.convert.PdfInfos;
import com.vcredit.jdev.p2p.pdf.exception.CommonDefinedException;
import com.vcredit.jdev.p2p.pdf.exception.ErrorCodeException.CommonException;
import com.vcredit.jdev.p2p.pdf.factory.ITextRendererObjectFactory;
import com.vcredit.jdev.p2p.pdf.freemarker.HtmlGenerator;
import com.vcredit.jdev.p2p.pdf.util.FtpUtils;
import com.vcredit.jdev.p2p.pdf.util.PdfUtil;
import com.vcredit.jdev.p2p.repository.AccountContractRepository;
import com.vcredit.jdev.p2p.repository.AccountRepository;
import com.vcredit.jdev.p2p.repository.InvestmentRepository;
import com.vcredit.jdev.p2p.repository.ResouceRepository;

/**
 * 
 * @author zhaopeijun
 *
 */
@Component
public class PdfDataManager {

	private static Logger logger = LoggerFactory.getLogger(PdfDataManager.class);

	@Autowired
	private AccountContractRepository accountContractRepository;

	@Autowired
	private HtmlGenerator htmlGenerator;

	@Autowired
	private PdfUtil pdfUtil;

	@Autowired
	private FtpUtils ftpUtils;

	@Autowired
	private InvestmentRepository investmentRepository;

	@Autowired
	private ResouceRepository resouceRepository;
	
	@Autowired
	private P2pSessionContext p2pSessionContext;

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private DictionaryUtil dictionaryUtil;

	/**
	 * 后台放款成功后 生成pdf
	 * 
	 * @param pdfInfos
	 * @return
	 * @throws CommonException
	 * @throws Exception
	 */
	public String generatePdf(PdfInfos pdfInfos, Boolean isBorrower) throws CommonException {

		PdfInfos infos = pdfInfos;
		
		//html版本
		String htmlTemplateNa = pdfUtil.getHtmlTemplateNa();
		String pdfPathNa = "";
		String pdfName = "";
		//数据转换
		PdfInfos convertInfos = prepareData(infos, isBorrower);
		if (isBorrower) {
			//借款人版本pdf
//			pdfName = "V" + infos.getContractNo() + "-B" + pdfUtil.PDF_SUFFIX;
			pdfName = infos.getContractNo() + "-B" + pdfUtil.PDF_SUFFIX;
			
		} else {
			//VBS版本pdf
//			pdfName = "V" + infos.getContractNo() + pdfUtil.PDF_SUFFIX;
			pdfName = infos.getContractNo() + pdfUtil.PDF_SUFFIX;
			//如果有值，借款人--投资协议（附件二）将屏蔽
			convertInfos.setIfborrower("true");
		}
		//临时路径生成pdf文件
		pdfPathNa = pdfUtil.getTempPdfPath() + pdfName;
		//
		try {
			generate(htmlTemplateNa, convertInfos, pdfPathNa);
		} catch (Exception e) {
			CommonException ce = new CommonDefinedException().PDF_CREATE_FAIL;
			ce.setDetail(e.getLocalizedMessage());
			logger.error("step[vbsPdf] 生成pdf fail，合同名："+ pdfName +",错误信息：" + e.getLocalizedMessage());
			throw ce;
		}

		return pdfName;
	}

	/**
	 * p2p前台生成pdf
	 * 
	 * @param pdfInfos
	 * @return
	 * @throws Exception
	 */
	public String generatePdf4Vester(PdfInfos pdfInfos) throws Exception {

		String htmlTemplateNa = pdfUtil.getHtmlTemplateNa();
		String pdfPathNa = "";
		String pdfName = "";
		//数据转换
		PdfInfos convertInfos = prepareData4Vester(pdfInfos);
		//投资人版本pdf
//		pdfName = "V-" + pdfInfos.getContractNo() + "-Vester" + ".pdf";
		pdfName = pdfInfos.getContractNo() + "-Vester" + ".pdf";
		//临时路径生成pdf文件
		pdfPathNa = pdfUtil.getTempPdfPath() + pdfName;
		generate(htmlTemplateNa, convertInfos, pdfPathNa);

		logger.debug("step[vesterPdf] 生成投资人pdf结束 合同编号={};",pdfInfos.getContractNo());

		return pdfName;
	}

	/**
	 * 根据合同数据，绑定模版html，生成pdf
	 * 
	 * @param htmlTemplateNa
	 * @param documentVo
	 * @param outputFile
	 * @return
	 * @throws Exception
	 */
	public void generate(String htmlTemplateNa, PdfInfos documentVo, String outputFile) throws Exception {
		Map<String, Object> variables = new HashMap<String, Object>();

		variables.put("vo", documentVo);

		//绑定html模版，生成html string
		logger.debug("step[pdf] html模版，生成html string：" + htmlTemplateNa + "处理in");
		String htmlContent = this.htmlGenerator.generateHtmlContent(htmlTemplateNa, variables);
		logger.debug("step[pdf] html模版，生成html string：" + htmlContent + "处理ok");

		//html生成pdf
		logger.debug("step[pdf] html string生成pdf in");
		this.generateHtml2Pdf(htmlContent, outputFile);
		logger.debug("step[pdf] html string生成pdf ok");
	}

	/**
	 * html生成pdf
	 * 
	 * @param htmlContent
	 * @param outputFile
	 * @throws Exception
	 */
	public void generateHtml2Pdf(String htmlContent, String outputFile) throws Exception {
		OutputStream out = null;
		ITextRenderer iTextRenderer = null;
		String fontPath = pdfUtil.getPdfPath()+"fonts/";
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(htmlContent.getBytes("UTF-8")));
			File f = new File(outputFile);
			if (f != null && !f.getParentFile().exists()) {
				f.getParentFile().mkdir();
			}
			out = new FileOutputStream(outputFile);
			
			iTextRenderer = (ITextRenderer) ITextRendererObjectFactory.getObjectPool(fontPath).borrowObject();

			try {
				iTextRenderer.setDocument(doc, null);
				iTextRenderer.layout();
				iTextRenderer.createPDF(out);

			} catch (Exception e) {
				ITextRendererObjectFactory.getObjectPool(fontPath).invalidateObject(iTextRenderer);
				iTextRenderer = null;
				logger.debug("step[pdf] html生成pdf,iTextRenderer处理时 发生异常:{}",e.getLocalizedMessage());
				throw e;
			}

		} catch (Exception e) {
			logger.error("step[pdf] html生成pdf：{},generateHtml2Pdf() 失败：{}",outputFile,e.getLocalizedMessage());
			throw e;
		} finally {
			if (out != null) {
				out.close();
			}
			if (iTextRenderer != null) {
				try {
					ITextRendererObjectFactory.getObjectPool(fontPath).returnObject(iTextRenderer);
				} catch (Exception ex) {
					logger.error("step[pdf],generateHtml2Pdf() finally失败， Cannot return object from pool.", ex);
				}
			}
		}
	}

	/**
	 * 生成合同数据转换
	 * 
	 * @param pdfInfos
	 * @param isBorrower
	 * @return
	 */
	private PdfInfos prepareData(PdfInfos pdfInfos, Boolean isBorrower) {
		logger.debug("step[vbsPdf] 生成合同数据转换in");
		//放款日期ymd
		String releaseYmd = pdfInfos.getInvestReleaseYmd();
		pdfInfos.setInvestReleaseYear(releaseYmd.split("-")[0]);
		pdfInfos.setInvestReleaseMonth(releaseYmd.split("-")[1]);
		pdfInfos.setInvestReleaseDay(releaseYmd.split("-")[2]);
		ArrayList<Lender> lenderList = (ArrayList) pdfInfos.getLenderList();
		//VBS版本
		if (!isBorrower) {
			for (Lender lender : lenderList) {
				String vName = lender.getInvesterName() + "-" + lender.getInvesterRealName() + "-" + lender.getInvesterPid();
				lender.setShowName(vName);
			}
		} else {
			//借款人版本
			for (Lender lender : lenderList) {
				String vName = lender.getInvesterName();
				lender.setShowName(vName);
			}
			//身份证mask
			pdfInfos.setBorrowerPid(pdfUtil.getMaskData(pdfInfos.getBorrowerPid(), 3, 3));
		}
		pdfInfos.setLenderList(lenderList);
		logger.debug("step[vbsPdf] 转换好的生成合同数据over，convertInfos:"+pdfInfos);
		return pdfInfos;
	}

	/**
	 * 投资人在p2p平台页面点击，动态生成
	 * 
	 * @param pdfInfos
	 * @return
	 */
	public PdfInfos prepareData4Vester(PdfInfos pdfInfos) {
		//放款日期ymd
		String releaseYmd = pdfInfos.getInvestReleaseYmd();
		pdfInfos.setInvestReleaseYear(releaseYmd.split("-")[0]);
		pdfInfos.setInvestReleaseMonth(releaseYmd.split("-")[1]);
		pdfInfos.setInvestReleaseDay(releaseYmd.split("-")[2]);
		ArrayList<Lender> lenderList = (ArrayList) pdfInfos.getLenderList();

		Long usrId = p2pSessionContext.getCurrentAid();
		String userName = "";
		if (null != usrId) {
			userName = accountRepository.findOne(usrId).getUserName();
		}

		for (Lender lender : lenderList) {
			//非投资人本人、只显示用户名；如本人，显示全部
			if (userName.equals(lender.getInvesterName())) {
				String vName = lender.getInvesterName() + "-" + lender.getInvesterRealName() + "-" + lender.getInvesterPid();
				lender.setShowName(vName);
			} else {
				lender.setShowName(lender.getInvesterName());
			}
		}
		pdfInfos.setLenderList(lenderList);

		return pdfInfos;
	}

	/**
	 * 获取合同数据
	 * 
	 * @param pdfInfos null为前台投资人生成、非null为后台放款成功后生成
	 * @return
	 * @throws CommonException
	 */
	public PdfInfos getPdfData(PdfInfos pdfInfos, String contractNum) throws CommonException {
		logger.debug("step[vbsPdf] 获取合同信息开始");
		List<AccountContract> list = new ArrayList<AccountContract>();
		if (null != pdfInfos && StringUtils.isEmpty(contractNum)) { //后台放款成功后 生成
			contractNum = pdfInfos.getContractNo();
			if (StringUtils.isBlank(contractNum)) {
				CommonException e = new CommonDefinedException().PDF_DATA_NOT_EXIST;
				logger.error("step[vbsPdf] 获取合同信息:合同号不能为空!");
				throw e;
			}
		} else if (null == pdfInfos && !StringUtils.isEmpty(contractNum)) { //前台投资人生成
			pdfInfos = new PdfInfos();
			pdfInfos.setContractNo(contractNum);
		}
		list = accountContractRepository.findByContractNumber(contractNum);
		
		if (list.isEmpty()) {
			CommonException e = new CommonDefinedException().PDF_DATA_NOT_EXIST;
			logger.error("step[vbsPdf] 获取合同信息:无法获取合同数据:contractNo," + contractNum);
			throw e;
		}

		ArrayList<Lender> lenderList = new ArrayList<Lender>();
		ArrayList<Lender> accreditLenderList = new ArrayList<Lender>();
		Map<String ,Lender> lenderMap = new HashMap<String, Lender>(); 

		for (AccountContract contract : list) {
			Lender lender = new Lender();
			lender.setInvesterName(contract.getInvesterAccountUserName());
			lender.setLendPeriod(contract.getInvestmentPeriod());
			lender.setInvestmentAmount(contract.getInvestmentAmount());
			lender.setMontyPayAmount(contract.getGatherMonthAmount());
			lender.setInvesterRealName(contract.getInvesterAccountName());
			lender.setInvesterPid(contract.getInvesterAccountPid());
			lenderList.add(lender);
			
			if(lenderMap.containsKey(lender.getInvesterPid())){
				lenderMap.put(lender.getInvesterPid(), lenderMap.get(lender.getInvesterPid()));
			}else{
				lenderMap.put(lender.getInvesterPid(), lender);
			}
		}
		
		for(String investerPid:lenderMap.keySet()){
			accreditLenderList.add(lenderMap.get(investerPid));
		}
		
		/** 投资人列表 */
		pdfInfos.setLenderList(lenderList);
		/** 授权协议投资人列表（同一个投资人只显示一次） */
		pdfInfos.setAccreditLenderList(accreditLenderList);
		AccountContract contract = list.get(0);
		/** 放款日期-yyyy-MM-dd */
		pdfInfos.setInvestReleaseYmd(DateFormatUtil.dateToString(contract.getContractCreateDate(), DateFormatUtil.YYYY_MM_DD));
		/** 借款人真实姓名 */
		pdfInfos.setBorrowerRealName(contract.getBorrowerAccountName());
		/** 借款人身份证ID */
		pdfInfos.setBorrowerPid(contract.getBorrowerAccountPid());
		/** 借款人平台用户名 */
		pdfInfos.setBorrowerUserName(contract.getBorrowerAccountUserNname());
		/** 借款人邮箱地址 */
		pdfInfos.setBorrowerEmail(contract.getBorrowerAccountEmail());
		/** 贷款用途 */
		pdfInfos.setLoanTarget(dictionaryUtil.getDicChinaMean(DictionaryEnum.T_INV.getCode() + DictionaryEnum.T_INV_INV_TARGET.getCode() + contract.getLoanTarget()));
		/** 贷款金额 */
		pdfInfos.setLoanAmount(contract.getLoanAmount().toString());
		/** 预期年利率 */
		pdfInfos.setInvestmentAnnualInterestRate(contract.getInvestmentAnnualInterestRate().multiply(new BigDecimal(100)));
		/** 贷款期限 */
		pdfInfos.setInvestmentPeriod(contract.getInvestmentPeriod());
		/** 月还款金额 */
		pdfInfos.setMontyPayAmount(contract.getPayMonthAmount());
		/** 平台管理费率 */
		pdfInfos.setP2pPayFeeRate(contract.getP2pPayFeeRate().multiply(new BigDecimal(100)));
		/** 还款日 */
		pdfInfos.setPayDay(contract.getPayDay().split("-")[2]);
		/** 投资管理费率 */
		pdfInfos.setInvestmentManageFeeRate(contract.getInvestmentProjectManagementFeeRate().multiply(new BigDecimal(100)));
		/** 平台管理费 */
		pdfInfos.setP2pPayFee(contract.getClaimPayPlanPlatformManagementFee());
		/** 罚息率 */
		pdfInfos.setJusticInterestRate(contract.getJusticInterestRate().multiply(new BigDecimal(100)));
		/** 可提前还款月 */
		if (contract.getInvestmentPeriod() == 36) {
			pdfInfos.setEarlierCleanMonth(9);
		} else {
			pdfInfos.setEarlierCleanMonth(6);
		}
		logger.debug("step[vbsPdf] 获取合同信息结束,pdfInfos:"+pdfInfos);
		return pdfInfos;
	}

	/**
	 * 新建resource，更新investment res关联
	 * 
	 * @param invId
	 * @param pdfNa
	 */
	public void savePdfResource(Long invId, String pdfNa) {
		logger.debug("step[vbsPdf] VBS版本pdf路径 更新资源表 in,传入investmentId={}",invId);
		Long resSeq = saveResouce(pdfNa);
		logger.debug("step[vbsPdf] 新增到资源表的流水号={}",resSeq);

		Investment investment = getInvestment(invId);
		logger.debug("step[vbsPdf] 获取标的记录成功，标的对象invId={}",investment.getInvestmentSequence());
		investment.setResourceSequence(Integer.parseInt((resSeq.toString())));
		investmentRepository.save(investment);
		
		logger.debug("step[vbsPdf] VBS版本pdf路径 更新资源表 over,更新到标的表成功，资源流水号{}",investment.getResourceSequence());
	}

	private Investment getInvestment(Long invId) {
		return investmentRepository.findOne(invId);
	}

	private Long saveResouce(String pdfNa) {
		Resource rs = new Resource();
		rs.setResourceCreateDate(new Date());
		rs.setResourceDescription("VBS版本合同pdf");
		rs.setResourceName(pdfNa);
		rs.setResourcePath("ftp://" + ftpUtils.FTP_SERVER + ftpUtils.IDENTITY_PATH + "/" + pdfNa);
		rs.setResourceType(5);
		rs = resouceRepository.save(rs);

		return rs.getResourceSequence();
	}
	
	/**
	 * 查找pdf是否生成成功，成功 --返回0和url两个参数 .失败--返回1和null
	 * @param number  项目ID
	 * @return
	 */
	public Map<String, Object> findResourceName(Long number){
		Map<String, Object> map = new HashMap<String, Object>();
		Resource re = findResource(number);
		if(re != null){
			map.put("exist", ResourceEnum.IS_FIND.getCode());
			map.put("url", re.getResourcePath());
		}else{
			map.put("exist", ResourceEnum.IS_NO_FIND.getCode());
			map.put("url", ResponseConstants.CommonCode.SUCCESS_CODE);
		}
		return map;
	}
	
	/**
	 * 查找项目合同编号
	 * @param number  项目ID
	 * @return
	 */
	public String getcontractNumber(Long number){
		List<AccountContract> listAccountContract = findAccountContract(number);
		if(listAccountContract.size() >0 ){
			return listAccountContract.get(0).getContractNumber();
		}
		return null;
	}
	
	private Resource findResource(Long number){
		return resouceRepository.findInvestmentID(number);
	}
	private List<AccountContract> findAccountContract(Long accountInvestmentSequence){
		return accountContractRepository.findAccountContractInvestment(accountInvestmentSequence);
	}
}
