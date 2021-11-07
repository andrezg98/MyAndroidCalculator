package com.andrea.my_android_calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO: Before start using the application, please read the README.md file first.
 */

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
        if (getSupportActionBar() != null) { getSupportActionBar().hide();}

        // * Display *
        // Don't show the keyboard
        screen = findViewById(R.id.result);
        screen.setShowSoftInputOnFocus(false);

        findViewById(R.id.result).setOnClickListener(this);
        preview = findViewById(R.id.preview_calculation);

        // * Buttons *
        int[] buttonsListIDs = new int[] {
                // Numbers
                R.id.dot, R.id.zero, R.id.one, R.id.two, R.id.three,
                R.id.four, R.id.five, R.id.six , R.id.seven, R.id.eight, R.id.nine,
                // Common Operators
                R.id.add, R.id.subtract, R.id.multiply, R.id.div, R.id.root, R.id.percent,
                R.id.power, R.id.equal, R.id.m_add, R.id.clear, R.id.clear_element, R.id.mr
        };

        for (int buttonID : buttonsListIDs) {
            findViewById(buttonID).setOnClickListener(this);
        }

        int[] uniqueButtonsListIDs = new int[] {
                // Operator that only exist in layout-vertical (Vertical mode)
                R.id.signe_change,
                // Operators that only exist in layout-land (Landscape mode)
                R.id.squared, R.id.invert, R.id.pi, R.id.sin, R.id.cos,
                R.id.tan, R.id.mc, R.id.m_subtract};

        for (int uniqueButtonID : uniqueButtonsListIDs) {
            if (findViewById(uniqueButtonID) != null){
                findViewById(uniqueButtonID).setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View view) {
        // To clear the screen when I tap to it
        if (getString(R.string.screen).equals(screen.getText().toString())) {
            screen.setText("");
        }

        String btnClicked;

        try {
            btnClicked = ((Button) view).getText().toString();
        } catch (ClassCastException e) {
            Log.e("ERROR", "Bug double click.");
            return;
        }

        if ("0123456789.".contains(btnClicked)) { // A number button was clicked
            if (isWritingNumber) {
                updateScreenText(btnClicked);
                operationsComputer.setScreenNumber(Double.parseDouble(screen.getText().toString()));
                Log.d("DEBUG", String.format("IsWritingNumber, operationsComputer screenNumber: %s, screen: %s",
                                    operationsComputer.getScreenNumber(), screen.getText().toString()));
            } else {
                screen.setText(btnClicked);
                operationsComputer.setScreenNumber(Double.parseDouble(screen.getText().toString()));
                isWritingNumber = true;
                Log.d("DEBUG", String.format("NotWritingNumber, operationsComputer screenNumber: %s, screen: %s",
                        operationsComputer.getScreenNumber(), screen.getText().toString()));
            }

            operationsComputer.addOperation(btnClicked);
            preview.setText(operationsComputer.getOperationPreview());

        } else if (btnClicked.equals("CE")) {

            String clearElement;
            String clearElementPreview;

            try {
                // Checking if it's an integer number
                if (operationsComputer.getScreenNumber() % 1 == 0) {
                    clearElement = String.valueOf((int) operationsComputer.getScreenNumber());
                    operationsComputer.setScreenNumber(Double.parseDouble(clearElement.substring(0, clearElement.length() - 1)));
                    screen.setText(String.valueOf((int) operationsComputer.getScreenNumber()));
                } else {
                    clearElement = String.valueOf(operationsComputer.getScreenNumber());
                    double new_element = Double.parseDouble(clearElement.substring(0, clearElement.length() - 1));
                    operationsComputer.setScreenNumber(new_element);

                    // Checking if it's an integer number
                    if (operationsComputer.getScreenNumber() % 1 == 0) {
                        screen.setText(String.valueOf((int) operationsComputer.getScreenNumber()));
                    } else {
                        screen.setText(String.valueOf(operationsComputer.getScreenNumber()));
                    }
                }

                clearElementPreview = operationsComputer.getOperationPreview();
                operationsComputer.setOperationPreview(clearElementPreview.substring(0, clearElementPreview.length() - 1));
                preview.setText(operationsComputer.getOperationPreview());

                Log.d("DEBUG", String.format("CE:  %s, screenNumber: %s, screen: %s",
                        clearElement, operationsComputer.getScreenNumber(), screen.getText().toString()));

            } catch (NumberFormatException e){
                // Case when no more input to erase
                Log.e("ERROR", "Bug no more input to erase.");
                screen.setText("");
                preview.setText("");
                operationsComputer.setOperationPreview("");
            }

            isWritingNumber = true;

        } else { // A operator button was clicked
            try {
                if (isWritingNumber) {
                    operationsComputer.setScreenNumber(Double.parseDouble(screen.getText().toString()));
                    isWritingNumber = false;
                }

                operationsComputer.computeOperation(btnClicked);

                // Checking if it's an integer number
                if (operationsComputer.getResult() % 1 == 0) {
                    screen.setText(String.valueOf((int) operationsComputer.getResult()));
                } else {
                    screen.setText(String.valueOf(operationsComputer.getResult()));
                }

            } catch (NumberFormatException ignored) {
                // Case when "C" try to clear something already empty
                Log.e("ERROR", "Bug \"C\" try to clear something already empty.");
            }

            // Do not add the following operators to the preview screen
            String [] notInPreview = {"C", "MC", "M+", "M-", "+/-", "1/x", "x²"};
            List<String> notInPreviewList = new ArrayList<>(Arrays.asList(notInPreview));

            if (notInPreviewList.contains(btnClicked)) {
                operationsComputer.setOperationPreview("");
            } else if (btnClicked.equals("MR")) { // Add the value of "MR" to operationPreview
                // Checking if it's an integer number
                if (operationsComputer.getResult() % 1 == 0) {
                    operationsComputer.addOperation(String.valueOf((int) operationsComputer.getResult()));
                } else {
                    operationsComputer.addOperation(String.valueOf(operationsComputer.getResult()));
                }
            } else if (btnClicked.equals("=")) {
                operationsComputer.setOperationPreview(operationsComputer.getOperationPreview());
            } else {
                operationsComputer.addOperation(btnClicked);
            }

            preview.setText(operationsComputer.getOperationPreview());
        }

        // To set the position of the cursor always to the right
        int position = screen.getText().toString().length();
        screen.setSelection(position);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // To save the display text and the saved memory when changing the display orientation.
        outState.putDouble("SCREEN_NUMBER", operationsComputer.getScreenNumber());
        outState.putDouble("RESULT_NUMBER", operationsComputer.getResult());
        outState.putDouble("MEMORY_NUMBER", operationsComputer.getMemoryNumber());
        outState.putString("OPERATION_PREVIEW", operationsComputer.getOperationPreview());

        Log.d("DEBUG", String.format("SaveInstanceState - SCREEN_NUMBER: %s, RESULT_NUMBER: %s, " +
                        "MEMORY_NUMBER: %s, OPERATION_PREVIEW: %s",
                outState.getDouble("SCREEN_NUMBER"),
                outState.getDouble("RESULT_NUMBER"),
                outState.getDouble("MEMORY_NUMBER"),
                outState.getString("OPERATION_PREVIEW")));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // To restore the display text and the saved memory when changing the display orientation.
        operationsComputer.setScreenNumber(savedInstanceState.getDouble("SCREEN_NUMBER"));
        operationsComputer.setResult(savedInstanceState.getDouble("RESULT_NUMBER"));
        operationsComputer.setMemoryNumber(savedInstanceState.getDouble("MEMORY_NUMBER"));
        operationsComputer.setOperationPreview(savedInstanceState.getString("OPERATION_PREVIEW"));

        // Checking if it's an integer number
        if (operationsComputer.getScreenNumber() % 1 == 0) {
            screen.setText(String.valueOf((int) operationsComputer.getScreenNumber()));
        } else {
            screen.setText(String.valueOf(operationsComputer.getScreenNumber()));
        }

        preview.setText(operationsComputer.getOperationPreview());

        Log.d("DEBUG", String.format("restoreInstanceState - getScreenNumber: %s, getResult: %s, " +
                        "getMemoryNumber: %s, getOperationPreview: %s",
                operationsComputer.getScreenNumber(),
                operationsComputer.getResult(),
                operationsComputer.getMemoryNumber(),
                operationsComputer.getOperationPreview()));

        // To erase the "0" when change the orientation
        if (!screen.getText().toString().equals("0")) {
            isWritingNumber = true;
        }

        // To set the position of the cursor always to the right
        int position = screen.getText().toString().length();
        screen.setSelection(position);
    }

    // * USEFUL FUNCTIONS *

    /**
     * UpdateScreenText helps to insert a number wherever you want in the EditText.
     * @param newNumber Number to insert
     */
    private void updateScreenText(String newNumber) {
        int cursorPos = screen.getSelectionStart();

        String oldNumber = screen.getText().toString();
        String leftNumber = oldNumber.substring(0, cursorPos);
        String rightNumber = oldNumber.substring(cursorPos);

        // Update the new string text
        screen.setText(String.format("%s%s%s", leftNumber, newNumber, rightNumber));
    }
}