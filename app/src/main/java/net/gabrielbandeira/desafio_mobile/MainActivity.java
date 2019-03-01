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
                            TextView saldo = (TextView)findViewById(R.id.text_center);
                            NumberFormat formatter = NumberFormat.getCurrencyInstance();
                            String moneyString = formatter.format(resource.balance);
                            saldo.setText("Disponível R$ "+moneyString);

                        }

                        @Override
                        public void onFailure(Call<Resume> call, Throwable t) {
                            call.cancel();
                        }
                    });
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
                saldo.setText("Disponível "+moneyString);

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
        Call<cardUsage> call1 = apiInterface.getGastos();
        call1.enqueue(new Callback<cardUsage>() {
            @Override
            public void onResponse(Call<cardUsage> call, Response<cardUsage> response) {


                Log.d("TAG",response.code()+"");

                String displayResponse = "";

                cardUsage resource = response.body();
                /*TextView saldo = (TextView)findViewById(R.id.profile_relative_layout_gasto);
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                String moneyString = formatter.format(resource.balance);
                saldo.setText("Disponível "+moneyString);*/

            }

            @Override
            public void onFailure(Call<cardUsage> call1, Throwable t) {
                /*TextView saldo = (TextView)findViewById(R.id.profile_relative_layout_saldo);
                saldo.setText("Disponível Erro ao carregar saldo");*/
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
    Call<cardUsage> getGastos();

}
class Resume {

    @SerializedName("balance")
    public Double balance;

}

class cardUsage {

    @SerializedName("balance")
    public Double balance;
}