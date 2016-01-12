### Design document


#### Minimum viable product

Mimimum
- De gebruiker moet maanden en daarin werkdagen kunnen toevoegen.
- De gebruiker moet in werkdagen zijn werktijden kunnen invoeren.
- De gebruiker moet contracturen, salaris en wijken kunnen toevoegen.
- De app moet uit kunnen rekenen hoeveel gebruiker heeft gewerkt.
- De app moet uit kunnen rekenen hoeveel gebruiker te veel of te weinig heeft gewerkt.
- De app moet uit kunnen rekenen hoeveel salaris de gebruiker hoort te ontvangen.
- De gegevens dienen opgeslagen, inzichtelijk en aanpasbaar te zijn bij later gebruik.

Optioneel
- De invoer van een loop kan op de walks-activity plaatsvinden in plaats van een eigen 
  activity, waarbij editTexts en buttons dynamisch aangemaakt worden.
- De listview kan veranderd worden naar een blok-stijl zoals bij Google Keep, waarbij 
  je items kan verwijderen door te swipen.
  

#### Classes en methods

Months.java
- onCreate
- onClickNewMonth
- onClickSettings

Days.java
- onCreate
- onClickNewDay
- onClickSettings

Walks.java
- onCreate
- onClickNewWalk
- onClickSettings

AddWalk.java
- onCreate
- onClickSave
- onClickCancel
- onClickSettings
- onEditTextChangedListener
Data van contracturen en salaris worden opgehaald van shared preferences.

Settings.java
- onCreate

ContractSalary.java
- onCreate
- onClickSave
- onClickCancel

Districts.java
- onCreate
- onClickNewDistrict

AddDistrict.java
- onCreate
- onClickSave
- onClickCancel

Model classes
- Month.java - Month, Amount of days, Total salary, Total time
- Day.java - Day, Districts, Total time, Goal time, Extra time
- Walk.java - District code, Begin time, End time, Goal time, Extra time, Total time
- District.java - District code, Busy day time, Calm day time


#### Schetsen UI

![sketchesUI](/doc/sketchesUI.jpg)


#### API's en frameworks

Om de database te implementeren zal er gebruikt gemaakt worden van SQLite.


#### Database tabellen

Months
- ID (int)
- Name (String)
- Days (int)
- Salary (float)
- Time (time)

Days
- ID1 (int)
- ID2 (int)
- Day (String)
- Districts (String)
- TimeTotal (time)
- Goal (time)
- Extra (time)

Walks
- ID1 (int)
- ID2 (int)
- ID3 (int)
- District (String)
- TimeBegin (time)
- TimeEnd (time)
- Goal (time)
- Extra (time)
- TimeTotal (time)