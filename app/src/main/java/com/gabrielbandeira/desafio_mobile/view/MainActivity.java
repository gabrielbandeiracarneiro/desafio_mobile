package com.gabrielbandeira.desafio_mobile.view;

import com.gabrielbandeira.desafio_mobile.R;
import com.gabrielbandeira.desafio_mobile.model.CardStatement;
import com.gabrielbandeira.desafio_mobile.model.CardUsage;
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
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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

        APIPresenter cardStatementPresenter = new APIPresenter(this);
        cardStatementPresenter.getCardStatement(10,2018,1);
        cardStatementPresenter.getCardUsage();
        cardStatementPresenter.getProfile();
        cardStatementPresenter.getResume();

        /*LinearLayout linha = new LinearLayout(getBaseContext());
        linha.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linha.setOrientation(LinearLayout.HORIZONTAL);

        TextView texto = new TextView(getBaseContext());
        texto.setText("Teste");
        linha.addView(texto);
        texto = new TextView(getBaseContext());
        texto.setText("Teste2");
        linha.addView(texto);
        texto = new TextView(getBaseContext());
        texto.setText("Teste3");
        linha.addView(texto);
        LinearLayout extrato = (LinearLayout) findViewById(R.id.extrato);
        extrato.addView(linha);*/
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

        Log.d("DEBUG",cardStatement.toString());
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
}
