package main;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Scanner;
import dao.loanimp;
import entity.model.loan;
import exception.*;
public class loanManagement {
    public static void main(String[] args) {
        loanimp loanService = new loanimp();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Apply Loan");
            System.out.println("2. Get All Loans");
            System.out.println("3. Get Loan");
            System.out.println("4. Loan Repayment");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    System.out.println("Enter Loan ID:");
                    int loanId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.println("Enter Loan Amount:");
                    BigDecimal loanAmount = scanner.nextBigDecimal();
                    System.out.println("Enter Interest Rate:");
                    BigDecimal interestRate = scanner.nextBigDecimal();
                    System.out.println("Enter Client ID:");
                    int clientId = scanner.nextInt();
                    loan newLoan = new loan();
                    newLoan.setLoanId(loanId);
                    boolean applied = loanService.applyLoan(newLoan, loanAmount, interestRate, clientId);
                    if (applied) {
                        System.out.println("Loan applied successfully.");
                    } else {
                        System.out.println("Failed to apply loan.");
                    }
                    break;

                case 2:
                    Collection<loan> loans = loanService.getAllLoans();
                    System.out.println("All Loans:");
                    for (loan loan : loans) {
                        System.out.println(loan);
                    }
                    break;

                case 3:
                    System.out.println("Enter Loan ID:");
                    loanId = scanner.nextInt();
                    try {
                        loan loan = loanService.getLoan(loanId);
                        if (loan != null) {
                            System.out.println(loan);
                        } else {
                            throw new InvalidLoanException("Loan not found.");
                        }
                    } catch (InvalidLoanException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    System.out.println("Enter Loan ID for repayment:");
                    loanId = scanner.nextInt();
                    System.out.println("Enter Repayment Amount:");
                    BigDecimal repaymentAmount = scanner.nextBigDecimal();
                    boolean repaid = loanService.repayLoan(loanId, repaymentAmount);
                    if (repaid) {
                        System.out.println("Loan repayment successful.");
                    } else {
                        System.out.println("Failed to repay loan.");
                    }
                    break;

                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}