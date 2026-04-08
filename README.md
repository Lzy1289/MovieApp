
# 🎬 Movie Ticket Booking App
<br>

Ứng dụng đặt vé xem phim trực tuyến sử dụng ngôn ngữ Java trên nền tảng Android Studio, tích hợp Firebase làm Backend.
<br>

## 🚀 Chức năng chính
- **Đăng ký tài khoản mới**.
<br>
<img width="396" height="746" alt="image" src="https://github.com/user-attachments/assets/be300ec6-e79c-492a-8aa4-b187b10df383" />
<br>
- **Đăng nhập**
<br>
<img width="356" height="794" alt="image" src="https://github.com/user-attachments/assets/3c2b676b-986f-4be5-8ee4-770a220ae08c" />
<br>
- **Quản lý danh sách phim:**
<br>
<img width="371" height="794" alt="image" src="https://github.com/user-attachments/assets/491a0a66-4fa6-4c97-99bc-a34f8c984158" />
<br>
- **Hệ thống đặt vé:**
<br>
  - Chọn rạp chiếu và suất chiếu.
<br>
<img width="387" height="788" alt="image" src="https://github.com/user-attachments/assets/fc7c917f-c68a-44a6-afe6-f8278fba8c2c" />
<br>
  - Chọn vị trí ghế ngồi.
<br>
<img width="388" height="804" alt="image" src="https://github.com/user-attachments/assets/a34d21ec-789f-480f-8093-5f7737273dc0" />
<br>
- **Quản lý dữ liệu (Cloud Firestore):**
<br>
  - Lưu trữ thông tin người dùng, phim, rạp, suất chiếu và lịch sử đặt vé (Tickets).
<br>
<img width="364" height="791" alt="image" src="https://github.com/user-attachments/assets/cfdce153-e113-49d9-8ac9-8f8e1affa086" />
<img width="403" height="785" alt="image" src="https://github.com/user-attachments/assets/39db77ba-7f7e-4cb7-8920-734bf065ca56" />
<br>
- **Thông báo (Push Notifications):**
<br>
  - Gửi thông báo nhắc nhở giờ chiếu phim thông qua Firebase Cloud Messaging (FCM).
<br>
<img width="334" height="698" alt="image" src="https://github.com/user-attachments/assets/90b35a6b-ccbf-4347-a3af-7360c7ab96b4" />
<br>
## 📊 Cấu trúc Cơ sở dữ liệu (Firestore)
<br>
Ứng dụng quản lý dữ liệu thông qua các Collections:
<br>
* `movies`: Danh sách các bộ phim.
<br>
* `theaters`: Danh sách các rạp chiếu.
<br>
* `showtimes`: Lịch chiếu của từng bộ phim tại các rạp.
<br>
* `tickets`: Thông tin vé đã đặt của người dùng.
<br>
---
