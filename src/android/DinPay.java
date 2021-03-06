package cordova.plugin.dinpay;

import android.content.Intent;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cordova.plugin.dinpay.OrderInfo;
//import permissions.dispatcher.NeedsPermission;


/**
 * This class echoes a string called from JavaScript.
 */
public class DinPay extends CordovaPlugin {

    private CallbackContext callbackContext;

    private static String payResult;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        try {
            if (action.equals("pay"))
            {
                pay(args);
                return true;
            }
            if (action.equals("payWithXML"))
            {
                payWithXML(args);
                return true;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
            e.printStackTrace(new java.io.PrintWriter(buf, true));
            String  expMessage = buf.toString();
            try {
                buf.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            callbackContext.error(expMessage);
            return false;
        }
        return false;
    }

//    @NeedsPermission(Mainfest.permission.READ_PHONE_STATE)
    public void pay(JSONArray args) throws Exception
    {
        JSONObject jsonObject = args.getJSONObject(0);
        OrderInfo orderInfo = new OrderInfo(jsonObject);
        String xml = orderInfo.toXML();
        Intent intent = new Intent(this.cordova.getActivity().getApplicationContext(), com.dinpay.plugin.activity.DinpayChannelActivity.class);
        Log.i("[pay_xml] = ", xml);
        intent.putExtra("xml", xml);
        intent.putExtra("ActivityName", "cordova.plugin.dinpay.ResultActivity");
        this.cordova.startActivityForResult(this, intent, 0);
    }

    public void payWithXML(JSONArray args) throws Exception
    {
        JSONObject jsonObject = args.getJSONObject(0);
        String xml = jsonObject.getString("data");
        Intent intent = new Intent(this.cordova.getActivity().getApplicationContext(), com.dinpay.plugin.activity.DinpayChannelActivity.class);
        Log.i("[pay_xml] = ", xml);
        intent.putExtra("xml", xml);
        intent.putExtra("ActivityName", "cordova.plugin.dinpay.ResultActivity");
        this.cordova.startActivityForResult(this, intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        callbackContext.success(payResult);
    }

    public static void setPayResult(String result) {
        DinPay.payResult = result;
    }
}

