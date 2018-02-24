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
    public void showExpenditures(View view)
    {
        showExpenses();
    }

    private void showExpenses()
    {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReference= mDatabase.getReference();

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // Get the count of expenditures for that particular user.
                long noOfExpenditures = dataSnapshot.child("users").child(user.getUid())
                        .getChildrenCount();
                //TextView
                for(int count = 1; count <= noOfExpenditures; count++)
                {
                    Expenditure expenditure = dataSnapshot.child("users").child(user.getUid())
                            .child("Expenditure " + count).getValue(Expenditure.class);
                    System.out.println(expenditure);
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
