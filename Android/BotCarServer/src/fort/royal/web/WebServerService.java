package fort.royal.web;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/*
Intent webServerService = new Intent(context, WebServerService.class);
context.startService(webServerService);
 */
public class WebServerService extends Service {

	private WebServer server = null;

	@Override
	public void onCreate() {
	    Toast.makeText(this, "Creating WebServerService", Toast.LENGTH_SHORT).show();
		Log.i("HTTPSERVICE", "Creating and starting httpService");
		super.onCreate();
		server = new WebServer(this);
		server.startServer();

	    Toast.makeText(this, "WebServerService.startServer...", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDestroy() {
		Log.i("HTTPSERVICE", "Destroying httpService");
		server.stopServer();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
