package com.example.a8_4_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a8_4_android.adapters.DateAdapter;
import com.example.a8_4_android.databinding.ActivityScheduleBinding;
import com.example.a8_4_android.databinding.ItemTheaterBinding;
import com.example.a8_4_android.models.Theater;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {

    private ActivityScheduleBinding binding;
    private String movieId;
    private String movieTitle;
    private List<Date> dates = new ArrayList<>();
    private List<Theater> theaters = new ArrayList<>();
    private TheaterAdapter theaterAdapter;
    private Date selectedDate;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        movieId = getIntent().getStringExtra("movieId");
        movieTitle = getIntent().getStringExtra("movieTitle");
        binding.tvMovieTitleHeader.setText(movieTitle);

        setupDateList();
        setupTheaterList();
    }

    private void setupDateList() {
        Calendar calendar = Calendar.getInstance();
        selectedDate = calendar.getTime();
        for (int i = 0; i < 7; i++) {
            dates.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        DateAdapter dateAdapter = new DateAdapter(dates, date -> {
            selectedDate = date;
            theaterAdapter.notifyDataSetChanged();
        });
        binding.rvDates.setAdapter(dateAdapter);
    }

    private void setupTheaterList() {
        theaters.add(new Theater("1", "CGV Aeon Hà Đông", "Tầng 3 & 4 - TTTM AEON MALL Hà Đông"));
        theaters.add(new Theater("2", "Lotte Cinema Hà Đông", "Tầng 4, TTTM Mê Linh Plaza"));
        theaters.add(new Theater("3", "BHD Star Phạm Ngọc Thạch", "Tầng 8, Vincom Center Phạm Ngọc Thạch"));

        theaterAdapter = new TheaterAdapter(theaters);
        binding.rvTheaters.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTheaters.setAdapter(theaterAdapter);
    }

    private class TheaterAdapter extends RecyclerView.Adapter<TheaterAdapter.TheaterViewHolder> {
        private List<Theater> theaterList;

        public TheaterAdapter(List<Theater> theaterList) {
            this.theaterList = theaterList;
        }

        @NonNull
        @Override
        public TheaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemTheaterBinding itemBinding = ItemTheaterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new TheaterViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull TheaterViewHolder holder, int position) {
            Theater theater = theaterList.get(position);
            holder.itemBinding.tvTheaterName.setText(theater.getName());
            holder.itemBinding.tvTheaterLocation.setText(theater.getLocation());

            holder.itemBinding.cgShowtimes.removeAllViews();
            
            List<String> dynamicTimes = getDynamicTimes();
            for (String time : dynamicTimes) {
                Chip chip = new Chip(ScheduleActivity.this);
                chip.setText(time);
                chip.setClickable(true);
                chip.setCheckable(false);
                chip.setOnClickListener(v -> {
                    Intent intent = new Intent(ScheduleActivity.this, BookingActivity.class);
                    intent.putExtra("movieId", movieId);
                    intent.putExtra("movieTitle", movieTitle);
                    intent.putExtra("theaterName", theater.getName());
                    intent.putExtra("selectedTime", time);
                    intent.putExtra("selectedDate", selectedDate.getTime());
                    startActivity(intent);
                });
                holder.itemBinding.cgShowtimes.addView(chip);
            }
        }

        private List<String> getDynamicTimes() {
            List<String> times = new ArrayList<>();
            Calendar now = Calendar.getInstance();
            
            // Chỉ thêm suất 1 phút nếu đang chọn ngày hôm nay
            Calendar selectedCal = Calendar.getInstance();
            selectedCal.setTime(selectedDate);
            
            boolean isToday = selectedCal.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                             selectedCal.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR);

            if (isToday) {
                Calendar testTime = (Calendar) now.clone();
                testTime.add(Calendar.MINUTE, 1);
                times.add(timeFormat.format(testTime.getTime()));
            }

            // Các suất chiếu cố định
            String[] fixedTimes = {"10:15", "13:30", "16:45", "19:00", "21:15"};
            for (String ft : fixedTimes) {
                if (!isToday) {
                    times.add(ft);
                } else {
                    // Nếu là hôm nay, chỉ thêm các giờ chưa tới
                    try {
                        Date d = timeFormat.parse(ft);
                        Calendar c = Calendar.getInstance();
                        c.setTime(d);
                        c.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                        
                        if (c.after(now)) {
                            times.add(ft);
                        }
                    } catch (Exception e) {}
                }
            }
            return times;
        }

        @Override
        public int getItemCount() {
            return theaterList.size();
        }

        class TheaterViewHolder extends RecyclerView.ViewHolder {
            ItemTheaterBinding itemBinding;
            public TheaterViewHolder(ItemTheaterBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }
        }
    }
}