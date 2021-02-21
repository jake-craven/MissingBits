package com.colummullally.missing_bits;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Jactivity extends AppCompatActivity {
    EditText uNameIn;
    String gameID;
    String uName;
    EditText roomC;
    int myPosc=0;
    Button Subbtn;
    DatabaseReference iRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference addCode = iRef.child("Monopoly").child("GameCode");
    boolean t = true;
    int myPos;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jactivity);
        uNameIn = (EditText) findViewById(R.id.editText2);
        roomC = (EditText) findViewById(R.id.editText);
        Subbtn = (Button) findViewById(R.id.Sbtn);
        uNameIn.setVisibility(View.INVISIBLE);

    }


    protected void onStart() {
        super.onStart();

        Subbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (t) {

                    gameID=roomC.getText().toString();
                    addCode.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(gameID))
                            {
                                if(dataSnapshot.child(gameID).child("Joinable").getValue(String.class).equals("0")) {
                                    roomC.setVisibility(View.INVISIBLE);
                                    uNameIn.setVisibility(View.VISIBLE);
                                    t = false;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    uName = uNameIn.getText().toString();
                    uName = uName.toLowerCase();
                    final DatabaseReference addUser = iRef.child("Monopoly").child("GameCode").child(gameID).child("Users");
                    //Creates space for the new player
                    addUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (; myPosc < 8; myPosc++) {
                                if (!dataSnapshot.hasChild("" + myPosc)){
                                    iRef.child("Monopoly").child("GameCode").child(gameID).child("Users").child("" + myPosc).child("Uname").setValue(uName);
                                    iRef.child("Monopoly").child("GameCode").child(gameID).child("Users").child(""+myPosc).child("Amount").setValue(1500);
                                    iRef.child("Monopoly").child("GameCode").child(gameID).child("Users").child(""+myPosc).child("Transaction").setValue(0);
                                    iRef.child("Monopoly").child("GameCode").child(gameID).child("Users").child(""+myPosc).child("LastRoll").setValue(0);
                                    iRef.child("Monopoly").child("GameCode").child(gameID).child("Users").child(""+myPosc).child("Turn").setValue(0);
                                    myPos=myPosc;
                                    myPosc = 10;


                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    final DatabaseReference lockIn =iRef.child("Monopoly/GameCode/"+gameID+"/Joinable");
                    lockIn.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue(String.class).equals("1")) {
                                Intent myIntent = new Intent(Jactivity.this, Pactivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("Code", gameID);
                                bundle.putInt("Position", myPos);
                                myIntent.putExtras(bundle);
                                startActivity(myIntent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        });

    }

}
