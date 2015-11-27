package com.dreamcard.app.constants;

import android.util.Log;

import com.dreamcard.app.entity.PersonalInfo;
import com.dreamcard.app.entity.SearchCriteria;
import com.dreamcard.app.entity.ServiceRequest;

import org.ksoap2.serialization.PropertyInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Moayed on 6/21/2014.
 */
public class ServicesConstants {

    public static final String WS_NAME_SPACE="http://tempuri.org/";//IService1/
    public static final String WS_NAME_SPACE2="http://tempuri.org/xsd";
    public static final String WS_ACTION="http://tempuri.org/IService1/";
    public static String WSDL_URL = "http://dream-card.net/b2bservice/service1.svc";

    public static String WS_METHOD_NAME_ALL_OFFERS= "SelectAllOffers";
    public static String WS_METHOD_NAME_BUSINESS_BY_ID="SelectBusinessByID";
    public static final String WS_METHOD_ALL_BUSINESS="SelectAllBusinesses";
    public static final String WS_METHOD_CATEGORIES="SelectAllCategories";
    public static final String WS_METHOD_TOTAL_SEVING="GetTotalSavings";
    public static final String WS_METHOD_TOTAL_OFFER_LIKES="GetLikesCountForOffer";
    public static final String WS_METHOD_OFFER_COMMENTS="SelectCommentsByOffer";
    public static final String WS_METHOD_OFFER_BY_BUSINESS= "SelectOffersByBusinessId";
    public static final String WS_METHOD_CONSUMER_LOGIN="VerifyConsumerLogin";
    public static final String WS_METHOD_LIKE_OFFER="LikeOffer";
    public static final String WS_METHOD_DISLIKE_OFFER="DislikeOffer";
    public static final String WS_METHOD_LATEST_OFFERS="SelectLatestOffers";
    public static final String WS_METHOD_INVOICE_PDF="GetInvoiceForOfferUsage";
    public static final String WS_METHOD_IS_CARD_AVAILABLE="IsCardNumberAvailable";
    public static final String WS_METHOD_REGISTER_CONSUMER="RegisterConsumer";
    public static final String WS_METHOD_OFFER_BY_CATEGORY="SelectOffersByCategoryID";
    public static final String WS_METHOD_IS_OFFER_LIKED="IsOfferLikedByConsumer";
    public static final String WS_METHOD_LIKE_BUSINESS="LikeBusiness";
    public static final String WS_METHOD_DISLIKE_BUSINESS="DislikeBusiness";
    public static final String WS_METHOD_TOTAL_BUSINESS_LIKES="GetLikesCountForBusiness";
    public static final String WS_METHOD_BUSINESS_COMMENTS="SelectCommentsByBusiness";
    public static final String WS_METHOD_IS_BUSINESS_LIKED="IsBusinessLikedByConsumer";
    public static final String WS_METHOD_RATE_BUSINESS="RateBusiness";
    public static final String WS_METHOD_ADD_BUSINESS_COMMENT="AddBusinessCommentAsync";
    public static final String WS_METHOD_ADD_INTEREST_CATEGORY="AddCategoryTointerestListForConsumer";
    public static final String WS_METHOD_REMOVE_INTEREST_CATEGORY="RemoveCategoryFromInterestListForConsumer";
    public static final String WS_METHOD_ADD_OFFER_COMMENT="AddOfferComment";
    public static final String WS_METHOD_CONSUMER_DISCOUNT="GetDiscountsForConsumer";
    public static final String WS_METHOD_GET_INTEREST_CATEGORY="GetInterestedCategoriesByConsumer";
    public static final String WS_METHOD_CITIES="GetAllCities";
    public static final String WS_METHOD_OFFER_RATE="RateOffer";
    public static final String WS_METHOD_FEEDBACK="SubmitFeedback";
    public static final String WS_METHOD_SLAG="SelectContentBySlug";
    public static final String WS_METHOD_FAQ="SelectAllFAQQuestions";
    public static final String WS_METHOD_COUNTRIES="SelectAllCountries";
    public static final String WS_METHOD_OFFER_BY_FILTER="SelectOffersByFilter";
    public static final String WS_METHOD_LATEST_OFFER_BY_BUSINESS= "SelectLatestOffersByStoreId";
    public static final String WS_METHOD_SUB_CATEGORIES="SelectCategoryByParentID";

