package dao;
import java.util.*;
import entity.model.loan;
import exception.InvalidLoanException;
public interface ILoanRepository {
	 void applyLoan(loan loan);
	    double calculateInterest(int loanId) throws InvalidLoanException;
	    String loanStatus(int loanId) throws InvalidLoanException;
	    double calculateEMI(int loanId) throws InvalidLoanException;
	    void loanRepayment(int loanId, double amount) throws InvalidLoanException;
	    void getAllLoan();
	    void getLoanById(int loanId);
}

