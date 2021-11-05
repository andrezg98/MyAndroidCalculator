package com.example.my_android_calculator;

import android.util.Log;

import androidx.annotation.NonNull;

public class OperationsComputer {
    private double screenNumber;
    private double bufferNumber;
    private String bufferOperator;
    private double memoryNumber;
    private double result;

    // Constructor
    protected OperationsComputer() {
        // Initialize variables
        screenNumber = 0.0;
        bufferNumber = 0.0;
        bufferOperator = "";
        memoryNumber = 0.0;
    }

    protected void computeOperation(String op) {

        switch (op) {
            case "√":
                screenNumber = Math.sqrt(screenNumber);
                break;
            case "%":
                screenNumber = screenNumber / 100;
                break;
            case "+/-":
                screenNumber = screenNumber * (-1);
                break;
            case "1/x":
                if (screenNumber != 0) {
                    screenNumber = 1 / screenNumber;
                }
                break;
            case "x²":
                screenNumber = screenNumber * screenNumber;
                break;
            case "Π":
                screenNumber = screenNumber * Math.PI;
                break;
            case "C":
                screenNumber = 0.0;
                bufferNumber = 0.0;
                result = 0.0;
                bufferOperator = "";
                break;
            case "MC":
                memoryNumber = 0.0;
                break;
            case "M+":
                memoryNumber = memoryNumber + screenNumber;
                break;
            case "M-":
                memoryNumber = memoryNumber - screenNumber;
                break;
            case "MR":
                screenNumber = memoryNumber;
                break;
            default:
                computeBufferOperation();
                bufferOperator = op;
                bufferNumber = screenNumber;
                setResult(screenNumber);
                break;
        }

    }

    private void computeBufferOperation() {

        switch (bufferOperator) {
            case "+":
                screenNumber = bufferNumber + screenNumber;
                break;
            case "-":
                screenNumber = bufferNumber - screenNumber;
                break;
            case "*":
                screenNumber = bufferNumber * screenNumber;
                break;
            case "÷":
                if (screenNumber != 0) {
                    screenNumber = bufferNumber / screenNumber;
                }
                break;
            case "^":
                screenNumber = Math.pow(bufferNumber, screenNumber);
                break;
            case "SIN":
                screenNumber = Math.sin(Math.toRadians(screenNumber)); // Math.toRadians(mOperand) converts result to degrees
                break;
            case "COS":
                screenNumber = Math.cos(Math.toRadians(screenNumber)); // Math.toRadians(mOperand) converts result to degrees
                break;
            case "TAN":
                screenNumber = Math.tan(Math.toRadians(screenNumber)); // Math.toRadians(mOperand) converts result to degrees
                break;
        }

    }

    // * Setters and Getters *
    public void setScreenNumber(double number) { screenNumber = number; }
    public double getScreenNumber() { return screenNumber; }

    public void setResult(double number) { result = number; }
    public double getResult() {
        return result;
    }

    public void setMemoryNumber(double memory) {
        memoryNumber = memory;
    }
    public double getMemoryNumber() {
        return memoryNumber;
    }

    @NonNull
    public String toString() {
        return Double.toString(screenNumber);
    }
}
