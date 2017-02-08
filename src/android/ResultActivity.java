package cordova.plugin.dinpay;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import cordova.plugin.dinpay.DinPay;

public class ResultActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle xmlData = getIntent().getExtras();
		if(xmlData!=null) {
			String response = xmlData.getString("xml");
			try{
				String status = "<trade_status>";
				int start = response.indexOf(status);
				int end = response.indexOf("</trade_status>");
				String str = response.substring(start+status.length(), end);
				DinPay.setPayResult(str);
			}catch (Exception e) {
				e.printStackTrace();
				DinPay.setPayResult("ERROR");
			}
		}
	}
}
