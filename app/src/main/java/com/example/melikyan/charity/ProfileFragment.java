package com.example.melikyan.charity;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by melikyan on 08.01.18.
 */

public class ProfileFragment extends Fragment {
    private EditText name,fam,reg,age;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.profile,null);
        name= v.findViewById(R.id.name);
        fam=v.findViewById(R.id.familia);
        reg=v.findViewById(R.id.Reg);
        age=v.findViewById(R.id.Age);
        SQLiteDatabase ann=getActivity().openOrCreateDatabase("charity.db",Context.MODE_PRIVATE,null);
        Cursor query=ann.rawQuery("SELECT * FROM user",null);
        if(query.moveToNext()) {
            name.setText(query.getString(1));
            fam.setText(query.getString(2));
            reg.setText(query.getString(3));
            age.setText(query.getString(4));
        }
        ann.close();
        return v;
    }
}
