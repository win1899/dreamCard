package com.dreamcard.app.view.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dreamcard.app.R;
import com.dreamcard.app.common.InputValidator;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.entity.City;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.PersonalInfo;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.services.CitiesAsyncTask;
import com.dreamcard.app.services.CountryAsyncTask;
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.interfaces.IDatePickerListener;
import com.dreamcard.app.view.interfaces.IServiceListener;
import com.dreamcard.app.view.interfaces.OnFragmentInteractionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.dreamcard.app.view.interfaces.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ActivationInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ActivationInformationFragment extends Fragment implements View.OnClickListener,IServiceListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private IDatePickerListener listener=new IDatePickerListener() {
        @Override
        public void setDate(String date, int processId) {
            txtBirthday.setText(date);
        }
    };

    private OnFragmentInteractionListener mListener;

    private Button btnMale;
    private Button btnFemale;
    private EditText txtFirstName;
//    private EditText txtLastName;
    private EditText txtPassword;
    private EditText txtRepeatPassword;
    private EditText txtBirthday;
    private EditText txtUsername;
    private EditText txtMobile;
    private EditText txtCountry;
    private EditText txtCity;
    private EditText txtId;
    private EditText txtEducation;
    private EditText txtWork;
    private EditText txtAddress;
    private EditText txtPhone;

    private String[] citiesArray;
    private ArrayList<City> citiesList=new  ArrayList<City>();

    private String[] countriesArray;
    private ArrayList<City> countriesList=new  ArrayList<City>();

    private int cityIndex;
    private String selectedCity;
    private int countryIndex;
    private String selectedCountry;
    private boolean isFacebook = false;

    private int gender;

    private CitiesAsyncTask citiesAsyncTask;
    private CountryAsyncTask countriesAsyncTask;

    public static ActivationInformationFragment newInstance(String param1, String param2) {
        ActivationInformationFragment fragment = new ActivationInformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public ActivationInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if (isFacebook) {
            view = inflater.inflate(R.layout.fragment_activiation_facebook, container, false);
        }
        else {
            view = inflater.inflate(R.layout.fragment_activation_information, container, false);
        }

        txtMobile = (EditText) view.findViewById(R.id.txt_mobile);

        if (!isFacebook) {
            btnFemale = (Button) view.findViewById(R.id.btn_female);
            btnMale = (Button) view.findViewById(R.id.btn_male);
            txtBirthday = (EditText) view.findViewById(R.id.txt_birth_date);
            txtFirstName = (EditText) view.findViewById(R.id.txt_full_name);
            txtPassword = (EditText) view.findViewById(R.id.txt_password);
            txtRepeatPassword = (EditText) view.findViewById(R.id.txt_repeat_password);
            txtUsername = (EditText) view.findViewById(R.id.txt_username);
            txtAddress = (EditText) view.findViewById(R.id.txt_address);
            txtCity = (EditText) view.findViewById(R.id.txt_city);
            txtCountry = (EditText) view.findViewById(R.id.txt_country);
            txtEducation = (EditText) view.findViewById(R.id.txt_education);
            txtPhone = (EditText) view.findViewById(R.id.txt_phone);
            txtWork = (EditText) view.findViewById(R.id.txt_work);
            txtId = (EditText) view.findViewById(R.id.txt_id);

            txtBirthday.setOnClickListener(this);
            txtBirthday.setClickable(false);
            txtBirthday.setFocusable(false);
            txtBirthday.setFocusableInTouchMode(false);

            txtCountry.setOnClickListener(this);
            txtCountry.setClickable(false);
            txtCountry.setFocusable(false);
            txtCountry.setFocusableInTouchMode(false);

            txtCity.setOnClickListener(this);
            txtCity.setClickable(false);
            txtCity.setFocusable(false);
            txtCity.setFocusableInTouchMode(false);

            btnMale.setOnClickListener(this);
            btnFemale.setOnClickListener(this);

            citiesAsyncTask = new CitiesAsyncTask(this, new ArrayList<ServiceRequest>()
                    , Params.SERVICE_PROCESS_1);
            citiesAsyncTask.execute(getActivity());

            countriesAsyncTask = new CountryAsyncTask(this, new ArrayList<ServiceRequest>()
                    , Params.SERVICE_PROCESS_2);
            countriesAsyncTask.execute(getActivity());
        }

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction("","");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (citiesAsyncTask != null && citiesAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            citiesAsyncTask.cancel(true);
        }
        if (countriesAsyncTask != null && countriesAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            countriesAsyncTask.cancel(true);
        }
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_male){
            if(this.gender!=Params.GENDER_MALE){
                btnMale.setBackgroundColor(getResources().getColor(R.color.gender_bg));
                btnFemale.setBackgroundColor(getResources().getColor(R.color.list_sliding_item));
                btnFemale.setTextColor(getResources().getColor(R.color.black));
                btnMale.setTextColor(getResources().getColor(R.color.list_sliding_item));
                this.gender=Params.GENDER_MALE;
            }
        }else if(view.getId()==R.id.btn_female){
            if(this.gender!=Params.GENDER_FEMALE){
                btnFemale.setBackgroundColor(getResources().getColor(R.color.gender_bg));
                btnMale.setBackgroundColor(getResources().getColor(R.color.list_sliding_item));
                btnFemale.setTextColor(getResources().getColor(R.color.list_sliding_item));
                btnMale.setTextColor(getResources().getColor(R.color.black));
                this.gender=Params.GENDER_FEMALE;
            }
        }
        else if(view.getId()==R.id.txt_birth_date) {
            DatePickerFragment newFragment = new DatePickerFragment();
            newFragment.setArgs(this.listener, 0, getBirthdayDate());
            newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
        } else if(view.getId()==R.id.txt_country){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select Imput Type");

            AlertDialog levelDialog = null;


            builder.setSingleChoiceItems(this.countriesArray,countryIndex,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {

                            countryIndex=item;
                            City cityBo=countriesList.get(item);
                            String city=cityBo.getName();
                            txtCountry.setText(city);
                            selectedCountry=cityBo.getId();
                            dialog.dismiss();
                        }
                    }).setTitle(getResources().getString(R.string.select_country));
            levelDialog = builder.create();
            levelDialog.show();
        } else if(view.getId()==R.id.txt_city){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select Imput Type");

            AlertDialog levelDialog = null;

            builder.setSingleChoiceItems(this.citiesArray,cityIndex,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {

                            cityIndex=item;
                            City cityBo=citiesList.get(item);
                            String city=cityBo.getName();
                            txtCity.setText(city);
                            selectedCity=cityBo.getId();
                            dialog.dismiss();
                        }
                    }).setTitle(getResources().getString(R.string.select_city));
            levelDialog = builder.create();
            levelDialog.show();
        }

    }

    public PersonalInfo getData(){
        PersonalInfo bean=new PersonalInfo();
        bean.setPassword(txtPassword.getText().toString());
        bean.setBirthday(txtBirthday.getText().toString());
        bean.setFullName(txtFirstName.getText().toString());
//        bean.setLastName(txtLastName.getText().toString());
        bean.setUsername(txtUsername.getText().toString());
        bean.setMobile(txtMobile.getText().toString());
        bean.setAddress(txtAddress.getText().toString());
        bean.setCity(this.selectedCity);
        bean.setCountry(this.selectedCountry);
        bean.setEducation(txtEducation.getText().toString());
        bean.setIdNum(txtId.getText().toString());
        bean.setPhone(txtPhone.getText().toString());
        bean.setWork(txtWork.getText().toString());

        String gender="M";
        if(this.gender==Params.GENDER_FEMALE)
            gender="F";
        bean.setGender(gender);
        bean.setBirthdayDate(getBirthdayDate());
        return bean;
    }

    public Date getBirthdayDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy");
        Date convertedStartDate = new Date();
        try {
            convertedStartDate = dateFormat.parse(txtBirthday.getText().toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedStartDate;
    }


    public boolean isValidInput(){
        if (InputValidator.isNotEmpty(txtFirstName, getString(R.string.first_name_empty))
//               && InputValidator.isNotEmpty(txtLastName, getString(R.string.last_name_empty))
                && emailValidator(txtUsername.getText().toString())
                && InputValidator.isNotEmpty(txtUsername, getString(R.string.username_not_valid))
                && InputValidator.isNotEmpty(txtPassword, getString(R.string.password_not_valid))
                && InputValidator.isPassNotEqualsEmpty(txtPassword
                , txtRepeatPassword, getString(R.string.password_not_equal_repeat_pass))
                ){
            return true;
        }
        return false;
    }

    private boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        if (!matcher.matches())
            txtUsername.setText("");

        return true;
    }

    @Override
    public void onServiceSuccess(Object b, int processType) {
        if (getActivity() == null) {
            Log.e(this.getClass().getName(), "Activity is null, avoid callback");
            return;
        }
        if(processType==Params.SERVICE_PROCESS_1){
            ArrayList<City> list= (ArrayList<City>) b;
            this.citiesList=list;
            this.citiesArray = new String[list.size()];
            int index = 0;
            for (City bean : list) {
                citiesArray[index] = bean.getName();
                index++;
            }

            if (list.size() > 0 && (selectedCity == null || selectedCity.isEmpty())) {
                cityIndex = 0;
                selectedCity = list.get(0).getId();
                txtCity.setText(list.get(0).getName());
            }
        }else if(processType==Params.SERVICE_PROCESS_2){
            ArrayList<City> list= (ArrayList<City>) b;
            this.countriesList=list;
            this.countriesArray = new String[list.size()];
            int index = 0;
            for (City bean : list) {
                countriesArray[index] = bean.getName();
                index++;
            }
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {

    }

    public void setIsFacebook(boolean isFacebook) {
        this.isFacebook = isFacebook;
    }

    private void hideOptions() {
        btnMale.setVisibility(View.INVISIBLE);
        btnFemale.setVisibility(View.INVISIBLE);
        txtFirstName.setVisibility(View.INVISIBLE);
        txtPassword.setVisibility(View.INVISIBLE);
        txtRepeatPassword.setVisibility(View.INVISIBLE);
        txtBirthday.setVisibility(View.INVISIBLE);
        txtUsername.setVisibility(View.INVISIBLE);
        txtCountry.setVisibility(View.INVISIBLE);
        txtCity.setVisibility(View.INVISIBLE);
        txtEducation.setVisibility(View.INVISIBLE);
        txtWork.setVisibility(View.INVISIBLE);
        txtAddress.setVisibility(View.INVISIBLE);
        txtPhone.setVisibility(View.INVISIBLE);
    }

    public PersonalInfo getFacebookData() {
        SharedPreferences pref = getActivity().getSharedPreferences(Params.APP_DATA, getActivity().MODE_PRIVATE);

        PersonalInfo bean=new PersonalInfo();
        bean.setPassword(pref.getString(Params.USER_INFO_ID, ""));
        bean.setBirthday("1/1/1975");
        bean.setFullName(pref.getString(Params.USER_INFO_FULL_NAME, ""));
        bean.setUsername(Utils.getFacebookUserName(pref.getString(Params.USER_INFO_ID, "")));

        bean.setMobile(txtMobile.getText().toString());
        bean.setAddress("");
        bean.setCity("7");
        bean.setCountry("1");
        bean.setIdNum("1");

        String fGender = pref.getString(Params.USER_INFO_GENDER, "Male");
        String gender = "F";
        if (fGender.equalsIgnoreCase("Male")) {
            gender = "M";
        }

        bean.setGender(gender);

        Calendar date = Calendar.getInstance();
        date.set(1975, 1, 1);
        bean.setBirthdayDate(date.getTime());
        return bean;
    }
}
