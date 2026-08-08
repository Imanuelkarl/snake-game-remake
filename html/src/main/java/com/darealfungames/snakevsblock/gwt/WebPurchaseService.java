package com.darealfungames.snakevsblock.gwt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.regensnakevsblock.sbb.enumaretors.StoreID;
import com.regensnakevsblock.sbb.service.purchase.PurchaseItem;
import com.regensnakevsblock.sbb.service.purchase.PurchaseListener;
import com.regensnakevsblock.sbb.service.purchase.PurchaseService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class WebPurchaseService implements PurchaseService {
    private static final String API_URL = "http://localhost:3000/api";
    private static final String API_KEY = "your-secret-api-key-here";

    // All product IDs that we need to fetch
    private static final String[] REQUIRED_PRODUCT_IDS = {
        "500_coins",
        "2000_coins",
        "5000_coins",
        "10000_coins",
        "100_diamonds",
        "500_diamonds",
        "2000_diamonds",
        "5000_diamonds",
        "50_potions",
        "250_potions",
        "600_potions",
        "1500_potions"
    };

    private List<PurchaseItem> items = new ArrayList<>();
    private boolean listLoaded = false;
    private PurchaseListener listener;
    private JsonReader jsonReader = new JsonReader();

    @Override
    public void initializeBillingService() {
        Gdx.app.log("WebPurchaseService", "Initialized");
    }

    @Override
    public void loadStoreItems() {
        // Build the IDs query parameter
        StringBuilder idsParam = new StringBuilder();
        for (int i = 0; i < REQUIRED_PRODUCT_IDS.length; i++) {
            if (i > 0) idsParam.append(",");
            idsParam.append(REQUIRED_PRODUCT_IDS[i]);
        }

        String url = API_URL + "/products?currency=USD&ids=" + idsParam.toString();
        Gdx.app.log("WebPurchaseService", "Loading products from: " + url);

        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest request = requestBuilder
            .newRequest()
            .method(Net.HttpMethods.GET)
            .url(url)
            .header("x-api-key", API_KEY)
            .build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                try {
                    String json = httpResponse.getResultAsString();
                    Gdx.app.log("WebPurchaseService", "Response received: " + json);

                    JsonValue root = jsonReader.parse(json);
                    boolean success = root.getBoolean("success", false);

                    if (success) {
                        JsonValue itemsArray = root.get("items");

                        if (itemsArray != null && itemsArray.isArray()) {
                            items.clear();
                            int loadedCount = 0;

                            for (JsonValue product : itemsArray.iterator()) {
                                String storeId = product.getString("storeId", "WEB_STORE");
                                String productId = product.getString("productId", "");
                                String title = product.getString("title", "");
                                String description = product.getString("description", "");
                                double amount = product.getDouble("amount", 0.0);
                                String currency = product.getString("currency", "USD");
                                String type = product.getString("type", "unknown");

                                Gdx.app.log("WebPurchaseService", "Loading product: " + productId + " - " + title);

                                PurchaseItem item = new PurchaseItem(
                                    StoreID.valueOf(storeId),
                                    productId,
                                    title,
                                    description,
                                    amount,
                                    currency
                                );

                                // Store additional data in productJson for later use
                                item.setProductJson(product.toString());
                                items.add(item);
                                loadedCount++;
                            }

                            listLoaded = true;
                            Gdx.app.log("WebPurchaseService", "Loaded " + loadedCount + " products (out of " + REQUIRED_PRODUCT_IDS.length + " requested)");

                            // Check if all products were loaded
                            if (loadedCount < REQUIRED_PRODUCT_IDS.length) {
                                Gdx.app.log("WebPurchaseService", "Only " + loadedCount + " of " + REQUIRED_PRODUCT_IDS.length + " products loaded");
                            }
                        } else {
                            Gdx.app.error("WebPurchaseService", "No items array in response");
                            listLoaded = true;
                        }
                    } else {
                        String errorMessage = root.getString("message", "Unknown error");
                        Gdx.app.error("WebPurchaseService", "Failed to load products: " + errorMessage);
                        listLoaded = true;
                    }

                } catch (Exception e) {
                    Gdx.app.error("WebPurchaseService", "Error parsing products", e);
                    listLoaded = true;
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.error("WebPurchaseService", "Failed to load products", t);
                listLoaded = true;
                // Notify listener if available
                if (listener != null) {
                    listener.onPaymentFailure(null, "Failed to load products: " + t.getMessage());
                }
            }

            @Override
            public void cancelled() {
                Gdx.app.log("WebPurchaseService", "Request cancelled");
                listLoaded = true;
            }
        });
    }

    @Override
    public void initiatePayment(String productId) {
        PurchaseItem product = items.stream()
            .filter(p -> p.getProductId().equals(productId))
            .findFirst()
            .orElse(null);

        if (product == null) {
            Gdx.app.error("WebPurchaseService", "Product not found: " + productId);
            if (listener != null) {
                listener.onPaymentFailure(null, "Product not found: " + productId);
            }
            return;
        }

        String userId = getUserId();
        long timestamp = System.currentTimeMillis();

        // Build JSON request
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        jsonBuilder.append("\"productId\":\"").append(productId).append("\",");
        jsonBuilder.append("\"userId\":\"").append(userId).append("\",");
        jsonBuilder.append("\"paymentMethod\":\"card\",");
        jsonBuilder.append("\"timestamp\":").append(timestamp);

        String signature =  "mkosddsdlzvjvd;kmvdask;vkm;SDl,s.xz";//generateSignature(productId, userId, timestamp);
        jsonBuilder.append(",\"signature\":\"").append(signature).append("\"");
        jsonBuilder.append("}");

        Gdx.app.log("WebPurchaseService", "Initiating payment for: " + productId);

        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest request = requestBuilder
            .newRequest()
            .method(Net.HttpMethods.POST)
            .url(API_URL + "/purchase")
            .header("x-api-key", API_KEY)
            .header("Content-Type", "application/json")
            .content(jsonBuilder.toString())
            .build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                try {
                    String json = httpResponse.getResultAsString();
                    Gdx.app.log("WebPurchaseService", "Payment response: " + json);

                    JsonValue root = jsonReader.parse(json);
                    boolean success = root.getBoolean("success", false);

                    if (success) {
                        JsonValue transaction = root.get("transaction");
                        if (transaction != null) {
                            String transactionId = transaction.getString("id", "");
                            String productId = transaction.getString("productId", "");

                            // Get the actual item details from the transaction
                            double amount = transaction.getDouble("amount", 0.0);
                            String currency = transaction.getString("currency", "USD");

                            Gdx.app.log("WebPurchaseService", "Payment successful! Transaction: " + transactionId);

                            // Verify the purchase on the server
                            verifyPurchase(transactionId, userId);
                        } else if (listener != null) {
                            listener.onPaymentFailure(product, "No transaction data");
                        }
                    } else if (listener != null) {
                        String message = root.getString("message", "Payment failed");
                        Gdx.app.error("WebPurchaseService", "Payment failed: " + message);
                        listener.onPaymentFailure(product, message);
                    }

                } catch (Exception e) {
                    Gdx.app.error("WebPurchaseService", "Error processing payment", e);
                    if (listener != null) {
                        listener.onPaymentFailure(product, e.getMessage());
                    }
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.error("WebPurchaseService", "Payment failed", t);
                if (listener != null) {
                    listener.onPaymentFailure(product, t.getMessage());
                }
            }

            @Override
            public void cancelled() {
                Gdx.app.log("WebPurchaseService", "Payment cancelled");
                if (listener != null) {
                    listener.onPaymentCancelled();
                }
            }
        });
    }

    private void verifyPurchase(String transactionId, String userId) {
        long timestamp = System.currentTimeMillis();

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        jsonBuilder.append("\"transactionId\":\"").append(transactionId).append("\",");
        jsonBuilder.append("\"userId\":\"").append(userId).append("\",");
        jsonBuilder.append("\"timestamp\":").append(timestamp);

        String signature = "mkosddsdlzvjvd;kmvdask;vkm;SDl,s.xz";// generateSignature(transactionId, userId, timestamp);
        jsonBuilder.append(",\"signature\":\"").append(signature).append("\"");
        jsonBuilder.append("}");

        Gdx.app.log("WebPurchaseService", "Verifying purchase: " + transactionId);

        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest request = requestBuilder
            .newRequest()
            .method(Net.HttpMethods.POST)
            .url(API_URL + "/verify")
            .header("x-api-key", API_KEY)
            .header("Content-Type", "application/json")
            .content(jsonBuilder.toString())
            .build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                try {
                    String json = httpResponse.getResultAsString();
                    Gdx.app.log("WebPurchaseService", "Verification response: " + json);

                    JsonValue root = jsonReader.parse(json);
                    boolean success = root.getBoolean("success", false);

                    if (success && listener != null) {
                        // Get product details from the purchase
                        String productId = root.getString("productId", "");

                        // Try to find the product in our list
                        PurchaseItem purchasedItem = items.stream()
                            .filter(p -> p.getProductId().equals(productId))
                            .findFirst()
                            .orElse(null);

                        if (purchasedItem != null) {
                            Gdx.app.log("WebPurchaseService", "Purchase verified: " + purchasedItem.getTitle());
                            listener.onPaymentSuccess(purchasedItem);
                        } else {
                            // Create a fallback item with details from the response
                            JsonValue purchase = root.get("purchase");
                            if (purchase != null) {
                                String productTitle = purchase.getString("productTitle", "Purchase");
                                double amount = purchase.getDouble("amount", 0.0);
                                String currency = purchase.getString("currency", "USD");

                                PurchaseItem fallbackItem = new PurchaseItem(
                                    StoreID.WEB_STORE,
                                    productId,
                                    productTitle,
                                    "Purchase verified",
                                    amount,
                                    currency
                                );
                                listener.onPaymentSuccess(fallbackItem);
                            } else {
                                PurchaseItem fallbackItem = new PurchaseItem(
                                    StoreID.WEB_STORE,
                                    "unknown",
                                    "Purchase",
                                    "Completed purchase",
                                    0.0,
                                    "USD"
                                );
                                listener.onPaymentSuccess(fallbackItem);
                            }
                        }
                    } else if (listener != null) {
                        String message = root.getString("message", "Verification failed");
                        Gdx.app.error("WebPurchaseService", "Verification failed: " + message);
                        listener.onPaymentFailure(null, message);
                    }

                } catch (Exception e) {
                    Gdx.app.error("WebPurchaseService", "Error verifying purchase", e);
                    if (listener != null) {
                        listener.onPaymentFailure(null, "Verification error: " + e.getMessage());
                    }
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.error("WebPurchaseService", "Verification failed", t);
                if (listener != null) {
                    listener.onPaymentFailure(null, "Verification failed: " + t.getMessage());
                }
            }

            @Override
            public void cancelled() {
                Gdx.app.log("WebPurchaseService", "Verification cancelled");
            }
        });
    }

    @Override
    public List<PurchaseItem> getPurchaseItems() {
        return items;
    }

    @Override
    public void setPaymentListener(PurchaseListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean isListLoaded() {
        return listLoaded;
    }

    private String getUserId() {
        Preferences prefs = Gdx.app.getPreferences("game_prefs");
        String userId = prefs.getString("user_id", null);
        if (userId == null) {
            userId =  "mkosddsdlzvjvd;kmvdask;vkm;SDl,s.xz";//java.util.UUID.randomUUID().toString();
            prefs.putString("user_id", userId);
            prefs.flush();
            Gdx.app.log("WebPurchaseService", "Generated new user ID: " + userId);
        } else {
            Gdx.app.log("WebPurchaseService", "Using existing user ID: " + userId);
        }
        return userId;
    }

//    private String generateSignature(String id, String userId, long timestamp) {
//        try {
//            String data = "{\"productId\":\"" + id + "\",\"userId\":\"" + userId + "\"}";
//            String payload = data + timestamp;
//            Mac mac = Mac.getInstance("HmacSHA256");
//            SecretKeySpec secretKey = new SecretKeySpec(
//                API_KEY.getBytes(StandardCharsets.UTF_8),
//                "HmacSHA256"
//            );
//            mac.init(secretKey);
//            byte[] signatureBytes = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
//            return Base64.getEncoder().encodeToString(signatureBytes);
//        } catch (Exception e) {
//            Gdx.app.error("WebPurchaseService", "Error generating signature", e);
//            return "";
//        }
//    }
}
