package product.presisco.yourdrivers.Network.Task;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by presisco on 2016/5/28.
 */
public abstract class ExtendedRequest<T> extends Request<T> {
    public static final String TAG = ExtendedRequest.class.getSimpleName();

    private OnLoadCompleteListener<T> onLoadCompleteListener;

    public ExtendedRequest(int method, String url, OnLoadCompleteListener<T> loadCompleteListener, Response.ErrorListener listener) {
        super(method, url, listener);
        onLoadCompleteListener = loadCompleteListener;
    }

    @Override
    protected abstract Response<T> parseNetworkResponse(NetworkResponse networkResponse);

    @Override
    protected void deliverResponse(T t) {
        if (onLoadCompleteListener != null) {
            onLoadCompleteListener.onLoadComplete(t);
        } else {
            Log.w(TAG, "null loadcomplelelistener!");
        }
    }

    public interface OnLoadCompleteListener<T> {
        void onLoadComplete(T data);
    }
}
