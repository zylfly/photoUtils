package com.zyl.photoselectutils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zyl.photoutils.PhotoUtils;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private TextView mTvPath, mTvUri;
    private ImageView mIvPic;

    String[] takePhotoPerms = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA};
    String[] selectPhotoPerms = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button) findViewById(R.id.btnTakePhoto)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnSelectPhoto)).setOnClickListener(this);
        mTvPath = (TextView) findViewById(R.id.tvPath);
        mTvUri = (TextView) findViewById(R.id.tvUri);
        mIvPic = (ImageView) findViewById(R.id.ivPic);

        //（上下文，是否裁剪，选取图片监听）
        PhotoUtils.getInstance().init(this, true, new PhotoUtils.OnSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                // 4、当拍照或从图库选取图片成功后回调
                mTvPath.setText(outputFile.getAbsolutePath());
                mTvUri.setText(outputUri.toString());
                Glide.with(MainActivity.this).load(outputUri).into(mIvPic);
            }
        });

        //或者使用设置带参数（上下文，图片裁剪时的宽度比例，图片裁剪时的高度比例，图片裁剪后的宽度，图片裁剪后的高度，选取图片监听）
//        PhotoUtils.getInstance().initParm(this, 2, 1, 400, 400
//                , new PhotoUtils.OnSelectListener() {
//                    @Override
//                    public void onFinish(File outputFile, Uri outputUri) {
//                        // 4、当拍照或从图库选取图片成功后回调
//                        mTvPath.setText(outputFile.getAbsolutePath());
//                        mTvUri.setText(outputUri.toString());
//                        Glide.with(MainActivity.this).load(outputUri).into(mIvPic);
//                    }
//                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnTakePhoto:
                checkPermission(takePhotoPerms, 1);
                break;
            case R.id.btnSelectPhoto:
                checkPermission(selectPhotoPerms, 2);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 2、在Activity中的onActivityResult()方法里与之关联
        PhotoUtils.getInstance().bindForResult(requestCode, resultCode, data);
    }


    //-----------------------------------------权限----------------------------------------------------------

    private void checkPermission(String[] perms, int requestCode) {
        if (EasyPermissions.hasPermissions(this, perms)) {//已经有权限了
            switch (requestCode) {
                case 1:
                    PhotoUtils.getInstance().takePhoto();
                    break;
                case 2:
                    PhotoUtils.getInstance().selectPhoto();
                    break;
            }
        } else {//没有权限去请求
            EasyPermissions.requestPermissions(this, "权限", requestCode, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {//设置成功
        switch (requestCode) {
            case 1:
                PhotoUtils.getInstance().takePhoto();
                break;
            case 2:
                PhotoUtils.getInstance().selectPhoto();
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("权限设置")
                    .setPositiveButton("设置")
                    .setRationale("当前应用缺少必要权限,可能会造成部分功能受影响！请点击\"设置\"-\"权限\"-打开所需权限。最后点击两次后退按钮，即可返回")
                    .build()
                    .show();
        }
    }
}
