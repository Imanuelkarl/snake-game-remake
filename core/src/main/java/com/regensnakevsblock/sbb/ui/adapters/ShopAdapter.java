package com.regensnakevsblock.sbb.ui.adapters;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.service.purchase.PurchaseService;
import com.regensnakevsblock.sbb.ui.cards.ShopCard;
import com.regensnakevsblock.sbb.ui.core.BaseAdapter;
import com.regensnakevsblock.sbb.ui.data.ShopItemData;

public class ShopAdapter extends BaseAdapter<ShopItemData, ShopCard> {
    private OnPurchaseListener purchaseListener;
    PurchaseService purchaseService;
    public ShopAdapter() {
        this.purchaseService = GameInstance.getInstance().purchaseService;
    }


    public interface OnPurchaseListener {
        void onPurchase(ShopItemData item, int position);
    }

    @Override
    public ShopCard createView(int position) {
        return new ShopCard();
    }

    @Override
    public void bindView(ShopCard view, int position) {
        view.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(purchaseService!=null){
                    purchaseService.initiatePayment(getItem(position).id);
                }

                //purchaseListener.onPurchase(getItem(position), position);
            }
        });

        view.bind(getItem(position), position);

    }

    @Override
    public void notifyDataSetChanged() {
        // Implementation
    }

    public void setOnPurchaseListener(OnPurchaseListener listener) {
        this.purchaseListener = listener;
    }
}
