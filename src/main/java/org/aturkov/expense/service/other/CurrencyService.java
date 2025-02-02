package org.aturkov.expense.service.other;

//todo
public class CurrencyService {
    private static final String API_KEY = "YOUR_API_KEY";
    private static final String URL = "https://api.exchangerate-api.com/v4/latest/USD";

//    public void convert() {
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(URL)
//                .addHeader("Authorization", "Bearer " + API_KEY)
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//            String responseBody = response.body().string();
//            JSONObject json = new JSONObject(responseBody);
//
//            System.out.println("Base Currency: " + json.getString("base"));
//            System.out.println("Date: " + json.getString("date"));
//            JSONObject rates = json.getJSONObject("rates");
//            System.out.println("USD to EUR: " + rates.getDouble("EUR"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
