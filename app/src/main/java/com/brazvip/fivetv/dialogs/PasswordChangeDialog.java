package com.brazvip.fivetv.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brazvip.fivetv.R;
import com.zhy.autolayout.utils.AutoUtils;

import com.brazvip.fivetv.utils.PrefUtils;
import com.brazvip.fivetv.utils.BsConf;


public class PasswordChangeDialog extends Dialog {

    /* compiled from: MyApplication */
    /* renamed from: e.b.a.h.o$a 3633 */
    /* loaded from: classes.dex */
    public static class Helper {

        /* renamed from: a 14093 */
        public Context mContext;

        /* renamed from: b 14094 */
        public DialogInterface.OnClickListener mOnClickListener;

        /* renamed from: c */
        public int f14095c;

        /* renamed from: d */
        public Button mPositiveButton;

        /* renamed from: e */
        public Button mNegativeButton;

        public Helper(Context context) {
            this.mContext = context;
        }

        /* renamed from: a */
        public Helper setClickListener(DialogInterface.OnClickListener listener) {
            this.mOnClickListener = listener;
            return this;
        }

        /* renamed from: a 2189 */
        public PasswordChangeDialog create() {
            final PasswordChangeDialog dlg = new PasswordChangeDialog(this.mContext, R.style.Dialog);
            View content = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                                        .inflate(R.layout.dialog_pwd_layout, (ViewGroup) null);
            AutoUtils.autoSize(content); //auto(content, Attrs.WIDTH | Attrs.HEIGHT, AutoAttr.BASE_DEFAULT);
            dlg.addContentView(content, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                               ViewGroup.LayoutParams.WRAP_CONTENT)); // -1, -2
            final EditText editText = (EditText) content.findViewById(R.id.et_password);
            final TextView textView = (TextView) content.findViewById(R.id.error);
            editText.setFocusable(true);
            editText.setOnClickListener(new View.OnClickListener() { //new View$OnClickListenerC3628k(this, editText));
                @Override
                public void onClick(View v) {
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null)
                        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED); //2
                }
            });
            editText.setOnLongClickListener(new View.OnLongClickListener() { //new View$OnLongClickListenerC3629l(this, editText)
                @Override
                public boolean onLongClick(View v) {
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED); //2
                        return true;
                    }
                    return true;
                }
            });
            mPositiveButton = (Button) content.findViewById(R.id.positiveButton);
            if (mOnClickListener != null) {
                mPositiveButton.setOnClickListener(new View.OnClickListener() { //new View$OnClickListenerC3630m(this, editText, textView, dlg)
                    @SuppressLint("StringFormatInvalid")
                    @Override
                    public void onClick(View v) {
                        String text = editText.getText().toString().trim(), result;
                        boolean show = true;
                        if (TextUtils.isEmpty(text))
                            result  = mContext.getString(R.string.password_cannot_blank);
                        else if (text.length() < 4)
                            result  = String.format(mContext.getString(R.string.password_too_short), 4);
                        else if (PrefUtils.getPrefString("password", "1321").equals(text)) { //RestApiUtils.f13731O
                            result  = "";
                            show    = false;
                        } else
                            result  = mContext.getString(R.string.password_err);
                        if (show) {
                            textView.setText(result);
                            textView.setVisibility(View.VISIBLE);
                        } else {
                            mOnClickListener.onClick(dlg, DialogInterface.BUTTON_POSITIVE); //-1
                            dlg.dismiss();
                        }
                    }
                });
            }
            mNegativeButton = (Button) content.findViewById(R.id.negativeButton);
            mNegativeButton.setOnClickListener(new View.OnClickListener() { //new View$OnClickListenerC3631n(this, dlg));
                @Override
                public void onClick(View v) {
                    dlg.dismiss();
                }
            });
            dlg.setContentView(content);
            editText.requestFocus();
            dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE); //4
            dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            return dlg;
        }
    }

    public PasswordChangeDialog(Context context) {
        super(context);
    }

    public PasswordChangeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }
}
