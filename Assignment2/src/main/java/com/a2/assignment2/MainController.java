package com.a2.assignment2;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

import java.util.List;

public class MainController {
    @FXML
    private BarChart airlineBarChart;

    @FXML
    public void initialize(){
        CSV csvData = new CSV("airline_safety.csv");

        List<CSV.AirlineData> dataList = csvData.airlineDataList;

        //Creating Barchart Elements
        Series series1 = new Series();
        series1.setName("Fatal Incidents 1985-1999");
        Series series2 = new Series();
        series2.setName("Fatal Incidents 2000-2014");

        for(CSV.AirlineData data : dataList){
            //Adding Data to BarChart Elements
            series1.getData().add(new XYChart.Data(data.airlineName, data.inc_99));
            series2.getData().add(new XYChart.Data(data.airlineName, data.inc_14));
        }

        airlineBarChart.getData().addAll(series1, series2);

    }
}