package com.oom.masterzuo.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;

import cn.finalteam.rxgalleryfinal.RxGalleryFinalApi;
import cn.finalteam.rxgalleryfinal.utils.ModelUtils;

import static com.oom.masterzuo.app.MasterZuoApplication.Debug;

public class FileUtils {
    
    private static final String ROOT = "MasterZuo";
    private static final String THUMBNAI = "thumbnail";
    private static final String PICTURE = "PIC";
    private static final String CHACHE = "cache";
    private static final String CRASH = "Crash";
    public static final File ROOTFILE;
    public static final File THUMBNAILFILE;
    public static final File PIC;
    public static final File CACHEFILE;
    public static final File CRASHFILE;
    
    static {
        ROOTFILE = new File( sdCardPath(), ROOT );
        THUMBNAILFILE = new File( ROOTFILE, THUMBNAI );
        PIC = new File( ROOTFILE, PICTURE );
        CACHEFILE = new File( ROOTFILE, CHACHE );
        CRASHFILE = new File( ROOTFILE, CRASH );
    }
    
    public static void init() {
        createFolder( ROOTFILE );
        createFolder( THUMBNAILFILE );
        createFolder( PIC );
        createFolder( CACHEFILE );
        createFolder( CRASHFILE );
        
        ModelUtils.setDebugModel( Debug );
        RxGalleryFinalApi.setImgSaveRxDir( PIC );
        RxGalleryFinalApi.setImgSaveRxCropDir( CRASHFILE );
    }
    
    private static File sdCardPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED );
        if ( sdCardExist ) {
            //获取跟目录
            sdDir = Environment.getExternalStorageDirectory();
            if ( Debug ) {
                Log.e( "FileUtils", "sdCardPath: Exist" + sdDir.getPath() );
            }
        } else {
            if ( Debug ) {
                Log.e( "FileUtils", "sdCardPath: not Exist" );
            }
        }
        return sdDir;
    }
    
    private static void createFolder( File file ) {
        if ( !file.exists() ) {
            if ( Debug ) {
                Log.e( "FileUtils", "createFolder: " + file.getPath() + " is not exist" );
            }
            boolean createFolder = file.mkdirs();
            if ( !createFolder ) {
                try {
                    throw new FileNotFoundException( "本地文件夹初始化失败." );
                } catch ( FileNotFoundException e ) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void copyFile( File oldfile, File newFile ) {
        try {
            int bytesum = 0;
            int byteread = 0;
            if ( oldfile.exists() ) { //文件存在时
                InputStream inStream = new FileInputStream( oldfile ); //读入原文件
                FileOutputStream fs = new FileOutputStream( newFile );
                copy( inStream, fs );
            }
        } catch ( Exception e ) {
            if ( Debug ) {
                Log.e( "FileUtils", "copyFile: 复制单个文件操作出错" );
            }
            e.printStackTrace();
        }
    }
    
    public static void copy( InputStream inputStream, FileOutputStream fileOutputStream ) {
        try {
            int bytesum = 0;
            int byteread = 0;
            byte[] buffer = new byte[ 1444 ];
            int length;
            while ( ( byteread = inputStream.read( buffer ) ) != -1 ) {
                bytesum += byteread; //字节数 文件大小
                if ( Debug ) {
                    Log.e( "FileUtils", "copy - bytesum: " + bytesum );
                }
                fileOutputStream.write( buffer, 0, byteread );
            }
            inputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch ( Exception e ) {
            if ( Debug ) {
                Log.e( "FileUtils", "copyFile: 复制单个文件操作出错" );
            }
            e.printStackTrace();
        }
    }
    
    /**
     * 获取文件夹大小
     *
     * @param file File实例
     *
     * @return long
     */
    public static long getFolderSize( File file ) {
        
        long size = 0;
        try {
            java.io.File[] fileList = file.listFiles();
            for ( int i = 0; i < fileList.length; i++ ) {
                if ( fileList[ i ].isDirectory() ) {
                    size = size + getFolderSize( fileList[ i ] );
                    
                } else {
                    size = size + fileList[ i ].length();
                    
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }
    
    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @param filePath
     *
     * @return
     */
    public static void deleteFolderFile( String filePath, boolean deleteThisPath ) {
        if ( !TextUtils.isEmpty( filePath ) ) {
            try {
                File file = new File( filePath );
                // 处理目录
                if ( file.isDirectory() ) {
                    File files[] = file.listFiles();
                    for ( int i = 0; i < files.length; i++ ) {
                        deleteFolderFile( files[ i ].getAbsolutePath(), true );
                    }
                }
                if ( deleteThisPath ) {
                    // 如果是文件，删除
                    if ( !file.isDirectory() ) {
                        file.delete();
                    } else {
                        // 目录下没有文件或者目录，删除
                        if ( file.listFiles().length == 0 ) {
                            file.delete();
                        }
                    }
                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 格式化单位
     *
     * @param size
     *
     * @return
     */
    public static String getFormatSize( double size ) {
        double kiloByte = size / 1024;
        if ( kiloByte < 1 ) {
            return size + "B";
        }
        
        double megaByte = kiloByte / 1024;
        if ( megaByte < 1 ) {
            BigDecimal result1 = new BigDecimal( Double.toString( kiloByte ) );
            return result1.setScale( 2, BigDecimal.ROUND_HALF_UP ).toPlainString() + "KB";
        }
        
        double gigaByte = megaByte / 1024;
        if ( gigaByte < 1 ) {
            BigDecimal result2 = new BigDecimal( Double.toString( megaByte ) );
            return result2.setScale( 2, BigDecimal.ROUND_HALF_UP ).toPlainString() + "MB";
        }
        
        double teraBytes = gigaByte / 1024;
        if ( teraBytes < 1 ) {
            BigDecimal result3 = new BigDecimal( Double.toString( gigaByte ) );
            return result3.setScale( 2, BigDecimal.ROUND_HALF_UP ).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal( teraBytes );
        return result4.setScale( 2, BigDecimal.ROUND_HALF_UP ).toPlainString() + "TB";
    }
}
