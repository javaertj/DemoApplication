package com.ykbjson.demo.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;

import com.drivingassisstantHouse.library.MApplication;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ykbjson.demo.R;

import java.io.File;

/**
 * 包名：com.ykbjson.demo.app
 * 描述：
 * 创建者：yankebin
 * 日期：2016/3/14
 */
public class DemoApplication extends MApplication {
    private static DemoApplication instance;
    private BaseHandler baseHandler = new BaseHandler();

    public static DemoApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init(this);
    }

    @Override
    public void exit() {

    }

    @Override
    public void backToLogin() {

    }

    public synchronized BaseHandler obtain() {
        return baseHandler;
    }

    private void init(Context context) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.empty_photo)// 空uri时的默认图片
                .showImageOnFail(R.drawable.empty_photo)// 加载失败时的默认图片
                .cacheInMemory(true)// 是否缓存到内存
                .cacheOnDisk(true)// 是否缓存到磁盘
                .bitmapConfig(Bitmap.Config.RGB_565)// 图片格式比RGB888少消耗2倍内存
                .imageScaleType(ImageScaleType.EXACTLY)// 图片缩放方式
                .build();

        // 获取到缓存的目录地址
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "ImageLoader/Cache");
        // 创建配置ImageLoader,可以设定在Application，设置为全局的配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaultOptions)
//		.memoryCacheExtraOptions(480, 800)//缓存文件的最大长宽，默认屏幕宽高
                // Can slow ImageLoader, use it carefully (Better don't use it)设置缓存的详细信息，最好不要设置这个
                // .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75,null)
                .threadPoolSize(3)// 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)// 线程优先级
        /*
         *当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
		 * When you display an image in a small ImageView and later you
		 * try to display this image (from identical URI) in a larger
		 * ImageView so decoded image of bigger size will be cached in
		 * memory as a previous decoded image of smaller size. So the
		 * default behavior is to allow to cache multiple sizes of one
		 * image in memory. You can deny it by calling this method: so
		 * when some image will be cached in memory then previous cached
		 * size of this image (if it exists) will be removed from memory
		 * cache before.
		 */
                // .denyCacheImageMultipleSizesInMemory()
                // You can pass your own memory cache implementation
                .memoryCache(new UsingFreqLimitedMemoryCache(10 * 1024 * 1024))
//                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(10 * 1024 * 1024)//内存缓存20MB
                .diskCacheSize(50 * 1024 * 1024)// 硬盘缓存50MB
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())// 将保存的时候的URI名称用MD5
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100)// 缓存的File数量
                .diskCache(new UnlimitedDiskCache(cacheDir))// 自定义缓存路径
//                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs()
                .build();

        // 全局初始化此配置
        ImageLoader.getInstance().init(config);

    }

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(base);
        super.attachBaseContext(base);
    }

    private static class BaseHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    }

}
