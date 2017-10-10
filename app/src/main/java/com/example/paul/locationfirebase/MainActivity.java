package com.example.paul.locationfirebase;

import android.app.ProgressDialog;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karan.churi.PermissionManager.PermissionManager;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GpsTracker gps;
    PermissionManager permissionManager;
    //TABLE REUEST IN FIREBASE DATABASE
    DatabaseReference databaseRequest;

    private ProgressDialog progressDialog;
    public String lat1,lat2,lon1,lon2;

    private static final String API_KEY = "AIzaSyBcfE-YgXF1b-V7Z8TDRphvX4EMwIN9h3U";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionManager = new PermissionManager() {};
        permissionManager.checkAndRequestPermissions(this);

        progressDialog=new ProgressDialog(this);

        databaseRequest=FirebaseDatabase.getInstance().getReference("Request");

        PlaceAutocompleteFragment places= (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                final LatLng location = place.getLatLng();
                double lat,lan;
                lat=location.latitude;
                lan=location.longitude;
                lat2=Double.toString(lat);
                lon2=Double.toString(lan);
                //send lat2 and lon2 to firebase on btn click(GET LOCATION FUNCTION)
                //Toast.makeText(getApplicationContext(), "Latitude " + lat1 + "\nLongitude " + lon1, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Status status) {

                Toast.makeText(getApplicationContext(),status.toString(),Toast.LENGTH_SHORT).show();

            }
        });

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                .build();

        places.setFilter(typeFilter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        ArrayList<String> granted=permissionManager.getStatus().get(0).granted;
        ArrayList<String> denied=permissionManager.getStatus().get(0).denied;
    }

    public void getLocation(View v) {
        double lat = 0, lon = 0;
        String id;
        progressDialog.setMessage("Sending Request...");
        progressDialog.show();

        gps = new GpsTracker(MainActivity.this);

        //Current Location
        lat = gps.getLatitude();
        lon = gps.getLongitude();

        lat1 = Double.toString(lat);
        lon1 = Double.toString(lon);
        //Toast.makeText(getApplicationContext(), "Latitude " + lat + "\nLongitude " + lon, Toast.LENGTH_LONG).show();


        id=databaseRequest.push().getKey();

        request r=new request(id,lat1,lat2,lon1,lon2);
        databaseRequest.child(id).setValue(r).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Server Error Please Try Agian",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Request Sent Sucessfully"+"\n please wiat.....",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        //send both the co-ordinates to firebase
    }

}