package core.elements;

import core.TourDeControleParfaite;
import core.outils.ParserConfig;
import enstabretagne.base.math.MoreRandom;
import enstabretagne.base.time.LogicalDuration;

import java.util.ArrayList;
import java.util.HashMap;

public class Aeroport {
    private static MoreRandom random;

    public static MoreRandom getRandom() {
        return random;
    }

    public static LogicalDuration limiteSoir = LogicalDuration.ofHours(22);
    public static LogicalDuration limiteMatin = LogicalDuration.ofHours(7);
    public static LogicalDuration debutHeureDePointeMatin = LogicalDuration.ofHours(7);
    public static LogicalDuration finHeureDePointeMatin = LogicalDuration.ofHours(10);
    public static LogicalDuration debutHeureDePointeSoir = LogicalDuration.ofHours(17);
    public static LogicalDuration finHeureDePointeSoir = LogicalDuration.ofHours(19);
    private final ArrayList<Piste> pistes = new ArrayList<>();
    private final HashMap<Integer, Piste> mapPistes = new HashMap<>();

    public HashMap<Integer, Piste> getMapPistes() {
        return mapPistes;
    }
    final private TourDeControleParfaite tourDeControle;

    public Aeroport(String cheminConfig) {
        try {
            tourDeControle = new TourDeControleParfaite(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int seed = ParserConfig.parse(cheminConfig, this);
        random = new MoreRandom(seed);
    }

    public Piste ajouterPiste() throws Exception {
        if (pistes.size() == 3)
        {
            throw new Exception("Le nombre de pistes est limite a 3");
        }
        Piste piste = new Piste();
        pistes.add(piste);
        mapPistes.put(piste.hashCode(), piste);
        return piste;
    }

    public ArrayList<Piste> getPistes() {
        return pistes;
    }


    public TourDeControleParfaite getTourDeControle() {
        return tourDeControle;
    }
}
