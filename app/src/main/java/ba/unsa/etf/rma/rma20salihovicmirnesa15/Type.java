package ba.unsa.etf.rma.rma20salihovicmirnesa15;

public enum Type {
    INDIVIDUALPAYMENT ("prvi"), REGULARPAYMENT("drugi"), PURCHASE("treci"), INDIVIDUALINCOME("cetvrti"),
    REGULARINCOME("peti");
    private String stringValue;

    Type(String toString) {
        stringValue = toString;

    }

    @Override
    public String toString() {
        return stringValue;
    }
}