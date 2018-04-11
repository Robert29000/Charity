package com.example.melikyan.charity.CreatingAnnoucment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.melikyan.charity.BitmapHelper;
import com.example.melikyan.charity.R;

public class ShowFullSizeImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_full_size_image);
        Intent intent=getIntent();
        Bitmap bit=BitmapFactory.decodeFile(intent.getStringExtra("Image"));
        bit= BitmapHelper.modifyOrientation(bit,intent.getStringExtra("Image"));
        ImageView view=findViewById(R.id.showimage);
        view.setImageBitmap(bit);
    }

}
