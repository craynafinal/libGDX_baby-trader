package com.jsl.babytrader.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.Timer;
import com.jsl.babytrader.BabyTrader;
import com.jsl.babytrader.Data.Attribute;
import com.jsl.babytrader.Data.Baby;
import com.jsl.babytrader.Data.ConstData;
import com.jsl.babytrader.Data.Customer;
import com.jsl.babytrader.Data.SharedData;
import com.jsl.babytrader.Data.Time;
import com.jsl.babytrader.Popups.PopupPause;
import com.jsl.babytrader.Runnables.PromotionTeam;
import com.jsl.babytrader.Runnables.PurchaseTeam;
import com.jsl.babytrader.Runnables.ResearchTeam;
import com.jsl.babytrader.Runnables.SalesTeam;

import java.util.ArrayList;
import java.util.List;

/**
 * Actual game screen for play.
 */

public class GameScreen extends BaseScreen {
    // background graphic
    private Texture sprite_background = new Texture("sprites/gameScreen_background_1024x576.png");

    // buttons
    private Texture sprite_button_browse_left = new Texture("sprites/gameScreen_browse_left_34x35.png");
    private Texture sprite_button_browse_left_inv = new Texture("sprites/gameScreen_browse_left_inv_34x35.png");
    private Texture sprite_button_browse_right = new Texture("sprites/gameScreen_browse_right_34x35.png");
    private Texture sprite_button_browse_right_inv = new Texture("sprites/gameScreen_browse_right_inv_34x35.png");
    private Texture sprite_button_menu = new Texture("sprites/gameScreen_menuButton_186x45.png");
    private Texture sprite_button_menu_inv = new Texture("sprites/gameScreen_menuButton_inv_186x45.png");
    private Texture sprite_button_promotion = new Texture("sprites/gameScreen_promotionButton_186x45.png");
    private Texture sprite_button_promotion_inv = new Texture("sprites/gameScreen_promotionButton_inv_186x45.png");
    private Texture sprite_button_research = new Texture("sprites/gameScreen_researchButton_186x45.png");
    private Texture sprite_button_research_inv = new Texture("sprites/gameScreen_researchButton_inv_186x45.png");
    private Texture sprite_button_upgrade = new Texture("sprites/gameScreen_upgradeButton_167x45.png");
    private Texture sprite_button_upgrade_inv = new Texture("sprites/gameScreen_upgradeButton_inv_167x45.png");

    private ImageButton button_browse_left = null;
    private ImageButton button_browse_right = null;
    private ImageButton button_menu = null;
    private ImageButton button_promotion = null;
    private ImageButton button_research = null;
    private ImageButton button_upgrade_sell = null;
    private ImageButton button_upgrade_buy = null;

    private Label label_money = null;
    private Label label_time = null;

    private Label label_count_babies = null;
    private Label label_count_customers_sell = null;
    private Label label_count_customers_buy = null;

    private Label label_properties_title_baby = null;
    private Label label_properties_list_baby = null;
    private Label label_properties_title_sell = null;
    private Label label_properties_list_sell = null;
    private Label label_properties_title_buy = null;
    private Label label_properties_list_buy = null;

    private Label label_level_sell = null;
    private Label label_level_buy = null;

    // popup windows
    private PopupPause popup_pause = null;
    private Texture sprite_popup_paused = new Texture("sprites/popup_pause_305x240.png");
    private Texture sprite_button_continue = new Texture("sprites/popup_pause_button_continue_186x45.png");
    private Texture sprite_button_continue_inv = new Texture("sprites/popup_pause_button_continue_186x45.png");
    private Texture sprite_button_mainMenu = new Texture("sprites/popup_pause_button_mainMenu_186x45.png");
    private Texture sprite_button_mainMenu_inv = new Texture("sprites/popup_pause_button_mainMenu_inv_186x45.png");

    private ImageButton button_continue = null;
    private ImageButton button_mainMenu = null;

    // meta data
    private int currentBabyIndex = 0;
    private Time time = new Time();

    // const default values
    final public static int MAX_LEVEL = 3;
    final public static int UPGRADE_INTERVAL_MONEY = 3000;
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

