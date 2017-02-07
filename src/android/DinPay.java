package cordova.plugin.dinpay;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;

import com.dinpay.plugin.activity.DinpayChannelActivity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;
import java.util.Vector;

/**
 * This class echoes a string called from JavaScript.
 */
public class DinPay extends CordovaPlugin {

    private CallbackContext callbackContext;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        if (action.equals("start"))
        {
            start();
            return true;
        }
        return false;
    }

    public void start()
    {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<dinpay>\n" +
                "　　<request>\n" +
                "　　<merchant_code>218002</merchant_code>\n" +
                "　　<notify_url>http://www.merchant.com/receiveNotify</notify_url>\n" +
                "　　<interface_version>V3.0</interface_version>\n" +
                "　　<sign_type>RSA</sign_type>\n" +
                "　　<sign>56ae9c3286886f76e57e0993625c71fe</sign>\n" +
                "　　<trade>\n" +
                "　　<order_no>2181230245</order_no>\n" +
                "　　<order_time>2013-05-10 11:20:01</order_time>\n" +
                "　　<order_amount>100</order_amount>\n" +
                "　　<product_name>移动手机充值卡</product_name>\n" +
                "　　<extra_return_param>tid=123</extra_return_param>\n" +
                "　　</trade>\n" +
                "　　</request>\n" +
                "</dinpay>\n";
        Intent intent = new Intent(this.cordova.getActivity(), DinpayChannelActivity.class);
        intent.putExtra("xml", xml);
        intent.putExtra("ActivityName", "cordova.plugin.dinpay.ResultActivity");
        this.cordova.startActivityForResult(this, intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        callbackContext.success(resultCode);
    }
}

