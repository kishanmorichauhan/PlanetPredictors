package com.droid7technolabs.planetpredictor.UserDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.droid7technolabs.planetpredictor.Navigation2Activity;
import com.droid7technolabs.planetpredictor.OnBoarding;
import com.droid7technolabs.planetpredictor.R;
import com.droid7technolabs.planetpredictor.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vikktorn.picker.City;
import com.vikktorn.picker.CityPicker;
import com.vikktorn.picker.Country;
import com.vikktorn.picker.CountryPicker;
import com.vikktorn.picker.OnCityPickerListener;
import com.vikktorn.picker.OnCountryPickerListener;
import com.vikktorn.picker.OnStatePickerListener;
import com.vikktorn.picker.State;
import com.vikktorn.picker.StatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class BirthProfileActivity extends AppCompatActivity implements OnStatePickerListener, OnCountryPickerListener, OnCityPickerListener {

    //For BirthProfile Page

    private CircleImageView profile_img;
    private EditText Full_name;
    private TextView date;
    private String D_ate;
    Uri selectedImage;
    private DatePickerDialog.OnDateSetListener setListener;
    public String set_time;
    private TextView Time;
    private int hour, min;
    private FirebaseFirestore fstore;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    Button save_details;
    private TextView DEVICE_ID;
    private String str_device_id;
    RadioGroup rg;
    String gender;
    RadioButton male, female, trans;
    ProgressDialog progressDialog;



    //country picker
    public static int countryID, stateID;
    private Button pickStateButton, pickCountry, pickCity;
    private TextView stateNameTextView, countryName, cityName;
    private ImageView flagImage;
    // Pickers
    private CountryPicker countryPicker;
    private StatePicker statePicker;
    private CityPicker cityPicker;
    // arrays of state object
    public static List<State> stateObject;
    // arrays of city object
    public static List<City> cityObject;
    public int countryCounter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birth_profile);

        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();


        profile_img = (CircleImageView) findViewById(R.id.profile_img);
        date = (TextView) findViewById(R.id.date);
        Time = (TextView) findViewById(R.id.time);
        Full_name = (EditText) findViewById(R.id.full_name);
        save_details = (Button) findViewById(R.id.button);
        rg = (RadioGroup) findViewById(R.id.radio_group1);
        male = findViewById(R.id.radioMale);
        female = findViewById(R.id.radioFemale);
        trans = findViewById(R.id.radioOther);

        //progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating your profile...");
        progressDialog.setCancelable(false);


        //   Device ID Number
        DEVICE_ID = findViewById(R.id.device_id);
        str_device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        // DEVICE_ID.setText("Device ID: "+str_device_id); for showing device id

        // Setting date
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        //country picker
        initView();
        // get state from assets JSON
        try {
            getStateJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // get City from assets JSON
        try {
            getCityJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // initialize country picker
        countryPicker = new CountryPicker.Builder().with(this).listener(this).build();

        // initialize listeners
        setListener();
        setCountryListener();
        setCityListener();


        //open gallery
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 45);
            }
        });

        //timePicker
        Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(BirthProfileActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Time.setText(selectedHour + ":" + selectedMinute);

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        //Date picker
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(BirthProfileActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                D_ate = dayOfMonth + "/" + month + "/" + year;
                date.setText(D_ate);
            }
        };


            // saving data Button
            save_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //save button by clicking once
                    progressDialog.show();
                        mAuth.signInAnonymously()
                                .addOnCompleteListener(BirthProfileActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInAnonymously:success");
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            //Store User Detail
                                            if (user.isAnonymous()) {
                                                progressDialog.show();
                                                if (selectedImage != null) {
                                                    StorageReference reference = storage.getReference().child("UserImage").child(mAuth.getUid());
                                                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    String imageUrl = uri.toString();
                                                                    String fullname = Full_name.getText().toString();
                                                                    String uid = mAuth.getCurrentUser().getUid();
                                                                    String City = cityName.getText().toString();
                                                                    String Country = countryName.getText().toString();
                                                                    String State = stateNameTextView.getText().toString();
                                                                    String time = Time.getText().toString();
                                                                    String Date = date.getText().toString();


                                                                    // Validation field , is empty or not.
                                                                    if (fullname.matches("")) {
                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(BirthProfileActivity.this, "You didn't enter a Your name", Toast.LENGTH_SHORT).show();
                                                                        Full_name.setError("Enter Your Name");
                                                                        return;
                                                                    }
                                                                    if (Date.matches("DOB")) {
                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(BirthProfileActivity.this, "You didn't enter a Your BirthDate", Toast.LENGTH_LONG).show();
                                                                        return;
                                                                    }

                                                                    if (time.matches("Birth Time")) {
                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(BirthProfileActivity.this, "Please Select Your BirthTime", Toast.LENGTH_LONG).show();
                                                                        return;
                                                                    }
                                                                    if (Country.matches("Country")) {
                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(BirthProfileActivity.this, "Please Select Your Country", Toast.LENGTH_LONG).show();
                                                                        return;
                                                                    }
                                                                    if (State.matches("State")) {
                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(BirthProfileActivity.this, "Please Select Your State", Toast.LENGTH_LONG).show();
                                                                        return;
                                                                    }
                                                                    if (City.matches("City")) {
                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(BirthProfileActivity.this, "Please Select Your City", Toast.LENGTH_LONG).show();
                                                                        return;
                                                                    }
                                                                    if (rg.getCheckedRadioButtonId() == -1) {
                                                                        progressDialog.dismiss();
                                                                        Toast.makeText(BirthProfileActivity.this, "select gender", Toast.LENGTH_SHORT).show();
                                                                        return;
                                                                    } else {

                                                                    }

                                                                    UserData userData = new UserData(fullname, Date, str_device_id, imageUrl, time, City, Country, uid, State, gender);
                                                                    fstore.collection("User").document(str_device_id).set(userData)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    progressDialog.dismiss();
                                                                                    Toast.makeText(BirthProfileActivity.this, "Profile Updated", Toast.LENGTH_LONG).show();
                                                                                    Intent main = new Intent(BirthProfileActivity.this, Navigation2Activity.class);
                                                                                    startActivity(main);

                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            progressDialog.dismiss();
                                                                            Toast.makeText(BirthProfileActivity.this, " Failure:" + e.getMessage(), Toast.LENGTH_LONG).show();
                                                                        }
                                                                    });


                                                                }
                                                            });
                                                        }
                                                    });
                                                } else {
                                                    String fullname = Full_name.getText().toString();
                                                    String uid = mAuth.getCurrentUser().getUid();
                                                    String City = cityName.getText().toString();
                                                    String Country = countryName.getText().toString();
                                                    String State = stateNameTextView.getText().toString();
                                                    String time = Time.getText().toString();
                                                    String Date = date.getText().toString();

                                                    // Validation field , is empty or not.
                                                    if (fullname.matches("")) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(BirthProfileActivity.this, "You didn't enter a Your name", Toast.LENGTH_SHORT).show();
                                                        Full_name.setError("Enter Your Name");
                                                        return;
                                                    }
                                                    if (Date.matches("DOB")) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(BirthProfileActivity.this, "You didn't enter a Your BirthDate", Toast.LENGTH_LONG).show();
                                                        return;
                                                    }

                                                    if (time.matches("Birth Time")) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(BirthProfileActivity.this, "Please Select Your BirthTime", Toast.LENGTH_LONG).show();
                                                        return;
                                                    }
                                                    if (Country.matches("Country")) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(BirthProfileActivity.this, "Please Select Your Country", Toast.LENGTH_LONG).show();
                                                        return;
                                                    }
                                                    if (State.matches("State")) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(BirthProfileActivity.this, "Please Select Your State", Toast.LENGTH_LONG).show();
                                                        return;
                                                    }
                                                    if (City.matches("City")) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(BirthProfileActivity.this, "Please Select Your City", Toast.LENGTH_LONG).show();
                                                        return;
                                                    }

                                                    if (rg.getCheckedRadioButtonId() == -1) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(BirthProfileActivity.this, "select gender", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    } else {

                                                    }

                                                    UserData userData = new UserData(fullname, Date, str_device_id, "No Image", time, City, Country, uid, State, gender);
                                                    fstore.collection("User").document(str_device_id).set(userData)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(BirthProfileActivity.this, "Profile Updated", Toast.LENGTH_LONG).show();
                                                                    Intent main = new Intent(BirthProfileActivity.this, Navigation2Activity.class);
                                                                    startActivity(main);

                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(BirthProfileActivity.this, " Failure:" + e.getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                }
                                            }

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                                            progressDialog.dismiss();
                                            Toast.makeText(BirthProfileActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });
                }
            });


            //fetching data from fireStore
        DocumentReference docRef = fstore.collection("User").document(str_device_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        String eName = document.getString("fullname");
                        String eBirthDate = document.getString("date");
                        String eTime = document.getString("time");
                        String eCountry = document.getString("country");
                        String eState = document.getString("state");
                        String eCity = document.getString("city");
                        String eGender = document.getString("gender");
                        String ePhoto = document.getString("profile_image");

                        try {
                            //setting the gender
                            if (eGender != null) {
                                if (eGender.equalsIgnoreCase("male")) {
                                    male.setChecked(true);
                                    gender = "male";
                                } else if (eGender.equalsIgnoreCase("female")) {
                                    female.setChecked(true);
                                    gender = "female";
                                } else if (eGender.equalsIgnoreCase("trans")) {
                                    trans.setChecked(true);
                                    gender = "trans";
                                }
                            } else {
                                Toast.makeText(BirthProfileActivity.this, "Please Select your gender", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(BirthProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        Glide.with(BirthProfileActivity.this).load(ePhoto)
                                .placeholder(R.drawable.profile_image)
                                .into((ImageView) findViewById(R.id.profile_img));

                        //set value
                        Full_name.setText(eName);
                        date.setText(eBirthDate);
                        Time.setText(eTime);
                        countryName.setText(eCountry);
                        stateNameTextView.setText(eState);
                        cityName.setText(eCity);


                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });

        //showing all the Details if user is present
        if (mAuth.getCurrentUser() != null) {
            countryName.setVisibility(View.VISIBLE);
            cityName.setVisibility(View.VISIBLE);
            stateNameTextView.setVisibility(View.VISIBLE);
        }


    } // end of OnCreate


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioMale:
                if (checked)
                    gender = "male";
                break;
            case R.id.radioFemale:
                if (checked)
                    gender = "female";
                break;
            case R.id.radioOther:
                if (checked)
                    gender = "trans";
                break;
        }
    }

    // county picker
    public void initView() {
        //Buttons
        pickStateButton = (Button) findViewById(R.id.pickState);
        //set state picker invisible
        // pickStateButton.setVisibility(View.INVISIBLE);
        pickCountry = (Button) findViewById(R.id.pickCountry);
        pickCity = (Button) findViewById(R.id.pick_city);
        // set city picker invisible
        //pickCity.setVisibility(View.INVISIBLE);
        // Text Views
        countryName = (TextView) findViewById(R.id.countryNameTextView);
        stateNameTextView = (TextView) findViewById(R.id.state_name);
        //set state name text view invisible
        // stateNameTextView.setVisibility(View.INVISIBLE);
        cityName = (TextView) findViewById(R.id.city_name);
        //set state name text view invisible
        // cityName.setVisibility(View.INVISIBLE);

        // ImageView
        flagImage = (ImageView) findViewById(R.id.flag_image);

        // initiate state object, parser, and arrays
        stateObject = new ArrayList<>();
        cityObject = new ArrayList<>();
        int countryCounter = 0;
    }


    //SET COUNTRY LISTENER
    private void setCountryListener() {

        pickCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryCounter++;
                countryPicker.showDialog(getSupportFragmentManager());
            }
        });
    }

    // SET STATE LISTENER
    private void setListener() {

        pickStateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countryCounter >= 1) {
                    countryCounter++;
                    statePicker.showDialog(getSupportFragmentManager());
                } else {
                    Toast.makeText(BirthProfileActivity.this, "First select your country", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //SET CITY LISTENER
    private void setCityListener() {
        pickCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countryCounter >= 2) {
                    try {
                        cityPicker.showDialog(getSupportFragmentManager());

                    } catch (Exception e) {
                        Toast.makeText(BirthProfileActivity.this, "Your country is not supported, Please change it", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(BirthProfileActivity.this, "First select your state", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // ON SELECTED COUNTRY ADD STATES TO PICKER
    @Override
    public void onSelectCountry(Country country) {
        // get country name and country ID
        countryName.setText(country.getName());
        countryName.setVisibility(View.VISIBLE);
        countryID = country.getCountryId();
        statePicker.equalStateObject.clear();
        cityPicker.equalCityObject.clear();

        //set state name text view and state pick button invisible
        //pickStateButton.setVisibility(View.VISIBLE);
        //stateNameTextView.setVisibility(View.VISIBLE);
        stateNameTextView.setText("State");
        cityName.setText("City");
        // set text on main view
        flagImage.setBackgroundResource(country.getFlag());


        // GET STATES OF SELECTED COUNTRY
        for (int i = 0; i < stateObject.size(); i++) {
            // init state picker
            statePicker = new StatePicker.Builder().with(this).listener(this).build();
            State stateData = new State();
            if (stateObject.get(i).getCountryId() == countryID) {

                stateData.setStateId(stateObject.get(i).getStateId());
                stateData.setStateName(stateObject.get(i).getStateName());
                stateData.setCountryId(stateObject.get(i).getCountryId());
                stateData.setFlag(country.getFlag());
                statePicker.equalStateObject.add(stateData);
            }
        }
    }

    // ON SELECTED STATE ADD CITY TO PICKER
    @Override
    public void onSelectState(State state) {
        // pickCity.setVisibility(View.VISIBLE);
        stateNameTextView.setVisibility(View.VISIBLE);
        cityName.setText("City");
        cityPicker.equalCityObject.clear();

        stateNameTextView.setText(state.getStateName());
        stateID = state.getStateId();


        for (int i = 0; i < cityObject.size(); i++) {
            cityPicker = new CityPicker.Builder().with(this).listener(this).build();
            City cityData = new City();
            if (cityObject.get(i).getStateId() == stateID) {
                cityData.setCityId(cityObject.get(i).getCityId());
                cityData.setCityName(cityObject.get(i).getCityName());
                cityData.setStateId(cityObject.get(i).getStateId());

                cityPicker.equalCityObject.add(cityData);
            }
        }
    }

    // ON SELECTED CITY
    @Override
    public void onSelectCity(City city) {
        try {
            cityName.setText(city.getCityName());
            cityName.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Please change your country", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    // GET STATE FROM ASSETS JSON
    public void getStateJson() throws JSONException {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("states.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }


        JSONObject jsonObject = new JSONObject(json);
        JSONArray events = jsonObject.getJSONArray("states");
        for (int j = 0; j < events.length(); j++) {
            JSONObject cit = events.getJSONObject(j);
            State stateData = new State();

            stateData.setStateId(Integer.parseInt(cit.getString("id")));
            stateData.setStateName(cit.getString("name"));
            stateData.setCountryId(Integer.parseInt(cit.getString("country_id")));
            stateObject.add(stateData);
        }
    }

    // GET CITY FROM ASSETS JSON
    public void getCityJson() throws JSONException {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("cities.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }


        JSONObject jsonObject = new JSONObject(json);
        JSONArray events = jsonObject.getJSONArray("cities");
        for (int j = 0; j < events.length(); j++) {
            JSONObject cit = events.getJSONObject(j);
            City cityData = new City();

            cityData.setCityId(Integer.parseInt(cit.getString("id")));
            cityData.setCityName(cit.getString("name"));
            cityData.setStateId(Integer.parseInt(cit.getString("state_id")));
            cityObject.add(cityData);
        }
    }

    //for getting image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (data.getData() != null) {
                profile_img.setImageURI(data.getData());
                selectedImage = data.getData();
            }
        }
    }
} // end of AppCompatActivity