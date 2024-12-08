package com.financial.management;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import java.util.ArrayList;
import java.util.List;

public class RecordStatistics extends AppCompatActivity {

    private LineChart lineChart;
    private BarChart barChart;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_statistics);

        lineChart = findViewById(R.id.lineChart);
        barChart = findViewById(R.id.barChart);
        dbHelper = new DBHelper(this);

        showLineChart();
        showBarChart();
    }

    private void showLineChart() {
        List<Record> records = dbHelper.getAllRecords();
        List<Entry> incomeEntries = new ArrayList<>();
        List<Entry> expenseEntries = new ArrayList<>();

        for (Record record : records) {
            float dateValue = Float.parseFloat(record.getDate());
            float money = record.getMoney();

            if ("收入".equals(record.getType())) {
                incomeEntries.add(new Entry(dateValue, money));
            } else if ("支出".equals(record.getType())) {
                expenseEntries.add(new Entry(dateValue, money));
            }
        }

        // 创建收入折线图数据集，颜色改为绿色
        LineDataSet incomeDataSet = new LineDataSet(incomeEntries, "收入");
        incomeDataSet.setColor(Color.parseColor("#00796B")); // 绿色
        incomeDataSet.setLineWidth(2f);
        incomeDataSet.setCircleRadius(4f);
        incomeDataSet.setCircleColor(Color.parseColor("#00796B"));

        // 创建支出折线图数据集，颜色改为橙色
        LineDataSet expenseDataSet = new LineDataSet(expenseEntries, "支出");
        expenseDataSet.setColor(Color.parseColor("#FF7043")); // 橙色
        expenseDataSet.setLineWidth(2f);
        expenseDataSet.setCircleRadius(4f);
        expenseDataSet.setCircleColor(Color.parseColor("#FF7043"));

        // 创建折线图数据
        LineData lineData = new LineData(incomeDataSet, expenseDataSet);

        // 设置图表数据
        lineChart.setData(lineData);

        // 设置 X 轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(true);
        xAxis.setValueFormatter((value, axis) -> String.format("%06.0f", value));

        // 设置 Y 轴
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularity(1f);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setDrawGridLines(true);
        lineChart.getAxisRight().setEnabled(false);

        // 去掉右下角的 description label
        lineChart.getDescription().setEnabled(false);

        // 刷新图表显示
        lineChart.invalidate();
    }

    private void showBarChart() {
        List<Record> records = dbHelper.getAllRecords();
        List<BarEntry> incomeEntries = new ArrayList<>();
        List<BarEntry> expenseEntries = new ArrayList<>();

        for (Record record : records) {
            float dateValue = Float.parseFloat(record.getDate());
            float money = record.getMoney();

            if ("收入".equals(record.getType())) {
                incomeEntries.add(new BarEntry(dateValue, money));
            } else if ("支出".equals(record.getType())) {
                expenseEntries.add(new BarEntry(dateValue, money));
            }
        }

        // 创建收入柱状图数据集，颜色改为绿色
        BarDataSet incomeDataSet = new BarDataSet(incomeEntries, "收入");
        incomeDataSet.setColor(Color.parseColor("#00796B")); // 绿色

        // 创建支出柱状图数据集，颜色改为橙色
        BarDataSet expenseDataSet = new BarDataSet(expenseEntries, "支出");
        expenseDataSet.setColor(Color.parseColor("#FF7043")); // 橙色

        // 创建柱状图数据
        BarData barData = new BarData(incomeDataSet, expenseDataSet);

        // 设置图表数据
        barChart.setData(barData);

        // 设置 X 轴
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(true);
        xAxis.setValueFormatter((value, axis) -> String.format("%06.0f", value));

        // 设置 Y 轴
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularity(1f);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setDrawGridLines(true);
        barChart.getAxisRight().setEnabled(false);

        // 去掉右下角的 description label
        barChart.getDescription().setEnabled(false);

        // 刷新图表显示
        barChart.invalidate();
    }
}
