package com.example.a8_4_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.a8_4_android.adapters.MovieAdapter;
import com.example.a8_4_android.databinding.ActivityMainBinding;
import com.example.a8_4_android.models.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MovieAdapter adapter;
    private List<Movie> movieList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        movieList = new ArrayList<>();
        adapter = new MovieAdapter(movieList, movie -> {
            Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
            intent.putExtra("movieId", movie.getId());
            intent.putExtra("movieTitle", movie.getTitle());
            startActivity(intent);
        });

        binding.rvMovies.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMovies.setAdapter(adapter);

        binding.fabLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        // Cập nhật dữ liệu mới lên Firestore trước khi load
        addDummyData();
    }

    private void loadMovies() {
        db.collection("movies").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                movieList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Movie movie = document.toObject(Movie.class);
                    movieList.add(movie);
                }
                adapter.notifyDataSetChanged();
            } else {
                String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                Log.e("FirestoreError", "Error: " + error);
            }
        });
    }

    private void addDummyData() {
        List<Movie> dummyMovies = new ArrayList<>();
        
        // Cập nhật đúng tên ảnh local bạn đã đặt: "endgame" và "inter"
        dummyMovies.add(new Movie("1", "Avengers: Endgame", "Action, Sci-Fi", 
                "endgame", 
                "Sau sự kiện thảm khốc trong Infinity War, các siêu anh hùng còn sống sót cố gắng đảo ngược hành động của Thanos.",
                "26/04/2019", "181 phút", "T13"));
        
        dummyMovies.add(new Movie("2", "Interstellar", "Adventure, Sci-Fi", 
                "inter", 
                "Một nhóm phi hành gia du hành qua một hố đen để tìm kiếm một hành tinh mới cho nhân loại.",
                "07/11/2014", "169 phút", "T13"));

        // Thực hiện ghi đè dữ liệu lên Firestore để cập nhật các trường mới
        for (Movie movie : dummyMovies) {
            db.collection("movies").document(movie.getId()).set(movie)
                .addOnSuccessListener(aVoid -> loadMovies()); // Load lại sau khi update thành công
        }
    }
}