package com.example.gorent.util.shared;

import android.content.Context;
import android.widget.Toast;

public class ToastMaker {

    private ToastMaker() {}

    public static void showLongToast(Context context, int messageResId) {
        Toast errorToast = Toast.makeText(context, messageResId, Toast.LENGTH_LONG);
        errorToast.show();
    }

    public static void showLongToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showShortToast(Context context, int messageResId) {
        Toast errorToast = Toast.makeText(context, messageResId, Toast.LENGTH_SHORT);
        errorToast.show();
    }

}
