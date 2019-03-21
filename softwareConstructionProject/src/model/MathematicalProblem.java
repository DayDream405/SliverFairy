package model;

import definedtype.ComputeNumber;

public class MathematicalProblem {
	private String problemExpression;
	private ComputeNumber rightAnswer;
	private ComputeNumber userAnswer;
	private String user;
	private boolean right;
	public String getProblemExpression() {
		return problemExpression;
	}
	public void setProblemExpression(String problemExpression) {
		this.problemExpression = problemExpression;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
	public ComputeNumber getRightAnswer() {
		return rightAnswer;
	}
	public void setRightAnswer(ComputeNumber rightAnswer) {
		this.rightAnswer = rightAnswer;
		if(userAnswer!=null) {
			right=userAnswer.equals(rightAnswer);
		}else right=false;
	}
	public ComputeNumber getUserAnswer() {
		return userAnswer;
	}
	public void setUserAnswer(ComputeNumber userAnswer) {
		this.userAnswer = userAnswer;
		if(rightAnswer!=null) {
			right=rightAnswer.equals(userAnswer);
		}else right=false;
	}
	public boolean isRight() {
		return right;
	}
}
