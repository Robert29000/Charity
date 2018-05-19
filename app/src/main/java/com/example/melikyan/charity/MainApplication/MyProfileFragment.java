package com.example.melikyan.charity.MainApplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.melikyan.charity.FirebaseManager;
import com.example.melikyan.charity.OnMoneyChangedService;
import com.example.melikyan.charity.R;
import com.example.melikyan.charity.StartActivities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by melikyan on 20.02.2018.
 */

public class MyProfileFragment extends Fragment implements View.OnClickListener{
    private EditText myInitials;
    private Button button;
    private DatabaseReference mDataBase;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change:
                myInitials.setEnabled(true);
                button.setVisibility(View.VISIBLE);
                return true;
            case R.id.quit:
                FirebaseAuth.getInstance().signOut();
                getActivity().stopService(new Intent(getActivity(), OnMoneyChangedService.class));
                Intent intent=new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                return true;
           default: return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.profile,container,false);
        button=view.findViewById(R.id.save);
        myInitials=view.findViewById(R.id.initials);
        myInitials.setText(FirebaseManager.currentUserInfo.name+" "+FirebaseManager.currentUserInfo.lastname);
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        mDataBase= FirebaseDatabase.getInstance().getReference();
        String in=myInitials.getText().toString();
        mDataBase.child("Users").child(user.getUid()).child("name").setValue(in.substring(0,in.lastIndexOf(" ")));
        mDataBase.child("Users").child(user.getUid()).child("lastname").setValue(in.substring(in.lastIndexOf(" "),in.length()));
        myInitials.setEnabled(false);
        button.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        myInitials.setEnabled(false);
        button.setVisibility(View.INVISIBLE);
        myInitials.setText(FirebaseManager.currentUserInfo.name+" "+FirebaseManager.currentUserInfo.lastname);
    }
}
