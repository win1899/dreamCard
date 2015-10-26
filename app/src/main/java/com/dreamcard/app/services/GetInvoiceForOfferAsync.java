package com.dreamcard.app.services;

import android.content.Context;
import android.util.Log;

import com.dreamcard.app.R;
import com.dreamcard.app.constants.Params;
import com.dreamcard.app.constants.ServicesConstants;
import com.dreamcard.app.entity.ErrorMessageInfo;
import com.dreamcard.app.entity.Offers;
import com.dreamcard.app.entity.ServiceRequest;
import com.dreamcard.app.utils.SystemOperation;
import com.dreamcard.app.view.interfaces.IServiceListener;

import org.json.JSONArray;
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
 * Created by WIN on 10/24/2015.
 */
public class GetInvoiceForOfferAsync extends AbstractAsyncTask<Object, Void, Object> {

    private Context context;
    private IServiceListener listener;
    private ArrayList<ServiceRequest> requestList = new ArrayList<ServiceRequest>();


    public GetInvoiceForOfferAsync(IServiceListener listener, ArrayList<ServiceRequest> list) {
        this.listener = listener;
        this.requestList = list;
    }

    @Override
    Object doInBackgroundSafe(Object[] data) {
        this.context = (Context) data[0];
        if (!SystemOperation.isOnline(this.context)) {
            ErrorMessageInfo bean = new ErrorMessageInfo();
            bean.setStatus("" + Params.STATUS_FAILED);
            bean.setMessage(this.context.getString(R.string.error_in_connection));
            return bean;
        }
        return callService();
    }

    @Override
    void onPostExecuteSafe(Object serviceResponse) {
        Log.i(GetInvoiceForOfferAsync.class.getName(), "OnPostExecute with: " + serviceResponse.toString());
        if (serviceResponse != null) {
            if (serviceResponse instanceof ErrorMessageInfo) {
                this.listener.onServiceFailed((ErrorMessageInfo) serviceResponse);
            } else {
                this.listener.onServiceSuccess(serviceResponse, 0);
            }
        }
    }


    private Object callService() {
        Object result = null;
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        String method = ServicesConstants.WS_METHOD_INVOICE_PDF;
        envelope.setOutputSoapObject(createRequest(method));
        envelope.dotNet = true;

        int timeout = Params.TIME_OUT;
        Object requestResult = new Object();
        for (int index = 0; index < Params.SERVICE_REQUEST_COUNT; index++) {
            try {
                HttpTransportSE httpTransport = new HttpTransportSE(ServicesConstants.WSDL_URL, timeout);
                httpTransport.debug = true;
                httpTransport.call(ServicesConstants.WS_ACTION + method, envelope);
                break;
            } catch (Exception e) {
                ErrorMessageInfo bean = new ErrorMessageInfo();
                bean.setStatus("" + Params.STATUS_FAILED);
                bean.setMessage(this.context.getString(R.string.error_in_connect_server));
                requestResult = bean;
            }
        }
        if (requestResult instanceof ErrorMessageInfo) {
            ErrorMessageInfo bean = (ErrorMessageInfo) requestResult;
            return bean;
        }
        if (envelope.bodyIn instanceof SoapObject) {
            try {
                if (envelope.getResponse() instanceof SoapPrimitive) {
                    String str = envelope.getResponse().toString();
                    str = str.replaceAll("\"", "");
                    return str;
                }
            } catch (SoapFault soapFault) {
                ErrorMessageInfo bean = new ErrorMessageInfo();
                bean.setMessage(this.context.getString(R.string.error_in_access_server));
                return bean;
            }
        } else if (envelope.bodyIn instanceof SoapFault) {
            ErrorMessageInfo bean = new ErrorMessageInfo();
            bean.setMessage(this.context.getString(R.string.error_in_access_server));
            return bean;
        } else {
            ErrorMessageInfo bean = new ErrorMessageInfo();
            bean.setMessage(this.context.getString(R.string.error_in_access_server));
            return bean;
        }
        return result;
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
}
