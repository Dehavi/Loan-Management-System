package entity.model;

import java.math.BigDecimal;

public class loan {
	private int loanId;
    private Customer customer;
    private double principalAmount;
    private double interestRate;
    public int getLoanId() {
		return loanId;
	}
	public void setLoanId(int loanId) {
		this.loanId = loanId;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public double getPrincipalAmount() {
		return principalAmount;
	}
	
	public double getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}
	public int getLoanTerm() {
		return loanTerm;
	}
	public void setLoanTerm(int loanTerm) {
		this.loanTerm = loanTerm;
	}
	public String getLoanType() {
		return loanType;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
	public String getLoanStatus() {
		return loanStatus;
	}
	public void setLoanStatus(String loanStatus) {
		this.loanStatus = loanStatus;
	}
	private int loanTerm;
    private String loanType;
    private String loanStatus;
	public void setprincipalAmount(double principalAmount) {
		this.principalAmount=principalAmount;
		
	}
	public void setCustomerId(int customerId) {
		this.customer=customer;
		
	}
    

}

 class HomeLoan extends loan {
    private String propertyAddress;
    private int propertyValue;
	public String getPropertyAddress() {
		return propertyAddress;
	}
	public void setPropertyAddress(String propertyAddress) {
		this.propertyAddress = propertyAddress;
	}
	public int getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(int propertyValue) {
		this.propertyValue = propertyValue;
	}
	public HomeLoan() {
		super();
		// TODO Auto-generated constructor stub
	}

}
 class CarLoan extends loan {
    private String carModel;
    private int carValue;
	public String getCarModel() {
		return carModel;
	}
	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}
	public int getCarValue() {
		return carValue;
	}
	public void setCarValue(int carValue) {
		this.carValue = carValue;
	}
	public CarLoan() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CarLoan(String carModel, int carValue) {
		super();
		this.carModel = carModel;
		this.carValue = carValue;
	}

}
