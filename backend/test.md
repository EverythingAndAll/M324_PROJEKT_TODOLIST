# Testbericht M324 ToDo-Liste

## Ziel

Im Rahmen von HANOK 4.1 wurden Systemintegrationstests fuer die ToDo-Applikation umgesetzt. Fokus war die fachliche Absicherung der wichtigsten Benutzeraktionen sowie der wichtigsten Fehlerfaelle auf Backend-Ebene.

Die automatisierten Tests befinden sich in:

- `backend/src/test/java/com/example/demo/DemoApplicationTests.java`

## Testumgebung

- Backend: Spring Boot 3 / Maven Wrapper
- Testframework: JUnit 5, Spring Boot Test, MockMvc
- Art der Tests: Systemintegrationstests gegen die REST-Endpunkte

## Abdeckung der vorgegebenen Aufgaben

### 1. Neues Element hinzufuegen

Automatisierter Test:

- `createTaskAddsItemToTaskList`

Prueft:

- POST auf `/tasks` erstellt eine neue Aufgabe
- Die Aufgabe erscheint anschliessend in der Aufgabenliste

### 2. Korrekte Anzahl und Anzeige in der Aufgabenliste

Automatisierter Test:

- `taskListContainsAllExpectedFieldsAfterLoad`

Prueft:

- Die Liste wird ueber `/tasks` geladen
- Die erwarteten Felder `id`, `taskdescription`, `priority`, `dueDate`, `creationDate`, `completed` sind vorhanden

### 3. "Erledigt"-Button funktioniert

Automatisierter Test:

- `completedButtonMarksTaskAsDone`

Prueft:

- Eine bestehende Aufgabe kann ueber `PUT /tasks/{id}` auf `completed=true` gesetzt werden
- Die Aufgabe erscheint danach im Filter `completed=true`

### 4. Entfernen eines Elements

Automatisierter Test:

- `deleteTaskRemovesTaskFromList`

Prueft:

- `DELETE /tasks/{id}` entfernt die Aufgabe
- Die Aufgabenliste ist danach leer

### 5. Fehlermeldung bei leerem Eintrag

Automatisierter Test:

- `emptyDescriptionsAreRejectedWithErrorMessage`

Prueft:

- Leere oder nur aus Leerzeichen bestehende Beschreibungen werden mit HTTP `400 Bad Request` abgelehnt
- Das Backend liefert eine konkrete Fehlermeldung zurueck

### 6. Fehlermeldung bei Fehlern rund um die Aufgabenliste

Automatisiert sinnvoll abgedeckt durch:

- `invalidUpdatesReturnNotFoundForMissingTask`

Prueft:

- Ungueltige Operationen auf nicht vorhandenen Aufgaben liefern einen sauberen Fehlerfall (`404 Not Found`)

Hinweis:

- Ein echter Netzwerk- oder Serverausfall beim Laden der Liste ist in diesen Backend-Tests nicht sinnvoll simulierbar, da genau das Backend selbst getestet wird.

### 7. Aufgabenliste nach dem Laden korrekt angezeigt

Automatisierte Tests:

- `taskListStartsEmpty`
- `taskListContainsAllExpectedFieldsAfterLoad`

Prueft:

- Leere Anfangsliste
- Korrekte Daten nach dem Anlegen

### 8. Doppelter Eintrag wird abgelehnt

Automatisierter Test:

- `duplicateDescriptionsAreRejectedWithErrorMessage`

Prueft:

- Eine Aufgabe mit identischer Beschreibung wird nicht doppelt gespeichert
- Die Rueckmeldung erfolgt ueber `400 Bad Request` mit Fehlermeldung

## Zusaetzliche umgesetzte User-Story-Tests

### Default-Prioritaet

Automatisierter Test:

- `defaultPriorityFallsBackToMedium`

Prueft:

- Ohne explizite Prioritaet wird `MEDIUM` gesetzt

### Filterfunktion

Automatisierter Test:

- `tasksCanBeFilteredByPriorityAndSearchText`

Prueft:

- Filtern nach Prioritaet
- Suchen nach Textfragment

### Listen-Reset

Automatisierter Test:

- `clearEndpointEmptiesListAndResetsIds`

Prueft:

- `/clear` leert die Liste
- Die ID-Vergabe startet danach wieder bei `1`

## Technische Anpassungen fuer korrekte Funktion

Folgende Punkte wurden im Backend nachgebessert:

- Leere Aufgabenbeschreibungen werden validiert und abgelehnt
- Doppelte Aufgabenbeschreibungen werden erkannt und abgelehnt
- Fehlerfaelle liefern strukturierte JSON-Fehlermeldungen
- `clearAll()` setzt den internen ID-Zaehler zurueck
- Prioritaeten werden normalisiert, Standardwert ist `MEDIUM`

## Ausfuehrung der Tests

Normaler Testlauf:

```powershell
cd backend
.\mvnw.cmd test
```

## Stand der lokalen Verifikation

Die Testfaelle sind implementiert und an die fachlichen Anforderungen angepasst.

In der aktuellen lokalen Umgebung konnte der Maven-Testlauf noch nicht gestartet werden, weil kein `JAVA_HOME` gesetzt ist und kein Java-Pfad verfuegbar war. Sobald auf dem Rechner ein JDK eingerichtet ist, kann der Testlauf mit dem obigen Befehl direkt in der IDE oder im Terminal gestartet werden.
