package com.dreamcard.app.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamcard.app.R;
import com.dreamcard.app.components.ExpandableHeightGridView;
import com.dreamcard.app.components.NonScrollableListView;
import com.dreamcard.app.components.TransparentProgressDialog;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.CashPointsTransaction;
import com.dreamcard.app.entity.Comments;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.LocationInfo;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.services.AllOffersAsync;
import com.dreamcard.app.services.CommentsAsync;
import com.dreamcard.app.services.GetBussinesByIdAsync;
import com.dreamcard.app.services.GetCashPointsAsync;
import com.dreamcard.app.services.IsOfferLikedAsyncTask;
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.adapters.CommentsAdapter;
import com.dreamcard.app.view.adapters.StoreImagePagerAdapter;
import com.dreamcard.app.view.adapters.StoreOffersGridAdapter;
import com.dreamcard.app.view.interfaces.IServiceListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class StoreDetailsActivity extends Activity implements View.OnClickListener, IServiceListener {

    private TextView txtBusinessName;
    private ImageView imgOfferLogo;
    private TextView txtNumOfLikes;
    private TextView txtLikeBtn;
    private TextView txtAnnualDiscount;
    private TextView txtAddress;
    private TextView _cashPointsTxt;
    private NonScrollableListView commentsListView;
    private RelativeLayout ratingPnl;
    private ExpandableHeightGridView grid;
    private StoreOffersGridAdapter adapter;
    private ProgressBar progressBar;
    private RelativeLayout noCommentPnl;
    private ImageView imgLogo;
    private ScrollView scroll;

    private TransparentProgressDialog progress;
    private Runnable runnable;
    private Handler handler;

    private CommentsAdapter commentsAdapter;

    private AllOffersAsync allOffersAsync;
    private CommentsAsync commentsAsync;
    private ArrayList<Comments> commentsList;
    private ArrayList<Offers> offersList;
    private StoreImagePagerAdapter imgAdapter;
    private ViewPager imgPager;
    private ImageButton btnFacebook;
    private ImageButton btnPhone;
    private ImageButton btnEmail;
    private ImageButton btnWebsite;
    private ImageView storeIcon;
    private Button btnAll;
    private Button btnLatest;
    private boolean firstLoad = true;
    private TextView txtAbout;
    private TextView txtStoreName;

    private Stores _storeData;

    private GetCashPointsAsync _getPointsAsync;
    private GetBussinesByIdAsync _getBussinesByIdAsync;
    private String _storeId;
    @Override
    public void onPause() {
        if (_getBussinesByIdAsync != null && _getBussinesByIdAsync.getStatus() == AsyncTask.Status.RUNNING) {
            _getBussinesByIdAsync.cancel(true);
        }

        if (_getPointsAsync != null && _getPointsAsync.getStatus() == AsyncTask.Status.RUNNING) {
            _getPointsAsync.cancel(true);
        }

        if (commentsAsync != null && commentsAsync.getStatus() == AsyncTask.Status.RUNNING) {
            commentsAsync.cancel(true);
        }

        if (allOffersAsync != null && allOffersAsync.getStatus() == AsyncTask.Status.RUNNING) {
            allOffersAsync.cancel(true);
        }
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_store_details);
        }
        catch (OutOfMemoryError e) {
            finish();
        }

        buildUI();
        setData();
    }

    private void continueLoading() {
        SharedPreferences prefs = getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
        String id = prefs.getString(Params.USER_INFO_ID, "");

        IsOfferLikedAsyncTask asyncTask = new IsOfferLikedAsyncTask(this
                , ServicesConstants.getIsBusinessLikedRequestList(id, this._storeData.getId())
                , Params.SERVICE_PROCESS_6, Params.TYPE_BUSINESS);
        asyncTask.execute(this);

        progressBar.setVisibility(View.VISIBLE);
        grid.setVisibility(View.GONE);

        ArrayList<ServiceRequest> list = ServicesConstants.getBusinessLikesNumRequestList(this._storeData.getId());
        allOffersAsync = new AllOffersAsync(this, list, Params.SERVICE_PROCESS_1, Params.TYPE_OFFERS_BY_BUSINESS);
        allOffersAsync.execute(this);

        commentsAsync = new CommentsAsync(this, ServicesConstants.getBusinessLikesNumRequestList(this._storeData.getId())
                , Params.SERVICE_PROCESS_2, Params.TYPE_BUSINESS);
        commentsAsync.execute(this);

        _getPointsAsync = new GetCashPointsAsync(this, ServicesConstants.getPointsRequestList(id), Params.SERVICE_PROCESS_4);
        _getPointsAsync.execute(this);
    }

    private void buildUI() {
        txtBusinessName = (TextView) findViewById(R.id.txt_business_name);
        imgOfferLogo = (ImageView) findViewById(R.id.img_offer_logo);
        txtAnnualDiscount = (TextView) findViewById(R.id.annual_discount);
        imgOfferLogo.setOnClickListener(this);
        scroll = (ScrollView) findViewById(R.id.scroll);
        commentsListView = (NonScrollableListView) findViewById(android.R.id.list);
        storeIcon = (ImageView) findViewById(R.id.main_store_icon);
        txtAddress = (TextView) findViewById(R.id.store_address);
        txtAddress.setOnClickListener(this);
        txtStoreName = (TextView) findViewById(R.id.store_name_details);
        _cashPointsTxt = (TextView) findViewById(R.id.cash_points_value);

        ratingPnl = (RelativeLayout) findViewById(R.id.rating_pnl);
        grid = (ExpandableHeightGridView) findViewById(R.id.offers_grid);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        noCommentPnl = (RelativeLayout) findViewById(R.id.no_comments_pnl);
        imgLogo = (ImageView) findViewById(R.id.img_menu_logo);
        imgLogo.setOnClickListener(this);
        imgPager = (ViewPager) findViewById(R.id.img_store_pager);
        ImageView location = (ImageView) findViewById(R.id.btn_location);
        location.setOnClickListener(this);

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

        btnEmail = (ImageButton) findViewById(R.id.btn_email);
        btnFacebook = (ImageButton) findViewById(R.id.btn_facebook);
        btnPhone = (ImageButton) findViewById(R.id.btn_phone);
        btnWebsite = (ImageButton) findViewById(R.id.btn_website);

        btnWebsite.setOnClickListener(this);
        btnEmail.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);
        btnPhone.setOnClickListener(this);

        btnAll = (Button) findViewById(R.id.btn_all);
        btnLatest = (Button) findViewById(R.id.btn_latest);
        btnAll.setOnClickListener(this);
        btnLatest.setOnClickListener(this);

        txtAbout = (TextView) findViewById(R.id.txt_description);

    }

    public void setData() {
        Intent intent = getIntent();
        if (_storeData == null) {
            _storeData = intent.getParcelableExtra(Params.DATA);

            if (_storeData == null) {
                _storeId = intent.getStringExtra(Stores.EXTRA_STORE_ID);
                if (_storeId == null) {
                    finish();
                    return;
                }
                _getBussinesByIdAsync = new GetBussinesByIdAsync(this, ServicesConstants.getBusinessById(_storeId),
                        Params.SERVICE_PROCESS_9);
                _getBussinesByIdAsync.execute(this);
                return;
            }
            _storeData.setPictures(intent.getStringArrayExtra(Params.PICTURE_LIST));
        }

        txtBusinessName.setText(_storeData.getStoreName());
        txtStoreName.setText(_storeData.getStoreName());
        if (_storeData.getDiscountPrecentage() > 0.0) {
            txtAnnualDiscount.setText(Integer.toString((int)_storeData.getDiscountPrecentage()) + "%");
            txtAnnualDiscount.setVisibility(View.VISIBLE);
            ratingPnl.setVisibility(View.VISIBLE);
        }
        else {
            txtAnnualDiscount.setVisibility(View.GONE);
            ratingPnl.setVisibility(View.GONE);
        }

        if (_storeData.getOurMessage() != null && !_storeData.getOurMessage().isEmpty() && !_storeData.getOurMessage().equalsIgnoreCase("null")) {
            String about = _storeData.getOurMessage();
            if (_storeData.getOurMessage().length() > 100) {
                about = _storeData.getOurMessage().substring(0, 100) + "...";
                txtAbout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtAbout.setText(_storeData.getOurMessage());
                    }
                });
            }
            txtAbout.setText(about);
        } else if (_storeData.getMission() != null && !_storeData.getMission().isEmpty() && !_storeData.getMission().equalsIgnoreCase("null")) {
            String mission = _storeData.getMission();
            if (_storeData.getMission().length() > 100) {
                mission = _storeData.getMission().substring(0, 100) + "...";
                txtAbout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtAbout.setText(_storeData.getMission());
                    }
                });
            }
            txtAbout.setText(mission);
        } else if (_storeData.getVision() != null && !_storeData.getVision().isEmpty() && !_storeData.getVision().equalsIgnoreCase("null")) {
            String vision = _storeData.getVision();
            if (_storeData.getVision().length() > 100) {
                vision = _storeData.getVision().substring(0, 100) + "...";
                txtAbout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtAbout.setText(_storeData.getVision());
                    }
                });
            }
            txtAbout.setText(vision);
        }
        txtAddress.setText(_storeData.getAddress1());

