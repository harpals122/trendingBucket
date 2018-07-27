package com.trending.trending_bucket.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;

import com.trending.trending.R;
import com.trending.trending_bucket.model.User;
import com.trending.trending_bucket.util.FloatLabel;
import com.trending.trending_bucket.util.GetUserCallBack;
import com.trending.trending_bucket.util.ServerRequest;
import com.trending.trending_bucket.util.utilMethods;


import static com.trending.trending_bucket.util.Validator.isInputted;
import static com.trending.trending_bucket.util.constants.JF_CONTACT_NUMBER;
import static com.trending.trending_bucket.util.constants.JF_EMAIL;
import static com.trending.trending_bucket.util.constants.JF_FNAME;
import static com.trending.trending_bucket.util.constants.JF_LNAME;
import static com.trending.trending_bucket.util.constants.JF_ID;
import static com.trending.trending_bucket.util.utilMethods.InternetConnectionListener;
import static com.trending.trending_bucket.util.utilMethods.hideSoftKeyboard;
import static com.trending.trending_bucket.util.utilMethods.isConnectedToInternet;
import static com.trending.trending_bucket.util.utilMethods.savePreference;
import static com.trending.trending_bucket.util.utilMethods.setUserLoggedIn;



//Todo: Make database check connection and create Session
/**
 * @author prabh kaur
 *  LoginActivity
 *  Responsible for making user logged in
 */

public class LoginActivity extends Activity implements View.OnClickListener, View.OnTouchListener, InternetConnectionListener {

    private final int SIGNED_IN_ACTION = 1;
    private FloatLabel etMobileNumber;
    private FloatLabel etPassword;
    private InternetConnectionListener internetConnectionListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.btnSignIn).setOnClickListener(this);
        findViewById(R.id.crossImgView).setOnClickListener(this);
        findViewById(R.id.btnNewUserTV).setOnClickListener(this);
        findViewById(R.id.showPasswordImg).setOnTouchListener(this);
        findViewById(R.id.btnForgotPasswordTV).setOnClickListener(this);
        etMobileNumber = (FloatLabel) findViewById(R.id.etMobileNumber);
        etPassword = (FloatLabel) findViewById(R.id.etPassword);
        etPassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());
        etMobileNumber.getEditText();
    }

    @Override
    public void onClick(View v)
    {
         switch (v.getId())
         {
            case R.id.crossImgView:
                hideSoftKeyboard(this);
                Intent intent = new Intent(this, LandingActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                onPause();
                break;

            case R.id.btnSignIn:
                if (isInputValid()) {
                    if (isConnectedToInternet(LoginActivity.this)) {
                        doLoginRequest(etMobileNumber.getEditText().getText().toString(),
                                etPassword.getEditText().getText().toString());

                    }
                    else {
                        internetConnectionListener = LoginActivity.this;
                        utilMethods.showNoInternetDialog(LoginActivity.this, internetConnectionListener, getResources().getString(R.string.no_internet),
                                getResources().getString(R.string.no_internet_text),
                                getResources().getString(R.string.retry_string),
                                getResources().getString(R.string.cancel_string), SIGNED_IN_ACTION);
                    }
                }
                break;

            case R.id.btnNewUserTV:
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                finish();
                break;
            case R.id.btnForgotPasswordTV:
                startActivity(new Intent(LoginActivity.this, ForgetPassword.class));
                break;
         }
    }

    private void doLoginRequest(String phone, String password) {
        User user = new User();
        user.setEmail(phone);
        user.setPassword(password);
        authenticate(user);
    }

    private void authenticate(User user){
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.fetchUserDataInBackground(user, new GetUserCallBack() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                } else {

                    logUserIn(returnedUser);
                }
            }
        });
    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(LoginActivity.this);
        dialogbuilder.setMessage("Incorrect user details");
        dialogbuilder.setPositiveButton("ok", null);
        dialogbuilder.show();
    }

    private void logUserIn(User returnedUser){
        savePreference(LoginActivity.this, JF_ID, returnedUser.getId());
        savePreference(LoginActivity.this, JF_CONTACT_NUMBER, returnedUser.getPhoneNumber());
        savePreference(LoginActivity.this, JF_FNAME, returnedUser.getFname());
        savePreference(LoginActivity.this, JF_LNAME, returnedUser.getLname());
        savePreference(LoginActivity.this, JF_EMAIL, returnedUser.getEmail());


        setUserLoggedIn(LoginActivity.this, true);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!TextUtils.isEmpty(etPassword.getEditText().getText())) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    etPassword.getEditText().setTransformationMethod(null);
                    etPassword.getEditText().setSelection(etPassword.getEditText().getText().length());
                    break;

                case MotionEvent.ACTION_UP:
                    etPassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());
                    etPassword.getEditText().setSelection(etPassword.getEditText().getText().length());
                    break;

            }
        }

        return false;
    }

    private boolean isInputValid() {

        if (!isInputted(this, etMobileNumber)) {
            return false;
        }

        if (!isInputted(this, etPassword)) {
            return false;
        }

        return true;
    }

    @Override
    public void onConnectionEstablished(int code) {
        if (code == SIGNED_IN_ACTION) {
            doLoginRequest(etMobileNumber.getEditText().getText().toString(),
                    etPassword.getEditText().getText().toString());
        }
    }

    @Override
    public void onUserCanceled(int code) {

    }


}