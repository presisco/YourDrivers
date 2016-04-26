package product.presisco.yourdrivers.Network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.cache.BitmapImageCache;
import com.android.volley.cache.SimpleImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by presisco on 2016/4/26.
 */
public class VolleyPlusRes {
    private static RequestQueue mRequestQueue;
    private static SimpleImageLoader mImageLoader;


    private VolleyPlusRes() {
        // no instances
    }

    public static void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new SimpleImageLoader(mRequestQueue, BitmapImageCache.getInstance(null));
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    /**
     * Returns instance of ImageLoader initialized with {@see FakeImageCache} which effectively means
     * that no memory caching is used. This is useful for images that you know that will be show
     * only once.
     *
     * @return
     */
    public static SimpleImageLoader getImageLoader() {
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }
}
