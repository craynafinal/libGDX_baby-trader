package com.jsl.babytrader.Runnables;

import com.badlogic.gdx.Gdx;
import com.jsl.babytrader.Data.Customer;
import com.jsl.babytrader.Data.SharedData;

import static com.jsl.babytrader.Data.SharedData.isEnded;

/**
 * Created by crayna on 7/7/17.
 */
public class PromotionTeam extends Team {
    private static int sleepTime = 500;

    @Override
    public void run() {
        while (!isEnded()) {
            if (!isPaused()) {
                sleep(sleepTime);

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        addCustomer(true);
                        addCustomer(false);
                    }
                });
            }
        }
    }

    private static void addCustomer(boolean isSelling) {
        int size = 0;
        Customer customer = new Customer(isSelling);

        if(isSelling) {
            SharedData.addCustomerSelling(customer);
            size = SharedData.getCustomerSellingSize();
        } else {
            SharedData.addCustomerBuying(customer);
            size = SharedData.getCustomerBuyingSize();
        }
    }
}
