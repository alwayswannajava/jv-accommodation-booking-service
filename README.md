Accommodation Booking Service
🌟 Introduction
The Accommodation Booking Service is designed to simplify the process of finding and booking accommodations online. Whether you're planning a getaway or managing rental properties, this platform streamlines the experience for both travelers and hosts. Inspired by the need for efficient, reliable, and user-friendly booking systems, this project bridges the gap between service providers and their customers with modern technology.

🛠️ Technologies and Tools
This project leverages a robust tech stack to deliver performance, scalability, and security:

Backend Framework: Spring Boot
Persistence Layer: Spring Data JPA, Hibernate
Security: Spring Security
Database: PostgreSQL
Caching: Redis
Payment Integration: Stripe API
Containerization: Docker
Notification System: Telegram (bots and chats)
🚀 Features and Functionality
Core Controllers:
Accommodation Controller:

Create, update, and delete listings.
View and search available accommodations by filters (location, price, dates, etc.).
Booking Controller:

Reserve accommodations with real-time availability checks.
Cancel bookings and manage booking histories.
Payment Controller:

Integrates with Stripe API to handle secure transactions.
Manage refunds and payment confirmations.
Notification Controller:

Sends real-time booking updates to users via Telegram.
Notify hosts about new bookings or cancellations.
User Management Controller:

Handles user authentication and role-based access (Spring Security).
Update user profiles and manage account settings.
🛠️ Setting Up the Project
Prerequisites:
Java 17+
Docker
PostgreSQL installed locally or accessible remotely
Redis server running
Steps to Run Locally:
Clone the Repository:

git clone https://github.com/your-repo/accommodation-booking-service.git  
cd accommodation-booking-service  
Configure the Environment:

Create a .env file in the root directory with:
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
STRIPE_SECRET_KEY=your_stripe_secret_key
TELEGRAM_BOT_SECRET_KEY=you_telegram_bot_key
Build and Start Containers:

docker-compose up --build  
Access the Application:

API Documentation (Swagger UI): http://localhost:8080/swagger-ui.html
Run Tests (Optional):

./mvnw test  
🎯 Challenges and How We Overcame Them
Payment Gateway Integration:

Challenge: Understanding Stripe's API and webhooks.
Solution: Leveraged extensive documentation and tested thoroughly with sandbox accounts.
Real-time Notifications:

Challenge: Implementing Telegram bots efficiently.
Solution: Built reusable utility classes for message formatting and API calls.
Data Caching with Redis:

Challenge: Handling cache invalidation for dynamic content.
Solution: Designed a cache eviction strategy based on event-driven updates.
Secure and Scalable Architecture:

Challenge: Implementing role-based access and securing sensitive endpoints.
Solution: Configured Spring Security with JWT for authentication and fine-grained access control.
🤝 Contributing
Contributions are welcome! Please fork the repository and submit a pull request.

📧 Contact
For any queries or suggestions, feel free to reach out at mykhailo.kornukh@gmail.com

Let me know if you'd like me to modify or add anything! 😊
