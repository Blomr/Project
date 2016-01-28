# Report

### Beschrijving

De app die ik heb gemaakt is bedoeld voor mijn werk als postbezorger.
Als postbezorger moet ik mijn werktijden bijhouden en in de online-omgeving van
PostNL invoeren wat ik te veel of te weinig heb gewerkt. Omdat ik dit in Google 
Keep bijhield, moest ik zelf de extra tijd berekenen. Dit kostte wat rekenwerk.

Om al mijn werktijden overzichtelijk te houden, makkelijk de extra gewerkte tijd
te weten te komen en inzicht te krijgen over het te ontvangen salaris, heb ik een
handige app gemaakt.

De app start in een maanden-scherm, vanuit daar kan je nieuw maanden toevoegen.
Door te klikken op een maand kom je in het dagen-scherm terecht. Hier zie je alle
dagen die je hebt toegevoegd in deze maand. Vanuit hier kan je ook nieuwe dagen 
toevoegen. Als je op een dag klikt, kom je in het lopen-scherm terecht. Hier zie
je alle wijken die je hebt gelopen en toegevoegd die dag. Ook vanuit hier kan je 
nieuwe lopen toevoegen. In het loop-toevoegen-scherm kan je de wijkcode, of het
een piek- of daldag is en de begin- en eindtijden invullen. Door op opslaan te 
klikken wordt de loop opgeslagen en veranderen de gegevens in de dag en maand.
Ook kan je vanuit alle schermen bij in de instellingen komen. De instellingen
bestaan uit twee onderdelen: 'contracturen en salaris' en 'wijken'. In de eerste
kan je je contracturen en bijbehorende salarissen per uur opgeven. Deze zijn nodig 
om voor iedere maand het totale salaris uit te kunnen rekenen. In de tweede kan je
wijken toevoegen met bijbehorende piekdag- en daldagtijden. Deze zijn dan te
selecteren in het scherm waar je een loop toevoegd.

### Technisch ontwerp

Ik maak gebruik van de volgende activities:
- MonthsActivity.java
- DaysActivity.java
- WalksActivity.java
- AddWalkActivity.java
- SettingsActivity.java
- ContractAndSalaryActivity.java
- DistrictsActivity.java
- AddDistrictActivity.java

Deze activities zijn subclasses van AppCompatActivity en komen overeen met de schermen
die ik beschreef in het vorige hoofdstuk.

Alle activities, op AddWalk-, ContractAndSalary en AddDistrictActivity na, maken gebruik
van listviews. Voor deze listviews heb ik zelf een listitem-layout gemaakt. Hierbij zit
in elke hoek een textview, in de rechterbovenhoek drie onder elkaar en boven in het midden,
maar deze is altijd onzichtbaar.

Om de listviews van content te voorzien heb ik voor iedere verschillende listview een
een adapter geschreven. De adapters heten als volgt:
- MonthAdapter.java
- DayAdapter.java
- WalkAdapter.java
- DistrictAdapter.java

Zoals je ziet, heeft settings geen eigen adapter, dat komt omdat deze gebruik maakt van
de standaard arrayadapter. De andere adapters plaatsen de content vanuit objecten die 
gemaakt zijn door middel van de zelfgemaakte modelclasses. Hieronder volgen de modelclasses
met het hun variabelen en hun locaties in de list item:
- Month.java 
	- id (int) - bovenmidden
	- month (String) - linksboven
	- days (int) - linksonder
	- salary (double) - rechtsboven
	- time (String) - rechtsonder
	
- Day.java 
	- id1 (int)
	- id2 (int) - bovenmidden
	- day (String) - linksboven
	- districts (String) - linksonder
	- timeTotal (String) - rechtsboven
	- timeGoal (String) - rechtsonder
	- timeExtra (String) - rechtsonder
	
- Walk.java 
	- id1 (int) 
	- id2 (int) 
	- id3 (int) - bovenmidden
	- districtCode (String) - linksboven
	- dayType (String) 
	- timeBegin1 (String) - rechtsboven
	- timeEnd1 (String) - rechtsboven
	- timeBegin2 (String) - rechtsboven
	- timeEnd2 (String) - rechtsboven
	- timeBegin3 (String) - rechtsboven
	- timeEnd3 (String) - rechtsboven
	- timeGoal (String) - linksonder
	- timeExtra (String) - linksonder
	- timeTotal (String)- linksonder
	
