package com.stripe.sample;

import java.nio.file.Paths;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.post;
import static spark.Spark.port;
import static spark.Spark.staticFiles;

import com.google.gson.Gson;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

public class Server {
  private static Gson gson = new Gson();

  public static void main(String[] args) {
    port(4242);

    // This is your real test secret API key.
    Stripe.apiKey = "sk_test_51IOf6ZIWSawBjWVnof1YyHsjhLwe069wqcC1q7bvYKT22yfAWjVTudAYCgp9MHmt9cglCWesKixEno34IDpSnBze00HIVjzqQp";

    staticFiles.externalLocation(
        Paths.get("").toAbsolutePath().toString());

    post("/create-checkout-session", (request, response) -> {
        response.type("application/json");

        final String YOUR_DOMAIN = "http://localhost:4242";
        SessionCreateParams params =
          SessionCreateParams.builder()
            .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(YOUR_DOMAIN + "/success.html")
            .setCancelUrl(YOUR_DOMAIN + "/cancel.html")
            .addLineItem(
              SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(
                  SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("inr")
                    .setUnitAmount(2000L)
                    .setProductData(
                      SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName("Stubborn Attachments")
                        .build())
                    .build())
                .build())
            .build();
      Session session = Session.create(params);
      HashMap<String, String> responseData = new HashMap<String, String>();
      
      responseData.put("id", session.getId());
      responseData.put("paymentId", session.getPaymentIntent());
      System.out.println("responseData : "+responseData);
      return gson.toJson(responseData);
    });
  }
}