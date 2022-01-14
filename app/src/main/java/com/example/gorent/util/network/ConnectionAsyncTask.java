package com.example.gorent.util.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class ConnectionAsyncTask extends AsyncTask<String, String, String> {

    private final AsyncTaskCommunicator asyncTaskCommunicator;

    private boolean hasError = false;

    public ConnectionAsyncTask(AsyncTaskCommunicator asyncTaskCommunicator) {
        this.asyncTaskCommunicator = asyncTaskCommunicator;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.asyncTaskCommunicator.onPreTaskExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String data = HttpManager.getData(params[0]);
            this.hasError = false;
            return data;
        } catch (IOException e) {
            Log.e("Connection", e.toString());
            this.hasError = true;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        this.asyncTaskCommunicator.onPostTaskExecute(s);

        if (this.hasError) {
            this.asyncTaskCommunicator.onTaskError();
        } else {
            this.asyncTaskCommunicator.onTaskSuccess();
        }
    }
}
