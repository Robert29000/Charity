package com.example.melikyan.charity.CreatingAnnoucment;


import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.melikyan.charity.Data.CreatingAnnInfo;
import com.example.melikyan.charity.MainApplication.ApplicationActivity;
import com.example.melikyan.charity.OnMoneyChangedService;
import com.example.melikyan.charity.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.example.melikyan.charity.R.id.toolbar;

public class FinishAnnoucment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_creating_annouc);
        Toolbar mytoolbar=findViewById(toolbar);
        setSupportActionBar(mytoolbar);
    }

    public void finish(View view) {
        final EditText text=findViewById(R.id.moneyneeded);
        final EditText wallet=findViewById(R.id.cardnumber);
        if(text.getText().toString().equals("") || wallet.getText().toString().equals("")){
            new Toast(this).makeText(this,"Введите необходимую сумму денег",Toast.LENGTH_LONG).show();
        }else {
            CreatingAnnInfo.moneyneeded = Integer.parseInt(text.getText().toString());
            CreatingAnnInfo.wallet= Long.parseLong(wallet.getText().toString());
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            final FirebaseUser user = mAuth.getCurrentUser();
            final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            StorageReference storage=FirebaseStorage.getInstance().getReference();
            final StorageReference imagesRef = storage.child("images");
            ValueEventListener listener=new ValueEventListener(){

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long counter = (long) dataSnapshot.getValue();
                    final long numberofan = counter + 1;
                    database.child("Users").child(user.getUid()).child("numberOfAnnouc").setValue(numberofan);
                    database.child("Annoucments").child(user.getUid()+"-"+numberofan).child("name").setValue(CreatingAnnInfo.name);
                    database.child("Annoucments").child(user.getUid()+"-"+numberofan).child("domain").setValue(CreatingAnnInfo.domain);
                    database.child("Annoucments").child(user.getUid()+"-"+numberofan).child("text").setValue(CreatingAnnInfo.anottext);
                    database.child("Annoucments").child(user.getUid()+"-"+numberofan).child("moneyneeded").setValue(CreatingAnnInfo.moneyneeded);
                    database.child("Annoucments").child(user.getUid()+"-"+numberofan).child("moneyincome").setValue(0);
                    database.child("Annoucments").child(user.getUid()+"-"+numberofan).child("imagescount").setValue(CreatingAnnInfo.bits.size());
                    database.child("Annoucments").child(user.getUid()+"-"+numberofan).child("yandexwallet").setValue(CreatingAnnInfo.wallet);
                    StorageReference anRef=imagesRef.child(user.getUid()+"-"+numberofan);
                    UploadTask task;
                    final ProgressBar bar=findViewById(R.id.progressbar);
                    bar.setVisibility(View.VISIBLE);
                    bar.setMax(CreatingAnnInfo.bits.size());
                    final int max= CreatingAnnInfo.bits.size()-1;
                    if(CreatingAnnInfo.bits.size()==0){
                        Intent intent = new Intent(FinishAnnoucment.this, ApplicationActivity.class);
                        startActivity(intent);
                    }else {
                        for (int i = 0; i < CreatingAnnInfo.bits.size(); i++) {
                            final int courent = i;
                            Uri file = Uri.fromFile(CreatingAnnInfo.bits.get(i));
                            StorageReference filesRef = anRef.child("image" + "-" + i);
                            task = filesRef.putFile(file);
                            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    bar.incrementProgressBy(1);
                                    if (courent == max) {
                                        CreatingAnnInfo.bits.clear();
                                        Intent serviceIntent=new Intent(getApplicationContext(), OnMoneyChangedService.class);
                                        serviceIntent.putExtra("UID",user.getUid()+"-"+numberofan);
                                        startService(serviceIntent);
                                        Intent intent = new Intent(FinishAnnoucment.this, ApplicationActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                            task.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    new Toast(FinishAnnoucment.this).makeText(FinishAnnoucment.this,
                                            "Не удалось загрузить файлы,проверьте подлючение",
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            DatabaseReference data=FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("numberOfAnnouc");
            data.addListenerForSingleValueEvent(listener);
            data.removeEventListener(listener);
        }
    }
}
