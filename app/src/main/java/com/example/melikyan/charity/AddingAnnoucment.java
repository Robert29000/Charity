package com.example.melikyan.charity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import static com.example.melikyan.charity.R.id.toolbar;

public class AddingAnnoucment extends AppCompatActivity {
    private AutoCompleteTextView domain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_annoucment);
        Toolbar mytoolbar=findViewById(toolbar);
        setSupportActionBar(mytoolbar);
        String[] domains=getResources().getStringArray(R.array.domains);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,domains);
        domain=findViewById(R.id.chooseDomain);
        domain.setAdapter(adapter);
    }

    static final int REQUEST_TAKE_PHOTO = 1;


    public void InsertImage(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            switch (view.getId()) {
                case R.id.imageView1:
                    chooseView = 1;
                    break;
                case R.id.imageView2:
                    chooseView = 2;
                    break;
                case R.id.imageView3:
                    chooseView = 3;
                    break;
            }
            File photoFile = null;
            try {
                photoFile = createFile();
            } catch (IOException e) {

            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.example.melikyan.charity.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    private int chooseView;
    private String mCurrentPhotPath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            AddtoGalPic();
            ImageView image=null;
            switch(chooseView){
                case 1:image=findViewById(R.id.imageView1);
                        break;
                case 2:image=findViewById(R.id.imageView2);
                    break;
                case 3:image=findViewById(R.id.imageView3);
                    break;
            }
            int targetW=image.getWidth();
            int targetH=image.getHeight();
            BitmapFactory.Options bmOption=new BitmapFactory.Options();
            bmOption.inJustDecodeBounds=true;
            BitmapFactory.decodeFile(mCurrentPhotPath,bmOption);
            int photoW = bmOption.outWidth;
            int photoH = bmOption.outHeight;
            int scaleFactor=Math.min(photoW/targetW,photoH/targetH);
            bmOption.inJustDecodeBounds=false;
            bmOption.inSampleSize=scaleFactor;
            Bitmap bitmap=BitmapFactory.decodeFile(mCurrentPhotPath,bmOption);
            try {
                bitmap=makeNormalBit(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            image.setBackground(null);
            image.setImageBitmap(bitmap);
        }
    }
    private File createFile() throws IOException{
        String fileName="photo_"+System.currentTimeMillis();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(fileName,".jpg",storageDir);
        mCurrentPhotPath=image.getAbsolutePath();
        return image;
    }
    private void AddtoGalPic(){
        Intent mediaScanIntent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f=new File(mCurrentPhotPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    private Bitmap makeNormalBit(Bitmap bm) throws IOException{
        ExifInterface ei = new ExifInterface(mCurrentPhotPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap rotatedBitmap=null;
        switch(orientation){
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap= rotateImage(bm, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bm, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bm, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bm;
        }
        return rotatedBitmap;
    }
    private Bitmap rotateImage(Bitmap source,float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
