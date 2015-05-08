package com.vcredit.jdev.p2p.base.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vcredit.jdev.p2p.entity.Dictionary;
import com.vcredit.jdev.p2p.entity.LoanTransfer;
import com.vcredit.jdev.p2p.repository.DictionaryRepository;
import com.vcredit.jdev.p2p.repository.LoanTransferRepository;

/**
 * 数据字典：通用类
 * @author 周佩
 *
 */
@Component
public class DictionaryUtil {
	@Autowired
	/**数据字典*/
	private DictionaryRepository dictionaryRepository;
	@Autowired
	/**小贷公司常量转化数据字典*/
	private LoanTransferRepository loanTransferRepository;
	
	/**通过[tab_name][col_name]+[col_cvcn]获取[col_value]*/
	private Map<String,String> dicData;
	/**通过[tab_name][col_name]+[col_value]获取[col_cvcn]*/
	private Map<String,String> dicDataChinaMean;
	
	/**通过[tab_name][col_name]+[col_cvcn]获取[col_value]*/
	private Map<String,String> dicDataSubsidiary;
	/**
	 * 初始化数据
	 */
	private void init(){
		List<Dictionary> res=dictionaryRepository.findAll();
		dicData = new HashMap<String,String>();
		dicDataChinaMean = new HashMap<String,String>();
		for(Dictionary d:res){
			dicDataChinaMean.put(d.getTableName()+d.getColumnName()+d.getColumnValue(), d.getColumnValueChineseMean());
			dicData.put(d.getTableName()+d.getColumnName()+d.getColumnValueChineseMean(), ""+d.getColumnValue());
		}
		dicDataSubsidiary = new HashMap<String,String>();
		List<LoanTransfer> res1=loanTransferRepository.findAll();
		for(LoanTransfer d:res1){
			dicDataSubsidiary.put(d.getTableName()+d.getColumnName()+d.getSourceData(), ""+d.getColumnValue());
		}
	}
	/**
	 * 通过[tab_name][col_name][col_cvcn]获取col_value
	 * @param key
	 * @return
	 */
	public String getDicValue(String key) {
		if(dicData==null||dicData.size()==0){
			init();
		}
		String result=dicDataSubsidiary.get(key);//取value
		if(result==null){
			result=dicData.get(key);
		}
		return result;
	}
	
	/**
	 * 通过[tab_name][col_name][col_value]获取col_cvcn
	 * @param key
	 * @return
	 */
	public String getDicChinaMean(String key) {
		if(dicData==null||dicData.size()==0){
			init();
		}
		return dicDataChinaMean.get(key);
	}
}
