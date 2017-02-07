package cordova.plugin.dinpay;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import cordova.plugin.dinpay.R;

/**
 * @author yw
 * 商家接收智付手机插件返回数据界面
 */
public class MerchantPayResultActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchant_pay_result);
		
		TextView merchant_payResult = (TextView)this.findViewById(
				R.id.merchant_payResult);
		merchant_payResult.setText("支付结果：支付成功");
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode==event.KEYCODE_BACK){
//			Intent intent =new Intent(MerchantPayResultActivity.this,MainActivity.class);
//			startActivity(intent);
			this.finish();
		}
		return super.onKeyUp(keyCode, event);
	}
}
