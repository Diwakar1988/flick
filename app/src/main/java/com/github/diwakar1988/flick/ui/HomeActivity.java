package com.github.diwakar1988.flick.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.github.diwakar1988.flick.BuildConfig;
import com.github.diwakar1988.flick.R;
import com.github.diwakar1988.flick.net.APIService;
import com.github.diwakar1988.flick.pojo.Movie;
import com.github.diwakar1988.flick.pojo.MovieResponse;
import com.github.diwakar1988.flick.ui.adaptor.MoviesAdaptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvMoviesGrid;
    private MoviesAdaptor moviesAdaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rvMoviesGrid = (RecyclerView) findViewById(R.id.rv_movies_grid);
        rvMoviesGrid.setHasFixedSize(true);
        GridLayoutManager glm = new GridLayoutManager(this,3);
        rvMoviesGrid.setLayoutManager(glm);
        moviesAdaptor=new MoviesAdaptor(this,getResources().getConfiguration().orientation);
        rvMoviesGrid.setAdapter(moviesAdaptor);

        //we can call this function as per our business requirements
        loadTopPopularMovies();
    }


    private void loadTopPopularMovies(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService service = retrofit.create(APIService.class);

        //we can define common/frequent headers in a separate file or make them constants
        Map<String, String> options=new HashMap<>();
        options.put("sort_by", "popularity.desc");
        options.put("api_key", BuildConfig.API_KEY);


        Call<MovieResponse> call = service.discoverMovies(options);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<Movie> movies = response.body().results;
                if (movies!=null && movies.size()>0){
                    Toast.makeText(HomeActivity.this, "Pass!", Toast.LENGTH_SHORT).show();
                    onMoviesDownloaded(movies);
                }else{
                    //show ui for movies not found kind of
                    Toast.makeText(HomeActivity.this, "movies not found!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onMoviesDownloaded(List<Movie> movies) {

        moviesAdaptor.setMovies(movies);
    }


}
