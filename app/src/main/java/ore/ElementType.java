package ore;

// ------------- Inner classes -------------

/**
 * Team 06
 * Pavit Vathna and Daniel Duong
 */

public enum ElementType {
    OUTSIDE("OS"), EMPTY("ET"), BORDER("BD"),
    PUSHER("P"), BULLDOZER("B"), EXCAVATOR("E"), ORE("O"),
    ROCK("R"), CLAY("C"), TARGET("T");
    private String shortType;

    ElementType(String shortType) {
        this.shortType = shortType;
    }

    public String getShortType() {
        return shortType;
    }

    public static ElementType getElementByShortType(String shortType) {
        ElementType[] types = ElementType.values();
        for (ElementType type : types) {
            if (type.getShortType().equals(shortType)) {
                return type;
            }
        }

        return ElementType.EMPTY;
    }
}
