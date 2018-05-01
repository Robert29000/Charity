package com.example.melikyan.charity.StartActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.melikyan.charity.FirebaseManager;
import com.example.melikyan.charity.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;

import static com.example.melikyan.charity.R.id.toolbar;

public class LoginActivity extends AppCompatActivity {
    protected static FirebaseAuth mAuth;
    private StorageReference storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar mytoolbar=findViewById(toolbar);
        setSupportActionBar(mytoolbar);
        mAuth=FirebaseAuth.getInstance();
    }

    public void Enter(View view) {
        EditText email=findViewById(R.id.eMail);
        EditText password=findViewById(R.id.passWord);
        String emailtext=email.getText().toString();
        String passtext=password.getText().toString();
        if(emailtext.equals("") || passtext.equals(""))
            new Toast(this).makeText(this,"Заполните все поля",Toast.LENGTH_SHORT).show();
        else {
            mAuth.signInWithEmailAndPassword(emailtext,passtext).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseManager.GettingData(LoginActivity.this,LoginActivity.this);
                        FirebaseManager.GetUserInfo(mAuth.getCurrentUser());
                    }else new Toast(LoginActivity.this).makeText(LoginActivity.this,"Неверный логин или пароль",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void CreateProfile(View view) {
        Intent intent=new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}
