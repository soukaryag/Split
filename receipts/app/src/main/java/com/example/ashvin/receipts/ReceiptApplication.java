package com.example.ashvin.receipts;

import android.app.Application;
import android.content.Context;

import java.util.HashMap;

public class ReceiptApplication extends Application {
    public static ApplicationState applicationState = new ApplicationState();

    public static class ApplicationState{
        HashMap<String, Double> priceMap = new HashMap<>();
    }
}
