package com.brazvip.fivetv.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.utils.StringUtil;
import com.brazvip.fivetv.utils.Utils;
import com.bumptech.glide.Glide;
import com.brazvip.fivetv.R;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import com.brazvip.fivetv.beans.vod.VodChannelBean;
import com.brazvip.fivetv.instances.VodChannelInstance;
import p123l2.C2007k;
import p123l2.C2011o;
import p123l2.C2013q;


public class MovieDialog extends VodDialog {
    private static final String TAG = "MovieDialog";
    private static MovieDialog movieDialog;
    private Context _context;
    private C2007k binding;
    private VodChannelBean.Episode movieSrc;
    private RecommendationAdapter recAdapter;

    
    public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder> {
        private View.OnFocusChangeListener itemFocusListener = new View.OnFocusChangeListener() {
            @Override // android.view.View.OnFocusChangeListener
            public final void onFocusChange(View view, boolean z) {
                MovieDialog.RecommendationAdapter.focusChange(view, z);
            }
        };
        private Context mContext;
        private List<VodChannelBean> recommendations;

        
        public class ViewHolder extends RecyclerView.ViewHolder {
            public C2011o binding;

            public ViewHolder(C2011o c2011o) {
                super(c2011o.f7169a);
                this.binding = c2011o;
            }
        }

        public RecommendationAdapter(List<VodChannelBean> list, Context context) {
            this.recommendations = list;
            this.mContext = context;
        }

        public static void focusChange(View view, boolean z) {
            try {
                View findViewById = view.findViewById(R.id.dark_tint_20_percent);
                if (findViewById != null) {
                    findViewById.setVisibility(z ? View.INVISIBLE : View.VISIBLE);
                }
            } catch (Exception unused) {
            }
        }

        public static void getVodInfo(final RecommendationAdapter recommendationAdapter, final ViewHolder viewHolder, final View view) {
            recommendationAdapter.onCreateView(viewHolder, view);
        }

        public void onCreateView(ViewHolder viewHolder, View view) {
            this.recommendations.get(viewHolder.getAbsoluteAdapterPosition());
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter<VH>
        public int getItemCount() {
            return this.recommendations.size();
        }

        @Override
        public void onBindViewHolder(RecommendationAdapter.ViewHolder viewHolder, int i) {
            String str;
            VodChannelBean vodChannelBean = this.recommendations.get(i);
            Glide.with(this.mContext)
                    .load(vodChannelBean.getPoster())
                    .placeholder(R.mipmap.loading)
                    //.load(DiskCacheStrategy.DATA)
                    .error(R.mipmap.load_error)
                    .into(viewHolder.binding.f7174f);
            viewHolder.binding.f7173e.setText(vodChannelBean.getTitle());
            if (vodChannelBean.getTags() != null) {
                Iterator<String> it = vodChannelBean.getTags().iterator();
                while (it.hasNext()) {
                    try {
                        String[] split = it.next().split("/");
                        if ("Filmes".equalsIgnoreCase(split[0]) && split.length > 1) {
                            str = split[1];
                            break;
                        }
                    } catch (Exception unused) {
                        String unused2 = MovieDialog.TAG;
                    }
                }
            }
            str = "";
            if (str == null || str.isEmpty()) {
                viewHolder.binding.f7172d.setVisibility(View.GONE);
            } else {
                viewHolder.binding.f7171c.setText(str);
            }
            if (vodChannelBean.getEpisodes() != null) {
                vodChannelBean.getEpisodes().get(0);
            }
            viewHolder.binding.f7170b.setText("");
            viewHolder.itemView.setOnFocusChangeListener(this.itemFocusListener);
            viewHolder.itemView.setOnClickListener(new DialogOnClickListener2(0, this, viewHolder));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter<VH>
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recommendation_item, viewGroup, false);
            int i2 = R.id.dark_tint_20_percent;
            if (Utils.m385i(R.id.dark_tint_20_percent, inflate) != null) {
                i2 = R.id.duration;
                TextView textView = (TextView) Utils.m385i(R.id.duration, inflate);
                if (textView != null) {
                    i2 = R.id.genre;
                    TextView textView2 = (TextView) Utils.m385i(R.id.genre, inflate);
                    if (textView2 != null) {
                        i2 = R.id.genre_card;
                        CardView cardView = (CardView) Utils.m385i(R.id.genre_card, inflate);
                        if (cardView != null) {
                            i2 = R.id.name;
                            TextView textView3 = (TextView) Utils.m385i(R.id.name, inflate);
                            if (textView3 != null) {
                                i2 = R.id.square_image;
                                ImageView imageView = (ImageView) Utils.m385i(R.id.square_image, inflate);
                                if (imageView != null) {
                                    return new ViewHolder(new C2011o((RelativeLayout) inflate, textView, textView2, cardView, textView3, imageView));
                                }
                            }
                        }
                    }
                }
            }
            throw new NullPointerException("Missing required view with ID: ".concat(inflate.getResources().getResourceName(i2)));
        }
    }


