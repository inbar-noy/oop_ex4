package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.avatar.Avatar;
import pepse.world.avatar.EnergyUI;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;

import java.awt.Color;
import java.util.List;

/**
 * Main game manager that coordinates all simulation subsystems in the PEPSE project.
 *
 * @author Inbar Noy
 */
public class PepseGameManager extends GameManager {
    private static final float WINDOW_WIDTH = 1200f;
    private static final float WINDOW_HEIGHT = 690f;
    public static final float DAY_CYCLE = 30.0f;

    public static final String SKY_TAG = "sky";
    public static final String AVATAR_TAG = "avatar";
    public static final String GROUND_SURFACE_TAG = "surface_ground";
    public static final String GROUND_INNER_TAG = "inner_ground";
    public static final String TRUNK_TAG = "trunk";
    public static final String LEAF_TAG = "leaf";
    public static final String FRUIT_TAG = "fruit";

    public static final Color BROWN = new Color(100, 50, 20);
    public static final Color GREEN = new Color(50, 200, 30);

    /**
     * Default constructor for PepseGameManager.
     */
    public PepseGameManager() {
        super("Pepse", new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT));
    }

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();

        // 1. Sky
        GameObject sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);

        // 2. Terrain / Ground
        Terrain terrain = new Terrain(windowDimensions, 0);
        List<GameObject> blocks = terrain.createInRange(-1000, 2000);
        for (GameObject obj : blocks) {
            gameObjects().addGameObject(obj, Layer.STATIC_OBJECTS);
        }

        // 3. Sun & SunHalo (SunHalo drawn behind Sun)
        GameObject sun = Sun.create(windowDimensions, DAY_CYCLE);
        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND + 5);
        gameObjects().addGameObject(sun, Layer.BACKGROUND + 10);

        // 4. Night Overlay (covers world in FOREGROUND)
        GameObject night = Night.create(windowDimensions, DAY_CYCLE);
        gameObjects().addGameObject(night, Layer.FOREGROUND);

        // 5. Avatar Creation
        float startX = windowDimensions.x() * 0.5f;
        float groundY = terrain.groundHeightAt(startX);
        Vector2 initialAvatarLocation = new Vector2(startX, groundY - 50);
        Avatar avatar = new Avatar(initialAvatarLocation, inputListener,
                imageReader, terrain::groundHeightAt);
        gameObjects().addGameObject(avatar, Layer.DEFAULT);

        // 6. Flora / Trees
        Flora flora = new Flora(terrain::groundHeightAt, avatar::updateEnergy);
        List<GameObject> treeParts = flora.createInRange(-1000, 2000);

        for (GameObject part : treeParts) {
            if (part.getTag().equals(TRUNK_TAG)) {
                gameObjects().addGameObject(part, Layer.STATIC_OBJECTS);
            } else {
                gameObjects().addGameObject(part, Layer.DEFAULT);
            }
        }

        // 7. Energy UI Display
        EnergyUI energyUI = new EnergyUI(avatar::getEnergy);
        gameObjects().addGameObject(energyUI, Layer.UI);

        // 8. Camera Tracking with initial offset
        Vector2 cameraOffset = windowDimensions.mult(0.5f).subtract(initialAvatarLocation);
        setCamera(new Camera(
                avatar,
                cameraOffset,
                windowDimensions,
                windowDimensions
        ));
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}