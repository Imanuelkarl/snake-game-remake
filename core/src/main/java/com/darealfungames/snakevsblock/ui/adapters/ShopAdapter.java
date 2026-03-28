package com.darealfungames.snakevsblock.ui.adapters;


import com.darealfungames.snakevsblock.ui.cards.ShopCard;
import com.darealfungames.snakevsblock.ui.core.BaseAdapter;
import com.darealfungames.snakevsblock.ui.data.ShopItemData;

public class ShopAdapter extends BaseAdapter<ShopItemData, ShopCard> {
    private OnPurchaseListener purchaseListener;

    public interface OnPurchaseListener {
        void onPurchase(ShopItemData item, int position);
    }

    @Override
    public ShopCard createView(int position) {
        return new ShopCard();
    }

    @Override
    public void bindView(ShopCard view, int position) {
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
