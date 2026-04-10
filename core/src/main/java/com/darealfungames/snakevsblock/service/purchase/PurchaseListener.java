package com.darealfungames.snakevsblock.service.purchase;

public interface PurchaseListener {

    void onPaymentSuccess(PurchaseItem item);
    void onPaymentFailure(PurchaseItem item,String message);
    void onPaymentCancelled();
}
