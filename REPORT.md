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
gemaakt zijn door middel van de zelfgemaakte modelclasses. Deze modelclasses zijn:
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
	

