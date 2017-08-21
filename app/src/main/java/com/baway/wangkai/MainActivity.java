package com.baway.wangkai;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText editTheme;
    private EditText editXiangQing;
    private Button btnDingWei;
    private ImageView imageXiangCe;
    private int GET_PHOTO = 1;    //取照片
    private Bitmap bitmap;
    private ImageView imageView;
    private int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String address = getIntent().getStringExtra("address");
        editTheme = (EditText) findViewById(R.id.edit_theme);
        editXiangQing = (EditText) findViewById(R.id.edit_xiangqing);
        btnDingWei = (Button) findViewById(R.id.btn_dingwei);
        imageXiangCe = (ImageView) findViewById(R.id.image_xiangce);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count == 0){
                    count += 1;
                    Toast.makeText(MainActivity.this, ""+count, Toast.LENGTH_SHORT).show();
                }else if (count == 1){
                    Toast.makeText(MainActivity.this, "发生了异常", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(CrashHandler.TAG + "自己抛出的异常");

                }
            }
        });

        btnDingWei.setOnClickListener(this);
        btnDingWei.setText(address);
        imageXiangCe.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dingwei:
                Intent intent1 = new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent1);
                break;
            case R.id.image_xiangce:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, GET_PHOTO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GET_PHOTO) {
            ContentResolver resolver = getContentResolver();
            // 获得图片的uri
            Uri originalUri = data.getData();
            bitmap = null;
            try {
                Bitmap originalBitmap = BitmapFactory.decodeStream(resolver
                        .openInputStream(originalUri));
                bitmap = originalBitmap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (bitmap != null) {
                insertIntoEditText(getBitmapMime(bitmap, originalUri));
            } else {
                Toast.makeText(MainActivity.this, "获取图片失败",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private SpannableString getBitmapMime(Bitmap pic, Uri uri) {
        String path = uri.getPath();
        SpannableString ss = new SpannableString(path);
        ImageSpan span = new ImageSpan(this, pic);
        ss.setSpan(span, 0, path.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
    private void insertIntoEditText(SpannableString ss) {
        Editable et = editXiangQing.getText();// 先获取Edittext中的内容
        int start = editXiangQing.getSelectionStart();
        et.insert(start, ss);// 设置ss要添加的位置
        editXiangQing.setText(et);// 把et添加到Edittext中
        editXiangQing.setSelection(start + ss.length());// 设置Edittext中光标在最后面显示
    }
}
