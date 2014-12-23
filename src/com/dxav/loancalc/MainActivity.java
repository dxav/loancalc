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

import java.util.Locale;

import com.dxav.loancalc.Loan;
import com.dxav.loancalc.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String EXTRA_BORROWED_CAPITAL = "com.dxav.loancalculator.BORROWED_CAPITAL";
	public static final String EXTRA_ANNUAL_RATE_PERCENTAGE = "com.dxav.loancalculator.ANNUAL_RATE_PERCENTAGE";
	public static final String EXTRA_NUMBER_OF_INSTALLMENTS = "com.dxav.loancalculator.NUMBER_OF_INSTALLMENTS";

	private static Intent intent = null;

	private double mCapital = 0;
	private double mAnnualRatePercentage = 0;
	private int mNumberOfInstallments = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		// Restore the data saved during the previous session
		restoreData();
		
		// Fill the editText boxes with the data saved during the previous session
		setBorrowedCapitalText();
		setInterestRateText();
		setNumberOfInstallmentsText();
	}


	public void enterButtonProcess(View v) {
		// Action when the user clicks on the "Enter" button

		// Create an intent
		intent = new Intent(this, DisplayInstallmentsListActivity.class);

		// Get the borrowed capital
		String capitalStr = getBorrowedCapitalText();
		try {
			//mCapital = stringToDouble(capitalStr);
			mCapital = Double.parseDouble(capitalStr);
		} catch (NumberFormatException e) {
			// If the rate has not been entered
			// Raise the helper message read from the resources strings
			Toast.makeText(
					MainActivity.this,
					getResources().getString(
							R.string.borrowed_capital_error_message),
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return;
		}
		// Add it to the intent
		intent.putExtra(EXTRA_BORROWED_CAPITAL, mCapital);

		// Get the interest rate (in percentage)
		String annualRatePercentageStr = getInterestRateText();
		try {
			mAnnualRatePercentage = Double.parseDouble(annualRatePercentageStr);
		} catch (NumberFormatException e) {
			// If the rate has not been entered
			// Raise the helper message read from the resources strings
			Toast.makeText(
					MainActivity.this,
					getResources().getString(
							R.string.interest_rate_error_message),
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return;
		}
		// Add it to the intent
		intent.putExtra(EXTRA_ANNUAL_RATE_PERCENTAGE, mAnnualRatePercentage);

		// Get the number of installments
		String numberOfInstallmentsStr = getNumberOfInstallmentsText();
		try {
			mNumberOfInstallments = Integer.valueOf(numberOfInstallmentsStr);
		} catch (NumberFormatException e) {
			// If the rate has not been entered
			// Raise the helper message read from the resources strings
			Toast.makeText(
					MainActivity.this,
					getResources().getString(
							R.string.number_of_installments_error_message),
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return;
		}
		// Add it to the intent
		intent.putExtra(EXTRA_NUMBER_OF_INSTALLMENTS, mNumberOfInstallments);

		saveData();
		startComputation();
	}
	
	public void resetButtonProcess(View v) {
		mCapital = 0;
		mAnnualRatePercentage = 0;
		mNumberOfInstallments = 0;
		
		// Reset the editText boxes hints
		EditText borrowedCapital = (EditText) findViewById(R.id.borrowedCapital);
		borrowedCapital.setText("");
		EditText interestRate = (EditText) findViewById(R.id.interestRate);
		interestRate.setText("");
		EditText numberOfInstallments = (EditText) findViewById(R.id.numberOfInstallments);
		numberOfInstallments.setText("");

		// Reset the result of the computation
		TextView amountOfInstallments = (TextView) findViewById(R.id.amountOfInstallments);
		amountOfInstallments.setText("");
		TextView loanCost = (TextView) findViewById(R.id.loanCost);
		loanCost.setText("");
		
		// Make invisible the loan table button
		Button displayLoanTableButton = (Button) findViewById(R.id.displayInstallmentsTableButton);
		displayLoanTableButton.setVisibility(Button.INVISIBLE);
	}

	public void displayInstallmentsTable(View v) {
		// Update the computation in case the user
		// has changed something in the text boxes
		enterButtonProcess(v);
		// Start the activity
		startActivity(intent);
	}
	

	private String getBorrowedCapitalText() {
		// Get the borrowed capital
		EditText borrowedCapital = (EditText) findViewById(R.id.borrowedCapital);
		String capitalStr = borrowedCapital.getText().toString();
		return capitalStr;
	}

	private void setBorrowedCapitalText() {
		// Set the borrowed capital in the edit text
		if (mCapital > 0) {
			EditText borrowedCapital = (EditText) findViewById(R.id.borrowedCapital);
			String capitalStr = String.format(Locale.ENGLISH, "%.2f", mCapital);
			borrowedCapital.setText(capitalStr, TextView.BufferType.EDITABLE);
		}
	}

	private String getInterestRateText() {
		// Get the interest rate (in percentage)
		EditText interestRate = (EditText) findViewById(R.id.interestRate);
		String annualRatePercentageStr = interestRate.getText().toString();
		return annualRatePercentageStr;
	}

	private void setInterestRateText() {
		// Set the interest rate in the edit text
		if (mAnnualRatePercentage > 0) {
			EditText interestRate = (EditText) findViewById(R.id.interestRate);
			String annualRatePercentageStr = String.format(Locale.ENGLISH, "%.2f",
					mAnnualRatePercentage);
			interestRate.setText(annualRatePercentageStr);
		}
	}

	private String getNumberOfInstallmentsText() {
		// Get the number of installments
		EditText numberOfInstallments = (EditText) findViewById(R.id.numberOfInstallments);
		String numberOfInstallmentsStr = numberOfInstallments.getText()
				.toString();
		return numberOfInstallmentsStr;
	}

	private void setNumberOfInstallmentsText() {
		// Set the number of installments in the edit text
		if (mNumberOfInstallments > 0) {
			EditText numberOfInstallments = (EditText) findViewById(R.id.numberOfInstallments);
			String numberOfInstallmentsStr = String.valueOf(mNumberOfInstallments);
			numberOfInstallments.setText(numberOfInstallmentsStr);
		}
	}

	private void saveData() {
		// Save user data
		SharedPreferences sharedPref = getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putFloat(getString(R.string.saved_borrowed_capital), (float) mCapital);
		editor.putFloat(getString(R.string.saved_annual_percentage_rate),
				 (float) mAnnualRatePercentage);
		editor.putInt(getString(R.string.saved_number_of_installments),
				mNumberOfInstallments);
		editor.commit();
	}

	private void restoreData() {
		// Restore the data saved
		SharedPreferences sharedPref = getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		mCapital = sharedPref.getFloat(
				getString(R.string.saved_borrowed_capital), (float) 0.0);
		mAnnualRatePercentage = sharedPref.getFloat(
				getString(R.string.saved_annual_percentage_rate), (float) 0.0);
		mNumberOfInstallments = sharedPref.getInt(
				getString(R.string.saved_number_of_installments), 0);
	}

	private void startComputation() {

		// Create the loan object
		Loan loan = new Loan(mCapital, mAnnualRatePercentage, mNumberOfInstallments);
		// Compute the amounts of the monthly installments
		double amount = loan.getInstallmentAmount(1);

		// Write the result into the amountOfInstallments TextView
		TextView amountOfInstallments = (TextView) findViewById(R.id.amountOfInstallments);
		amountOfInstallments.setText(String.format(Locale.ENGLISH, "%.2f", amount));

		// Compute the cost of the loan
		double cost = loan.computeLoanCost();

		// Write the result into the amountOfInstallments TextView
		TextView loanCost = (TextView) findViewById(R.id.loanCost);
		loanCost.setText(String.format(Locale.ENGLISH, "%.2f", cost));

		// Make visible the loan table button
		Button displayLoanTableButton = (Button) findViewById(R.id.displayInstallmentsTableButton);
		displayLoanTableButton.setVisibility(Button.VISIBLE);
	}
}
