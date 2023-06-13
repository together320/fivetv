package com.brazvip.fivetv.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.brazvip.fivetv.adapters.GalleryAdapter;
import com.brazvip.fivetv.beans.ChannelBean;
import com.brazvip.fivetv.utils.PrefUtils;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.brazvip.fivetv.R;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.List;

public class GalleryLayout extends RelativeLayout implements
        DiscreteScrollView.OnItemChangedListener<GalleryAdapter.ViewHolder>,
        View.OnClickListener {

    private Context mContext;

    private DiscreteScrollView itemPicker;
    private TextView itemName;
    private InfiniteScrollAdapter<?> infiniteAdapter;
    private List<ChannelBean> mChannelList;

    public GalleryLayout(Context context) {
        super(context);
        init(context);
    }

    public GalleryLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GalleryLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(@NonNull Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.gallery_layout, this, true);

        itemPicker = findViewById(R.id.item_picker);
        itemName = findViewById(R.id.item_name);
    }

    public void setGalleryData(List<ChannelBean> list) {
        mChannelList = list;

        infiniteAdapter = InfiniteScrollAdapter.wrap(new GalleryAdapter(mContext, mChannelList));

        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
        itemPicker.addOnItemChangedListener(this);
        itemPicker.setAdapter(infiniteAdapter);
        itemPicker.setItemTransitionTimeMillis(150);
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        onItemChanged(mChannelList.get(0));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onCurrentItemChanged(@Nullable GalleryAdapter.ViewHolder viewHolder, int adapterPosition) {
        int positionInDataSet = infiniteAdapter.getRealPosition(adapterPosition);
        onItemChanged(mChannelList.get(positionInDataSet));
    }

    private void onItemChanged(ChannelBean item) {
        itemName.setText(item.getName().getInit());
    }
}
