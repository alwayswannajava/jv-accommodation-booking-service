
# Accommodation Booking Service  

## 🌟 Introduction  
The **Accommodation Booking Service** is designed to simplify the process of finding and booking accommodations online. Whether you're planning a getaway or managing rental properties, this platform streamlines the experience for both travelers and hosts. Inspired by the need for efficient, reliable, and user-friendly booking systems, this project bridges the gap between service providers and their customers with modern technology.  

---

## 🛠️ Technologies and Tools  

This project leverages a robust tech stack to deliver performance, scalability, and security:  
- **Backend Framework:** Spring Boot 3.2.1
- **Persistence Layer:** Spring Data JPA, Jakarta Validation 
- **Security:** Spring Security
- **Database:** PostgreSQL 42.7.1
- **Caching:** Redis
- **Payment Integration:** Stripe API 28.0.1
- **Containerization:** Docker 25.0.0
- **Notification System:** Telegram (bots and chats) 8.0

---

## 🚀 Features and Functionality  

### Core Controllers and Services:  
- **Accommodation Controller:**  
  - Create, update, and delete accommodation.   

- **Booking Controller:**  
  - Reserve accommodations with real-time availability checks.  
  - Cancel bookings and manage booking histories.  

- **Payment Controller:**  
  - Integrates with Stripe API to handle secure transactions.  
  - Manage refunds and payment confirmations.
  
- **User Management Controller:**  
  - Handles user authentication and role-based access (Spring Security).  
  - Update user profiles and manage account settings
  
- **Notification Service:**  
  - Sends real-time booking updates to users via Telegram.  
  - Notify hosts about new bookings or cancellations.
  - Notify hosts about successful payments.
 
---

## 🛠️ Setting Up the Project  

### Prerequisites:  
- Java 17+  
- Docker  
- PostgreSQL installed locally or accessible remotely  
- Redis server running  

### Steps to Run Locally:  
1. **Clone the Repository:**  
   ```bash  
   git clone https://github.com/your-repo/accommodation-booking-service.git  
   cd accommodation-booking-service  
   ```  

2. **Configure the Environment:**  
   - Create a `.env` file in the root directory with:  
     ```dotenv  
     POSTGRESQLDB_USER=
     POSTGRESQLDB_ROOT_PASSWORD=
     POSTGRESQLDB_DATABASE=
     POSTGRESQLDB_LOCAL_PORT=
     POSTGRESQLDB_DOCKER_PORT=
     SPRING_LOCAL_PORT=
     SPRING_DOCKER_PORT=
     DEBUG_PORT=
     REDIS_PORT=
     REDIS_HOST=localhost
     JWT_SECRET=
     STRIPE_SECRET_KEY=stripe-secret-key
     TELEGRAM_BOT_SECRET_KEY=telegram-bot-key
     ```  

3. **Build and Start Containers:**  
   ```bash  
   docker-compose up --build  
   ```  

4. **Access the Application:**  
   - API Documentation (Swagger UI): [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  

5. **Run Tests (Optional):**  
   ```bash  
   ./mvnw test  
   ```  

---

## 🎯 Challenges and How We Overcame Them  

1. **Payment Gateway Integration:**  
   - *Challenge:* Understanding Stripe's API and webhooks.  
   - *Solution:* Leveraged extensive documentation and tested thoroughly with sandbox accounts.  

2. **Real-time Notifications:**  
   - *Challenge:* Implementing Telegram bots efficiently.  
   - *Solution:* Built reusable utility classes for message formatting and API calls.  

3. **Data Caching with Redis:**  
   - *Challenge:* Handling cache invalidation for dynamic content.  
   - *Solution:* Designed a cache eviction strategy based on event-driven updates.  

4. **Secure and Scalable Architecture:**  
   - *Challenge:* Implementing role-based access and securing sensitive endpoints.  
   - *Solution:* Configured Spring Security with JWT for authentication and fine-grained access control.

## 📝 API Endpoints

### Authentication Controller
```
POST: /register - User account registration
POST: /login - JWT token generation
```
### User Controller
```
PUT: /users/{id}/role - Update user roles
GET: /users/me - Retrieve current user profile
PUT/PATCH: /users/me - Update user profile
```
### Accommodation Controller
```
POST: /accommodations - Add new accommodation
GET: /accommodations - List available accommodations
GET: /accommodations/{id} - Get accommodation details
PUT/PATCH: /accommodations/{id} - Update accommodation
DELETE: /accommodations/{id} - Remove accommodation
```
### Booking Controller
```
POST: /bookings - Create new booking
GET: /bookings/ - Retrieve bookings (with filters)
GET: /bookings/my - User's personal bookings
GET: /bookings/{id} - Get specific booking details
PUT/PATCH: /bookings/{id} - Update booking
DELETE: /bookings/{id} - Cancel booking
```
### Payment Controller
```
GET: /payments/ - Retrieve payment information
POST: /payments/ - Initiate payment session
GET: /payments/success/ - Handle successful payment
GET: /payments/cancel/ - Manage payment cancellation
```
## 📺 Visual 
### Flowchart 

![image](https://github.com/user-attachments/assets/3277fd40-9970-4db9-901c-bf85da8487b0)

### Accommodation service collections 
```
[[https://web.postman.co/workspace/My-Workspace~094913b8-3731-4cce-8108-9a9ee90872b2/api/8847db5f-52c1-4c57-859b-79c52f435a47/collection/25455394-423d22c9-14bb-42ee-a469-4bf86dde8005?action=share&source=copy-link&creator=25455394](https://web.postman.co/workspace/My-Workspace~094913b8-3731-4cce-8108-9a9ee90872b2/api/029af70d-0ece-4399-bb5f-1eb8ddf500ca/collection/25455394-0ce36e2b-54cf-4da5-8511-1ed5fd5f3d76?action=share&source=copy-link&creator=25455394)](https://web.postman.co/workspace/My-Workspace~094913b8-3731-4cce-8108-9a9ee90872b2/api/029af70d-0ece-4399-bb5f-1eb8ddf500ca?action=share&source=copy-link&creator=25455394)
```
---

## 🤝 Contributing  

Contributions are welcome! Please fork the repository and submit a pull request.  

---
## 👥 Team
- Mykhailo Kornukh - Junior Backend Developer

## 📬 Contact
For any questions or feedback, please reach out to:
- Email: mykhailo.kornukh@gmail.com
- Telegram: @miSHYRIK
- Discord: stress_ful
---
