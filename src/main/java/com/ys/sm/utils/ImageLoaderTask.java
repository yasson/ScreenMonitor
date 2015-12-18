package com.ys.sm.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 * image load task
 * Created by ys on 2015/12/18.
 */
public class ImageLoaderTask extends AsyncTask<String, Integer, Bitmap> {

    private ImageView mView;

    private String mPName;

    private Context mContext;

    private ImageCache mCache;

    public ImageLoaderTask(Context context, ImageView view, String pName, ImageCache mCache) {
        this.mContext = context;
        this.mView = view;
        this.mPName = pName;
        this.mCache = mCache;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        PackageManager pm = mContext.getPackageManager();
        try {
            Log.i("ys","doInBackground:"+mPName);
            PackageInfo pi = pm.getPackageInfo(mPName,1);
            Drawable drawable =pi.applicationInfo.loadIcon(pm);
            if (drawable!=null){
                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                if (bitmap != null) {
                    mCache.put(mPName, bitmap);
                }
                return bitmap;
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap drawable) {
        if (drawable != null) {
            if (mView.getTag().equals(mPName)) {
                mView.setImageBitmap(drawable);
            }
        }
        super.onPostExecute(drawable);
    }
}
