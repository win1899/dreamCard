package com.dreamcard.app.services;

import android.content.Context;
import android.os.AsyncTask;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.Categories;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.entity.Stores;
import com.dreamcard.app.utils.SystemOperation;
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
public class CategoriesAsync extends AbstractAsyncTask<Object, Void, Object> {

    private Context context;
    private IServiceListener listener;
    private ArrayList<ServiceRequest> requestList=new ArrayList<ServiceRequest>();
    private int processType;
    private int requestType;

    public CategoriesAsync(IServiceListener listener,ArrayList<ServiceRequest> list,int processType, int requestType){
        this.listener=listener;
        this.processType=processType;
        this.requestList=list;
        this.requestType = requestType;
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
        String method = ServicesConstants.WS_METHOD_CATEGORIES;
        if (this.requestType == Params.TYPE_PARENT_CATEGORY
                || this.requestType == Params.TYPE_ALL_CATEGORY) {
            envelope.setOutputSoapObject(createRequest(method));
        }else if (this.requestType == Params.TYPE_SUB_CATEGORY) {
            method = ServicesConstants.WS_METHOD_SUB_CATEGORIES;
            envelope.setOutputSoapObject(createRequest(method));
        }
        int timeout= Params.TIME_OUT;
        Object requestResult=new Object();
        SoapObject  resultx=new SoapObject();
        for(int index=0;index<Params.SERVICE_REQUEST_COUNT;index++) {

            try {
                HttpTransportSE httpTransport = new HttpTransportSE(ServicesConstants.WSDL_URL);
                httpTransport.debug = true;
                httpTransport.call(ServicesConstants.WS_ACTION + method, envelope);
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
                    ArrayList<Categories> list=new ArrayList<Categories>();
                    try {
                        int index=0;
                        JSONArray jArray = new JSONArray(str);
                        for (int i=0; i < jArray.length(); i++){
                            JSONObject oneObject = jArray.getJSONObject(i);

                            if (this.requestType == Params.TYPE_PARENT_CATEGORY) {
                                if(oneObject.getString("ParentId") == null
                                        || oneObject.getString("ParentId").equalsIgnoreCase("null")
                                        || oneObject.getString("ParentId").equalsIgnoreCase("")){
                                    Categories bean=new Categories();
                                    bean.setId(oneObject.getString("Id"));
                                    bean.setTitle(oneObject.getString("Title"));
                                    bean.setLogo(oneObject.getString("Icon"));
                                    bean.setParentId(oneObject.getString("ParentId"));
                                    bean.setPosition(index);
                                    list.add(bean);
                                    index++;
                                }
                            }else{
                                Categories bean=new Categories();
                                bean.setId(oneObject.getString("Id"));
                                bean.setTitle(oneObject.getString("Title"));
                                bean.setLogo(oneObject.getString("Icon"));
                                bean.setParentId(oneObject.getString("ParentId"));
                                bean.setPosition(index);
                                list.add(bean);
                                index++;
                            }


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
    public SoapObject createRequest(String method) {
        SoapObject request = new SoapObject(ServicesConstants.WS_NAME_SPACE, method);
        for (ServiceRequest bean : this.requestList) {
            PropertyInfo propInfo = new PropertyInfo();
            propInfo.name = bean.getName();
            propInfo.type = bean.getType();
            propInfo.setValue(bean.getValue());
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
