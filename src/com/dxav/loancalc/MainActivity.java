package com.dxav.loancalc;


import com.dxav.loancalc.Loan;
import com.dxav.loancalc.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static Intent intent = null;
	
	public static final String EXTRA_BORROWED_CAPITAL = "com.dxav.loancalculator.BORROWED_CAPITAL";
	public static final String EXTRA_ANNUAL_RATE_PERCENTAGE = "com.dxav.loancalculator.ANNUAL_RATE_PERCENTAGE";
	public static final String EXTRA_NUMBER_OF_INSTALLMENTS = "com.dxav.loancalculator.NUMBER_OF_INSTALLMENTS";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	public void startComputation(View v) {

		// Create an intent
		intent = new Intent(this, DisplayInstallmentsTableActivity.class);
		
		// Get the borrowed capital
		EditText borrowedCapital = (EditText) findViewById(R.id.borrowedCapital);
		String capitalStr = borrowedCapital.getText().toString();
		float capital = 0;
		try {
			capital = Float.valueOf(capitalStr);
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
		intent.putExtra(EXTRA_BORROWED_CAPITAL, capital);
		
		
		// Get the interest rate (in percentage)
		EditText interestRate = (EditText) findViewById(R.id.interestRate);
		String annualRatePercentageStr = interestRate.getText().toString();
		float annualRatePercentage = 0;
		try {
			annualRatePercentage = Float.valueOf(annualRatePercentageStr);
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
		intent.putExtra(EXTRA_ANNUAL_RATE_PERCENTAGE, annualRatePercentage);

		// Get the number of installments
		EditText numberOfInstallments = (EditText) findViewById(R.id.numberOfInstallments);
		String numberOfInstallmentsStr = numberOfInstallments.getText()
				.toString();
		int nInstallments = 1;
		try {
			nInstallments = Integer.valueOf(numberOfInstallmentsStr);
		} catch (NumberFormatException e) {
			// If the rate has not been entered
			// Raise the helper message read from the ressources strings
			Toast.makeText(
					MainActivity.this,
					getResources().getString(
							R.string.number_of_installments_error_message),
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return;
		}
		// Add it to the intent
		intent.putExtra(EXTRA_NUMBER_OF_INSTALLMENTS, nInstallments);
		
		// Create the loan object
		Loan loan = new Loan(capital, annualRatePercentage, nInstallments);
		// Compute the amounts of the monthly installments
		float amount = loan.getInstallmentAmount(1);

		// Write the result into the amountOfInstallments TextView
		TextView amountOfInstallments = (TextView) findViewById(R.id.amountOfInstallments);
		amountOfInstallments.setText(String.format("%.2f", amount));

		// Compute the cost of the loan
		float cost = loan.computeLoanCost();
		
		// Write the result into the amountOfInstallments TextView
		TextView loanCost = (TextView) findViewById(R.id. loanCost);
		loanCost.setText(String.format("%.2f", cost));
		
		
		// Make visible the loan table button
		Button displayLoanTableButton = (Button) findViewById(R.id.displayInstallmentsTableButton);
		displayLoanTableButton.setVisibility(0);
	}
	
	public void displayInstallmentsTable(View v) {
		// Update the computation
		startComputation(v);
		// Start the activity
		startActivity(intent);
	}
}
