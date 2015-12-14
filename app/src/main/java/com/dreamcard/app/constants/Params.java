package com.dreamcard.app.constants;

import android.graphics.Typeface;

import com.dreamcard.app.entity.Offers;

import java.util.ArrayList;

/**
 * Created by Moayed on 6/10/2014.
 */
public class Params {

    public static final String FRAGMENT_DASHBOARD="FRAGMENT_DASHBOARD";
    public static final String FRAGMENT_LATEST_OFFERS="LATERST_OFFERS";
    public static final String FRAGMENT_LOCATIONS="FRAGMENT_LOCATIONS";
    public static final String FRAGMENT_STORES="FRAGMENT_STORES";
    public static final String FRAGMENT_CATEGORIES="CATEGORIES";
    public static final String FRAGMENT_OFFER_INVOICE_PDF = "FRAGMENT_OFFER_INVOICE_PDF";
    public static final String FRAGMENT_OFFER_DETAILS="FRAGMENT_OFFER_DETAILS";
    public static final String FRAGMENT_ACTIVATION_1="FRAGMENT_ACTIVATION_1";
    public static final String FRAGMENT_ACTIVATION_2="FRAGMENT_ACTIVATION_2";
    public static final String FRAGMENT_ACTIVATION_3="FRAGMENT_ACTIVATION_3";
    public static final String FRAGMENT_ACTIVATION_4="FRAGMENT_ACTIVATION_4";
    public static final String FRAGMENT_ACTIVATION_5="FRAGMENT_ACTIVATION_5";
    public static final String FRAGMENT_SETTING="FRAGMENT_SETTING";
    public static final String FRAGMENT_LEFT_DRAWER="FRAGMENT_LEFT_DRAWER";
    public static final String FRAGMENT_FEEDBACK="FRAGMENT_FEEDBACK";
    public static final String FRAGMENT_ABOUTUS="FRAGMENT_ABOUTUS";
    public static final String FRAGMENT_FAQ="FRAGMENT_FAQ";
    public static final String FRAGMENT_SUB_CATEGORY="FRAGMENT_SUB_CATEGORY";
    public static final String FRAGMANT_CATEGORY_EMPTY="FRAGMENT_CATEGORY_EMPTY";

    public static final String USER_INFO_NAME="USER_INFO_NAME";
    public static final String USER_INFO_ID="USER_INFO_ID";
    public static final String USER_INFO_EMAIL="USER_INFO_EMAIL";
    public static final String USER_INFO_PASSWORD="USER_INFO_PASSWORD";
    public static final String USER_INFO_FIRST_NAME="USER_INFO_FIRST_NAME";
    public static final String USER_INFO_LAST_NAME="USER_INFO_LAST_NAME";
    public static final String USER_INFO_BIRTHDAY="USER_INFO_BIRTHDAY";
    public static final String USER_INFO_WORK="USER_INFO_WORK";
    public static final String USER_INFO_GENDER="USER_INFO_GENDER";
    public static final String USER_INFO_MOBILE="USER_INFO_MOBILE";
    public static final String USER_INFO_CITY="USER_INFO_CITY";
    public static final String USER_INFO_FULL_NAME="USER_INFO_FULL_NAME";
    public static final String USER_INFO_ADDRESS="USER_INFO_ADDRESS";
    public static final String USER_INFO_PHONE="USER_INFO_PHONE";
    public static final String USER_INFO_ID_NUM="USER_INFO_ID_NUM";
    public static final String USER_INFO_COUNTRY="USER_INFO_COUNTRY";
    public static final String USER_INFO_EDUCATION="USER_INFO_EDUCATION";
    public static final String USER_INFO_CARD_NUMBER="USER_INFO_CARD_NUMBER";

    public static final String APP_DATA="APP_DATA";
    public static final String RATING_TYPE="RATING_TYPE";
    public static final String OFFER_ID="OFFER_ID";
    public static final String ABOUT_US_SLAG="aboutus";
    public static final String TYPE="TYPE";
    public static final int MODE_EDIT=1;
    public static final int MODE_ADD=0;

    public static ArrayList<Offers> DISCOUNT_LIST;

    public static final class CURRENCY{
        public static final String ILS=""+2;
        public static final String USD=""+1;
    }

    public static final int TIME_OUT=10000;
    public static final int SERVICE_REQUEST_COUNT=1;

    public static final int TYPE_OFFER=1;
    public static final int TYPE_BUSINESS=2;
    public static final int TYPE_OFFER_BY_BUSINESS=3;
    public static final int TYPE_LIKE_OFFER=4;
    public static final int TYPE_OFFERS_BY_CAT=5;
    public static final int TYPE_OFFERS_BY_BUSINESS=6;
    public static final int TYPE_LIKE_BUSINESS=7;
    public static final int TYPE_ADD_CATEGORY=8;
    public static final int TYPE_REMOVE_CATEGORY=9;
    public static final int TYPE_LATEST_OFFERS_BY_BUSINESS=10;
    public static final int TYPE_PARENT_CATEGORY = 11;
    public static final int TYPE_SUB_CATEGORY = 12;
    public static final int TYPE_ALL_CATEGORY = 13;

    public static final int STATUS_MOVE_TO_DASHBOARD=200;
    public static final int STATUS_ADD_COMMENT=300;
    public static final int STATUS_ADD_RATING=400;
    public static final int STATUS_NOTHING=-100;

    public static final String DATA="DATA";
    public static final String PICTURE_LIST = "PictureList";
    public static String BUSINESS_ID=null;

    public static final int STATUS_FAILED=1;
    public static final int STATUS_SUCCESS=0;

    public static final int SERVICE_PROCESS_1=1;
    public static final int SERVICE_PROCESS_2=2;
    public static final int SERVICE_PROCESS_3=3;
    public static final int SERVICE_PROCESS_4=4;
    public static final int SERVICE_PROCESS_5=5;
    public static final int SERVICE_PROCESS_6=6;
    public static final int SERVICE_PROCESS_7=7;
    public static final int SERVICE_PROCESS_8=8;
    public static final int SERVICE_PROCESS_9=9;

    public static final int NAVIGATE = 58;
    public static final int START_OFFERS_DETAILS_REQUEST_CODE = 17;
    public static final String DREAMCARD_URL="http://www.dream-card.net/";

    public static Typeface FONT;

    public static final int GENDER_MALE=1;
    public static final int GENDER_FEMALE=2;

    public static final int STORE_CLASS_GOLD=2;
    public static final int STORE_CLASS_SILVER=1;
    public static final int STORE_CLASS_REGULAR=0;

    public static final int OFFER_TYPE_DISCOUNT=1;
    public static final int OFFER_TYPE_BUY_ONE_TAKE_MORE=2;
    public static final int OFFER_TYPE_BUY_ONE_TAKE_MORE_SALE=3;
    public static final int OFFER_TYPE_GENERIC_AD=4;
    public static final int OFFER_TYPE_EVENT=5;

    /**
     * Database
     */
    public static final String SYSTEM_DB="DREAMCARD";
    public static final int DB_VERSION=2;

    /**
     * FAQ
     */
    public static final String QUESTION_EXTRA = "questionExtra";
    public static final String ANSWER_EXTRA = "answerExtra";
}
