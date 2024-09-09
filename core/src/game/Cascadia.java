package game;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

import config.GameScreen;
import config.Storage;

public class Cascadia implements Screen {
    Skin skin;
    Viewport vp;
    public Stage stage;
    private Game game;
    private GameScreen gameScreen;
    private Storage storage;
    private TextButton spin;
    private Label[][] elements = new Label[5][5];
    private Label scoreLabel;
    private int score;
    private Random rand = new Random();
    private float elX, elY, inc = 2f;
    private Texture aTex, bTex, cTex, dTex;

    public Cascadia(Viewport viewport, Game game, GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.game = game;
        stage = new Stage(viewport);
        vp = viewport;
        Gdx.input.setInputProcessor(stage);
        storage = Storage.getInstance();
        storage.createFont();
        skin = storage.skin;
        elX = vp.getWorldWidth() / 10f;
        elY = vp.getWorldHeight() / inc;
        score = 0;

        loadTextures();
        createComponents();
    }
    
    private void loadTextures() {
        // Ensure assets are loaded
        Storage.assetManager.finishLoading(); // Wait until all assets are loaded

        // Check if the assets are loaded
        if (Storage.assetManager.isLoaded("A.png")) {
            aTex = Storage.assetManager.get("A.png", Texture.class);
        } else {
            Gdx.app.error("Cascadia", "Asset not loaded: A.png");
        }
        if (Storage.assetManager.isLoaded("B.png")) {
            bTex = Storage.assetManager.get("B.png", Texture.class);
        } else {
            Gdx.app.error("Cascadia", "Asset not loaded: B.png");
        }
        if (Storage.assetManager.isLoaded("C.png")) {
            cTex = Storage.assetManager.get("C.png", Texture.class);
        } else {
            Gdx.app.error("Cascadia", "Asset not loaded: C.png");
        }
        if (Storage.assetManager.isLoaded("D.png")) {
            dTex = Storage.assetManager.get("D.png", Texture.class);
        } else {
            Gdx.app.error("Cascadia", "Asset not loaded: D.png");
        }
        
        aTex.setFilter(Texture.TextureFilter.MipMap, Texture.TextureFilter.Nearest);
        bTex.setFilter(Texture.TextureFilter.MipMap, Texture.TextureFilter.Nearest);
        cTex.setFilter(Texture.TextureFilter.MipMap, Texture.TextureFilter.Nearest);
        dTex.setFilter(Texture.TextureFilter.MipMap, Texture.TextureFilter.Nearest);
    }

    private Texture getRandomTexture() {
        int randIndex = rand.nextInt(4);
        switch (randIndex) {
            case 0:
                return aTex;
            case 1:
                return bTex;
            case 2:
                return cTex;
            case 3:
                return dTex;
            default:
                return null;
        }
    }
    
    private void startSpinningImage(final Image image) {
        // Action to continuously change the image's texture for 2 seconds
        image.addAction(Actions.repeat(20, Actions.sequence(
            Actions.run(() -> image.setDrawable(new TextureRegionDrawable(getRandomTexture()))),
            Actions.delay(0.05f) // Delay of 50 milliseconds
        )));

        // After 2 seconds, stop the spinning and set the final texture
        image.addAction(Actions.sequence(
            Actions.delay(0.1f), // Wait 2 seconds
            Actions.run(() -> image.setDrawable(new TextureRegionDrawable(getRandomTexture()))) // Set final random texture
        ));
    }

