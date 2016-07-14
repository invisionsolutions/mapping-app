package app.invision.mapping;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import app.invision.mapping.utils.Utils;

/**
 * Created by hp on 6/23/2016.
 */

public class Application extends android.app.Application {
    //============================================================================================//
    //                                        Constants                                           //
    //============================================================================================//
    public static final String TAG = Application.class.getSimpleName();
    public static final String SHARED_PREFS_NAME = "mapping.prefs";

    private RequestQueue mQueue;


    @Override
    public void onCreate() {
        super.onCreate();
        // Set up global exception handler
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Utils.appendLog(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), Log.getStackTraceString(ex));
                ex.printStackTrace();
                Log.e(Application.TAG, "ERROR! " + ex.toString());
            }
        });

    }

    /**
     * Sigleton provider for Volley Request Queue
     */
    public RequestQueue getQueue(Context c) {
        if (mQueue == null) mQueue = Volley.newRequestQueue(c);
        return mQueue;
    }


    /**
     * Method to cancel all requests in queue when called
     */
    public void cancelAllRequests(Context context) {
        getQueue(context).cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }
}
