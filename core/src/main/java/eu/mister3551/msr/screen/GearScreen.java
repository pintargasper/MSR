package eu.mister3551.msr.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import eu.mister3551.msr.Constants;
import eu.mister3551.msr.database.object.Gear;
import eu.mister3551.msr.screen.components.Navigation;
import eu.mister3551.msr.screen.components.Popup;
import eu.mister3551.msr.screen.link.Callback;

import java.util.ArrayList;

public class GearScreen implements Screen {

    private final Skin skin;
    private final Stage stage;
    private final Navigation navigation;
    private final Popup popup;
    private final Gear gear;
    private final ArrayList<Gear> gears;
    private final ArrayList<TextButton> textButtons;
    private int selectedWeapon;

    public GearScreen() {
        this.skin = Constants.skin;
        this.stage = Constants.stage;
        this.navigation = Constants.navigation;
        this.popup = Constants.popup;
        this.gear = Constants.gears.stream()
            .filter(gear -> gear.getIdUser() != null)
            .findFirst()
            .orElse(new Gear());
        this.gears = Constants.gears;
        this.textButtons = new ArrayList<>();
        this.selectedWeapon = 0;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        Table navigationTable = navigation.back(skin, "MenuScreen");
        mainTable.add(navigationTable).growX();

        Table table = new Table();
        table.padTop(50.0f);

        Image image = new Image(skin, "gear-text");
        image.setScaling(Scaling.none);
        table.add(image).padBottom(20.0f).minHeight(20.0f).maxHeight(20.0f).colspan(2);

        table.row();
        Table table1 = new Table();
        Table table2 = new Table();

        TextButton textButton = new TextButton("Weapons", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!popup.isOpen()) {
                    popup.close();
                }
                table2.clear();
                gearTable(table2, gears);
                return true;
            }
        });
        table1.add(textButton).padBottom(5.0f);

        table1.row();
        textButton = new TextButton("Skins", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!popup.isOpen()) {
                    popup.close();
                }
                table2.clear();
                return true;
            }
        });
        table1.add(textButton);
        table.add(table1).padTop(20).align(Align.top);

        ScrollPane scrollPane = new ScrollPane(table2, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setFlickScroll(Gdx.app.getType().equals(Application.ApplicationType.Android));
        float width = Gdx.app.getType().equals(Application.ApplicationType.Android) ? 700.0f : 850.0f;
        table.add(scrollPane).padTop(20.0f).padBottom(20.0f).minWidth(width).width(width);

        table.row();
        table.add().padTop(5.0f).growY().colspan(2);

        table.row();
        textButton = new TextButton("Save gear", skin, "navigation");
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (popup.isOpen()) {
                    stage.addActor(popup.loadingPopup(skin));
                }

                gear.setActiveWeapon(String.valueOf(selectedWeapon));
                Constants.data.updateGear(gear, new Callback() {

                    @Override
                    public void onSuccess(Object object) {
                        Gdx.app.postRunnable(() -> {
                            if (!popup.isOpen()) {
                                popup.close();
                            }
                            String message = object.equals("SUCCESS") ? "Gear saved successfully" : "Something went wrong";

                            for (Gear gear : gears) {
                                gear.setActiveWeapon(String.valueOf(selectedWeapon));
                            }

                            if (popup.isOpen()) {
                                stage.addActor(popup.errorPopup(skin, message));
                            }
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Gdx.app.postRunnable(() -> {
                            if (!popup.isOpen()) {
                                popup.close();
                            }

                            if (popup.isOpen()) {
                                stage.addActor(popup.errorPopup(skin, errorMessage));
                            }
                        });
                    }
                });
                return true;
            }
        });
        table.add(textButton).colspan(2);

        mainTable.row();
        mainTable.add(table).expand().center().growY();

        gearTable(table2, gears);

        stage.setScrollFocus(scrollPane);
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        popup.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        popup.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
    }

    private void gearTable(Table table, ArrayList<Gear> gears) {
        int i = 0;
        textButtons.clear();
        for (Gear gear : gears) {
            Table table2 = new Table();
            table2.setBackground(skin.getDrawable("table-background"));

            Label label = new Label(gear.getName(), skin);
            label.setEllipsis(true);
            label.setWrap(true);
            table2.add(label).pad(5.0f).align(Align.left).minWidth(150.0f).maxWidth(150.0f);

            table2.row();
            Image image = new Image(skin, gear.getImage());
            image.setScaling(Scaling.fillX);
            table2.add(image).minWidth(150.0f).minHeight(100.0f).maxWidth(150.0f).maxHeight(100.0f);

            table2.row();
            TextButton textButton = new TextButton(
                gear.getIdUser() != null ?
                    gear.getActiveWeapon().matches(String.valueOf(gear.getId())) ?
                        "Selected" : "Select" : "Buy", skin, "navigation");
            textButtons.add(textButton);

            selectedWeapon = (gear.getIdUser() != null && selectedWeapon == 0) ?
                (gear.getActiveWeapon().matches(String.valueOf(gear.getId())) ? gear.getId() : selectedWeapon) : selectedWeapon;

            if (gear.getIdUser() == null) {
                textButton.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        if (popup.isOpen()) {
                            stage.addActor(popup.gearPopup(skin, gear));
                        }
                        return true;
                    }
                });
            } else {
                textButton.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                        if ("Select".equals(textButton.getText().toString())) {
                            textButton.setText("Selected");
                            selectedWeapon = gear.getId();

                            for (TextButton btn : textButtons) {
                                if (!btn.equals(textButton) && btn.getText().toString().matches("Selected")) {
                                    btn.setText("Select");
                                }
                            }
                        }
                        return true;
                    }
                });
            }
            table2.add(textButton).padBottom(5.0f);

            int perRow = Gdx.app.getType().equals(Application.ApplicationType.Android) ? 4 : 5;
            if (i % perRow == 0) {
                table.row();
            }
            i++;
            table.add(table2).pad(5.0f);
        }
    }
}
