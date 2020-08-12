package com.denzo.wakil.addFavorites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.denzo.wakil.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Favorites extends AppCompatActivity {
    private static final String HI = "https://uniqueandrocode.000webhostapp.com/hiren/favouritelist.php";
    private List<Product_List>product_lists;
    private RecyclerView recyclerView;
    ProductAdapter adapter;

    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    public static FavoriteDatabase favoriteDatabase;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        product_lists=new ArrayList<>();
        btn=(Button)findViewById(R.id.favbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Favorites.this,FavoriteListActivity.class));
            }
        });
        favoriteDatabase= Room.databaseBuilder(getApplicationContext(),FavoriteDatabase.class,"myfavdb").allowMainThreadQueries().build();


        getData();


    }

    private void getData() {
        request=new JsonArrayRequest(HI, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject=null;
                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject ob=response.getJSONObject(i);
                        Product_List pr=new Product_List(ob.getInt("id"),
                                ob.getString("product_name"),
                                ob.getString("product_img"));
                        product_lists.add(pr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setupData( product_lists);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void setupData(List<Product_List> product_lists) {
        adapter=new ProductAdapter(product_lists,getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

}