    public static final String CONSUMER_ID="consumerID";
    public static final String OFFER_ID="offerID";
    public static final String BUSINESS_ID="businessID";
    public static final String OFFER_USAGE_ID = "OfferUsageID";
    public static final String EMAIL="email";
    public static final String PASSWORD="password";
    public static final String COUNT="count";
    public static final String CARD_NUMBER="cardNumber";
    public static final String FIRST_NAME="FirstName";
    public static final String LAST_NAME="LastName";
    public static final String GENDER="Gender";
    public static final String BIRTHDAY="Birthday";
    public static final String MOBILE_NUMBER="MobileNumber";
    public static final String CATEGORY_ID="categoryID";
    public static final String COMMENT="Comment";

    public static ArrayList<ServiceRequest> getTotalSavingRequestList(String id){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName(CONSUMER_ID);
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(id);
        list.add(bean);

        return list;
    }

    public static ArrayList<ServiceRequest> getLatestOfferRequestList(String count){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName(COUNT);
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(count);
        list.add(bean);

        return list;
    }

    public static ArrayList<ServiceRequest> getLikesNumRequestList(String offerId){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName(OFFER_ID);
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(offerId);
        list.add(bean);

        return list;
    }

    public static ArrayList<ServiceRequest> getBusinessLikesNumRequestList(String businessId){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName(BUSINESS_ID);
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(businessId);
        list.add(bean);

        return list;
    }

    public static ArrayList<ServiceRequest> getLatestOfferByStoreId(String businessId, int count){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName("storeId");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(businessId);
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("count");
        bean.setType(PropertyInfo.INTEGER_CLASS);
        bean.setValue(count);
        list.add(bean);

        return list;
    }

    public static ArrayList<ServiceRequest> getOfferByBusinessRequestList(String businessId){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName(BUSINESS_ID);
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(businessId);
        list.add(bean);

        return list;
    }
    public static ArrayList<ServiceRequest> getLikeOfferRequestList(String id,String offerId){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName("OfferID");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(offerId);
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("ConsumerId");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(id);
        list.add(bean);

        return list;
    }
    public static ArrayList<ServiceRequest> getIsOfferLikedRequestList(String id,String offerId){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName("offerID");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(offerId);
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("consumerID");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(id);
        list.add(bean);

        return list;
    }

    public static ArrayList<ServiceRequest> getIsBusinessLikedRequestList(String id,String businessId){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName(BUSINESS_ID);
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(businessId);
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("consumerID");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(id);
        list.add(bean);

        return list;
    }
    public static ArrayList<ServiceRequest> getLoginRequestList(String userName,String password){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName(EMAIL);
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(userName);
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName(PASSWORD);
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(password);
        list.add(bean);

        return list;
    }

    public static ArrayList<ServiceRequest> getActivationCardRequestList(String cardNum){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName(CARD_NUMBER);
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(cardNum);
        list.add(bean);

        return list;
    }

