package net.gabrielbandeira.desafio_mobile;

import android.graphics.Color;
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
import android.widget.FrameLayout;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

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
            FrameLayout view_cartao = (FrameLayout)findViewById(R.id.view_cartao);
            com.jjoe64.graphview.GraphView view_grafico = (com.jjoe64.graphview.GraphView)findViewById(R.id.graph);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    view_cartao.setVisibility(FrameLayout.VISIBLE);
                    view_grafico.setVisibility(com.jjoe64.graphview.GraphView.GONE);
                    return true;
                case R.id.navigation_dashboard:
                    view_cartao.setVisibility(FrameLayout.GONE);
                    view_grafico.setVisibility(com.jjoe64.graphview.GraphView.VISIBLE);
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

                int index = 0;
                DataPoint[] dadosGrafico= new DataPoint[resource.size()];
                for(CardUsage gasto: resource){
                    valorGasto += gasto.value;
                    dadosGrafico[index++]=new DataPoint(Double.parseDouble(gasto.month), gasto.value);
                }
                GraphView graph = (GraphView) findViewById(R.id.graph);
                BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dadosGrafico);
                graph.addSeries(series);

// styling
                series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                    @Override
                    public int get(DataPoint data) {
                        return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
                    }
                });

                series.setSpacing(15);

// draw values on top
                series.setDrawValuesOnTop(false);
                series.setValuesOnTopColor(Color.RED);
                series.setOnDataPointTapListener(new OnDataPointTapListener() {
                    @Override
                    public void onTap(Series series, DataPointInterface dataPoint) {
                        NumberFormat formatter = NumberFormat.getCurrencyInstance();
                        String moneyString = formatter.format(dataPoint.getY());
                        Toast.makeText(getBaseContext(), "Mês: "+dataPoint.getX()+", Valor: "+moneyString, Toast.LENGTH_SHORT).show();
                    }
                });
                graph.getGridLabelRenderer().setGridStyle( GridLabelRenderer.GridStyle.HORIZONTAL );
                graph.getViewport().setScrollable(true); // enables horizontal scrolling
                graph.getViewport().setScrollableY(true); // enables vertical scrolling
                graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
                graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
//series.setValuesOnTopSize(50);
                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            // show normal x values
                            return super.formatLabel(value, isValueX);
                        } else {
                            // show currency for y values
                            NumberFormat formatter = NumberFormat.getCurrencyInstance();
                            String moneyString = formatter.format(value);
                            return moneyString;
                        }
                    }
                });
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