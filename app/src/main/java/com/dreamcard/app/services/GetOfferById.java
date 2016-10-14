package com.dreamcard.app.services;

import android.content.Context;
import android.util.Log;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.utils.SystemOperation;
import com.dreamcard.app.utils.Utils;
import com.dreamcard.app.view.interfaces.IServiceListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by WIN on 10/14/2016.
 */
public class GetOfferById extends AbstractAsyncTask<Object, Void, Object> {

    private Context context;
    private IServiceListener listener;
    private ArrayList<ServiceRequest> requestList=new ArrayList<ServiceRequest>();
    private int processType;

    public GetOfferById(IServiceListener listener,ArrayList<ServiceRequest> list,int processType){
        this.listener=listener;
        this.processType=processType;
        this.requestList=list;
    }

    protected Object doInBackgroundSafe(Object... data) {
        this.context= (Context) data[0];
        if(!SystemOperation.isOnline(this.context)){
            ErrorMessageInfo bean=new ErrorMessageInfo();
            bean.setMessage(this.context.getString(R.string.error_in_connection));
            return bean;
        }
        return callService();
    }
    private Object callService(){
        Object result=null;
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        SoapObject request = createRequest();

        envelope.setOutputSoapObject(request);
        Object requestResult=new Object();
        for(int index=0;index<Params.SERVICE_REQUEST_COUNT;index++) {

            try {
                HttpTransportSE httpTransport = new HttpTransportSE(ServicesConstants.WSDL_URL);
                httpTransport.debug = true;
                httpTransport.call(ServicesConstants.WS_ACTION+ServicesConstants.WS_METHOD_NAME_OFFER_BY_ID, envelope);
                break;
            } catch (Exception e) {
                ErrorMessageInfo bean = new ErrorMessageInfo();
                bean.setStatus("" + Params.STATUS_FAILED);
                bean.setMessage(this.context.getString(R.string.error_in_connect_server));
                requestResult=bean;
            }
        }
        if(requestResult instanceof ErrorMessageInfo){
            ErrorMessageInfo bean= (ErrorMessageInfo) requestResult;
            return bean;
        }
        if (envelope.bodyIn instanceof SoapObject) {
            try {
                if(envelope.getResponse() instanceof SoapPrimitive){
                    String str = envelope.getResponse().toString();
                    ArrayList<Offers> list = new ArrayList<Offers>();
                    try {
                        int index=0;

                        JSONObject oneObject = new JSONObject(str);

                        Offers bean = new Offers();
                        bean.setId(oneObject.getString("Id"));
                        bean.setType(oneObject.getString("Type"));
                        bean.setBusinessId(oneObject.getString("BusinessId"));
                        bean.setDescription(oneObject.getString("Description"));
                        bean.setSaleOldPrice(oneObject.getString("SaleOldPrice"));
                        bean.setStatus(oneObject.getString("Status"));
                        bean.setTitle(oneObject.getString("Title"));
                        bean.setValidationPeriod(oneObject.getString("ValidationPeriod"));
                        bean.setValidFrom(oneObject.getString("ValidFrom"));
                        bean.setCurrency(oneObject.getString("Currency"));
                        bean.setOfferMainPhoto(oneObject.getString("MainPicture"));
                        bean.setNumOfLikes(oneObject.getString("LikesCount"));
                        bean.setCategoryIcon(oneObject.getString("CategoryIconURL"));
                        String pictures = oneObject.getString("Pictures");
                        if (pictures != null && !pictures.equalsIgnoreCase("null") && pictures.length() > 0) {
                            bean.setPicturesList(pictures.split(";"));
                        }

                        if (oneObject.getString("SaleNewPrice") == null
                                || oneObject.getString("SaleNewPrice").equalsIgnoreCase("null"))
                            bean.setSaleNewPrice("");
                        else
                            bean.setSaleNewPrice(oneObject.getString("SaleNewPrice"));
                        if (oneObject.getString("DiscountPercentage") == null
                                || oneObject.getString("DiscountPercentage").equalsIgnoreCase("null"))
                            bean.setDiscount("");
                        else
                            bean.setDiscount(oneObject.getString("DiscountPercentage") + "%");

                        if (oneObject.getString("Rating") == null
                                || oneObject.getString("Rating").equalsIgnoreCase("null")) {
                            bean.setOfferRating(0);
                        } else {
                            bean.setOfferRating(oneObject.getInt("Rating"));
                        }

                        if (oneObject.getString("RatingCount") == null
                                || oneObject.getString("RatingCount").equalsIgnoreCase("null")) {
                            bean.setRatingCount(0);
                        } else {
                            bean.setRatingCount(oneObject.getInt("RatingCount"));
                        }

                        bean.setPosition(index);

                        String business = oneObject.getString("business");
                        if (business != null
                                && !business.equalsIgnoreCase("null")) {
                            JSONObject businessObject = oneObject.getJSONObject("business");

                            bean.setMobile(businessObject.getString("Mobile"));
                            bean.setPhone(businessObject.getString("Phone"));
                            bean.setCity(businessObject.getString("AddressLine1"));
                            bean.setBusinessName(businessObject.getString("BusinessName"));
                            bean.setBusinessLogo(businessObject.getString("Logo"));
                            bean.setBusinessClass(businessObject.getString("Class"));

                            if (businessObject.getString("Lat") != null) {
                                if (businessObject.getString("Lat").length() > 0 && !businessObject.getString("Lat").equalsIgnoreCase("null")) {
                                    bean.setLatitude(Double.parseDouble(businessObject.getString("Lat")));
                                }
                            }

                            if (businessObject.getString("Long") != null) {
                                if (businessObject.getString("Long").length() > 0 && !businessObject.getString("Lat").equalsIgnoreCase("null")) {
                                    bean.setLongitude(Double.parseDouble(businessObject.getString("Long")));
                                }
                            }

                            String rating = businessObject.getString("Rating");
                            if (rating != null) {
                                if (!rating.equalsIgnoreCase("null") && rating.length() > 0) {
                                    int x = rating.indexOf(".");
                                    if (x == -1)
                                        bean.setRating(Integer.parseInt(rating));
                                    else
                                        bean.setRating(Integer.parseInt(rating.substring(0, x)));
                                }
                            }
                        }

                        index++;
                        list.add(bean);

                        return list;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    if(envelope.getResponse() instanceof Vector) {
                        Vector response = (Vector) envelope.getResponse();
                        if (response != null) {
                            result = parseSOAPResponse(response);
                        }
                    }else{
                        SoapObject soapObject = (SoapObject) envelope.getResponse();
                        result=parseSOAPResponse(soapObject);
                        return result;
                    }
                }

            } catch (SoapFault soapFault) {
                soapFault.printStackTrace();
            }
        } else if (envelope.bodyIn instanceof SoapFault) {
            ErrorMessageInfo bean=new ErrorMessageInfo();
            bean.setMessage(this.context.getString(R.string.error_in_access_server));
            return bean;
        }else{
            ErrorMessageInfo bean=new ErrorMessageInfo();
            bean.setMessage(this.context.getString(R.string.error_in_access_server));
            return bean;
        }
        return result;
    }
    private Object parseSOAPResponse(SoapObject response){
        String status = response.getPrimitivePropertySafelyAsString("SelectAllOffersResult");
        ErrorMessageInfo bean = new ErrorMessageInfo();
        bean.setStatus(status);
        bean.setMessage(response.getPrimitivePropertySafelyAsString("message"));
        return bean;
//        }
    }
    private Object parseSOAPResponse(Vector list) {
        if(list.size()>0){
            SoapObject returnedValueNode= (SoapObject) list.get(0);
            String status = returnedValueNode.getPrimitivePropertySafelyAsString("status");
            if(!status.equalsIgnoreCase(""+ Params.STATUS_SUCCESS)){
                ErrorMessageInfo bean=new ErrorMessageInfo();
                bean.setStatus(status);
                bean.setMessage(returnedValueNode.getPrimitivePropertySafelyAsString("message"));
                return bean;
            }else
                list.remove(0);
        }

        return null;
    }

    /**
     * build request property info.
     * @return SoapObject
     */
    public SoapObject createRequest() {
        SoapObject request = new SoapObject(ServicesConstants.WS_NAME_SPACE, ServicesConstants.WS_METHOD_NAME_OFFER_BY_ID);
        for(ServiceRequest bean:this.requestList){
            PropertyInfo propInfo=new PropertyInfo();
            propInfo.name=bean.getName();
            propInfo.type=bean.getType();
            propInfo.setValue(Utils.toEnUs(bean.getValue()));
            request.addProperty(propInfo);
        }
        return request;
    }

    protected void onPostExecuteSafe(Object serviceResponse) {
        if(serviceResponse!=null) {
            if (serviceResponse instanceof ErrorMessageInfo) {
                this.listener.onServiceFailed((ErrorMessageInfo) serviceResponse);
            } else {
                this.listener.onServiceSuccess(serviceResponse,this.processType);
            }
        }
    }
}