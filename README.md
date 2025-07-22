1. Contesto di riferimento e obiettivi

Contesto
Si vuole realizzare un sistema informativo su web che offre informazioni relativi alle console e ai videogames. Oltre agli utenti occasionale, due tipologie di attori interagiscono con il sistema: gli utenti registrati e l'amministratore. Un utente registrato, oltre a poter consultare le informazioni, possono aggiungere recensioni alle console (ogni utente registrato può aggiungere al più una recensione, ogni console può avere più recensioni, al più una per ogni utente).

L'amministratore può svolgere le seguenti operazioni: possono aggiungere, modificare console e cancellare videogames che appartengono ad una console. Gli amministratori possono anche cancellare le recensioni (ma non possono modificarle).

Per ogni console sono di interesse il nome, l'anno, una immagine, un gioco esclusivo e uno o più giochi (ipotizziamo ogni console possa avere anche più di un gioco). Per i giochi sono di interesse il nome e l'immagine.
Alle console possono essere associate recensioni scritte da utenti registrati. Una recensione ha un titolo, un rating (un intero compresso tra 1 e 5) e un commento

2. Obiettivi

L'obiettivo è creare un sistema informativo su Web che contempli i seguenti casi d'uso che seguono:

3. Casi d'uso

Caso d'uso UC1: Consulta informazioni
Attore primario: un utente occasionale, registrato e un'amministratore
Un qualunque utente che accede alla pagina web può consultare tutte le informazioni sulle console e sui giochi.

Scenario principale di successo:
Il sistema mostra i dettagli della richiesta visualizzando la pagina. L'utente ripete i passi precedente un numero indefinito di volte.

Caso d'uso UC2: Inserimento una nuova console
Attore primario: l'amministratore
Si presuppone che l'utente principale sia quello registrato con appositi permessi di 'amministrazione' , registrato con apposito ruolo su DB in grado di effettuare operazioni di inserimento.

Scenario principale di successo: E' altresì necessario ovviamente loggarsi al sistema con le proprie credenziali che verranno riconosciute dal sistema come utenza amministrazione.
L'amministratore (o amministrazione) crea un libro dall'apposita voce di menu, successivamente imposta un nome, anno e una immagine.

Caso d'uso UC3: Aggiornamento Console
Attore principale: l'amministratore
Si presuppone che l'utente principale sia quello registrato con appositi permessi di 'amministrazione', registrato con apposito ruolo su DB in grado di effettuare operazioni di aggiornamenti.

Scenario principale di successo: E' altresì necessario ovviamente loggarsi al sistema con le proprie credenziali che verranno riconosciute dal sistema come utenza amministrazione. L'amministratore (o amministrazione) aggiorna una console dall'apposita voce di menu, successivamente può aggiornare il titolo, l'anno di uscita e potrebbe anche aggiornare i giochi della console.

Caso d'uso UC4: Cancellamento recensione
Attore principale: l'amministratore
Si presuppone che l'utente principale sia quello registrato con appositi permessi di 'amministrazione', registrato con apposito ruolo su DB in grado di effettuare operazioni di cancellazione.

Scenario principale di successo: E' altresì necessario ovviamente loggarsi al sistema con le proprie credenziali che verranno riconosciute da sistema come utenza amministrazione. L'amministratore(o amministrazione) cancella una recensione, questa influenza anche ai dati che sono contenuti al DB.

Caso d'uso UC5 : Consulta informazione Attore principale: utente registrato 
Si presuppone che l'utente registrato sia quello registrato con appositi permessi di "utente registrato", registrato con apposito ruolo sul DB in grado di consultare le informazioni presenti.

Scenario principale di successo: L'utente registrato, come l'utente occasionale, può consultare le informazioni sulle console e sui videogiochi presenti nel sito web.

*Caso d'uso UC6: Contatti Attore primario: utente occasionale

Scenario principale di successo: L'utente seleziona i contatti del sito web. Il sistema mostra la form e l'utente inserisce il proprio nome, email e richieste. Il sistema inoltra tutto all'indirizzo email del sito.

*Caso d'uso UC7: Chi siamo Attore primario: utente occasionale

Scenario principale di successo: L'utente seleziona le informazioni del sito e il sistema mostra la pagina con le informazioni. 






