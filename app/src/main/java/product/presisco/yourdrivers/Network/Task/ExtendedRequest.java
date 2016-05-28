package product.presisco.yourdrivers.Network.Task;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

import product.presisco.yourdrivers.Network.Constants;

/**
 * Created by presisco on 2016/5/28.
 */
public abstract class ExtendedRequest<T> extends Request<T> {
    public static final String TAG = ExtendedRequest.class.getSimpleName();

    private OnLoadCompleteListener<T> onLoadCompleteListener;

    public ExtendedRequest(int method, String path, OnLoadCompleteListener<T> loadCompleteListener, Response.ErrorListener listener) {
        super(method, Constants.MOBILE_WEB_HOST + path, listener);
        onLoadCompleteListener = loadCompleteListener;
    }

    public ExtendedRequest(int method, OnLoadCompleteListener<T> loadCompleteListener, Response.ErrorListener listener) {
        super(method, Constants.MOBILE_WEB_HOST, listener);
        onLoadCompleteListener = loadCompleteListener;
    }

    @Override
    protected abstract Response<T> parseNetworkResponse(NetworkResponse networkResponse);

    protected String getStringFromResponse(NetworkResponse networkResponse) {
        String raw;
        try {
            raw = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
        } catch (UnsupportedEncodingException e) {
            raw = new String(networkResponse.data);
        }
        return raw;
    }

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
