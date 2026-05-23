package com.regensnakevsblock.sbb.service.purchase;

import java.util.List;

public interface PurchaseService {
    void initializeBillingService();
    void initiatePayment(PurchaseItem item);
    List<PurchaseItem> loadStoreItems();
    void setPaymentListener(PurchaseListener listener);

}
