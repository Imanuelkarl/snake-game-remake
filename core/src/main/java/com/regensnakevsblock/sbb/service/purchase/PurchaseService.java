package com.regensnakevsblock.sbb.service.purchase;

import java.util.List;

public interface PurchaseService {
    void initializeBillingService();
    void initiatePayment(String productId);
    void loadStoreItems();
    List<PurchaseItem> getPurchaseItems();
    void setPaymentListener(PurchaseListener listener);

    boolean isListLoaded();
}
