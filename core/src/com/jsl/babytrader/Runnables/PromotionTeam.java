package com.jsl.babytrader.Runnables;

import com.badlogic.gdx.Gdx;
import com.jsl.babytrader.Controls.Configuration;
import com.jsl.babytrader.Data.Customer;
import com.jsl.babytrader.Data.SharedData;

import java.util.Random;

import static com.jsl.babytrader.Data.SharedData.isEnded;

/**
 * Team that brings customers over.
 */
public class PromotionTeam extends Team {
    final private static int SLEEP_TIME_MIN = 50;
    final private static int SLEEP_TIME_MAX = 200;

    @Override
    public void run() {
        while (!isEnded()) {
            sleep(getWaitTime(SLEEP_TIME_MIN, SLEEP_TIME_MAX, Configuration.getLevelPromotion()));

            if (!isPaused()) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        addCustomer(new Random().nextBoolean());
                        Configuration.increaseCustomersVisited();
                    }
                });
            }
        }
    }

    private static void addCustomer(boolean isSelling) {
        Customer customer = new Customer(isSelling);

        if(isSelling) {
            SharedData.addCustomerSelling(customer);
        } else {
            SharedData.addCustomerBuying(customer);
        }
    }
}
