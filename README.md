# i200projekt - ISBN generaator
###Liina Abner DK14

ISBN (International Standard Book Number) on rahvusvahelise ISBN agentuuri poolt hallatav standard.<br>
Number on 13-kohaline ja koosneb viiest osast: prefix-rühmatunnus-kirjastustunnus-raamatutunnus-kontrollnumber.<br>
ISBN vahemikud iga riigi jaoks saab siit https://www.isbn-international.org/export_rangemessage.xml<br>
Riigile kuuluvaid kirjastustunnuseid (kirjastajaplokke) jagab kirjastajatele iga riigi vastav agentuur.<br>
978-9985 - prefix ja Eesti rühmatunnus (riigitunnus)<br>
Eestile eraldatud kirjastustunnused (kirjastajaplokid) on:<br>
 0-4<br>
 50-79<br>
 800-899<br>
 9000-9999<br>
 
Vastavalt kirjastustunnuse kohtade arvule jääb raamatutunnuse jaoks 1-4 kohta. Seega mahub iga kirjastustunnuse sisse 10, 100,1000 või 10000 raamatutunnust. Näiteks, kui kirjastustunnus on 9000, siis jääb raamatutunnuse jaoks 1 koht, mille väärtus saab olla 0-9, kokku 10 võimalikku arvu. Kui kirjastustunnus on 50, on raamatutunnus 3-kohaline, igal kohal võib olla arv 0-9 - seega 10 astmel 3 ehk 1000
 
Viimane ehk kontrollnumber arvutatakse algoritmi järgi, mis on leitav siit:<br> https://www.isbn-international.org/sites/default/files/Manual_Estonian_2013.pdf Lk. 27
 

Kasutajaks on ISBN agentuuri töötaja
- Saab luua uue kirjastaja või valida olemasolevate seast.
- Saab kirjastajaga siduda sobiva mahuga ISBN kirjastajaploki.
- Peab järge vabade kirjastajaplokkide üle.
- Saab raamatule isbn-i määrata.
- Peab järge vabade raamatutunnuste üle kirjastajaploki sees, kui vabu raamatutunnuseid ei ole, sunnib valima uut kirjastajaplokki

Objektid:
- Andmebaas - meetodid ab päringute tegemiseks
- Isbn - meetodid korrektse isbn kokkupanemiseks, kontrollnumbri arvutamine
- Kirjastaja
- Plokk - meetod vabade raamatutunnuste arvutamiseks
- Raamat

Andmebaas:
- Apache Derby

Tabelid
- plokivahemikud
- kirjastaja
- plokk
- raamat

Edasised arendused:
- Sisestatavate andmete kontroll - kas kõik väljad on täidetud, ega pole sisestatud rohkem sümboleid kui ab väli lubab jne.
- Triipkoodi genereeerimine.
- GUI ilustamine
