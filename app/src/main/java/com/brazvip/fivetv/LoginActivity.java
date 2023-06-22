package com.brazvip.fivetv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.brazvip.fivetv.instances.AuthInstance;
import com.brazvip.fivetv.utils.PrefUtils;
import com.brazvip.fivetv.utils.Utils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static Handler mMsgHandler = null;
    public static EditText et_username = null;
    public static EditText et_password = null;
    public static ProgressBar loading_progress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_password);

        loading_progress = findViewById(R.id.loading_progress);
        loading_progress.setVisibility(View.GONE);

        String savedUserName = PrefUtils.getPrefString("username", "");
        if (savedUserName.length() > 0)
            et_username.setText(savedUserName);

        Button btn_login = (Button)findViewById(R.id.username_login_submit_btn);
        btn_login.setOnClickListener(this);

        initMessageHandler();
    }

    @Override
    public void onBackPressed() {
        finish();
        Utils.exitApp();
    }

    public void initMessageHandler() {
        mMsgHandler = new Handler(Looper.getMainLooper()) {
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                switch (message.what) {
                    case Constant.MSG_LOGIN_SUCCESS:
                        onLoginSuccess();
                        break;
                    case Constant.MSG_LOGIN_FAIL:
                        onLoginFail();
                        break;
                }
                super.handleMessage(message);
            }
        };
    }

    private void onLoginSuccess() {
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();
        AuthInstance.SaveAuthParams(username, password);

        loading_progress.setVisibility(View.GONE);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();
    }

    private void onLoginFail() {
        loading_progress.setVisibility(View.GONE);

        PrefUtils.ToastShort("Account Login Failure!");
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.username_login_submit_btn) {
            if (loading_progress.getVisibility() == View.VISIBLE)
                return;

            login();
        }
    }

    private void login() {
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();

        if (username.length() == 0)
            PrefUtils.ToastShort(getString(R.string.username_cannot_blank));
        else if (username.length() < 4)
            PrefUtils.ToastShort(String.format(getString(R.string.username_too_short), 4));
        else if (password.length() == 0)
            PrefUtils.ToastShort(getString(R.string.password_cannot_blank));
        else if (password.length() < 4)
            PrefUtils.ToastShort(String.format(getString(R.string.password_too_short), 4));
        else {
            loading_progress.setVisibility(View.VISIBLE);
            AuthInstance.doAuth(username, password, mMsgHandler);
        }
    }
}