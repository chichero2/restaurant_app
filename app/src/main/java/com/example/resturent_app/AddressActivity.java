package com.example.resturent_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddressActivity extends AppCompatActivity {


    ImageView addAddress;
    Button imageAdd;
    RecyclerView addressrecycle;

    AccessToken accessToken;
    FirebaseUser firebaseUser;
    GoogleSignInAccount gacc;

    EditText ad1,ad2,pin;

    String userid;
    DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);




        addAddress = findViewById(R.id.addAddress);
        addressrecycle = findViewById(R.id.addressrecycle);

        imageAdd = findViewById(R.id.imageAddress);

        addAddress.setOnClickListener(new View.OnClickListener()//imageview of address activity
        {
            @Override
            public void onClick(View view) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddressActivity.this);
                bottomSheetDialog.setContentView(R.layout.addressdetailsbottomsheet);

                bottomSheetDialog.setCanceledOnTouchOutside(true);

                //If  .show not used then bottomsheet will not come

                ad1 =bottomSheetDialog. findViewById(R.id.Add1);
                ad2 = bottomSheetDialog.findViewById(R.id.Add2);
                pin = bottomSheetDialog.findViewById(R.id.pincd);
                imageAdd = bottomSheetDialog.findViewById(R.id.imageAddress);
                bottomSheetDialog.show();
                accessToken = AccessToken.getCurrentAccessToken();//Facebook
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();//For Mobile


                gacc = GoogleSignIn.getLastSignedInAccount(AddressActivity.this);//for Google
                if(gacc!=null)
                {

                    userid  = gacc.getId();//for google


                }else if(accessToken!=null && !accessToken.isExpired())
                {
                    userid =accessToken.getUserId();//for facebook

                }else if(firebaseUser!=null)
                {
                    userid = firebaseUser.getUid();//for Phone
                }

                imageAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String addline1 = ad1.getText().toString();
                        String addline2 = ad2.getText().toString();
                        String pinc = pin.getText().toString();

                        HashMap<String,Object> mp= new HashMap<>();

                        mp.put("UserId", userid);
                        mp.put("Addresslineone",addline1);
                        mp.put("Addresslinetwo",addline2);
                        mp.put("Pincode",pinc);
                        mRef = FirebaseDatabase.getInstance().getReference();
                        mRef.child("Address").child(userid).push().setValue(mp).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(AddressActivity.this, "Address Addded Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddressActivity.this, "Error,Not Added", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });





            }
        });






    }
}