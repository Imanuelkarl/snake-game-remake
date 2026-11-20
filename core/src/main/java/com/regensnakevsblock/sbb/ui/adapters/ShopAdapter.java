package com.regensnakevsblock.sbb.ui.adapters;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.service.purchase.PurchaseItem;
import com.regensnakevsblock.sbb.service.purchase.PurchaseListener;
import com.regensnakevsblock.sbb.service.purchase.PurchaseService;
import com.regensnakevsblock.sbb.ui.cards.ShopCard;
import com.regensnakevsblock.sbb.ui.core.BaseAdapter;
import com.regensnakevsblock.sbb.ui.data.ShopItemData;

import java.util.concurrent.atomic.AtomicReference;

public class ShopAdapter extends BaseAdapter<ShopItemData, ShopCard> implements PurchaseListener {

    PurchaseService purchaseService;
    public ShopAdapter() {
        this.purchaseService = GameInstance.getInstance().purchaseService;
    }

    @Override
    public void onPaymentSuccess(PurchaseItem item) {
        AtomicReference<ShopItemData> myData = new AtomicReference<>();
        items.stream()
            .filter(data -> data.id != null && data.id.equals(item.getProductId()))
            .findFirst()
            .ifPresent(data -> {
                myData.set(data);
                notifyDataSetChanged();
            });
        if (myData.get()==null){
            return;
        }
        performPurchase(myData.get());

    }

    @Override
    public void onPaymentFailure(PurchaseItem item, String message) {

    }

    @Override
    public void onPaymentCancelled() {

    }

    public void performPurchase(ShopItemData data){
        switch(data.category){
            case COIN:
                GameInstance.getSaveService().saveCoins(GameInstance.getSaveService().getCoins()+data.itemQuantity);
                GameInstance.getSaveService().saveLifeTimeCoins((int) (GameInstance.getSaveService().getLifeTimeCoins()+data.itemQuantity));
                break;
            case DIAMOND:
                GameInstance.getSaveService().saveDiamonds(GameInstance.getSaveService().getDiamonds()+data.itemQuantity);
                break;
            case PORTION:
                GameInstance.getSaveService().saveHealths(GameInstance.getSaveService().getHealths()+data.itemQuantity);
                break;
            default:
                break;
        }
    }

    @Override
    public ShopCard createView(int position) {
        return new ShopCard();
    }

    @Override
    public void bindView(ShopCard view, int position) {
        PurchaseListener purchaseListener = this;
        view.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ShopItemData data = getItem(position);
                if(purchaseService != null && data != null && !data.isPurchased){
                    purchaseService.setPaymentListener(purchaseListener);
                    purchaseService.initiatePayment(data.id);
                }
            }
        });

        view.bind(getItem(position), position);

    }

    @Override
    public void notifyDataSetChanged() {
        // Implementation
    }


}
