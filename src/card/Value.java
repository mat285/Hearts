package card;

public enum Value {
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING,
    ACE;

    public static Value Parse(String str) {
        switch (str.toUpperCase()) {
            case "TWO":
                return TWO;
            case "THREE":
                return THREE;
            case "FOUR":
                return FOUR;
            case "FIVE":
                return FIVE;
            case "SIX":
                return SIX;
            case "SEVEN":
                return SEVEN;
            case "EIGHT":
                return EIGHT;
            case "NINE":
                return NINE;
            case "TEN":
                return TEN;
            case "JACK":
                return JACK;
            case "QUEEN":
                return QUEEN;
            case "KING":
                return KING;
            case "ACE":
                return ACE;
            default:
                return null;
        }
    }
}
