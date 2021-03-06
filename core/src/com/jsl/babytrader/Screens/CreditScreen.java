package com.jsl.babytrader.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.jsl.babytrader.BabyTrader;

/**
 * Credit screen that shows the creator name and tools used for this project.
 */

public class CreditScreen extends BaseScreen {
    // pure graphics
    private  Texture sprite_title = new Texture("sprites/credits_title_400x100.png");

    // buttons
    private Texture sprite_button_back_up = new Texture("sprites/credits_backToTitle_400x500.png");
    private Texture sprite_button_back_down = new Texture("sprites/credits_backToTitle_400x500_inv.png");

    private ImageButton button_back = null;

    public CreditScreen(final BabyTrader game) {
        super(game);

        // bgm setup
        setupMusic("music/credit_kokoro_kizu.mp3", true);

        // startThreadsAndTimer button setup
        button_back = generateButton(sprite_button_back_up, sprite_button_back_down);
        button_back.setPosition((SCREEN_WIDTH / 2) - (sprite_button_back_up.getWidth() / 2), 60);

        button_back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sound_buttonClick.play();
                switchScreen(BabyTrader.initScreen);
            }
        });

        addElementsToStage(getLabelTable(), button_back);

        // taking inputs from ui
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        clearingScreen();
        viewportRender();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.getBatch().begin();
        stage.getBatch().draw(
            sprite_title,
            (SCREEN_WIDTH / 2) - (sprite_title.getWidth() / 2),
            SCREEN_HEIGHT - sprite_title.getHeight() - 50
        );
        stage.getBatch().end();

        stage.draw();

        game.batch.begin();
        game.batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dispose() {
        super.dispose();

        // sprites
        sprite_title.dispose();

        // buttons
        sprite_button_back_up.dispose();
        sprite_button_back_down.dispose();
    }

    private Label getLabel(String text, Label.LabelStyle style) {
        Label label = new Label(text.toUpperCase(), style);
        label.setAlignment(Align.center);
        label.setPosition(0, 0);

        return label;
    }

    public Table getLabelTable() {
        final String website_developer = "chaosdepot.com";
        final String website_tool = "libgdx.badlogicgames.com";
        final String website_music = "rengoku-teien.com";
        final String website_prefix = "http://";

        Table result = new Table();

        Label.LabelStyle style_small = getLabelStyle(FONT_WORK_EXTRA_BOLD, 26, FONT_COLOR_DARK_BLUE);
        Label.LabelStyle style_big = getLabelStyle(FONT_WORK_EXTRA_BOLD, 40, FONT_COLOR_DARK_BLUE);

        Label label_developedBy = getLabel("Developed by", style_small);
        Label label_developer = getLabel("Jong Seong Lee", style_big);
        Label label_developerWebsite = getLabel(website_developer, style_small);

        label_developerWebsite.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI(website_prefix + website_developer);
            }
        });

        Label label_toolBy = getLabel("Game framework engine", style_small);
        Label label_tool = getLabel("LibGDX", style_big);
        Label label_toolWebsite = getLabel(website_tool, style_small);

        label_toolWebsite.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI(website_prefix + website_tool);
            }
        });

        Label label_musicBy = getLabel("Background music", style_small);
        Label label_music = getLabel("Rengoku Teien", style_big);
        Label label_musicWebsite = getLabel(website_music, style_small);

        label_musicWebsite.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI(website_prefix + website_music);
            }
        });

        float padBottom = 20;
        float padTop = -6;

        result.add(label_developedBy);
        result.row();
        result.add(label_developer).padTop(padTop);
        result.row();
        result.add(label_developerWebsite).padTop(padTop).padBottom(padBottom);
        result.row();
        result.add(label_toolBy);
        result.row();
        result.add(label_tool).padTop(padTop);
        result.row();
        result.add(label_toolWebsite).padTop(padTop).padBottom(padBottom);
        result.row();
        result.add(label_musicBy);
        result.row();
        result.add(label_music).padTop(padTop);
        result.row();
        result.add(label_musicWebsite).padTop(padTop);

        result.setPosition((SCREEN_WIDTH / 2) - result.getWidth(), 290);

        return result;
    }
}