    public GameScreen(BabyTrader game) {
        super(game);

        //initialize();
        timerSetup();
        //startThreadsAndTimer();

        // bgm setup
        // TODO: switch the file extension to something cheap
        setupMusic("music/bgm_usodarake.wav", true);

        popupSetup();
        labelSetup();
        buttonSetup();

        addElementsToStage(
            button_browse_left,
            button_browse_right,
            button_menu,
            button_promotion,
            button_research,
            button_upgrade_sell,
            button_upgrade_buy,
            label_money,
            label_time,
            label_count_babies,
            label_count_customers_sell,
            label_count_customers_buy,
            label_properties_title_baby,
            label_properties_list_baby,
            label_properties_title_sell,
            label_properties_list_sell,
            label_properties_title_buy,
            label_properties_list_buy,
            label_level_sell,
            label_level_buy,
            popup_pause.getTable()
        );

        // taking inputs from ui
        Gdx.input.setInputProcessor(stage);
    }

    private void startSeller() {
        if (team_seller_count < MAX_SELLER_THREADS) {
            team_seller.get(team_seller_count).start();
            team_seller_count++;
        }
    }

    private void startBuyer() {
        if (team_buyer_count < MAX_BUYER_THREADS) {
            team_buyer.get(team_buyer_count).start();
            team_buyer_count++;
        }
    }

    private void levelUpSeller() {
        if (level_seller < MAX_LEVEL) {
            level_seller++;
            startSeller();
        }
    }

    private void levelUpBuyer() {
        if (level_buyer < MAX_LEVEL) {
            level_buyer++;
            startBuyer();
        }
    }

    private void levelUpPromotion() {
        if (level_promotion < MAX_LEVEL) {
            level_promotion++;
        }
    }

    private void levelUpResearch() {
        if (level_research < MAX_LEVEL) {
            level_research++;
        }
    }

    private void startThreadsAndTimer() {
        startSeller();
        startBuyer();
        team_promotion.start();
        team_research.start();

        Timer.instance().start();
    }

