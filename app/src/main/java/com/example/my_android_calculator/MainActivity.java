package com.example.my_android_calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    EditText screen;
    TextView preview;
    OperationsComputer operationsComputer;

    private Boolean isWritingNumber = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        operationsComputer = new OperationsComputer();

        // To hide the actionBar
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Don't show the keyboard
        screen = findViewById(R.id.result);
        screen.setShowSoftInputOnFocus(false);

        // * Display *
        findViewById(R.id.result).setOnClickListener(this);
        preview = findViewById(R.id.preview_calculation);

        // * Numbers *
        findViewById(R.id.dot).setOnClickListener(this);
        findViewById(R.id.zero).setOnClickListener(this);
        findViewById(R.id.one).setOnClickListener(this);
        findViewById(R.id.two).setOnClickListener(this);
        findViewById(R.id.three).setOnClickListener(this);
        findViewById(R.id.four).setOnClickListener(this);
        findViewById(R.id.five).setOnClickListener(this);
        findViewById(R.id.six).setOnClickListener(this);
        findViewById(R.id.seven).setOnClickListener(this);
        findViewById(R.id.eight).setOnClickListener(this);
        findViewById(R.id.nine).setOnClickListener(this);

        // * Operators *
        findViewById(R.id.mas).setOnClickListener(this);
        findViewById(R.id.menos).setOnClickListener(this);
        findViewById(R.id.multiply).setOnClickListener(this);
        findViewById(R.id.div).setOnClickListener(this);
        findViewById(R.id.raiz).setOnClickListener(this);
        findViewById(R.id.percent).setOnClickListener(this);
        findViewById(R.id.potencia).setOnClickListener(this);
        findViewById(R.id.equal).setOnClickListener(this);
        findViewById(R.id.m_mas).setOnClickListener(this);
        findViewById(R.id.clear).setOnClickListener(this);
        findViewById(R.id.clear_element).setOnClickListener(this);
        findViewById(R.id.mr).setOnClickListener(this);

        // * Operators that only exist in layout-vertical (Vertical mode) *
        if (findViewById(R.id.sign_change) != null) {
            findViewById(R.id.sign_change).setOnClickListener(this);
        }

        // * Operators that only exist in layout-land (Landscape mode) *
        if (findViewById(R.id.invert) != null) {
            findViewById(R.id.invert).setOnClickListener(this);
        }
        if (findViewById(R.id.squared) != null) {
            findViewById(R.id.squared).setOnClickListener(this);
        }
        if (findViewById(R.id.pi) != null) {
            findViewById(R.id.pi).setOnClickListener(this);
        }
        if (findViewById(R.id.sin) != null) {
            findViewById(R.id.sin).setOnClickListener(this);
        }
        if (findViewById(R.id.cos) != null) {
            findViewById(R.id.cos).setOnClickListener(this);
        }
        if (findViewById(R.id.tan) != null) {
            findViewById(R.id.tan).setOnClickListener(this);
        }
        if (findViewById(R.id.mc) != null) {
            findViewById(R.id.mc).setOnClickListener(this);
        }
        if (findViewById(R.id.m_menos) != null) {
            findViewById(R.id.m_menos).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        // To clear the screen when I tap to it
        if (getString(R.string.screen).equals(screen.getText().toString())) {
            screen.setText("");
        }
        String btnPressed;
        try{
            btnPressed = ((Button) view).getText().toString();
        }catch (ClassCastException e){
            Log.e("ERROR", "Bug double click.");
            return;
        }

        if ("0123456789.".contains(btnPressed)) {
            if (isWritingNumber) {
                screen.append(btnPressed);
                operationsComputer.setScreenNumber(Double.parseDouble(screen.getText().toString()));
                Log.d("DEBUG", String.format("IsWritingNumber, operationsComputer screenNumber: %s, screen: %s",
                                    operationsComputer.getScreenNumber(), screen.getText().toString()));
            } else {
                screen.setText(btnPressed);
                operationsComputer.setScreenNumber(Double.parseDouble(screen.getText().toString()));
                isWritingNumber = true;
                Log.d("DEBUG", String.format("NotWriting, operationsComputer screenNumber: %s, screen: %s",
                        operationsComputer.getScreenNumber(), screen.getText().toString()));
            }
        } else if (btnPressed.equals("CE")) {
            String clear_element;
            try {
                if (operationsComputer.getScreenNumber() % 1 == 0) {
                    clear_element = String.valueOf((int) operationsComputer.getScreenNumber());
                    operationsComputer.setScreenNumber(Double.parseDouble(clear_element.substring(0, clear_element.length() - 1)));
                    screen.setText(String.valueOf((int) operationsComputer.getScreenNumber()));
                } else {
                    clear_element = String.valueOf(operationsComputer.getScreenNumber());
                    double new_element = Double.parseDouble(clear_element.substring(0, clear_element.length() - 1));
                    operationsComputer.setScreenNumber(new_element);
                    if (operationsComputer.getScreenNumber() % 1 == 0) {
                        screen.setText(String.valueOf((int) operationsComputer.getScreenNumber()));
                    } else {
                        screen.setText(String.valueOf(operationsComputer.getScreenNumber()));
                    }
                }
                Log.d("DEBUG", String.format("CE:  %s, screenNumber: %s, screen: %s",
                        clear_element, operationsComputer.getScreenNumber(), screen.getText().toString()));
            } catch (NumberFormatException e){
                //Case when no more input to erase
                screen.setText("");
            }

            isWritingNumber = true;
        } else {
            try {
                if (isWritingNumber) {
                    operationsComputer.setScreenNumber(Double.parseDouble(screen.getText().toString()));
                    isWritingNumber = false;
                }
                operationsComputer.computeOperation(btnPressed);
                if (operationsComputer.getResult() % 1 == 0) {
                    screen.setText(String.valueOf((int) operationsComputer.getResult()));
                } else {
                    screen.setText(String.valueOf(operationsComputer.getResult()));
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save variables on screen orientation change
        outState.putDouble("SCREEN_NUMBER", operationsComputer.getScreenNumber());
        outState.putDouble("RESULT_NUMBER", operationsComputer.getResult());
        outState.putDouble("MEMORY_NUMBER", operationsComputer.getMemoryNumber());
        Log.d("DEBUG", String.format("SaveInstanceState - SCREEN_NUMBER: %s, RESULT_NUMBER: %s, MEMORY_NUMBER: %s",
                outState.getDouble("SCREEN_NUMBER"),
                outState.getDouble("RESULT_NUMBER"),
                outState.getDouble("MEMORY_NUMBER")));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore variables on screen orientation change
        operationsComputer.setScreenNumber(savedInstanceState.getDouble("SCREEN_NUMBER"));
        operationsComputer.setResult(savedInstanceState.getDouble("RESULT_NUMBER"));
        operationsComputer.setMemoryNumber(savedInstanceState.getDouble("MEMORY_NUMBER"));
        if (operationsComputer.getScreenNumber() % 1 == 0) {
            screen.setText(String.valueOf((int) operationsComputer.getScreenNumber()));
        } else {
            screen.setText(String.valueOf(operationsComputer.getScreenNumber()));
        }
        Log.d("DEBUG", String.format("restoreInstanceState - getScreenNumber: %s, getResult: %s, getMemoryNumber: %s",
                operationsComputer.getScreenNumber(),
                operationsComputer.getResult(),
                operationsComputer.getMemoryNumber()));
    }

    // --- USEFUL FUNCTIONS ---

}