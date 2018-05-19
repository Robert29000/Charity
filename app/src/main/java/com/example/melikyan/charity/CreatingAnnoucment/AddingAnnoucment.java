package com.example.melikyan.charity.CreatingAnnoucment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.melikyan.charity.Data.CreatingAnnInfo;
import com.example.melikyan.charity.BitmapHelper;
import com.example.melikyan.charity.Manifest;
import com.example.melikyan.charity.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.os.Build.VERSION_CODES.M;
import static com.example.melikyan.charity.R.id.toolbar;

public class AddingAnnoucment extends AppCompatActivity implements View.OnLongClickListener {
    private File[] files;
    private String[] paths;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_annoucment);
        Toolbar mytoolbar=findViewById(toolbar);
        setSupportActionBar(mytoolbar);
        files=new File[3];
        paths=new String[3];
        for(int i=0;i<files.length;i++){
            try {
                files[i]=createFile();
                paths[i]=files[i].getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ImageView image1=findViewById(R.id.imageView1);
        ImageView image2=findViewById(R.id.imageView2);
        ImageView image3=findViewById(R.id.imageView3);
        image1.setOnLongClickListener(this);
        image2.setOnLongClickListener(this);
        image3.setOnLongClickListener(this);
    }

    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_GET_FROM_GALLERY=2;
    private static final int REQUEST_PERMISSION=10;

    public void InsertImage(View view) {
        switch (view.getId()) {
            case R.id.imageView1:
                chooseView = 0;
                break;
            case R.id.imageView2:
                chooseView = 1;
                break;
            case R.id.imageView3:
                chooseView = 2;
                break;
        }
        ImageView image=(ImageView)view;
        if(image.getBackground()!=null) {
            final String[] actions = {"Снять фото", "Выбрать фото из галлереи"};
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Выберите действие");
            dialog.setItems(actions, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    switch (item) {
                        case 0:
                            MakePicture();
                            break;
                        case 1:
                            getPictureFromGallery();
                    }
                }
            });
            dialog.show();
        }else{
            Intent intent=new Intent(this,ShowFullSizeImage.class);
            intent.putExtra("Image",paths[chooseView]);
            startActivity(intent);
        }
    }
    private int chooseView;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            ImageView image=null;
        switch(chooseView){
            case 0:image=findViewById(R.id.imageView1);
                break;
            case 1:image=findViewById(R.id.imageView2);
                break;
            case 2:image=findViewById(R.id.imageView3);
                break;
        }
            int targetW = image.getWidth();
            int targetH = image.getHeight();
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            BitmapFactory.decodeFile(paths[chooseView],options);
            int photoW = options.outWidth;
            int photoH = options.outHeight;
            int scaleFactor =Math.min(photoW/targetW,photoH/targetH);
            options.inJustDecodeBounds=false;
            options.inSampleSize=scaleFactor;
            options.inPurgeable=true;
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bitmap newbitmap=BitmapFactory.decodeFile(paths[chooseView],options);
            newbitmap=BitmapHelper.modifyOrientation(newbitmap,paths[chooseView]);
            image.setBackground(null);
            image.setImageBitmap(newbitmap);
            FileOutputStream out = new FileOutputStream(files[chooseView]);
            newbitmap.compress(Bitmap.CompressFormat.JPEG,40,out);
            out.flush();
            out.close();
        }else if(requestCode==REQUEST_GET_FROM_GALLERY && resultCode==RESULT_OK) {
            Uri pickedImage = data.getData();
            String result;
            Cursor cursor = getContentResolver().query(pickedImage, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path
                result = pickedImage.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
                cursor.close();
            }
            Bitmap newbitmap = BitmapFactory.decodeFile(result,options);
            newbitmap = BitmapHelper.modifyOrientation(newbitmap, result);
            image.setBackground(null);
            image.setImageBitmap(newbitmap);
            FileOutputStream out = new FileOutputStream(files[chooseView]);
            newbitmap.compress(Bitmap.CompressFormat.JPEG, 10, out);
            out.flush();
            out.close();
          }
        } catch (IOException e) {
                e.printStackTrace();
        }
        hasDrawable[chooseView]=true;
    }
    private File createFile() throws IOException{
        String fileName="photo_"+System.currentTimeMillis();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(fileName,".jpg",storageDir);
        return image;
    }


    private void MakePicture(){
        if(Build.VERSION.SDK_INT>=M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_PERMISSION);
            }
        }else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(this, "com.example.melikyan.charity.fileprovider", files[chooseView]);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        }
    }
    private void getPictureFromGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_GET_FROM_GALLERY);
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
                for(int i=0;i<3;i++){
                    ImageView image;
                    switch(i){
                        case 0:image=findViewById(R.id.imageView1);
                            if(image.getBackground()==null)
                                CreatingAnnInfo.bits.add(files[i]);
                            else {
                                File f=new File(paths[i]);
                                boolean deleted=f.delete();
                            }
                            break;
                        case 1:image=findViewById(R.id.imageView2);
                            if(image.getBackground()==null)
                                CreatingAnnInfo.bits.add(files[i]);
                            else {
                                File f=new File(paths[i]);
                                boolean deleted=f.delete();
                            }
                            break;
                        case 2:image=findViewById(R.id.imageView3);
                            if(image.getBackground()==null)
                                CreatingAnnInfo.bits.add(files[i]);
                            else {
                                File f=new File(paths[i]);
                                boolean deleted=f.delete();
                            }
                            break;
                    }
                }
                EditText name=findViewById(R.id.invalidName);
                if(name.getText().toString().equals("")){
                    new Toast(this).makeText(this,"Заполните все поля",Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(this, WritingText.class);
                    startActivity(intent);
                }
                return true;
            default:return super.onOptionsItemSelected(item);
        }
    }


    private boolean[] hasDrawable=new boolean[3];
    @Override
    public boolean onLongClick(View v) {
        final ImageView image=(ImageView)v;
        switch (v.getId()) {
            case R.id.imageView1:
                chooseView = 0;
                break;
            case R.id.imageView2:
                chooseView = 1;
                break;
            case R.id.imageView3:
                chooseView = 2;
                break;
        }
        if(hasDrawable[chooseView]) {
            final String[] actions = {"Переснять фото", "Выбрать другое фото","Удалить фото"};
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Выберите действие");
            dialog.setItems(actions, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    switch (item) {
                        case 0:
                            MakePicture();
                            break;
                        case 1:
                            getPictureFromGallery();
                        case 2:image.setImageBitmap(null);
                               image.setBackground(getResources().getDrawable(R.drawable.image_border,null));
                               image.setImageResource(R.drawable.ic_plus_svg);
                               hasDrawable[chooseView]=false;
                            break;
                    }
                }
            });
            dialog.show();
            return false;
        }
        else return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        EditText nam=findViewById(R.id.invalidName);
        CreatingAnnInfo.name=nam.getText().toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_PERMISSION){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoUri = FileProvider.getUriForFile(this, "com.example.melikyan.charity.fileprovider", files[chooseView]);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }else{

            }
        }
    }
}
