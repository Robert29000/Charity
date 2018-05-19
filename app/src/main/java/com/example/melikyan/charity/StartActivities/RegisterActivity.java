package com.example.melikyan.charity.StartActivities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.melikyan.charity.FirebaseManager;
import com.example.melikyan.charity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class RegisterActivity extends AppCompatActivity {
    private DatabaseReference mDataBase;
    private EditText name,lastname;
    private FirebaseAuth mAuth;
    private StorageReference storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar mytoolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mytoolbar);

    }

    public void CreateProfile(View view) {
        final EditText email=findViewById(R.id.mail);
        final EditText password=findViewById(R.id.Passwd);
        final String mail=email.getText().toString();
        String pass=password.getText().toString();
        name=findViewById(R.id.Name);
        lastname=findViewById(R.id.LastName);
        final Button button =findViewById(R.id.button2);
        mAuth=FirebaseAuth.getInstance();
        if(mail.equals("") || pass.equals("") ||
                name.getText().toString().equals("") ||
                lastname.getText().toString().equals("")){
            new Toast(RegisterActivity.this).makeText(RegisterActivity.this,"Заполните все поля",Toast.LENGTH_LONG).show();
        }else{
            mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user=mAuth.getCurrentUser();
                        mDataBase = FirebaseDatabase.getInstance().getReference();
                        mDataBase.child("Users").child(user.getUid()).child("email").setValue(mail);
                        mDataBase.child("Users").child(user.getUid()).child("name").setValue(name.getText().toString());
                        mDataBase.child("Users").child(user.getUid()).child("lastname").setValue(lastname.getText().toString());
                        mDataBase.child("Users").child(user.getUid()).child("numberOfAnnouc").setValue(0);
                        email.setFocusable(false);
                        password.setFocusable(false);
                        name.setFocusable(false);
                        lastname.setFocusable(false);
                        button.setClickable(false);
                        FirebaseManager.GettingData(RegisterActivity.this,RegisterActivity.this);
                        FirebaseManager.GetUserInfo(user);
                    } else
                        new Toast(RegisterActivity.this).makeText(RegisterActivity.this, "Почтовый адресс уже используется", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
