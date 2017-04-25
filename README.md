# Dashboard 

Dashboard er en app laget for å vise siste data fra tjeneren din. Den viser statistikk over alle besøk til web-området den er installert på. Gjennom datamodellen kan vi se hvem som har besøkt siden, og hvor mange ganger. Vi lagerer også annen informasjon som geografisk plassering og dato per besøk, samt hvilken side besøket var til. Dataene som blir lagret skal utvides videre, og da blir denne appen mer og mer nyttig.



![alt text](http://byteme.no/image/Dashboard-all.png)



### Appens “sider”: 

###### Dashboard: 
Her har man kjapt oversikt over noen statistikk for traffikken samt en graf som viser trafikk dette året. 

###### Besøksliste: 
Dette er en liste over alle besøkende. Denne er sortert på dato på telefonen slik at jeg kan spare kapasiteten på tjeneren (den er svak). 

###### Informasjon:
Informasjonssiden er en detaljert visning av en gitt besøkende. Den inneholder kart av hvor besøkende holder til, samt et av disse fragmentene:

- Detaljert informasjon: Her finner man detaljer om en gitt besøkende.

- Endre informasjon:her kan man endre enkel informasjon om besøkende. Man kan også gjøre oppslag for å få komplette informasjon fra nettet. 

###### Innstillinger: 
Her kan man velge om man vil bruke lokal eller live tilkobling. Denne siden kan utvides videre.


Alle disse sidene er til for å vise informasjon om tjeneren fort. Til fremtiden skal det også implementeres støtte for sesjoner, slik at man kan se hvor lenge hvert besøk varer. Jeg vil også legge inn støtte for engelsk språk.

### Design 
Jeg har hovedsakelig forsøkt å bruke “Material Design” gjennom hele appen. Appens design er basert på noen skisser jeg lagde tidligere i semesteret (milepæl 1). [Se Design spesifikasjon her.](http://byteme.no/document/designspesifikasjon.pdf "Design spesifikasjon fra før utførelse av prosjektet") Alle sidene ble veldig like denne siden, utenom “dashboard” der grafen ble til en linjegraf istedenfor paigraf. 

Jeg har valgt å kun tillate portrett modus på alle sidene grunnet dette er den eneste funksjonelle måten å bruke appen på. Man kan bruke landskapsmodus på Dashboard siden, den vil da vise en større og mer detaljert versjon av grafen. Landskapsmodus er kun lagt til her fordi det la til funksjonalitet til appen. 

Grafen heter MPChart, og er laget av GitHub brukeren “PhilJay”. (https://github.com/PhilJay/MPAndroidChart).     
