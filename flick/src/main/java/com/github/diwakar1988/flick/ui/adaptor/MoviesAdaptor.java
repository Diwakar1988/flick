package com.github.diwakar1988.flick.ui.adaptor;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
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

public class MoviesAdaptor extends RecyclerView.Adapter<MoviesAdaptor.MovieViewHolder> {
    private List<Movie>movies;
    private LayoutInflater inflater;
    private Context context;
    private OnItemClickedListener listener;
    private int orientation;

    public MoviesAdaptor(Context context,OnItemClickedListener listener,int orientation) {
        this.context = context;
        this.listener = listener;
        this.orientation = orientation;
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
    public void addMovies(List<Movie> list) {
        if (list!=null){
            this.movies.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        CardView card = new CardView(context);

        // Set the CardView layoutParams
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        card.setLayoutParams(params);

        // Set CardView corner radius
        card.setRadius(dpToPx(4));

        card.setUseCompatPadding(true);

        // Set CardView elevation
        card.setCardElevation(dpToPx(8));


        // Initialize a new TextView to put in CardView
        ImageView iv = new ImageView(context);

        ViewGroup.LayoutParams ivParams =null;

        if (orientation== Configuration.ORIENTATION_PORTRAIT){
            ivParams= new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dpToPx(280)
            );

        }else{
            ivParams= new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dpToPx(200)
            );
        }
        iv.setId(R.id.iv_movie_pic);
        iv.setLayoutParams(ivParams);


        // Put the TextView in CardView
        card.addView(iv);

        return new MovieViewHolder(card);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder vh, int position) {

       if (position==getItemCount()-1){
           //last item values needs tobe bounded
           vh.ivPic.setScaleType(ImageView.ScaleType.CENTER);
           Picasso.with(context)
                   .load(R.drawable.ic_load_more)
                   .into(vh.ivPic);

       }else{
           Movie m = getMovie(position);
           String picURL="http://image.tmdb.org/t/p/w500/"+m.posterPath;
           vh.ivPic.setScaleType(ImageView.ScaleType.CENTER_CROP);
           Picasso.with(context)
                   .load(picURL)
                   .into(vh.ivPic);
       }
    }

    @Override
    public int getItemCount() {
        int size = movies.size();
        if (size>0){
            size+=1;
        }
        return size;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public  Movie getMovie(int position){
        return movies.get(position);
    }


    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView ivPic;
        public MovieViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            ivPic = (ImageView) view.findViewById(R.id.iv_movie_pic);

        }

        @Override
        public void onClick(View v) {
            listener.onItemClicked(v,getAdapterPosition());
        }
    }
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

}