    public static ArrayList<ServiceRequest> getActivationInformationList(String cardNum,PersonalInfo info){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName("CardNumber");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(cardNum);
        list.add(bean);


        bean = new ServiceRequest();
        bean.setName("FullName");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(info.getFullName());
        list.add(bean);


        bean = new ServiceRequest();
        bean.setName("Email");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(info.getUsername());
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("Password");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(info.getPassword());
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName(GENDER);
        bean.setType(PropertyInfo.OBJECT_CLASS);
        bean.setValue(info.getGender());
        list.add(bean);

        SimpleDateFormat formater=new SimpleDateFormat("dd/MM/yyyy");
        try {
            String dateString=info.getBirthday();
            Date d=new Date();
            if(dateString!=null) {
                if(dateString.length()>0)
                    d = formater.parse(info.getBirthday());
            }
            d.setHours(11);
            d.setMinutes(11);
            d.setSeconds(11);

            SimpleDateFormat formater2=new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
            String newDate=formater2.format(d);

            bean = new ServiceRequest();
            bean.setName(BIRTHDAY);
            bean.setType(PropertyInfo.STRING_CLASS);
            bean.setValue(newDate);
            list.add(bean);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        bean = new ServiceRequest();
        bean.setName(MOBILE_NUMBER);
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(info.getMobile());
        list.add(bean);


        if(info.getId()!=null){
            bean = new ServiceRequest();
            bean.setName("consumerID");
            bean.setType(PropertyInfo.STRING_CLASS);
            bean.setValue(Integer.parseInt(info.getId()));
            list.add(bean);
        }

        bean = new ServiceRequest();
        bean.setName("Job");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(info.getWork());
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("CityId");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(info.getCity());
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("IdNumber");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(info.getIdNum());
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("countryId");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(info.getCountry());
        list.add(bean);


        bean = new ServiceRequest();
        bean.setName("Education");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(info.getEducation());
        list.add(bean);


        return list;
    }
    public static ArrayList<ServiceRequest> getOffersByCategoryRequestList(String id){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName(CATEGORY_ID);
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(id);
        list.add(bean);

        return list;
    }

    public static ArrayList<ServiceRequest> getOffersByBusinessRequestList(String id){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName(BUSINESS_ID);
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(id);
        list.add(bean);

        return list;
    }

    public static ArrayList<ServiceRequest> getOfferInvoicePdfRequestList(int offerUsageId) {
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName(OFFER_USAGE_ID);
        bean.setType(PropertyInfo.INTEGER_CLASS);
        bean.setValue(offerUsageId);
        list.add(bean);

        return list;
    }

    public static ArrayList<ServiceRequest> getLikeBusinessRequestList(String id,String businessId){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName("businessID");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(businessId);
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("ConsumerId");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(id);
        list.add(bean);

        return list;
    }

    public static ArrayList<ServiceRequest> getRateBusinessRequestList(String id,String businessId,String rate){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName("businessID");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(businessId);
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("ConsumerId");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(id);
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("rateValue");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(rate);
        list.add(bean);

        return list;
    }

    public static ArrayList<ServiceRequest> getRateOfferRequestList(String id,String offerId,String rate){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName("offerID");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(offerId);
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("consumerID");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(id);
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("rateValue");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(rate);
        list.add(bean);

        return list;
    }

    public static ArrayList<ServiceRequest> getAddBusinessCommentRequestList(String id,String businessId,String comment){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName("comment");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(comment);
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("consumerId");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(id);
        list.add(bean);


        bean = new ServiceRequest();
        bean.setName("businessId");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(businessId);
        list.add(bean);



        return list;
    }
    public static ArrayList<ServiceRequest> getAddRemoveCategoryRequestList(String categoryId,String consumerId) {
        ArrayList<ServiceRequest> list = new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName("catId");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(categoryId);
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("consumerId");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(consumerId);
        list.add(bean);

        return list;
    }

    public static ArrayList<ServiceRequest> getAddOfferCommentRequestList(String id,String offerId,String comment){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName("comment");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(comment);
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("consumerId");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(id);
        list.add(bean);

        bean.setName("offerId");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(offerId);
        list.add(bean);

        return list;
    }
    public static ArrayList<ServiceRequest> getActivationInformationList(PersonalInfo info){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();


        bean = new ServiceRequest();
        bean.setName("CardNumber");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue("");
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("FullName");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(info.getFullName());
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("FirstName");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(info.getFirstName());
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("LastName");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(info.getLastName());
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("Email");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(info.getUsername());
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("Password");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(info.getPassword());
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName(GENDER);
        bean.setType(PropertyInfo.OBJECT_CLASS);
        bean.setValue(info.getGender());
        list.add(bean);

        SimpleDateFormat formater=new SimpleDateFormat("dd/MM/yyyy");
        try {
            String dateString=info.getBirthday();
            Date d=new Date();
            if(dateString!=null) {
                if(dateString.length()>0)
                    d = formater.parse(info.getBirthday());
            }
            d.setHours(11);
            d.setMinutes(11);
            d.setSeconds(11);

            SimpleDateFormat formater2=new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
            String newDate=formater2.format(d);

            bean = new ServiceRequest();
            bean.setName(BIRTHDAY);
            bean.setType(PropertyInfo.STRING_CLASS);
            bean.setValue(""+newDate);
            list.add(bean);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        bean = new ServiceRequest();
        bean.setName(MOBILE_NUMBER);
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(info.getMobile());
        list.add(bean);


        if(info.getId()!=null){
            bean = new ServiceRequest();
            bean.setName("consumerID");
            bean.setType(PropertyInfo.INTEGER_CLASS);
            bean.setValue(Integer.parseInt(info.getId()));
            list.add(bean);
        }

        bean = new ServiceRequest();
        bean.setName("workField");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(info.getWork());
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("CityId");
        bean.setType(PropertyInfo.INTEGER_CLASS);
        try {
            bean.setValue(Integer.parseInt(info.getCity()));
            list.add(bean);
        } catch (NumberFormatException e) {
            Log.e(ServicesConstants.class.getName(), "Can't parse CityId");
        }

        if(info.getCountry()!=null && !info.getCountry().equalsIgnoreCase("null")) {
            bean = new ServiceRequest();
            bean.setName("countryId");
            bean.setType(PropertyInfo.INTEGER_CLASS);
            bean.setValue(Integer.parseInt(info.getCountry()));
            list.add(bean);
        }

        bean = new ServiceRequest();
        bean.setName("IdNumber");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(info.getIdNum());
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("Education");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(info.getEducation());
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("Job");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(info.getWork());
        list.add(bean);

        return list;
    }

    public static ArrayList<ServiceRequest> getInterestCatRequestList(String consumerId){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName("consummerID");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(consumerId);
        list.add(bean);

        return list;
    }

    public static ArrayList<ServiceRequest> getFeedbackRequestParams(String userId, String feedback){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName("feedback");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(feedback);
        list.add(bean);


        bean = new ServiceRequest();
        bean.setName("consumer");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(userId);
        list.add(bean);

        //Create date params request
        Date d=new Date();

        d.setHours(11);
        d.setMinutes(11);
        d.setSeconds(11);

        SimpleDateFormat formater2=new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        String newDate=formater2.format(d);

        bean = new ServiceRequest();
        bean.setName("date");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(newDate);
        list.add(bean);

        return list;
    }

    public static ArrayList<ServiceRequest> getContentSlagRequestParams(String slag){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName("slug");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(slag);
        list.add(bean);

        return list;
    }

    public static ArrayList<ServiceRequest> getOffersByFilterRequestParams(SearchCriteria criteria){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName("CategoryIds");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(criteria.getCategories());
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("cityId");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(criteria.getCityId());
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("rating");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(criteria.getRate());
        list.add(bean);

        bean = new ServiceRequest();
        bean.setName("discounPercentage");
        bean.setType(PropertyInfo.STRING_CLASS);
        if (criteria.getDiscRate().equalsIgnoreCase("1000+")) {
            bean.setValue("1000-1000+");
        }
        else {
            bean.setValue(criteria.getDiscRate());
        }
        list.add(bean);

//        bean = new ServiceRequest();
//        bean.setName("rating");
//        bean.setType(PropertyInfo.STRING_CLASS);
//        bean.setValue(criteria.getRate());
//        list.add(bean);
//
//        bean = new ServiceRequest();
//        bean.setName("discountPercentage");
//        bean.setType(PropertyInfo.STRING_CLASS);
//        bean.setValue(criteria.getDiscRate());
//        list.add(bean);


        return list;
    }

    public static ArrayList<ServiceRequest> getSubCategoryRequestList(String parentId){
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName("ParentCategoryID");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(parentId);
        list.add(bean);

        return list;
    }

    public static ArrayList<ServiceRequest> getBusinessById(String businessId) {
        ArrayList<ServiceRequest> list=new ArrayList<ServiceRequest>();

        ServiceRequest bean = new ServiceRequest();
        bean.setName("businessID");
        bean.setType(PropertyInfo.STRING_CLASS);
        bean.setValue(businessId);
        list.add(bean);

        return list;
    }
}
