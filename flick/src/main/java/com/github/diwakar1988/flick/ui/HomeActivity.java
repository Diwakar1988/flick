package com.github.diwakar1988.flick.ui;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.github.diwakar1988.flick.BuildConfig;
import com.github.diwakar1988.flick.Flick;
import com.github.diwakar1988.flick.R;
import com.github.diwakar1988.flick.net.APIService;
import com.github.diwakar1988.flick.pojo.Movie;
import com.github.diwakar1988.flick.pojo.MovieResponse;
import com.github.diwakar1988.flick.ui.adaptor.MoviesAdaptor;
import com.github.diwakar1988.flick.ui.adaptor.OnItemClickedListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity implements OnItemClickedListener {

    private RecyclerView rvMoviesGrid;
    private MoviesAdaptor moviesAdaptor;
    private int currentPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rvMoviesGrid = (RecyclerView) findViewById(R.id.rv_movies_grid);
        rvMoviesGrid.setHasFixedSize(true);
        int spans = getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT?2:4;
        GridLayoutManager glm = new GridLayoutManager(this,spans);
        rvMoviesGrid.setLayoutManager(glm);
        moviesAdaptor=new MoviesAdaptor(this,this,getResources().getConfiguration().orientation);
        rvMoviesGrid.setAdapter(moviesAdaptor);

        //we can call this function as per our business requirements
        currentPage=1;
        loadTopPopularMovies();
    }



    private void loadTopPopularMovies(){

        showProgress("Please wait...");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService service = retrofit.create(APIService.class);

        //we can define common/frequent headers in a separate file or make them constants
        Map<String, String> options=new HashMap<>();
        options.put("sort_by", "popularity.desc");
        options.put("api_key", BuildConfig.API_KEY);
        if (currentPage>1){
            options.put("page", String.valueOf(currentPage));

        }

        Call<MovieResponse> call = service.discoverMovies(options);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                hideProgress();

                //check for more pages
                if (response.body().total_pages> currentPage){
                    currentPage++;
                }
                List<Movie> movies = response.body().results;
                if (movies!=null && movies.size()>0){
                    onMoviesDownloaded(movies);
                }else{
                    //show ui for movies not found kind of
                    Toast.makeText(HomeActivity.this, "No more popular movies found!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                hideProgress();
                if (Flick.getInstance().isNetworkAvailable()){

                    Toast.makeText(HomeActivity.this, "Error while downloading movies, please try again!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(HomeActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onMoviesDownloaded(List<Movie> movies) {

        moviesAdaptor.addMovies(movies);
    }

    private ProgressDialog progress;

    protected void showProgress(String message) {
        if (progress == null) {
            progress = ProgressDialog.show(this, "",
                    message, true);
        } else if (progress.isShowing()) {
            progress.setMessage(message);
        } else {
            progress.setMessage(message);
            progress.show();
        }
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(true);


    }

    protected void hideProgress() {
        if (progress != null) {
            progress.dismiss();
        }
    }

    @Override
    public void onItemClicked(View v, int position) {
        if (position==moviesAdaptor.getItemCount()-1){
            //means load more item clicked
            loadTopPopularMovies();
        }
    }
}
