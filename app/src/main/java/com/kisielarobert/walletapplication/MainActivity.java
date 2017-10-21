package com.kisielarobert.walletapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    public static final int STATIC_INTEGER_VALUE = 1;
    public static final String USER_VALUE_SAVED = "SharedPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        changeHistory();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.mipmap.ic_plus_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTransactionActivity();
            }
        });

        TextView mainValue = (TextView) findViewById(R.id.MainValueId);
        android.content.SharedPreferences sharedPref = getSharedPreferences(USER_VALUE_SAVED, Context.MODE_PRIVATE);
        String sharedPrefLoad = sharedPref.getString("StringValue", "Please insert any values first");
        String StringSum = "£ " + sharedPrefLoad;
        mainValue.setText(StringSum);
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

                    Double newValueDouble = data.getDoubleExtra(TransactionActivity.VALUE_AMOUNT_KEY, 0);
                    TextView mainValue = (TextView) findViewById(R.id.MainValueId);

                    android.content.SharedPreferences loadSharedPref = getSharedPreferences(USER_VALUE_SAVED, Context.MODE_PRIVATE);
                    String sharedPrefOldValueString = loadSharedPref.getString("StringValue", "0");
                    Double oldValueDouble = Double.parseDouble(sharedPrefOldValueString);
                    double doubleSum = newValueDouble + oldValueDouble;
                    String StringDoubleSum = Double.toString(doubleSum);

                    android.content.SharedPreferences sharedPref = getSharedPreferences(USER_VALUE_SAVED, Context.MODE_PRIVATE);
                    android.content.SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("StringValue", StringDoubleSum);
                    editor.apply();
                    String StringSum = "£ " + StringDoubleSum;
                    mainValue.setText(StringSum);

                    String infoString = data.getStringExtra(TransactionActivity.ADDITIONAL_INFORMATION);
                    String historyValueAndInfo = StringSum + "      " + infoString + "\n";

                    writeToFile(historyValueAndInfo, this);

                    changeHistory();

                    Toast.makeText(this, "Value saved!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    }

    private void changeHistory(){
        TextView HistoryTextView;
        HistoryTextView = (TextView) findViewById(R.id.HistoryId);
        HistoryTextView.setMovementMethod(new ScrollingMovementMethod());
        HistoryTextView.setText(readFromFile(this));
    }

    private void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("configTest.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("configTest.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
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