    public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.TagHolder> {
        private List<String> tags = new ArrayList();


        public class TagHolder extends RecyclerView.ViewHolder {
            public C2013q tagBinding;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public TagHolder(C2013q c2013q) {
                super(c2013q.f7186a);
                this.tagBinding = c2013q;
            }
        }

        public TagsAdapter(List<String> list) {
            String str;
            for (String str2 : list) {
                String[] split = str2.split("/");
                if (split.length > 1 && (str = split[1]) != null && !str.isEmpty()) {
                    this.tags.add(split[1]);
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter<VH>
        public int getItemCount() {
            return this.tags.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter<VH>
        public void onBindViewHolder(TagHolder tagHolder, int i) {
            tagHolder.tagBinding.f7187b.setText(this.tags.get(i));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter<VH>
        public TagHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tag_item, viewGroup, false);
            TextView textView = (TextView) Utils.m385i(R.id.tag, inflate);
            if (textView != null) {
                return new TagHolder(new C2013q((CardView) inflate, textView));
            }
            throw new NullPointerException("Missing required view with ID: ".concat(inflate.getResources().getResourceName(R.id.tag)));
        }
    }

    private MovieDialog(Context context, VodChannelBean vodChannelBean) {
        this.movieSrc = null;
        this._context = context;
        this.channel = vodChannelBean;
        this.FRAGMENT_TAG = TAG;
        if (vodChannelBean.getEpisodes() == null || vodChannelBean.getEpisodes().isEmpty()) {
            return;
        }
        this.movieSrc = vodChannelBean.getEpisodes().get(0);
    }

    private void checkFavorite() {
        boolean isFavoriteVod = VodChannelInstance.isFavoriteVod(this.channel.getId());
        this.binding.f7150l.setText(isFavoriteVod ? R.string.rem_favorite : R.string.add_favorite);
        this.binding.f7147i.setVisibility(isFavoriteVod ? View.VISIBLE : View.GONE);
    }

    public static MovieDialog createDialogImpl(Context context, VodChannelBean vodChannelBean) {
        VodChannelBean vodChannelBean2;
        MovieDialog movieDialog2 = movieDialog;
        if (movieDialog2 == null || (vodChannelBean2 = movieDialog2.channel) == null || !Objects.equals(vodChannelBean2.getId(), vodChannelBean.getId())) {
            movieDialog = new MovieDialog(context, vodChannelBean);
        }
        return movieDialog;
    }

    public static void destroy() {
        MovieDialog movieDialog2 = movieDialog;
        if (movieDialog2 != null) {
            movieDialog2.dismiss();
            movieDialog = null;
        }
    }

    public static MovieDialog getInstance() {
        return movieDialog;
    }

    public static void hide() {
        MovieDialog movieDialog2 = movieDialog;
        if (movieDialog2 != null) {
            movieDialog2.dismiss();
        }
    }

    public void lambda$onCreateView$0(View view) {
        String str;
        VodChannelBean.Episode episode = this.movieSrc;
        if (episode != null) {
            str = episode.address;
        } else {
            str = null;
        }
        String str2 = str;
        if (str2 != null && !str2.isEmpty()) {
            String title = this.channel.getTitle();
            StringBuilder m6212l = StringUtil.m6212l("");
            m6212l.append(this.movieSrc.f8661id);
            requestVideoPlayback(title, m6212l.toString(), str2, "", "", this.channel.getRestricted());
            return;
        }
        //SopCast.sendShowToastEvent("Video not available", 1);
    }

    public void lambda$onCreateView$1(View view) {
        if (VodChannelInstance.isFavoriteVod(this.channel.getId())) {
            Context context = this._context;
            Toast.makeText(context, this.channel.getTitle() + " " + this._context.getString(R.string.remove_fav), Toast.LENGTH_SHORT).show();
            VodChannelInstance.removeFavoriteChannel(this.channel.getId());
        } else {
            Context context2 = this._context;
            Toast.makeText(context2, this.channel.getTitle() + " " + this._context.getString(R.string.favorited), Toast.LENGTH_LONG).show();
            VodChannelInstance.addFavoriteChannel(this.channel.getId());
        }
        checkFavorite();
    }

    public String formatVodDuration(float f) {
        if (Float.isNaN(f) || f == 0.0f) {
            return "-";
        }
        int i = (int) ((f / 60.0f) % 60.0f);
        int i2 = (int) (f / 3600.0f);
        return i2 > 0 ? String.format(getString(R.string.duration_hr_min), Integer.valueOf(i2), Integer.valueOf(i)) : String.format(getString(R.string.duration_min), Integer.valueOf(i));
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        float f;
        View inflate = layoutInflater.inflate(R.layout.movie_dialog, viewGroup, false);
        int i = R.id.backdrop_tint;
        View m385i = Utils.m385i(R.id.backdrop_tint, inflate);
        if (m385i != null) {
            i = R.id.certificate;
            TextView textView = (TextView) Utils.m385i(R.id.certificate, inflate);
            if (textView != null) {
                i = R.id.certificate_area;
                if (((LinearLayout) Utils.m385i(R.id.certificate_area, inflate)) != null) {
                    i = R.id.certificate_info;
                    if (((LinearLayout) Utils.m385i(R.id.certificate_info, inflate)) != null) {
                        i = R.id.duration;
                        TextView textView2 = (TextView) Utils.m385i(R.id.duration, inflate);
                        if (textView2 != null) {
                            i = R.id.genre_r_v;
                            RecyclerView recyclerView = (RecyclerView) Utils.m385i(R.id.genre_r_v, inflate);
                            if (recyclerView != null) {
                                i = R.id.genre_row;
                                TableRow tableRow = (TableRow) Utils.m385i(R.id.genre_row, inflate);
                                if (tableRow != null) {
                                    i = R.id.genre_string;
                                    TextView textView3 = (TextView) Utils.m385i(R.id.genre_string, inflate);
                                    if (textView3 != null) {
                                        i = R.id.left_spacer;
                                        if (Utils.m385i(R.id.left_spacer, inflate) != null) {
                                            i = R.id.movie_backdrop;
                                            ImageView imageView = (ImageView) Utils.m385i(R.id.movie_backdrop, inflate);
                                            if (imageView != null) {
                                                i = R.id.movie_fav_img;
                                                ImageView imageView2 = (ImageView) Utils.m385i(R.id.movie_fav_img, inflate);
                                                if (imageView2 != null) {
                                                    i = R.id.movie_image;
                                                    ShapeableImageView shapeableImageView = (ShapeableImageView) Utils.m385i(R.id.movie_image, inflate);
                                                    if (shapeableImageView != null) {
                                                        i = R.id.movie_toggle_fav;
                                                        LinearLayout linearLayout = (LinearLayout) Utils.m385i(R.id.movie_toggle_fav, inflate);
                                                        if (linearLayout != null) {
                                                            i = R.id.movie_toggle_fav_txt;
                                                            TextView textView4 = (TextView) Utils.m385i(R.id.movie_toggle_fav_txt, inflate);
                                                            if (textView4 != null) {
                                                                i = R.id.overview;
                                                                TextView textView5 = (TextView) Utils.m385i(R.id.overview, inflate);
                                                                if (textView5 != null) {
                                                                    i = R.id.play_button;
                                                                    LinearLayout linearLayout2 = (LinearLayout) Utils.m385i(R.id.play_button, inflate);
                                                                    if (linearLayout2 != null) {
                                                                        i = R.id.rating_layout;
                                                                        if (((ConstraintLayout) Utils.m385i(R.id.rating_layout, inflate)) != null) {
                                                                            i = R.id.recommendations_layout;
                                                                            LinearLayout linearLayout3 = (LinearLayout) Utils.m385i(R.id.recommendations_layout, inflate);
                                                                            if (linearLayout3 != null) {
                                                                                i = R.id.recommendations_r_v;
                                                                                RecyclerView recyclerView2 = (RecyclerView) Utils.m385i(R.id.recommendations_r_v, inflate);
                                                                                if (recyclerView2 != null) {
                                                                                    i = R.id.release_y;
                                                                                    TextView textView6 = (TextView) Utils.m385i(R.id.release_y, inflate);
                                                                                    if (textView6 != null) {
                                                                                        i = R.id.right_spacer;
                                                                                        if (Utils.m385i(R.id.right_spacer, inflate) != null) {
                                                                                            i = R.id.separator_1;
                                                                                            View m385i2 = Utils.m385i(R.id.separator_1, inflate);
                                                                                            if (m385i2 != null) {
                                                                                                i = R.id.separator_2;
                                                                                                if (Utils.m385i(R.id.separator_2, inflate) != null) {
                                                                                                    i = R.id.separator_3;
                                                                                                    if (Utils.m385i(R.id.separator_3, inflate) != null) {
                                                                                                        i = R.id.separator_gender;
                                                                                                        if (Utils.m385i(R.id.separator_gender, inflate) != null) {
                                                                                                            i = R.id.title;
                                                                                                            TextView textView7 = (TextView) Utils.m385i(R.id.title, inflate);
                                                                                                            if (textView7 != null) {
                                                                                                                i = R.id.views_count;
                                                                                                                if (((TextView) Utils.m385i(R.id.views_count, inflate)) != null) {
                                                                                                                    i = R.id.vote_average;
                                                                                                                    TextView textView8 = (TextView) Utils.m385i(R.id.vote_average, inflate);
                                                                                                                    if (textView8 != null) {
                                                                                                                        i = R.id.x_details;
                                                                                                                        if (((TableLayout) Utils.m385i(R.id.x_details, inflate)) != null) {
                                                                                                                            i = R.id.x_ll_center;
                                                                                                                            if (((LinearLayout) Utils.m385i(R.id.x_ll_center, inflate)) != null) {
                                                                                                                                i = R.id.x_ll_left;
                                                                                                                                if (((LinearLayout) Utils.m385i(R.id.x_ll_left, inflate)) != null) {
                                                                                                                                    i = R.id.x_scroll1;
                                                                                                                                    if (((ScrollView) Utils.m385i(R.id.x_scroll1, inflate)) != null) {
                                                                                                                                        this.binding = new C2007k((ConstraintLayout) inflate, m385i, textView, textView2, recyclerView, tableRow, textView3, imageView, imageView2, shapeableImageView, linearLayout, textView4, textView5, linearLayout2, linearLayout3, recyclerView2, textView6, m385i2, textView7, textView8);
                                                                                                                                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                                                                                                                        try {
                                                                                                                                            Glide.with(this._context)
                                                                                                                                                    .load(this.channel.getPoster())
                                                                                                                                                    .placeholder(R.mipmap.loading)
                                                                                                                                                    //.load(DiskCacheStrategy.DATA))
                                                                                                                                                    .error(R.mipmap.load_error)
                                                                                                                                                    .into(this.binding.f7148j);
                                                                                                                                        } catch (Exception e) {
                                                                                                                                            e.getMessage();
                                                                                                                                        }
//                                                                                                                                        InterfaceC3271f interfaceC3271f = new InterfaceC3271f() {
//                                                                                                                                            @Override
//                                                                                                                                            public boolean onLoadFailed(C1757b0 c1757b0, Object obj, InterfaceC3306e interfaceC3306e, boolean z) {
//                                                                                                                                                return false;
//                                                                                                                                            }
//
//                                                                                                                                            @Override
//                                                                                                                                            public boolean onResourceReady(Drawable drawable, Object obj, InterfaceC3306e interfaceC3306e, EnumC1644a enumC1644a, boolean z) {
//                                                                                                                                                MovieDialog.this.binding.f7140b.setVisibility(View.VISIBLE);
//                                                                                                                                                return false;
//                                                                                                                                            }
//                                                                                                                                        };
                                                                                                                                        String str = this.channel.backdrop;
                                                                                                                                        if (str != null && !str.trim().isEmpty()) {
                                                                                                                                            try {
                                                                                                                                                Glide.with(this._context)
                                                                                                                                                        .load(this.channel.backdrop)
                                                                                                                                                        //.load(DiskCacheStrategy.DATA)
                                                                                                                                                        //.m4978s(interfaceC3271f)
                                                                                                                                                        .into(this.binding.f7146h);
                                                                                                                                            } catch (Exception e2) {
                                                                                                                                                e2.getMessage();
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                        if (this.channel.getTitle() != null) {
                                                                                                                                            this.binding.f7157s.setText(this.channel.getTitle());
                                                                                                                                        }
                                                                                                                                        String str2 = this.channel.certificate;
                                                                                                                                        if (str2 != null) {
                                                                                                                                            this.binding.f7141c.setText(str2);
                                                                                                                                        }
                                                                                                                                        String str3 = this.channel.releaseDate;
                                                                                                                                        if (str3 != null) {
                                                                                                                                            this.binding.f7155q.setText(str3);
                                                                                                                                        }
                                                                                                                                        String str4 = this.channel.genres;
                                                                                                                                        if (str4 != null) {
                                                                                                                                            this.binding.f7145g.setText(str4);
                                                                                                                                        }
                                                                                                                                        if (this.channel.getVoteAverage() != null) {
                                                                                                                                            TextView textView9 = this.binding.f7158t;
                                                                                                                                            textView9.setText(this.channel.getVoteAverage().toString() + "%");
                                                                                                                                        }
                                                                                                                                        String overview = this.channel.getOverview();
                                                                                                                                        if (overview != null) {
                                                                                                                                            this.binding.f7151m.setText(overview.trim());
                                                                                                                                        } else {
                                                                                                                                            this.binding.f7151m.setVisibility(View.GONE);
                                                                                                                                        }
                                                                                                                                        ArrayList arrayList = new ArrayList();
                                                                                                                                        this.binding.f7143e.setLayoutManager(new LinearLayoutManager(_context, RecyclerView.HORIZONTAL, false));
                                                                                                                                        if (this.channel.getTags() != null && this.channel.getTags().size() > 0) {
                                                                                                                                            this.binding.f7143e.setHasFixedSize(true);
                                                                                                                                            this.binding.f7143e.setAdapter(new TagsAdapter(this.channel.getTags()));
                                                                                                                                        } else {
                                                                                                                                            this.binding.f7144f.setVisibility(View.GONE);
                                                                                                                                            this.binding.f7156r.setVisibility(View.GONE);
                                                                                                                                        }
                                                                                                                                        VodChannelBean.Episode episode = this.movieSrc;
                                                                                                                                        if (episode != null) {
                                                                                                                                            f = episode.getDurationInSeconds();
                                                                                                                                        } else {
                                                                                                                                            f = 0.0f;
                                                                                                                                        }
                                                                                                                                        this.binding.f7142d.setText(formatVodDuration(f));
                                                                                                                                        this.binding.f7154p.setLayoutManager(new LinearLayoutManager(_context, RecyclerView.VERTICAL, false));
                                                                                                                                        if (!arrayList.isEmpty()) {
                                                                                                                                            RecommendationAdapter recommendationAdapter = new RecommendationAdapter(arrayList, this._context);
                                                                                                                                            this.recAdapter = recommendationAdapter;
                                                                                                                                            this.binding.f7154p.setAdapter(recommendationAdapter);
                                                                                                                                        } else {
                                                                                                                                            this.binding.f7153o.setVisibility(View.GONE);
                                                                                                                                        }
                                                                                                                                        checkFavorite();
                                                                                                                                        this.binding.f7152n.setOnClickListener(new View.OnClickListener() {
                                                                                                                                            @Override
                                                                                                                                            public void onClick(View view) {
                                                                                                                                                lambda$onCreateView$0(view);
                                                                                                                                            }
                                                                                                                                        });
                                                                                                                                        this.binding.f7149k.setOnClickListener(new View.OnClickListener() {
                                                                                                                                            @Override
                                                                                                                                            public void onClick(View view) {
                                                                                                                                                lambda$onCreateView$1(view);
                                                                                                                                            }
                                                                                                                                        });
                                                                                                                                        return this.binding.f7139a;
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(inflate.getResources().getResourceName(i)));
    }
}
