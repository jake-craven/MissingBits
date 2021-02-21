package com.colummullally.missing_bits;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class MainActivity extends AppCompatActivity {
    DatabaseReference iRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference gCode = iRef.child("Monopoly/GameCode");
    int[] code = new int[5];
    String gameID;
    Button Jbtn;
    Button Cbtn;
    Button sBtn;
    EditText uNameIn;
    String uName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Jbtn=(Button)findViewById(R.id.JoinBtn);
        Cbtn=(Button)findViewById(R.id.CreateBtn);
        sBtn = (Button) findViewById(R.id.Sbtn);
        uNameIn = (EditText)findViewById(R.id.editText);
        uNameIn.setVisibility(View.INVISIBLE);
        sBtn.setVisibility(View.INVISIBLE);
    }
    protected void onStart() {
        super.onStart();

        sBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uName = uNameIn.getText().toString();
            uName = uName.toLowerCase();
                    iRef.child("Monopoly").child("GameCode").child(gameID).child("Users").child(""+1).child("Uname").setValue(uName);
                    iRef.child("Monopoly").child("GameCode").child(gameID).child("Users").child(""+1).child("Amount").setValue(1500);
                    iRef.child("Monopoly").child("GameCode").child(gameID).child("Users").child(""+1).child("Transaction").setValue(0);
                    iRef.child("Monopoly").child("GameCode").child(gameID).child("Users").child(""+1).child("LastRoll").setValue(0);
                    iRef.child("Monopoly").child("GameCode").child(gameID).child("Users").child(""+1).child("Turn").setValue(0);

            Intent myIntent=new Intent(MainActivity.this,Cactivity.class);
            Bundle bundle= new Bundle();
            bundle.putString("Code",gameID);
            myIntent.putExtras(bundle);
            startActivity(myIntent);

            }
        });


        Cbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View V) {
                iRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(int x = 0; x < code.length ; x++)
                        {
                            code[x] = (int) (Math.random()*9);
                        }
                        while(dataSnapshot.hasChild(""+code[0]+code[1]+code[2]+code[3]+code[4]))
                        {
                            for(int x = 0; x < code.length ; x++)
                            {
                                code[x] = (int) (Math.random()*9);
                            }
                        }
                        gCode.child(""+code[0]+code[1]+code[2]+code[3]+code[4]).child("Users").child("0").child("Uname").setValue("Bank");
                        gCode.child(""+code[0]+code[1]+code[2]+code[3]+code[4]).child("Joinable").setValue("0");
                        gameID =""+code[0]+code[1]+code[2]+code[3]+code[4];
                        Cbtn.setVisibility(View.INVISIBLE);
                        Jbtn.setVisibility(View.INVISIBLE);
                        uNameIn.setVisibility(View.VISIBLE);
                        sBtn.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                    gameID =""+code[0]+code[1]+code[2]+code[3]+code[4];

            }
        });
        Jbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View V) {
                Intent mIntent=new Intent(MainActivity.this,Jactivity.class);
                startActivity(mIntent);
            }
        });
    }
}

