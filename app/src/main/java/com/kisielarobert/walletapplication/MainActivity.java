package com.kisielarobert.walletapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    public static final int STATIC_INTEGER_VALUE = 1;
    public static final String USER_VALUE_SAVED = "SharedPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        updateTransactions();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //icon to change size but it doesn't work
        fab.setImageDrawable(getResources().getDrawable(R.mipmap.fab_add_button));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTransactionActivity();
            }
        });

        TextView Balance = (TextView) findViewById(R.id.BalanceId);
        String sumString = loadSharedPreferences("sumString", "0");
        Balance.setText(sumString);
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
                    Double changeDouble = data.getDoubleExtra(TransactionActivity.VALUE_AMOUNT_KEY, 0);
                    String transactionInformation = data.getStringExtra(TransactionActivity.ADDITIONAL_INFORMATION);
                    TextView mainValue = (TextView) findViewById(R.id.BalanceId);
                    String changeString = Double.toString(changeDouble);
                    String balanceString = loadSharedPreferences("sumString", "0");
                    Double balanceDouble = Double.parseDouble(balanceString);

                    String sumString = Double.toString(changeDouble + balanceDouble);
                    saveSharedPreferences("sumString", sumString);
                    mainValue.setText(sumString);
                    String balanceAndTransaction = changeString + "      " + sumString + "       " + transactionInformation + "\n";

                    if (isExternalStorageWritable()) {
                        writeToFile(balanceAndTransaction);
                        updateTransactions();
                        Toast.makeText(this, "Transaction saved!", Toast.LENGTH_SHORT).show();
                    }
                } break;
            }
        }
    }

    private void updateTransactions(){
        TextView HistoryTextView;
        HistoryTextView = (TextView) findViewById(R.id.TransactionHistoryId);
        HistoryTextView.setMovementMethod(new ScrollingMovementMethod());
        HistoryTextView.setText(readFromFile(this));
    }

    private void writeToFile(String data) {
        try {
            if (data.contentEquals("clear_data")) {
                FileOutputStream fos = openFileOutput("my data.txt", Context.MODE_PRIVATE);
                fos.write("\n".getBytes());
                fos.close();
            } else {
                FileOutputStream fos = openFileOutput("my data.txt", Context.MODE_APPEND);
                fos.write(data.getBytes());
                fos.close();
            }
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {
        String ret = "";
        try {
            InputStream inputStream = context.openFileInput("my data.txt");

            if (inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                    stringBuilder.append("\n");
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

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TextView mainValue = (TextView) findViewById(R.id.BalanceId);
        int id = item.getItemId();

        if (id == R.id.action_clear) {
            saveSharedPreferences("sumString", "0");
            mainValue.setText("0");
            writeToFile("clear_data");
            TextView HistoryTextView = (TextView) findViewById(R.id.TransactionHistoryId);
            HistoryTextView.setText(R.string.no_transaction);
            Toast.makeText(this, "Data cleared", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id==R.id.day_night_mode) {
            String activeMode = loadSharedPreferences("vision mode", "day_mode");
            changeNightDayMode(activeMode);

            if (activeMode.contentEquals("day_mode")) {
                Toast.makeText(this, "Night mode selected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Day mode selected", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    private String loadSharedPreferences(String key, String defaultValue) {
        android.content.SharedPreferences loadSharedPref = getSharedPreferences(USER_VALUE_SAVED, Context.MODE_PRIVATE);
        return loadSharedPref.getString(key, defaultValue);
    }


    private void saveSharedPreferences(String key, String savedValue) {
        android.content.SharedPreferences sharedPref = getSharedPreferences(USER_VALUE_SAVED, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, savedValue);
        editor.apply();
    }

    private void changeNightDayMode(String activeMode) {
        if (activeMode.contentEquals("day_mode")) {
            //getApplication().setTheme(R.style.AppTheme);
            TextView dayNightMode = (TextView) findViewById(R.id.day_night_mode);
            TextView stateNumber = (TextView) findViewById(R.id.StateNumber);
            dayNightMode.setText(R.string.day_mode);
            stateNumber.setText(R.string.night_mode);
            saveSharedPreferences("vision mode", "night_mode");

        } else /*(activeMode.contentEquals("night_mode"))*/ {
            //getApplication().setTheme(R.style.MarineBlueDay);
            TextView dayNightMode = (TextView) findViewById(R.id.day_night_mode);
            TextView stateNumber = (TextView) findViewById(R.id.StateNumber);
            dayNightMode.setText(R.string.night_mode);
            stateNumber.setText(R.string.day_mode);
            saveSharedPreferences("vision mode", "day_mode");
        }
    }
}
