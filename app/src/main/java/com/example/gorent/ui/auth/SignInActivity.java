package com.example.gorent.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gorent.MainActivity;
import com.example.gorent.R;
import com.example.gorent.data.models.auth.LoginModel;
import com.example.gorent.repository.RepositoryManager;
import com.example.gorent.repository.api.AuthenticationRepository;
import com.example.gorent.util.shared.SharedPreferencesKey;
import com.example.gorent.util.shared.SharedPreferencesManager;
import com.example.gorent.util.shared.ToastMaker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class SignInActivity extends AppCompatActivity {

    private AuthenticationRepository authenticationRepository;

    @BindView(R.id.edit_login)
    EditText loginEditText;

    @BindView(R.id.edit_password)
    EditText passwordEditText;

    @BindView(R.id.switch_remember_me)
    SwitchCompat rememberMeSwitch;

    @BindView(R.id.temp_text_view)
    TextView textView;

    private List<Disposable> disposables;

    private SharedPreferencesManager sharedPreferencesManager;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        authenticationRepository = RepositoryManager.getInstance(getApplicationContext()).getAuthenticationRepository();
        sharedPreferencesManager = SharedPreferencesManager.getInstance(getApplicationContext());
        disposables = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private LoginModel getLoginInfo() {
        return new LoginModel(
                loginEditText.getText().toString(),
                passwordEditText.getText().toString(),
                rememberMeSwitch.isChecked()
        );
    }

    private void proceedToMainActivity() {
        sharedPreferencesManager.put(SharedPreferencesKey.REMEMBER_ME, rememberMeSwitch.isChecked() ? "True" : "False");
        disposables.add(authenticationRepository.getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    sharedPreferencesManager.saveUser(user);
                    progressDialog.hide();
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                }, error -> {
                    progressDialog.hide();
                    Log.e("GET User", error.getMessage());
                }));
    }

    @Override
    protected void onPause() {
        disposables.forEach(Disposable::dispose);
        super.onPause();
    }

    @OnClick(R.id.btn_sign_up)
    void signUp() {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_sign_in)
    void signIn() {
        progressDialog = ProgressDialog.show(SignInActivity.this, "", "Signing in. Please wait...", true);
        disposables.add(authenticationRepository.authenticate(getLoginInfo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jwtToken -> {
                    sharedPreferencesManager.put(SharedPreferencesKey.AUTH_TOKEN, jwtToken.getId_token());
                    proceedToMainActivity();
                }, error -> {
                    progressDialog.hide();
                    error.printStackTrace();
                    Log.e("LOGIN", error.toString());
                    if (error instanceof HttpException && ((HttpException) error).response().code() == 401) {
                        ToastMaker.showLongToast(SignInActivity.this, R.string.alrt_wrong_login);
                    } else {
                        ToastMaker.showLongToast(SignInActivity.this, R.string.connection_failed_toast);
                    }
                }));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}