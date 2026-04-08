package com.example.a8_4_android.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a8_4_android.databinding.ItemDateBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

    private List<Date> dateList;
    private int selectedPosition = 0;
    private OnDateClickListener listener;
    private SimpleDateFormat dayFormat = new SimpleDateFormat("EE", new Locale("vi", "VN"));
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());

    public interface OnDateClickListener {
        void onDateClick(Date date);
    }

    public DateAdapter(List<Date> dateList, OnDateClickListener listener) {
        this.dateList = dateList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDateBinding binding = ItemDateBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DateViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        Date date = dateList.get(position);
        holder.binding.tvDayOfWeek.setText(dayFormat.format(date).toUpperCase());
        holder.binding.tvDateMonth.setText(dateFormat.format(date));

        if (selectedPosition == position) {
            holder.binding.cvDate.setCardBackgroundColor(Color.parseColor("#3F51B5"));
            holder.binding.tvDayOfWeek.setTextColor(Color.WHITE);
            holder.binding.tvDateMonth.setTextColor(Color.WHITE);
        } else {
            holder.binding.cvDate.setCardBackgroundColor(Color.WHITE);
            holder.binding.tvDayOfWeek.setTextColor(Color.BLACK);
            holder.binding.tvDateMonth.setTextColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(v -> {
            int oldPos = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(oldPos);
            notifyItemChanged(selectedPosition);
            listener.onDateClick(date);
        });
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public static class DateViewHolder extends RecyclerView.ViewHolder {
        ItemDateBinding binding;
        public DateViewHolder(ItemDateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}