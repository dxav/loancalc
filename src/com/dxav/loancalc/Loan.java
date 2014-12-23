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

	// ==============================================================
	// Members
	// ==============================================================

	// Input members
	private double mCapital;
	private double mAnnualRate;
	private int mNumberOfInsatllments;

	// Members computed
	private double mMonthlyRate;
	private double mInstallmentAmount;
	private double mOutstandingCapital[] = null;

	// ==============================================================
	// Constructor
	// ==============================================================

	/**
	 * Constructor
	 */
	public Loan(double capital, double annualInterestRatePercentage,
			int numberOfInsatllments) {
		mCapital = capital;
		mAnnualRate = annualInterestRatePercentage;
		mNumberOfInsatllments = numberOfInsatllments;

		// Compute the other members
		mMonthlyRate = computeMonthlyRate();
		mInstallmentAmount = computeInstallmentAmount();
		mOutstandingCapital = new double[mNumberOfInsatllments + 1];
		// Compute the outstanding capital for every installment
		computeOutstandingCapitalBeforeEveryInstallments();
	}

	// ==============================================================
	// Public methods
	// ==============================================================

	/**
	 * @return the amount of the installment i.
	 */
	public double getInstallmentAmount(int i) {
		if ((i <= 0) || (i > mNumberOfInsatllments)) {
			// Return in case of invalid input value
			return 0;
		} else if (i == mNumberOfInsatllments) {
			// Last installment: pay what is still due
			return getOutstandingCapital(i) + computeInterestRepayment(i);
		} else {
			if (getOutstandingCapital(i) + computeInterestRepayment(i) < mInstallmentAmount) {
				// If the normal installment amount is higher than what should be repaid,
				// return only what should be repaid
				return getOutstandingCapital(i) + computeInterestRepayment(i);
			} else {
				// Return the installment amount in normal cases
				return mInstallmentAmount;
			}
		}
	}

	/**
	 * @return the outstanding capital before installment i.
	 */
	public double getOutstandingCapital(int i) {
		if ((i <= 0) || (i > mNumberOfInsatllments)) {
			// Return in case of invalid input value
			return 0;
		} else {
			return mOutstandingCapital[i - 1];
		}
	}

	/**
	 * @return the amount of interest paid at the installment i.
	 */
	public double computeInterestRepayment(int i) {
		if ((i <= 0) || (i > mNumberOfInsatllments)) {
			// Return in case of invalid input value
			return 0;
		} else {
			// Compute the outstanding capital before installment i
			double outstandingCapital = getOutstandingCapital(i);
			// Return the interest amounts for installment i
			return roundToCent(outstandingCapital * mMonthlyRate);
		}
	}

	/**
	 * @return the principal amount repaid at installment i.
	 */
	public double computePrincipalRepayment(int i) {
		if ((i <= 0) || (i > mNumberOfInsatllments)) {
			// Return in case of invalid input value
			return 0;
		} else if (i == mNumberOfInsatllments) {
			// The principal amount repaid of the last installment
			// is the outstanding capital
			return getOutstandingCapital(i);
		} else {
			// Compute the amount of interest paid at the installment i
			double interestAmount = computeInterestRepayment(i);
			// Compute the principal amount repaid at installment i
			return roundToCent(getInstallmentAmount(i) - interestAmount);
		}
	}

	/**
	 * @return the total cost of the loan.
	 */
	public double computeLoanCost() {
		// Compute the sum of all the installments
		float sumOfInstallmentsAmount = 0;
		for (int i = 1; i <= mNumberOfInsatllments; i++) {
			sumOfInstallmentsAmount += getInstallmentAmount(i);
		}
		return sumOfInstallmentsAmount;
	}

	/**
	 * Round a value to the nearest cent.
	 * 
	 * @return the input value rounded at the nearest cent.
	 */
	public double roundToCent(double d) {
		return (double) (Math.round(d * 100) / 100.0);
	}

	/**
	 * Round a value to the upper cent.
	 * 
	 * @return the input value rounded at upper cent.
	 */
	public double floorToCent(double d) {
		return (double) (Math.floor(d * 100) / 100.0);
	}

	// ==============================================================
	// Private methods
	// ==============================================================

	/**
	 * Compute the monthly interest rate from the annual interest rate.
	 */
	private double computeMonthlyRate() {
		return (double) (0.01 * mAnnualRate / 12);
	}

	/**
	 * Compute the amounts of the monthly installments.
	 */
	private double computeInstallmentAmount() {
		if (mMonthlyRate != 0) {
			return floorToCent((mCapital * mMonthlyRate)
					/ (1 - Math.pow(1 + mMonthlyRate, -mNumberOfInsatllments)));
		} else {
			return floorToCent(mCapital/mNumberOfInsatllments);
		}
	}

	/**
	 * Compute the outstanding capital before each installment.
	 */
	private void computeOutstandingCapitalBeforeEveryInstallments() {
		for (int i = 1; i <= mNumberOfInsatllments; i++) {
			mOutstandingCapital[i - 1] = computeOutstandingCapitalBeforeInstallment(i);
		}
	}

	/**
	 * Compute the outstanding capital before installment i.
	 */
	private double computeOutstandingCapitalBeforeInstallment(int i) {
		if (i <= 1) {
			return mCapital;
		} else if (i > mNumberOfInsatllments) {
			return 0;
		} else {
			// Compute the principal amount outstanding before installment i-1
			double previousOutstandingCapital = getOutstandingCapital(i - 1);
			// Compute the principal amount repaid at installment i-1
			double previousPrincipalRepaid = computePrincipalRepayment(i - 1);
			// Return the principal amount outstanding before installment i
			return roundToCent(previousOutstandingCapital
					- previousPrincipalRepaid);
		}
	}

}
