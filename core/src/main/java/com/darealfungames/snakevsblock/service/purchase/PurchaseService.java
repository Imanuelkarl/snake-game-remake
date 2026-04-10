package com.darealfungames.snakevsblock.service.purchase;

import java.util.List;

public interface PurchaseService {
    void initiatePayment(PurchaseItem item);
    List<PurchaseItem> loadStoreItems();
    void setPaymentListener(PurchaseListener listener);

}
