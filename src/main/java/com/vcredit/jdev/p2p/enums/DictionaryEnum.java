package com.vcredit.jdev.p2p.enums;

public enum DictionaryEnum {
	/*************t_inv表对应的所有常量colName*************************/
	T_INV("t_inv"),
	T_INV_T_RES_SSEQ("t_res_sseq"),
	T_INV_T_RES_SEQ("t_res_seq"),
	T_INV_T_RES_LSEQ("t_res_lseq"),
	T_INV_T_INV_SEQ("t_inv_seq"),
	T_INV_T_INV_NUM("t_inv_num"),
	T_INV_INV_TYPE("inv_type"),
	T_INV_INV_TOTAL("inv_total"),
	T_INV_INV_TARGET("inv_target"),
	T_INV_INV_STYPE("inv_stype"),
	T_INV_INV_STAT("inv_stat"),
	T_INV_INV_SRC("inv_src"),
	T_INV_INV_SMOUNT("inv_smount"),
	T_INV_INV_SDESC("inv_sdesc"),
	T_INV_INV_SDATE("inv_sdate"),
	T_INV_INV_REM("inv_rem"),
	T_INV_INV_PTYPE("inv_ptype"),
	T_INV_INV_PROCESS("inv_process"),
	T_INV_INV_PERIOD("inv_period"),
	T_INV_INV_LDATE("inv_ldate"),
	T_INV_INV_JCOUNT("inv_jcount"),
	T_INV_INV_IRATE("inv_irate"),
	T_INV_INV_FDATE("inv_fdate"),
	T_INV_INV_EDATE("inv_edate"),
	T_INV_INV_CSCORE("inv_cscore"),
	T_INV_INV_CDATE("inv_cdate"),
	T_INV_BUSS_CODE("buss_code"),
	T_INV_ACT_THIRD("act_third"),
	T_INV_LEVEL("inv_lv"), //信用
	/*************t_inv表对应的所有常量colName*************************/
	
	/*************t_act_bkcard表对应的所有常量colName*************************/
	T_ACT_BKCARD("t_act_bkcard"),
	T_ACT_BKCARD_ABC_CHAR_SET("abc_char_set"),
	T_ACT_BKCARD_ABC_CITY("abc_city"),
	T_ACT_BKCARD_ABC_DDSTAT("abc_ddstat"),
	T_ACT_BKCARD_ABC_MOBILE("abc_mobile"),
	T_ACT_BKCARD_ABC_NAME("abc_name"),
	T_ACT_BKCARD_ABC_NUM("abc_num"),
	T_ACT_BKCARD_ABC_PRI_ZONE("abc_pri_zone"),
	T_ACT_BKCARD_ABC_PROV("abc_prov"),
	T_ACT_BKCARD_ABC_TOWN("abc_town"),
	T_ACT_BKCARD_ACT_THIRD("act_third"),
	T_ACT_BKCARD_NB_ID("nb_id"),
	T_ACT_BKCARD_T_ACT_BKCARD_SEQ("t_act_bkcard_seq"),
	/*************t_act_bkcard表对应的所有常量colName*************************/
	
