package com.kisielarobert.walletapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public static final int STATIC_INTEGER_VALUE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView HistoryTextView;
        HistoryTextView = (TextView) findViewById(R.id.HistoryId);
        HistoryTextView.setMovementMethod(new ScrollingMovementMethod());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTransactionActivity();
            }
        });
    }

    private void startTransactionActivity() {
        Intent transactionIntent = new Intent(this, TransactionActivity.class);
        startActivityForResult(transactionIntent, STATIC_INTEGER_VALUE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (STATIC_INTEGER_VALUE) : {
                if (resultCode == Activity.RESULT_OK) {
                    Double newValue = data.getDoubleExtra(TransactionActivity.VALUE_AMOUNT_KEY, 0);
                    TextView mainValue = (TextView) findViewById(R.id.MainValueId);
                    //oldValue change to everytime shared preferences value.
                    Double oldValue = Double.parseDouble(mainValue.getText().toString());
                    double doubleSum = newValue + oldValue;
                    String StringDoubleSum = /*"£ " +*/ Double.toString(doubleSum);
                    mainValue.setText(StringDoubleSum);
                }
                break;
            }
        }
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
