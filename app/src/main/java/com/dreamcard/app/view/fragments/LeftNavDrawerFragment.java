package com.dreamcard.app.view.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Criteria;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dreamcard.app.R;
import com.dreamcard.app.common.DatabaseController;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.Categories;
import com.dreamcard.app.entity.City;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.RecordHolder;
import com.dreamcard.app.entity.SearchCriteria;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.services.CategoriesAsync;
import com.dreamcard.app.services.CitiesAsyncTask;
import com.dreamcard.app.services.InterestCategoriesAsyncTask;
import com.dreamcard.app.view.adapters.CommentsAdapter;
import com.dreamcard.app.view.adapters.FilterCategoryAdapter;
import com.dreamcard.app.view.interfaces.IServiceListener;
import com.dreamcard.app.view.interfaces.OnFragmentInteractionListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LeftNavDrawerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeftNavDrawerFragment extends Fragment implements View.OnClickListener, IServiceListener, AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;
    private TextView txtCity;
    private TextView txtDiscRate;
    private ListView categoriesList;
    private RatingBar ratingBar;
    private String cityId;
    int imputSelection = 0;
    int discountIndex = 0;
    private HashMap<Integer, RecordHolder> recordHolderList = new HashMap<Integer, RecordHolder>();
    private ArrayList<Categories> categories;
    private ArrayList<String> discountList = new ArrayList<String>();

    private ActivationSettingFragment.RecordListener recordListener = new ActivationSettingFragment.RecordListener() {
        @Override
        public void setHolder(RecordHolder holder, int position) {
            recordHolderList.put(position, holder);
        }
    };

    private String[] citiesArray;
    private ArrayList<City> citiesList = new ArrayList<City>();
    private Activity activity;
    private SearchCriteria criteria = null;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeftNavDrawerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeftNavDrawerFragment newInstance(String param1, String param2) {
        LeftNavDrawerFragment fragment = new LeftNavDrawerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LeftNavDrawerFragment() {
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
        View view = inflater.inflate(R.layout.fragment_left_nav_drawer, container, false);
        ImageView btnCity = (ImageView) view.findViewById(R.id.img_city_btn);
        ImageView btnDiscRate = (ImageView) view.findViewById(R.id.img_discount_rate_btn);
        Button btnSave = (Button) view.findViewById(R.id.btn_save);
        Button btnClear = (Button) view.findViewById(R.id.btn_clear);
        categoriesList = (ListView) view.findViewById(android.R.id.list);
        categoriesList.setOnItemClickListener(this);

        categoriesList.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        btnSave.setOnClickListener(this);
        btnCity.setOnClickListener(this);
        btnDiscRate.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        txtCity = (TextView) view.findViewById(R.id.txt_city);
        txtDiscRate = (TextView) view.findViewById(R.id.txt_discount_rate);
        txtCity.setOnClickListener(this);
        txtDiscRate.setOnClickListener(this);
        ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);

        CitiesAsyncTask citiesAsyncTask = new CitiesAsyncTask(this, new ArrayList<ServiceRequest>()
                , Params.SERVICE_PROCESS_1);
        citiesAsyncTask.execute(this.activity);

        CategoriesAsync categoriesAsync = new CategoriesAsync(this, new ArrayList<ServiceRequest>()
                , Params.SERVICE_PROCESS_2, Params.TYPE_ALL_CATEGORY);
        categoriesAsync.execute(this.activity);

        discountList.add("0% - 10%");
        discountList.add("10% - 20%");
        discountList.add("20% - 30%");
        discountList.add("30% - 40%");
        discountList.add("40% - 50%");
        discountList.add("50% - 60%");
        discountList.add("60% - 70%");
        discountList.add("70% - 80%");
        discountList.add("80% - 90%");

        setData();

        return view;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

//        setData();

    }

    private void setData() {
        SearchCriteria criteria = DatabaseController.getInstance(this.activity).getCriteria();
        if (criteria != null) {
            this.criteria = criteria;
            if (criteria.getCityId() != null) {
                this.cityId = criteria.getCityId();
                this.imputSelection = Integer.parseInt(criteria.getCityId());
            }
            if (criteria.getCityName() != null) {
                txtCity.setText(criteria.getCityName());
            }
            if (criteria.getDiscRate() != null) {
                txtDiscRate.setText(criteria.getDiscRate());
            }
            if (criteria.getRate() != null) {
                ratingBar.setRating(criteria.getRate());
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity = activity;
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_save) {
            saveData();
        } else if (view.getId() == R.id.img_city_btn || view.getId() == R.id.txt_city) {
            showCityDialog();
        } else if (view.getId() == R.id.img_discount_rate_btn || view.getId() == R.id.txt_discount_rate) {
            showDiscountDialog();
        } else if (view.getId() == R.id.btn_clear) {
            clearCriteria();

            if (mDrawerLayout != null) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            }

            this.mListener.doAction(criteria, Params.FRAGMENT_LEFT_DRAWER);
        }
    }

    private void clearCriteria() {
        DatabaseController.getInstance(this.activity).deleteCriteria();
        this.criteria = null;
        this.cityId = null;
        txtCity.setText(getString(R.string.select_city));
        txtDiscRate.setText(getString(R.string.select_disc_rate));
        ratingBar.setRating(0);

        for (Categories category : this.categories) {
            RecordHolder holder = recordHolderList.get(Integer.parseInt(category.getId()));
            if (holder != null) {
                holder.getImageItem().setVisibility(View.GONE);
            }
            category.setSelected(false);
        }
    }

    private void showDiscountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        builder.setTitle("Select Imput Type");

        AlertDialog levelDialog = null;


        builder.setSingleChoiceItems(discountList.toArray(new String[discountList.size()]), discountIndex,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        discountIndex = item;
                        String dicount = discountList.get(item);
                        txtDiscRate.setText(dicount);
                        dialog.dismiss();
                    }
                }).setTitle(getResources().getString(R.string.select_disc_rate));
        levelDialog = builder.create();
        levelDialog.show();
    }

    private void saveData() {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setRate((int) ratingBar.getRating());
        criteria.setDiscRate(txtDiscRate.getText().toString());
        criteria.setCityId(this.cityId);
        criteria.setCityName(txtCity.getText().toString());
        criteria.setCategories(getSelectedCategories());

        DatabaseController.getInstance(this.activity).saveCriteria(criteria);

        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }

        this.mListener.doAction(criteria, Params.FRAGMENT_LEFT_DRAWER);
    }

    private String getSelectedCategories() {
        String selected = null;
        for (Categories category : this.categories) {
            if (category.isSelected()) {
                if (selected == null) {
                    selected = category.getId();
                } else {
                    selected += "," + category.getId();
                }
            }
        }
        return selected;
    }

    private void showCityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        builder.setTitle("Select Imput Type");

        AlertDialog levelDialog = null;


        builder.setSingleChoiceItems(this.citiesArray, imputSelection,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        imputSelection = item;
                        City cityBo = citiesList.get(item);
                        String city = cityBo.getName();
                        txtCity.setText(city);
                        cityId = cityBo.getId();
                        dialog.dismiss();
                    }
                }).setTitle(getResources().getString(R.string.select_city));
        levelDialog = builder.create();
        levelDialog.show();
    }

    @Override
    public void onServiceSuccess(Object b, int processType) {
        if (processType == Params.SERVICE_PROCESS_1) {
            ArrayList<City> list = (ArrayList<City>) b;
            this.citiesList = list;
            this.citiesArray = new String[list.size()];
            int index = 0;
            for (City bean : list) {
                citiesArray[index] = bean.getName();
                index++;
            }
        } else if (processType == Params.SERVICE_PROCESS_2) {
            ArrayList<Categories> list = (ArrayList<Categories>) b;
            this.categories = list;

            if (criteria != null) {
                if (criteria.getCategories() != null) {
                    if (!criteria.getCategories().isEmpty()) {
                        String[] categories = criteria.getCategories().split(",");
                        for (String categoryId : categories) {
                            findCategory(categoryId);
                        }
                    }
                }
            }

            FilterCategoryAdapter categoryAdapter = new FilterCategoryAdapter(this.activity, list, recordListener);
            categoriesList.setAdapter(categoryAdapter);
            categoryAdapter.notifyDataSetChanged();
        }
    }

    private void findCategory(String categoryId) {
        for (Categories category : this.categories) {
            if (category.getId().equalsIgnoreCase(categoryId)) {
                category.setSelected(true);
            }
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Categories category = categories.get(position);
        RecordHolder holder = recordHolderList.get(Integer.parseInt(category.getId()));
        if (category.isSelected()) {
            holder.getImageItem().setVisibility(View.GONE);
            category.setSelected(false);
        } else {
            holder.getImageItem().setVisibility(View.VISIBLE);
            category.setSelected(true);
        }
    }
}
