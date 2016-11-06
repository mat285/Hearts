package card;

public enum Suit {
    CLUBS,
    DIAMONDS,
    SPADES,
    HEARTS;

    public static Suit Parse(String str) {
        switch (str) {
            case "CLUBS":
                return CLUBS;
            case "DIAMONDS":
                return DIAMONDS;
            case "SPADES":
                return SPADES;
            case "HEARTS":
                return HEARTS;
            default:
                return null;
        }
    }
}
