// NOTE: not for submission
import danogl.*;
import danogl.collisions.Layer;
import danogl.components.*;
import danogl.gui.*;
import danogl.gui.rendering.*;
import danogl.util.Vector2;

// Import implemented avatar classes
import pepse.world.avatar.Avatar;
import pepse.world.avatar.EnergyUI;

import java.awt.*;

/**
 * Test harness for testing the new Avatar and EnergyUI.
 */
public class Platformer extends GameManager {
    private static final Color BACKGROUND_COLOR = Color.decode("#80C6E5");
    private static final Color PLATFORM_COLOR = new Color(212, 123, 74);

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        // 1. Background
        var background = new GameObject(
                Vector2.ZERO,
                windowController.getWindowDimensions(),
                new RectangleRenderable(BACKGROUND_COLOR)
        );
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);

        // 2. Platforms
        placePlatform(Vector2.of(-1024, 1000), Vector2.ONES.mult(2048));
        placePlatform(Vector2.of(-512, 700), Vector2.of(1024, 50));
        placePlatform(Vector2.of(-256, 400), Vector2.of(512, 50));
        placePlatform(Vector2.of(-128, 100), Vector2.of(256, 50));

        // 3. Create your new Avatar (passing imageReader for the sprites)
        var avatar = new Avatar(Vector2.of(0, 900), inputListener, imageReader);
        gameObjects().addGameObject(avatar);

        // 4. Create EnergyUI using method reference callback
        var energyUI = new EnergyUI(avatar::getEnergy);
        gameObjects().addGameObject(energyUI, Layer.UI);

        // 5. Camera tracking
        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(), windowController.getWindowDimensions()));
    }

    private void placePlatform(Vector2 pos, Vector2 size) {
        var platform = new GameObject(pos, size, new RectangleRenderable(PLATFORM_COLOR));
        platform.physics().preventIntersectionsFromDirection(Vector2.UP);
        platform.physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        gameObjects().addGameObject(platform, Layer.STATIC_OBJECTS);
    }

    public static void main(String[] args) {
        new Platformer().run();
    }
}