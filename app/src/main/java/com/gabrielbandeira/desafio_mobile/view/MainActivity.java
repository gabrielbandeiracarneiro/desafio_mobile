package com.gabrielbandeira.desafio_mobile.view;

import com.gabrielbandeira.desafio_mobile.R;
import com.gabrielbandeira.desafio_mobile.model.CardStatement;
import com.gabrielbandeira.desafio_mobile.model.CardUsage;
import com.gabrielbandeira.desafio_mobile.model.Compras;
import com.gabrielbandeira.desafio_mobile.model.Profile;
import com.gabrielbandeira.desafio_mobile.model.Resume;
import com.gabrielbandeira.desafio_mobile.presenter.APIPresenter;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Date data_extrato = new Date();
    private int month_extrato;
    private int year_extrato;
    private int pagina_extrato = 1;
    APIPresenter apiPresenter = new APIPresenter(this);

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FrameLayout view_cartao = (FrameLayout)findViewById(R.id.view_cartao);
            com.jjoe64.graphview.GraphView view_grafico = (com.jjoe64.graphview.GraphView)findViewById(R.id.graph);
            ScrollView extrato = (ScrollView)findViewById(R.id.scrool_extrato);

            limparExtrato();
            data_extrato = new Date();
            pagina_extrato = 1;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(data_extrato);
            month_extrato = calendar.get(Calendar.MONTH);
            year_extrato = calendar.get(Calendar.YEAR);

            apiPresenter.getCardStatement(month_extrato,year_extrato,pagina_extrato);
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    view_cartao.setVisibility(FrameLayout.VISIBLE);
                    view_grafico.setVisibility(com.jjoe64.graphview.GraphView.GONE);
                    extrato.setVisibility(ScrollView.VISIBLE);

                    return true;
                case R.id.navigation_dashboard:

                    view_cartao.setVisibility(FrameLayout.GONE);
                    view_grafico.setVisibility(com.jjoe64.graphview.GraphView.VISIBLE);
                    extrato.setVisibility(ScrollView.VISIBLE);

                    return true;
            }

            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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



        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data_extrato);
        month_extrato = calendar.get(Calendar.MONTH);
        year_extrato = calendar.get(Calendar.YEAR);
        limparExtrato();
        apiPresenter.getCardStatement(month_extrato,year_extrato,pagina_extrato);
        apiPresenter.getCardUsage();
        apiPresenter.getProfile();
        apiPresenter.getResume();

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

    public void cardStatementReady(CardStatement cardStatement) {
        LinearLayout linha;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int width_text = (width-(int)convertDpToPx(this,75))/2;
        for(Compras compra: cardStatement.getPurchases()){
            linha = new LinearLayout(getBaseContext());
            linha.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linha.setOrientation(LinearLayout.HORIZONTAL);
            TextView texto = new TextView(linha.getContext());

            Locale local = new Locale("pt","BR");
            DateFormat dateFormat = new SimpleDateFormat("dd MMM",local);

            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String moneyString = formatter.format(compra.getValue());
            Log.d("D",compra.getDate().toString());
            texto.setText(dateFormat.format(compra.getDate()));
            texto.setLayoutParams(new LinearLayout.LayoutParams((int)convertDpToPx(this,75),(int)convertDpToPx(this,75)));
            texto.setBackgroundColor(0x64EBEBEB);
            texto.setGravity((Gravity.CENTER));
            linha.addView(texto);

            ImageView imagem = new ImageView(linha.getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins((int)convertDpToPx(this,-70), (int)convertDpToPx(this,3), (int)convertDpToPx(this,70), 0);
            lp.gravity = Gravity.CENTER_VERTICAL;
            imagem.setLayoutParams(lp);
            imagem.setImageResource(R.drawable.vermelho);
            linha.addView(imagem);

            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(linha.getContext());
            horizontalScrollView.setLayoutParams(new LinearLayout.LayoutParams(width_text, (int)convertDpToPx(this,75)));

            texto = new TextView(getBaseContext());
            texto.setText(compra.getStore());
            texto.setTextColor(Color.BLACK);
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, (int)convertDpToPx(this,25), (int)convertDpToPx(this,15), 0);
            texto.setGravity((Gravity.CENTER_VERTICAL));
            horizontalScrollView.addView(texto);
            linha.addView(horizontalScrollView);

            horizontalScrollView = new HorizontalScrollView(linha.getContext());

            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)convertDpToPx(this,75));
            lp.setMargins(0, 0, (int)convertDpToPx(this,15), 0);
            lp.gravity = Gravity.RIGHT;
            horizontalScrollView.setLayoutParams(lp);
            horizontalScrollView.setForegroundGravity(Gravity.RIGHT);

            LinearLayout linha1 = new LinearLayout(horizontalScrollView.getContext());
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.RIGHT;
            linha1.setLayoutParams(lp);
            linha1.setGravity(Gravity.RIGHT);
            linha1.setOrientation(LinearLayout.HORIZONTAL);
            linha1.setHorizontalGravity(Gravity.RIGHT);

            texto = new TextView(linha1.getContext());
            texto.setText("R$ ");
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, (int)convertDpToPx(this,25), 0, 0);
            texto.setLayoutParams(lp);

            linha1.addView(texto);

            texto = new TextView(linha1.getContext());
            texto.setText(moneyString.substring(2,moneyString.length()));
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, (int)convertDpToPx(this,25), 0, 0);
            texto.setLayoutParams(lp);
            texto.setTextSize(18);

            linha1.addView(texto);

            horizontalScrollView.addView(linha1);

            linha.addView(horizontalScrollView);

            LinearLayout extrato = (LinearLayout) findViewById(R.id.extrato);
            extrato.addView(linha);

        }
        pagina_extrato++;
        Log.d("D",month_extrato+", "+year_extrato+", "+pagina_extrato);
        if(cardStatement.getLastPage()>=pagina_extrato){
            apiPresenter.getCardStatement(month_extrato,year_extrato,pagina_extrato);
        }
    }
    public float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public void cardUsageReady(List<CardUsage> cardUsage) {
        Double valorGasto = 0.0;

        int index = 0;
        DataPoint[] dadosGrafico= new DataPoint[cardUsage.size()];
        for(CardUsage gasto: cardUsage){
            valorGasto += gasto.getValue();
            dadosGrafico[index++]=new DataPoint(Double.parseDouble(gasto.getMonth()), gasto.getValue());
        }
        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dadosGrafico);
        graph.addSeries(series);

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(15);

        series.setDrawValuesOnTop(false);
        series.setValuesOnTopColor(Color.RED);
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {

                limparExtrato();
                month_extrato = (int)dataPoint.getX();
                pagina_extrato = 1;
                Log.d("D",month_extrato+", "+year_extrato+", "+pagina_extrato);
                apiPresenter.getCardStatement(month_extrato,year_extrato,pagina_extrato);

                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                String moneyString = formatter.format(dataPoint.getY());
                Toast.makeText(getBaseContext(), "Mês: "+(int)dataPoint.getX()+", Valor: "+moneyString, Toast.LENGTH_SHORT).show();

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

    public void profileReady(Profile profile) {
        TextView gasto = (TextView)findViewById(R.id.numero_cartao);
        gasto.setText("XXXX XXXX XXXX "+profile.getCardNumber().substring(12,16));
    }

    public void resumeReady(Resume resume) {
        TextView saldo = (TextView)findViewById(R.id.profile_relative_layout_saldo);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(resume.getBalance());
        saldo.setText(moneyString);
    }
    private void limparExtrato(){
        LinearLayout extrato_layout = (LinearLayout) findViewById(R.id.extrato);
        if(extrato_layout.getChildCount() > 0)
            extrato_layout.removeAllViews();
    }
}
