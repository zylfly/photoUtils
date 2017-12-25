# photoUtils
封装了Android原生的选择图片，拍照（支持Android 7.0以及解决部分小米选择图片崩溃的问题）

### 1、在自己项目中添加本项目依赖：

    compile 'com.github.zylRookie:photoUtils:1.0.1'

### 2、在根目录中添加：

    allprojects {
        repositories {
           ...
          maven {
              url "https://jitpack.io"
          }
       }
    }
  
 ### 3、在项目中使用：
 
  a . 在onCreate中初始化PhotoUtils 不带设置参数（上下文，是否裁剪，选取图片监听）
      
       PhotoUtils.getInstance().init(this, true, new PhotoUtils.OnSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                // 当拍照或从图库选取图片成功后回调
                Glide.with(MainActivity.this).load(outputUri).into(mIvPic);
            }
        });
        
  b . 在onCreate中初始化PhotoUtils 带设置参数（上下文，图片裁剪时的宽度比例，图片裁剪时的高度比例，图片裁剪后的宽度，图片裁剪后的高度，选取图片监                                  听）
  
       PhotoUtils.getInstance().initParm(this, 2, 1, 400, 400
                , new PhotoUtils.OnSelectListener() {
                    @Override
                    public void onFinish(File outputFile, Uri outputUri) {
                        // 当拍照或从图库选取图片成功后回调
                        Glide.with(MainActivity.this).load(outputUri).into(mIvPic);
                    }
                });
                
  c . 关联Activity的onActivityResult（）方法
   
       @Override
       protected void onActivityResult(int requestCode, int resultCode, Intent data) {
           super.onActivityResult(requestCode, resultCode, data);
          PhotoUtils.getInstance().bindForResult(requestCode, resultCode, data);
       }
       
  d . 调用拍照和选择图片的方法
   
        PhotoUtils.getInstance().takePhoto();
        
        PhotoUtils.getInstance().selectPhoto();
        
 ### end、提醒：
 
 
   别忘记权限申请，项目中使用的是EasyPermissions权限申请，你也可以使用自己熟悉的！！！
     
     

