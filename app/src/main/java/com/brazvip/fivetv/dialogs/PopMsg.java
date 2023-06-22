package com.brazvip.fivetv.dialogs;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.utils.Utils;
import com.zhy.autolayout.utils.AutoUtils;
import p129l8.C2053a;


public class PopMsg extends PopupWindow {
    public PopMsg(Context context, String str, String str2) {
        super(context);
        View inflate = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_pop_msg, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R.id.message_title);
        TextView textView2 = (TextView) inflate.findViewById(R.id.message_body);
        Button button = (Button) inflate.findViewById(R.id.close_button);
        textView.setTextSize(0, Utils.calculateTextSize(30));
        textView2.setTextSize(0, Utils.calculateTextSize(30));
        button.setTextSize(0, Utils.calculateTextSize(26));
        if (str != null && !str.equals("")) {
            textView.setText(str);
        }
        if (str2 != null && !str2.equals("")) {
            textView2.setText(str2);
        }
        AutoUtils.auto(inflate, 3, 2);
        setContentView(inflate);
        int calculateTextSize = Utils.calculateTextSize(500);
        C2053a c2053a = C2053a.f7440f;
        int i = c2053a.f7442b;
        setWidth(calculateTextSize);
        setHeight((int) ((400.0f / c2053a.f7444d) * i));
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        button.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopMsg.this.dismiss();
                //SopCast.handler.sendEmptyMessage(101);
            }
        });
    }
}
