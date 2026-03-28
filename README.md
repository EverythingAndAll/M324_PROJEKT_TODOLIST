# Kurzanleitung für die Installation der Entwicklungsumgebung zum Basisprojekt im Modul 324

## TLDR

ToDo-Liste mit React (frontend) und Spring (backend). Weitere Details sind in den
Kommentaren vor allem in App.js zu finden.

**Liebe Lernende, bitte FORKT dieses Repo für M324, und macht die Pull-Requests in euren FORKS.**

## Relevante Dateien in den Teil-Projekten (Verzeichnisse):

1. diese Beschreibung
2. frontend (Tools: npm und VSCode)
	* App.js

3. backend (Eclipse oder VS-Code)
	* DemoApplication.java
	* Task.java
	* pom.xml (JAR configuration, mit div. Plugins s.u.)

## Inbetriebnahme

1. forken oder clonen
1. *backend* in Eclipse importieren und mit Maven starten, oder in VS-Code via Java Extension Pack. Ohne Persistenz - nach dem Serverneustart sind die Todos futsch. Läuft auf default port 8080.
2. Im Terminal im *frontend* Verzeichnis
	1. mit `npm install` benötige Module laden
	2. mit `npm run dev` den Frontend-Server starten

## Benutzung

1. http://localhost:5173 zeigt das Frontend an. Hier kann man Tasks eingeben, die sofort darunter in der Liste mit einem *Done*-Button angezeigt werden.
2. Klickt man auf den *Done*-Button eines Tasks wird dieser aus der Liste entfernt (und natürlich auch von Backend-Server).
3. Die Task Beschreibungen müssen eindeutig (bzw. einmalig) sein.

### Anstehende Aufgaben

- Erweiterung der Funktionalität durch die Lernenden
- Alternatives Backend für eine VM (WAR Konfiguration)
- Test Umbegung mit Unit-Tests erweitern
(Ausgaben für white-box debugging sind bereits auf den beiden Server vorhanden)

## CI/CD Pipeline (GitHub Actions)

Dieses Projekt verwendet eine automatisierte Build-Pipeline mittels GitHub Actions, um die Qualität und Build-Fähigkeit der Software zu gewährleisten.

### Wie die Pipeline funktioniert:
Die Pipeline wird **automatisch bei jedem Pull Request (bzw. Merge Request) getriggert**, der gegen den `main` Branch gerichtet ist. Die Konfiguration dazu befindet sich unter `.github/workflows/build.yml`.

Sie besteht aus zwei getrennten und parallel laufenden Jobs:
1. **Frontend Build (`build-frontend`)**: 
   Verwendet ein Image mit vorinstalliertem Node (`node:20`). Die Pipeline wechselt in das `frontend/` Verzeichnis, installiert die Abhängigkeiten mittels `npm install` und führt den Build-Befehl `npm run build` aus. Dabei werden die fertigen HTML, JS und CSS Dateien des React-Projekts generiert.
2. **Backend Build (`build-backend`)**: 
   Verwendet ein Image mit vorinstalliertem Maven (`maven:3.9.6-eclipse-temurin-17`). Die Pipeline wechselt in das `backend/` Verzeichnis und baut die Java-Applikation mittels `mvn clean package`. Dadurch wird sichergestellt, dass das Backend korrekt kompiliert wird und das Deploy-Artefakt erfolgreich erstellt werden kann.

Diese Pipeline garantiert, dass fehlerhafter Code, der sich nicht bauen lässt, frühzeitig im Pull Request erkannt wird.
