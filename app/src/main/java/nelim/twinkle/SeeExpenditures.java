package nelim.twinkle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class SeeExpenditures extends AppCompatActivity
{
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_expenditures);
    }

    // Called when user clicks the "Show Expenditures button"
    public void showExpenditures(View view)
    {
        TextView expenditureBox = findViewById(R.id.expenditureBox);
        expenditureBox.setVisibility(View.VISIBLE);
        showExpenses();
    }

    // Gives a text box to the user with all of their expenses.
    private void showExpenses()
    {
        // Obtain a reference to the database.
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReference= mDatabase.getReference();

        // Get a snapshot of the database.
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // Get the count of expenditures for that particular user.
                long noOfExpenditures = dataSnapshot.child("users").child(user.getUid())
                        .getChildrenCount();

                // Empty the box.
                TextView expenditureBox = findViewById(R.id.expenditureBox);
                expenditureBox.setText("");

                // Show the user their expenses.
                for(int count = 1; count <= noOfExpenditures; count++)
                {
                    Expenditure expenditure = dataSnapshot.child("users").child(user.getUid())
                            .child("Expenditure " + count).getValue(Expenditure.class);
                    expenditureBox.append(expenditure.formatForUser() + "\n");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                System.out.println("rejected");
            }
        });
    }
}
