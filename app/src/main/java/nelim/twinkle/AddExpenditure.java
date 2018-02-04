package nelim.twinkle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;

public class AddExpenditure extends AppCompatActivity {

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference= mDatabase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenditure);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    // Called when user taps add button
    public void buttonAdd(View view)
    {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
            addUser(user);
        EditText purchaseTextBox = findViewById(R.id.purchaseTextBox);
        EditText txtCost = findViewById(R.id.costTextBox);
        System.out.println(txtCost.getText());
        System.out.println(Double.parseDouble(txtCost.getText().toString()));
        double cost = Double.parseDouble(txtCost.getText().toString());
        Product product = new Product(purchaseTextBox.getText().toString(),cost);
        System.out.println(product);


        String filename = "purchases.txt";
        String string = product.toString() + "\n";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addUser(final FirebaseUser user)
    {
        //DatabaseReference users = mDatabaseReference.child("users");
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

    private void writeNewUser(FirebaseUser user)
    {
        mDatabaseReference.child("users").child(user.getUid()).setValue(user.getUid());
    }

}


