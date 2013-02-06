package com.example.vtresearch;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	Button submit, get;
	EditText busName, busStop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		submit = (Button) findViewById(R.id.button1);
		busName = (EditText) findViewById(R.id.editText1);
		busStop = (EditText) findViewById(R.id.editText2);
		get = (Button) findViewById(R.id.button2);

		submit.setOnClickListener(new OnClickListener() {

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View arg0) {
				Date currentTime = Calendar.getInstance().getTime();
				SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
				String time = formatter.format(currentTime);
				postToServer(busName.getText().toString(), busStop.getText()
						.toString(), time);
			}

		});

		get.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getFromServer(busName.getText().toString(), busStop.getText()
						.toString());
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void getFromServer(String busName, String busCode) {
		String line = "";
		try {
			HttpClient client = new DefaultHttpClient();
			String actUrl = "http://10.0.2.2:3000";
			HttpPost getRes = new HttpPost(actUrl);
			List<NameValuePair> names = new ArrayList<NameValuePair>();
			names.add(new BasicNameValuePair("busName", busName));
			names.add(new BasicNameValuePair("busCode", busCode));
			getRes.setEntity(new UrlEncodedFormEntity(names));
			HttpResponse response = client.execute(getRes);

			System.out.println(response.getEntity().getContentLength());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void postToServer(String name, String code, String time) {
		HttpClient client = new DefaultHttpClient();
		String url = "http://10.0.2.2:3000/post";
		HttpPost post = new HttpPost(url);

		try {
			List<NameValuePair> names = new ArrayList<NameValuePair>();
			names.add(new BasicNameValuePair("busName", name));
			names.add(new BasicNameValuePair("busCode", code));
			names.add(new BasicNameValuePair("timing", time));
			post.setEntity(new UrlEncodedFormEntity(names));

			client.execute(post);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
