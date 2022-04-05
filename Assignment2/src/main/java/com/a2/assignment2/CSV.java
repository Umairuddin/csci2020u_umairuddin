package com.a2.assignment2;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

public class CSV {

    private final String filename;

    public static class DataResult {
        public int max, min, avg, total;

        @Override
        public String toString() {
            return "Max: " + max + "\tMin: " + min + " \tAverage: " + avg;
        }
    }

    public static class AirlineData {
        public String airlineName;
        public int inc_99, inc_14, fat_acc_99, fat_acc_14, fat_99, fat_14, inc_total;
        public long avail_seats;

        public AirlineData(CSVRecord data) {
            airlineName = data.get("Airline").replaceAll("\\*", "").replaceAll(" ", "").replaceAll("/", "");

            inc_99 = Integer.parseInt(data.get("inc_85_99"));
            inc_14 = Integer.parseInt(data.get("inc_00_14"));
            fat_acc_99 = Integer.parseInt(data.get("fat_acc_85_99"));
            fat_acc_14 = Integer.parseInt(data.get("fat_acc_00_14"));
            fat_99 = Integer.parseInt(data.get("fat_00_14"));
            fat_14 = Integer.parseInt(data.get("inc_85_99"));
            avail_seats = Long.parseLong(data.get("Avail_Seats"));
            inc_total = inc_14 + inc_99;
        }

        @Override
        public String toString() {
            return airlineName + ": Incidents 85-99: " + inc_99 + "\tIncidents 00-14: " + inc_14 + "\tTotal Incidents: "
                    + inc_total;
        }
    }

    public Map<String, DataResult> summaryData = new HashMap<>();
    public List<AirlineData> airlineDataList = new ArrayList<>();

    public CSV(String filename) {
        this.filename = filename;
        loadData();
        convertToXML();
        calculateValues();
    }

