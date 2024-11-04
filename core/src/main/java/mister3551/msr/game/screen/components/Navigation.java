package mister3551.msr.game.screen.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import mister3551.msr.game.Static;
import mister3551.msr.game.database.object.Gear;
import mister3551.msr.game.screen.link.Callback;

import java.util.ArrayList;

public class Navigation {

    public static Table signIn(Skin skin) {
        Table table = new Table();
        table.align(Align.top);

        Table table1 = new Table();
        table1.setBackground(skin.getDrawable("table-background"));
        table1.align(Align.left);

        Image image = new Image(skin, "logo-text");
        image.setScaling(Scaling.fit);
        table1.add(image).maxHeight(45.0f);
        table.add(table1).growX().align(Align.left);

        table1 = new Table();
        table1.setBackground(skin.getDrawable("table-background"));
        table1.align(Align.right);

        TextButton textButton = new TextButton("News", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.net.openURI("https://msr.gasperpintar.com/news");
                return true;
            }
        });

        table1.add(textButton).padRight(5.0f).minHeight(45.0f).maxHeight(45.0f);

        textButton = new TextButton("Sign up", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.net.openURI("https://msr.gasperpintar.com/sign-in");
                return true;
            }
        });

        table1.add(textButton).minHeight(45.0f).maxHeight(45.0f);
        table.add(table1).growX();
        return table;
    }

    public Table back(Skin skin, String screenName) {
        Table table = new Table();
        table.align(Align.top);

        Table table1 = new Table();
        table1.setBackground(skin.getDrawable("table-background"));
        table1.align(Align.left);

        Image image = new Image(skin, "logo-text");
        image.setScaling(Scaling.fit);
        table1.add(image).maxHeight(45.0f);
        table.add(table1).growX().align(Align.left);

        table1 = new Table();
        table1.setBackground(skin.getDrawable("table-background"));
        table1.align(Align.right);

        if (screenName != null) {
            TextButton textButtonBack = new TextButton("Back", skin, "navigation");
            textButtonBack.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Static.getScreenChanger().changeScreen(screenName);
                    return true;
                }
            });
            table1.add(textButtonBack).minHeight(45.0f).maxHeight(45.0f);
        } else {
            table1.add().minHeight(45.0f).maxHeight(45.0f);
        }
        table.add(table1).growX();
        return table;
    }

    public Table menu(Skin skin, Popup popup) {
        Table table = new Table();
        table.align(Align.top);

        Table table1 = new Table();
        table1.setBackground(skin.getDrawable("table-background"));
        table1.align(Align.left);

        Image image = new Image(skin, "logo-text");
        image.setScaling(Scaling.fit);
        table1.add(image).maxHeight(45.0f);
        table.add(table1).growX().align(Align.left);

        table1 = new Table();
        table1.setBackground(skin.getDrawable("table-background"));
        table1.align(Align.right);

        TextButton textButton = new TextButton("Gear", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (popup.isOpen()) {
                    Static.getStage().addActor(popup.loadingPopup(skin));
                }
                Static.getData().gear(new Callback() {
                    @Override
                    public void onSuccess(Object object) {
                        Static.setGears((ArrayList<Gear>) object);
                        Gdx.app.postRunnable(() -> {
                            popup.close();
                            Static.getScreenChanger().changeScreen("GearScreen");
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Gdx.app.postRunnable(() -> {
                            if (!popup.isOpen()) {
                                popup.close();
                                popup.setOpen(false);
                            }

                            if (popup.isOpen()) {
                                Static.getStage().addActor(popup.errorPopup(skin, errorMessage));
                            }
                        });
                    }
                });
                return true;
            }
        });
        table1.add(textButton).minHeight(45.0f).maxHeight(45.0f);
        table.add(table1).growX();
        return table;
    }
}
