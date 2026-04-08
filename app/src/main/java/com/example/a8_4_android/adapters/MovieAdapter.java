package com.example.a8_4_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.a8_4_android.databinding.ItemMovieBinding;
import com.example.a8_4_android.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public MovieAdapter(List<Movie> movieList, OnMovieClickListener listener) {
        this.movieList = movieList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieBinding binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        Context context = holder.itemView.getContext();
        
        holder.binding.tvTitle.setText(movie.getTitle());
        holder.binding.tvGenre.setText(movie.getGenre());
        holder.binding.tvReleaseDate.setText(movie.getReleaseDate() != null ? movie.getReleaseDate() : "Chưa cập nhật");
        holder.binding.tvDuration.setText(movie.getDuration() != null ? movie.getDuration() : "");
        holder.binding.tvAgeRating.setText(movie.getAgeRating() != null ? movie.getAgeRating() : "");

        String poster = movie.getPosterUrl();
        if (poster != null && !poster.isEmpty()) {
            if (poster.startsWith("http")) {
                Glide.with(context)
                        .load(poster)
                        .placeholder(android.R.drawable.progress_horizontal)
                        .error(android.R.drawable.ic_menu_report_image)
                        .into(holder.binding.ivPoster);
            } else {
                // Load từ drawable local
                int resId = context.getResources().getIdentifier(poster, "drawable", context.getPackageName());
                if (resId != 0) {
                    Glide.with(context).load(resId).into(holder.binding.ivPoster);
                } else {
                    holder.binding.ivPoster.setImageResource(android.R.drawable.ic_menu_report_image);
                }
            }
        } else {
            holder.binding.ivPoster.setImageResource(android.R.drawable.ic_menu_report_image);
        }

        // Click vào nút Đặt vé hoặc cả item đều dẫn đến Schedule
        holder.binding.btnBook.setOnClickListener(v -> listener.onMovieClick(movie));
        holder.itemView.setOnClickListener(v -> listener.onMovieClick(movie));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ItemMovieBinding binding;

        public MovieViewHolder(ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}