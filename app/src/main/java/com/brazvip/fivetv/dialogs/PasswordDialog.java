package com.brazvip.fivetv.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brazvip.fivetv.Config;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.SopApplication;
import com.brazvip.fivetv.utils.Utils;
import com.zhy.autolayout.utils.AutoUtils;


public class PasswordDialog extends Dialog {

    public static class Builder {
        public Context context;
        public Button negativeButton;
        public Button positiveButton;
        public DialogInterface.OnClickListener positiveClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public PasswordDialog build() {
            final PasswordDialog passwordDialog = new PasswordDialog(this.context, R.style.Dialog);
            View inflate = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_pwd_layout, (ViewGroup) null);
            AutoUtils.auto(inflate, 3, 3);
            passwordDialog.addContentView(inflate, new ViewGroup.LayoutParams(-1, -2));
            final EditText editText = (EditText) inflate.findViewById(R.id.et_password);
            final TextView textView = (TextView) inflate.findViewById(R.id.error);
            editText.setFocusable(true);
            editText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view) {
                    editText.setInputType(16);
                    editText.setImeOptions(6);
                    InputMethodManager inputMethodManager = (InputMethodManager) Builder.this.context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager == null) {
                        return true;
                    }
                    inputMethodManager.showSoftInput(editText, 0);
                    return true;
                }
            });
            positiveButton = inflate.findViewById(R.id.positiveButton);
            if (positiveClickListener != null) {
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String strPassword = editText.getText().toString().trim();
                        boolean empty = TextUtils.isEmpty(strPassword);
                        if (empty) {
                            textView.setText(SopApplication.getAppContext().getString(R.string.password_cannot_blank));
                            textView.setVisibility(View.VISIBLE);
                            return;
                        }
                        if (strPassword.length() < 4) {
                            textView.setText(SopApplication.getAppContext().getString(R.string.password_too_short));
                            textView.setVisibility(View.VISIBLE);
                            return;
                        }
                        if (Utils.getValue(Config.PASSWORD, Config.defaultPassword).equals(strPassword)) {
                            positiveClickListener.onClick((DialogInterface)passwordDialog, -1);
                            passwordDialog.dismiss();
                        } else {
                            textView.setText(SopApplication.getAppContext().getString(R.string.password_err));
                            textView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
            negativeButton = inflate.findViewById(R.id.negativeButton);
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    passwordDialog.dismiss();
                }
            });
            passwordDialog.setContentView(inflate);
            editText.requestFocus();
            passwordDialog.getWindow().setSoftInputMode(4);
            return passwordDialog;
        }
    }

    public PasswordDialog(Context context, int i) {
        super(context, i);
    }
}