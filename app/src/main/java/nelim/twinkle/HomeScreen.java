package nelim.twinkle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeScreen extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
    }


    /** Called when the user taps the Add Expenditure button */
    public void addPurchase(View view)
    {
        Intent intent = new Intent(this,AddExpenditure.class);
        startActivity(intent);
    }

}
