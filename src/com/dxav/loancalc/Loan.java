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
	
	public Loan(float capital, float annualInterestRatePercentage, int numberOfInsatllments)
	{
		capital_m = capital;
		monthlyRate_m = (float) (0.01 * annualInterestRatePercentage / 12);
		numberOfInsatllments_m = numberOfInsatllments;
	}
	
	public float computeInstallmentAmount() {
		// Compute the amounts of the monthly installments
		return (float) ((capital_m * monthlyRate_m) / (1 - Math.pow(1 + monthlyRate_m, -numberOfInsatllments_m)));
	}
	
	public float computeLoanCost() {
		// Compute the amounts of the monthly installments
		float installmentAmount = computeInstallmentAmount();
		// Compute the total cost of the loan
		return (float) (numberOfInsatllments_m * installmentAmount - capital_m);
	}
	
}
