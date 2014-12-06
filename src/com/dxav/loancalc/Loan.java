/**
 * 
 */
package com.dxav.loancalc;

/**
 * @author xavier
 *
 */
public class Loan {

	private float capital_m;
	private float monthlyRate_m; 
	private int numberOfInsatllments_m;
	private float installmentAmount_m;
	private float outstandingCapital_m[] = null;
	
	// Constructor
	public Loan(float capital, float annualInterestRatePercentage, int numberOfInsatllments)
	{
		capital_m = capital;
		monthlyRate_m = (float) (0.01 * annualInterestRatePercentage / 12);
		numberOfInsatllments_m = numberOfInsatllments;
		
		// Compute the amounts of the monthly installments
		installmentAmount_m = computeInstallmentAmount();
		
		// Compute the outstanding capital before installment i
		outstandingCapital_m = new float[numberOfInsatllments+1];
		for (int i=1; i<=numberOfInsatllments; i++) {
			outstandingCapital_m[i-1] = computeOutstandingCapital(i);
		}
	}
	
	// Getters
	public float getInstallmentAmount(int i) {
		if (i<numberOfInsatllments_m) {
			return installmentAmount_m;
		} else if (i==numberOfInsatllments_m) {
			return computeLastInstallmentAmount();
		} else {
			return 0;
		}
	}

	// Return the outstanding capital before installment i
	public float getOutstandingCapital(int i) {
		return outstandingCapital_m[i-1];
	}
	
	// Compute the total cost of the loan
	public float computeLoanCost() {
		// Compute the total cost of the loan
		return roundToCent((numberOfInsatllments_m-1) * installmentAmount_m  + computeLastInstallmentAmount() - capital_m);
	}

	// Compute the amount of interest paid at the installment i
	public float computeInterestRepayment(int i) {
		if ( (i <= 0) || (i > numberOfInsatllments_m) ) {
			// Return in case of invalid input value
			return 0;
		} else {
			// Compute the outstanding capital before installment i
			float outstandingCapital = getOutstandingCapital(i);
			// Return the interest amounts for installment i
			return roundToCent(outstandingCapital * monthlyRate_m);
		}
	}
	
	// Compute the principal amount repaid at installment i
	public float computePrincipalRepayment(int i) {
		if ( (i <= 0) || (i > numberOfInsatllments_m) ) {
			// Return in case of invalid input value
			return 0;
		} else if (i == numberOfInsatllments_m) {
			// The principal amount repaid of the last installment 
			// is the outstanding capital
			return getOutstandingCapital(i);
		} else {
			// Compute the amount of interest paid at the installment i
			float interestAmount = computeInterestRepayment(i);
			// Compute the principal amount repaid at installment i
			return roundToCent(installmentAmount_m - interestAmount);
		}
	}

	// Compute the outstanding capital before installment i
	private float computeOutstandingCapital(int i) {
		if (i <= 1) {
			return capital_m;
		} else if (i>numberOfInsatllments_m) {
			return 0;
		} else {
			// Compute the principal amount outstanding before installment i-1
			float previousOutstandingCapital = getOutstandingCapital(i-1);
			// Compute the principal amount repaid at installment i-1
			float previousPrincipalRepaid = computePrincipalRepayment(i-1);			
			// Return the principal amount outstanding before installment i
			return roundToCent(previousOutstandingCapital - previousPrincipalRepaid);
		}
	}
	
	
	// Compute the amounts of the monthly installments
	private float computeInstallmentAmount() {
		// Compute the amounts of the monthly installments
		return ceilToCent((capital_m * monthlyRate_m) / (1 - Math.pow(1 + monthlyRate_m, -numberOfInsatllments_m)));
	}
	
	// Compute the amount of the last installment
	private float computeLastInstallmentAmount() {
		return roundToCent(getOutstandingCapital(numberOfInsatllments_m) + computeInterestRepayment(numberOfInsatllments_m));
	}
	
	// Round a value to the nearest cent
	private float roundToCent(double d) {
		return (float) (Math.round(d*100)/100.0);
	}
	
	// Ceil a value to the upper cent
	private float ceilToCent(double d) {
		return (float) (Math.ceil(d*100)/100.0);
	}

}
