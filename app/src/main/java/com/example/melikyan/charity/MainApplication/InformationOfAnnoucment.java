package com.example.melikyan.charity.MainApplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.melikyan.charity.Adapters.ImageAdapter;
import com.example.melikyan.charity.BitmapHelper;
import com.example.melikyan.charity.StartActivities.MainActivity;
import com.example.melikyan.charity.R;
import com.example.melikyan.charity.UsersAnnotations;
import com.example.melikyan.charity.Web;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yandex.money.api.methods.InstanceId;
import com.yandex.money.api.methods.payment.ProcessExternalPayment;
import com.yandex.money.api.methods.payment.RequestExternalPayment;
import com.yandex.money.api.net.clients.ApiClient;
import com.yandex.money.api.net.clients.DefaultApiClient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InformationOfAnnoucment extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final String client_id="F1C0CD270B37CA6AC828AA46D98648A5493CD63F99BF0AA0C9CAEC8641A8474C";
    final private ArrayList<Bitmap> bitmaps=new ArrayList<>();
    UsersAnnotations users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_of_annoucment);
        users=getIntent().getParcelableExtra("VALUE");
        bitmaps.add(BitmapFactory.decodeFile(users.bimapUri));
        ProgressBar bar=findViewById(R.id.progressbar);
        bar.setMax(users.moneyneeded);
        bar.setProgress(users.moneyincome);
        TextView name=findViewById(R.id.name_of_ann);
        name.setText(users.name);
        TextView text=findViewById(R.id.text_of_ann);
        text.setText(users.text);
        mRecyclerView=findViewById(R.id.imagepager);
        mLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        final TextView moneyprogress=findViewById(R.id.mnprogress);
        moneyprogress.setText(users.moneyincome+"/"+users.moneyneeded);
        final ProgressBar waitingBar=findViewById(R.id.waitingbar);
        waitingBar.setVisibility(View.VISIBLE);
        Log.d("EASY",users.imagescount+"");
        final StorageReference storage=FirebaseStorage.getInstance().getReference();
        try {
            switch (users.imagescount) {
                case 1:
                    mAdapter = new ImageAdapter(bitmaps);
                    mRecyclerView.setAdapter(mAdapter);
                    waitingBar.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    StorageReference ref = storage.child("images/"+users.uid + "/" + "image-1");
                    final File localFile=File.createTempFile("Images"+System.currentTimeMillis(),".jpg");
                    ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            bitmaps.add(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                            mAdapter = new ImageAdapter(bitmaps);
                            mRecyclerView.setAdapter(mAdapter);
                            waitingBar.setVisibility(View.INVISIBLE);
                        }
                    });
                    break;
                case 3:ref = storage.child("images/"+users.uid + "/" + "image-1");
                    final File localfile=File.createTempFile("Images"+System.currentTimeMillis(),".jpg");
                    ref.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            bitmaps.add(BitmapFactory.decodeFile(localfile.getAbsolutePath()));
                        }
                    });
                    ref = storage.child("images/"+users.uid + "/" + "image-2");
                    ref.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            bitmaps.add(BitmapFactory.decodeFile(localfile.getAbsolutePath()));
                            mAdapter = new ImageAdapter(bitmaps);
                            mRecyclerView.setAdapter(mAdapter);
                            waitingBar.setVisibility(View.INVISIBLE);
                        }
                    });
                    break;
            }
        }catch (IOException e){

        }
    }

    public void Donate(View view) {
            final EditText text=new EditText(this);
            final AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setMessage("Введите сумму для зачисления");
            alert.setView(text);
            alert.setPositiveButton("Продолжить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ApiClient client = new DefaultApiClient.Builder().setClientId(client_id).create();
                                InstanceId instanceId = client.execute(new InstanceId.Request(client_id));
                                Log.d("EASY", instanceId.toString());
                                Map<String, String> map = new HashMap<>();
                                map.put("to", users.wallet+"");
                                map.put("amount", text.getText().toString());
                                RequestExternalPayment request = client.execute(RequestExternalPayment.Request.newInstance(instanceId.instanceId, "p2p",
                                        map));
                                Log.d("EASY", request.toString());
                                ProcessExternalPayment payment = client.execute(new ProcessExternalPayment.Request(
                                        instanceId.instanceId, request.requestId, "https://money.yandex.ru",
                                        "https://money.yandex.ru",true));
                                Log.d("EASY", payment.toString());
                                String post="";
                                for(String key:payment.acsParams.keySet()){
                                    post=post+key+"="+map.get(key)+"&";
                                }
                                post=post.substring(0, post.length()-1);
                                Intent intent = new Intent(InformationOfAnnoucment.this, Web.class);
                                intent.putExtra("URI",payment.acsUri);
                                intent.putExtra("DATA",post);
                                startActivity(intent);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }).start();
                }
            });
            alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
    }


}
