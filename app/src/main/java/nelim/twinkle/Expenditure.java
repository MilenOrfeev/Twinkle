package nelim.twinkle;

import java.text.DateFormat;
import java.text.ParseException;
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
    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public Expenditure()
    {
        //empty
    }
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

    // The next setters and getters are needed when Firebase creates expenditure objects.
    public double getCost()
    {
        return cost;
    }

    public void setCost(double givenCost)
    {
        cost = givenCost;
    }

    public Date getDateOfPurchase()
    {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(String givenDateOfPurchase)
    {

        try
        {
            dateOfPurchase =  dateFormat.parse(givenDateOfPurchase);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    public String formatForUser()
    {
        DateFormat dateAndTimeFormat  = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return "Bought " + name + " for " + cost + " on "
                + dateAndTimeFormat.format(dateOfPurchase);
    }

    // Used mainly for testing
    public String toString()
    {
        return "Name: " + name + ", cost:" + cost + "date of purchase: " + dateOfPurchase;
    }

    public Map<String, Object> toMap()
    {
        HashMap<String, Object> mapExpenditure = new HashMap<>();
        mapExpenditure.put("name", name);
        mapExpenditure.put("cost", cost);
        mapExpenditure.put("dateOfPurchase", dateFormat.format(dateOfPurchase));

        return mapExpenditure;
    }
}
