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
import com.jsl.babytrader.Controls.Configuration;
import com.jsl.babytrader.Data.Attribute;
import com.jsl.babytrader.Data.Baby;
import com.jsl.babytrader.Data.Customer;
import com.jsl.babytrader.Data.SharedData;
import com.jsl.babytrader.Popups.PopupPause;
import com.jsl.babytrader.Popups.PopupUpgrade;
import com.jsl.babytrader.Utilities.CommonUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    private Label label_sold = null;
    private Label label_purchased = null;

    // pause popup windows
    private PopupPause popup_pause = null;
    private Texture sprite_popup_paused = new Texture("sprites/popup_pause_305x240.png");
    private Texture sprite_button_popup_continue = new Texture("sprites/popup_pause_button_continue_186x45.png");
    private Texture sprite_button_popup_continue_inv = new Texture("sprites/popup_pause_button_continue_186x45.png");
    private Texture sprite_button_popup_mainMenu = new Texture("sprites/popup_pause_button_mainMenu_186x45.png");
    private Texture sprite_button_popup_mainMenu_inv = new Texture("sprites/popup_pause_button_mainMenu_inv_186x45.png");

    private ImageButton button_popup_continue = null;
    private ImageButton button_popup_mainMenu = null;

    // upgrade popup windows
    private PopupUpgrade popup_upgrade = null;
    private Texture sprite_popup_upgrade = new Texture("sprites/popup_upgrade_background_525x390.png");
    private Texture sprite_button_popup_cancel = new Texture("sprites/button_popup_cancel_186x45.png");
    private Texture sprite_button_popup_cancel_inv = new Texture("sprites/button_popup_cancel_inv_186x45.png");
    private Texture sprite_button_popup_upgrade = new Texture("sprites/button_popup_upgrade_186x45.png");
    private Texture sprite_button_popup_upgrade_inv = new Texture("sprites/button_popup_upgrade_inv_186x45.png");

    private ImageButton button_popup_cancel = null;
    private ImageButton button_popup_upgrade = null;

    // baby trader faces
    private Texture sprite_babyTrader_face_normal = new Texture("sprites/babyTraderFaceUi_normal_163x190.png");
    private Texture sprite_babyTrader_face_crazy = new Texture("sprites/babyTraderFaceUi_crazy_163x190.png");

    // meta data
    private int currentBabyIndex = 0;

    public static final float COLOR_BG_RED_GAME = 0.3882f;
    public static final float COLOR_BG_BLUE_GAME = 0.4392f;
    public static final float COLOR_BG_GREEN_GAME = 0.4745f;

    // configuration
    private Configuration config = null;

    public GameScreen(BabyTrader game) {
        super(game);

        config = new Configuration();

        // bgm setup
        setupMusic(getMusic(), true);

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
            popup_pause.getTable(),
            popup_upgrade.getTable(),
            label_sold,
            label_purchased
        );

        // taking inputs from ui
        Gdx.input.setInputProcessor(stage);
    }

    private String getMusic() {
        List<String> musicList = new ArrayList<String>();
        musicList.add("music/game_boukyaku_eq.mp3");
        musicList.add("music/game_katarusis.mp3");
        musicList.add("music/game_omoide_loft.mp3");

        return musicList.get(CommonUtilities.getRandomInteger(0, musicList.size()));
    }

    private void popupSetup() {
        // popup pause
        popup_pause = new PopupPause(sprite_popup_paused);

        button_popup_continue = generateButton(sprite_button_popup_continue, sprite_button_popup_continue_inv);
        button_popup_continue.setPosition(0, 0);

        button_popup_continue.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sound_buttonClick.play();
                popup_pause.getTable().setVisible(false);
                resume();
            }
        });

        button_popup_mainMenu = generateButton(sprite_button_popup_mainMenu, sprite_button_popup_mainMenu_inv);
        button_popup_mainMenu.setPosition(0, 0);

        button_popup_mainMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sound_buttonClick.play();
                switchScreen(BabyTrader.initScreen);
            }
        });

        popup_pause.addElements(button_popup_continue, button_popup_mainMenu);

        // popup upgrade
        popup_upgrade = new PopupUpgrade(
                sprite_popup_upgrade,
                getLabelStyle(FONT_WORK_EXTRA_BOLD, 29, Color.WHITE)
        );

        button_popup_cancel = generateButton(sprite_button_popup_cancel, sprite_button_popup_cancel_inv);
        button_popup_cancel.setPosition(0, 0);

        button_popup_cancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sound_buttonClick.play();
                resume();
                popup_upgrade.setVisible(false);
            }
        });

        button_popup_upgrade = generateButton(sprite_button_popup_upgrade, sprite_button_popup_upgrade_inv);
        button_popup_upgrade.setPosition(0, 0);

        button_popup_upgrade.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sound_buttonClick.play();
                upgrade();
            }
        });

        popup_upgrade.addElements(button_popup_cancel, button_popup_upgrade);
    }

    private void upgrade() {
        String warningMsg = "You don't have\nenough money to upgrade.".toUpperCase();
        String type = popup_upgrade.getTextType();

        if (type.equals(PopupUpgrade.TYPE_SELLER)) {
            if (SharedData.getMoney() >= Configuration.getUpgradeCostSeller()) {
                SharedData.spendMoney(Configuration.getUpgradeCostSeller());
                config.levelUpSeller();
                resume();
                popup_upgrade.setVisible(false);
            } else {
                popup_upgrade.setTextDescription(warningMsg);
            }
        } else if (type.equals(PopupUpgrade.TYPE_BUYER)){
            if (SharedData.getMoney() >= Configuration.getUpgradeCostBuyer()) {
                SharedData.spendMoney(Configuration.getUpgradeCostBuyer());
                config.levelUpBuyer();
                resume();
                popup_upgrade.setVisible(false);
            } else {
                popup_upgrade.setTextDescription(warningMsg);
            }
        } else if (type.equals(PopupUpgrade.TYPE_PROMOTION)) {
            if (SharedData.getMoney() >= Configuration.getUpgradeCostPromotion()) {
                SharedData.spendMoney(Configuration.getUpgradeCostPromotion());
                config.levelUpPromotion();
                resume();
                popup_upgrade.setVisible(false);
            } else {
                popup_upgrade.setTextDescription(warningMsg);
            }
        } else if (type.equals(PopupUpgrade.TYPE_RESEARCH)) {
            if (SharedData.getMoney() >= Configuration.getUpgradeCostResearch()) {
                SharedData.spendMoney(Configuration.getUpgradeCostResearch());
                config.levelUpResearch();
                resume();
                popup_upgrade.setVisible(false);
            } else {
                popup_upgrade.setTextDescription(warningMsg);
            }
        }
    }

    private void renderCustomer(Customer customer, Label label_title, Label label_properties, int x, int y, String description, boolean displaySprite, Label labelReplaceSprite) {
        if (customer != null) {
            if (displaySprite) {
                labelReplaceSprite.setVisible(true);
            } else {
                labelReplaceSprite.setVisible(false);
                stage.getBatch().draw(customer.getSprite(), x, y);
            }

            label_title.setText(customer.getName() + " (" + customer.getAge() + ")");

            StringBuilder stringBuilder = new StringBuilder((customer.isMale() ? "His" : "Her") + " " + description + ":\n");

            Set<Attribute> attributes = customer.isSelling() ? customer.getAttributes() : customer.getBaby().getAttributes();

            for (Attribute attribute : attributes) {
                stringBuilder.append(propertyFormat(attribute.getName()));
            }

            label_properties.setText(stringBuilder.toString().toUpperCase());
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

        if (baby != null) {
            stage.getBatch().draw(baby.getSprite(), 484, 131);

            label_properties_title_baby.setText(baby.getName().toUpperCase() + " (" + baby.getAge() + ") $" + baby.getSellPrice());

            StringBuilder stringBuilder = new StringBuilder();

            for (Attribute attribute : baby.getAttributes()) {
                stringBuilder.append(propertyFormat(attribute.getName()));
            }

            label_properties_list_baby.setText(stringBuilder.toString().toUpperCase());
        } else {
            label_properties_title_baby.setText("");
            label_properties_list_baby.setText("");
        }
    }

    // returns an attribute in a formatted string
    private static String propertyFormat(String attribute) {
        return "• " + attribute + "\n";
    }

    @Override
    public void render(float delta) {
        // exit if game is over
        if (SharedData.getMoney() <= 0 || config.isTimeOver()) {
            switchScreen(BabyTrader.gameOverScreen);
        }


        clearingScreen(COLOR_BG_RED_GAME, COLOR_BG_BLUE_GAME, COLOR_BG_GREEN_GAME, COLOR_BG_ALPHA);
        viewportRender();

        // stage.draw() must appear before game batch
        stage.act(Gdx.graphics.getDeltaTime());

        stage.getBatch().begin();
        stage.getBatch().draw(sprite_background, 0, 0);
        stage.getBatch().draw(Configuration.isBabyTraderFaceNormal() ? sprite_babyTrader_face_normal : sprite_babyTrader_face_crazy, 831, 319);

        // baby sprite
        renderBaby();

        // customer sprites
        // this one should appear when customer is accepted by sales / purchase team
        renderCustomer(SharedData.getCustomerSellingLatest(), label_properties_title_sell, label_properties_list_sell, 15, 306, "dream baby", Configuration.isSellerSold(), label_sold);
        renderCustomer(SharedData.getCustomerBuyingLatest(), label_properties_title_buy, label_properties_list_buy, 15, 15, "baby for sale", Configuration.isBuyerPurchased(), label_purchased);

        label_money.setText("$" + SharedData.getMoney());
        label_time.setText(config.getTime());
        label_count_babies.setText(SharedData.getBabySize() + "");
        label_count_customers_sell.setText(SharedData.getCustomerSellingSize() + "");
        label_count_customers_buy.setText(SharedData.getCustomerBuyingSize() + "");

        label_level_sell.setText(Configuration.getLevelSeller() + "");
        label_level_buy.setText(Configuration.getLevelBuyer() + "");

        stage.getBatch().end();

        stage.draw();

        game.batch.begin();
        game.batch.end();

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
                popupUpgradeOpen(PopupUpgrade.TYPE_PROMOTION, Configuration.getLevelPromotion(), Configuration.isNextMaxPromotion(), Configuration.getUpgradeCostPromotion(), PopupUpgrade.DESCRIPTION_PROMOTION);
            }
        });

        button_research = generateButton(sprite_button_research, sprite_button_research_inv);
        button_research.setPosition(817, 66);

        button_research.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Clicking Research button", "Activated");
                sound_buttonClick.play();
                popupUpgradeOpen(PopupUpgrade.TYPE_RESEARCH, Configuration.getLevelResearch(), Configuration.isNextMaxResearch(), Configuration.getUpgradeCostResearch(), PopupUpgrade.DESCRIPTION_RESEARCH);
            }
        });

        button_upgrade_sell = generateButton(sprite_button_upgrade, sprite_button_upgrade_inv);
        button_upgrade_sell.setPosition(13, 484);

        button_upgrade_sell.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Clicking Sell Upgrade button", "Activated");
                sound_buttonClick.play();
                popupUpgradeOpen(PopupUpgrade.TYPE_SELLER, Configuration.getLevelSeller(), Configuration.isNextMaxSeller(), Configuration.getUpgradeCostSeller(), PopupUpgrade.DESCRIPTION_SELLER);
            }
        });

        button_upgrade_buy = generateButton(sprite_button_upgrade, sprite_button_upgrade_inv);
        button_upgrade_buy.setPosition(13, 193);

        button_upgrade_buy.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Clicking Buy Upgrade button", "Activated");
                sound_buttonClick.play();
                popupUpgradeOpen(PopupUpgrade.TYPE_BUYER, Configuration.getLevelBuyer(), Configuration.isNextMaxBuyer(), Configuration.getUpgradeCostBuyer(), PopupUpgrade.DESCRIPTION_BUYER);
            }
        });
    }

    private void popupUpgradeOpen(String type, int level, boolean isMax, int cost, String description) {
        if (level != Configuration.MAX_LEVEL) {
            pause();
            popup_upgrade.setTextType(type);
            popup_upgrade.setTextLevel(PopupUpgrade.getLevelText(level, level + 1, isMax));
            popup_upgrade.setTextCost(PopupUpgrade.getMoneyText(cost));
            popup_upgrade.setTextDescription(description);
            popup_upgrade.setVisible(true);
        }
    }

    private void labelSetup() {
        label_money = new Label("", getLabelStyle(FONT_SARPANCH_SEMI_BOLD, 28, FONT_COLOR_GREEN));
        label_money.setAlignment(Align.right);
        label_money.setPosition(994, 190);

        label_time = new Label("", getLabelStyle(FONT_SARPANCH_SEMI_BOLD, 28, FONT_COLOR_GREEN));
        label_time.setAlignment(Align.right);
        label_time.setPosition(994, 260);

        label_count_babies = new Label("", getLabelStyle(FONT_WORK_EXTRA_BOLD, 43, FONT_COLOR_LIGHT_GRAY));
        label_count_babies.setAlignment(Align.right);
        label_count_babies.setPosition(607, 545);

        label_count_customers_sell = new Label("", getLabelStyle(FONT_WORK_EXTRA_BOLD, 43, FONT_COLOR_LIGHT_GRAY));
        label_count_customers_sell.setAlignment(Align.right);
        label_count_customers_sell.setPosition(290, 502);

        label_count_customers_buy = new Label("", getLabelStyle(FONT_WORK_EXTRA_BOLD, 43, FONT_COLOR_LIGHT_GRAY));
        label_count_customers_buy.setAlignment(Align.right);
        label_count_customers_buy.setPosition(290, 209);

        label_properties_title_baby = new Label("", getLabelStyle(FONT_WORK_EXTRA_BOLD, 20, FONT_COLOR_LIGHT_GRAY));
        label_properties_title_baby.setPosition(505, 113);

        label_properties_list_baby = new Label("", getLabelStyle(FONT_WORK_EXTRA_BOLD, 14, FONT_COLOR_LIGHT_GRAY));
        label_properties_list_baby.setPosition(505, 55);

        label_properties_title_sell = new Label("", getLabelStyle(FONT_WORK_EXTRA_BOLD, 20, FONT_COLOR_LIGHT_GRAY));
        label_properties_title_sell.setPosition(193, 418);

        label_properties_list_sell = new Label("", getLabelStyle(FONT_WORK_EXTRA_BOLD, 14, FONT_COLOR_LIGHT_GRAY));
        label_properties_list_sell.setPosition(193, 350);

        label_properties_title_buy = new Label("", getLabelStyle(FONT_WORK_EXTRA_BOLD, 20, FONT_COLOR_LIGHT_GRAY));
        label_properties_title_buy.setPosition(193, 127);

        label_properties_list_buy = new Label("", getLabelStyle(FONT_WORK_EXTRA_BOLD, 14, FONT_COLOR_LIGHT_GRAY));
        label_properties_list_buy.setPosition(193, 59);

        label_level_sell = new Label("", getLabelStyle(FONT_WORK_EXTRA_BOLD, 15, Color.WHITE));
        label_level_sell.setPosition(154, 549);

        label_level_buy = new Label("", getLabelStyle(FONT_WORK_EXTRA_BOLD, 15, Color.WHITE));
        label_level_buy.setPosition(154, 258);

        label_sold = new Label("Sold!", getLabelStyle(FONT_WORK_EXTRA_BOLD, 30, FONT_COLOR_LIGHT_GRAY));
        label_sold.setPosition(97 - label_sold.getWidth() / 2, 390);
        label_sold.setVisible(false);

        label_purchased = new Label("Purchased!", getLabelStyle(FONT_WORK_EXTRA_BOLD, 26, FONT_COLOR_LIGHT_GRAY));
        label_purchased.setPosition(97 - label_purchased.getWidth() / 2, 99);
        label_purchased.setVisible(false);
    }

    @Override
    public void pause() {
        Timer.instance().stop();
        SharedData.pause();

        setButtonDisabled(true);
    }

    @Override
    public void resume() {
        Timer.instance().start();
        SharedData.resume();

        setButtonDisabled(false);
    }

    private void setButtonDisabled(boolean isDisabled) {
        button_browse_left.setDisabled(isDisabled);
        button_browse_right.setDisabled(isDisabled);
        button_menu.setDisabled(isDisabled);
        button_promotion.setDisabled(isDisabled);
        button_research.setDisabled(isDisabled);
        button_upgrade_sell.setDisabled(isDisabled);
        button_upgrade_buy.setDisabled(isDisabled);
    }

    @Override
    public void hide() {
        super.hide();
        config.killThreads();
        config.timerCancel();

        // pick another random music
        setupMusic(getMusic(), true);
    }

    @Override
    public void show() {
        super.show();
        config.initialize();
        config.timerSetup();
        config.startThreadsAndTimer();
        popup_pause.setVisible(false);
        setButtonDisabled(false);

        // in case when game stopped in the middle of sprite change
        label_sold.setVisible(false);
        label_purchased.setVisible(false);
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
        sprite_button_popup_continue.dispose();
        sprite_button_popup_continue_inv.dispose();
        sprite_button_popup_mainMenu.dispose();
        sprite_button_popup_mainMenu_inv.dispose();

        sprite_popup_upgrade.dispose();
        sprite_button_popup_cancel.dispose();
        sprite_button_popup_cancel_inv.dispose();
        sprite_button_popup_upgrade.dispose();
        sprite_button_popup_upgrade_inv.dispose();

        sprite_babyTrader_face_normal.dispose();
        sprite_babyTrader_face_crazy.dispose();
    }
}
