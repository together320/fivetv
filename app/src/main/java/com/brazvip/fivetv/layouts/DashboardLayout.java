package com.brazvip.fivetv.layouts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brazvip.fivetv.Constant;
import com.brazvip.fivetv.MainActivity;
import com.brazvip.fivetv.R;
import com.brazvip.fivetv.adapters.DashboardGroupL1Adapter;
import com.brazvip.fivetv.adapters.DashboardGroupL2Adapter;
import com.brazvip.fivetv.adapters.DashboardLineAdapter;
import com.brazvip.fivetv.adapters.NavigationListener;
import com.brazvip.fivetv.adapters.SportAdapter;
import com.brazvip.fivetv.beans.DashboardInfo;
import com.brazvip.fivetv.bindings.DashboardBinding;
import com.brazvip.fivetv.instances.DashboardInstance;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class DashboardLayout extends RelativeLayout implements View.OnKeyListener {
    
    public Context mContext;
    
    public static final int FOCUS_GROUP_L1_RV = 10;
    public static final int FOCUS_GROUP_L2_RV = 11;
    public static final int FOCUS_SPORTS_RV = 12;
    public static final int FOCUS_NEW_RELEASES_RV = 13;
    public static final int L1_GROUP_SELECTED = 20;
    public static final int L2_GROUP_SELECTED = 21;
    public static final int LOAD_DASHBOARD_CONTENT = 26;
    private static int RELEASES_COLUMN_COUNT = 3;
    private static int SPORTS_COLUMN_COUNT = 3;
    public static final String TAG = "Dashboard";
    public static Handler dashboardHandler;
    public static ScrollView linesScrollView;
    private DashboardBinding binding;
    private DashboardGroupL1Adapter groupL1Adapter;
    private RecyclerView.LayoutManager newReleasesLM;
    private SportAdapter sportAdapter;
    private RecyclerView.LayoutManager sportsLayoutManager;
    private Map<Integer, RecyclerView> linesRvs = new LinkedHashMap();
    private NavigationListener topLineListener = new NavigationListener() {
        @Override // com.brazvip.fivetv.adapter.NavigationListener
        public boolean navigateAbove() {
            binding.groupL2_rv.requestFocus();
            return true;
        }

        @Override // com.brazvip.fivetv.adapter.NavigationListener
        public boolean navigateBelow() {
            if (binding.others_grid_ll.getVisibility() == View.VISIBLE) {
                binding.others_grid_ll.requestFocus();
                return true;
            }
            return true;
        }

        @Override // com.brazvip.fivetv.adapter.NavigationListener
        public boolean navigateLeft() {
            MainActivity.SendMessage(Constant.EVENT_FOCUS_DASH_BUTTON);
            return true;
        }

        @Override // com.brazvip.fivetv.adapter.NavigationListener
        public boolean navigateRight() {
            return true;
        }
    };
    private NavigationListener gridNavListener = new NavigationListener() {
        @Override // com.brazvip.fivetv.adapter.NavigationListener
        public boolean navigateAbove() {
            return false;
        }

        @Override // com.brazvip.fivetv.adapter.NavigationListener
        public boolean navigateBelow() {
            return false;
        }

        @Override // com.brazvip.fivetv.adapter.NavigationListener
        public boolean navigateLeft() {
            MainActivity.SendMessage(Constant.EVENT_FOCUS_DASH_BUTTON);
            return true;
        }

        @Override // com.brazvip.fivetv.adapter.NavigationListener
        public boolean navigateRight() {
            return true;
        }
    };

    public DashboardLayout(Context context) {
        super(context);
        init(context);
    }

    public DashboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DashboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        initComponent();
        initHandler();
        focusDefaultView();
    }

    @SuppressLint({"HandlerLeak"})
    private void initHandler() {
        dashboardHandler = new Handler(Looper.myLooper()) { // from class: com.brazvip.fivetv.fragment.DashboardLayout.3
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                RecyclerView recyclerView;
                int what = message.what;
                if (what == L1_GROUP_SELECTED) {
                    onGroupL1Selected(message.getData().getString("title"));
                } else if (what == L2_GROUP_SELECTED) {
                    onGroupL2Selected(message.getData().getString("title"));
                } else {
                    if (what == LOAD_DASHBOARD_CONTENT) {
                        loadDashboardContent();
                    }
                    if (binding == null) {
                        return;
                    }
                    switch (message.what) {
                        case FOCUS_GROUP_L1_RV:
                            recyclerView = binding.groupL1_rv;
                            break;
                        case FOCUS_GROUP_L2_RV:
                            recyclerView = binding.groupL2_rv;
                            break;
                        case FOCUS_SPORTS_RV:
                            recyclerView = binding.sports_rv;
                            break;
                        case FOCUS_NEW_RELEASES_RV:
                            recyclerView = binding.new_releases_rv;
                            break;
                        default:
                            return;
                    }
                    recyclerView.requestFocus();
                }
            }
        };
    }

    public void onGroupL1Selected(String str) {
        if (this.binding != null) {
            this.binding.groupL2_rv.setAdapter(new DashboardGroupL2Adapter(DashboardInstance.getInstance().groupL1L2Map.get(str), getContext(), new NavigationListener() {
                @Override // com.brazvip.fivetv.adapter.NavigationListener
                public boolean navigateAbove() {
                    if (binding.groupL1_rv.requestFocus()) {
                        return true;
                    }
                    return navigateLeft();
                }

                @Override // com.brazvip.fivetv.adapter.NavigationListener
                public boolean navigateBelow() {
                    return navigateLeft();
                }

                @Override // com.brazvip.fivetv.adapter.NavigationListener
                public boolean navigateLeft() {
                    MainActivity.SendMessage(Constant.EVENT_FOCUS_DASH_BUTTON);
                    return true;
                }

                @Override // com.brazvip.fivetv.adapter.NavigationListener
                public boolean navigateRight() {
                    return true;
                }
            }));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onGroupL2Selected(String str) {
        List<DashboardInfo.Line> list = DashboardInstance.getInstance().groupL2LinesMap.get(str);
        if (list != null && list.size() > 1) {
            this.binding.others_grid_ll.removeAllViews();
            this.binding.others_grid_ll.setVisibility(View.VISIBLE);
            for (int index = 0; index < list.size(); index++) {
                TextView textView = new TextView(mContext);
                textView.setTextColor(0xffffffff);
                textView.setTextSize(16);
                if (index == 0) textView.setText("Filmes");
                if (index == 1) textView.setText("Séries");
                if (index == 2) textView.setText("Novelas");
                this.binding.others_grid_ll.addView(textView);

                RecyclerView recyclerView = new RecyclerView(mContext, null);
                recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                recyclerView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                recyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
                recyclerView.setAdapter(new DashboardLineAdapter(list.get(index), mContext, this.gridNavListener, index, null));
                this.binding.others_grid_ll.addView(recyclerView);
                this.linesRvs.put(index, recyclerView);
            }
            this.binding.line_1_placeholder.setVisibility(View.GONE);
        } else {
            this.binding.line_1_placeholder.setVisibility(View.VISIBLE);
            this.binding.others_grid_ll.removeAllViews();
            this.binding.others_grid_ll.setVisibility(View.GONE);
        }
    }

    public void ClearDashboard() {
        if (this.binding != null) {
            this.binding.groupL1_rv.setAdapter(null);
            this.binding.groupL2_rv.setAdapter(null);
            this.binding.others_grid_ll.removeAllViews();
            this.binding.others_grid_ll.setVisibility(View.GONE);
            this.binding.sports_rv.setAdapter(null);
            this.binding.new_releases_rv.setAdapter(null);
        }
    }
    public void focusDefaultView() {
        if (this.binding == null || this.binding.groupL1_rv.requestFocus()) {
            return;
        }
        MainActivity.SendMessage(Constant.EVENT_FOCUS_DASH_BUTTON);
    }

    public void loadDashLayout() {
        loadDashboardContent();
    }

    public void loadDashboardContent() {
        if (DashboardInstance.getInstance().groupL1L2Map == null) {
            binding.line_1_placeholder.setVisibility(View.VISIBLE);
        } else {
            try {
                groupL1Adapter = new DashboardGroupL1Adapter(
                        new ArrayList<>(DashboardInstance.getInstance().groupL1L2Map.keySet()), getContext(), new NavigationListener() {
                    @Override // com.brazvip.fivetv.adapter.NavigationListener
                    public boolean navigateAbove() {
                        return true;
                    }

                    @Override // com.brazvip.fivetv.adapter.NavigationListener
                    public boolean navigateBelow() {
                        if (binding.groupL2_rv.requestFocus()) {
                            return true;
                        }
                        return navigateLeft();
                    }

                    @Override // com.brazvip.fivetv.adapter.NavigationListener
                    public boolean navigateLeft() {
                        MainActivity.SendMessage(Constant.EVENT_FOCUS_DASH_BUTTON);
                        return true;
                    }

                    @Override // com.brazvip.fivetv.adapter.NavigationListener
                    public boolean navigateRight() {
                        return true;
                    }
                });
                binding.groupL1_rv.setAdapter(groupL1Adapter);

//                sportAdapter = new SportAdapter(DashboardInstance.getInstance().getSportsData(), mContext);
//                binding.sports_rv.setAdapter(sportAdapter);
//                binding.new_releases_rv.setLayoutManager(newReleasesLM);

            } catch(Exception e) {
                e.printStackTrace();
                binding.line_1_placeholder.setVisibility(View.VISIBLE);
            }
        }

        binding.dashboard_root.setVisibility(View.VISIBLE);
    }

    public void initComponent() {
        LayoutInflater.from(mContext).inflate(R.layout.dashboard_layout, this, true);

        RelativeLayout  dashboard_root      = findViewById(R.id.dashboard_root);
        RecyclerView    groupL1_rv          = findViewById(R.id.groupL1_rv);
        RecyclerView    groupL2_rv          = findViewById(R.id.groupL2_rv);
        TextView        line_1_placeholder  = findViewById(R.id.line_1_placeholder);
        ScrollView      lines_scroll_view   = findViewById(R.id.lines_scroll_view);
        RecyclerView    new_releases_rv     = findViewById(R.id.new_releases_rv);
        LinearLayout    others_grid_ll      = findViewById(R.id.others_grid_ll);
        RecyclerView    sports_rv           = findViewById(R.id.sports_rv);

        this.binding = new DashboardBinding(
                dashboard_root, groupL1_rv, groupL2_rv, line_1_placeholder,
                lines_scroll_view, new_releases_rv, others_grid_ll, sports_rv);
        this.binding.groupL1_rv.setOnKeyListener(this);
        this.binding.groupL2_rv.setOnKeyListener(this);
        this.binding.others_grid_ll.setOnKeyListener(this);

        linesScrollView = this.binding.lines_scroll_view;
        linesScrollView.setOnKeyListener(this);

        this.sportsLayoutManager = new GridLayoutManager(mContext, SPORTS_COLUMN_COUNT);
        this.newReleasesLM = new GridLayoutManager(mContext, RELEASES_COLUMN_COUNT);

        this.binding.sports_rv.setLayoutManager(this.sportsLayoutManager);

        this.binding.dashboard_root.setVisibility(View.GONE);
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (keyEvent.getAction() == 0) {
            if (keyCode == 4) {
                MainActivity.SendMessage(Constant.EVENT_FOCUS_DASH_BUTTON);
                return true;
            }
            int id = view.getId();
            if (id == this.binding.groupL1_rv.getId()) {
                switch (keyCode) {
                    case 19:
                    case 22:
                        return true;
                    case 20:
                    case 21:
                        MainActivity.SendMessage(Constant.EVENT_FOCUS_DASH_BUTTON);
                        return true;
                }
            } else if (id == this.binding.groupL2_rv.getId()) {
                switch (keyCode) {
                    case 19:
                    case 22:
                        return true;
                    case 20:
                    case 21:
                        MainActivity.SendMessage(Constant.EVENT_FOCUS_DASH_BUTTON);
                        return true;
                }
            } else if (id == this.binding.others_grid_ll.getId() || id == linesScrollView.getId()) {
                switch (keyCode) {
                    case 19:
                    case 20:
                    case 22:
                        return true;
                    case 21:
                        MainActivity.SendMessage(Constant.EVENT_FOCUS_DASH_BUTTON);
                        return true;
                }
            }
        }
        //return super.onKey(view, keyCode, keyEvent);
        return false;
    }

    public void setDashboardVisibility(int i) {
        DashboardBinding dashboardBinding = this.binding;
        if (dashboardBinding != null) {
            dashboardBinding.dashboard_root.setVisibility(i);
        }
    }
}