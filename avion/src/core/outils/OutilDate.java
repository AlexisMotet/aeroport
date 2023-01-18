package core.outils;

import core.elements.Aeroport;
import enstabretagne.base.time.LogicalDateTime;
import enstabretagne.base.time.LogicalDuration;

public class OutilDate {
    public static boolean checkSiJour(LogicalDateTime date)
    {
        LogicalDateTime dateComparaison = date.getCopy().truncateToDays();
        return date.soustract(dateComparaison).compareTo(Aeroport.limiteMatin) >= 0 &&
                date.soustract(dateComparaison).compareTo(Aeroport.limiteSoir) <= 0;
    }

    public static LogicalDateTime obtenirProchainMatin(LogicalDateTime date)
    {
        return date.truncateToDays().add(LogicalDuration.ofDay(1)).add(Aeroport.limiteMatin);
    }
}
