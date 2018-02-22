package com.example.melikyan.charity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private DatabaseReference mDataBase;
    private EditText name,lastname,age;
    private AutoCompleteTextView region;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar mytoolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mytoolbar);
        region=findViewById(R.id.Region);
        String[] countries=getResources().getStringArray(R.array.countries_array);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,countries);
        region.setAdapter(adapter);
    }

    public void CreateProfile(View view) {
        EditText email=findViewById(R.id.mail);
        EditText password=findViewById(R.id.Passwd);
        final String mail=email.getText().toString();
        String pass=password.getText().toString();
        name=findViewById(R.id.Name);
        lastname=findViewById(R.id.LastName);
        age=findViewById(R.id.Age);
        region=findViewById(R.id.Region);
        mAuth=FirebaseAuth.getInstance();
        if(mail.equals("") || pass.equals("") ||
                name.getText().toString().equals("") ||
                lastname.getText().toString().equals("") || age.getText().toString().equals("") ||
                region.getText().toString().equals("")){
            new Toast(RegisterActivity.this).makeText(RegisterActivity.this,"Заполните все поля",Toast.LENGTH_LONG).show();
        }else{
            mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isComplete()) {
                        FirebaseUser user=mAuth.getCurrentUser();
                        mDataBase = FirebaseDatabase.getInstance().getReference();
                        mDataBase.child("Users").child(user.getUid()).child("email").setValue(mail);
                        mDataBase.child("Users").child(user.getUid()).child("name").setValue(name.getText().toString());
                        mDataBase.child("Users").child(user.getUid()).child("lastname").setValue(lastname.getText().toString());
                        mDataBase.child("Users").child(user.getUid()).child("region").setValue(region.getText().toString());
                        mDataBase.child("Users").child(user.getUid()).child("age").setValue(age.getText().toString());
                        Intent intent=new Intent(RegisterActivity.this,ApplicationActivity.class);
                        startActivity(intent);
                    } else
                        new Toast(RegisterActivity.this).makeText(RegisterActivity.this, "Почтовый адресс уже используется", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
