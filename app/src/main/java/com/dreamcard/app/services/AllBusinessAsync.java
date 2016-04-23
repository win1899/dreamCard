package com.dreamcard.app.services;

import android.content.Context;
import android.os.AsyncTask;
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

import org.json.JSONArray;
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
 * Created by Moayed on 6/24/2014.
 */
public class AllBusinessAsync extends AbstractAsyncTask<Object, Void, Object> {

    private Context context;
    private IServiceListener listener;
    private ArrayList<ServiceRequest> requestList=new ArrayList<ServiceRequest>();
    private int processType;

    public AllBusinessAsync(IServiceListener listener,ArrayList<ServiceRequest> list,int processType){
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
        SoapObject request = new SoapObject(ServicesConstants.WS_NAME_SPACE, ServicesConstants.WS_METHOD_ALL_BUSINESS);
        envelope.setOutputSoapObject(request);
        int timeout= Params.TIME_OUT;
        Object requestResult=new Object();
        SoapObject  resultx=new SoapObject();
        for(int index=0;index<Params.SERVICE_REQUEST_COUNT;index++) {

            try {
                HttpTransportSE httpTransport = new HttpTransportSE(ServicesConstants.WSDL_URL);
                httpTransport.debug = true;
                httpTransport.call(ServicesConstants.WS_ACTION+ServicesConstants.WS_METHOD_ALL_BUSINESS, envelope);
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
                    ArrayList<Stores> list=new ArrayList<Stores>();
                    try {
                        int index=0;
                        JSONArray jArray = new JSONArray(str);
                        for (int i=0; i < jArray.length(); i++){
                            JSONObject oneObject = jArray.getJSONObject(i);

                            Stores bean=new Stores();
                            bean.setId(oneObject.getString("Id"));
                            bean.setStoreName(oneObject.getString("BusinessName"));
                            bean.setAddress1(oneObject.getString("AddressLine1"));
                            bean.setAddress2(oneObject.getString("AddressLine2"));
                            bean.setCity(oneObject.getString("City"));
                            bean.setPhone(oneObject.getString("Phone"));
                            bean.setMobile(oneObject.getString("Mobile"));
                            bean.setWebsite(oneObject.getString("Website"));
                            bean.setFacebook(oneObject.getString("Facebook"));
                            bean.setRepresentativeName(oneObject.getString("RepresentativeName"));
                            bean.setEmail(oneObject.getString("Email"));
                            bean.setLogo(oneObject.getString("Logo"));
                            bean.setWideLogo(oneObject.getString("WideLogo"));
                            bean.setLatitude(oneObject.getString("Lat"));
                            bean.setLongitude(oneObject.getString("Long"));
                            bean.setOurMessage(oneObject.getString("OurMessage"));
                            bean.setMission(oneObject.getString("Mission"));
                            bean.setVision(oneObject.getString("Vision"));
                            String storeClass=oneObject.getString("Class");
                            if(storeClass==null || storeClass.length()==0 || storeClass.equalsIgnoreCase("null"))
                                bean.setStoreClass(0);
                            else
                                bean.setStoreClass(Integer.parseInt(storeClass));
                            String rating=oneObject.getString("Rating");
                            if(rating!=null){
                                if(!rating.equalsIgnoreCase("null") && rating.length()>0)
                                    bean.setRating(Integer.parseInt(rating.substring(0,1)));
                            }

                            String pictures=oneObject.getString("Pictures");
                            if (pictures != null && !pictures.equalsIgnoreCase("null") && pictures.length() > 0) {
                                bean.setPictures(pictures.split(";"));
                            }

                            String discountString = oneObject.optString("DiscountPercentage");
                            double discount = 0.0;
                            try {
                                discount = Double.parseDouble(discountString);
                            }
                            catch (Exception e) {
                                Log.i(AllBusinessAsync.class.getName(), e.getMessage());
                            }
                            bean.setDiscountPrecentage(discount);

                            JSONObject pointsObject = oneObject.getJSONObject("StorePoints");
                            if (pointsObject != null) {
                                String cashPoints = pointsObject.getString("Points");
                                if (cashPoints == null || cashPoints.equalsIgnoreCase("null")) {
                                    cashPoints = "0";
                                }
                                bean.setCashPoints(cashPoints);
                            }
                            bean.setPosition(index);
                            index++;
                            list.add(bean);
                        }
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
            SoapFault soapFault = (SoapFault) envelope.bodyIn;
            ErrorMessageInfo bean=new ErrorMessageInfo();
//            bean.setStatus(""+Params.STATUS_FAILED);
            bean.setMessage(this.context.getString(R.string.error_in_access_server));
            return bean;
        }else{
            ErrorMessageInfo bean=new ErrorMessageInfo();
//            bean.setStatus(""+Params.STATUS_FAILED);
            bean.setMessage(this.context.getString(R.string.error_in_access_server));
            return bean;
        }
        return result;
    }
    private Object parseSOAPResponse(SoapObject response){
        String status = response.getPrimitivePropertySafelyAsString("SelectAllOffersResult");
//        if(status.equalsIgnoreCase(""+Params.STATUS_NO_DATA_FOUND)){
//            ArrayList<Category> dataList=new ArrayList<Category>();
//            return dataList;
//        }else {
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
//        ArrayList<Category> dataList=new ArrayList<Category>();
//        for(Object b:list) {
//            SoapObject returnedValueNode= (SoapObject) b;
//            Category bean=new Category();
//            bean.setId(returnedValueNode.getPrimitivePropertySafelyAsString("ID"));
//            bean.setName(returnedValueNode.getPrimitivePropertySafelyAsString("name"));
//            bean.setUrl(returnedValueNode.getPrimitivePropertySafelyAsString("imageName"));
//            dataList.add(bean);
//        }
        return null;
    }

    /**
     * build request property info.
     * @return SoapObject
     */
    public SoapObject createRequest() {
        SoapObject request = new SoapObject(ServicesConstants.WS_NAME_SPACE, ServicesConstants.WS_METHOD_NAME_BUSINESS_BY_ID);
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