    private void createComponents() {
        spin = new TextButton("Spin", storage.buttonStyle);
        spin.setColor(Color.LIGHT_GRAY);
        spin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (elements[0][0] != null)
                    for (int i = 0; i < 5; i++)
                        for (int j = 0; j < 5; j++)
                            elements[i][j].remove();
                spinElements();
            }
        });
        spin.setSize(100, 100);
        spin.setPosition(vp.getWorldWidth() / 1.2f, vp.getWorldHeight() / 32f);
        stage.addActor(spin);

        // Create score label
        scoreLabel = new Label("Score: 0", storage.labelStyle);
        scoreLabel.setPosition(vp.getWorldWidth() / 20f, vp.getWorldHeight() - 50);
        stage.addActor(scoreLabel);
    }

    private void spinElements() {
        float elementSpacingX = 60f;
        float elementSpacingY = 60f;
        elX = vp.getWorldWidth() / 10f;
        elY = vp.getWorldHeight() - 100;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                elements[i][j] = new Label("", storage.labelStyle);
                float xPos = elX + j * elementSpacingX;
                float yPos = elY - i * elementSpacingY;
                elements[i][j].setPosition(xPos, yPos);
                elements[i][j].setSize(50, 50);
                stage.addActor(elements[i][j]);

                // Start the spin animation for each label
                startSpinningLabel(elements[i][j]);
            }
        }

        // Check for matches after the spin ends (delayed by 2 seconds)
        stage.addAction(Actions.sequence(
            Actions.delay(2f),
            Actions.run(this::checkMatches)
        ));
    }

    private void startSpinningLabel(final Label label) {
        // Action to continuously change the label's text for 2 seconds
        label.addAction(Actions.repeat(20, Actions.sequence(
            Actions.run(() -> label.setText(String.valueOf(rand.nextInt(1, 5)))), // Change label text randomly
            Actions.delay(0.05f) // Delay of 100 milliseconds
        )));
        
        // After 2 seconds, stop the spinning and set the final value
        label.addAction(Actions.sequence(
            Actions.delay(0.1f), // Wait 2 seconds
            Actions.run(() -> label.setText(String.valueOf(rand.nextInt(1, 5)))) // Set final random value
        ));
    }

    private void checkMatches() {
        boolean[][] toRemove = new boolean[5][5];

        // Check for horizontal matches
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                int matchLength = 1;
                String val = elements[i][j].getText().toString();

                for (int k = j + 1; k < 5 && val.equals(elements[i][k].getText().toString()); k++) {
                    matchLength++;
                }

                if (matchLength >= 3) {
                    for (int k = 0; k < matchLength; k++) {
                        toRemove[i][j + k] = true;
                    }
                    addPoints(matchLength);
                }
            }
        }

        // Check for vertical matches
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 3; i++) {
                int matchLength = 1;
                String val = elements[i][j].getText().toString();

                for (int k = i + 1; k < 5 && val.equals(elements[k][j].getText().toString()); k++) {
                    matchLength++;
                }

                if (matchLength >= 3) {
                    for (int k = 0; k < matchLength; k++) {
                        toRemove[i + k][j] = true;
                    }
                    addPoints(matchLength);
                }
            }
        }

        // Remove matches and trigger cascade
        applyMatches(toRemove);
    }

    private void addPoints(int matchLength) {
        if (matchLength == 3) {
            score += 1;
        } else if (matchLength == 4) {
            score += 3;
        } else if (matchLength == 5) {
            score += 6;
        }

        // Update score label
        scoreLabel.setText("Score: " + score);
    }

    private void applyMatches(boolean[][] toRemove) {
        boolean hasMatches = false;

        // First, fade out the matched elements across the entire grid
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (toRemove[i][j]) {
                    hasMatches = true;
                    elements[i][j].addAction(Actions.fadeOut(0.5f)); // Fade out all matched elements
                }
            }
        }

        // After all elements have faded out, perform the cascading
        if (hasMatches) {
            stage.addAction(Actions.sequence(
                Actions.delay(0.5f),  // Wait for the fade-out to complete
                Actions.run(this::performCascading) // Perform cascading after the delay
            ));
        }
    }

    private void cascadeDown(int col) {
        // Shift elements down in the column
        for (int i = 0; i < 5; i++) {
            if (elements[i][col].getColor().a == 0) { // If element is invisible
                // Move elements down from the row above
                for (int k = i; k > 0; k--) {
                    elements[k][col].setText(elements[k - 1][col].getText()); // Move down the element above
                    elements[k][col].setColor(elements[k - 1][col].getColor()); // Move down the color
                }
                // Assign new random value to the top row
                elements[0][col].setText(String.valueOf(rand.nextInt(1, 5)));
                elements[0][col].setColor(Color.WHITE); // Ensure the new element is visible
            }
        }
    }



    private void performCascading() {
        for (int col = 0; col < 5; col++) {
            cascadeDown(col);
        }
        
        // Optionally, you can trigger another match check after cascading
        // This ensures that new matches formed by cascading are also handled
        stage.addAction(Actions.sequence(
            Actions.delay(1f),  // Wait for the cascading to complete
            Actions.run(this::checkMatches) // Check for new matches
        ));
    }


    @Override
    public void show() {
        // TODO Auto-generated method stub
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Keys.F5)) {
            stage.clear();
            createComponents();
        }

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
    }
}