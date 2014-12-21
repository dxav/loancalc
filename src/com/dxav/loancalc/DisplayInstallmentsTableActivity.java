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

import com.dxav.loancalc.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DisplayInstallmentsTableActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_display_installments_table);
	
		Intent intent = getIntent();
		double capital = intent.getDoubleExtra(MainActivity.EXTRA_BORROWED_CAPITAL, 0);
		double annualRatePercentage = intent.getDoubleExtra(MainActivity.EXTRA_ANNUAL_RATE_PERCENTAGE, 0);
		int nInstallments = intent.getIntExtra(MainActivity.EXTRA_NUMBER_OF_INSTALLMENTS, 0);

		// Create the loan object
		Loan loan = new Loan(capital, annualRatePercentage, nInstallments);
				
		// Write the result into the amountOfInstallments TextView
		TextView amountOfInstallments = (TextView) findViewById(R.id.amountOfInstallments);
		amountOfInstallments.setText(String.format(Locale.ENGLISH, "%.2f", loan.getInstallmentAmount(1)));
		
		// Get the table layout defined in the display installments activity
		TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
		
		// Create the table
		for (int i=1; i<=nInstallments; i++){
			// Create a new row
			TableRow row = new TableRow(this);
			tableLayout.addView(row);
			
			
			// Create the new row with text views 
			TextView textViewInstallmentNumber = new TextView(this);
			TextView textViewOutstandingCapital = new TextView(this);
			TextView textViewInterest = new TextView(this);
			TextView textViewPrincipal = new TextView(this);
			TextView textViewInstallment = new TextView(this);
			
			textViewInstallmentNumber.setText(String.valueOf(i));
			textViewOutstandingCapital.setText(String.format(Locale.ENGLISH, "%.2f", loan.getOutstandingCapital(i)));
			textViewInterest.setText(String.format(Locale.ENGLISH, "%.2f", loan.computeInterestRepayment(i)));
			textViewPrincipal.setText(String.format(Locale.ENGLISH, "%.2f", loan.computePrincipalRepayment(i)));
			textViewInstallment.setText(String.format(Locale.ENGLISH, "%.2f", loan.getInstallmentAmount(i)));
			
			double scale = getResources().getDisplayMetrics().density;			
			int paddingPixel = (int) (10*scale + 0.5f);
			
			textViewInstallmentNumber.setPadding(paddingPixel, 0, paddingPixel, 0);
			textViewOutstandingCapital.setPadding(paddingPixel, 0, paddingPixel, 0);
			textViewInterest.setPadding(paddingPixel, 0, paddingPixel, 0);
			textViewPrincipal.setPadding(paddingPixel, 0, paddingPixel, 0);
			textViewInstallment.setPadding(paddingPixel, 0, paddingPixel, 0);
			
			row.addView(textViewInstallmentNumber);
			row.addView(textViewOutstandingCapital);
			row.addView(textViewInterest);
			row.addView(textViewPrincipal);
			row.addView(textViewInstallment);
		}	
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
