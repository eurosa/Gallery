package me.devsaki.hentoid.util;

import android.util.SparseArray;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.devsaki.hentoid.HentoidApp;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Manages a single instance of OkHttpClient per timeout delay
 */
public class OkHttpClientSingleton {

    private static volatile Map<String, OkHttpClient> instance = new Hashtable<>();
    private static int DEFAULT_TIMEOUT = 20 * 1000;

    private OkHttpClientSingleton() {
    }

    public static OkHttpClient getInstance() {
        return getInstance(DEFAULT_TIMEOUT);
    }

    public static OkHttpClient getInstance(int timeoutMs) {
        return getInstance(timeoutMs, OkHttpClientSingleton::onIntercept);
    }

    public static OkHttpClient getInstance(int timeoutMs, Interceptor interceptor) {
        if (null == OkHttpClientSingleton.instance.get(timeoutMs + "" + interceptor.toString())) {
            synchronized (OkHttpClientSingleton.class) {
                if (null == OkHttpClientSingleton.instance.get(timeoutMs + "" + interceptor.toString())) {

                    int CACHE_SIZE = 2 * 1024 * 1024; // 2 MB

                    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                            .addInterceptor(interceptor)
                            .connectTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                            .readTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                            .writeTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                            .cache(new Cache(HentoidApp.getAppContext().getCacheDir(), CACHE_SIZE));


                    OkHttpClientSingleton.instance.put(timeoutMs + "" + interceptor.toString(), clientBuilder.build());
                }
            }
        }
        return OkHttpClientSingleton.instance.get(timeoutMs + "" + interceptor.toString());
    }

    private static okhttp3.Response onIntercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .header("User-Agent", Consts.USER_AGENT)
//                .header("Data-type", "application/json")
                .build();
        return chain.proceed(request);
    }
}
