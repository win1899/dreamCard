package com.dreamcard.app.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamcard.app.R;
import com.dreamcard.app.components.AddCommentDialog;
import com.dreamcard.app.components.TransparentProgressDialog;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.Comments;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.LocationInfo;
import com.dreamcard.app.entity.MessageInfo;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.services.AddBusinessCommentAsync;
import com.dreamcard.app.services.AllOffersAsync;
import com.dreamcard.app.services.CommentsAsync;
import com.dreamcard.app.services.GetBussinesByIdAsync;
import com.dreamcard.app.utils.ImageViewLoader;
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.adapters.CommentsAdapter;
import com.dreamcard.app.view.adapters.ImagePagerAdapter;
import com.dreamcard.app.view.adapters.OtherOffersAdapter;
import com.dreamcard.app.view.fragments.LeftNavDrawerFragment;
import com.dreamcard.app.view.interfaces.AddCommentListener;
import com.dreamcard.app.view.interfaces.IServiceListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class OfferDetailsActivity extends Activity
        implements IServiceListener, View.OnClickListener, AddCommentListener {

    private TextView txtOfferPeriod;
    private TextView txtOfferValidFrom;
    private TextView txtMobile;
    private TextView txtPhone;
    private TextView txtCity;
    private TextView txtYouSaveLbl;
    private TextView txtNewPrice;
    private TextView txtBusinessName;
    private TextView txtDescription;
    private TextView txtOtherOfferBusiness;
    private ListView commentsListView;
    private ImageView btnAddComment;
    private AddCommentDialog commentDialog;
    private LinearLayout otherOfferGallery;
    private LinearLayout ratingPnl;
    private ImageView imgCircle;
    private TextView txtOfferDiscount;
    private RelativeLayout noCommentPnl;
    private ImagePagerAdapter imgAdapter;
    private ViewPager imgPager;

    private TransparentProgressDialog progress;
    private Runnable runnable;
    private Handler handler;

    private CommentsAdapter commentsAdapter;
    private ProgressBar commentProgress;
    private ProgressBar offersProgress;
    private OtherOffersAdapter otherOffersAdapter;

    private CommentsAsync commentsAsync;
    private AllOffersAsync otherOfferAsync;
    private Offers bean;
    private ArrayList<Comments> commentsList;
    private ArrayList<Offers> offersList;
    private ArrayList<Offers> otherOfferList;
    private int mLastFirstVisibleItem;
    private RelativeLayout otherOffersPnl;
    private TextView txtRatingPercentage;
    private TextView txtRatingTotalPer;
    private TextView txtRatingTotal;
    private LinearLayout editRatingPnl;
    private boolean isRatingChanged = false;
    private ImageView imgTakeMeThere;
    private ImageView imgLogo;
    private ScrollView scroll;
    private ImageView imgStoreLogo;

    private Stores offerStore;

    private GetBussinesByIdAsync getBussinesByIdAsync;

    @Override
    protected void onPause() {
        if (commentsAsync != null && commentsAsync.getStatus() == AsyncTask.Status.RUNNING) {
            commentsAsync.cancel(true);
        }
        if (otherOfferAsync != null && otherOfferAsync.getStatus() == AsyncTask.Status.RUNNING) {
            otherOfferAsync.cancel(true);
        }
        if (getBussinesByIdAsync != null && getBussinesByIdAsync.getStatus() == AsyncTask.Status.RUNNING) {
            getBussinesByIdAsync.cancel(true);
        }
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);
        buildUI();
        setData();

        SharedPreferences prefs = getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
        String id = prefs.getString(Params.USER_INFO_ID, "");

        commentsAsync = new CommentsAsync(this, ServicesConstants.getLikesNumRequestList(this.bean.getId())
                , Params.SERVICE_PROCESS_2, Params.TYPE_OFFER);
        commentsAsync.execute(this);

        otherOfferAsync = new AllOffersAsync(this
                , ServicesConstants.getOffersByBusinessRequestList(this.bean.getBusinessId())
                , Params.SERVICE_PROCESS_7, Params.TYPE_OFFERS_BY_BUSINESS);
        otherOfferAsync.execute(this);

        getBussinesByIdAsync = new GetBussinesByIdAsync(this, ServicesConstants.getBusinessById(bean.getBusinessId()),
                Params.SERVICE_PROCESS_9);
        getBussinesByIdAsync.execute(this);
    }

    private void buildUI() {

        txtBusinessName = (TextView) findViewById(R.id.txt_business_name);
        txtCity = (TextView) findViewById(R.id.txt_city);
        imgTakeMeThere = (ImageView) findViewById(R.id.img_take_me_there);
        imgTakeMeThere.setOnClickListener(this);
        txtDescription = (TextView) findViewById(R.id.txt_description);
        txtMobile = (TextView) findViewById(R.id.txt_mobile);
        txtOfferPeriod = (TextView) findViewById(R.id.txt_offer_period_until);
        txtPhone = (TextView) findViewById(R.id.txt_phone);
        txtNewPrice = (TextView) findViewById(R.id.txt_new_price);
        txtYouSaveLbl = (TextView) findViewById(R.id.txt_you_save_lbl);
        txtOfferDiscount = (TextView) findViewById(R.id.txt_discount);
        txtRatingPercentage = (TextView) findViewById(R.id.txt_per_rating);
        txtRatingTotal = (TextView) findViewById(R.id.txt_total_num_rating);
        txtRatingTotalPer = (TextView) findViewById(R.id.txt_total_per_rating);
        editRatingPnl = (LinearLayout) findViewById(R.id.edit_rating_pnl);
        editRatingPnl.setOnClickListener(this);
        scroll = (ScrollView) findViewById(R.id.scroll);
        imgStoreLogo = (ImageView) findViewById(R.id.img_store_logo);
        imgLogo = (ImageView) findViewById(R.id.img_menu_logo);
        imgLogo.setOnClickListener(this);

        imgPager = (ViewPager) findViewById(R.id.offer_details_pager);

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

        txtOtherOfferBusiness = (TextView) findViewById(R.id.txt_other_offer_business_name);
        noCommentPnl = (RelativeLayout) findViewById(R.id.no_comments_pnl);
        commentsListView = (ListView) findViewById(android.R.id.list);
        otherOffersPnl = (RelativeLayout) findViewById(R.id.other_offers_pnl);

        commentsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int i) {
                if (view.getId() == commentsListView.getId()) {
                    final int currentFirstVisibleItem = commentsListView.getFirstVisiblePosition();
                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                        otherOffersPnl.setVisibility(View.GONE);
                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                        otherOffersPnl.setVisibility(View.VISIBLE);
                    }
                    mLastFirstVisibleItem = currentFirstVisibleItem;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {

            }
        });

        btnAddComment = (ImageView) findViewById(R.id.img_add_comment);
        btnAddComment.setOnClickListener(this);
        otherOfferGallery = (LinearLayout) findViewById(R.id.other_offer_gallery);
        ratingPnl = (LinearLayout) findViewById(R.id.rating_pnl);
        imgCircle = (ImageView) findViewById(R.id.imageView);

        // add PhoneStateListener for monitoring
        MyPhoneListener phoneListener = new MyPhoneListener();
        TelephonyManager telephonyManager =
                (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        // receive notifications of telephony state changes
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        txtMobile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    String uri = "tel:" + txtMobile.getText().toString();
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));

                    startActivity(dialIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        txtPhone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    String uri = "tel:" + txtPhone.getText().toString();
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));

                    startActivity(dialIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setData() {
        Intent intent = getIntent();
        Offers bean = intent.getParcelableExtra(Params.DATA);
        bean.setPicturesList(intent.getStringArrayExtra(Params.PICTURE_LIST));
        this.bean = bean;
        txtBusinessName.setText(bean.getTitle());
        if (bean.getTitle().length() > 12) {
            txtBusinessName.setTextSize(18);
        }
        if (bean.getDescription() == null || bean.getDescription().equalsIgnoreCase("null"))
            txtDescription.setText("");
        else
            txtDescription.setText(bean.getDescription());

        if (bean.getCity() == null || bean.getCity().equalsIgnoreCase("null"))
            txtCity.setText("");
        else
            txtCity.setText(bean.getCity());

        //Fix data, if null or empty set 0
        if (bean.getSaleNewPrice() == null || bean.getSaleNewPrice().equalsIgnoreCase("null")
                || bean.getSaleNewPrice().length() == 0) {
            bean.setSaleNewPrice(String.valueOf(0));
        }

        //Fix data
        if (bean.getSaleOldPrice() == null || bean.getSaleOldPrice().equalsIgnoreCase("null") || bean.getSaleOldPrice().length() == 0) {
            txtYouSaveLbl.setText("");
        } else {

            //some data sent from server with infinite numbers, so we truncate 2 digits after dot
            String youSave = String.valueOf(Double.parseDouble(bean.getSaleOldPrice()) - Double.parseDouble(bean.getSaleNewPrice()));
            if (youSave.contains(".")) {
                String fraction = youSave.substring(youSave.indexOf("."));
                if (fraction.length() > 2) {
                    youSave = youSave.substring(0, (youSave.indexOf(".") + 3))
                            + youSave.substring(youSave.length() - 1);
                }
            }
            txtYouSaveLbl.setText(getString(R.string.you_saved) + " " + youSave);
        }
        if (bean.getMobile() == null || bean.getMobile().equalsIgnoreCase("null"))
            txtMobile.setText("");
        else
            txtMobile.setText(bean.getMobile());
        if (bean.getPhone() == null || bean.getPhone().equalsIgnoreCase("null"))
            txtPhone.setText("");
        else
            txtPhone.setText(bean.getPhone());

        //Fix data when number has infinite numbers after dot
        if (bean.getSaleNewPrice().contains(".")) {
            String fraction = bean.getSaleNewPrice().substring(bean.getSaleNewPrice().indexOf("."));
            if (fraction.length() > 2) {
                bean.setSaleNewPrice(bean.getSaleNewPrice().substring(0, (bean.getSaleNewPrice().indexOf(".") + 3))
                        + bean.getSaleNewPrice().substring(bean.getSaleNewPrice().length() - 1));
            }
        }

        txtNewPrice.setText(bean.getSaleNewPrice());
        txtOtherOfferBusiness.setText(bean.getBusinessName());

        if (bean.getDiscount().contains(".")) {
            String fraction = bean.getDiscount().substring(bean.getDiscount().indexOf("."));
            if (fraction.length() > 2) {
                bean.setDiscount(bean.getDiscount().substring(0, (bean.getDiscount().indexOf(".") + 3))
                        + bean.getDiscount().substring(bean.getDiscount().length() - 1));
            }
        }

        txtOfferDiscount.setText(bean.getDiscount());

        if (bean.getCurrency() != null) {
            if (bean.getCurrency().equalsIgnoreCase(Params.CURRENCY.ILS)) {
                txtNewPrice.setText(txtNewPrice.getText() + getString(R.string.ils));
                txtYouSaveLbl.setText(txtYouSaveLbl.getText() + getString(R.string.ils));
            } else if (bean.getCurrency().equalsIgnoreCase(Params.CURRENCY.USD)) {
                txtNewPrice.setText(txtNewPrice.getText() + getString(R.string.usd));
                txtYouSaveLbl.setText(txtYouSaveLbl.getText() + getString(R.string.usd));
            }
        }

        imgAdapter = new ImagePagerAdapter(this, bean);
        imgPager.setAdapter(imgAdapter);

        setRating(bean.getOfferRating());
        txtRatingPercentage.setText(String.valueOf(bean.getOfferRating()));
        txtRatingTotal.setText(String.valueOf(bean.getRatingCount()));

        try {
            Date date = new Date(Long.parseLong(bean.getValidFrom().replaceAll(".*?(\\d+).*", "$1")));
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            String period = bean.getValidationPeriod();
            if (period != null) {
                if (!period.equalsIgnoreCase("null") && period.length() > 0)
                    c.add(Calendar.DATE, Integer.parseInt(period));
            }


            Date validUntil = new Date(Long.parseLong(bean.getValidFrom().replaceAll(".*?(\\d+).*", "$1")));
            Calendar valid = Calendar.getInstance();
            valid.setTime(validUntil);


            // Get the represented dates in milliseconds
            long milis1 = c.getTimeInMillis();
            long milis2 = valid.getTimeInMillis();

            // Calculate difference in milliseconds
            long diff = Math.abs(milis2 - milis1);

            int result =  (int)(diff / (24 * 60 * 60 * 1000));
            if (result > 0) {
                txtOfferPeriod.setText(" باقي " + result + "يوم على انتهاء العرض ");
            }else{
                txtOfferPeriod.setText(" انتهت فترة العرض ");
            }
        }
        catch (Exception e) {
            Log.e(OfferDetailsActivity.class.getName(), "Parsing error on date ...");
        }

        if (bean.getType().equalsIgnoreCase("" + Params.OFFER_TYPE_EVENT)) {
            txtNewPrice.setVisibility(View.GONE);
            txtYouSaveLbl.setVisibility(View.GONE);
        }

        Utils.loadImage(this, bean.getBusinessLogo(), imgStoreLogo);
        imgStoreLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (offerStore == null) {
                    return;
                }
                Intent intent = new Intent(OfferDetailsActivity.this, StoreDetailsActivity.class);
                intent.putExtra(Params.DATA, offerStore);
                intent.putExtra(Params.PICTURE_LIST, offerStore.getPictures());
                startActivity(intent);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_in);
                finish();
            }
        });

        if (bean.getBusinessLogo() != null && bean.getBusinessLogo().length() > 0 && bean.getBusinessLogo().contains("http")) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) txtBusinessName.getLayoutParams();
            params.addRule(RelativeLayout.END_OF, R.id.img_store_logo);
            txtBusinessName.setLayoutParams(params);
        }
    }

    private void setRating(int rating) {
        for (int index = 0; index < rating; index++) {
            ratingPnl.addView(insertRating(index, 50, 50, getResources().getDrawable(R.drawable.stars_active)));
        }
        for (int index = 0; index < 5 - rating; index++) {
            ratingPnl.addView(insertRating(index, 50, 50, getResources().getDrawable(R.drawable.stars)));
        }
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.offer_details, menu);
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
        if (processType == Params.SERVICE_PROCESS_2) {
            ArrayList<Comments> list = (ArrayList<Comments>) b;
            this.commentsList = list;
            commentsAdapter = null;
            commentsAdapter = new CommentsAdapter(this, list);
            commentsListView.setAdapter(commentsAdapter);
            commentsAdapter.notifyDataSetChanged();

            if (list.size() == 0) {
                noCommentPnl.setVisibility(View.VISIBLE);
            }
            else {
                noCommentPnl.setVisibility(View.GONE);
            }
            scroll.fullScroll(ScrollView.FOCUS_UP);
        } else if (processType == Params.SERVICE_PROCESS_3) {
            progress.dismiss();
            MessageInfo bean = (MessageInfo) b;
            if (bean.isValid()) {
                Toast.makeText(this, getResources().getString(R.string.comment_added_successfully), Toast.LENGTH_LONG).show();
                commentsAsync = new CommentsAsync(this, ServicesConstants.getLikesNumRequestList(this.bean.getId())
                        , Params.SERVICE_PROCESS_2, Params.TYPE_OFFER);
                commentsAsync.execute(this);
            } else
                Toast.makeText(this, getResources().getString(R.string.comment_not_added), Toast.LENGTH_LONG).show();
        } else if (processType == Params.SERVICE_PROCESS_7) {
            ArrayList<Offers> list = (ArrayList<Offers>) b;
            this.otherOfferList = list;
            if (list.size() >= 1) {
                otherOffersPnl.setVisibility(View.VISIBLE);
                for (Offers bean : list) {
                    if (bean.getId().equalsIgnoreCase(this.bean.getId()))
                        continue;
                    otherOfferGallery.addView(insertPhoto(bean.getPosition(), bean.getOfferMainPhoto()
                            , bean.getSaleNewPrice(), 170, 150));
                }
            }
        }
        else if (processType == Params.SERVICE_PROCESS_9) {
            ArrayList<Stores> list = (ArrayList<Stores>) b;
            if (list != null && list.size() == 1) {
                this.offerStore = list.get(0);
            }
            else {
                for (Stores s : list) {
                    if (s.getId().equalsIgnoreCase(this.bean.getBusinessId())) {
                        offerStore = s;
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
        else if (view.getId() == R.id.img_add_comment)
            addCommentDialog();
        else if (view.getId() == R.id.img_take_me_there
                || view.getId() == R.id.take_me_there_layout
                || view.getId() == R.id.txt_city
                || view.getId() == R.id.img_marker_icon) {
            Intent intent = new Intent();
            LocationInfo info = new LocationInfo();
            info.setLatitude(this.bean.getLatitude());
            info.setLongitude(this.bean.getLongitude());
            intent.putExtra("location", info);
            setResult(Params.NAVIGATE, intent);
            finish();
        } else if (view.getId() == R.id.img_offer_logo) {
            Intent intent = new Intent(this, ImageViewerActivity.class);
            intent.putExtra("imageURL", bean.getOfferMainPhoto());
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        } else if (view.getId() == R.id.edit_rating_pnl) {
            Intent intent = new Intent(this, RateBusinessActivity.class);
            intent.putExtra(Params.RATING_TYPE, Params.TYPE_OFFER);
            intent.putExtra(Params.BUSINESS_ID, this.bean.getId());
            startActivityForResult(intent, Params.STATUS_ADD_RATING);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        } else if (view.getId() == R.id.img_menu_logo) {
            Intent intent = new Intent();
            setResult(Params.STATUS_MOVE_TO_DASHBOARD, intent);
            LeftNavDrawerFragment.setDrawerMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            finish();
        } else {
            Offers info = null;
            if (this.otherOfferList != null) {
                for (Offers bean : this.otherOfferList) {
                    if (view.getId() == bean.getPosition()) {
                        info = bean;
                        break;
                    }
                }
            }
            if (info != null) {
                Intent intent = new Intent(this, OfferDetailsActivity.class);
                intent.putExtra(Params.DATA, info);
                intent.putExtra(Params.PICTURE_LIST, info.getPicturesList());
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Params.STATUS_ADD_RATING) {
            String rating = data.getStringExtra("item");
            ratingPnl.removeAllViews();
            int ratingInt = Integer.parseInt(rating.substring(0, 1));
            bean.setOfferRating(ratingInt);
            setRating(ratingInt);
            this.isRatingChanged = true;
        } else if (resultCode == Params.STATUS_ADD_COMMENT) {
            commentsAsync = new CommentsAsync(this, ServicesConstants.getLikesNumRequestList(this.bean.getId())
                    , Params.SERVICE_PROCESS_2, Params.TYPE_OFFER);
            commentsAsync.execute(this);
        } else if (requestCode == 1) {
            if (data != null) {
                setResult(RESULT_OK, data);
                finish();
            }

        }
    }

    private View insertPhoto(int position, String url, String price, int width, int height) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.other_offers_pnl_layout, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(7, 0, 7, 5);
        layout.setLayoutParams(params);
        layout.setGravity(Gravity.CENTER);
        ImageView imageView = (ImageView) layout.findViewById(R.id.img_offer_logo);
        ImageViewLoader imgLoader = new ImageViewLoader(this);
        imgLoader.DisplayImage(url, imageView, getResources());

        TextView txtPrice = (TextView) layout.findViewById(R.id.txt_offer_price);
        txtPrice.setText(price);

        layout.setId(position);
        layout.setOnClickListener(this);
        return layout;
    }

    private View insertRating(int position, int width, int height, Drawable drawable) {
        Bitmap bm = drawableToBitmap(drawable);

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

    private void addCommentDialog() {
        Intent intent = new Intent(this, AddCommentActivity.class);
        intent.putExtra(Params.OFFER_ID, this.bean.getId());
        intent.putExtra(Params.TYPE, Params.TYPE_OFFER);
        startActivityForResult(intent, Params.STATUS_ADD_COMMENT);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void addComment(String comment) {
        commentDialog.dismiss();

        progress.show();
        handler.postDelayed(runnable, 5000);

        SharedPreferences prefs = getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
        String id = prefs.getString(Params.USER_INFO_ID, "");

        AddBusinessCommentAsync asyncTask = new AddBusinessCommentAsync(this
                , ServicesConstants.getAddOfferCommentRequestList(id, this.bean.getId(), comment)
                , Params.SERVICE_PROCESS_3, Params.TYPE_OFFER);
        asyncTask.execute(this);
    }

    @Override
    public void cancel() {
        commentDialog.dismiss();
    }

    public class MyPhoneListener extends PhoneStateListener {

        private boolean onCall = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
//                    // phone ringing...
//                    Toast.makeText(OfferDetailsActivity.this, incomingNumber + " calls you",
//                            Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // one call exists that is dialing, active, or on hold
//                    Toast.makeText(OfferDetailsActivity.this, "on call...",
//                            Toast.LENGTH_LONG).show();
                    //because user answers the incoming call
                    onCall = true;
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    // in initialization of the class and at the end of phone call

                    // detect flag from CALL_STATE_OFFHOOK
                    if (onCall == true) {
//                        Toast.makeText(OfferDetailsActivity.this, "restart app after call",
//                                Toast.LENGTH_LONG).show();

                        // restart our application
                        Intent restart = getBaseContext().getPackageManager().
                                getLaunchIntentForPackage(getBaseContext().getPackageName());
                        restart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(restart);
                        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

                        onCall = false;
                    }
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    public void onBackPressed() {
        if (isRatingChanged) {
            Intent intent = new Intent();
            setResult(100, intent);
        }
        finish();
    }
}
