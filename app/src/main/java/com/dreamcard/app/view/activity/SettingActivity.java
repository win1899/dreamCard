package com.dreamcard.app.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamcard.app.R;
import com.dreamcard.app.components.TransparentProgressDialog;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.Categories;
import com.dreamcard.app.entity.City;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.GridItem;
import com.dreamcard.app.entity.ItemButton;
import com.dreamcard.app.entity.PersonalInfo;
import com.dreamcard.app.entity.RecordHolder;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.entity.UserInfo;
import com.dreamcard.app.services.AddRemoveInterestCatAsync;
import com.dreamcard.app.services.CategoriesAsync;
import com.dreamcard.app.services.CitiesAsyncTask;
import com.dreamcard.app.services.CountryAsyncTask;
import com.dreamcard.app.services.InterestCategoriesAsyncTask;
import com.dreamcard.app.services.LoginAsync;
import com.dreamcard.app.services.RegisterConsumerAsync;
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.adapters.CustomGridViewAdapterButton;
import com.dreamcard.app.view.adapters.PurchasesListAdapter;
import com.dreamcard.app.view.fragments.ActivationSettingFragment;
import com.dreamcard.app.view.fragments.DatePickerFragment;
import com.dreamcard.app.view.interfaces.IDatePickerListener;
import com.dreamcard.app.view.interfaces.IServiceListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SettingActivity extends FragmentActivity implements IServiceListener, View.OnClickListener, View.OnTouchListener {
    private GridView grid;
    private CustomGridViewAdapterButton customGridAdapter;
    private EditText txtFullName;
    private EditText txtGender;
    private TextView txtFullNamelbl;
    private TextView txtGenderLbl;
    private EditText txtPassword;
    private EditText txtBirthday;
    private TextView txtBirthdayLbl;
    private EditText txtMobile;
    private TextView txtMobileLbl;
    private EditText txtWork;
    private EditText txtEmail;
    private Button btnSave;
    private TextView txtUsername;
    private TextView txtUserId;
    private TextView txtCountry;
    private TextView txtCity;
    private EditText txtPhone;
    private EditText txtAddress;
    private EditText txtEducation;
    private EditText txtId;
    private TextView txtPhoneLbl;
    private TextView txtAddressLbl;
    private TextView txtEducationLbl;
    private TextView txtIdLbl;
    private TextView txtWorkLbl;
    private Button btnEdit;
    private TextView txtEmailLbl;
    private TextView txtPasswordLbl;
    private Button btnEditCategories;
    private Button btnDone;
    private ListView purchasesListView;
    private RelativeLayout noPurchasesPnl;

    private HashMap<Integer, RecordHolder> recordHolderList = new HashMap<Integer, RecordHolder>();
    private ArrayList<Categories> gridList = new ArrayList<Categories>();
    private RecordHolder holder = new RecordHolder();
    private Categories selectedCategory = new Categories();
    private ArrayList<City> citiesList = new ArrayList<City>();
    private City selectedCity = new City();
    private City selectedCountry = new City();
    private String userId;
    private ArrayList<City> countriesList = new ArrayList<City>();
    private String[] countryArray;
    private String[] cityArray;
    private int countryIndex;
    private int cityIndex;
    private ScrollView scroll;

    private TransparentProgressDialog progress;
    private Runnable runnable;
    private Handler handler;
    private ArrayList<GridItem> gridArray = new ArrayList<GridItem>();
    HashMap<String, String> selectedCategories = new HashMap();
    private int genderIndex;
    String selectedGender;

    private CategoriesAsync categoriesAsync;
    private CitiesAsyncTask citiesAsyncTask;
    private CountryAsyncTask countriesAsyncTask;
    private InterestCategoriesAsyncTask interestCategoriesAsync;
    private LoginAsync loginAsync;
    private AddRemoveInterestCatAsync async;
    private RegisterConsumerAsync registerConsumerAsync;
    private AddRemoveInterestCatAsync addRemoveInterestCatAsync;

    private ActivationSettingFragment.RecordListener recordListener = new ActivationSettingFragment.RecordListener() {
        @Override
        public void setHolder(RecordHolder holder, int position) {
            recordHolderList.put(position, holder);
        }
    };
    private IDatePickerListener listener = new IDatePickerListener() {
        @Override
        public void setDate(String date, int processId) {
            txtBirthday.setText(date);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        buildUI();
        loginAsync = new LoginAsync(this, ServicesConstants.getLoginRequestList(txtEmail.getText().toString()
                , txtPassword.getText().toString())
                , Params.SERVICE_PROCESS_6);
        loginAsync.execute(this);
    }

    private void buildUI() {
        grid = (GridView) findViewById(R.id.category_grid);

        handler = new Handler();
        progress = new TransparentProgressDialog(this, R.drawable.loading);
        runnable = new Runnable() {
            @Override
            public void run() {
                if (progress.isShowing()) {
                    progress.dismiss();
                }
            }
        };

        txtBirthday = (EditText) findViewById(R.id.txt_birth_date);
        txtFullName = (EditText) findViewById(R.id.txt_full_name);
        txtGender = (EditText) findViewById(R.id.txt_gender);
        txtFullNamelbl = (TextView) findViewById(R.id.txt_full_name_lbl);
        txtGenderLbl = (TextView) findViewById(R.id.txt_gender_lbl);
        txtPassword = (EditText) findViewById(R.id.txt_password);
        txtMobile = (EditText) findViewById(R.id.txt_mobile);
        txtMobileLbl = (TextView) findViewById(R.id.txt_mobile_lbl);
        txtWork = (EditText) findViewById(R.id.txt_work);
        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtBirthdayLbl = (TextView) findViewById(R.id.txt_birthday_lbl);
        txtCountry = (TextView) findViewById(R.id.txt_country);
        txtCity = (TextView) findViewById(R.id.txt_city);
        txtPhone = (EditText) findViewById(R.id.txt_phone);
        txtAddress = (EditText) findViewById(R.id.txt_address);
        txtEducation = (EditText) findViewById(R.id.txt_education);
        txtId = (EditText) findViewById(R.id.txt_id);
        txtPhoneLbl = (TextView) findViewById(R.id.txt_phone_lbl);
        txtAddressLbl = (TextView) findViewById(R.id.txt_address_lbl);
        txtEducationLbl = (TextView) findViewById(R.id.txt_education_lbl);
        txtIdLbl = (TextView) findViewById(R.id.txt_id_lbl);
        txtWorkLbl = (TextView) findViewById(R.id.txt_work_lbl);
        btnEdit = (Button) findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(this);
        txtEmailLbl = (TextView) findViewById(R.id.txt_email_lbl);
        txtPasswordLbl = (TextView) findViewById(R.id.txt_password_lbl);
        scroll = (ScrollView) findViewById(R.id.scroll);
        txtCity.setOnClickListener(this);
        txtCountry.setOnClickListener(this);

        txtBirthday.setOnTouchListener(this);
        txtBirthday.setClickable(false);
        txtBirthday.setFocusable(false);
        txtBirthday.setFocusableInTouchMode(false);

        txtGender.setOnClickListener(this);
        txtGender.setClickable(false);
        txtGender.setFocusable(false);
        txtGender.setFocusableInTouchMode(false);

        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

        btnEditCategories = (Button) findViewById(R.id.btn_edit_categories);
        btnEditCategories.setOnClickListener(this);
        btnDone = (Button) findViewById(R.id.btn_done);
        btnDone.setOnClickListener(this);

        txtUserId = (TextView) findViewById(R.id.txt_user_id);
        txtUsername = (TextView) findViewById(R.id.txt_username);

        purchasesListView = (ListView) findViewById(android.R.id.list);
        noPurchasesPnl = (RelativeLayout) findViewById(R.id.no_purchases_pnl);

        setData();

        categoriesAsync = new CategoriesAsync(this, new ArrayList<ServiceRequest>()
                , Params.SERVICE_PROCESS_1, Params.TYPE_ALL_CATEGORY);
        categoriesAsync.execute(this);

        progress.show();
        handler.postDelayed(runnable, 5000);

        citiesAsyncTask = new CitiesAsyncTask(this, new ArrayList<ServiceRequest>()
                , Params.SERVICE_PROCESS_7);
        citiesAsyncTask.execute(this);

        countriesAsyncTask = new CountryAsyncTask(this, new ArrayList<ServiceRequest>()
                , Params.SERVICE_PROCESS_8);
        countriesAsyncTask.execute(this);

    }

    @Override
    protected void onPause() {
        if (categoriesAsync != null && categoriesAsync.getStatus() == AsyncTask.Status.RUNNING) {
            categoriesAsync.cancel(true);
        }
        if (citiesAsyncTask != null && citiesAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            citiesAsyncTask.cancel(true);
        }
        if (countriesAsyncTask != null && countriesAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            countriesAsyncTask.cancel(true);
        }
        if (interestCategoriesAsync != null && interestCategoriesAsync.getStatus() == AsyncTask.Status.RUNNING) {
            interestCategoriesAsync.cancel(true);
        }
        if (loginAsync != null && loginAsync.getStatus() == AsyncTask.Status.RUNNING) {
            loginAsync.cancel(true);
        }
        if (async != null && async.getStatus() == AsyncTask.Status.RUNNING) {
            async.cancel(true);
        }
        if (registerConsumerAsync != null && registerConsumerAsync.getStatus() == AsyncTask.Status.RUNNING) {
            registerConsumerAsync.cancel(true);
        }
        if (addRemoveInterestCatAsync != null && addRemoveInterestCatAsync.getStatus() == AsyncTask.Status.RUNNING) {
            addRemoveInterestCatAsync.cancel(true);
        }

        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setData() {
        SharedPreferences prefs = getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
        String id = prefs.getString(Params.USER_INFO_EMAIL, "");
        String password = prefs.getString(Params.USER_INFO_PASSWORD, "");
        String birthday = prefs.getString(Params.USER_INFO_BIRTHDAY, "");
        String gender = prefs.getString(Params.USER_INFO_GENDER, "");
        String name = Utils.getUserName(this);
        String cardNum = prefs.getString(Params.USER_INFO_CARD_NUMBER, "");

        txtUsername.setText(name);
        txtUserId.setText(cardNum);


        userId = prefs.getString(Params.USER_INFO_ID, "");

        if (prefs.getString(Params.USER_INFO_WORK, "") != null &&
                !prefs.getString(Params.USER_INFO_WORK, "").equalsIgnoreCase("null")) {
            txtWork.setText(prefs.getString(Params.USER_INFO_WORK, ""));
            txtWorkLbl.setText(prefs.getString(Params.USER_INFO_WORK, ""));
        }

        if (prefs.getString(Params.USER_INFO_FIRST_NAME, "") != null &&
                !prefs.getString(Params.USER_INFO_FIRST_NAME, "").equalsIgnoreCase("null")) {
            txtFullName.setText(prefs.getString(Params.USER_INFO_FIRST_NAME, ""));
            txtFullNamelbl.setText(prefs.getString(Params.USER_INFO_FIRST_NAME, ""));
        }

        if (prefs.getString(Params.USER_INFO_FULL_NAME, "") != null &&
                !prefs.getString(Params.USER_INFO_FULL_NAME, "").equalsIgnoreCase("null")) {
            txtFullName.setText(prefs.getString(Params.USER_INFO_FULL_NAME, ""));
            txtFullNamelbl.setText(prefs.getString(Params.USER_INFO_FULL_NAME, ""));
        }

        if (prefs.getString(Params.USER_INFO_MOBILE, "") != null &&
                !prefs.getString(Params.USER_INFO_MOBILE, "").equalsIgnoreCase("null")) {
            txtMobile.setText(prefs.getString(Params.USER_INFO_MOBILE, ""));
            txtMobileLbl.setText(prefs.getString(Params.USER_INFO_MOBILE, ""));
        }
        String city = prefs.getString(Params.USER_INFO_CITY, "");
        if (city != null && !city.equalsIgnoreCase("null")) {
            int position = 0;
            for (City bean : this.citiesList) {
                if (bean.getId().equalsIgnoreCase(city)) {
                    selectedCity = bean;
                    break;
                }
                position++;
            }
            txtCity.setText(selectedCity.getName());
        }

        txtPassword.setText(password);
        txtEmail.setText(id);
        txtEmailLbl.setText(id);

        if (birthday != null && birthday.length() > 0) {
            try {
                Date date = new Date(Long.parseLong(birthday.replaceAll(".*?(\\d+).*", "$1")));
                android.text.format.DateFormat df = new android.text.format.DateFormat();
                Calendar c = Calendar.getInstance();
                c.setTime(date);

                String x = df.format("dd/MM/yyyy", c.getTime()).toString();
                txtBirthday.setText(x);
                txtBirthdayLbl.setText(x);
            } catch (Exception e) {

            }
        }

        if (gender != null) {
            if (gender.equalsIgnoreCase("F")) {
                txtGender.setText("Female");
                txtGenderLbl.setText("Female");
            } else {
                txtGender.setText("Male");
                txtGenderLbl.setText("Male");
            }

            this.selectedGender = gender;
        }

        if (prefs.getString(Params.USER_INFO_ADDRESS, "") != null &&
                !prefs.getString(Params.USER_INFO_ADDRESS, "").equalsIgnoreCase("null")) {
            txtAddress.setText(prefs.getString(Params.USER_INFO_ADDRESS, ""));
            txtAddressLbl.setText(prefs.getString(Params.USER_INFO_ADDRESS, ""));
        }

        if (prefs.getString(Params.USER_INFO_ID_NUM, "") != null &&
                !prefs.getString(Params.USER_INFO_ID_NUM, "").equalsIgnoreCase("null")) {
            txtId.setText(prefs.getString(Params.USER_INFO_ID_NUM, ""));
            txtIdLbl.setText(prefs.getString(Params.USER_INFO_ID_NUM, ""));
        }

        if (prefs.getString(Params.USER_INFO_EDUCATION, "") != null &&
                !prefs.getString(Params.USER_INFO_EDUCATION, "").equalsIgnoreCase("null")) {
            txtEducation.setText(prefs.getString(Params.USER_INFO_EDUCATION, ""));
            txtEducationLbl.setText(prefs.getString(Params.USER_INFO_EDUCATION, ""));
        }

        if (prefs.getString(Params.USER_INFO_PHONE, "") != null &&
                !prefs.getString(Params.USER_INFO_PHONE, "").equalsIgnoreCase("null")) {
            txtPhone.setText(prefs.getString(Params.USER_INFO_PHONE, ""));
            txtPhoneLbl.setText(prefs.getString(Params.USER_INFO_PHONE, ""));
        }


        if (Params.DISCOUNT_LIST != null) {
            if (Params.DISCOUNT_LIST.size() == 0) {
                purchasesListView.setVisibility(View.GONE);
                noPurchasesPnl.setVisibility(View.VISIBLE);
            } else {
                PurchasesListAdapter adapter = new PurchasesListAdapter(this, Params.DISCOUNT_LIST);
                purchasesListView.setAdapter(adapter);

                int totalHeight = purchasesListView.getPaddingTop() + purchasesListView.getPaddingBottom();
                for (int i = 0; i < adapter.getCount(); i++) {
                    View listItem = adapter.getView(i, null, purchasesListView);
                    if (listItem instanceof ViewGroup) {
                        listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }

                ViewGroup.LayoutParams params = purchasesListView.getLayoutParams();
                params.height = totalHeight + (purchasesListView.getDividerHeight() * (adapter.getCount() - 1));
                purchasesListView.setLayoutParams(params);
            }
        }
    }

    public PersonalInfo getData() {
        PersonalInfo bean = new PersonalInfo();
        bean.setBirthday(txtBirthday.getText().toString());
        bean.setFullName(txtFullName.getText().toString());

        String[] fullName = txtFullName.getText().toString().split(" ");
        if (fullName.length > 0) {
            bean.setFirstName(fullName[0]);
        }
        if (fullName.length > 1) {
            bean.setLastName(fullName[1]);
        }
        bean.setMobile(txtMobile.getText().toString());
        bean.setPassword(txtPassword.getText().toString());
        bean.setUsername(txtEmail.getText().toString());
        if (this.selectedCity != null) {
            bean.setCity(this.selectedCity.getId());
        }
        bean.setGender(this.selectedGender);
        bean.setWork(txtWork.getText().toString());
        bean.setAddress(txtAddress.getText().toString());
        bean.setPhone(txtPhone.getText().toString());
        bean.setEducation(txtEducation.getText().toString());
        bean.setIdNum(txtId.getText().toString());
        if (this.selectedCountry != null) {
            bean.setCountry(this.selectedCountry.getId());
        }
        bean.setCardNumber(txtUserId.getText().toString());

        SharedPreferences prefs = getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
        bean.setId(prefs.getString(Params.USER_INFO_ID, null));

        return bean;

    }

    public void onServiceSuccess(Object b, int processType) {
        if (processType == Params.SERVICE_PROCESS_1) {
            ArrayList<Categories> list = (ArrayList<Categories>) b;
            this.gridList = list;


            interestCategoriesAsync = new InterestCategoriesAsyncTask(this
                    , ServicesConstants.getInterestCatRequestList(userId), Params.SERVICE_PROCESS_5);
            interestCategoriesAsync.execute(this);
        } else if (processType == Params.SERVICE_PROCESS_2) {
            progress.dismiss();
            holder.getImgSelected().setVisibility(View.GONE);
            holder.getPnl().setBackground(getResources().getDrawable(R.drawable.category_btn_bg));
            holder.getPnl2().setBackground(getResources().getDrawable(R.drawable.category_btn_bg));
            this.selectedCategory.setSelected(false);
            this.selectedCategories.remove(this.selectedCategory.getId());
        } else if (processType == Params.SERVICE_PROCESS_3) {
            progress.dismiss();
            holder.getPnl().setBackground(getResources().getDrawable(R.drawable.category_btn_bg_press));
            holder.getPnl2().setBackground(getResources().getDrawable(R.drawable.category_btn_bg_press));
            holder.getImgSelected().setVisibility(View.VISIBLE);
            this.selectedCategory.setSelected(true);
            this.selectedCategories.put(this.selectedCategory.getId(), this.selectedCategory.getTitle());
        } else if (processType == Params.SERVICE_PROCESS_4) {
            loginAsync = new LoginAsync(this, ServicesConstants.getLoginRequestList(txtEmail.getText().toString()
                    , txtPassword.getText().toString())
                    , Params.SERVICE_PROCESS_6);
            loginAsync.execute(this);
            Toast.makeText(this, getResources().getString(R.string.information_save_successfully), Toast.LENGTH_LONG).show();

        } else if (processType == Params.SERVICE_PROCESS_5) {
            HashMap<String, String> map = (HashMap<String, String>) b;
            this.selectedCategories = map;
            ArrayList<GridItem> beforeEditArray = new ArrayList<GridItem>();
            for (Categories trans : this.gridList) {
                boolean isSelected = false;
                if (map.get(trans.getId()) != null) {
                    isSelected = true;
                    trans.setSelected(true);

                    beforeEditArray.add(new ItemButton(trans.getLogo(), trans.getTitle(), trans.getId()
                            , Integer.parseInt(trans.getId()), false));
                }
                gridArray.add(new ItemButton(trans.getLogo(), trans.getTitle(), trans.getId()
                        , Integer.parseInt(trans.getId()), isSelected));
            }
            customGridAdapter = new CustomGridViewAdapterButton(this, R.layout.category_grid_button
                    , beforeEditArray, this, this.recordListener);
            grid.setAdapter(customGridAdapter);

            scroll.fullScroll(ScrollView.FOCUS_UP);

        } else if (processType == Params.SERVICE_PROCESS_6) {

            if (b != null) {
                UserInfo bean = (UserInfo) b;
                SharedPreferences pref = getSharedPreferences(Params.APP_DATA, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(Params.USER_INFO_ID, bean.getId());
                editor.putString(Params.USER_INFO_EMAIL, txtEmail.getText().toString());
                editor.putString(Params.USER_INFO_NAME, bean.getFullName());
                editor.putString(Params.USER_INFO_PASSWORD, txtPassword.getText().toString());
                editor.putString(Params.USER_INFO_FIRST_NAME, bean.getFirstName());
                editor.putString(Params.USER_INFO_LAST_NAME, bean.getLastName());
                editor.putString(Params.USER_INFO_MOBILE, bean.getMobile());
                editor.putString(Params.USER_INFO_GENDER, bean.getGender());
                editor.putString(Params.USER_INFO_WORK, bean.getWork());
                editor.putString(Params.USER_INFO_BIRTHDAY, bean.getBirthday());
                editor.putString(Params.USER_INFO_CITY, bean.getCity());
                editor.putString(Params.USER_INFO_COUNTRY, bean.getCountry());
                editor.putString(Params.USER_INFO_PHONE, bean.getPhone());
                editor.putString(Params.USER_INFO_ID_NUM, bean.getIdNum());
                editor.putString(Params.USER_INFO_ADDRESS, bean.getAddress());
                editor.putString(Params.USER_INFO_EDUCATION, bean.getEducation());
                editor.putString(Params.USER_INFO_FULL_NAME, bean.getFullName());
                editor.putString(Params.USER_INFO_CARD_NUMBER, bean.getCardNumber());
                editor.apply();

                setData();
            }
            progress.dismiss();
            changeMode(Params.MODE_ADD);
        } else if (processType == Params.SERVICE_PROCESS_7) {
            progress.dismiss();
            ArrayList<City> list = (ArrayList<City>) b;
            this.citiesList = list;
            String[] data = new String[list.size()];
            int index = 0;

            SharedPreferences prefs = getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
            String city = prefs.getString(Params.USER_INFO_CITY, "");

            for (City bean : list) {
                data[index] = bean.getName();

                //Set user city
                if (city != null && !city.equalsIgnoreCase("null")) {
                    if (bean.getId().equalsIgnoreCase(city)) {
                        selectedCity = bean;
                        txtCity.setText(bean.getName());
                        this.cityIndex = index;
                    }
                }
                index++;
            }
            cityArray = data;

            String userCity = prefs.getString(Params.USER_INFO_CITY, "");
            if (userCity != null && !userCity.equalsIgnoreCase("null")) {
                int position = 0;
                for (City bean : this.citiesList) {
                    if (bean.getId().equalsIgnoreCase(userCity)) {
                        selectedCity = bean;
                        break;
                    }
                    position++;
                }
                txtCity.setText(selectedCity.getName());
            }
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item
//                    , data);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            cmbCity.setAdapter(adapter);
        } else if (processType == Params.SERVICE_PROCESS_8) {
            ArrayList<City> list = (ArrayList<City>) b;
            this.countriesList = list;
            this.countryArray = new String[list.size()];
            int index = 0;

            SharedPreferences prefs = getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
            String country = prefs.getString(Params.USER_INFO_COUNTRY, "");

            for (City bean : list) {
                countryArray[index] = bean.getName();

                //Set user country
                if (country != null && !country.equalsIgnoreCase("null")) {
                    if (bean.getId().equalsIgnoreCase(country)) {
                        selectedCountry = bean;
                        txtCountry.setText(bean.getName());
                        this.countryIndex = index;
                    }
                }
                index++;
            }
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {
        progress.dismiss();
        Toast.makeText(this, info.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save) {
            saveData();
        }
        if (view.getId() == R.id.txt_country) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Imput Type");

            AlertDialog levelDialog = null;


            builder.setSingleChoiceItems(this.countryArray, countryIndex,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {

                            countryIndex = item;
                            City cityBo = countriesList.get(item);
                            String city = cityBo.getName();
                            txtCountry.setText(city);
                            selectedCountry = cityBo;
                            dialog.dismiss();
                        }
                    }).setTitle(getResources().getString(R.string.select_country));
            levelDialog = builder.create();
            levelDialog.show();
        } else if (view.getId() == R.id.txt_city) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Imput Type");

            AlertDialog levelDialog = null;


            builder.setSingleChoiceItems(this.cityArray, cityIndex,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {

                            cityIndex = item;
                            City cityBo = citiesList.get(item);
                            String city = cityBo.getName();
                            txtCity.setText(city);
                            selectedCity = cityBo;
                            dialog.dismiss();
                        }
                    }).setTitle(getResources().getString(R.string.select_city));
            levelDialog = builder.create();
            levelDialog.show();
        } else if (view.getId() == R.id.btn_edit) {
            changeMode(Params.MODE_EDIT);

        } else if (view.getId() == R.id.btn_edit_categories) {
            editCategories();
        } else if (view.getId() == R.id.btn_done) {
            doneCategories();
        } else if (view.getId() == R.id.txt_gender) {
            showGender();
        } else {
            Categories data = null;
            int x = view.getId();
            for (Categories bean : this.gridList) {
                if (Integer.parseInt(bean.getId()) == view.getId()) {
                    data = bean;
                    break;
                }
            }
            if (data != null) {
                RecordHolder holder = this.recordHolderList.get(Integer.parseInt(data.getId()));
                if (data.isSelected()) {
                    this.holder = holder;
                    this.selectedCategory = data;

                    progress.show();
                    handler.postDelayed(runnable, 5000);

                    async = new AddRemoveInterestCatAsync(this
                            , ServicesConstants.getAddRemoveCategoryRequestList(data.getId(), userId)
                            , Params.SERVICE_PROCESS_2, Params.TYPE_REMOVE_CATEGORY);
                    async.execute(this);
                } else {
                    this.holder = holder;
                    this.selectedCategory = data;

                    progress.show();
                    handler.postDelayed(runnable, 5000);

                    addRemoveInterestCatAsync = new AddRemoveInterestCatAsync(this
                            , ServicesConstants.getAddRemoveCategoryRequestList(data.getId(), userId)
                            , Params.SERVICE_PROCESS_3, Params.TYPE_ADD_CATEGORY);
                    addRemoveInterestCatAsync.execute(this);
                }
            }
        }
    }

    private Date getBirthdayDate(){
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


    private void showGender() {


        final List<String> genderList = new ArrayList<String>();
        genderList.add("Male");
        genderList.add("Female");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Imput Type");

        AlertDialog levelDialog = null;


        builder.setSingleChoiceItems(genderList.toArray(new String[genderList.size()]), genderIndex,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        genderIndex = item;
                        String gender = genderList.get(item);
                        if (gender.equalsIgnoreCase("Male")) {
                            selectedGender = "M";
                        } else {
                            selectedGender = "F";
                        }
                        txtGender.setText(gender);
                        dialog.dismiss();
                    }
                }).setTitle(getResources().getString(R.string.select_disc_rate));
        levelDialog = builder.create();
        levelDialog.show();
    }

    private void doneCategories() {
        ArrayList<GridItem> beforeEditArray = new ArrayList<GridItem>();
        for (Categories trans : this.gridList) {
            if (this.selectedCategories.get(trans.getId()) != null) {
                trans.setSelected(true);
                beforeEditArray.add(new ItemButton(trans.getLogo(), trans.getTitle(), trans.getId()
                        , Integer.parseInt(trans.getId()), false));
            }
        }
        customGridAdapter = new CustomGridViewAdapterButton(this, R.layout.category_grid_button
                , beforeEditArray, this, this.recordListener);
        grid.setAdapter(customGridAdapter);

        btnEditCategories.setVisibility(View.VISIBLE);
        btnDone.setVisibility(View.GONE);
    }

    private void editCategories() {
        gridArray = new ArrayList<GridItem>();
        for (Categories trans : this.gridList) {
            boolean isSelected = false;
            if (this.selectedCategories.get(trans.getId()) != null) {
                isSelected = true;
                trans.setSelected(true);
            }
            gridArray.add(new ItemButton(trans.getLogo(), trans.getTitle(), trans.getId()
                    , Integer.parseInt(trans.getId()), isSelected));
        }

        customGridAdapter = new CustomGridViewAdapterButton(this, R.layout.category_grid_button
                , gridArray, this, this.recordListener);
        grid.setAdapter(customGridAdapter);

        btnEditCategories.setVisibility(View.GONE);
        btnDone.setVisibility(View.VISIBLE);
    }

    private void changeMode(int mode) {
        if (mode == Params.MODE_EDIT) {
            btnEdit.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
            txtFullNamelbl.setVisibility(View.GONE);
            txtGenderLbl.setVisibility(View.GONE);
            txtBirthdayLbl.setVisibility(View.GONE);
            txtMobileLbl.setVisibility(View.GONE);
            txtPhoneLbl.setVisibility(View.GONE);
            txtIdLbl.setVisibility(View.GONE);
            txtEducationLbl.setVisibility(View.GONE);
            txtWorkLbl.setVisibility(View.GONE);
            txtAddressLbl.setVisibility(View.GONE);
            txtEmailLbl.setVisibility(View.GONE);
            txtPasswordLbl.setVisibility(View.GONE);

            txtFullName.setVisibility(View.VISIBLE);
            txtGender.setVisibility(View.VISIBLE);
            txtBirthday.setVisibility(View.VISIBLE);
            txtMobile.setVisibility(View.VISIBLE);
            txtPhone.setVisibility(View.VISIBLE);
            txtId.setVisibility(View.VISIBLE);
            txtEducation.setVisibility(View.VISIBLE);
            txtWork.setVisibility(View.VISIBLE);
            txtAddress.setVisibility(View.VISIBLE);
            txtEmail.setVisibility(View.VISIBLE);
            txtPassword.setVisibility(View.VISIBLE);
        } else if (mode == Params.MODE_ADD) {
            btnEdit.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);
            txtFullNamelbl.setVisibility(View.VISIBLE);
            txtGenderLbl.setVisibility(View.VISIBLE);
            txtBirthdayLbl.setVisibility(View.VISIBLE);
            txtMobileLbl.setVisibility(View.VISIBLE);
            txtPhoneLbl.setVisibility(View.VISIBLE);
            txtIdLbl.setVisibility(View.VISIBLE);
            txtEducationLbl.setVisibility(View.VISIBLE);
            txtWorkLbl.setVisibility(View.VISIBLE);
            txtAddressLbl.setVisibility(View.VISIBLE);
            txtEmailLbl.setVisibility(View.VISIBLE);
            txtPasswordLbl.setVisibility(View.VISIBLE);

            txtFullName.setVisibility(View.GONE);
            txtGender.setVisibility(View.GONE);
            txtBirthday.setVisibility(View.GONE);
            txtMobile.setVisibility(View.GONE);
            txtPhone.setVisibility(View.GONE);
            txtId.setVisibility(View.GONE);
            txtEducation.setVisibility(View.GONE);
            txtWork.setVisibility(View.GONE);
            txtAddress.setVisibility(View.GONE);
            txtEmail.setVisibility(View.GONE);
            txtPassword.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            DatePickerFragment newFragment = new DatePickerFragment();
            newFragment.setArgs(this.listener, 0, getBirthdayDate());
            newFragment.show(getSupportFragmentManager(), "datePicker");
        }
        return false;
    }

    private void saveData() {
        progress.show();
        handler.postDelayed(runnable, 5000);
        registerConsumerAsync = new RegisterConsumerAsync(this
                , ServicesConstants.getActivationInformationList(getData())
                , Params.SERVICE_PROCESS_4);
        registerConsumerAsync.execute(this);
    }
}
