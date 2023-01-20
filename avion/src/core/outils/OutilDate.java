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

    public static Affluence obtenirAffluence(LogicalDateTime date)
    {
        LogicalDateTime dateComparaison = date.getCopy().truncateToDays();
        boolean res = date.soustract(dateComparaison).compareTo(Aeroport.debutHeureDePointeMatin) >= 0 &&
                date.soustract(dateComparaison).compareTo(Aeroport.finHeureDePointeMatin) <= 0;
        res |= date.soustract(dateComparaison).compareTo(Aeroport.debutHeureDePointeSoir) >= 0 &&
                date.soustract(dateComparaison).compareTo(Aeroport.finHeureDePointeSoir) <= 0;
        if (res) return Affluence.HEURE_DE_POINTE;
        else if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
            return Affluence.WEEK_END;
        return Affluence.NORMALE;
    }

    public static LogicalDateTime obtenirProchainMatin(LogicalDateTime date)
    {
        return date.truncateToDays().add(LogicalDuration.ofDay(1)).add(Aeroport.limiteMatin);
    }
}
