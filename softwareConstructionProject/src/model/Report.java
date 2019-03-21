package model;

public class Report {
	private String userAccount;
	private int problemCount;
	private float rate;
	private String date;
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public int getProblemCount() {
		return problemCount;
	}
	public void setProblemCount(int problemCount) {
		this.problemCount = problemCount;
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
