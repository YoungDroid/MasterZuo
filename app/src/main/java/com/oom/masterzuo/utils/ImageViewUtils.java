package com.oom.masterzuo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

/**
 * Created by CcYang on 2016/4/8.
 */
public class ImageViewUtils {
    
    public static DraweeController simpleDraweeViewAutoScale( final SimpleDraweeView simpleDraweeView, Uri imageUri ) {
        
        ControllerListener controllerListener = new BaseControllerListener() {
            
            @Override
            public void onFinalImageSet( String id, @Nullable Object object, @Nullable Animatable animatable ) {
                super.onFinalImageSet( id, object, animatable );
                if ( object == null ) {
                    return;
                }
                ImageInfo imageInfo = ( ImageInfo ) object;
                //QualityInfo qualityInfo = imageInfo.getQualityInfo();
                if ( ( float ) imageInfo.getWidth() / ( float ) imageInfo.getHeight() > 0.6 ) {
                    simpleDraweeView.setAspectRatio( ( float ) imageInfo.getWidth() / ( float ) imageInfo.getHeight() );
                } else {
                    simpleDraweeView.setAspectRatio( 0.6f );
                }
            }
            
            @Override
            public void onIntermediateImageSet( String id, @Nullable Object imageInfo ) {
                super.onIntermediateImageSet( id, imageInfo );
            }
            
            @Override
            public void onFailure( String id, Throwable throwable ) {
                super.onFailure( id, throwable );
            }
        };
        
        DraweeController controller = Fresco.newDraweeControllerBuilder().setControllerListener( controllerListener ).setUri( imageUri ).build();
        
        return controller;
    }
    
    public static DraweeController simpleDraweeViewAutoScaleXY( final SimpleDraweeView simpleDraweeView, Uri imageUri ) {
        
        ControllerListener controllerListener = new BaseControllerListener() {
            
            @Override
            public void onFinalImageSet( String id, @Nullable Object object, @Nullable Animatable animatable ) {
                super.onFinalImageSet( id, object, animatable );
                if ( object == null ) {
                    return;
                }
                ImageInfo imageInfo = ( ImageInfo ) object;
                simpleDraweeView.setAspectRatio( ( float ) imageInfo.getWidth() / ( float ) imageInfo.getHeight() );
            }
            
            @Override
            public void onIntermediateImageSet( String id, @Nullable Object imageInfo ) {
                super.onIntermediateImageSet( id, imageInfo );
            }
            
            @Override
            public void onFailure( String id, Throwable throwable ) {
                super.onFailure( id, throwable );
            }
        };
        
        DraweeController controller = Fresco.newDraweeControllerBuilder().setControllerListener( controllerListener ).setUri( imageUri ).build();
        
        return controller;
    }
    
    //图片缩放比例
    private static final float BITMAP_SCALE = 0.4f;
    
    /**
     * 模糊图片的具体方法
     *
     * @param context 上下文对象
     * @param image   需要模糊的图片
     * @return 模糊处理后的图片
     */
    public static Bitmap blurBitmap( Context context, Bitmap image, float blurRadius ) {
        if ( VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1 ) {
            // 计算图片缩小后的长宽
            int width = Math.round( image.getWidth() * BITMAP_SCALE );
            int height = Math.round( image.getHeight() * BITMAP_SCALE );
            
            // 将缩小后的图片做为预渲染的图片
            Bitmap inputBitmap = Bitmap.createScaledBitmap( image, width, height, false );
            // 创建一张渲染后的输出图片
            Bitmap outputBitmap = Bitmap.createBitmap( inputBitmap );
            
            // 创建RenderScript内核对象
            RenderScript rs = RenderScript.create( context );
            // 创建一个模糊效果的RenderScript的工具对象
            ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create( rs, Element.U8_4( rs ) );
            
            // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
            // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
            Allocation tmpIn = Allocation.createFromBitmap( rs, inputBitmap );
            Allocation tmpOut = Allocation.createFromBitmap( rs, outputBitmap );
            
            // 设置渲染的模糊程度, 25f是最大模糊度
            blurScript.setRadius( blurRadius );
            // 设置blurScript对象的输入内存
            blurScript.setInput( tmpIn );
            // 将输出数据保存到输出内存中
            blurScript.forEach( tmpOut );
            
            // 将数据填充到Allocation中
            tmpOut.copyTo( outputBitmap );
            
            return outputBitmap;
        } else throw new NullPointerException( "API Level Must than 17" );
    }
}
