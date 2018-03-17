package com.example.melikyan.charity.CreatingAnnoucment;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import com.example.melikyan.charity.AnnotationInfo;
import com.example.melikyan.charity.CreatingAnnoucment.finish_creating_annouc;
import com.example.melikyan.charity.R;

import static com.example.melikyan.charity.R.id.toolbar;

public class MakingAnnotation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_annotation);
        Toolbar mytoolbar=findViewById(toolbar);
        setSupportActionBar(mytoolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_application, menu);
        return super.onCreateOptionsMenu(menu);
    }

        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.next:
                EditText ed=findViewById(R.id.makeAnnouc);
                if(ed.getText().toString().equals("")){
                    new Toast(this).makeText(this,"Напишите краткое описание",Toast.LENGTH_LONG).show();
                }else {
                    AnnotationInfo.anottext = ed.getText().toString();
                    Intent intent = new Intent(this, finish_creating_annouc.class);
                    startActivity(intent);
                    return true;
                }
            default:return super.onOptionsItemSelected(item);

        }
    }
}
