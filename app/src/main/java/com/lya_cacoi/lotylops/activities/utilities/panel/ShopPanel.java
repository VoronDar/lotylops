package com.lya_cacoi.lotylops.activities.utilities.panel;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.mainPlain;
import com.lya_cacoi.lotylops.activities.utilities.Blockable;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import static android.view.View.GONE;
import static com.lya_cacoi.lotylops.R.id.shop_panel;
import static com.lya_cacoi.lotylops.activities.mainPlain.activity;
import static com.lya_cacoi.lotylops.activities.mainPlain.dp;

public class ShopPanel {

    public static boolean isOpened = false;

    @SuppressLint("SetTextI18n")
    public static void openShop(final mainPlain view, Fragment fragment, final com.lya_cacoi.lotylops.Shop.Shop.Thing thing, final ShopHoldable holdable){
        isOpened = true;

        View shopPanel = view.findViewById(shop_panel);
        view.findViewById(shop_panel).setVisibility(View.VISIBLE);
        Animation a = new TranslateAnimation(0, 0,  dp(500), 0);
        a.setDuration(300);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ((Blockable)fragment).setEnable(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        shopPanel.startAnimation(a);
        final int money = transportPreferences.getMoney(view.getContext());

        ((TextView)view.findViewById(R.id.price)).setText(Integer.toString(money));


        ((TextView)view.findViewById(R.id.shopName)).setText(thing.getString());

        final int price;

        /*
        if (transportPreferences.isStarted(view.getContext()))
            price = thing.getSalesPrice();
        else
            price = thing.getPrice();

         */
        price = thing.getSalesPrice();

        ((TextView)view.findViewById(R.id.realPrice)).setText(Integer.toString(price));


        view.findViewById(R.id.cancelHolder).setOnClickListener(v -> {
            ((Blockable)fragment).setEnable(true);
            //findViewById(R.id.panel).setVisibility(View.VISIBLE);
            View shopPanel1 = view.findViewById(shop_panel);
            Animation a12 = new TranslateAnimation(0, 0,  0, dp(500));
            a12.setDuration(200);
            a12.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.findViewById(shop_panel).setVisibility(GONE);
                    isOpened = false;
                    holdable.onShopClosed();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            shopPanel1.startAnimation(a12);
        });

        view.findViewById(R.id.acceptHolder).setOnClickListener(v -> {


            if (money >= price) {
                transportPreferences.setMoney(view.getContext(), money-price);

                ((Blockable) fragment).setEnable(true);
                //findViewById(R.id.panel).setVisibility(View.VISIBLE);
                thing.apply(view.getContext());

                Animation a1 = new TranslateAnimation(0, 0,  0, dp(500));
                a1.setDuration(200);
                a1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.findViewById(shop_panel).setVisibility(GONE);
                        activity.slideFragment(thing.getFragment());
                        isOpened = false;
                        holdable.onShopClosed();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                view.findViewById(shop_panel).startAnimation(a1);
            }
        });
    }

    public static void closeShop(final mainPlain view, final ShopHoldable holdable){
        if (!isOpened) return;
        Animation a1 = new TranslateAnimation(0, 0,  0, dp(500));
        a1.setDuration(200);
        a1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.findViewById(shop_panel).setVisibility(GONE);
                isOpened = false;
                holdable.onShopClosed();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.findViewById(shop_panel).startAnimation(a1);

    }
    /** интерфейс, который информирует фрагмент о том, что панельку закрыли*/
    public interface ShopHoldable{
        void onShopClosed();
    }
}
