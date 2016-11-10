package com.github.diwakar1988.flick.ui.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.diwakar1988.flick.R;
import com.github.diwakar1988.flick.pojo.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diwakar.mishra on 10/11/16.
 */

public class MoviesAdaptor extends RecyclerView.Adapter<MoviesAdaptor.MovieViewHolder> implements View.OnClickListener{
    private List<Movie>movies;
    private LayoutInflater inflater;
    private Context context;

    public MoviesAdaptor(Context context) {
        this.context = context;
        this.movies=new ArrayList<>();
        this.inflater=LayoutInflater.from(context);
    }

    public void setMovies(List<Movie> list) {
        if (list!=null){
            this.movies.clear();
        }
        this.movies.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = inflater.inflate(R.layout.layout_movie_grid_item,null);
        return new MovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder vh, int position) {

        Movie m = getMovie(position);
        String picURL="http://image.tmdb.org/t/p/w500/"+m.posterPath;
        Picasso.with(context)
                .load(picURL)
                .into(vh.ivPic);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public  Movie getMovie(int position){
        return movies.get(position);
    }

    @Override
    public void onClick(View v) {


    }

    class MovieViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivPic;
        public MovieViewHolder(View view) {
            super(view);
            view.setOnClickListener(MoviesAdaptor.this);
            ivPic = (ImageView) view.findViewById(R.id.iv_movie_pic);
        }
    }
}
