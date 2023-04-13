package com.example.gorent;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gorent.repository.RepositoryManager;
import com.example.gorent.ui.auth.SignInActivity;
import com.example.gorent.util.network.AsyncTaskCommunicator;
import com.example.gorent.util.network.ConnectionAsyncTask;
import com.example.gorent.util.shared.SharedPreferencesKey;
import com.example.gorent.util.shared.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

// A new commit 1
public class IntroActivity extends AppCompatActivity implements AsyncTaskCommunicator {

    private static final String INIT_CONNECTION_URL =
            "http://www.mocky.io/v2/5b4e6b4e3200002c009c2a44";

    @BindView(R.id.main_logo)
    ImageView mainLogoImageView;

    @BindView(R.id.prgrss_bar_connection)
    ProgressBar connectionProgressBar;

    @BindView(R.id.txt_connection)
    TextView connectionTextView;

    @BindView(R.id.btn_try_connect)
    Button connectionTryAgainButton;

    private SharedPreferencesManager sharedPreferencesManager;

    private RepositoryManager repositoryManager;

    private List<Disposable> disposables;

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Handler mHideHandler = new Handler();
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);
        repositoryManager = RepositoryManager.getInstance(getApplicationContext());
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getApplicationContext());
        disposables = new ArrayList<>();
        mVisible = true;
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        mainLogoImageView.startAnimation(AnimationUtils.loadAnimation(IntroActivity.this, R.anim.fade));
        this.mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                connect();
            }
        }, 2000);
    }

    @Override
    protected void onPause() {
        disposables.forEach(Disposable::dispose);
        super.onPause();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void connect() {
        ConnectionAsyncTask connectionAsyncTask =
                new ConnectionAsyncTask((AsyncTaskCommunicator) IntroActivity.this);

        connectionAsyncTask.execute(INIT_CONNECTION_URL);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private void proceedToApp() {
        String rememberMe = sharedPreferencesManager.get(SharedPreferencesKey.REMEMBER_ME, "False");
        String authToken = sharedPreferencesManager.get(SharedPreferencesKey.AUTH_TOKEN, "");
        if (authToken.isEmpty() || rememberMe.equals("False")) {
            sharedPreferencesManager.remove(SharedPreferencesKey.AUTH_TOKEN);
            Intent intent = new Intent(IntroActivity.this, SignInActivity.class);
            startActivity(intent);
        } else {
            disposables.add(repositoryManager.getAuthenticationRepository().getCurrentUser()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                        sharedPreferencesManager.saveUser(result);
                        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                        startActivity(intent);
                    }, error -> {
                        error.printStackTrace();
                        Log.e("INTRO", error.getMessage());
                        Intent intent = new Intent(IntroActivity.this, SignInActivity.class);
                        startActivity(intent);
                    }));
        }
    }

    @OnClick(R.id.btn_try_connect)
    public void onTryAgainClick() {
        this.connect();
    }

    @Override
    public void onPreTaskExecute() {
        this.connectionTryAgainButton.setVisibility(View.GONE);
        this.connectionProgressBar.setVisibility(View.VISIBLE);
        this.connectionTextView.setVisibility(View.VISIBLE);
        this.connectionTextView.setText(R.string.connecting);
    }

    @Override
    public void onPostTaskExecute(String result) {
        this.connectionProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onTaskError() {
        this.connectionTextView.setVisibility(View.VISIBLE);
        Toast errorToast = Toast.makeText(IntroActivity.this,
                R.string.connection_failed_toast, Toast.LENGTH_LONG);

        errorToast.show();
        this.connectionTextView.setText(R.string.connection_failed);
        this.connectionTryAgainButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTaskSuccess() {
        this.connectionTextView.setVisibility(View.GONE);
        this.proceedToApp();
    }
}
