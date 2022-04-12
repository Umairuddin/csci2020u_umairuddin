package co.lab09;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class Lab09 extends Application {

  private Stage stage;

  ArrayList<String> colours = new ArrayList<>(
    Arrays.asList(
      "BLUE",
      "RED",
      "BLACK",
      "YELLOW",
      "PURPLE",
      "ORANGE",
      "GREEN",
      "CYAN",
      "MAGENTA"
    )
  );

  private String reqURL =
    "https://query1.finance.yahoo.com/v7/finance/download/C-O-D-E" +
    "?period1=1262322000&period2=1451538000&interval=1mo&eve" +
    "nts=history&includeAdjustedClose=true";

  @Override
  public void start(Stage stage) throws IOException {
    AnchorPane pane = new AnchorPane();
    Scene scene = new Scene(pane, 700, 600);
    stage.setTitle("Lab09");
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
    this.stage = stage;
    String[] stockCodes = { "GOOG", "AAPL", "AMZN", "GE" };
    List<List<Double>> stockPrices = new ArrayList<>();
    for (String code : stockCodes) {
      stockPrices.add(downloadStockPrices(code));
    }
    drawLinePlot(pane, stockPrices);
  }

  private List<Double> downloadStockPrices(String code) {
    List<Double> closingPrices = new ArrayList<>();
    try {
      URL url = new URL(reqURL.replaceAll("C-O-D-E", code));
      URLConnection conn = url.openConnection();
      BufferedReader reader = new BufferedReader(
        new InputStreamReader(conn.getInputStream())
      );
      Iterable<CSVRecord> inputs = CSVFormat.Builder
        .create()
        .setHeader(
          "Date",
          "Open",
          "High",
          "Low",
          "Close",
          "Adj Close",
          "Volume"
        )
        .build()
        .parse(reader);
      for (CSVRecord rec : inputs) {
        double closeValue = 0;
        try {
          closeValue = Double.parseDouble(rec.get("Close"));
        } catch (NumberFormatException e) {
          continue;
        }
        closingPrices.add(closeValue);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return closingPrices;
  }

  private void drawLinePlot(Pane pane, List<List<Double>> closingPrices) {
    Line yAxis = new Line();
    yAxis.setStartX(50);
    yAxis.setStartY(50);
    yAxis.setEndX(50);
    yAxis.setEndY(stage.getHeight() - 100);
    yAxis.setStrokeWidth(2);

    Line xAxis = new Line();
    xAxis.setStartX(50);
    xAxis.setStartY(stage.getHeight() - 100);
    xAxis.setEndX(stage.getWidth() - 50);
    xAxis.setEndY(stage.getHeight() - 100);
    xAxis.setStrokeWidth(2);

    double maxPrice = 0;
    for (List<Double> closingList : closingPrices) {
      for (double price : closingList) {
        if (price > maxPrice) maxPrice = price;
      }
    }

    for (List<Double> closingList : closingPrices) {
      plotLine(pane, maxPrice, closingList);
    }

    pane.getChildren().add(yAxis);
    pane.getChildren().add(xAxis);
  }

  private void plotLine(
    Pane pane,
    double maxPrice,
    List<Double> closingPrices
  ) {
    String colour = colours.get((new Random()).nextInt(colours.size()));
    colours.remove(colour);
    Color lineColor = Color.valueOf(colour);

    double startingX = 50;
    double startingY = stage.getHeight() - 100;
    double endingX = stage.getWidth() - 50;
    double endingY = 50;

    double zoneY = startingY - endingY;
    double zoneX = endingX - startingX;

    double padding = zoneX / closingPrices.size();
    double positionX = startingX;

    List<Double> xPoints = new ArrayList<>();
    List<Double> yPoints = new ArrayList<>();

    for (double price : closingPrices) {
      double pricePong = price / maxPrice;
      double positionY = (startingY) - zoneY * pricePong;
      yPoints.add(positionY);
      xPoints.add(positionX);
      positionX += padding;
    }

    for (int i = 0; i < xPoints.size(); i++) {
      if (i == xPoints.size() - 1) break;
      Line line = new Line();
      line.setStrokeWidth(1);
      line.setStroke(lineColor);
      line.setStartX(xPoints.get(i));
      line.setStartY(yPoints.get(i));

      line.setEndX(xPoints.get(i + 1));
      line.setEndY(yPoints.get(i + 1));
      pane.getChildren().add(line);
    }
  }

  public static void main(String[] args) {
    launch();
  }
}
