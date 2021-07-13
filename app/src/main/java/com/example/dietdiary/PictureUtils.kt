package com.example.dietdiary

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Point


fun getScaledBitmap(path: String, activity: Activity) : Bitmap{
    val size = Point()
    val matrix = Matrix()
    matrix.postRotate(90F)

    @Suppress("DEPRECATION")
    activity.windowManager.defaultDisplay.getSize(size)

    val bitmap = getScaledBitmap(path, size.x, size.y)

    return Bitmap.createBitmap(bitmap, 0,0, bitmap.getWidth(), bitmap.getHeight(), matrix, true)

}

fun getScaledBitmap(path:String, destWidth: Int, destHeight: Int) : Bitmap{
    var options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, options)

    val srcWidth = options.outWidth.toFloat()
    val srcHeight = options.outHeight.toFloat()

    var inSampleSize = 1
    if(srcHeight > destHeight || srcWidth > destWidth){
        val heightScale = srcHeight/destHeight
        val widthScale = srcWidth/destWidth

        val sampleScale = if(heightScale > widthScale){
            heightScale
        }else{
            widthScale
        }
        inSampleSize = Math.round(sampleScale)
    }

    options = BitmapFactory.Options()
    options.inSampleSize = inSampleSize

    return BitmapFactory.decodeFile(path, options)
}