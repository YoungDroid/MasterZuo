package com.oom.masterzuo.utils;

import android.content.Context;
import android.net.Uri;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;

import java.io.File;

/**
 * Created by xiaojunhuang on 16/3/17.
 *
 * Changed by YoungDroid on 16/09/12
 *
 * Fresco 0.9.0 -> 0.12.0
 */
public class FrescoUtils {

//    public static File getPathByImageRequest( ImageRequest imageRequest, Context context ) {
//        File localFile = null;
//        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey( imageRequest, context );
//        if ( ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey( cacheKey ) ) {
//            BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource( cacheKey );
//            localFile = ( ( FileBinaryResource ) resource ).getFile();
//        } else if ( ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey( cacheKey ) ) {
//            BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().getResource( cacheKey );
//            localFile = ( ( FileBinaryResource ) resource ).getFile();
//        }
//        return localFile;
//    }
//
//    public static File getPathByImageRequest( String url, Context context ) {
//
//        ImageRequest imageRequest = ImageRequest.fromUri( Uri.parse( url ) );
//
//        File localFile = null;
//        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey( imageRequest, context );
//        if ( ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey( cacheKey ) ) {
//            BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource( cacheKey );
//            localFile = ( ( FileBinaryResource ) resource ).getFile();
//        } else if ( ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey( cacheKey ) ) {
//            BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().getResource( cacheKey );
//            localFile = ( ( FileBinaryResource ) resource ).getFile();
//        }
//        return localFile;
//    }
}
