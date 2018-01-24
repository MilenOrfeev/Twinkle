package nelim.twinkle;

/**
 * Created by milen on 24.01.18.
 */

public class Product {
    private String name;
    private double cost;

    public Product(String reqName, double reqCost) {
        name = reqName;
        cost = reqCost;
    }

    public String toString()
    {
        return String.format("%s\t%s", name, cost);
    }
}
