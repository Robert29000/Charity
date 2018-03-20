package com.example.melikyan.charity.MainApplication;




import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;


import com.example.melikyan.charity.AnnoucAdapter;
import com.example.melikyan.charity.R;
import com.example.melikyan.charity.UsersAnnotations;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;


import java.util.ArrayList;


public class AnnoucmentFragment extends Fragment  {
    TabLayout tableLayout;
    static ActionBar ab;
    public static void getAb(ActionBar bar){
        AnnoucmentFragment.ab=bar;
    }
    ObservableListView list;
    static public ArrayList<UsersAnnotations> users=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.annoucments,container,false);
        list=view.findViewById(R.id.annouclists);
        AnnoucAdapter adapter=new AnnoucAdapter(getContext(),users);
        list.setAdapter(adapter);
        tableLayout=view.findViewById(R.id.tablelayout);
        list.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

            }

            @Override
            public void onDownMotionEvent() {

            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {
                if (ab == null) {
                    return;
                }
                if (scrollState == ScrollState.UP) {
                    if (ab.isShowing()) {
                        ab.hide();
                        tableLayout.setVisibility(View.GONE);
                    }
                } else if (scrollState == ScrollState.DOWN) {
                    if (!ab.isShowing()) {
                        ab.show();
                        tableLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        return view;
    }



}
