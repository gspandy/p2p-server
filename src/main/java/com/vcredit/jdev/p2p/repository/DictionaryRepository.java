package com.vcredit.jdev.p2p.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vcredit.jdev.p2p.entity.Dictionary;

/**
 * 接口	福利
 * @author 周佩
 * 创建时间	20141212
 */
public interface DictionaryRepository extends JpaRepository<Dictionary, Long>{
	
	/**查询数据字典的基础数据*/
	List<Dictionary> findByTableNameAndColumnName(String tableName,String columnName);
	/**查询数据字典的基础数据*/
	List<Dictionary> findByTableNameAndColumnNameAndColumnValueChineseMean(String tableName,String columnName,String colvn);
	/**查询数据字典的基础数据*/
	List<Dictionary> findByTableName(String tableName);
}
