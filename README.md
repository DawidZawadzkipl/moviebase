# MovieBase

Mini klon Filmwebu: Spring Boot + Thymeleaf + Bootstrap + PostgreSQL/MySQL.

## Co jest gotowe
- logowanie i rejestracja,
- role `ADMIN` i `USER`,
- BCrypt dla haseł,
- CRUD filmów w panelu admina,
- upload plakatu,
- gatunki,
- wyszukiwarka,
- stronicowanie,
- oceny 1-10,
- recenzje,
- watchlista,
- walidacja,
- i18n PL/EN.

## Uruchomienie z PostgreSQL

```sql
CREATE DATABASE moviebase;
```

W `src/main/resources/application.properties` ustaw login/hasło do bazy.

```bash
mvn spring-boot:run
```

Adres: `http://localhost:8090`

## Konta testowe
Tworzą się automatycznie przy pierwszym starcie:

- `admin@example.com` / `admin123`
- `user@example.com` / `user123`

## Tomcat
Projekt buduje się jako `.war`:

```bash
mvn clean package
```

Plik z `target/` można wrzucić do `webapps` w Apache Tomcat 10.1+.
