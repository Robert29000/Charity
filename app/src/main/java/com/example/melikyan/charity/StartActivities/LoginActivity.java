package com.example.melikyan.charity.StartActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
        final EditText email=findViewById(R.id.eMail);
        final EditText password=findViewById(R.id.passWord);
        final Button login=findViewById(R.id.Enter);
        final Button reg=findViewById(R.id.CreateProf);
        String emailtext=email.getText().toString();
        String passtext=password.getText().toString();
        if(emailtext.equals("") || passtext.equals(""))
            new Toast(this).makeText(this,"Заполните все поля",Toast.LENGTH_SHORT).show();
        else {
            mAuth.signInWithEmailAndPassword(emailtext,passtext).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        email.setFocusable(false);
                        password.setFocusable(false);
                        login.setClickable(false);
                        reg.setClickable(false);
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
