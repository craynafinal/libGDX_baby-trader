package com.jsl.babytrader.Controls;

import com.badlogic.gdx.utils.Timer;
import com.jsl.babytrader.Data.Baby;
import com.jsl.babytrader.Data.SharedData;
import com.jsl.babytrader.Data.Time;
import com.jsl.babytrader.Runnables.PromotionTeam;
import com.jsl.babytrader.Runnables.PurchaseTeam;
import com.jsl.babytrader.Runnables.ResearchTeam;
import com.jsl.babytrader.Runnables.SalesTeam;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles threads and level information of game.
 */

public class Configuration {
    // timer
    private Time time = new Time();

    // const default values
    final public static int MAX_LEVEL = 3;
    final public static int UPGRADE_INTERVAL_MONEY = 5000;
    final public static int DEFAULT_STARTING_BABY = 5;

    final public static int MAX_SELLER_THREADS = 5;
    final public static int MAX_BUYER_THREADS = 5;

    // levels
    private int level_seller = 1;
    private int level_buyer = 1;
    private int level_promotion = 1;
    private int level_research = 1;

    // thread related
    private List<Thread> team_seller = null;
    private List<Thread> team_buyer = null;

    private Thread team_promotion = null;
    private Thread team_research = null;

    private int team_seller_count = 0;
    private int team_buyer_count = 0;

    public int getLevelSeller() {
        return level_seller;
    }

    public int getLevelBuyer() {
        return level_buyer;
    }

    public int getLevelPromotion() {
        return level_promotion;
    }

    public int getLevelResearch() {
        return level_research;
    }

    public boolean isNextMaxSeller() {
        return level_seller + 1 == MAX_LEVEL;
    }

    public boolean isNextMaxBuyer() {
        return level_buyer + 1 == MAX_LEVEL;
    }

    public boolean isNextMaxPromotion() {
        return level_promotion + 1 == MAX_LEVEL;
    }

    public boolean isNextMaxResearch() {
        return level_research + 1 == MAX_LEVEL;
    }

    public int getUpgradeCostSeller() {
        return level_seller * UPGRADE_INTERVAL_MONEY;
    }

    public int getUpgradeCostBuyer() {
        return level_buyer * UPGRADE_INTERVAL_MONEY;
    }

    public int getUpgradeCostPromotion() {
        return level_promotion * UPGRADE_INTERVAL_MONEY;
    }

    public int getUpgradeCostResearch() {
        return level_research * UPGRADE_INTERVAL_MONEY;
    }

    public String getTime() {
        return time.getTime();
    }

    public void startSeller() {
        if (team_seller_count < MAX_SELLER_THREADS) {
            team_seller.get(team_seller_count).start();
            team_seller_count++;
        }
    }

    public void startBuyer() {
        if (team_buyer_count < MAX_BUYER_THREADS) {
            team_buyer.get(team_buyer_count).start();
            team_buyer_count++;
        }
    }

    public void levelUpSeller() {
        if (level_seller < MAX_LEVEL) {
            level_seller++;
            startSeller();
        }
    }

    public void levelUpBuyer() {
        if (level_buyer < MAX_LEVEL) {
            level_buyer++;
            startBuyer();
        }
    }

    public void levelUpPromotion() {
        if (level_promotion < MAX_LEVEL) {
            level_promotion++;
        }
    }

    public void levelUpResearch() {
        if (level_research < MAX_LEVEL) {
            level_research++;
        }
    }

    public void startThreadsAndTimer() {
        startSeller();
        startBuyer();
        team_promotion.start();
        team_research.start();

        Timer.instance().start();
    }

    public void timerSetup() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                time.countDown();
            }
        }, 0, 1);
    }

    public void initialize() {
        SharedData.initialize();

        time = new Time();

        level_seller = 1;
        level_buyer = 1;
        level_promotion = 1;
        level_research = 1;

        team_seller = null;
        team_buyer = null;

        team_promotion = null;
        team_research = null;

        team_seller_count = 0;
        team_buyer_count = 0;

        for (int i = 0; i < DEFAULT_STARTING_BABY; i++) {
            SharedData.addBaby(new Baby());
        }

        team_seller = new ArrayList<Thread>();
        team_buyer = new ArrayList<Thread>();

        for (int i = 0; i < MAX_SELLER_THREADS; i++) {
            team_seller.add(new Thread(new SalesTeam()));
            System.out.println(i);
        }

        for (int i = 0; i < MAX_BUYER_THREADS; i++) {
            team_buyer.add(new Thread(new PurchaseTeam()));
        }

        team_promotion = new Thread(new PromotionTeam());
        team_research = new Thread(new ResearchTeam());
    }

    public void killThreads() {
        SharedData.endThreads();

        for (int i = 0; i < team_seller_count; i++) {
            try {
                team_seller.get(i).interrupt();
                team_seller.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(team_seller.get(i).isAlive());
        }

        for (int i = 0; i < team_buyer_count; i++) {
            try {
                team_buyer.get(i).interrupt();
                team_buyer.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(team_buyer.get(i).isAlive());
        }

        team_seller = null;
        team_buyer = null;

        team_seller_count = 0;
        team_buyer_count = 0;

        team_promotion.interrupt();
        team_research.interrupt();

        System.gc();
    }
}