	/*************t_dic表对应的所有常量colName*************************/
	T_DIC("t_dic"),
	T_DIC_ACT_INV_PMFRATE("act_inv_pmfrate"),
	T_DIC_BANK_CODE("bank_code"),
	T_DIC_BANK_ENAME("bank_ename"),
	T_DIC_CLAIM_PAY_RRATE("claim_pay_rrate"),
	T_DIC_COL_CN("col_cn"),
	T_DIC_COL_CVCN("col_cvcn"),
	T_DIC_COL_NAME("col_name"),
	T_DIC_COL_VALUE("col_value"),
	T_DIC_LC_JFRATE("lc_jfrate"),
	T_DIC_P2P_CT_FRATE("p2p_ct_frate"),
	T_DIC_P2P_DRAW_FRATE("p2p_draw_frate"),
	T_DIC_P2P_EPAY_FRATE("p2p_epay_frate"),
	T_DIC_P2P_PAY_FRATE("p2p_pay_frate"),
	T_DIC_P2P_RC_FRATE("p2p_rc_frate"),
	T_DIC_REC_CDATE("rec_cdate"),
	T_DIC_REC_EDATE("rec_edate"),
	T_DIC_REC_STATUS("rec_status"),
	T_DIC_TAB_CN("tab_cn"),
	T_DIC_TAB_NAME("tab_name"),
	T_DIC_THIRD_RCRATE("third_rcrate"),
	T_DIC_THIRD_RC_FRATE("third_rc_frate"),
	T_DIC_T_DIC_SEQ("t_dic_seq"),
	/*************t_dic表对应的所有常量colName*************************/
	/*************用户获得的项目表t_act_inv对应的所有常量colName*************************/
	T_ACT_INV("t_act_inv"),
	T_ACT_INV_ACT_INV_STAT("act_inv_stat"),
	T_ACT_INV_ACT_INV_EFORM("act_inv_eform"),
	/*************用户获得的项目表t_act_inv对应的所有常量colName***********/
	/*************用户订单表t_act_order对应的所有常量colName*************************/
	T_ACT_ORDER("t_act_order"),
	T_ACT_ORDER_ORDER_STAT("order_stat"),
	/*************用户订单表t_act_order对应的所有常量colName***********/
	/*************债权还款计划t_clm_pplan对应的所有常量colName*************************/
	T_CLM_PPLAN("t_clm_pplan"),
	T_CLM_PPLAN_CLM_PP_STAT("clm_pp_stat"),
	/*************债权还款计划t_clm_pplan对应的所有常量colName***********/
	
	/*************费率对应的所有常量colName***********/
	LC_JFRATE("lc_jfrate"),
	THIRD_RC_FRATE("third_rc_frate"),
	THIRD_RCRATE("third_rcrate"),
	CLAIM_PAY_RRATE("claim_pay_rrate"),
	P2P_PAY_FRATE("p2p_pay_frate"),
	P2P_DRAW_FRATE("p2p_draw_frate"),
	P2P_EPAY_FRATE("p2p_epay_frate"),
	ACT_INV_PMFRATE("act_inv_pmfrate"),
	P2P_CT_FRATE("p2p_ct_frate"),
	P2P_RC_FRATE("p2p_rc_frate"),
	/*************费率对应的所有常量colName***********/
	
	
	/*************用户订单表t_acct对应的所有常量colName*************************/
	T_ACCT("t_acct"),
	TAC_GENDER("tac_gender"),
	TAC_STAT("tac_stat"),
	TAC_MBSTAT("tac_mbstat"),
	TAC_EMSTAT("tac_emstat"),
	TAC_IDCS("tac_idcs"),
	TAC_ICCS("tac_iccs"),
	TAC_JCS("tac_jcs"),
	TAC_ADDCS("tac_addcs"),
	TAC_SLV("tac_slv"),
	TAC_EDU_DEGREE("tac_edu_degree"),
	TAC_INCOME("tac_income"),
	TAC_MSTAT("tac_mstat"),
	TAC_HOUSE("tac_house"),
	TAC_HOUSE_LOAN("tac_house_loan"),
	TAC_CINDS("tac_cinds"),
	TAC_CSIZE("tac_csize"),
	TAC_CPT("tac_cpt"),
	TAC_LTYPE("tac_ltype"),
	TAC_ROLE("tac_role"),
	/*************用户订单表t_acct对应的所有常量colName***********/
	
	/*************贷款人基础信息和贷款业务信息表t_loan_data对应的所有常量colName***********/
	T_LOAN_DATA("t_loan_data"),
	SS_STAT("ss_stat"),
	/*************贷款人基础信息和贷款业务信息表t_loan_data对应的所有常量colName***********/
	
	;
	DictionaryEnum(String code) {
		this.code = code;
	}

	private String code;

	public String getCode() {
		return code;
	}
}
