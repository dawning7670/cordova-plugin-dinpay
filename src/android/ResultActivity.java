package cordova.plugin.dinpay;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

public class ResultActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle xmlData = getIntent().getExtras();
		if(xmlData!=null){
			String response = xmlData.getString("xml");
			try{
				String status = "<trade_status>";
				int start = response.indexOf(status);
				int end = response.indexOf("</trade_status>");
				String str = response.substring(start+status.length(), end);
				if("SUCCESS".equals(str)){
					setResult(0);
				}else if("UNPAY".equals(str)){
					setResult(-1);
				}else{
					setResult(1);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode==event.KEYCODE_BACK){
//			Intent intent =new Intent(ResultActivity.this,MainActivity.class);
//			startActivity(intent);
			this.finish();
		}
		return super.onKeyUp(keyCode, event);
	}
}
