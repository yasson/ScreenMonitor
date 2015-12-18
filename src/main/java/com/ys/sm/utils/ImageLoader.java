package com.ys.sm.utils;

import android.content.Context;
import android.widget.ImageView;
import com.ys.sm.R;

/**
 * image loader
 * Created by ys on 2015/12/18.
 */
public class ImageLoader {


    static int cacheSize = (int) Runtime.getRuntime().maxMemory()/8;

    private ImageCache mCache;

    static private ImageLoader instance;

    private Context mContext;

    private ImageLoader() {
        mCache = new ImageCache(cacheSize);
    }

    public void init(Context context) {
        mContext = context;
    }


    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    public void loadImg(ImageView iv, String pname) {
        if (null != mCache.get(pname)) {
            if (iv.getTag().equals(pname)) {
                iv.setImageBitmap(mCache.get(pname));
            }
        } else {
            if (iv.getTag().equals(pname)) {
                iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logo_default));
            }
            new ImageLoaderTask(mContext, iv, pname, mCache).execute();

        }
    }
}
