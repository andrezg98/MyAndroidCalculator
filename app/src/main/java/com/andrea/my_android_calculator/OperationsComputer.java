package com.andrea.my_android_calculator;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

/**
 * This class compute the operations of the calculator and helps with the memory storage.
 */
public class OperationsComputer {
    private double screenNumber;
    private double result;
    private double memoryNumber;
    private double bufferNumber;
    private String bufferOperator;
    private String operationPreview;

    protected OperationsComputer() {
        screenNumber = 0.0;
        result = 0.0;
        memoryNumber = 0.0;
        bufferNumber = 0.0;
        bufferOperator = "";
        operationPreview = "";
    }

    /**
     * Performs the relevant operation with the number displayed on the screen according
     * to the operator passed as a parameter.
     * @param op operator
     */
    protected void computeOperation(String op) {
        switch (op) {
            case "%":
                screenNumber = screenNumber / 100;
                setResult(screenNumber);
                break;
            case "+/-":
                screenNumber = screenNumber * (-1);
                setResult(screenNumber);
                break;
            case "1/x":
                if (screenNumber != 0) {
                    screenNumber = 1 / screenNumber;
                }
                setResult(screenNumber);
                break;
            case "x²":
                screenNumber = screenNumber * screenNumber;
                setResult(screenNumber);
                break;
            case "Π":
                screenNumber = screenNumber * Math.PI;
                setResult(screenNumber);
                break;
            case "C":
                // This case doesn't clear the memoryNumber
                screenNumber = 0.0;
                result = 0.0;
                bufferNumber = 0.0;
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
                setResult(screenNumber);
                break;
            default:
                computeBufferOperation();
                // Update buffer
                bufferOperator = op;
                bufferNumber = screenNumber;
                setResult(screenNumber);
                break;
        }
    }

    /**
     * Waiting the next operand to calculate the final result.
     */
    @SuppressLint("DefaultLocale")
    private void computeBufferOperation() {
        switch (bufferOperator) {
            case "+":
                screenNumber = bufferNumber + screenNumber;
                break;
            case "-":
                screenNumber = bufferNumber - screenNumber;
                break;
            case "×":
                screenNumber = bufferNumber * screenNumber;
                break;
            case "÷":
                if (screenNumber != 0) {
                    screenNumber = bufferNumber / screenNumber;
                } else {
                    screenNumber = Double.POSITIVE_INFINITY;
                }
                break;
            case "√":
                screenNumber = Math.sqrt(screenNumber);
                break;
            case "^":
                screenNumber = Math.pow(bufferNumber, screenNumber);
                break;
            // Results in degrees
            case "SIN":
                screenNumber = Math.sin(Math.toRadians(screenNumber));
                break;
            case "COS":
                screenNumber = Math.cos(Math.toRadians(screenNumber));
                screenNumber = Double.parseDouble(String.format("%.6f", screenNumber));
                break;
            case "TAN":
                double sin = Math.sin(Math.toRadians(screenNumber));
                double cos = Math.cos(Math.toRadians(screenNumber));
                cos = Double.parseDouble(String.format("%.6f", cos));
                screenNumber = sin / cos;
                break;
        }
    }

    // * Setters, Getters and more *
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

    public void setOperationPreview(String operation) { operationPreview = operation; }
    public String getOperationPreview() { return operationPreview; }
    public void addOperation(String operationToAdd) { operationPreview += operationToAdd; }

    @NonNull
    public String toString() {
        return Double.toString(screenNumber);
    }
}
