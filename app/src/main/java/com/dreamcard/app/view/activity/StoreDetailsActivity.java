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
import android.os.Handler;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.dreamcard.app.R;
import com.dreamcard.app.components.AddCommentDialog;
import com.dreamcard.app.components.ExpandableHeightGridView;
import com.dreamcard.app.components.TransparentProgressDialog;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.BusinessComment;
import com.dreamcard.app.entity.Comments;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.MessageInfo;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.services.AddBusinessCommentAsync;
import com.dreamcard.app.services.AllOffersAsync;
import com.dreamcard.app.services.CommentsAsync;
import com.dreamcard.app.services.IsOfferLikedAsyncTask;
import com.dreamcard.app.view.adapters.CommentsAdapter;
import com.dreamcard.app.view.adapters.StoreOffersGridAdapter;
import com.dreamcard.app.view.interfaces.AddCommentListener;
import com.dreamcard.app.view.interfaces.IServiceListener;

import java.util.ArrayList;

public class StoreDetailsActivity extends Activity implements View.OnClickListener, IServiceListener, AddCommentListener {

    private TextView txtBusinessName;
    private ImageView imgOfferLogo;
    private TextView txtNumOfLikes;
    private TextView txtLikeBtn;
    private TextView txtAnnualDiscount;
    private ListView commentsListView;
    private ImageView btnAddComment;
    private AddCommentDialog commentDialog;
    private LinearLayout ratingPnl;
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
    private ImageView imgStoreLogo;
    private ImageButton btnFacebook;
    private ImageButton btnPhone;
    private ImageButton btnEmail;
    private ImageButton btnWebsite;
    private Button btnAll;
    private Button btnLatest;
    private boolean firstLoad = true;
    private TextView txtAbout;

