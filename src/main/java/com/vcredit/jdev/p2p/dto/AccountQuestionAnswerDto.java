package com.vcredit.jdev.p2p.dto;

public class AccountQuestionAnswerDto {
	
	private Long systemQuestionSequence;
	private String answer;
	
	public Long getSystemQuestionSequence() {
		return systemQuestionSequence;
	}
	public void setSystemQuestionSequence(Long systemQuestionSequence) {
		this.systemQuestionSequence = systemQuestionSequence;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
