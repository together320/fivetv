package com.brazvip.fivetv.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.brazvip.fivetv.R;

/* loaded from: classes.dex */
public class LogoutDialog extends Dialog {

    /* loaded from: classes.dex */
    public static class Builder implements View.OnKeyListener {
        public DialogInterface.OnClickListener cancelListener;
        public Context context;
        public String message;
        public Button negativeButton;
        public String negativeOption;
        public Button positiveButton;
        public DialogInterface.OnClickListener positiveListener;
        public String positiveOption;

        public Builder(Context context) {
            this.context = context;
        }

        public LogoutDialog build() {
            final LogoutDialog logoutDialog = new LogoutDialog(this.context, R.style.Dialog);
            View inflate = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_logout_layout, (ViewGroup) null);
            logoutDialog.addContentView(inflate, new ViewGroup.LayoutParams(-1, -2));
            if (this.positiveOption != null) {
                Button button = (Button) inflate.findViewById(R.id.positiveButton);
                this.positiveButton = button;
                button.setText(this.positiveOption);
                this.positiveButton.setOnKeyListener(this);
                if (this.positiveListener != null) {
                    this.positiveButton.setOnClickListener(new View.OnClickListener() { // from class: org.sopcast.android.dialog.LogoutDialog.Builder.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            Builder.this.positiveListener.onClick(logoutDialog, -1);
                        }
                    });
                }
            } else {
                inflate.findViewById(R.id.positiveButton).setVisibility(View.GONE);
            }
            if (this.negativeOption != null) {
                Button button2 = (Button) inflate.findViewById(R.id.negativeButton);
                this.negativeButton = button2;
                button2.setText(this.negativeOption);
                this.negativeButton.setOnKeyListener(this);
                if (this.cancelListener != null) {
                    this.negativeButton.setOnClickListener(new View.OnClickListener() { // from class: org.sopcast.android.dialog.LogoutDialog.Builder.2
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view) {
                            Builder.this.cancelListener.onClick(logoutDialog, -2);
                        }
                    });
                }
            } else {
                inflate.findViewById(R.id.negativeButton).setVisibility(View.GONE);
            }
            if (this.message != null) {
                ((TextView) inflate.findViewById(R.id.message)).setText(this.message);
            }
            logoutDialog.setContentView(inflate);
            return logoutDialog;
        }

        @Override // android.view.View.OnKeyListener
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            Button button;
            int id = view.getId();
            if (id == R.id.negativeButton) {
                if (i != 4) {
                    return false;
                }
                button = this.negativeButton;
            } else if (id != R.id.positiveButton || i != 4) {
                return false;
            } else {
                button = this.positiveButton;
            }
            button.requestFocus();
            return true;
        }
    }

    public LogoutDialog(Context context, int i) {
        super(context, i);
    }
}