    private void loadData() {
        try {
            Reader in = new FileReader(filename);

            Iterable<CSVRecord> records = CSVFormat.Builder.create()
                    .setHeader("Airline", "Avail_Seats", "inc_85_99", "fat_acc_85_99",
                            "fat_85_99", "inc_00_14", "fat_acc_00_14", "fat_00_14")
                    .build().parse(in);
            for (CSVRecord record : records) {

                if (record.get("Airline").equalsIgnoreCase("airline"))
                    continue;

                airlineDataList.add(new AirlineData(record));
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void convertToXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dataDoc = builder.newDocument();

            Element root = dataDoc.createElement("Stats");
            dataDoc.appendChild(root);

            for (AirlineData data : airlineDataList) {

                Element elementAirlineName = dataDoc.createElement(data.airlineName);
                Element elementInc99 = dataDoc.createElement("incidents_85_99");
                Element elementInc14 = dataDoc.createElement("incidents_00_14");
                Element elementFatAcc99 = dataDoc.createElement("fatal_accidents_85_99");
                Element elementFatAcc14 = dataDoc.createElement("fatal_accidents_00_14");
                Element elementFat99 = dataDoc.createElement("fatalities_85_99");
                Element elementFat14 = dataDoc.createElement("fatalities_00_14");
                Element elementIncTotal = dataDoc.createElement("incidents_total");
                Element elementAvailSeats = dataDoc.createElement("avail_seat_km_per_week");

                elementInc99.appendChild(dataDoc.createTextNode("" + data.inc_99));
                elementInc14.appendChild(dataDoc.createTextNode("" + data.inc_14));
                elementFatAcc99.appendChild(dataDoc.createTextNode("" + data.fat_acc_99));
                elementFatAcc14.appendChild(dataDoc.createTextNode("" + data.fat_acc_14));
                elementFat99.appendChild(dataDoc.createTextNode("" + data.fat_99));
                elementFat14.appendChild(dataDoc.createTextNode("" + data.fat_14));
                elementIncTotal.appendChild(dataDoc.createTextNode("" + data.inc_total));
                elementAvailSeats.appendChild(dataDoc.createTextNode("" + data.avail_seats));

                elementAirlineName.appendChild(elementInc99);
                elementAirlineName.appendChild(elementInc14);
                elementAirlineName.appendChild(elementFatAcc99);
                elementAirlineName.appendChild(elementFatAcc14);
                elementAirlineName.appendChild(elementFat99);
                elementAirlineName.appendChild(elementFat14);
                elementAirlineName.appendChild(elementIncTotal);
                elementAirlineName.appendChild(elementAvailSeats);

                root.appendChild(elementAirlineName);
            }

            Source source = new DOMSource(dataDoc);
            File xmlFile = new File(filename.replaceAll(".csv", ".xml"));
            StreamResult result = new StreamResult(new FileOutputStream(xmlFile));
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty("indent", "yes");
            xformer.transform(source, result);

        } catch (IOException | ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private void calculateValues() {
        try {
            File xmlFile = new File(filename.replaceAll(".csv", ".xml"));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document dataDoc = builder.parse(xmlFile);

            NodeList airlineNodes = dataDoc.getElementsByTagName("Stats").item(0).getChildNodes();
            for (int i = 0; i < airlineNodes.getLength(); i++) {
                Node airlineNode = airlineNodes.item(i);
                NodeList statistics = airlineNode.getChildNodes();

                for (int j = 0; j < statistics.getLength(); j++) {
                    Node dataNode = statistics.item(j);
                    String content = dataNode.getTextContent().strip();
                    if (Objects.equals(content, ""))
                        continue;
                    String dataName = dataNode.getNodeName().replaceAll("#text", "").strip();
                    if (Objects.equals(dataName, "avail_seat_km_per_week"))
                        continue;
                    int value = Integer.parseInt(content);

                    DataResult data;
                    if (summaryData.containsKey(dataName)) {
                        data = summaryData.get(dataNode.getNodeName());
                        if (data.max < value)
                            data.max = value;
                        else if (data.max > value)
                            data.min = value;
                        data.total += value;
                    } else {
                        data = new DataResult();
                        data.max = value;
                        data.min = value;
                        data.total = value;
                    }
                    summaryData.put(dataName, data);
                }
            }

            for (String dataName : summaryData.keySet()) {
                DataResult data = summaryData.get(dataName);
                data.avg = data.total / airlineNodes.getLength();
            }

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        summaryXML();
    }

    private void summaryXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document summaryDoc = builder.newDocument();

            Element root = summaryDoc.createElement("Summary");
            summaryDoc.appendChild(root);

            for (String dataType : summaryData.keySet()) {

                DataResult data = summaryData.get(dataType);

                Element mainElement = summaryDoc.createElement("Stat");

                Element nameElement = summaryDoc.createElement("Name");
                Element minElement = summaryDoc.createElement("Min");
                Element maxElement = summaryDoc.createElement("Max");
                Element avgElement = summaryDoc.createElement("Avg");

                nameElement.appendChild(summaryDoc.createTextNode(dataType));
                minElement.appendChild(summaryDoc.createTextNode("" + data.min));
                maxElement.appendChild(summaryDoc.createTextNode("" + data.max));
                avgElement.appendChild(summaryDoc.createTextNode("" + data.avg));

                mainElement.appendChild(nameElement);
                mainElement.appendChild(minElement);
                mainElement.appendChild(maxElement);
                mainElement.appendChild(avgElement);
                root.appendChild(mainElement);
            }

            Element inc99 = summaryDoc.createElement("avg_incidents_85_99");
            Element inc14 = summaryDoc.createElement("avg_incidents_85_99");

            inc99.appendChild(summaryDoc.createTextNode("" + summaryData.get("incidents_85_99").avg));
            inc14.appendChild(summaryDoc.createTextNode("" + summaryData.get("incidents_00_14").avg));

            root.appendChild(inc99);
            root.appendChild(inc14);

            Source source = new DOMSource(summaryDoc);
            File xmlFile = new File("airline_summary_statistic.xml");
            StreamResult result = new StreamResult(new FileOutputStream(xmlFile));
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty("indent", "yes");
            xformer.transform(source, result);

        } catch (ParserConfigurationException | FileNotFoundException | TransformerException e) {
            e.printStackTrace();
        }
    }
}
