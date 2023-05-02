package pl.agh.edu.generator;

public class ClientGenerator {
    /*
    WIZJA
    mamy plik, który mowi jaki procent klientow jest danego typu np: turysci: 80%, biznesmeni 15%, kuracjusze 5%
    każdy rodzaj klienta ma preferowaną rangę, cenę, dlugosc pobytu i ilosc osob w pokoju np: biznesmen: ranga 4, ilosc osob: 1 cena x, turysta: ranga 2, ilosc osob 5: cena y
    preferowana cena,ranga,ilosc osob,ilosc nocy bedzie losowana z rozkladow normalnych ( zazwyczaj biznesmen bedzie chciał range 4, rzadzij 3, bardzo zadko 2

    pomysł: typ klienta bedzie też miał szanse zespawnowania w danym dniu na podstawie rozkładu normalnego, np: biznesmen zazwczycaj sie pojawi na pocz tygodnia, turysci w piątek, kuracjusze roznie

    w zaleznosci od popularnosci hotelu, clientGenerator bedzie w wypluwał wiecej lub mniej klientow
    clientGenerator bedzie wypluwał wszystkich mozliwych klintow a nie tylko tych co mozemy przyjąć: klient chce pokoj 5 osobowy a my mamy tylko 3 osobowe itd
    clientGenerator bedzie wypluwał o północy wszystkich klientow na dany dzien razem z godziną przybycia

    pomysł: klienci będą się ustawiać w wirtualnej kolejce i będa mieli atrybut mowiący jak długo mogą czekać (jak klienci beda wolno obslugiwani to trzeba zatrudnic dddatkową recepcjoniste, recepcjonistka moze miec atrybut jak  szybko obsluguje klintow)
    mozemy ustalic ze recepcja jest czynna od 8 do 24 wiec potrzeba zatrudnic przynajmniej na dwie zmiany!

    pomysł: na podstawie standardu miejsca (pokoj i recepcja) klient wystawia ilosc gwiazdek, ktora wpływa na popularność (razem z reklamą)
    pomysł: w wakacje i ferie bedzie bonus do turystow
     */
}