- District.java 
	- id (int) - bovenmidden
	- districtCode (String) - linksboven
	- timeGoalBusy (String) - rechtsboven
	- timeGoalCalm (String) - rechtsonder
	
Het opslaan, ophalen, aanpassen en verwijderen van deze data gaat met behulp van een database: SQLite.
Ik heb een speciale class, databaseHandler, gemaakt die specifieke acties uitvoert in de database. De 
activities kunnen deze class aanroepen. De database heeft vier tabellen: Months, Days, Walks en
Districts. De kolommen in deze tabellen komen overeen met de variabelen in de modelclasses. Bij het
ophalen en wegschrijven van informatie wordt dan ook grotendeels gebruik gemaakt van deze modelclasses.
De databaseHandler heeft de volgende methods:
- addMonth
- addDay
- addWalk
- addDistrict
- getWalk
- getDistrict
- getWalksOfDay
- getDaysOfMonth
- getMonths
- getDistricts
- getMonthName
- getDayName
- deleteMonth
- deleteDay
- deleteWalk
- editMonthName
- editDayName
- editWalk
- editDistrict

Ik denk dat de namen van deze methods duidelijk de functie uitleggen. Naast deze methods heb ik ook drie
private methods geschreven. De eerste is salaryCalculator, deze berekent het bijbehorende salaris van de
tijd die in een maand is gewerkt. De twee is msToTimeStrConverter, deze zet een long-waarde in 
milliseconden om in een String met een tijdnotatie van HH:mm. De derde is strTotDateParser, deze zet een
tijd-string van HH:mm om in een Date-object. Door deze private methods werden het aantal regels code een
stuk minder.

### Uitdagingen

Voor het project had ik nog niet met bepaalde dingen gewerkt die ik nodig had om mijn app te werken. De
belangrijkste hiervan is het werken met een database. SQL was niet helemaal nieuw voor me, dus het duurde
niet heel lang om de logica voor het maken van een database te begrijpen.

Daarnaast had ik nog nooit eerder een custom adapter gemaakt. Dit vond ik aan het begin nog vrij lastig,
omdat overal op internet het anders werd uitgelegd. Een andere student wees op een bepaalde bron en sindsdien
werd ook het maken van een adapter helemaal logisch. 

Voor het maken van een keuze tussen een aantal opties heb ik gebruik gemaakt van de spinner, die ik nog nooit
eerder had gebruikt. Ik was in eerste instantie van plan om een dialoog met radio buttons te maken, maar daar
ben ik toch op terug gekomen, omdat ik radio buttons wat ouderwets vond en het gebruik van een spinner beter
vond werken.

Ook heb ik voor het eerst gewerkt met tijd. Ik ben hier best lang mee bezig geweest. Vooral aan het begin
kwamen er hele rare tijden op het scherm tevoorschijn. Een grote fout ontdekte ik al snel: met het rekenen
ging ik er van uit dat de tijd 00:00 gelijk stond aan 0 milliseconden. Dit was niet het geval, dat was namelijk
01:00. 00:00 stond gelijk aan -3.600.000. Daarom heb ik in veel van mijn tijdsberekeningen overal 3.600.000 bij
moeten optellen om het resultaat kloppend te krijgen. Bij het toevoegen van de loop heb ik nog wel één aanpassing
gedaan: in plaats van het opslaan van één begintijd en één eindtijd, sla ik er nu drie op. Dit komt goed van pas,
omdat postbezorgers tijdens hun loop nog wel eens een pauze kunnen nemen.

Ik heb nog een kleine aanpassing gedaan in het ontwerp. De toevoeg-knop stond op de ontwerptekeningen in de
rechter onderhoek. Bij het langer worden van de listview zou deze knop echter een aantal gegevens bedekken.
Dit is niet de bedoeling en daarom heb ik de knop naar het midden verplaatst.
