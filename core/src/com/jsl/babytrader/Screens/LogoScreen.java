package com.jsl.babytrader.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.jsl.babytrader.BabyTrader;
import com.jsl.babytrader.Data.ConstData;
import com.jsl.babytrader.Utilities.CommonUtilities;

/**
 * Displays logo.
 */

public class LogoScreen extends BaseScreen {
    private Texture texture_logo = new Texture("sprites/logoScreen_logo_400x85.png");
    private Thread thread_counter = null;

    public LogoScreen(BabyTrader game) {
        super(game);

        // taking inputs from ui
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        clearingScreen(1f, 1f, 1f, 1f);
        viewportRender();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.getBatch().begin();
        stage.getBatch().draw(texture_logo, ConstData.SCREEN_WIDTH / 2 - texture_logo.getWidth() / 2, ConstData.SCREEN_HEIGHT / 2 - texture_logo.getHeight() / 2);
        stage.getBatch().end();

        stage.draw();

        game.batch.begin();
        game.batch.end();
    }

    @Override
    public void hide() {
        super.hide();

        try {
            thread_counter.interrupt();
            thread_counter.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.gc();
    }

    @Override
    public void show() {
        super.show();
        thread_counter = new Thread(new Runnable() {
            @Override
            public void run() {
            CommonUtilities.sleep(3000);

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    switchScreen(BabyTrader.initScreen);
                }
            });
            }
        });

        thread_counter.start();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