    private Stores bean = new Stores();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_details);

        buildUI();
        setData();

        SharedPreferences prefs = getSharedPreferences(Params.APP_DATA, Activity.MODE_PRIVATE);
        String id = prefs.getString(Params.USER_INFO_ID, "");

        IsOfferLikedAsyncTask asyncTask = new IsOfferLikedAsyncTask(this
                , ServicesConstants.getIsBusinessLikedRequestList(id, this.bean.getId())
                , Params.SERVICE_PROCESS_6, Params.TYPE_BUSINESS);
        asyncTask.execute(this);

        progressBar.setVisibility(View.VISIBLE);
        grid.setVisibility(View.GONE);

        ArrayList<ServiceRequest> list = ServicesConstants.getBusinessLikesNumRequestList(this.bean.getId());
        allOffersAsync = new AllOffersAsync(this, list, Params.SERVICE_PROCESS_1, Params.TYPE_OFFERS_BY_BUSINESS);
        allOffersAsync.execute(this);

        commentsAsync = new CommentsAsync(this, ServicesConstants.getBusinessLikesNumRequestList(this.bean.getId())
                , Params.SERVICE_PROCESS_2, Params.TYPE_BUSINESS);
        commentsAsync.execute(this);
    }

    private void buildUI() {
        txtBusinessName = (TextView) findViewById(R.id.txt_business_name);
        imgOfferLogo = (ImageView) findViewById(R.id.img_offer_logo);
        txtAnnualDiscount = (TextView) findViewById(R.id.annual_discount);
        imgOfferLogo.setOnClickListener(this);
        scroll = (ScrollView) findViewById(R.id.scroll);
        commentsListView = (ListView) findViewById(android.R.id.list);
        commentsListView.setOnTouchListener(new ListView.OnTouchListener() {
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
        btnAddComment = (ImageView) findViewById(R.id.img_add_comment);
        btnAddComment.setOnClickListener(this);
        ratingPnl = (LinearLayout) findViewById(R.id.rating_pnl);
        grid = (ExpandableHeightGridView) findViewById(R.id.offers_grid);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        noCommentPnl = (RelativeLayout) findViewById(R.id.no_comments_pnl);
        imgLogo = (ImageView) findViewById(R.id.img_menu_logo);
        imgLogo.setOnClickListener(this);
        imgStoreLogo = (ImageView) findViewById(R.id.img_store_logo);

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
        Stores bean = intent.getParcelableExtra(Params.DATA);
        this.bean = bean;

        txtBusinessName.setText(bean.getStoreName());
        // TODO: txtAnnualDiscount.setText(bean.get);
        if (bean.getOurMessage() != null && !bean.getOurMessage().isEmpty() && !bean.getOurMessage().equalsIgnoreCase("null")) {
            if (bean.getOurMessage().length() > 100) {
                bean.setOurMessage(bean.getOurMessage().substring(0, 100) + "...");
            }
            txtAbout.setText(bean.getOurMessage());
        } else if (bean.getMission() != null && !bean.getMission().isEmpty() && !bean.getMission().equalsIgnoreCase("null")) {
            if (bean.getMission().length() > 100) {
                bean.setMission(bean.getMission().substring(0, 100) + "...");
            }
            txtAbout.setText(bean.getMission());
        } else if (bean.getVision() != null && !bean.getVision().isEmpty() && !bean.getVision().equalsIgnoreCase("null")) {
            if (bean.getVision().length() > 100) {
                bean.setVision(bean.getVision().substring(0, 100) + "...");
            }
            txtAbout.setText(bean.getVision());
        }

        AQuery aq = new AQuery(this);
        aq.id(R.id.img_offer_logo).progress(R.id.store_detail_progress).image(bean.getWideLogo(), true
                , true, imgOfferLogo.getWidth(), 0, null, AQuery.FADE_IN, AQuery.RATIO_PRESERVE);
        setRating(bean.getRating());
        ratingPnl.setOnClickListener(this);

        if (bean.getLogo() != null && bean.getLogo().length() > 0 && bean.getLogo().contains("http")) {
            aq.id(imgStoreLogo).image(bean.getLogo(), true, true
                    , imgStoreLogo.getWidth(), 0, null, AQuery.FADE_IN, AQuery.RATIO_PRESERVE);
            txtBusinessName.setVisibility(View.GONE);
        } else {
            imgStoreLogo.setVisibility(View.GONE);
        }
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

        } else if (processType == Params.SERVICE_PROCESS_3) {
            progress.dismiss();
            MessageInfo bean = (MessageInfo) b;
            if (bean.isValid()) {
                Toast.makeText(this, getResources().getString(R.string.comment_added_successfully), Toast.LENGTH_LONG).show();
                commentsAsync = new CommentsAsync(this, ServicesConstants.getBusinessLikesNumRequestList(this.bean.getId())
                        , Params.SERVICE_PROCESS_2, Params.TYPE_BUSINESS);
                commentsAsync.execute(this);
            } else
                Toast.makeText(this, getResources().getString(R.string.comment_not_added), Toast.LENGTH_LONG).show();
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
        else if (view.getId() == R.id.rating_pnl)
            callRatingActivity();
        else if (view.getId() == R.id.img_offer_logo) {
            Intent intent = new Intent(this, ImageViewerActivity.class);
            intent.putExtra("imageURL", bean.getWideLogo());
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

            ArrayList<ServiceRequest> list = ServicesConstants.getBusinessLikesNumRequestList(this.bean.getId());
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

            ArrayList<ServiceRequest> list = ServicesConstants.getLatestOfferByStoreId(this.bean.getId(), 10);
            allOffersAsync = new AllOffersAsync(this, list, Params.SERVICE_PROCESS_1, Params.TYPE_LATEST_OFFERS_BY_BUSINESS);
            allOffersAsync.execute(this);
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
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        }
    }

    private void openFacebookPage() {
        if (bean.getFacebook() != null && bean.getFacebook().length() > 0 && !bean.getFacebook().equalsIgnoreCase("null")) {
            try {
                if (!bean.getFacebook().contains("http")) {
                    bean.setFacebook("https://" + bean.getFacebook());
                }

                int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
                if (versionCode >= 3002850) {
                    Uri uri = Uri.parse("fb://facewebmodal/f?href=" + bean.getFacebook());
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    ;
                } else {
                    // open the Facebook app using the old method (fb://profile/id or fb://page/id)
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/336227679757310")));
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(bean.getFacebook())));
                }
            } catch (PackageManager.NameNotFoundException e) {
                // Facebook is not installed. Open the browser
//                String facebookUrl = "https://www.facebook.com/JRummyApps";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(bean.getFacebook())));
            }
        } else {
            Toast.makeText(this, "Facebook page not available", Toast.LENGTH_LONG).show();
        }
    }

    private void makePhoneCall() {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + Uri.encode(bean.getPhone())));
        startActivity(callIntent);
    }

    private void openWebsite() {
        if (bean.getWebsite() != null && bean.getWebsite().length() > 0) {
            if (!bean.getWebsite().contains("http")) {
                bean.setWebsite("http://" + bean.getWebsite());
            }
            Uri uri = Uri.parse(bean.getWebsite());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Website not available", Toast.LENGTH_LONG).show();
        }
    }

    private void sendEmail() {
        if (bean.getEmail() != null && bean.getEmail().length() > 0) {
        /* Create the Intent */
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        /* Fill it with Data */
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{bean.getEmail()});

        /* Send it off to the Activity-Chooser */
            startActivity(Intent.createChooser(emailIntent, "Send mail to " + bean.getEmail()));
        } else {
            Toast.makeText(this, "Email not available", Toast.LENGTH_LONG).show();
        }
    }

    private void callRatingActivity() {
        Intent intent = new Intent(this, RateBusinessActivity.class);
        intent.putExtra(Params.BUSINESS_ID, this.bean.getId());
        startActivityForResult(intent, Params.STATUS_ADD_RATING);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Params.STATUS_ADD_RATING) {
            String rating = data.getStringExtra("item");
            ratingPnl.removeAllViews();
            setRating(Integer.parseInt(rating.substring(0, 1)));
        } else if (resultCode == Params.STATUS_ADD_COMMENT) {
            commentsAsync = new CommentsAsync(this, ServicesConstants.getBusinessLikesNumRequestList(this.bean.getId())
                    , Params.SERVICE_PROCESS_2, Params.TYPE_BUSINESS);
            commentsAsync.execute(this);
        }
    }

    private void addCommentDialog() {
        Intent intent = new Intent(this, AddCommentActivity.class);
        intent.putExtra(Params.OFFER_ID, this.bean.getId());
        intent.putExtra(Params.TYPE, Params.TYPE_BUSINESS);
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

        BusinessComment bean = new BusinessComment();
        bean.Consumer = 87;
        bean.Comment = comment;
        bean.BusinessID = 22;

        AddBusinessCommentAsync asyncTask = new AddBusinessCommentAsync(this
                , ServicesConstants.getAddBusinessCommentRequestList(id, this.bean.getId(), comment)
                , Params.SERVICE_PROCESS_3, Params.TYPE_BUSINESS);
        asyncTask.execute(this);
    }

    @Override
    public void cancel() {
        commentDialog.dismiss();
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
