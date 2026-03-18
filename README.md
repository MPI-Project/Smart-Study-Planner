# Smart-Study-Planner

## Run database with Docker

### 1. Create the environment file

Copy the example environment file:

```
cp .env.example .env
```
or rename it:

```
mv .env.example .env
```
---
Then modify the credentials in .env file to the ones provided by the admin

### 2. Start the database

From the project root run:
```
docker compose up -d
```

This will start the PostgreSQL container in the background

---
### 3. Verify the container is running
```
docker ps
```
You should see a container named `**smart-study-planner-db**` running on port `**5432**`

---
### 4. Stop the database

To stop the containers:
```
docker compose down
```
---
### 5. Reset the database

If you need to completely reset the database:
```
docker compose down -v
docker compose up -d
```

This removes the Docker volume and recreates the database from `database/init.sql`.

## Inspect the Database

### 1. Connect to the database container

Run:
```
docker exec -it smart-study-planner-db psql -U admin -d app
```


This opens the PostgreSQL interactive shell. You should see a prompt like:
```
app=#
```

---

### 2. List all tables

Inside the PostgreSQL shell run:
```
\dt
```
This will display all tables in the database.

---

### 3. View table structure

To see the columns of a table:
```
\d users
```
---

### 4. View data inside a table

To display the rows inside a table:
```
SELECT * FROM users;
```

---

### 5. Exit PostgreSQL

To exit PostgreSQL shell:
```
\q
```



### 6. Unit testing

**Requirements:** JDK 22, Maven. Use JDK 22 for build and tests (e.g. set `JAVA_HOME` or select it in the IDE). 
Lombok does not work on JDK 25+. 
If you run into issues with OpenJDK, [Amazon Corretto 22](https://docs.aws.amazon.com/corretto/latest/corretto-22-ug/downloads-list.html) is a known-working alternative.


**Run tests:**
- CLI: `mvn test`
- IntelliJ: right‑click `src/test/java` or a test class → Run

**Test layout:** `src/test/java` mirrors `src/main/java` package structure.

| Test class              | Under test        |
|-------------------------|-------------------|
| `UserValidatorTest`    | validation        |
| `AuthServiceTest`      | service           |
| `AuthControllerTest`   | REST API (MockMvc)|
| `UserRepositoryTest`   | repository (H2)   |

No running database needed for tests (H2 in memory).
