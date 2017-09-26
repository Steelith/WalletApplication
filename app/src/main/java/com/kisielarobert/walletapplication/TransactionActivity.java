package com.kisielarobert.walletapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

public class TransactionActivity extends AppCompatActivity {

    public static final String VALUE_AMOUNT_KEY = "value key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
    }

    public void saveTransaction (View view){
        //get information about values and related with it additional information and close that activity
        EditText ValueAmount = (EditText) findViewById(R.id.ValueAmountId);
        Double newValue = Double.parseDouble(ValueAmount.getText().toString());
        newValue = newValue * (-1);
        //change switch state when you would like to add money
        Switch plusMinusSwitch = (Switch) findViewById(R.id.PlusMinusSwitchId);
        if (plusMinusSwitch.isChecked()) {
            newValue = newValue * (-1);
        }

        Intent saveTransactionIntent = new Intent(this, MainActivity.class);
        saveTransactionIntent.putExtra(VALUE_AMOUNT_KEY, newValue);
        setResult(Activity.RESULT_OK, saveTransactionIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
