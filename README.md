# Aufgabe:
Codewars als Rest-Anwendung
- REST-Anwendung (Wenn es in eurer Programmiersprache nicht zu 100% mit REST-"Standard" übereinstimmt, dann nehmt die Branchenübliche Lösung)
- TDD (Unit-Tests)
- Muss nicht persistente Daten enthalten
- Sprache egal

# Backend:

## TODOs:
- Highscore Liste ausgeben
- ~~Aufgaben hinzufügen~~
- ~~Aufgaben editieren~~
- ~~Aufgaben abrufen~~
- Ergebnis eintragen, Punkte bekommen
- Nutzerverwaltung
- ~~Nutzer hinzufügen~~
- ~~Nutzer löschen~~
- ~~Nutzer Punkte addieren~~
- Put Mapping anpassen
- Spring Security Principal einführen
- ~~Swagger einbinden~~

## Voraussetzungen:
- Java 17
- Docker
- Postman (optional)

## Installation:
Docker starten

Im Terminal im backend Folder folgenden Befehl eingeben:

```
docker-compose -p restbildungsrunde up -d --force-recreate --no-deps --build
```

Damit sollte ein Container namens "backend" laufen. Dieser beinhaltet die Datenbank und das Backend.

## Swagger / Openapi:
Swagger/Openapi sind nach dem Starten unter folgenden Adressen erreichbar:

Swagger : ```localhost:8080/swagger```

Openapi : ```localhost:8080/openapi```


## Schnittstellen:

### User Schnittstellen:
- GET http://localhost:8080/api/user/{id} - User mit der ID {id} abfragen
- POST http://localhost:8080/api/user - User anlegen (User im RequestBody erforderlich)
- PUT http://localhost:8080/api/user/{id} - User mit der ID {id} updaten (User im RequestBody erforderlich)
- DELETE http://localhost:8080/api/user/{id} - User mit der ID {id} löschen

### Exercise Schnittstellen:
- GET http://localhost:8080/api/exercise/{id} - Exercise mit der ID {id} abfragen
- POST http://localhost:8080/api/exercise - Exercise anlegen (Exercise im RequestBody erforderlich)
- PUT http://localhost:8080/api/exercise/{id} - Exercise mit der ID {id} updaten (Exercise im RequestBody erforderlich)
- DELETE http://localhost:8080/api/exercise/{id} - Exercise mit der ID {id} löschen

## Datenbank:
Die Datenbank ist eine Postgres Datenbank und ist unter folgender Adresse erreichbar:

```
localhost:5432
user: postgres
password: SomeRandomPassword
```




# Frontend:
WIP