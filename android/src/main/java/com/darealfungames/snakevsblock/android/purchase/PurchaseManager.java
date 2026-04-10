package com.darealfungames.snakevsblock.android.purchase;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.PendingPurchasesParams;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.SkuDetails;
import com.darealfungames.snakevsblock.enumaretors.StoreID;
import com.darealfungames.snakevsblock.service.purchase.PurchaseItem;
import com.darealfungames.snakevsblock.service.purchase.PurchaseListener;
import com.darealfungames.snakevsblock.service.purchase.PurchaseService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PurchaseManager implements PurchaseService {
    private final Map<String,ProductDetails> productDetailsMap;
    private final BillingClient billingClient;
    List<String> productIds;
    Map<String, PurchaseItem> productMap = new HashMap<>();
    private PurchaseListener purchaseListener;
    private final Activity activity;

    public PurchaseManager(Activity activity){
        this.activity=activity;
        productDetailsMap=new HashMap<>();
        productIds =new ArrayList<>();
        this.billingClient= BillingClient.newBuilder(activity)
            .enablePendingPurchases(PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .build())
            .setListener(new PurchasesUpdatedListener() {
                @Override
                public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
                    if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK&&list!=null){
                        for (Purchase purchase:list) {
                            if(purchase.getPurchaseState()==Purchase.PurchaseState.PURCHASED&&!purchase.isAcknowledged()){
                                handlePurchase(purchase);
                            }
                        }
                    }else if (billingResult.getResponseCode()==BillingClient.BillingResponseCode.USER_CANCELED) {
                        //paymentListener.onPaymentFailure(currentItem,"Payment canceled");
                    }else {
                        //paymentListener.onPaymentFailure(currentItem,"Payment error:"+billingResult.getDebugMessage()+" . For Code: "+billingResult.getResponseCode());

                    }
                }
            })
            .build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {

            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                Log.d("CONNECTION","Billing Connection is completed");
            }
        });
    }
    @Override
    public void initiatePayment(PurchaseItem paymentItem) {

        ProductDetails productDetails = productDetailsMap.get(paymentItem.getProductId());

        if (productDetails == null) {
            Log.e("BILLING", "ProductDetails not found for: " + paymentItem.getProductId());
            return;
        }

        BillingFlowParams.ProductDetailsParams.Builder productDetailsParamsBuilder =
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails);

        // 🔴 Required for subscriptions
        if (productDetails.getSubscriptionOfferDetails() != null
            && !productDetails.getSubscriptionOfferDetails().isEmpty()) {

            String offerToken = productDetails.getSubscriptionOfferDetails()
                .get(0)
                .getOfferToken();

            productDetailsParamsBuilder.setOfferToken(offerToken);
        }

        BillingFlowParams billingFlowParams =
            BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(
                    Collections.singletonList(productDetailsParamsBuilder.build())
                )
                .build();

        billingClient.launchBillingFlow(activity, billingFlowParams);
    }

    @Override
    public List<PurchaseItem> loadStoreItems() {
        List<PurchaseItem> items = new ArrayList<>();
        List<QueryProductDetailsParams.Product> productList =
            productIds.stream()
                .map(id -> QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(id)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build())
                .collect(Collectors.toList());

        QueryProductDetailsParams params =
            QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();
        billingClient.queryProductDetailsAsync(params, (billingResult, productDetailsList) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && !productDetailsList.getProductDetailsList().isEmpty()) {

                for (ProductDetails productDetails : productDetailsList.getProductDetailsList()) {

                    String title = productDetails.getTitle();
                    String description = productDetails.getDescription();

                    String price = "";
                    String currency = "";
                    double amount = 0;

                    if (productDetails.getOneTimePurchaseOfferDetails() != null) {
                        ProductDetails.OneTimePurchaseOfferDetails offer =
                            productDetails.getOneTimePurchaseOfferDetails();

                        price = offer.getFormattedPrice();
                        currency = offer.getPriceCurrencyCode();
                        amount = offer.getPriceAmountMicros();
                    }

                    PurchaseItem paymentItem = new PurchaseItem(
                        StoreID.GOOGLE_PLAY,
                        productDetails.getProductId(),
                        title.split(" ")[0],
                        description,
                        amount,
                        currency
                    );

                    paymentItem.setCurrency(currency);
                    paymentItem.setAmount(amount);
                    productMap.put(productDetails.getProductId(), paymentItem);
                    productDetailsMap.put(productDetails.getProductId(),productDetails);
                    items.add(paymentItem);

                    Log.d("ITEM", "Getting Item " + description + " " + productDetailsList.getProductDetailsList().size());
                }
            }
        });

        return items;
    }

    @Override
    public void setPaymentListener(PurchaseListener listener) {
        this.purchaseListener=listener;

    }
    private void handlePurchase(Purchase purchase) {

        List<String> productIds = purchase.getProducts();

        for (String productId : productIds) {

            // 🔍 Find your item based on productId
            PurchaseItem purchasedItem = productMap.get(productId);

            if (purchasedItem != null) {

                ConsumeParams consumeParams = ConsumeParams.newBuilder()
                    .setPurchaseToken(purchase.getPurchaseToken())
                    .build();

                billingClient.consumeAsync(consumeParams, (billingResult, token) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                        purchaseListener.onPaymentSuccess(purchasedItem);

                        Toast.makeText(activity,
                            "Purchase successful: " + purchasedItem.getDescription(),
                            Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

}
