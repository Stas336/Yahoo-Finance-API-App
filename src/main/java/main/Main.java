package main;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Main
{
    private static final int YAHOO_LIMIT = 510;
    private enum Currencies
    {
        EUR, USD, RUB
    }
    private static List<Currencies> allCurrencies = Arrays.asList(Currencies.values());
    private static int userInputYear, userInputMonth, userInputDate;
    private static LocalDate startDate, endDate;

    public static void main(String args[])
    {
        System.out.println("Enter year for startDate");
        userInputYear = Integer.parseInt(ValueReader.readValue());
        System.out.println("Enter month for startDate");
        userInputMonth = Integer.parseInt(ValueReader.readValue());
        System.out.println("Enter date for startDate");
        userInputDate = Integer.parseInt(ValueReader.readValue());
        startDate = LocalDate.of(userInputYear, userInputMonth, userInputDate);
        System.out.println("Enter year for endDate");
        userInputYear = Integer.parseInt(ValueReader.readValue());
        System.out.println("Enter month for endDate");
        userInputMonth = Integer.parseInt(ValueReader.readValue());
        System.out.println("Enter date for endDate");
        userInputDate = Integer.parseInt(ValueReader.readValue());
        endDate = LocalDate.of(userInputYear, userInputMonth, userInputDate);
        System.out.println("Select currency:");
        System.out.println(allCurrencies);
        userInputDate = Integer.parseInt(ValueReader.readValue());
        try
        {
            requestYahoo(allCurrencies.get(userInputDate-1), startDate, endDate);
        }catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            System.out.println("Press any key to exit...");
            ValueReader.readValue();
            System.exit(1);
        }
    }

    public static void requestYahoo(Currencies currency, LocalDate startDate, LocalDate endDate) throws Exception
    {
        if (ChronoUnit.DAYS.between(startDate, endDate) > YAHOO_LIMIT)
        {
            System.out.printf("Between two dates more than %d days. Enter time period with duration less than %d days.", YAHOO_LIMIT, YAHOO_LIMIT);
            System.exit(1);
        }
        String startDateYear = String.valueOf(startDate.getYear());
        String startDateMonth = String.valueOf(startDate.getMonthValue());
        if (startDate.getMonthValue() < 10)
        {
            startDateMonth = "0" + startDateMonth;
        }
        String startDateDate = String.valueOf(startDate.getDayOfMonth());
        if (startDate.getDayOfMonth() < 10)
        {
            startDateDate = "0" + startDateDate;
        }
        String endDateYear = String.valueOf(endDate.getYear());
        String endDateMonth = String.valueOf(endDate.getMonthValue());
        if (endDate.getMonthValue() < 10)
        {
            endDateMonth = "0" + endDateMonth;
        }
        String endDateDate = String.valueOf(endDate.getDayOfMonth());
        if (endDate.getDayOfMonth() < 10)
        {
            endDateDate = "0" + endDateDate;
        }
        String yqlBaseUri = "https://query.yahooapis.com/v1/public/yql";
        String yqlUrl = yqlBaseUri + "?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22"+currency+"%3DX%22%20and%20startDate%20%3D%20%22"+startDateYear+"-"+startDateMonth+"-"+startDateDate+"%22%20and%20endDate%20%3D%20%22"+endDateYear+"-"+endDateMonth+"-"+endDateDate+"%22&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
        JAXBContext jc = JAXBContext.newInstance(Stock.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        URL url = new URL(yqlUrl);
        InputStream xmlStream = url.openStream();
        Stock stock = (Stock) unmarshaller.unmarshal(xmlStream);
        /*Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(stock, System.out);*/
        List<Quote> quotes = stock.getQuotes();
        for (Quote quote:quotes)
        {
            System.out.println(quote.getDate() + ";Price " + quote.getOpen());
        }
        /*OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        //ArrayList<Integer> returnResult = new ArrayList<Integer>();
        String returnResult;
        String responseLine;
        try
        {
            Response response = client.newCall(request).execute();
            if (response.message().equals("OK"))
            {
                responseLine = response.body().string();
                System.out.println(responseLine);
                String[] responseLineSplit = responseLine.split("\n");
                for (String requiredNumber:responseLineSplit)
                {
                    returnResult.add(Integer.parseInt(requiredNumber));
                }
            }
        }catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            System.exit(1);
        }*/
    }
}