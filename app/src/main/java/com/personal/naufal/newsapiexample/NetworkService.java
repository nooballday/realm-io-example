package com.personal.naufal.newsapiexample;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Naufal on 16/12/2017.
 */

public interface NetworkService {
    @GET("v2/top-headlines?sources=bbc-news&apiKey}")
    Call<ResponseBody> getNews(@Query("source") String source, @Query("apiKey") String key);
}
