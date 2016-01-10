package com.coconut.atv;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.coconut.R;
import com.coconut.fragment.FrgCalendar;
import com.coconut.util.network.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONObject;

public class AtvMain extends AppCompatActivity implements Response.Listener, Response.ErrorListener {
    private final String TAG = getClass().getSimpleName();

    NavigationView naviView = null;
    DrawerLayout dlMain = null;

    private FrameLayout flFragmentView = null;

    final String IMAGE_URL = "https://scontent.xx.fbcdn.net/hphotos-xtf1/v/t1.0-9/12227181_976201605784169_1612126058527332888_n.jpg?oh=53a552410f19dacd8c11bfd291e15cce&oe=56DFF1CC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atv_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        flFragmentView = (FrameLayout) findViewById(R.id.flFragmentView);
        naviView = (NavigationView) findViewById(R.id.naviView);
        dlMain = (DrawerLayout) findViewById(R.id.dlMain);
        naviView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                toolbar.setTitle(menuItem.getTitle());

                switch (menuItem.getNumericShortcut()) {
                    case '2':
                        Log.i(TAG, "[PYH] 부서원 일정");
                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();

                        FrgCalendar frgCalendar = new FrgCalendar();
                        transaction.add(R.id.flFragmentView, frgCalendar);
                        transaction.commit();

                        break;
                }

                dlMain.closeDrawer(naviView);
                return true;
            }
        });


        ImageRequest profileRequest = new ImageRequest(
                IMAGE_URL,
                this,
                (int) getResources().getDimension(R.dimen.header_drawer_image),
                (int) getResources().getDimension(R.dimen.header_drawer_image),
                ImageView.ScaleType.CENTER,
                Bitmap.Config.ARGB_8888,
                this
        );

        JsonRequest jsonRequest = new JsonObjectRequest("http://220.73.135.178:8214/whowho_app/ver2/app2_scid_get.jsp?I_SCH_PH=0215889999&I_USER_ID=ppyh0610@gmail.com&I_USER_PH=01091115151&I_LANG=ko&I_IN_OUT=I&I_CALL_TYPE=P&I_RQ_TYPE=1&I_PH_BOOK_FLAG=N&I_PH_COUNTRY=82&I_FAKE_PH=0215889999", this, this);

        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(profileRequest);
        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().add(jsonRequest);


//        nivProfile.setImageUrl(IMAGE_URL, V);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_atv_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(final Object response) {

        if (response instanceof Bitmap) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView nivProfile = (ImageView) findViewById(R.id.nivPhofile);
                    nivProfile.setImageBitmap((Bitmap) response);
                }
            });
        }

        if(response instanceof JSONObject) {
            Gson gson = new Gson();
            Log.i(TAG, "onResponse : " + gson.toJson(response));
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
    }
}
