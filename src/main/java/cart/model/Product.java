package cart.model;

public enum Product {
    CHEERIOS("cheerios"),
    CORNFLAKES("cornflakes"),
    FROSTIES("frosties"),
    SHREDDIES("shreddies"),
    WEETABIX("weetabix");

    Product(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }
}