package com.github.diwakar1988.flick.net;


import com.github.diwakar1988.flick.pojo.MovieResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by diwakar.mishra on 10/11/16.
 */

public interface APIService {

    @GET("3/discover/movie")
    Call<MovieResponse> discoverMovies(@QueryMap Map<String, String> options);

}
