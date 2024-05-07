package dao;


	import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import dao.*;
import entity.model.loan;
import exception.InvalidLoanException;
import util.DBConnUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class loanimp implements ILoanRepository {
    private Connection conn;
    private static final double ANNUAL_INTEREST_RATE = 0.1; // 10% annual interest rate

    public loanimp() {
        conn = DBConnUtil.getConnection(); // Initialize the connection in the constructor
    }

    @Override
    public void applyLoan(loan loan) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you confirm to apply for the loan? (Yes/No)");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("Yes")) {
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Loan (loanId, loanAmount, status) VALUES (?, ?, ?)")) {
                stmt.setInt(1, loan.getLoanId());
                stmt.setBigDecimal(2, BigDecimal.valueOf(loan.getPrincipalAmount()));
                stmt.setString(3, "Pending");
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Loan applied successfully.");
                } else {
                    System.out.println("Failed to apply loan.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Loan application cancelled.");
        }
    }

    @Override
    public double calculateInterest(int loanId) throws InvalidLoanException {
        // Fetch loan details from the database
        loan loan = getLoanFromDatabase(loanId);
        if (loan == null) {
            throw new InvalidLoanException("Loan not found with ID: " + loanId);
        }
        // Calculate interest
        double interest = (loan.getPrincipalAmount() * ANNUAL_INTEREST_RATE * loan.getLoanTerm()) / 12;
        return interest;
    }

    @Override
    public String loanStatus(int loanId) throws InvalidLoanException {
        // Fetch loan details from the database
        loan loan = getLoanFromDatabase(loanId);
        if (loan == null) {
            throw new InvalidLoanException("Loan not found with ID: " + loanId);
        }
        // Assume credit score check and decision logic here
        if (loan.getLoanStatus().equalsIgnoreCase("Pending")) {
            return "Loan pending approval";
        } else if (loan.getLoanStatus().equalsIgnoreCase("Approved")) {
            return "Loan approved";
        } else if (loan.getLoanStatus().equalsIgnoreCase("Rejected")) {
            return "Loan rejected";
        } else {
            return "Unknown loan status";
        }
    }

    @Override
    public double calculateEMI(int loanId) throws InvalidLoanException {
        // Fetch loan details from the database
        loan loan = getLoanFromDatabase(loanId);
        if (loan == null) {
            throw new InvalidLoanException("Loan not found with ID: " + loanId);
        }
        // Calculate EMI
        double monthlyInterestRate = loan.getInterestRate() / 12;
        double emi = (loan.getPrincipalAmount() * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, loan.getLoanTerm()))
                / (Math.pow(1 + monthlyInterestRate, loan.getLoanTerm()) - 1);
        return emi;
    }

    @Override
    public void loanRepayment(int loanId, double amount) throws InvalidLoanException {
        // Implement  logic
        loan loan = getLoanFromDatabase(loanId);
        if (loan == null) {
            throw new InvalidLoanException("Loan not found with ID: " + loanId);
        }
        double emi = calculateEMI(loanId);
        int noOfEmi = (int) (amount / emi);
        if (amount < emi) {
            System.out.println("Repayment amount is less than the EMI. Payment rejected.");
        } else {
            System.out.println("Number of EMIs paid: " + noOfEmi);
            // Update database or loan object with remaining details
        }
    }


    @Override
    public void getAllLoan() {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Loans")) {
            ResultSet rs = stmt.executeQuery();
            System.out.println("All Loans:");
            System.out.println("LoanID\tCustomerID\tPrincipalAmount\tInterestRate\tLoanTerm\tLoanType\tLoanStatus");
            while (rs.next()) {
                int loanId = rs.getInt("LoanID");
                int customerId = rs.getInt("CustomerID");
                BigDecimal principalAmount = rs.getBigDecimal("PrincipalAmount");
                double interestRate = rs.getDouble("InterestRate");
                int loanTerm = rs.getInt("LoanTerm");
                String loanType = rs.getString("LoanType");
                String loanStatus = rs.getString("LoanStatus");

                System.out.println(loanId + "\t" + customerId + "\t\t" + principalAmount + "\t\t" + interestRate + "\t\t" + loanTerm + "\t\t" + loanType + "\t\t" + loanStatus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getLoanById(int loanId) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Loans WHERE LoanID = ?")) {
            stmt.setInt(1, loanId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int customerId = rs.getInt("CustomerID");
                BigDecimal principalAmount = rs.getBigDecimal("PrincipalAmount");
                double interestRate = rs.getDouble("InterestRate");
                int loanTerm = rs.getInt("LoanTerm");
                String loanType = rs.getString("LoanType");
                String loanStatus = rs.getString("LoanStatus");

                System.out.println("LoanID: " + loanId);
                System.out.println("CustomerID: " + customerId);
                System.out.println("PrincipalAmount: " + principalAmount);
                System.out.println("InterestRate: " + interestRate);
                System.out.println("LoanTerm: " + loanTerm);
                System.out.println("LoanType: " + loanType);
                System.out.println("LoanStatus: " + loanStatus);
            } else {
                System.out.println("Loan not found with ID: " + loanId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private loan getLoanFromDatabase(int loanId) {
        loan loan = null;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Loans WHERE LoanID = ?")) {
            stmt.setInt(1, loanId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int customerId = rs.getInt("CustomerID");
                double principalAmount = rs.getDouble("PrincipalAmount");
                double interestRate = rs.getDouble("InterestRate");
                int loanTerm = rs.getInt("LoanTerm");
                String loanType = rs.getString("LoanType");
                String loanStatus = rs.getString("LoanStatus");

                loan = new loan();
                loan.setLoanId(loanId);
                loan.setCustomerId(customerId);
                loan.setprincipalAmount(principalAmount);
                loan.setInterestRate(interestRate);
                loan.setLoanTerm(loanTerm);
                loan.setLoanType(loanType);
                loan.setLoanStatus(loanStatus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loan;
    }

    public boolean applyLoan(loan newLoan, BigDecimal loanAmount, BigDecimal interestRate, int clientId) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you confirm to apply for the loan? (Yes/No)");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("Yes")) {
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Loans (LoanID, CustomerID, PrincipalAmount, InterestRate, LoanTerm, LoanType, LoanStatus) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                stmt.setInt(1, newLoan.getLoanId());
                stmt.setInt(2, clientId);
                stmt.setBigDecimal(3, loanAmount);
                stmt.setBigDecimal(4, interestRate);
                // Assuming LoanTerm, LoanType, and LoanStatus are not provided in the parameters and need to be set separately
                stmt.setInt(5, newLoan.getLoanTerm());
                stmt.setString(6, newLoan.getLoanType());
                stmt.setString(7, newLoan.getLoanStatus());
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Loan applied successfully.");
                    return true;
                } else {
                    System.out.println("Failed to apply loan.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Loan Application cancelled.");
        }
        return false;
    }
    public Collection<loan> getAllLoans() {
        Collection<loan> loans = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Loans")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int loanId = rs.getInt("LoanID");
                int customerId = rs.getInt("CustomerID");
                BigDecimal principalAmount = rs.getBigDecimal("PrincipalAmount");
                double interestRate = rs.getDouble("InterestRate");
                int loanTerm = rs.getInt("LoanTerm");
                String loanType = rs.getString("LoanType");
                String loanStatus = rs.getString("LoanStatus");

                loan loan = new loan();
                loan.setLoanId(loanId);
                loan.setCustomerId(customerId);
                loan.setprincipalAmount(principalAmount.doubleValue());
                loan.setInterestRate(interestRate);
                loan.setLoanTerm(loanTerm);
                loan.setLoanType(loanType);
                loan.setLoanStatus(loanStatus);
                // Assuming you have a method to set the Customer object
                // loan.setCustomer(customer); 

                loans.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }
    public loan getLoan(int loanId) {
	    loan loan = null;
	    try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Loans WHERE LoanID = ?")) {
	        stmt.setInt(1, loanId);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            int customerId = rs.getInt("CustomerID");
	            BigDecimal principalAmount = rs.getBigDecimal("PrincipalAmount");
	            double interestRate = rs.getDouble("InterestRate");
	            int loanTerm = rs.getInt("LoanTerm");
	            String loanType = rs.getString("LoanType");
	            String loanStatus = rs.getString("LoanStatus");

	            loan = new loan();
	            loan.setLoanId(loanId);
	            loan.setCustomerId(customerId);
	            loan.setprincipalAmount(principalAmount.doubleValue());
	            loan.setInterestRate(interestRate);
	            loan.setLoanTerm(loanTerm);
	            loan.setLoanType(loanType);
	            loan.setLoanStatus(loanStatus);
	            // Assuming you have a method to set the Customer object
	            // loan.setCustomer(customer); 
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return loan;
	}

    public boolean repayLoan(int loanId, BigDecimal repaymentAmount) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Loans WHERE LoanID = ?")) {
            stmt.setInt(1, loanId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                BigDecimal principalAmount = rs.getBigDecimal("PrincipalAmount");
                // Calculate the remaining loan amount
                BigDecimal remainingAmount = principalAmount.subtract(repaymentAmount);
                // Update the loan status based on the remaining amount
                String loanStatus = remainingAmount.compareTo(BigDecimal.ZERO) <= 0 ? "Repaid" : "Partial Repayment";
                // Update the loan record with the new loan status and remaining amount
                try (PreparedStatement updateStmt = conn.prepareStatement("UPDATE Loans SET LoanStatus = ?, PrincipalAmount = ? WHERE LoanID = ?")) {
                    updateStmt.setString(1, loanStatus);
                    updateStmt.setBigDecimal(2, remainingAmount);
                    updateStmt.setInt(3, loanId);
                    int rowsAffected = updateStmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Loan repayment processed successfully.");
                        return true;
                    } else {
                        System.out.println("Failed to process loan repayment.");
                    }
                }
            } else {
                System.out.println("Loan not found with ID: " + loanId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
}