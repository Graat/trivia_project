### Trivia Time ###

Een eenvoudige full-stack quiz-applicatie bestaande uit een Java Spring Boot backend en een React frontend.

# De Stack
## Backend
- Java 21
- Spring Boot
- Maven
- JUnit 5 + Mockito (unit testing)
- Jackson (JSON parsing)

## Frontend
- React
- Vite
- Node.js
- DOMPurify

---

# Applicatie starten

Eerst moet de backend draaien voordat je de frontend runt.

## Backend
1. Open het project in een IDE (zoals IntelliJ).
2. Zorg dat Java 21 en Maven zijn geïnstalleerd.
3. Start de backend in command prompt:
   cd [path/to/backend-folder]
   mvn spring-boot:run

   Of start direct via de IDE door `TriviaBackendApplication.java` te runnen (Die zit direct in de backend folder).

Side-note: De backend draait standaard op `http://localhost:8080`.

## Frontend
1. Start de frontend in command prompt:
   cd [path/to/frontend-folder]
   npm install
   npm run dev

Side-note: De frontend draait standaard op `http://localhost:5173`.

---

# Tests

Er zijn unit tests geschreven voor de backend services. Andere classes en frontend hebben geen unit tests in verband met de gelimiteerde tijd die ik had.

Om de tests uit te voeren:
1. Start de backend in command prompt:
   cd [path/to/backend-folder]
   mvnw test

---

# Overige opmerkingen

- De DebugController in de backend maakt geen deel uit van de applicatie. Die was enkel gebruikt tijdens het debuggen.
- Ik had plannen om de gebruiker categorieën te laten kiezen, vandaar dat er een Category model en een CategoryService aanwezig zijn. Deze worden momenteel nog niet gebruikt.
- Als voorbereiding op die category selectie functionaliteit haal ik telkens 4 vragen op van de Open Trivia API en gebruik ik ze één voor één. Als ze op zijn, worden er nieuwe vragen opgevraagd. Dit lijkt nu misschien over-engineered, maar had dus een reden.
- In het zeldzame geval dat een gebruiker (bijna) alle vragen correct beantwoordt, kan er een exception optreden omdat er geen unieke vragen meer beschikbaar zijn. Ik had hiervoor een oplossing gepland door alle vragen lokaal te cachen. Vraag me er gerust naar als je daar meer over wilt weten.