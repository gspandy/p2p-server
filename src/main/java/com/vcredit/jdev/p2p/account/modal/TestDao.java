package com.vcredit.jdev.p2p.account.modal;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Transactional(rollbackOn={Exception.class,RuntimeException.class})
	public void test() throws Exception{
			jdbcTemplate.update("INSERT INTO test (id,name)VALUES(?, ?)", new Object[] {UUID.randomUUID().toString(),"name2"});
			throw new Exception("123456");
	} 

}
