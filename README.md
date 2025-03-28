# 🎰 Mini Casino Backend

This is a simple Spring Boot backend for a mini casino application. It supports user registration, betting, game management, and XML-based game importing.

---

## 🛠️ How to Run the Project

### ✅ Prerequisites

- Java 17 or later  
- Maven 3.6+  
- IDE (e.g., IntelliJ, VS Code) or terminal access  
- (Optional) Postman or Swagger UI for testing

---

### 🚀 Run the Application

#### Option 1: Using an IDE

1. Open the project in your IDE.
2. Locate and run the main class:
   ```
   com.erika.minicasino.MiniCasinoApplication
   ```

#### Option 2: Using the terminal

```bash
# Navigate to the project root
cd MiniCasino

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

---

### 🌐 API Access

Once running, the application is available at:

```
http://localhost:8080
```

#### ✅ Swagger UI

Use Swagger UI to explore and test APIs interactively:

```
http://localhost:8080/swagger-ui.html
```

---

### 📂 Game Initialization from XML

The game list can be initialized or updated using one of the following methods:

---

#### ✅ Option 1: Load from `games.xml` at Startup

At application startup, the system attempts to load games from a file named:

```
src/main/resources/games.xml
```

- If the file exists and is valid, the game list is loaded from it.
- If the file does **not** exist, the system automatically falls back to **default hardcoded game data** (see Option 2).
- This logic is handled during the construction of `GameServiceImpl`.

```xml
<!-- Example games.xml format -->
<games>
  <game>
    <id>1</id>
    <name>Slot Machine</name>
    <chanceOfWinning>0.2</chanceOfWinning>
    <winningMultiplier>5.0</winningMultiplier>
    <minBet>1.0</minBet>
    <maxBet>100.0</maxBet>
  </game>
  <!-- Add more game entries here -->
</games>
```

---

#### ✅ Option 2: Use Default In-Memory Game List

If `games.xml` is not found at startup, the following default games are loaded:

```java
private void initializeGames() {
    gameMap.put(1L, new Game(1L, "Slot Machine", 0.2, 5.0, 1.0, 100.0));
    gameMap.put(2L, new Game(2L, "Roulette", 0.4, 2.5, 2.0, 50.0));
    gameMap.put(3L, new Game(3L, "Blackjack", 0.5, 2.0, 5.0, 200.0));
}
```

These are useful as demo or fallback content for development and testing.

---

#### ✅ Option 3: Upload XML via API (Runtime Update)

You can dynamically upload and overwrite the game list **after the application has started** using the `/game/upload-xml` endpoint.

- **Method:** `POST`
- **Endpoint:** `/game/upload-xml`
- **Content-Type:** `multipart/form-data`
- **Form Field:** `file`

Supported via:

- **Swagger UI** → [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Postman** or curl:

```bash
curl -X POST http://localhost:8080/game/upload-xml \
  -F "file=@src/main/resources/games.xml"
```

✅ **Note:** When uploading, the existing in-memory game list is completely **cleared** before adding the new games from XML.

---

### 🧪 Running Tests

```bash
mvn test
```

Tests include user registration, deposit, bet placement, balance checks, and game loading.

---

## 🧭 API Endpoints Summary

| Method | Endpoint               | Description                     |
|--------|------------------------|---------------------------------|
| GET    | `/user`                | Get all users                   |
| POST   | `/user/register`       | Register a new user             |
| POST   | `/user/deposit`        | Deposit to user balance         |
| GET    | `/user/balance`        | Get current balance             |
| POST   | `/user/placeBet`       | Place a bet                     |
| GET    | `/user/betSummary`     | Get bet history summary         |
| GET    | `/game`                | List all games                  |
| GET    | `/game/{id}`           | Get game by ID                  |
| POST   | `/game/games`          | Add a new game                  |
| POST   | `/game/upload-xml`     | Upload games from an XML file   |

---

### 💡 Tips

- To customize the startup games, edit or add `games.xml` in `src/main/resources`.
- All API responses are wrapped in `BaseResponse<T>` with fields: `code`, `message`, `data`, and (if error) `description`.
