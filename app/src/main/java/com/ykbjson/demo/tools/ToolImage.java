package com.ykbjson.demo.tools;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ykbjson.demo.R;

import java.io.File;

/**
 * 图片加载工具类
 *
 * @author sunji
 * @version 1.0
 */
public abstract class ToolImage {

    /**
     * 初始化ImageLoader
     *
     * @param context 上下文，建议用Application
     * @return
     */
    public static void init(Context context) {

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
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
//                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(2 * 1024 * 1024)//内存缓存20MB
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

    /**
     * 获取渐现显示选项
     *
     * @param errorImageResid 加载错误时显示的图片
     * @param emptyImageResId 空图片或者解析图片出错时显示的图片
     * @return
     */
    public static DisplayImageOptions getFadeOptions(int errorImageResid, int emptyImageResId) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // 设置图片加载/解码过程中错误时候显示的图片
                .showImageOnFail(errorImageResid)
                        // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(emptyImageResId)
                        // 设置下载的图片是否缓存在内存中
                .cacheInMemory(true)
                        // 设置下载的图片是否缓存在SD卡中
                .cacheOnDisk(true)
                        /**设置图片缩放方式：
                         EXACTLY :图像将完全按比例缩小到目标大小
                         EXACTLY_STRETCHED:图片会缩放到目标大小完全
                         IN_SAMPLE_INT:图像将被二次采样的整数倍
                         IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
                         NONE:图片不会调整
                         ***/
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                        // 设置图片的解码类型
                .bitmapConfig(Bitmap.Config.RGB_565)
                        // 设置图片下载前的延迟
                .delayBeforeLoading(100)
                        // delayInMillis为你设置的延迟时间
                        // 设置图片加入缓存前，对bitmap进行设置
                        // .preProcessor(BitmapProcessor preProcessor)
                        /**
                         * 图片显示方式：
                         *  RoundedBitmapDisplayer（int roundPixels）设置圆角图片
                         *  FakeBitmapDisplayer（）这个类什么都没做
                         *  FadeInBitmapDisplayer（int durationMillis）设置图片渐显的时间
                         　　　　  	 *　   SimpleBitmapDisplayer()正常显示一张图片
                         **/
                .displayer(new FadeInBitmapDisplayer(1 * 1000))// 渐显--设置图片渐显的时间
                .build();
        return options;
    }

    /**
     * 指定圆弧角度加载图片
     *
     * @param errorImageResid 加载错误时显示的图片
     * @param roundPixels     图片圆角弧度 320
     * @return
     */
    public static DisplayImageOptions getRoundedOptions(int errorImageResid, int roundPixels) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // 设置图片加载/解码过程中错误时候显示的图片
                .showImageOnFail(errorImageResid)
                        // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(errorImageResid)
                        /**设置图片缩放方式：
                         EXACTLY :图像将完全按比例缩小到目标大小
                         EXACTLY_STRETCHED:图片会缩放到目标大小完全
                         IN_SAMPLE_INT:图像将被二次采样的整数倍
                         IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
                         NONE:图片不会调整
                         ***/
                .imageScaleType(ImageScaleType.EXACTLY)
                        // 设置图片的解码类型
                .bitmapConfig(Bitmap.Config.RGB_565)
                        // 设置图片下载前的延迟
                .delayBeforeLoading(10)
                        /**
                         * 图片显示方式：
                         *  RoundedBitmapDisplayer（int roundPixels）设置圆角图片
                         *  FakeBitmapDisplayer（）这个类什么都没做
                         *  FadeInBitmapDisplayer（int durationMillis）设置图片渐显的时间
                         　　　　   	 *　   SimpleBitmapDisplayer()正常显示一张图片
                         **/
                .displayer(new RoundedBitmapDisplayer(roundPixels))
                .build();
        return options;
    }

    /**
     * 圆形加载图片
     *
     * @param defaultImage 默认图片
     * @return
     */
    public static DisplayImageOptions getRoundOptions(int defaultImage) {
        return
                new DisplayImageOptions.Builder()
                        .displayer(new RoundedBitmapDisplayer(320))
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .showImageOnFail(defaultImage)
                        .showImageForEmptyUri(defaultImage)
                        .imageScaleType(ImageScaleType.EXACTLY)
                        .cacheInMemory(true)
                        .cacheOnDisk(true).build();
    }

    /**
     * 获取默认显示配置选项
     */
    public static DisplayImageOptions getDefaultOptions() {
        return DisplayImageOptions.createSimple();
    }

    /**
     * 清除缓存
     */
    public static void clearCache() {
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
    }

}
