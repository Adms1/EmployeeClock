package com.adms.employeeclock.Utility;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by admsandroid on 11/20/2017.
 */

public class ApiHandler {

    public static final String BASE_URL = AppConfig.BASEURL;

    private static final long HTTP_TIMEOUT = TimeUnit.SECONDS.toMillis(6000);
    private static Webservice apiService;


    public static Webservice getApiService() {
        if (apiService == null) {
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(70 * 1000, TimeUnit.MILLISECONDS);
            okHttpClient.setWriteTimeout(70 * 1000, TimeUnit.MILLISECONDS);
            okHttpClient.setReadTimeout(70 * 1000, TimeUnit.MILLISECONDS);

//            okHttpClient.setSslSocketFactory(new NoSSLv3Factory());

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(BASE_URL)
                    .setClient(new OkClient(myOkHttpClient()))
                    .setConverter(new GsonConverter(new Gson()))
                    .build();

            apiService = restAdapter.create(Webservice.class);
            return apiService;
        } else {
            return apiService;
        }
    }


    protected static OkHttpClient myOkHttpClient() {

        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setReadTimeout(70 * 1000, TimeUnit.MILLISECONDS);
            okHttpClient.setConnectTimeout(70 * 1000, TimeUnit.MILLISECONDS);

            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};


            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            okHttpClient.setSslSocketFactory(sslSocketFactory);

            return okHttpClient;

        } catch (Exception e) {
            return null;
        }
    }

}
