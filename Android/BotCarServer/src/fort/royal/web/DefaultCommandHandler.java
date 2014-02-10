package fort.royal.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.json.JSONException;
import org.json.JSONObject;

import fort.royal.botcar.ConfigureActivity;
import fort.royal.botcar.R;

import android.content.Context;
import android.net.Uri;

public class DefaultCommandHandler implements HttpRequestHandler {
	private Context context = null;

	public DefaultCommandHandler(Context context) {
		this.context = context;
	}

	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext httpContext) throws HttpException, IOException {

		JSONObject json = new JSONObject();
		final Uri uri = Uri.parse(request.getRequestLine().getUri());
		final String layout = context.getResources().getString(R.string.html_layout);
		final String scripts = context.getResources().getString(R.string.html_script);
		
		String sDuration = uri.getQueryParameter("duration");
		String sSpeed = uri.getQueryParameter("speed");
		String dir = uri.getQueryParameter("dir");
		
		int duration = 5;
		if (sDuration != null)
			duration = Integer.parseInt(sDuration);
		
		int speed = 255;
		if (sSpeed != null)
			speed = Integer.parseInt(sSpeed); // bytes are signed in java
		
		final String content;
		String repeatDirection = "";
		if (dir != null) {// FBLR
			for (int i=0; i < duration; i++)
				repeatDirection += dir;

			String command = ((char)speed) + repeatDirection;
			try {
				json.put("duration", duration);
				json.put("speed", speed);
				json.put("direction", dir);
				json.put("command", command);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			content = json.toString();
			response.setHeader("Content-Type", "application/json");
			
			// Drive the car
			if (ConfigureActivity.mIsBluetoothConnected)
				ConfigureActivity.mBTSocket.getOutputStream().write(command.getBytes());
		}
		else // Regular page
		{
			String body = context.getResources().getString(R.string.html_controls);
			content = layout.replace("{body}", body).replace("{script}", scripts);
			response.setHeader("Content-Type", "text/html");
		}
		
		
		HttpEntity entity = new EntityTemplate(new ContentProducer() {
			public void writeTo(final OutputStream outstream) throws IOException {
				OutputStreamWriter writer = new OutputStreamWriter(outstream, "UTF-8");
				writer.write(content);
				writer.flush();
			}
		});
		response.setEntity(entity);
	}

	public Context getContext() {
		return context;
	}
}
