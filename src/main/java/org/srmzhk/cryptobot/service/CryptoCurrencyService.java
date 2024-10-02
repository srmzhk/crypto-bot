package org.srmzhk.cryptobot.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class CryptoCurrencyService {
    public static String getCryptoCurrenciesPrice() throws IOException {
        URL url = new URL("https://api.binance.com/api/v3/ticker/price");

        // Open a connection to the URL
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");  // Set the request method to GET

        // Check if the request was successful (HTTP response code 200)
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Create a BufferedReader to read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            // Read the response line by line
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();  // Close the reader

            // Parse the response to JSON Array (the Binance API returns a JSON array)
            JSONArray jsonArray = new JSONArray(response.toString());

            List<JSONObject> cryptoList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject crypto = jsonArray.getJSONObject(i);
                String symbol = crypto.getString("symbol");
                if (symbol.contains("USDT")) {  // Filter to include only symbols that contain "USDT"
                    cryptoList.add(crypto);
                }
            }


            // Sort the List based on the "price" field
            Collections.sort(cryptoList, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject a, JSONObject b) {
                    // Extract prices from the JSONObjects and compare them
                    double priceA = Double.parseDouble(a.getString("price"));
                    double priceB = Double.parseDouble(b.getString("price"));
                    return Double.compare(priceB, priceA);  // Sort in descending order (highest price first)
                }
            });

            // Rebuild a sorted JSONArray (optional, if you want to work with sorted JSON data)
            JSONArray sortedJsonArray = new JSONArray(cryptoList);

            // Build a string with the top 10 cryptocurrency prices
            StringBuilder result = new StringBuilder("Top 10 cryptocurrency rates:\n");
            for (int i = 0; i < 10 && i < sortedJsonArray.length(); i++) {
                JSONObject jsonObject = sortedJsonArray.getJSONObject(i);
                String symbol = jsonObject.getString("symbol");
                String price = jsonObject.getString("price");
                result.append(symbol).append(": ").append(price).append("\n");
            }

            // Return the response as a String
            return result.toString();
        } else {
            // If the request failed, return an error message
            return "Failed to get cryptocurrency prices. Response Code: " + responseCode;
        }
    }
}
