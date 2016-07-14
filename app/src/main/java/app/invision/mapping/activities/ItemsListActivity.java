package app.invision.mapping.activities;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import app.invision.mapping.Application;
import app.invision.mapping.Constants;
import app.invision.mapping.R;
import app.invision.mapping.models.ChildItem;
import app.invision.mapping.models.DataItem;
import app.invision.mapping.utils.Utils;

public class ItemsListActivity extends AppCompatActivity {

    private Application app;

    private ListView itemsListView;
    private MaterialSearchView searchView;
    private Toolbar toolbar;

    private ArrayList<DataItem> parentItems = new ArrayList<>();
    private ArrayList<ChildItem> childItems = new ArrayList<>();
    private DataItemAdapter adapter;
    private String token;



    private JSONObject object;
    private JSONArray parentJSONArray;
    private JSONObject childJSONObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.hideStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        app = (Application) getApplication();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.darker_gray));
        setSupportActionBar(toolbar);

        itemsListView = (ListView) findViewById(R.id.LVItems);
        adapter = new DataItemAdapter(parentItems);
        itemsListView.setAdapter(adapter);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Object> filtered = new ArrayList<>();

                if (newText.length() > 0) {

                    for(DataItem parent:parentItems)
                    {
                        if(parent.getName().toLowerCase().contains(newText))
                        {
                            filtered.add(parent);
                        }
                    }

                    for (ChildItem item : childItems) {
                        if (item.getName().toLowerCase().contains(newText))
                            filtered.add(item);
                    }
                    final ChildItemAdapter tempAdapter = new ChildItemAdapter(filtered);
                    itemsListView.setAdapter(tempAdapter);
                    tempAdapter.notifyDataSetChanged();
                } else {
                    itemsListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                return true;
            }
        });

        getToken();
    }

    private void getToken() {
        try {
            String URL = "https://uhjwxqetod.execute-api.us-west-2.amazonaws.com/Phase1/auth-user";
            JSONObject requestParam = new JSONObject();
            requestParam.put("user_id", "RazaMarchant");
            requestParam.put("client_id", "1000000");
            requestParam.put("password", "abc");

            if (Utils.isNetworkOnline(this)) {
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.NEW_Auth_URL, requestParam, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            Log.e(Application.TAG, jsonObject.toString());
                            boolean status = jsonObject.getBoolean("status");
                            String message = jsonObject.getString("message");
                            if (status) {
                                token = jsonObject.getString("token");
                                getDataItems(token);
                            } else {
                                Utils.showAlertDialogWithoutCancel(ItemsListActivity.this, "Authentication Failed", message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }
                });

                app.getQueue(this).add(request);
            } else {
                Utils.showAlertDialogWithoutCancel(this, "No Internet Connection", "Please connect to a network and try again");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDataItems(String token) {
        try {
            String URL = "https://uhjwxqetod.execute-api.us-west-2.amazonaws.com/Phase1/child-list-items";
            JSONObject requestParam = new JSONObject();
            requestParam.put("user_id", "RazaMarchant");
            requestParam.put("client_id", "1000000");
            requestParam.put("token", token);

            if (Utils.isNetworkOnline(this)) {
                final ProgressDialog dialog = ProgressDialog.show(this, "Initializing", "Downloading data from server");
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.NEW_GET_CHILD_LIST, requestParam, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            dialog.dismiss();
                            //Log.e(Application.TAG, jsonObject.toString());
                            boolean status = jsonObject.getBoolean("status");
                            String message = jsonObject.getString("message");
                            if (status) {
                                object = jsonObject.getJSONObject("data");
                                parentJSONArray = object.getJSONArray("avaiL_table");
                                childJSONObject = object.getJSONObject("item_table");
                                //Fill parent table
                                parentItems.clear();
                                for (int i = 0; i < parentJSONArray.length(); i++) {
                                    JSONObject o = parentJSONArray.getJSONObject(i);
                                    parentItems.add(new DataItem(
                                            o.getLong(Constants.JSON_KEY_S_MAPPKG_ID),
                                            o.getString(Constants.JSON_KEY_S_MAPPKG_CLIENT_NAME),
                                            o.getString(Constants.JSON_KEY_S_MAPPKG_MAPS_S_VER),
                                            o.getString(Constants.JSON_KEY_S_MAPPKG_MAPS_L_VER),
                                            o.getString(Constants.JSON_KEY_S_MAPPKG_ITEMS_VER),
                                            o.getString(Constants.JSON_KEY_S_MAPPKG_DESCRIPTION),
                                            o.getString(Constants.JSON_KEY_S_MAPPKG_MAPS_S_FILE),
                                            o.getJSONArray(Constants.JSON_KEY_S_MAPPKG_LOCATION).toString(),
                                            o.getString(Constants.JSON_KEY_S_MAPPKG_OVERVIEW_TOPO),
                                            o.getString(Constants.JSON_KEY_S_MAPPKG_MAPS_L_UP_UTC),
                                            o.getString(Constants.JSON_KEY_S_MAPPKG_PROVINCE),
                                            o.getString(Constants.JSON_KEY_S_MAPPKG_MAPS_L_FILE),
                                            o.getString(Constants.JSON_KEY_S_MAPPKG_COUNTRY),
                                            o.getString(Constants.JSON_KEY_S_MAPPKG_MAPS_S_UP_UTC),
                                            o.getString(Constants.JSON_KEY_S_MAPPKG_NAME),
                                            o.getString(Constants.JSON_KEY_S_MAPPKG_OVERVIEW_SATELLITE),
                                            o.getString(Constants.JSON_KEY_S_MAPPKG_TOWN),
                                            o.getString(Constants.JSON_KEY_S_MAPPKG_ITEMS_UP_UTC)
                                    ));
                                }

                                //Fill child tables
                                childItems.clear();
                                Iterator<String> iter = childJSONObject.keys();
                                while (iter.hasNext()) {
                                    String key = iter.next();
                                    try {
                                        JSONArray array = childJSONObject.getJSONArray(key);
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject o = array.getJSONObject(i);
                                            final ChildItem childItem = new ChildItem(
                                                    o.getString(Constants.JSON_KEY_PHOTO),
                                                    o.getString(Constants.JSON_KEY_SOCIAL_TAG),
                                                    o.getString(Constants.JSON_KEYCOORDINATES),
                                                    o.getInt(Constants.JSON_KEY_NUMBER),
                                                    o.getString(Constants.JSON_KEY_DIFFICULTY),
                                                    o.getString(Constants.JSON_KEY_DESCRIPTION),
                                                    o.getString(Constants.JSON_KEY_SUBTYPE),
                                                    o.getString(Constants.JSON_KEY_SPECIAL_REPORT),
                                                    o.getString(Constants.JSON_KEY_TYPE),
                                                    o.getJSONArray(Constants.JSON_KEY_THUMBNAIL).getString(0),
                                                    o.getString(Constants.JSON_KEY_COORDINATE_TYPE),
                                                    o.getString(Constants.JSON_KEY_DURATION),
                                                    o.getString(Constants.JSON_KEY_LINK),
                                                    o.getString(Constants.JSON_KEY_WEBCAM),
                                                    o.getString(Constants.JSON_KEY_STATUS),
                                                    o.getString(Constants.JSON_KEY_VIDEO),
                                                    o.getString(Constants.JSON_KEY_LOOKAT),
                                                    o.getString(Constants.JSON_KEY_NAME));
                                            childItem.setParentName(key);
                                            childItems.add(childItem);
                                        }
                                    } catch (JSONException e) {
                                        // Something went wrong!
                                        e.printStackTrace();
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                Utils.showAlertDialogWithoutCancel(ItemsListActivity.this, "Error", message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dialog.dismiss();
                        volleyError.printStackTrace();
                        Log.e(Application.TAG, volleyError.getMessage());
                    }
                });

                app.getQueue(this).add(request);
            } else {
                Utils.showAlertDialogWithoutCancel(this, "No Internet Connection", "Please connect to a network and try again");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            getToken();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.cancelAllRequests(this);
    }

    private class DataItemAdapter extends ArrayAdapter<DataItem> {

        public DataItemAdapter(ArrayList<DataItem> items) {
            super(ItemsListActivity.this, R.layout.list_item, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.list_item, null);

            final DataItem d = getItem(position);

            TextView nameTextView = (TextView) convertView.findViewById(R.id.TVName);
            TextView addressTextView = (TextView) convertView.findViewById(R.id.TVAddress);
            ImageView thumbnailImageView = (ImageView) convertView.findViewById(R.id.IVThumbnail);

            nameTextView.setText(d.getName());
            addressTextView.setText(d.getTown() + " " + d.getProvince() + " " + d.getCountry());
            // TODO Add image to thumbnail
            // Placeholder
            thumbnailImageView.setBackground(new ColorDrawable(getResources().getColor(android.R.color.white)));
            thumbnailImageView.setImageDrawable(getResources().getDrawable(R.drawable.bike));

            return convertView;
        }
    }

    private class ChildItemAdapter extends ArrayAdapter<Object> {

        public ChildItemAdapter(ArrayList<Object> items) {
            super(ItemsListActivity.this, R.layout.list_item, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.list_item, null);

            // new Logic


            final Object o = getItem(position);






            TextView nameTextView = (TextView) convertView.findViewById(R.id.TVName);
            TextView addressTextView = (TextView) convertView.findViewById(R.id.TVAddress);
            final ImageView thumbnailImageView = (ImageView) convertView.findViewById(R.id.IVThumbnail);
            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);


            try{
                final ChildItem c = (ChildItem) getItem(position);
                DataItem d = null;

            for (DataItem item : parentItems)
                if (c.getParentName().toLowerCase().contains(String.valueOf(item.getMapID())))
                    d = item;

            nameTextView.setText((d == null) ? "" : d.getName());
            addressTextView.setText(c.getDescription());
            // TODO Add image to thumbnail
            // Placeholder
            //thumbnailImageView.setBackground(new ColorDrawable(getResources().getColor(android.R.color.white)));
            Log.e(Application.TAG, Constants.IMAGE_URL + c.getThumbnail());
            Picasso.with(ItemsListActivity.this).load(Constants.IMAGE_URL + c.getThumbnail())
                    .placeholder(getResources().getDrawable(R.drawable.bike)).fit()
                    .into(thumbnailImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });

            }
            catch (ClassCastException e)
            {
                e.printStackTrace();

                final DataItem p = (DataItem) getItem(position);

                nameTextView.setText(p.getName());
              String loc = p.getTown()+" "+p.getProvince()+" "+p.getCountry();
                addressTextView.setText(loc);
                progressBar.setVisibility(View.GONE);

            }
            //thumbnailImageView.setImageDrawable(getResources().getDrawable(R.drawable.bike, null));
            return convertView;
        }
    }
}
