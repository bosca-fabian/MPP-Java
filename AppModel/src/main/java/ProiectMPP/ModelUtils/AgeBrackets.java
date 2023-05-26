package ProiectMPP.ModelUtils;

public enum AgeBrackets {
    SIX_EIGHT("6 - 8", 6, 8),
    NINE_ELEVEN("9 - 11", 9, 11),
    TWELVE_FIFTEEN("12 - 15", 12, 15),
    NO_AGE_RESTRICTION("NO AGE RESTRICTION", 6, 150);

    private final String desc;
    private int lowerBound;
    private int upperBound;

    AgeBrackets(String description, int gLowerBound, int gUpperBound){desc = description; lowerBound = gLowerBound; upperBound = gUpperBound;}

    
    public int getLowerBound(){return this.lowerBound;}
    public int getUpperBound(){return this.upperBound;}

    @Override
    public String toString(){
        return desc;
    }
//    this.ageBrackets.getItems().add("No age constrictions");
//        this.ageBrackets.getItems().add("6 - 8");
//        this.ageBrackets.getItems().add("9 - 11");
//        this.ageBrackets.getItems().add("12 - 15");
//        this.ageBrackets.getItems().add("16+");


}
