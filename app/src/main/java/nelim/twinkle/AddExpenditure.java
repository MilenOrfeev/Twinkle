package nelim.twinkle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddExpenditure extends AppCompatActivity {

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference= mDatabase.getReference();

    // Add new users to the database.
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenditure);
    }

    // Called when user taps add button
    public void buttonAdd(View view)
    {

        if(user != null)
            addUser(user);

        // Create a product object.
        EditText purchaseTextBox = findViewById(R.id.purchaseTextBox);
        EditText txtCost = findViewById(R.id.costTextBox);
        double cost = Double.parseDouble(txtCost.getText().toString());
        Date dateOfPurchase = Calendar.getInstance().getTime();
        Expenditure expenditure;
        expenditure = new Expenditure(purchaseTextBox.getText().toString(),cost, dateOfPurchase);
        Map<String, Object> mappedExpenditure = expenditure.toMap();

        addProduct(mappedExpenditure);

    }

    private void addProduct(final Map<String, Object> mappedExpenditure)
    {
        Query lastQuery =
                mDatabaseReference.child("users").child(user.getUid())
                        .orderByChild("Product number").limitToLast(1);

        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //String productNo = dataSnapshot.child("users").child(user.getUid())
                   //     .child("Product number").getValue().toString();
                long noOfProducts = dataSnapshot.child("users").child(user.getUid())
                        .getChildrenCount();
                addProductNo(noOfProducts, mappedExpenditure);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        });
    }

    private void addProductNo(long noOfProducts, Map<String, Object> mappedExpenditure)
    {

        noOfProducts +=1;
        String productNo = noOfProducts + "";
        Map<String, Object> productToProductNo = new HashMap<>();
        productToProductNo.put("Product number", productNo);
        mDatabaseReference.child("users").child(user.getUid())
                .updateChildren(productToProductNo);
        mDatabaseReference.child("users").child(user.getUid()).child(productNo)
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
                    System.out.println(!snapshot.hasChild(user.getUid()));
                    writeNewUser(user);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    // Include the user in the database.
    private void writeNewUser(FirebaseUser user)
    {
        mDatabaseReference.child("users").child(user.getUid()).setValue(user.getDisplayName());
    }

}


