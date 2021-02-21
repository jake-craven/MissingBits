package com.colummullally.missing_bits;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ToggleButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Pactivity extends AppCompatActivity {
    boolean transfer = true;
    public static String code, myName;
    public static DatabaseReference iRef= FirebaseDatabase.getInstance().getReference();
    DatabaseReference gCode;
    int x, nextTurn;
    TextView pAmount, dRoll;
    public static TextView iDie;
    Button dice, end;
    ToggleButton views;
    public static int p = 0, tPos, bankViewJail;
    Button[] Player=  new Button[9];
    public static int myPos, cash, nextPos, tot;
    boolean doubles = false, jail = false;
    public static String transfername;
    public static boolean bankView = false;

    public static class transaction extends DialogFragment {

        public static transaction newInstance(int title) {
            transaction frag = new transaction();
            Bundle args = new Bundle();
            args.putInt("title", title);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final EditText numIn = new EditText(getActivity());
            numIn.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(numIn);
            if(bankView){
                if(bankViewJail == 2 || bankViewJail == 3){
                    builder.setMessage("Transfer Money to "+transfername)
                            .setPositiveButton("Transfer", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String ed = numIn.getText().toString().trim();
                                    if(!ed.isEmpty()) {
                                        p = Integer.parseInt(numIn.getText().toString());

                                        if(bankView){
                                            iRef.child("Monopoly/GameCode/"+code+"/Users/"+tPos+"/Transaction").setValue(p);
                                            iRef.child("Monopoly/GameCode/"+code+"/Users/"+tPos+"/LastRoll").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    iRef.child("Monopoly/GameCode/"+code+"/Users/"+tPos+"/Summary").setValue(transfername+"\nLast Roll: "+dataSnapshot.getValue()+"\nReceived $"+p+" from bank");
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                        else if(cash >= p && p > 0){
                                            iRef.child("Monopoly/GameCode/"+code+"/Users/"+tPos+"/Transaction").setValue(p);
                                            iRef.child("Monopoly/GameCode/"+code+"/Users/"+myPos+"/Amount").setValue(cash-p);
                                            iRef.child("Monopoly/GameCode/"+code+"/Users/"+myPos+"/Summary").setValue(myName+"\n"+"Last Roll: "+tot+"\nTransfered $"+p+" to "+transfername);
                                            iDie.setText("Transferred $" + p+" to "+transfername);
                                            iDie.setBackgroundColor(0xFF3BFF2D);
                                        }
                                        else {
                                            iDie.setText("Insufficient funds");
                                            iDie.setBackgroundColor(0xFFE71224);
                                        }
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    p = -1;
                                }
                            })
                            .setNeutralButton("Release", new DialogInterface.OnClickListener()   {
                                public void onClick(DialogInterface dialog, int id) {
                                    iRef.child("Monopoly/GameCode/"+code+"/Users/"+tPos+"/Turn").setValue(bankViewJail-2);
                                    bankViewJail = bankViewJail -2;
                                    iRef.child("Monopoly/GameCode/"+code+"/Users/"+tPos+"/LastRoll").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            iRef.child("Monopoly/GameCode/"+code+"/Users/"+tPos+"/Summary").setValue(transfername+"\nLast Roll: "+dataSnapshot.getValue()+"\nReleased from jail");
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                }
                else if(bankViewJail == 0 || bankViewJail == 1){
                    builder.setMessage("Transfer Money to "+transfername)
                            .setPositiveButton("Transfer", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    String ed = numIn.getText().toString().trim();
                                    if(!ed.isEmpty()) {
                                        p = Integer.parseInt(numIn.getText().toString());
                                        iRef.child("Monopoly/GameCode/"+code+"/Users/"+tPos+"/Transaction").setValue(p);
                                        iRef.child("Monopoly/GameCode/"+code+"/Users/"+tPos+"/LastRoll").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                iRef.child("Monopoly/GameCode/"+code+"/Users/"+tPos+"/Summary").setValue(transfername+"\nLast Roll: "+dataSnapshot.getValue()+"\nReceived $"+p+" from bank");
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    p = -1;
                                }
                            })
                            .setNeutralButton("Imprison", new DialogInterface.OnClickListener()   {
                                public void onClick(DialogInterface dialog, int id) {
                                    iRef.child("Monopoly/GameCode/"+code+"/Users/"+tPos+"/Turn").setValue(bankViewJail+2);
                                    bankViewJail = bankViewJail +2;
                                    iRef.child("Monopoly/GameCode/"+code+"/Users/"+tPos+"/LastRoll").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            iRef.child("Monopoly/GameCode/"+code+"/Users/"+tPos+"/Summary").setValue(transfername+"\nLast Roll: "+dataSnapshot.getValue()+"\nImprisoned");
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                 }
                            });

                }
            }
            else{
                builder.setMessage("Transfer Money to "+transfername)
                        .setPositiveButton("Transfer", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String ed = numIn.getText().toString().trim();
                                if(!ed.isEmpty()) {
                                    p = Integer.parseInt(numIn.getText().toString());
                                    if(cash >= p && p > 0){
                                        iRef.child("Monopoly/GameCode/"+code+"/Users/"+tPos+"/Transaction").setValue(p);
                                        iRef.child("Monopoly/GameCode/"+code+"/Users/"+myPos+"/Amount").setValue(cash-p);
                                        iRef.child("Monopoly/GameCode/"+code+"/Users/"+myPos+"/Summary").setValue(myName+"\n"+"Last Roll: "+tot+"\nTransfered $"+p+" to "+transfername);
                                        iDie.setText("Transferred $" + p+" to "+transfername);
                                        iDie.setBackgroundColor(0xFF3BFF2D);
                                    }
                                    else {
                                        iDie.setText("Insufficient funds");
                                        iDie.setBackgroundColor(0xFFE71224);
                                    }
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                p = -1;
                            }
                        });
            }
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pactivity);
        dRoll = (TextView) findViewById(R.id.diceView);
        pAmount = (TextView) findViewById(R.id.pAmount);
        DisplayMetrics metrics = new DisplayMetrics();
        int densityDpi = (int)(metrics.density * 160f);
        dRoll = (TextView) findViewById(R.id.diceView);
        pAmount.setMinWidth(dRoll.getWidth());
        dRoll.setMinWidth(dRoll.getWidth());
        iDie = (TextView) findViewById(R.id.iDie);
        iDie.setMaxHeight(20);
        views = (ToggleButton)findViewById(R.id.viewSwitch);
        Player[0]=(Button)findViewById(R.id.P0Name);
        Player[1]=(Button)findViewById(R.id.P1Name);
        Player[2]=(Button)findViewById(R.id.P2Name);
        Player[3]=(Button)findViewById(R.id.P3Name);
        Player[4]=(Button)findViewById(R.id.P4Name);
        Player[5]=(Button)findViewById(R.id.P5Name);
        Player[6]=(Button)findViewById(R.id.P6Name);
        Player[7]=(Button)findViewById(R.id.P7Name);
        Player[8]=(Button)findViewById(R.id.P8Name);
        Player[8].setVisibility(View.INVISIBLE);
        Player[0].setVisibility(View.INVISIBLE);
        Player[1].setVisibility(View.INVISIBLE);
        Player[2].setVisibility(View.INVISIBLE);
        Player[3].setVisibility(View.INVISIBLE);
        Player[4].setVisibility(View.INVISIBLE);
        Player[5].setVisibility(View.INVISIBLE);
        Player[6].setVisibility(View.INVISIBLE);
        Player[7].setVisibility(View.INVISIBLE);
        dice =(Button)findViewById(R.id.diceButton);
        end =(Button)findViewById(R.id.endButton);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            code =(String) b.get("Code");
            myPos=(int) b.get("Position");

        }

        if (myPos != 1){
            views.setVisibility(View.INVISIBLE);
        }



        gCode=iRef.child("Monopoly/GameCode/"+code+"/Users");
        gCode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int y =0;
                boolean t = true;
                for(x=0;x<Player.length && t;x++){
                    if(dataSnapshot.child("" + x).hasChild("Uname")){
                        String text = dataSnapshot.child(""+x).child("Uname").getValue(String.class);
                        Player[x].setText(text);
                        Player[x].setVisibility(View.VISIBLE);
                    }
                    else if(myPos == x-1){
                        nextPos = 1;
                        t = false;
                        x--;
                    }
                    else{
                        nextPos = x+1;
                        t = false;
                        x--;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Player[myPos].setClickable(false);

        gCode.child(""+myPos).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cash = dataSnapshot.child("Amount").getValue(int.class);
                pAmount.setText("$"+cash);
                myName = dataSnapshot.child("Uname").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        views.setTextOn(myName+" View");
    }
    protected void onStart(){
        super.onStart();
        iRef.child("Monopoly/GameCode/"+code+"/Users/"+myPos+"/Summary").setValue(myName);
        views.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Player[myPos].setClickable(true);
                    Player[0].setVisibility(View.INVISIBLE);
                    end.setVisibility(View.INVISIBLE);
                    dice.setVisibility(View.INVISIBLE);
                    for(int i = 1; i < Player.length; i++){
                        Player[i].setBackgroundColor(0xFF000000);
                        Player[i].setTextColor(0xFFFFFFFF);
                    }

                    bankView = true;
                }
                else {
                    Player[myPos].setClickable(false);
                    Player[0].setVisibility(View.VISIBLE);
                    end.setVisibility(View.VISIBLE);
                    dice.setVisibility(View.VISIBLE);
                    for(int i = 1; i < Player.length; i++){
                        Player[i].setBackgroundColor(0xFFFFFFFF);
                        Player[i].setTextColor(0xFF000000);
                    }
                    bankView = false;
                }
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRef.child("Monopoly/GameCode/"+code+"/Users/"+nextPos+"/Turn").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        nextTurn = dataSnapshot.getValue(int.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                iRef.child("Monopoly/GameCode/"+code+"/Users/"+nextPos+"/Turn").setValue(nextTurn + 2);
            }
        });

        dice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int roll1 =(int) (Math.random()*6)+1;
                int roll2 =(int) (Math.random()*6)+1;
                tot = roll1 + roll2;
                if (roll1 == roll2 && doubles){
                        doubles = false;
                        jail = true;
                        iRef.child("Monopoly/GameCode/"+code+"/Users/"+myPos+"/Summary").setValue(myName+"\n"+"Last Roll: "+tot+"\n2 Doubles, in Jail");
                        iDie.setText("In Jail");
                        iDie.setBackgroundColor(0xFFE71224);
                        iRef.child("Monopoly/GameCode/"+code+"/Users/"+myPos+"/Turn").setValue(2);
                    }
                else if (roll1 == roll2){
                    if(jail){
                        iRef.child("Monopoly/GameCode/"+code+"/Users/"+myPos+"/Summary").setValue(myName+"\n"+"Last Roll: "+tot+"\nReleased");
                        iDie.setText("Doubles - Released");
                        jail = false;
                        iDie.setBackgroundColor(0xFF3BFF2D);
                        iRef.child("Monopoly/GameCode/"+code+"/Users/"+myPos+"/Turn").setValue(0);
                    }
                    else{
                        iDie.setText("Doubles! Roll again");
                        iDie.setBackgroundColor(0xFF3BFF2D);
                        iRef.child("Monopoly/GameCode/"+code+"/Users/"+myPos+"/Summary").setValue(myName+"\n"+"Last Roll: "+tot+"\nRolled Doubles");
                        doubles = true;
                    }}
                else{
                    doubles = false;
                    if(!jail) {
                        iDie.setText("Rolled singles");
                        iDie.setBackgroundColor(0xFF3BFF2D);
                        iRef.child("Monopoly/GameCode/"+code+"/Users/"+myPos+"/Summary").setValue(myName+"\n"+"Last Roll: "+tot+"\nRolled Singles");
                        iRef.child("Monopoly/GameCode/" + code + "/Users/" + nextPos + "/Turn").setValue(0);
                    }
                }
                iRef.child("Monopoly/GameCode/"+code+"/Users/"+myPos+"/LastRoll").setValue(tot);
                dRoll.setText("Last Roll: "+tot);
            }
        });

        Player[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tPos = 0;
                transaction();
            }
        });

        Player[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tPos = 1;
                transaction();
                
            }
        });

        Player[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tPos = 2;
                transaction();
            }
        });

        Player[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tPos = 3;
                transaction();
                
            }
        });

        Player[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tPos = 4;
                transaction();
                
            }
        });

        Player[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tPos = 5;
                transaction();
                
            }
        });

        Player[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tPos = 6;
                transaction();
                
            }
        });

        Player[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tPos = 7;
                transaction();
            }
        });

        Player[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tPos = 8;
                transaction();
            }
        });

       final DatabaseReference player1 = iRef.child("Monopoly/GameCode/"+code+"/Users/"+myPos);
        player1.child("Transaction").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue(int.class) > 0) {
                    cash = cash + dataSnapshot.getValue(int.class);
                    player1.child("Amount").setValue(cash);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        player1.child("Amount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pAmount.setText("$" + dataSnapshot.getValue(int.class));
                cash = dataSnapshot.getValue(int.class);
                player1.child("Transaction").setValue(0);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        player1.child("Turn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Turn").equals("1")) {
                    jail = false;
                }
                else if (dataSnapshot.child("Turn").equals("3")) {
                    jail = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        iRef.child("Monopoly/GameCode/"+code+"/Users/1/Summary/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Player[1].setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        iRef.child("Monopoly/GameCode/"+code+"/Users/2/Summary/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Player[2].setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        iRef.child("Monopoly/GameCode/"+code+"/Users/2/Summary/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Player[2].setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        iRef.child("Monopoly/GameCode/"+code+"/Users/3/Summary/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Player[3].setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        iRef.child("Monopoly/GameCode/"+code+"/Users/4/Summary/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Player[4].setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        iRef.child("Monopoly/GameCode/"+code+"/Users/5/Summary/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Player[5].setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        iRef.child("Monopoly/GameCode/"+code+"/Users/6/Summary/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Player[6].setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        iRef.child("Monopoly/GameCode/"+code+"/Users/7/Summary/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Player[7].setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
/*
    public void updateMoney()
    {
        iRef.child("Monopoly/GameCode/"+code+"/Users/"+myPos+"/Transaction").setValue(""+cash);
        iRef.child("Monopoly/GameCode/"+code+"/Users/"+myPos+"/Transaction").setValue(0);
    }
*/
    public void transaction() {
        iRef.child("Monopoly/GameCode/"+code+"/Users/"+tPos).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                   transfername = dataSnapshot.child("Uname").getValue(String.class);
                    bankViewJail = dataSnapshot.child("Turn").getValue(int.class);
                }
            public void onCancelled(DatabaseError error){

            }});
        new transaction().show(getSupportFragmentManager(), "pop up");
    }
}