package com.github.uiadapter;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.lang.reflect.Field;

/***
 *   created by android on 2019/4/17
 */
public class UIAdapter {

    private static boolean isInitMiUi = false;
    private static DisplayMetrics tempDisplayMetrics;

    public static Resources adaptWidth(Resources resources,int uiWidth){
        return adaptWidth(null,resources,uiWidth);
    }
    public static Resources adaptWidth(Context context, Resources resources,int uiWidth){

        DisplayMetrics displayMetrics =getDisplayMetrics(resources);
        float width=displayMetrics.widthPixels;

        Configuration configuration = resources.getConfiguration();
        if(configuration!=null&&configuration.orientation==Configuration.ORIENTATION_LANDSCAPE){
            width=displayMetrics.heightPixels;
        }

        float scaleXdpi=width*72f/uiWidth;
        displayMetrics.xdpi=scaleXdpi;
        if(context!=null){
            context.getApplicationContext().getResources().getDisplayMetrics().xdpi=scaleXdpi;
        }
        return resources;
    }

    public static Resources adaptHeight(Resources resources,int uiHeight){
        return adaptHeight(null,resources,uiHeight,true);
    }
    public static Resources adaptHeight(Context context, Resources resources,int uiHeight,boolean ignoreStatusBarHeight){
        DisplayMetrics displayMetrics =getDisplayMetrics(resources);
        float height=displayMetrics.heightPixels;
        Configuration configuration = resources.getConfiguration();
        if(configuration!=null&&configuration.orientation==Configuration.ORIENTATION_LANDSCAPE){
            height=displayMetrics.widthPixels;
        }
        if(ignoreStatusBarHeight){
            height=height-getStatusBarHeight(context);
        }

        float scaleXdpi=height*72f/uiHeight;
        displayMetrics.xdpi=scaleXdpi;
        if(context!=null){
            context.getApplicationContext().getResources().getDisplayMetrics().xdpi=scaleXdpi;
        }
        return resources;
    }
    public static int getStatusBarHeight(Context context){
        int statusBarHeight;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId != 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        } else {
            statusBarHeight = 0;
        }
        return statusBarHeight;
    }

    public static int pt2Px(Context context,float ptValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (ptValue * metrics.xdpi / 72f);
    }
    public static int px2Pt(Context context,float pxValue) {
        DisplayMetrics metrics =context.getResources().getDisplayMetrics();
        return (int) (pxValue * 72 / metrics.xdpi);
    }


    public static Resources closeUIAdapter(Resources resources) {
        return closeUIAdapter(null,resources);
    }
    public static Resources closeUIAdapter(Context context,Resources resources) {
        DisplayMetrics dm = getDisplayMetrics(resources);
        float xdpi = Resources.getSystem().getDisplayMetrics().xdpi;
        dm.xdpi=xdpi;
        if(context!=null){
            context.getApplicationContext().getResources().getDisplayMetrics().xdpi=xdpi;
        }
        return resources;
    }

    private static DisplayMetrics getDisplayMetrics(Resources resources) {
        DisplayMetrics miuiDisplayMetrics = getMiUiTmpMetrics(resources);
        if (miuiDisplayMetrics == null) {
            return resources.getDisplayMetrics();
        }
        return miuiDisplayMetrics;
    }

    private static DisplayMetrics getMiUiTmpMetrics(Resources resources) {
        if (!isInitMiUi) {
            String simpleName = resources.getClass().getSimpleName();
            if ("MiuiResources".equals(simpleName) || "XResources".equals(simpleName)) {
                try {
                    Field tempMetrics = Resources.class.getDeclaredField("tempMetrics");
                    tempMetrics.setAccessible(true);
                    tempDisplayMetrics = (DisplayMetrics) tempMetrics.get(resources);
                } catch (Exception e) {
                    tempDisplayMetrics=null;
                    Log.e("AdaptScreenUtils", "no field of tempMetrics in resources.");
                }
            }
            isInitMiUi = true;
        }
        return tempDisplayMetrics;
    }
}
