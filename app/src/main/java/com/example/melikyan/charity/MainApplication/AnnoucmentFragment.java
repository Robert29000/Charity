package com.example.melikyan.charity.MainApplication;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.melikyan.charity.AnnoucAdapter;
import com.example.melikyan.charity.CreatingAnnoucment.AddingAnnoucment;
import com.example.melikyan.charity.R;
import com.example.melikyan.charity.UsersAnnotations;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class AnnoucmentFragment extends Fragment implements View.OnClickListener{
    ListView list;
    static public ArrayList<UsersAnnotations> users=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.annoucments,container,false);
        Button button=view.findViewById(R.id.addannoucment);
        button.setOnClickListener(this);
        list=view.findViewById(R.id.annouclists);
        AnnoucAdapter adapter=new AnnoucAdapter(getContext(),users);
        list.setAdapter(adapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        AnnoucAdapter adapter=new AnnoucAdapter(getContext(),users);
        list.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getContext(),AddingAnnoucment.class);
        startActivity(intent);
    }


}
