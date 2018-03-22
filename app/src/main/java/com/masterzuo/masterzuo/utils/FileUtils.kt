package com.masterzuo.masterzuo.utils

import android.os.Environment
import java.io.*

/**
 * Created by YoungDroid on 17/3/20.
 * Email: YoungDroid@163.com
 */
object FileUtils {

    private val ROOT = "MasterZuo"
    private val THUMBNAIL = "thumbnail"
    private val PICTURE = "PICTURE_FILE"
    private val CACHE = "cache"
    private val CRASH = "Crash"
    val ROOT_FILE: File
    val THUMBNAIL_FILE: File
    val PICTURE_FILE: File
    val CACHE_FILE: File
    val CRASH_FILE: File

    init {
        ROOT_FILE = File(sdCardPath(), ROOT)
        THUMBNAIL_FILE = File(ROOT_FILE, THUMBNAIL)
        PICTURE_FILE = File(ROOT_FILE, PICTURE)
        CACHE_FILE = File(ROOT_FILE, CACHE)
        CRASH_FILE = File(ROOT_FILE, CRASH)
    }

    fun init() {
        createFolder(ROOT_FILE)
        createFolder(THUMBNAIL_FILE)
        createFolder(PICTURE_FILE)
        createFolder(CACHE_FILE)
        createFolder(CRASH_FILE)
    }

    private fun sdCardPath(): File? {
        var sdDir: File? = null
        val sdCardExist = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        if (sdCardExist) {
            //获取跟目录
            sdDir = Environment.getExternalStorageDirectory()
            error("sdCardPath: Exist" + sdDir!!.path)
        } else {
            error("sdCardPath: not Exist")
        }
        return sdDir
    }

    private fun createFolder(file: File) {
        if (!file.exists()) {
            error("createFolder: " + file.path + " is not exist")
            val createFolder = file.mkdirs()
            if (!createFolder) {
                try {
                    throw FileNotFoundException("本地文件夹初始化失败.")
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun copyFile(oldFile: File, newFile: File) {
        try {
            if (oldFile.exists()) {
                val inStream = FileInputStream(oldFile)
                val fs = FileOutputStream(newFile)
                copy(inStream, fs)
            }
        } catch (e: Exception) {
            error("复制单个文件操作出错")
            e.printStackTrace()
        }
    }

    fun copy(inputStream: InputStream, fileOutputStream: FileOutputStream) {
        try {
            var bytesum: Int = 0
            var byteread: Int = 0
            val buffer = ByteArray(1444)
            while (byteread != -1) {
                bytesum += byteread
                debug("copy: " + bytesum)
                fileOutputStream.write(buffer, 0, byteread)
                byteread = inputStream.read(buffer)
            }
            inputStream.close()
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: Exception) {
            error("复制单个文件操作出错")
            e.printStackTrace()
        }

    }
}
