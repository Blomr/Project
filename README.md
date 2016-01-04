# PostNL Werktijden & Salaris
  

### Proposal
  

#### Het probleem

Sinds de zomervakantie werk ik als postbezorger bij PostNL. Per dag loop ik meestal twee wijken en voor
deze wijken staan een richttijd aangegeven waarin deze gelopen moeten worden. Meer of minder tijd dien ik
achteraf in een online-omgeving van PostNL aan te geven, waar uiteindelijk het salaris op gebaseerd wordt.
Tegenwoordig houd ik mijn werktijden bij in Google Keep. Dit zijn echter alleen notities. De tijd die ik 
meer of minder heb gewerkt, moet ik zelf uitrekenen. Dit kan makkelijker. Daarnaast heb ik op deze manier
ook geen overzicht welk bedrag aan salaris ik aan het einde van de maand hoor te ontvangen.
  

#### De oplossing

Ik wil een app maken waarin ik kan aangeven welke wijk ik loop en wat mijn begin- en eindtijd daarvan is.
Op basis van de tijd die er voor de wijk staat, geeft de app mij de tijd die ik teveel of te weinig heb
gelopen. In de app kan ik ook mijn contracturen en het uurloon aangeven, van zowel de contracturen als de
overuren. Dit wil ik per werkdag op kunnen slaan in een overzicht. De app kan van die werkdagen bij elkaar 
het maandloon uitrekenen. Zo heb ik alles op een rijtje en hoef ik geen dingen zelf meer uit te rekenen.


#### Schetsen

![sketches](/doc/sketches.jpg)
  

#### Data sets & sources

Voor elke wijk staat een richttijd aangegeven. De hoeveelheid post varieert per dag: er zijn piek- en
daldagen. Per wijk zijn er dus twee verschillende tijden. Ik zou de tijden van alle wijken in de app kunnen
zetten, maar ik heb besloten om dit door de gebruiker zelf te laten doen. Dit komt omdat de tijden na een
bepaalde periode aangepast kunnen worden door het bedrijf. Hierdoor kan de app niet meer goed zijn werk doen.
Daardoor vind ik fijner werken als de gebruiker zelf de instellingen aan kan passen.
  

#### Onderdelen

- Timeline: Dit scherm zie je bij het opstarten van de app. Het laat alle werkmaanden zien die je hebt bijgehouden.
  Per werkmaand staat hoeveel dagen en hoeveel uur je hebt gewerkt. Ook het bijbehorende salaris staat
  erbij. Er is een knop om een nieuwe maand toe te voegen.

- Werkmaand: In dit scherm zie je alle werkdagen die je in de maand hebt bijgehouden. De werkdagen bestaan uit lopen die 
  je erin hebt toegevoegd. Per loop staat de wijk, piek- of daldag, de richttijd, de begin- en eindtijd, de
  tijd die te veel of te weinig is gelopen. Per werkdag staat ook het bijbehorende salaris aangegeven.
  
- Werkdag: Bij dit scherm zie je de lopen die je al eerder hebt toegevoegd en daarnaast kan je ook een loop toevoegen.
  Je geeft zelf aan welke wijk het is, of het een piek- of daldag is en wat de begin- en eindtijd is. De app
  komt zelf met de richttijd en rekent uit wat de tijd is die je te veel of te weinig hebt gelopen.
  
- Instellingen: Via de andere drie schermen kan je bij de instellingen komen. Hier kan je aangeven hoeveel contracturen
  je hebt en wat je salaris is, en wat je krijgt voor overuren. Daarnaast kan je wijken toevoegen. Per
  wijk kan je voor de dal- en piekdag een richttijd instellen.
  	
	
#### Platform & API's

Ik wil voor elk onderdeel een layout in XML maken en bijbehorende programmatuur in java. Voor het opslaan van de data wil 
ik gebruik maken van een database.	
  

#### Potentiële problemen

Ik heb nog nooit eerder in Android Studio met een database gewerkt. Hier zou ik nog eerst zorgvuldig onderzoek naar moeten
doen.
  

#### Vergelijkbare apps

Ik ben in de Play Store veel verschillende apps tegengekomen die met werktijden bijhouden te maken hebben. De hoeveelheid
features lopen daarbij erg uiteen. Er zijn apps die puur draaien om het bijhouden van tijden, eigenlijk zoals ik ook al
eerder deed, maar dan in Google Keep. Andere apps zijn veel uitgebreider en combineren het ook met uurtarieven. Ik ben 
nog geen apps tegengekomen die inforamtie specifiek verwerkt zoals ik dat wil. Het systeem met richttijden en te veel of
te weinig werken is ook iets wat denk ik dat vooral bij mijn werk gebeurd.

