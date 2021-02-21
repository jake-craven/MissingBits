package com.colummullally.missing_bits;

import android.content.Intent;
import android.media.AsyncPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Cactivity extends AppCompatActivity {
    String code;
    DatabaseReference iRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference gCode;
    Button Abtn;
    TextView rCode;
    TextView[] Player=  new TextView[8];
    int x=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cactivity);
        Abtn=(Button)findViewById(R.id.Abtn);
        rCode=(TextView)findViewById(R.id.Rcode);
        Player[0]=(TextView)findViewById(R.id.p1Text);
        Player[1]=(TextView)findViewById(R.id.p2Text);
        Player[2]=(TextView)findViewById(R.id.p3Text);
        Player[3]=(TextView)findViewById(R.id.p4Text);
        Player[4]=(TextView)findViewById(R.id.p5Text);
        Player[5]=(TextView)findViewById(R.id.p6Text);
        Player[6]=(TextView)findViewById(R.id.p7Text);
        Player[7]=(TextView)findViewById(R.id.p8Text);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            code =(String) b.get("Code");
            rCode.setText("Room Code:"+code);

        }
    }
    protected void onStart(){
        super.onStart();

                gCode=iRef.child("Monopoly/GameCode/"+code+"/Users");
                gCode.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(x=0;x<Player.length;x++){
                        String text =dataSnapshot.child(""+x).child("Uname").getValue(String.class);
                        Player[x].setText(text);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            Abtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View V) {
                iRef.child("Monopoly/GameCode/"+code+"/Joinable").setValue("1");
                Intent MyI=new Intent(Cactivity.this,Gactivity.class);
                Intent myIntent = new Intent(Cactivity.this, Pactivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Code", code);
                bundle.putInt("Position", 1);
                myIntent.putExtras(bundle);
                startActivity(myIntent);
            }
        });
    }
}