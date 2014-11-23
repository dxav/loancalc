package com.dxav.loancalc;

import com.dxav.loancalc.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DisplayInstallmentsTableActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_installments_table);
		
		Intent intent = getIntent();
		float capital = intent.getFloatExtra(MainActivity.EXTRA_BORROWED_CAPITAL, 0);
		float annualRatePercentage = intent.getFloatExtra(MainActivity.EXTRA_ANNUAL_RATE_PERCENTAGE, 0);
		int nInstallments = intent.getIntExtra(MainActivity.EXTRA_NUMBER_OF_INSTALLMENTS, 0);

		// Create the loan object
		Loan loan = new Loan(capital, annualRatePercentage, nInstallments);
		// Compute the amounts of the monthly installments
		float amount = loan.computeInstallmentAmount();

		// Write the result into the amountOfInstallments TextView
		TextView amountOfInstallments = (TextView) findViewById(R.id.amountOfInstallments);
		amountOfInstallments.setText(String.format("%.2f", amount));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_loan_results, menu);
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
}
