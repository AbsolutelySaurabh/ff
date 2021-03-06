package dd.com.myq.Activity;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import dd.com.myq.App.Config;
import dd.com.myq.R;
import dd.com.myq.Util.SessionManager;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.picasso.Picasso;

public class Badges extends AppCompatActivity {
    GridView gv;
    Context context;
    ArrayList prgmName;
    SessionManager currentSession;

    private ImageLoader imageLoader;
    public  ArrayList<String> prgmNameList;
    public  ArrayList<String> prgmNameList1;
    String user_id;



    //public  int [] prgmImages={R.drawable.badge20,R.drawable.badge50,R.drawable.badge100,R.drawable.circle_orange,R.drawable.cancel_btn,R.drawable.facebook_icon,R.drawable.fb_icon};
    public ArrayList<String> prgmImages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badges);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentSession = new SessionManager(this);

        HashMap<String, String> user_details = currentSession.getUserDetails();
        user_id = user_details.get(SessionManager.KEY_UID);

        gv=(GridView) findViewById(R.id.gridView1);

        prgmNameList=new ArrayList<String>();
        prgmNameList1=new ArrayList<String>();
        prgmImages=new ArrayList<String>();
        call_add_api();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void call_add_api()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        String addbadgeurl="http://myish.com:10011/api/addbadges/"+user_id;

        client.get(this,addbadgeurl , new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,JSONObject responseArray) {
                try {
                    callgetapi();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("exception_add=", String.valueOf(e));

                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.d("errorinadd=", String.valueOf(errorResponse));
            }

        });

    }

    public  void  callgetapi()
{
    AsyncHttpClient client = new AsyncHttpClient();
    String getbadgeurl="http://myish.com:10011/api/getbadges/"+user_id;

    client.get(this,getbadgeurl, new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers,JSONArray responseArray) {
            try {
                fn(responseArray);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("exception_get=", String.valueOf(e));

            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.d("erroringet=", String.valueOf(errorResponse));
        }
    });
}

    public void fn(JSONArray responseArray) {

        for (int i = 0; i < responseArray.length(); i++) {
            JSONObject object,object1;
            String badgedescription1 = null;
            String badgename1 = null;
            String badgeimage1= null;
            try {
                object = responseArray.getJSONObject(i);
                JSONArray badges = object.getJSONArray("badges");

                Log.d("badgekaarray", String.valueOf(badges));



                for (int j = 0; j < badges.length(); j++) {
                    object1 = badges.getJSONObject(j);

                     badgename1 = object1.getString("badgename");
                     badgedescription1 = object1.getString("badgedescription");
                    badgeimage1 = object1.getString("badgeimage");

                    prgmNameList.add(badgename1);
                    prgmNameList1.add(badgedescription1);
                    prgmImages.add(badgeimage1);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Log.d("badgesname", String.valueOf(prgmNameList));
      //  Log.d("badgesdesc", String.valueOf(prgmNameList1));

        gv.setAdapter(new CustomAdapter(this, prgmNameList, prgmNameList1,prgmImages));
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}