package core;

public class TourDeControleParfaite {
    public enum enumMessage {
        OK,
        NOK,
        ARRIVEE,
        FIN_DE_VOL,
        DEPART,
        DECOLLAGE
    }

    Boolean TW1occupee = false;
    Boolean TW2occupee = false;
    public TourDeControleParfaite() {
    }
    public enumMessage communication(enumMessage msg)
    {
        switch (msg)
        {
            case ARRIVEE:
                if (TW1occupee)
                {
                    return enumMessage.NOK;
                }
                else
                {
                    TW1occupee = true;
                    return enumMessage.OK;
                }
            case FIN_DE_VOL:
                TW1occupee = false;
                return enumMessage.OK;
            case DEPART:
                if (TW2occupee)
                {
                    return enumMessage.NOK;
                }
                else
                {
                    TW2occupee = true;
                    return enumMessage.OK;
                }
            case DECOLLAGE:
                TW2occupee = false;
                return enumMessage.OK;
            default :
                return enumMessage.NOK;

        }
    }
}
