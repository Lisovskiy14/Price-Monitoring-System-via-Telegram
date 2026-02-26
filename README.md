# 📦 Price Monitoring System (Telegram Bot)

[![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://www.oracle.com/java/technologies/downloads/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=for-the-badge&logo=postgresql)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-ready-blue?style=for-the-badge&logo=docker)](https://www.docker.com/)

**Price Monitoring System** is a high-performance backend service designed for automatic price and availability tracking across popular online stores. The system instantly notifies users via Telegram whenever a price drops or a product returns to stock.

---

## ✨ Key Features

* **Automatic Scraping:** Leverages the `Jsoup` library to extract real-time price and availability data directly from HTML pages.
* **High Concurrency (Java 21):** Implements **Virtual Threads** to process thousands of products simultaneously without blocking system resources during I/O operations.
* **Load Management:** Utilizes a `Semaphore` to limit the number of concurrent requests, preventing IP bans from marketplaces and database overloads.
* **Smart Notifications:** A "Snapshot" comparison algorithm ensures alerts are sent only when a genuine price reduction or availability change occurs.
* **Telegram Integration:** A seamless user experience allowing everything from product registration to receiving personalized alerts via a bot.

---

## 🤖 Bot Commands

Interact with the bot using the following commands:

* **`<URL>`**: Simply send a product link to register it in your tracking list.
* **`/all`**: View a complete list of all products currently being tracked for you.
* **`/remove <URL>`**: Remove a specific URL from your tracking list.

---

## 🛠 Tech Stack

| Category | Technology |
| :--- | :--- |
| **Backend Core** | Java 21, Spring Boot 3.x, Spring Scheduling |
| **Concurrency** | Virtual Threads, Semaphore, CompletableFuture |
| **Data Storage** | Spring Data JPA, PostgreSQL, Hibernate |
| **Database Migrations** | Liquibase |
| **Web Scraping** | Jsoup |
| **Integration** | Telegram Bot API |
| **DevOps** | Docker, Docker Compose, Gradle |

---

## 🏗 Monitoring Architecture



The price check process is implemented as a cyclic background task:

1.  **Pagination:** The service fetches products from the database in batches (Pages), allowing the system to scale to millions of records without RAM exhaustion.
2.  **Asynchrony:** A virtual thread is created for every product in the batch to perform parallel scraping.
3.  **Synchronization:** The main thread waits for the entire batch to complete using `CompletableFuture.allOf().join()`, then performs batch updates to the DB.
4.  **Validation:** The system evaluates `isCheaper` and `becomeAvailable` conditions before triggering a notification.



---

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone [https://github.com/Lisovskiy14/Price-Monitoring-System-via-Telegram.git](https://github.com/Lisovskiy14/Price-Monitoring-System-via-Telegram.git)
cd Price-Monitoring-System-via-Telegram
```

## 2. Telegram Bot Setup

To run the service, you must obtain an API token and provide it in the configuration:

1. Find **@BotFather** on Telegram.
2. Create a new bot and receive your **API Token**.
3. Create `.env` file in the root directory and fill in the following fields:
```
BOT_TOKEN=
BOT_USERNAME=
```

## 3. Run via Docker Compose

The project is fully containerized, allowing you to deploy the PostgreSQL database and the Java application with a single command:

```bash
# 1. Build and start services in the background
docker-compose up --build -d

# 2. View application logs
docker-compose logs -f price_monitoring_app
```

After launch:

- **Liquibase** will automatically create all necessary tables in PostgreSQL.
- The **App** will connect to the DB and begin monitoring according to the schedule.
