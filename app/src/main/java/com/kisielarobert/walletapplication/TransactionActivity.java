package com.kisielarobert.walletapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        int intValue = Integer.parseInt(ValueAmount.getText().toString());
        Toast.makeText(this, "Save work. Almost\n£" + intValue, Toast.LENGTH_SHORT).show();
        Intent saveTransactionIntent = new Intent(this, MainActivity.class);
        saveTransactionIntent.putExtra(VALUE_AMOUNT_KEY, intValue);
        startActivity(saveTransactionIntent);
    }
}
