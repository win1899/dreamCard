package com.dreamcard.app.services;

import android.content.Context;
import android.util.Log;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.entity.UserInfo;
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
 * Created by WIN on 3/1/2016.
 */
public class RegisterDeviceGCMAsync extends AbstractAsyncTask<Object, Void, Object> {

    private Context context;
    private IServiceListener listener;
    private ArrayList<ServiceRequest> requestList=new ArrayList<ServiceRequest>();
    private int processType;

    public RegisterDeviceGCMAsync(IServiceListener listener, ArrayList<ServiceRequest> list, int processType){
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
        envelope.setOutputSoapObject(createRequest());
        envelope.dotNet = true;
        int timeout= Params.TIME_OUT;
        Object requestResult=new Object();
        for(int index=0; index<Params.SERVICE_REQUEST_COUNT; index++) {

            try {
                HttpTransportSE httpTransport = new HttpTransportSE(ServicesConstants.WSDL_URL,timeout);
                httpTransport.debug = true;
                httpTransport.call(ServicesConstants.WS_ACTION+ServicesConstants.WS_METHOD_ADD_GCM_TOKEN, envelope);
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
                    if(str==null || str.equalsIgnoreCase("null")){
                        ErrorMessageInfo bean= new ErrorMessageInfo();
                        bean.setMessage(this.context.getString(R.string.user_name_or_password_not_valid));
                        return bean;
                    }
                    return true;
                } else {
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
        bean.setMessage(this.context.getString(R.string.user_name_or_password_not_valid));
        return bean;
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
        SoapObject request = new SoapObject(ServicesConstants.WS_NAME_SPACE, ServicesConstants.WS_METHOD_ADD_GCM_TOKEN);
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
                Log.e("RegisterGCM", "Finished waiting ... calling onServiceSuccess");
                this.listener.onServiceSuccess(serviceResponse,this.processType);
            }
        }
    }
}
