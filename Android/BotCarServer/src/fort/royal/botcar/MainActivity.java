package fort.royal.botcar;

import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onConfigure(View view) {
		Intent intent = new Intent(this, ConfigureActivity.class);
		startActivity(intent);
	}
	
	public void onForward(View view) throws IOException {
		if (ConfigureActivity.mIsBluetoothConnected)
		{
			String command = ((char)64) + "FFFFFFFFFF";
			ConfigureActivity.mBTSocket.getOutputStream().write(command.getBytes());
		}
	}
	
	public void onBackward(View view) throws IOException {
		if (ConfigureActivity.mIsBluetoothConnected)
		{
			String command = ((char)64) + "BBBBBBBBBB";
			ConfigureActivity.mBTSocket.getOutputStream().write(command.getBytes());
		}
	}
	
	public void onLeft(View view) throws IOException {
		if (ConfigureActivity.mIsBluetoothConnected)
		{
			String command = ((char)128) + "LLLLL";
			ConfigureActivity.mBTSocket.getOutputStream().write(command.getBytes());
		}
	}
	
	public void onRight(View view) throws IOException {
		if (ConfigureActivity.mIsBluetoothConnected)
		{
			String command = ((char)128) + "8RRRRR";
			ConfigureActivity.mBTSocket.getOutputStream().write(command.getBytes());
		}
	}

}
