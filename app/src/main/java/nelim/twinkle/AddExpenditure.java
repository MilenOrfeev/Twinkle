package nelim.twinkle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;


// This class takes care of adding new expenditures to the database.
public class AddExpenditure extends AppCompatActivity
{

    // Get a reference from the database.
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference= mDatabase.getReference();

    // Get the current user.
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenditure);
    } // onCreate

    // Called when user taps add button
    public void buttonAdd(View view)
    {

        // Add the user only if they have logged in.
        if(user != null)
            addUser(user);

        // Create an expenditure object.
        EditText purchaseTextBox = findViewById(R.id.purchaseTextBox);
        EditText txtCost = findViewById(R.id.costTextBox);
        double cost = Double.parseDouble(txtCost.getText().toString());
        Date dateOfPurchase = Calendar.getInstance().getTime();
        Expenditure expenditure;
        expenditure = new Expenditure(purchaseTextBox.getText().toString(),cost, dateOfPurchase);
        Map<String, Object> mappedExpenditure = expenditure.toMap();

        // Add it to the database.
        countAndAddExpenditures(mappedExpenditure);

    }

    // Need the number of expenditures in order to add the new one
    private void countAndAddExpenditures(final Map<String, Object> mappedExpenditure)
    {
        // Listener is for single value event since data is needed only once.
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // Get the count of expenditures for that particular user.
                long noOfExpenditures = dataSnapshot.child("users").child(user.getUid())
                        .getChildrenCount();
                addExpenditure(noOfExpenditures, mappedExpenditure);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                System.out.println("rejected");
            }
        });
    }

    // Invoked after all the necessary data is collected.
    private void addExpenditure(long noOfExpenditures, Map<String, Object> mappedExpenditure)
    {

        // Needed in a string format in order to be added into the database.
        long newExpenditureNo = noOfExpenditures + 1;
        String expenditureNo = "Expenditure " + newExpenditureNo;

        // The only information about the user is about their purchases, so it is no
        // problem to be added right underneath the userID.
        mDatabaseReference.child("users").child(user.getUid()).child(expenditureNo)
                .updateChildren(mappedExpenditure);

    }

    // Add the user if they do not exist in the database.
    private void addUser(final FirebaseUser user)
    {
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();
        DatabaseReference users = root.child("users");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                if (!snapshot.hasChild(user.getUid()))
                {
                    writeNewUser(user);
                } // if
            } // onDataChange

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        }); // addListerForSingleValueEvent
    } // addUser

    // Include the user in the database.
    private void writeNewUser(FirebaseUser user)
    {
        mDatabaseReference.child("users").child(user.getUid()).setValue(user.getDisplayName());
    } // writeNewUser

} // addExpenditure


