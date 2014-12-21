/**
 *    Copyright 2014 Xavier Delaunay
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.dxav.loancalc;


public class Loan {

	private double capital_m;
	private double monthlyRate_m; 
	private int numberOfInsatllments_m;
	private double installmentAmount_m;
	private double outstandingCapital_m[] = null;
	
	// Constructor
	public Loan(double capital, double annualInterestRatePercentage, int numberOfInsatllments)
	{
		capital_m = capital;
		monthlyRate_m = (double) (0.01 * annualInterestRatePercentage / 12);
		numberOfInsatllments_m = numberOfInsatllments;
		
		// Compute the amounts of the monthly installments
		installmentAmount_m = computeInstallmentAmount();
		
		// Compute the outstanding capital before installment i
		outstandingCapital_m = new double[numberOfInsatllments+1];
		for (int i=1; i<=numberOfInsatllments; i++) {
			outstandingCapital_m[i-1] = computeOutstandingCapital(i);
		}
	}
	
	// Getters
	public double getInstallmentAmount(int i) {
		if (i<numberOfInsatllments_m) {
			return installmentAmount_m;
		} else if (i==numberOfInsatllments_m) {
			return computeLastInstallmentAmount();
		} else {
			return 0;
		}
	}

	// Return the outstanding capital before installment i
	public double getOutstandingCapital(int i) {
		if ((i>0) && (i<numberOfInsatllments_m)) {
			return outstandingCapital_m[i-1];
		} else {
			return 0;
		}
	}
	
	// Compute the total cost of the loan
	public double computeLoanCost() {
		// Compute the total cost of the loan
		return roundToCent((numberOfInsatllments_m-1) * installmentAmount_m  + computeLastInstallmentAmount() - capital_m);
	}

	// Compute the amount of interest paid at the installment i
	public double computeInterestRepayment(int i) {
		if ( (i <= 0) || (i > numberOfInsatllments_m) ) {
			// Return in case of invalid input value
			return 0;
		} else {
			// Compute the outstanding capital before installment i
			double outstandingCapital = getOutstandingCapital(i);
			// Return the interest amounts for installment i
			return roundToCent(outstandingCapital * monthlyRate_m);
		}
	}
	
	// Compute the principal amount repaid at installment i
	public double computePrincipalRepayment(int i) {
		if ( (i <= 0) || (i > numberOfInsatllments_m) ) {
			// Return in case of invalid input value
			return 0;
		} else if (i == numberOfInsatllments_m) {
			// The principal amount repaid of the last installment 
			// is the outstanding capital
			return getOutstandingCapital(i);
		} else {
			// Compute the amount of interest paid at the installment i
			double interestAmount = computeInterestRepayment(i);
			// Compute the principal amount repaid at installment i
			return roundToCent(installmentAmount_m - interestAmount);
		}
	}

	// Compute the outstanding capital before installment i
	private double computeOutstandingCapital(int i) {
		if (i <= 1) {
			return capital_m;
		} else if (i>numberOfInsatllments_m) {
			return 0;
		} else {
			// Compute the principal amount outstanding before installment i-1
			double previousOutstandingCapital = getOutstandingCapital(i-1);
			// Compute the principal amount repaid at installment i-1
			double previousPrincipalRepaid = computePrincipalRepayment(i-1);			
			// Return the principal amount outstanding before installment i
			return roundToCent(previousOutstandingCapital - previousPrincipalRepaid);
		}
	}
	
	
	// Compute the amounts of the monthly installments
	private double computeInstallmentAmount() {
		// Compute the amounts of the monthly installments
		return ceilToCent((capital_m * monthlyRate_m) / (1 - Math.pow(1 + monthlyRate_m, -numberOfInsatllments_m)));
	}
	
	// Compute the amount of the last installment
	private double computeLastInstallmentAmount() {
		return roundToCent(getOutstandingCapital(numberOfInsatllments_m) + computeInterestRepayment(numberOfInsatllments_m));
	}
	
	// Round a value to the nearest cent
	private double roundToCent(double d) {
		return (double) (Math.round(d*100)/100.0);
	}
	
	// Ceil a value to the upper cent
	private double ceilToCent(double d) {
		return (double) (Math.ceil(d*100)/100.0);
	}

}
