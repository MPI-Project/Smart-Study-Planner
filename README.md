# Unit testing

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
