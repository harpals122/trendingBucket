package com.trending.trending_bucket.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.trending.trending.R;
import com.trending.trending_bucket.model.User;
import com.trending.trending_bucket.util.ApiHandler;
import com.trending.trending_bucket.util.FloatLabel;
import com.trending.trending_bucket.util.utilMethods;
import com.trending.trending_bucket.util.utilMethods.InternetConnectionListener;

import static com.trending.trending_bucket.util.constants.JF_EMAIL;
import static com.trending.trending_bucket.util.constants.MSG_PASSWORD_CHANGE_FAILED;
import static com.trending.trending_bucket.util.constants.URL_CHANGE_PASSWORD;
import static com.trending.trending_bucket.util.utilMethods.getPreferenceString;
import static com.trending.trending_bucket.util.utilMethods.hideSoftKeyboard;
import static com.trending.trending_bucket.util.utilMethods.isConnectedToInternet;
import static com.trending.trending_bucket.util.Validator.isInputted;
import static com.trending.trending_bucket.util.Validator.isPasswordMatched;
import static com.trending.trending_bucket.util.Validator.isPasswordValid;

/**
 * @author prabh kaur
 * @class ChangePasswordActivity
 * @brief Activity for change user password
 */

public class ChangePasswordActivity extends Activity implements View.OnClickListener, View.OnTouchListener,
        InternetConnectionListener, ApiHandler.ApiHandlerListener {

    private final int CHANGE_PASSWORD_ACTION = 1;
    private FloatLabel etOldPassword;
    private FloatLabel etPassword;
    private FloatLabel etRetypePassword;
    private String oldPassword;
    private String password;
    private boolean isUserCanceled = false;
    private InternetConnectionListener internetConnectionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        findViewById(R.id.crossImgView).setOnClickListener(this);
        findViewById(R.id.btnUpdate).setOnClickListener(this);
        findViewById(R.id.showOldPasswordImg).setOnTouchListener(this);
        findViewById(R.id.showPasswordImg).setOnTouchListener(this);
        findViewById(R.id.showRetypePasswordImg).setOnTouchListener(this);
        etOldPassword = (FloatLabel) findViewById(R.id.etOldPassword);
        etPassword = (FloatLabel) findViewById(R.id.etPassword);
        etRetypePassword = (FloatLabel) findViewById(R.id.etRetypePassword);
        etOldPassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());
        etPassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());
        etRetypePassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isUserCanceled) {
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            finish();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.crossImgView:
                hideSoftKeyboard(this);
                isUserCanceled = true;
                onPause();
                break;

            case R.id.btnUpdate:
                if (isInputValid()) {
                    if (isConnectedToInternet(this)) {
                        doChangePasswordRequest();
                    }
                    else {
                        internetConnectionListener = ChangePasswordActivity.this;
                        utilMethods.showNoInternetDialog(ChangePasswordActivity.this, internetConnectionListener, getResources().getString(R.string.no_internet),
                                getResources().getString(R.string.no_internet_text),
                                getResources().getString(R.string.retry_string),
                                getResources().getString(R.string.cancel_string), CHANGE_PASSWORD_ACTION);
                    }
                }
                break;
        }
    }

    private void doChangePasswordRequest() {
        ContentValues values = new ContentValues();
        oldPassword = etOldPassword.getEditText().getText().toString();
        password = etPassword.getEditText().getText().toString();
        values.put("password", password);
        User user = new User();
        user.setEmail(getPreferenceString(this, JF_EMAIL));
        String email = user.getEmail();
        values.put("email", email);
        ApiHandler apiHandler = new ApiHandler(this, URL_CHANGE_PASSWORD, values);
        apiHandler.doApiRequest(ApiHandler.REQUEST_POST);
        Toast.makeText(ChangePasswordActivity.this,"Password Changed ",Toast.LENGTH_LONG).show();
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }


    private boolean isInputValid() {

        if (!isInputted(this, etOldPassword)) {
            return false;
        }

        if (!isPasswordValid(this, etOldPassword)) {
            return false;
        }

        if (!isInputted(this, etPassword)) {
            return false;
        }

        if (!isPasswordValid(this, etPassword)) {
            return false;
        }

        if (!isInputted(this, etRetypePassword)) {
            return false;
        }

        if (!isPasswordMatched(this, etPassword, etRetypePassword)) {
            return false;
        }

        return true;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {

            case R.id.showOldPasswordImg:
                if (!TextUtils.isEmpty(etOldPassword.getEditText().getText())) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        etOldPassword.getEditText().setTransformationMethod(null);
                        etOldPassword.getEditText().setSelection(etOldPassword.getEditText().getText().length());
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        etOldPassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());
                        etOldPassword.getEditText().setSelection(etOldPassword.getEditText().getText().length());
                    }
                }
                break;

            case R.id.showPasswordImg:
                if (!TextUtils.isEmpty(etPassword.getEditText().getText())) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        etPassword.getEditText().setTransformationMethod(null);
                        etPassword.getEditText().setSelection(etPassword.getEditText().getText().length());
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        etPassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());
                        etPassword.getEditText().setSelection(etPassword.getEditText().getText().length());
                    }
                }
                break;

            case R.id.showRetypePasswordImg:

                if (!TextUtils.isEmpty(etRetypePassword.getEditText().getText())) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        etRetypePassword.getEditText().setTransformationMethod(null);
                        etRetypePassword.getEditText().setSelection(etRetypePassword.getEditText().getText().length());
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        etRetypePassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());
                        etRetypePassword.getEditText().setSelection(etRetypePassword.getEditText().getText().length());
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public void onConnectionEstablished(int code) {
        if (code == CHANGE_PASSWORD_ACTION) {
            if (isConnectedToInternet(this)) {
                doChangePasswordRequest();
            }
        }
    }

    @Override
    public void onUserCanceled(int code) {
        if (code == CHANGE_PASSWORD_ACTION) {
            isUserCanceled = true;
            onPause();
        }
    }

    @Override
    public void onSuccessResponse(String tag, String jsonString) {

    }

    @Override
    public void onFailureResponse(String tag) {

    }
}
