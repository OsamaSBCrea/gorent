package com.example.gorent.util.network;

public interface AsyncTaskCommunicator {

    void onPreTaskExecute();

    void onPostTaskExecute(String result);

    void onTaskError();

    void onTaskSuccess();

}
