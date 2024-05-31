

## Startup 
all' interno della cartella del progetto eseguire il comando  

mvn spring-boot:run

questo farà partire l'esecuzione del progetto.


Per accedere alla ui di swagger il link è il seguente

http://localhost:8080/swagger-ui/index.html#/

é configurata un'atenticazione basic di default le credenziali sono  

user : password 


## build Docker

mvn package

cd movie
docker build  -t temera/movie .  

docker run -p 8080:8080 temera/movie


# TESTO ESERCIZIO

## Esercizio backend
### Implementazione di un'API di Gestione Film
#### Descrizione
Realizzare un'API REST per gestire film e attori. Ad alto livello, il servizio dovrebbe consentire la visualizzazione, la creazione, la modifica e
la cancellazione di tali entità e delle relazioni tra di loro. Inoltre dovrebbe essere presente un endpoint per visualizzare i 3 film col punteggio
più alto in un dato anno.
#### Requisiti Funzionali
Il servizio dovrebbe permettere le normali operazioni CRUD su due tipologie di entità:
Movie (id, name, year, rating)
Actor (id, name)
Oltre ai campi indicati, è necessario modellare il vincolo N-N tra Movie e Actor (ossia: un Movie può avere N Actor, e un Actor può aver
recitato in N Movie). Mi aspetto quindi che quando recupero le informazioni su un Movie avrò anche la lista di Actor che vi hanno recitato, e
viceversa quando recupero un Actor avrò la lista di Movie in cui questi vi ha recitato. Quando creo o modifico un Movie posso specificare la
lista di Actor che vi hanno recitato.
Inoltre dovrebbe essere presente un endpoint che, dato l’anno, restituisce i 3 film con rating più alto di quell’anno (ordinati in ordine
decrescente).
#### Requisiti Tecnici

Utilizzare Java o Kotlin (indicativamente JDK 11 in poi).
Come framework di backend puoi utilizzare Spring Boot o un altro framework a tua scelta.
Usa pure qualunque altra libreria che ritieni ti possa essere utile.
Utilizzare Maven o Gradle come build tool.
Utilizzare un database locale standalone (H2 o SQLite).
Il progetto dovrebbe avere un’adeguata copertura di test (unit/integration)

### Bonus points
#### Se ti avanza tempo, potresti valutare di aggiungere i seguenti elementi

Validazioni sulla creazione e modifica delle entità (ad esempio il rating dovrebbe essere un valore tra 0 e 10)
Readme con una guida passo passo al run del progetto
Dockerfile
Breve documentazione ai servizi (anche generata con openapi/swagger) e/o javadoc nel codice
Autenticazione/autorizzazione (molto basilare)