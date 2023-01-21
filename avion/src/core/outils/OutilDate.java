package core.outils;

import core.elements.Aeroport;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;

import java.time.DayOfWeek;

public class OutilDate {
    public static boolean checkSiJour(LogicalDateTime date)
    {
        LogicalDateTime dateComparaison = date.getCopy().truncateToDays();
        return date.soustract(dateComparaison).compareTo(Aeroport.limiteMatin) >= 0 &&
                date.soustract(dateComparaison).compareTo(Aeroport.limiteSoir) <= 0;
    }

    public static boolean checkSiWeekEnd(LogicalDateTime date){
        return(date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                date.getDayOfWeek() == DayOfWeek.SUNDAY);
    }

    public static long obtenirHeure(LogicalDateTime date)
    {
        LogicalDateTime dateComparaison = date.getCopy().truncateToDays();
        return date.soustract(dateComparaison).getTotalOfHours();
    }

    public static Affluence obtenirAffluence(LogicalDateTime date)
    {
        if (!checkSiJour(date)) return Affluence.NUIT;
        else if (checkSiWeekEnd(date))
            return Affluence.WEEK_END;
        LogicalDateTime dateComparaison = date.getCopy().truncateToDays();
        boolean res = date.soustract(dateComparaison).compareTo(Aeroport.debutHeureDePointeMatin) >= 0 &&
                date.soustract(dateComparaison).compareTo(Aeroport.finHeureDePointeMatin) <= 0;
        res |= date.soustract(dateComparaison).compareTo(Aeroport.debutHeureDePointeSoir) >= 0 &&
                date.soustract(dateComparaison).compareTo(Aeroport.finHeureDePointeSoir) <= 0;
        if (res) return Affluence.HEURE_DE_POINTE;
        return Affluence.NORMALE;
    }

    public static LogicalDateTime obtenirProchainMatin(LogicalDateTime date)
    {
        return date.truncateToDays().add(LogicalDuration.ofDay(1)).add(Aeroport.limiteMatin);
    }
}