//        _cashPointsTxt.setText(_storeData.getCashPoints());

        imgAdapter = new StoreImagePagerAdapter(this, _storeData);
        imgPager.setAdapter(imgAdapter);

        Utils.loadImage(this, _storeData.getLogo(), storeIcon);
        if (_storeData.getLogo() == null && _storeData.getPictures() == null) {
            txtBusinessName.setVisibility(View.VISIBLE);
        }
        continueLoading();
    }

    private void setRating(int rating) {
        for (int index = 0; index < rating; index++) {
            ratingPnl.addView(insertRating(index, 20, 20));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.store_details, menu);
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

    @Override
    public void onServiceSuccess(Object b, int processType) {
        if (processType == Params.SERVICE_PROCESS_1) {
            final int x = scroll.getScrollX();
            final int y = scroll.getScrollY();

            ArrayList<Offers> list = (ArrayList<Offers>) b;
            this.offersList = list;
            grid.setExpanded(true);
            adapter = new StoreOffersGridAdapter(this, list, this);
            grid.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            grid.setVisibility(View.VISIBLE);
            scroll.post(new Runnable() {
                public void run() {
                    scroll.scrollTo(x, y);
                }
            });
        } else if (processType == Params.SERVICE_PROCESS_2) {
            ArrayList<Comments> list = (ArrayList<Comments>) b;
            this.commentsList = list;
            commentsAdapter = null;
            commentsAdapter = new CommentsAdapter(this, list);
            commentsListView.setAdapter(commentsAdapter);
            commentsAdapter.notifyDataSetChanged();

            if (list.size() == 0) {
                noCommentPnl.setVisibility(View.VISIBLE);
            } else {
                noCommentPnl.setVisibility(View.GONE);
                scroll.fullScroll(ScrollView.FOCUS_UP);
            }

        } else if (processType == Params.SERVICE_PROCESS_4) {
            HashMap<Integer, ArrayList<CashPointsTransaction>> transactions = (HashMap<Integer, ArrayList<CashPointsTransaction>>) b;
            Set<Integer> set = transactions.keySet();
            int totalCashpoints = 0;
            for (Integer id : set) {
                if (_storeData.getId().equalsIgnoreCase(Integer.toString(id))) {
                    ArrayList<CashPointsTransaction> trans = transactions.get(id);
                    for (CashPointsTransaction t : trans) {
                        if (t.getStatus() != null && t.getStatus().equalsIgnoreCase("Earned")) {
                            totalCashpoints += t.getPointsValue();
                        }
                    }
                    break;
                }
            }
            _cashPointsTxt.setText("" + totalCashpoints);
        } else if (processType == Params.SERVICE_PROCESS_9) {
            ArrayList<Stores> list = (ArrayList<Stores>) b;
            if (list != null && list.size() == 1) {
                _storeData = list.get(0);
                setData();
            }
            else {
                for (Stores s : list) {
                    if (s.getId().equalsIgnoreCase(_storeId)) {
                        _storeData = s;
                        setData();
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onServiceFailed(ErrorMessageInfo info) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txt_back || view.getId() == R.id.txt_arrow)
            finish();
        else if (view.getId() == R.id.rating_pnl) {
            if (Utils.promoteActivation(StoreDetailsActivity.this)) {
                return;
            }
            callRatingActivity();
        }
        else if (view.getId() == R.id.img_offer_logo) {
            Intent intent = new Intent(this, ImageViewerActivity.class);
            intent.putExtra("imageURL", _storeData.getWideLogo());
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        } else if (view.getId() == R.id.img_menu_logo) {
            Intent intent = new Intent();
            setResult(Params.STATUS_MOVE_TO_DASHBOARD, intent);
            finish();
        } else if (view.getId() == R.id.btn_facebook) {
            openFacebookPage();
        } else if (view.getId() == R.id.btn_email) {
            sendEmail();
        } else if (view.getId() == R.id.btn_phone) {
            makePhoneCall();
        } else if (view.getId() == R.id.btn_website) {
            openWebsite();
        } else if (view.getId() == R.id.btn_all) {
            GradientDrawable bgShape = (GradientDrawable) btnLatest.getBackground();
            bgShape.setColor(getResources().getColor(R.color.not_selected_offer_status_bg));

            GradientDrawable totalShape = (GradientDrawable) btnAll.getBackground();
            totalShape.setColor(getResources().getColor(R.color.selected_offer_status_bg));

            progressBar.setVisibility(View.VISIBLE);
            grid.setVisibility(View.GONE);
            firstLoad = false;

            ArrayList<ServiceRequest> list = ServicesConstants.getBusinessLikesNumRequestList(this._storeData.getId());
            allOffersAsync = new AllOffersAsync(this, list, Params.SERVICE_PROCESS_1, Params.TYPE_OFFERS_BY_BUSINESS);
            allOffersAsync.execute(this);

        } else if (view.getId() == R.id.btn_latest) {
            GradientDrawable bgShape = (GradientDrawable) btnLatest.getBackground();
            bgShape.setColor(getResources().getColor(R.color.selected_offer_status_bg));

            GradientDrawable totalShape = (GradientDrawable) btnAll.getBackground();
            totalShape.setColor(getResources().getColor(R.color.not_selected_offer_status_bg));

            progressBar.setVisibility(View.VISIBLE);
            grid.setVisibility(View.GONE);
            firstLoad = false;

            ArrayList<ServiceRequest> list = ServicesConstants.getLatestOfferByStoreId(this._storeData.getId(), 10);
            allOffersAsync = new AllOffersAsync(this, list, Params.SERVICE_PROCESS_1, Params.TYPE_LATEST_OFFERS_BY_BUSINESS);
            allOffersAsync.execute(this);
        } else if (view.getId() == R.id.btn_location || view.getId() == R.id.store_address) {
            Intent data = new Intent();
            try {
                LocationInfo info = new LocationInfo();
                double latitude = Double.parseDouble(_storeData.getLatitude());
                double longitude = Double.parseDouble(_storeData.getLongitude());
                info.setLatitude(latitude);
                info.setLongitude(longitude);
                data.putExtra("location", info);
            } catch (Exception e) {

            }
            setResult(Params.NAVIGATE, data);
            finish();
        } else {
            Offers info = null;
            for (Offers bean : this.offersList) {
                if (view.getId() == bean.getPosition()) {
                    info = bean;
                    break;
                }
            }
            if (info != null) {
                Intent intent = new Intent(this, OfferDetailsActivity.class);
                intent.putExtra(Params.DATA, info);
                intent.putExtra(Params.PICTURE_LIST, info.getPicturesList());
                startActivityForResult(intent, Params.START_OFFERS_DETAILS_REQUEST_CODE);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        }
    }

    private void openFacebookPage() {
        if (_storeData.getFacebook() != null && _storeData.getFacebook().length() > 0 && !_storeData.getFacebook().equalsIgnoreCase("null")) {
            try {
                if (!_storeData.getFacebook().contains("http")) {
                    _storeData.setFacebook("https://" + _storeData.getFacebook());
                }

                int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
                if (versionCode >= 3002850) {
                    Uri uri = Uri.parse("fb://facewebmodal/f?href=" + _storeData.getFacebook());
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));

                } else {
                    // open the Facebook app using the old method (fb://profile/id or fb://page/id)
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/336227679757310")));
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(_storeData.getFacebook())));
                }
            } catch (PackageManager.NameNotFoundException e) {
                // Facebook is not installed. Open the browser
//                String facebookUrl = "https://www.facebook.com/JRummyApps";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(_storeData.getFacebook())));
            }
        } else {
            Toast.makeText(this, "Facebook page not available", Toast.LENGTH_LONG).show();
        }
    }

    private void makePhoneCall() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + Uri.encode(_storeData.getPhone())));
        startActivity(callIntent);
    }

    private void openWebsite() {
        if (_storeData.getWebsite() != null && _storeData.getWebsite().length() > 0) {
            if (!_storeData.getWebsite().contains("http")) {
                _storeData.setWebsite("http://" + _storeData.getWebsite());
            }
            Uri uri = Uri.parse(_storeData.getWebsite());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Website not available", Toast.LENGTH_LONG).show();
        }
    }

    private void sendEmail() {
        if (_storeData.getEmail() != null && _storeData.getEmail().length() > 0) {
        /* Create the Intent */
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        /* Fill it with Data */
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{_storeData.getEmail()});

        /* Send it off to the Activity-Chooser */
            startActivity(Intent.createChooser(emailIntent, "Send mail to " + _storeData.getEmail()));
        } else {
            Toast.makeText(this, "Email not available", Toast.LENGTH_LONG).show();
        }
    }

    private void callRatingActivity() {
        if (Utils.promoteActivation(StoreDetailsActivity.this)) {
            return;
        }
        Intent intent = new Intent(this, RateBusinessActivity.class);
        intent.putExtra(Params.BUSINESS_ID, this._storeData.getId());
        startActivityForResult(intent, Params.STATUS_ADD_RATING);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Params.START_OFFERS_DETAILS_REQUEST_CODE && resultCode == Params.NAVIGATE) {
            setResult(Params.NAVIGATE, data);
            finish();
        }
        if (resultCode == Params.STATUS_ADD_RATING) {
            String rating = data.getStringExtra("item");
            ratingPnl.removeAllViews();
            setRating(Integer.parseInt(rating.substring(0, 1)));
        } else if (resultCode == Params.STATUS_ADD_COMMENT) {
            commentsAsync = new CommentsAsync(this, ServicesConstants.getBusinessLikesNumRequestList(this._storeData.getId())
                    , Params.SERVICE_PROCESS_2, Params.TYPE_BUSINESS);
            commentsAsync.execute(this);
        }
    }

    private View insertRating(int position, int width, int height) {
        Bitmap bm = drawableToBitmap(getResources().getDrawable(R.drawable.gold_star));
        //decodeSampledBitmapFromUri(path, 220, 220);

        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(5, 0, 0, 0);
        layout.setLayoutParams(params);
        layout.setGravity(Gravity.CENTER);


        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setId(position);
        imageView.setOnClickListener(this);

        imageView.setImageBitmap(bm);

        layout.addView(imageView);
        return layout;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
