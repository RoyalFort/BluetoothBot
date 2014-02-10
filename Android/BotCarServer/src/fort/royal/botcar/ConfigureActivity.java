package fort.royal.botcar;

import java.io.IOException;
import java.util.UUID;

import fort.royal.web.WebServerService;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class ConfigureActivity extends Activity {

	private static final String TAG = "ConfigureActivity";
	private CheckBox cbConnectBt;
	private CheckBox cbWebHost;
	private Button btnRefreshBt;

	public static BluetoothSocket mBTSocket;
	private ProgressDialog progressDialog;
	public static boolean mIsBluetoothConnected = false;

	private BluetoothDevice mDevice;
	private UUID mDeviceUUID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configure);
		
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		if (b != null) {
			mDevice = b.getParcelable(ConfigureBluetooth.DEVICE_EXTRA);
			mDeviceUUID = UUID.fromString(b.getString(ConfigureBluetooth.DEVICE_UUID));
		}
		
		cbConnectBt = (CheckBox) findViewById(R.id.cbConnectBt);
		cbWebHost = (CheckBox) findViewById(R.id.cbWebHost);
		btnRefreshBt = (Button) findViewById(R.id.btnRefreshBt);
		
		btnRefreshBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onConfigureBluetooth(view);
			}
		});
		cbConnectBt.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				if (mDevice != null) {
					if (isChecked) {
						if (mBTSocket == null || !mIsBluetoothConnected) {
							new ConnectBT().execute();
						}
					}
					else {
						if (mBTSocket != null && mIsBluetoothConnected) {
							new DisConnectBT().execute();
						}
					}
				}
				else {
					msg("Bluetooth is not configured.");
				}
			}
		});
		final Activity me = this;
		cbWebHost.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				if (isChecked) {

				    // Start web server
				    msg("Starting web service");
				    Intent webServerService = new Intent(me, WebServerService.class);
				    me.startService(webServerService);
				    msg("Please wait");
				}
				else {
				    // Stop web server
				    Intent webServerService = new Intent(me, WebServerService.class);
				    me.stopService(webServerService);
				    msg("Stopped web service");
				}
			}
		});
	}

	public void onConfigureBluetooth(View view) {
		Intent intent = new Intent(this, ConfigureBluetooth.class);
		startActivity(intent);
	}
	
	private void msg(String s) {
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	}

	private class DisConnectBT extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Void... params) {

			/*if (mReadThread != null) {
				mReadThread.stop();
				while (mReadThread.isRunning())
					; // Wait until it stops
				mReadThread = null;
			}*/

			try {
				mBTSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mIsBluetoothConnected = false;
		}

	}

	private class ConnectBT extends AsyncTask<Void, Void, Void> {
		private boolean mConnectSuccessful = true;

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(ConfigureActivity.this, "Hold on", "Connecting");// http://stackoverflow.com/a/11130220/1287554
		}

		@Override
		protected Void doInBackground(Void... devices) {

			try {
				if (mBTSocket == null || !mIsBluetoothConnected) {
					mBTSocket = mDevice.createRfcommSocketToServiceRecord(mDeviceUUID);
					BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
					mBTSocket.connect();
				}
			} catch (IOException e) {
				// Unable to connect to device
				e.printStackTrace();
				mConnectSuccessful = false;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (!mConnectSuccessful) {
				Toast.makeText(getApplicationContext(), "Could not connect to device. Is it a Serial device? Also check if the UUID is correct in the settings", Toast.LENGTH_LONG).show();
				finish();
			} else {
				msg("Connected to device");
				mIsBluetoothConnected = true;
				//mReadThread = new ReadInput(); // Kick off input reader
			}

			progressDialog.dismiss();
		}

	}

}
