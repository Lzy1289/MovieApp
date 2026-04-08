package com.example.a8_4_android;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a8_4_android.databinding.ActivityBookingBinding;
import com.example.a8_4_android.models.Ticket;
import com.example.a8_4_android.services.NotificationReceiver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class BookingActivity extends AppCompatActivity {

    private ActivityBookingBinding binding;
    private FirebaseFirestore db;
    private String movieId, movieTitle, theaterName, selectedTime;
    private long selectedDateMillis;
    private Set<String> selectedSeats = new HashSet<>();
    private SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private double unitPrice = 12.0; // Giá mặc định

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        
        // Nhận dữ liệu từ ScheduleActivity
        movieId = getIntent().getStringExtra("movieId");
        movieTitle = getIntent().getStringExtra("movieTitle");
        theaterName = getIntent().getStringExtra("theaterName");
        selectedTime = getIntent().getStringExtra("selectedTime");
        selectedDateMillis = getIntent().getLongExtra("selectedDate", System.currentTimeMillis());

        binding.tvMovieTitleHeader.setText(movieTitle);
        
        // Tạo đối tượng Date đầy đủ từ Ngày và Giờ đã chọn
        Date fullShowtime = getFullShowtimeDate();
        
        // Cập nhật thông tin rạp và suất chiếu to và rõ ràng hơn
        TextView tvTheater = new TextView(this);
        tvTheater.setText("Rạp: " + theaterName);
        tvTheater.setTextSize(22); // To hơn hẳn
        tvTheater.setTextColor(Color.BLACK);
        tvTheater.setTypeface(null, Typeface.BOLD);
        tvTheater.setPadding(0, 0, 0, 8);

        TextView tvTime = new TextView(this);
        tvTime.setText("Suất: " + displayFormat.format(fullShowtime));
        tvTime.setTextSize(18); // Rõ ràng
        tvTime.setTextColor(Color.parseColor("#616161")); // Xám đậm
        tvTime.setPadding(0, 0, 0, 32);

        binding.containerInfo.addView(tvTheater, 0);
        binding.containerInfo.addView(tvTime, 1);

        createSeatGrid();

        binding.btnBookTicket.setOnClickListener(v -> {
            if (selectedSeats.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ít nhất 1 ghế", Toast.LENGTH_SHORT).show();
                return;
            }
            showBillDialog(fullShowtime);
        });
    }

    private Date getFullShowtimeDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(selectedDateMillis);
        
        String[] parts = selectedTime.split(":");
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    private void showBillDialog(Date showtimeDate) {
        double totalPrice = unitPrice * selectedSeats.size();
        String seats = String.join(", ", selectedSeats);
        
        String billContent = "Phim: " + movieTitle + "\n" +
                          "Rạp: " + theaterName + "\n" +
                          "Suất chiếu: " + displayFormat.format(showtimeDate) + "\n" +
                          "Ghế chọn: " + seats + "\n" +
                          "Tổng tiền: $" + totalPrice;

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận thanh toán")
                .setMessage(billContent)
                .setPositiveButton("Thanh toán", (dialog, which) -> {
                    bookTicket(showtimeDate, seats, totalPrice);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void createSeatGrid() {
        char[] rows = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        int cols = 7;
        binding.glSeats.setColumnCount(cols);
        for (char row : rows) {
            for (int col = 1; col <= cols; col++) {
                String seatName = row + String.valueOf(col);
                Button seatButton = new Button(this);
                seatButton.setText(seatName);
                seatButton.setTextSize(12);
                android.widget.GridLayout.LayoutParams params = new android.widget.GridLayout.LayoutParams();
                params.width = 110; params.height = 110; params.setMargins(8, 8, 8, 8);
                seatButton.setLayoutParams(params);
                
                // Sử dụng drawable bo tròn
                seatButton.setBackgroundResource(R.drawable.seat_available);
                seatButton.setTextColor(Color.BLACK);
                seatButton.setAllCaps(false);
                
                seatButton.setOnClickListener(v -> {
                    if (selectedSeats.contains(seatName)) {
                        selectedSeats.remove(seatName);
                        seatButton.setBackgroundResource(R.drawable.seat_available);
                        seatButton.setTextColor(Color.BLACK);
                    } else {
                        selectedSeats.add(seatName);
                        seatButton.setBackgroundResource(R.drawable.seat_selected);
                        seatButton.setTextColor(Color.WHITE);
                    }
                    updateSelectedSeatsText();
                });
                binding.glSeats.addView(seatButton);
            }
        }
    }

    private void updateSelectedSeatsText() {
        if (selectedSeats.isEmpty()) {
            binding.tvSelectedSeats.setText("Ghế chọn: Chưa có");
        } else {
            double totalPrice = selectedSeats.size() * unitPrice;
            String seatList = String.join(", ", selectedSeats);
            
            // Highlight tổng tiền rất to và rõ ràng
            String text = "Ghế chọn: <b>" + seatList + "</b><br/>" +
                         "Tổng tiền: <font color='#3F51B5'><b><big><big><big>$" + totalPrice + "</big></big></big></b></font>";
            
            binding.tvSelectedSeats.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
        }
    }

    private void bookTicket(Date showtimeDate, String seats, double totalPrice) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String ticketId = db.collection("tickets").document().getId();
        Ticket ticket = new Ticket(ticketId, userId, "showtime_id", movieTitle, theaterName, showtimeDate, seats, totalPrice);

        db.collection("tickets").document(ticketId).set(ticket).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Đặt vé thành công!", Toast.LENGTH_LONG).show();
            scheduleNotification(showtimeDate, movieTitle);
            finish();
        });
    }

    private void scheduleNotification(Date date, String title) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("movieTitle", title);
        intent.putExtra("showtime", selectedTime); // Thêm dòng này để truyền giờ chiếu
        PendingIntent pi = PendingIntent.getBroadcast(this, (int)System.currentTimeMillis(), intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (am != null) am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, date.getTime(), pi);
    }
}
