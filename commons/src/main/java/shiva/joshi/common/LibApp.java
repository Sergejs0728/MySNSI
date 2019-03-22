package shiva.joshi.common;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;

import shiva.joshi.common.logger.AppLogger;

/**
 * Base class for those who need to maintain global application state.
 */
public class LibApp extends Application {


    private final static String TAG = LibApp.class.getName();
    /**
     * Instance of the current application.
     */
    private static LibApp instance;
    private Gson mGson;

    /**
     * Constructor.
     */
    public LibApp() {
        instance = this;
    }

    /**
     * Gets the application context.
     *
     * @return the application context
     */
    public Context getContext() {
        return instance;
    }

    public static LibApp getInstance() {
        return instance;
    }

    public static void showException(String tag, Exception ex) {
        AppLogger.Logger.error(tag, ex.getMessage(), ex);
    }

    // get GSON Object
    public Gson getGsonBuilder() {
        if (mGson == null)
            mGson = new GsonBuilder()
                    .enableComplexMapKeySerialization()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)

                    .setPrettyPrinting()
                    .setVersion(1.0)
                    .setLenient()
                    .create();
        return mGson;
    }

    public ImageLoader getImageLoaderInstance(Context context, DisplayImageOptions displayImageOptions) {
        // UNIVERSAL IMAGE LOADER SETUP
        if (displayImageOptions == null)
            displayImageOptions = getDisplayImageOptions(0);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(displayImageOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(250 * 1024 * 1024)
                .writeDebugLogs()
                .build();


        if (ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().destroy();
        }
        ImageLoader.getInstance().init(config);
        return ImageLoader.getInstance();
    }


    public ImageLoader getImageLoaderInstance(Context context, File cacheDir, DisplayImageOptions displayImageOptions) {
        // UNIVERSAL IMAGE LOADER SETUP
        if (displayImageOptions == null)
            displayImageOptions = getDisplayImageOptions(0);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .defaultDisplayImageOptions(displayImageOptions)
                .threadPoolSize(3) // default
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .writeDebugLogs()
                .build();

        /*
        ImageLoaderConfiguration.Builder(context)
  .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
  .diskCacheExtraOptions(480, 800, null)
  .taskExecutor(...)
  .taskExecutorForCachedImages(...)
  .threadPoolSize(3) // default
  .threadPriority(Thread.NORM_PRIORITY - 2) // default
  .tasksProcessingOrder(QueueProcessingType.FIFO) // default
  .denyCacheImageMultipleSizesInMemory()
  .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
  .memoryCacheSize(2 * 1024 * 1024)
  .memoryCacheSizePercentage(13) // default
  .diskCache(new UnlimitedDiskCache(cacheDir)) // default
  .diskCacheSize(50 * 1024 * 1024)
  .diskCacheFileCount(100)
  .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
  .imageDownloader(new BaseImageDownloader(context)) // default
  .imageDecoder(new BaseImageDecoder()) // default
  .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
  .writeDebugLogs()
  .build();
         */
        if (ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().destroy();
        }

        ImageLoader.getInstance().init(config);
        return ImageLoader.getInstance();
    }

    public DisplayImageOptions getDisplayImageOptions(int defaultDrawable) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();

        builder.cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .bitmapConfig(Bitmap.Config.RGB_565);

        if (defaultDrawable != 0) {
            builder.showImageOnLoading(defaultDrawable) // resource or drawable
                    .showImageForEmptyUri(defaultDrawable) // resource or drawable
                    .showImageOnFail(defaultDrawable);
        }
        return builder.build();
    }
}