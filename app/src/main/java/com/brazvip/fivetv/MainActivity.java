package com.brazvip.fivetv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.brazvip.fivetv.instances.AuthInstance;
import com.brazvip.fivetv.utils.PrefUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_logout = (Button)findViewById(R.id.logout_btn);
        btn_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.logout_btn) {
            logout();
        }
    }

    private void logout() {
        AuthInstance.SaveAuthParams("", "");

        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}