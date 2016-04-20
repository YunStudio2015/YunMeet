package yunstudio2015.android.yunmeet.app;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;

/**
 * Created by abiguime on 4/21/2016.
 */
public class MyGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Apply options to the builder here.
        builder = new GlideBuilder(context);
        String  cacheDirectoryName = UtilsFunctions.getAppImageCachePath(context);
        builder.setDiskCache(
                new InternalCacheDiskCacheFactory(context, cacheDirectoryName,  10 * 1020 * 1024));
        builder.setMemoryCache(new LruResourceCache(10 * 1024 * 1024));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
        Glide.get(context).setMemoryCategory(MemoryCategory.HIGH);
    }

}
