# Aufbau des Vortrags / Roter Faden

## Problem heraus arbeiten


### Szenario A: Events über die Zeit (UI, Updates from Server, Monitoring, ...)

* Listener notwendig, wir wollen aktiv benachtichtigt werden, wenn etwas passiert
--> Klassischer Anwendungsfall für Listener-Interface

#### Observable-Pattern (heute "Listener-Pattern") löst das grundsätzlich
Aber: Ist technisch äußerst komplex

##### Problemfelder: 
* Threading, Threading, Threading
* Exceptions
* nur sehr schwer "korrekt" zu implementieren


***--> Erkenntnis 1: Threading ist schwer!***
***--> Erkenntnis 2: Wie Exceptions handeln?***


### Szenario B: Asynchronität / Callbacks

* Synchrone Calls skalieren/funktionieren nicht
Parallelität ist notwendig

* Callbacks / Listeners sind notwendig
* Aber: Skaliert nicht --> Verschachtelung immer tiefer

***--> Erkenntnis 3: Callbacks skalieren nicht***


### Szenario B: Pageination (Nachladen von Ergebnissen, z.B. Shop, endless Scrolling)

* Wie mitteilen, dass wir das nächste Ergebnis brauchen? 

***--> Erkenntnis 4: Manchmal ist ein "Rückkanal" notwendig***


## Lösung für alles: Reactive Programming

Das alles und noch viel mehr, löst Reactive Programming.

Reactive Programming ist eine stable und mächtige *Implementierung* des Listener-Patterns.
Außerdem gibt es noch weitere Features (Operatoren), welche die Benutzung deutlich vereinfachen! 



## Ablauf / Informationen

### Betrachten aus zwei Sichten

* Beobachter / Observer / (im Java-Land: Listener)
* Subject / Observable