    private void timerSetup() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                time.countDown();
            }
        }, 0, 1);
    }

    private void initialize() {
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

    private void popupSetup() {
        popup_pause = new PopupPause(sprite_popup_paused);

        button_continue = generateButton(sprite_button_continue, sprite_button_continue_inv);
        button_continue.setPosition(0, 0);

        button_continue.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Clicking Continue button", "Activated");
                sound_buttonClick.play();
                popup_pause.getTable().setVisible(false);
                resume();
            }
        });

        button_mainMenu = generateButton(sprite_button_mainMenu, sprite_button_mainMenu_inv);
        button_mainMenu.setPosition(0, 0);

        button_mainMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Clicking Main Menu button", "Activated");
                sound_buttonClick.play();

                switchScreen(BabyTrader.initScreen);
            }
        });

        popup_pause.addElements(button_continue, button_mainMenu);
    }

    private void renderCustomer(Customer customer, Label label_title, Label label_properties, int x, int y, String description) {
        if (customer != null) {
            stage.getBatch().draw(customer.getSprite(), x, y);

            label_title.setText(customer.getName() + " (" + customer.getAge() + ")");

            StringBuilder stringBuilder = new StringBuilder((customer.isMale() ? "His" : "Her") + " " + description + ":\n");

            for (Attribute attribute : customer.getAttributes()) {
                stringBuilder.append(propertyFormat(attribute.getName()));
            }

            label_properties.setText(stringBuilder);
        }
    }

    private void renderBaby() {
        Baby baby = null;

        synchronized (this) {
            if (currentBabyIndex >= SharedData.getBabySize()) {
                currentBabyIndex = SharedData.getBabySize() - 1;
            } else if (currentBabyIndex < 0) {
                currentBabyIndex = 0;
            }
            baby = SharedData.getBabyWithoutRemoval(currentBabyIndex);
        }

        stage.getBatch().draw(baby.getSprite(), 484, 131);

        label_properties_title_baby.setText(baby.getName() + " (" + baby.getAge() + ") $"+ baby.getSellPrice());

        StringBuilder stringBuilder = new StringBuilder();

        for (Attribute attribute : baby.getAttributes()) {
            stringBuilder.append(propertyFormat(attribute.getName()));
        }

        label_properties_list_baby.setText(stringBuilder);
    }

    // returns an attribute in a formatted string
    private static String propertyFormat(String attribute) {
        return "• " + attribute + "\n";
    }

    @Override
    public void render(float delta) {
        clearingScreen();
        viewportRender();

        // stage.draw() must appear before game batch
        stage.act(Gdx.graphics.getDeltaTime());

        stage.getBatch().begin();
        stage.getBatch().draw(sprite_background, 0, 0);

        // baby sprite
        renderBaby();

        // customer sprites
        // this one should appear when customer is accepted by sales / purchase team
        renderCustomer(SharedData.getCustomerSellingLatest(), label_properties_title_sell, label_properties_list_sell, 15, 306, "dream baby");
        renderCustomer(SharedData.getCustomerBuyingLatest(), label_properties_title_buy, label_properties_list_buy, 15, 15, "baby for sale");

        label_money.setText("$" + SharedData.getMoney());
        label_time.setText(time.getTime());
        label_count_babies.setText(SharedData.getBabySize() + "");
        label_count_customers_sell.setText(SharedData.getCustomerSellingSize() + "");
        label_count_customers_buy.setText(SharedData.getCustomerBuyingSize() + "");

        label_level_sell.setText(level_seller + "");
        label_level_buy.setText(level_buyer + "");

        stage.getBatch().end();

        stage.draw();

        game.batch.begin();
        game.batch.end();

    }

    protected static void clearingScreen() {
        Gdx.gl.glClearColor(
                ConstData.COLOR_BG_RED_GAME,
                ConstData.COLOR_BG_BLUE_GAME,
                ConstData.COLOR_BG_GREEN_GAME,
                ConstData.COLOR_BG_ALPHA
        );

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void buttonSetup() {
        button_browse_left = generateButton(sprite_button_browse_left, sprite_button_browse_left_inv);
        button_browse_left.setPosition(467, 528);

        button_browse_left.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Clicking Browse Left button", "Activated");
                currentBabyIndex--;
                sound_buttonClick.play();
            }
        });

        button_browse_right = generateButton(sprite_button_browse_right, sprite_button_browse_right_inv);
        button_browse_right.setPosition(738, 528);

        button_browse_right.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Clicking Browse Right button", "Activated");
                currentBabyIndex++;
                sound_buttonClick.play();
            }
        });

        button_menu = generateButton(sprite_button_menu, sprite_button_menu_inv);
        button_menu.setPosition(817, 14);

        button_menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Clicking Menu button", "Activated");
                sound_buttonClick.play();

                // TODO: currently this is here for testing purpose
                pause();
                popup_pause.setVisible(true);
            }
        });

        button_promotion = generateButton(sprite_button_promotion, sprite_button_promotion_inv);
        button_promotion.setPosition(817, 118);

        button_promotion.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Clicking Promotion button", "Activated");
                sound_buttonClick.play();
                pause();
            }
        });

        button_research = generateButton(sprite_button_research, sprite_button_research_inv);
        button_research.setPosition(817, 66);

        button_research.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Clicking Research button", "Activated");
                sound_buttonClick.play();

                // TODO: currently this is here for testing purpose
                pause();
            }
        });

        button_upgrade_sell = generateButton(sprite_button_upgrade, sprite_button_upgrade_inv);
        button_upgrade_sell.setPosition(13, 484);

        button_upgrade_sell.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Clicking Sell Upgrade button", "Activated");
                sound_buttonClick.play();

                // TODO: currently this is here for testing purpose
                // need more configuration such as popup window
                levelUpSeller();
            }
        });

        button_upgrade_buy = generateButton(sprite_button_upgrade, sprite_button_upgrade_inv);
        button_upgrade_buy.setPosition(13, 193);

        button_upgrade_buy.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Clicking Buy Upgrade button", "Activated");
                sound_buttonClick.play();

                // need more configuration such as popup window
                levelUpBuyer();
            }
        });
    }

    private void labelSetup() {
        label_money = new Label("", new Label.LabelStyle(generateFont(FONT_SARPANCH_SEMI_BOLD, 28, FONT_COLOR_GREEN), FONT_COLOR_GREEN));
        label_money.setAlignment(Align.right);
        label_money.setPosition(994, 190);

        label_time = new Label("", new Label.LabelStyle(generateFont(FONT_SARPANCH_SEMI_BOLD, 28, FONT_COLOR_GREEN), FONT_COLOR_GREEN));
        label_time.setAlignment(Align.right);
        label_time.setPosition(994, 260);

        label_count_babies = new Label("", new Label.LabelStyle(generateFont(FONT_WORK_EXTRA_BOLD, 43, FONT_COLOR_LIGHT_GRAY), FONT_COLOR_LIGHT_GRAY));
        label_count_babies.setAlignment(Align.right);
        label_count_babies.setPosition(607, 545);

        label_count_customers_sell = new Label("", new Label.LabelStyle(generateFont(FONT_WORK_EXTRA_BOLD, 43, FONT_COLOR_LIGHT_GRAY), FONT_COLOR_LIGHT_GRAY));
        label_count_customers_sell.setAlignment(Align.right);
        label_count_customers_sell.setPosition(290, 502);

        label_count_customers_buy = new Label("", new Label.LabelStyle(generateFont(FONT_WORK_EXTRA_BOLD, 43, FONT_COLOR_LIGHT_GRAY), FONT_COLOR_LIGHT_GRAY));
        label_count_customers_buy.setAlignment(Align.right);
        label_count_customers_buy.setPosition(290, 209);

        label_properties_title_baby = new Label("", new Label.LabelStyle(generateFont(FONT_WORK_EXTRA_BOLD, 20, FONT_COLOR_LIGHT_GRAY), FONT_COLOR_LIGHT_GRAY));
        label_properties_title_baby.setPosition(505, 113);

        label_properties_list_baby = new Label("", new Label.LabelStyle(generateFont(FONT_WORK_EXTRA_BOLD, 14, FONT_COLOR_LIGHT_GRAY), FONT_COLOR_LIGHT_GRAY));
        label_properties_list_baby.setPosition(505, 55);

        label_properties_title_sell = new Label("", new Label.LabelStyle(generateFont(FONT_WORK_EXTRA_BOLD, 20, FONT_COLOR_LIGHT_GRAY), FONT_COLOR_LIGHT_GRAY));
        label_properties_title_sell.setPosition(193, 418);

        label_properties_list_sell = new Label("", new Label.LabelStyle(generateFont(FONT_WORK_EXTRA_BOLD, 14, FONT_COLOR_LIGHT_GRAY), FONT_COLOR_LIGHT_GRAY));
        label_properties_list_sell.setPosition(193, 354);

        label_properties_title_buy = new Label("", new Label.LabelStyle(generateFont(FONT_WORK_EXTRA_BOLD, 20, FONT_COLOR_LIGHT_GRAY), FONT_COLOR_LIGHT_GRAY));
        label_properties_title_buy.setPosition(193, 127);

        label_properties_list_buy = new Label("", new Label.LabelStyle(generateFont(FONT_WORK_EXTRA_BOLD, 14, FONT_COLOR_LIGHT_GRAY), FONT_COLOR_LIGHT_GRAY));
        label_properties_list_buy.setPosition(193, 63);

        label_level_sell = new Label("", new Label.LabelStyle(generateFont(FONT_WORK_EXTRA_BOLD, 15, Color.WHITE), Color.WHITE));
        label_level_sell.setPosition(154, 549);

        label_level_buy = new Label("", new Label.LabelStyle(generateFont(FONT_WORK_EXTRA_BOLD, 15, Color.WHITE), Color.WHITE));
        label_level_buy.setPosition(154, 258);
    }

    @Override
    public void pause() {
        Timer.instance().stop();
        SharedData.pause();
    }

    @Override
    public void resume() {
        Timer.instance().start();
        SharedData.resume();
    }

    @Override
    public void hide() {
        System.out.println("hide called");

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

    @Override
    public void show() {
        super.show();
        initialize();
        startThreadsAndTimer();
        //config.startThreadsAndTimer();
        popup_pause.setVisible(false);
    }

    @Override
    public void dispose() {
        super.dispose();

        sprite_background.dispose();
        sprite_button_browse_left.dispose();
        sprite_button_browse_left_inv.dispose();
        sprite_button_browse_right.dispose();
        sprite_button_browse_right_inv.dispose();
        sprite_button_menu.dispose();
        sprite_button_menu_inv.dispose();
        sprite_button_promotion.dispose();
        sprite_button_promotion_inv.dispose();
        sprite_button_research.dispose();
        sprite_button_research_inv.dispose();
        sprite_button_upgrade.dispose();
        sprite_button_upgrade_inv.dispose();

        sprite_popup_paused.dispose();
        sprite_button_continue.dispose();
        sprite_button_continue_inv.dispose();
        sprite_button_mainMenu.dispose();
        sprite_button_mainMenu_inv.dispose();
    }
}
