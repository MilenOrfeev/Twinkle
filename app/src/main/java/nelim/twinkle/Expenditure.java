package nelim.twinkle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by milen on 24.01.18.
 */

public class Expenditure
{
    private String name;
    private double cost;
    private Date dateOfPurchase;

    public Expenditure(String reqName, double reqCost, Date givenDateOfPurchase)
    {
        name = reqName;
        cost = reqCost;
        dateOfPurchase = givenDateOfPurchase;

    }

    public String getName()
    {
        return name;
    }

    public String toString()
    {
        return String.format("%s\t%s", name, cost);
    }

    public Map<String, Object> toMap()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        HashMap<String, Object> mapExpenditure = new HashMap<>();
        mapExpenditure.put("name", name);
        mapExpenditure.put("cost", cost);
        mapExpenditure.put("purchase date", dateFormat.format(dateOfPurchase));

        return mapExpenditure;
    }
}
