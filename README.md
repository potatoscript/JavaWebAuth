# 🔐 JavaWebAuth: Fullstack Web Authentication Demo

Build a complete authentication system with:

- ✅ **Frontend**: HTML + JavaScript (hosted on GitHub Pages)
- ✅ **Backend**: Java Spring Boot + SQLite (deployed to Railway)
- ✅ **Repository**: [`JavaWebAuth`](https://github.com/potatoscript/JavaWebAuth)

---

## 📁 Project Structure

```
JavaWebAuth/
├── backend/       ← Spring Boot + SQLite REST API
└── frontend/      ← HTML + JS UI
```

---

## 🚀 1. Backend Setup: Java + Spring Boot + SQLite

### ✅ Step 1: Generate Spring Boot Project

Go to [https://start.spring.io](https://start.spring.io):

- Project: Maven
- Language: Java
- Spring Boot: 3.x
- Project Name: `JavaWebAuth`
- Packaging: Jar
- Java: 17 or 21
- Dependencies:
  - Spring Web
  - Spring Data JPA

Click "Generate" and unzip it into `JavaWebAuth/backend/`.

### ✅ Step 2: Add SQLite Dependency in `backend/pom.xml`

```xml
<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
    <version>3.42.0.0</version>
</dependency>
```

---

### ✅ Step 3: Configure SQLite in `application.properties`

Create this in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:sqlite:auth.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
server.port=8080
```

> 🔧 You’ll need a custom `SQLiteDialect` for Hibernate, or you can use a simplified one (I can provide one if needed).

---

### ✅ Step 4: Define `User` Entity

Create `User.java`:

```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    // Getters and setters
}
```

---

### ✅ Step 5: Create `UserRepository`

```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
```

---

### ✅ Step 6: Create AuthController

```java
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Allow frontend to access
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        userRepo.save(user);
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        return userRepo.findByUsername(user.getUsername())
            .filter(u -> u.getPassword().equals(user.getPassword()))
            .map(u -> ResponseEntity.ok("Login successful"))
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials"));
    }
}
```

#### 🔧 Folder structure in `src/main/java` should look like:

```
JavaWebAuth/backend/src/main/java/com/example/javawebauth/
├── controller/
│   └── AuthController.java
├── model/
│   └── User.java
├── repository/
│   └── UserRepository.java   ✅ ← create it here!
└── JavaWebAuthApplication.java
```

---

## ☁️ 2. Deploy Backend to Railway

### ✅ Step 1: Push to GitHub

Create a repo named `JavaWebAuth`:

```bash
cd JavaWebAuth
git init
git remote add origin https://github.com/potatoscript/JavaWebAuth.git
git add .
git commit -m "Initial commit"
git push -u origin main
```

### ✅ Step 2: Deploy to Railway

1. Go to [https://railway.app](https://railway.app)
2. Click **New Project → Deploy from GitHub Repo**
3. Select `JavaWebAuth`
4. Railway auto-builds the Spring Boot app
5. After successful deploy, copy your public URL, e.g.,  
   `https://javawebauth.up.railway.app`

---

## 🌐 3. Frontend Setup (HTML + JavaScript)

### ✅ Step 1: Create `frontend/index.html`

```html
<!DOCTYPE html>
<html>
<head>
  <title>JavaWebAuth Demo</title>
</head>
<body>
  <h2>Register</h2>
  <input id="reg-user" placeholder="Username">
  <input id="reg-pass" type="password" placeholder="Password">
  <button onclick="register()">Register</button>

  <h2>Login</h2>
  <input id="login-user" placeholder="Username">
  <input id="login-pass" type="password" placeholder="Password">
  <button onclick="login()">Login</button>

  <p id="response"></p>

  <script>
    const API = "https://javawebauth.up.railway.app/api/auth"; // Use your Railway backend URL

    async function register() {
      const res = await fetch(`${API}/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          username: document.getElementById("reg-user").value,
          password: document.getElementById("reg-pass").value
        })
      });
      document.getElementById("response").textContent = await res.text();
    }

    async function login() {
      const res = await fetch(`${API}/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          username: document.getElementById("login-user").value,
          password: document.getElementById("login-pass").value
        })
      });
      document.getElementById("response").textContent = await res.text();
    }
  </script>
</body>
</html>
```

---

## 📦 4. Deploy Frontend to GitHub Pages

### ✅ Step 1: Move into `frontend` and Push

```bash
cd JavaWebAuth/frontend
git init
git remote add origin https://github.com/potatoscript/JavaWebAuth.git
git checkout -b gh-pages
git add index.html
git commit -m "Add frontend"
git push -u origin gh-pages
```

### ✅ Step 2: Enable GitHub Pages

- Go to GitHub → Settings → Pages
- Select branch: `gh-pages`, directory: `/ (root)`
- Save
- Access at:  
  `https://potatoscript.github.io/JavaWebAuth/`

---

## ✅ Final Result

- 🔗 Frontend URL (GitHub Pages):  
  `https://potatoscript.github.io/JavaWebAuth/`
- 🔗 Backend API URL (Railway):  
  `https://javawebauth.up.railway.app/api/auth`

---

## 🛡️ Notes

- This is a demo only. In production, you must:
  - Hash passwords with BCrypt
  - Use JWT for auth
  - Add CORS configuration properly
  - Sanitize user input
  - Use HTTPS

---
