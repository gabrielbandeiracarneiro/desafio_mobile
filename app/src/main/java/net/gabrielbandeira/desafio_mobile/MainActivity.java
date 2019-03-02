package net.gabrielbandeira.desafio_mobile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    APIInterface apiInterface;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_dashboard:
                    //mTextMessage.setText(R.string.title_home);

                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        /**
         GET List Resources
         **/
        Call<Resume> call = apiInterface.getResume();
        call.enqueue(new Callback<Resume>() {
            @Override
            public void onResponse(Call<Resume> call, Response<Resume> response) {


                Log.d("TAG",response.code()+"");

                String displayResponse = "";

                Resume resource = response.body();
                TextView saldo = (TextView)findViewById(R.id.profile_relative_layout_saldo);
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                String moneyString = formatter.format(resource.balance);
                saldo.setText(moneyString);

            }

            @Override
            public void onFailure(Call<Resume> call, Throwable t) {
                TextView saldo = (TextView)findViewById(R.id.profile_relative_layout_saldo);
                saldo.setText("Disponível Erro ao carregar saldo");
            }
        });
        /**
         GET List Resources
         **/
        Call<List<CardUsage>> call1 = apiInterface.getGastos();
        call1.enqueue(new Callback<List<CardUsage>>() {
            @Override
            public void onResponse(Call<List<CardUsage>> call, Response<List<CardUsage>> response) {


                Log.d("TAG",response.code()+"");

                List<CardUsage> resource = response.body();
                Double valorGasto = 0.0;
                for(CardUsage gasto: resource){
                    valorGasto += gasto.value;
                }

                TextView gasto = (TextView)findViewById(R.id.profile_relative_layout_gasto);
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                String moneyString = formatter.format(valorGasto);
                gasto.setText(moneyString);

            }

            @Override
            public void onFailure(Call<List<CardUsage>> call, Throwable t) {
                Log.d("TAG-ERRO",t.getMessage()+"");
                /*TextView saldo = (TextView)findViewById(R.id.profile_relative_layout_saldo);
                saldo.setText("Disponível Erro ao carregar saldo");*/
            }
        });

        /**
         GET List Resources
         **/
        Call<Profile> call2 = apiInterface.getProfile();
        call2.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {


                Log.d("TAG",response.code()+"");

                Profile resource = response.body();


                TextView gasto = (TextView)findViewById(R.id.numero_cartao);
                gasto.setText("XXXX XXXX XXXX "+resource.cardNumber.substring(12,16));

            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Log.d("TAG-ERRO",t.getMessage()+"");
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    /*@Override
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
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
class APIClient {

    private static Retrofit retrofit = null;

    static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        retrofit = new Retrofit.Builder()
                .baseUrl("https://2hm1e5siv9.execute-api.us-east-1.amazonaws.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();



        return retrofit;
    }

}
interface APIInterface {

    @GET("/dev/resume")
    Call<Resume> getResume();

    @GET("/dev/card-usage")
    Call<List<CardUsage>> getGastos();

    @GET("/dev/users/profile")
    Call<Profile> getProfile();

}
class Resume {

    @SerializedName("balance")
    public Double balance;

}

class CardUsage {

    @SerializedName("month")
    public String month;
    @SerializedName("name")
    public String name;
    @SerializedName("value")
    public Double value;

}

class Profile {

    @SerializedName("name")
    public String name;
    @SerializedName("cardNumber")
    public String cardNumber;
    @SerializedName("expirationDate")
    public String expirationDate;

}