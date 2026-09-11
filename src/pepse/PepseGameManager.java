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
import java.util.List;

/**
 * Main game manager that coordinates and initializes all simulation subsystems in the
 * PEPSE simulation. This class extends GameManager.
 */
public class PepseGameManager extends GameManager {

    // ~~~~~~~~~~~~~~~~~~
    //  PUBLIC CONSTANTS
    // ~~~~~~~~~~~~~~~~~~

    /** Total duration in seconds of a single full day-night cycle. */
    public static final float DAY_CYCLE = 30.0f;

    // GAME TAGS
    /** Identification tag for the sky background object. */
    public static final String SKY_TAG = "sky";

    /** Identification tag for the avatar character. */
    public static final String AVATAR_TAG = "avatar";

    /** Identification tag for top-surface ground blocks. */
    public static final String GROUND_SURFACE_TAG = "surface_ground";

    /** Identification tag for deep/subsurface ground blocks. */
    public static final String GROUND_INNER_TAG = "inner_ground";

    /** Identification tag for tree trunk blocks. */
    public static final String TRUNK_TAG = "trunk";

    /** Identification tag for foliage/leaf blocks. */
    public static final String LEAF_TAG = "leaf";

    /** Identification tag for fruit objects. */
    public static final String FRUIT_TAG = "fruit";

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //  PRIVATE MANAGING CONSTANTS
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private static final float WINDOW_WIDTH = 1200f;
    private static final float WINDOW_HEIGHT = 690f;
    private static final int SEED = 0;
    private static final int TERRAIN_RANGE_RIGHT = 2000;
    private static final int TERRAIN_RANGE_LEFT = -1000;
    private static final int HALO_LAYER = Layer.BACKGROUND + 5;
    private static final int SUN_LAYER = Layer.BACKGROUND + 10;

    // ~~~~~~~~~~~~~~~~~~~~~~~
    //  PRIVATE GAME OBJECTS
    // ~~~~~~~~~~~~~~~~~~~~~~~
    private static GameObject sky;
    private static GameObject sun;
    private static GameObject sunHalo;
    private static GameObject night;
    private static Terrain terrain;
    private static Avatar avatar;
    private static Flora flora;

    /**
     * Constructs a new PepseGameManager instance with default window dimensions.
     */
    public PepseGameManager() {
        super("Pepse", new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT));
    }

    /**
     * Initializes the simulation world and its game objects.
     * Creates and adds all components across layers:
     *   BACKGROUND: Sky background.
     *   BACKGROUND sublayers: Sun halo and Sun.
     *   STATIC_OBJECTS: Terrain blocks, Tree trunks, Tree leaves and Fruits.
     *   DEFAULT: Avatar.
     *   FOREGROUND: Night darkness overlay.
     *   UI: Energy user interface indicator.
     *
     * Also configures camera tracking to follow the avatar.
     *
     * @param imageReader      utility to read image assets from disk.
     * @param soundReader      utility to read audio assets from disk.
     * @param inputListener    listener for user keyboard inputs.
     * @param windowController controller for querying window properties and controlling the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();

        createSky(windowDimensions);
        createTerrain(windowDimensions);
        createAvatar(windowDimensions, inputListener, imageReader);
        createFlora();
    }

    /**
     * The main entry point for running the PEPSE simulation.
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~
    //  PRIVATE HELPER FUNCTIONS
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void createSky(Vector2 windowDimensions) {
        // Create Sky
        this.sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);

        // Create Sun & SunHalo
        this.sun = Sun.create(windowDimensions, DAY_CYCLE);
        this.sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, HALO_LAYER);
        gameObjects().addGameObject(sun, SUN_LAYER);

        // Create Night Overlay
        this.night = Night.create(windowDimensions, DAY_CYCLE);
        gameObjects().addGameObject(night, Layer.FOREGROUND);

    }

    private void createTerrain(Vector2 windowDimensions) {
        this.terrain = new Terrain(windowDimensions, SEED);
        List<GameObject> blocks = terrain.createInRange(TERRAIN_RANGE_LEFT, TERRAIN_RANGE_RIGHT);
        for (GameObject obj : blocks) {
            gameObjects().addGameObject(obj, Layer.STATIC_OBJECTS);
        }
    }

    private void createAvatar(Vector2 windowDimensions,
                              UserInputListener inputListener,
                              ImageReader imageReader) {
        // Create Avatar
        float startX = windowDimensions.x() * 0.5f;
        float groundY = terrain.groundHeightAt(startX);
        Vector2 initialAvatarLocation = new Vector2(startX, groundY - Avatar.AVATAR_HEIGHT);
        this.avatar = new Avatar(initialAvatarLocation, inputListener,
                imageReader, terrain::groundHeightAt);
        gameObjects().addGameObject(avatar, Layer.DEFAULT);

        // Create Energy UI Display
        EnergyUI energyUI = new EnergyUI(avatar::getEnergy);
        gameObjects().addGameObject(energyUI, Layer.UI);

        // Set Camera Tracking with initial offset
        Vector2 cameraOffset = windowDimensions.mult(0.5f).subtract(initialAvatarLocation);
        setCamera(new Camera(
                avatar,
                cameraOffset,
                windowDimensions,
                windowDimensions
        ));
    }

    private void createFlora() {
        this.flora = new Flora(terrain::groundHeightAt, avatar::updateEnergy);
        List<GameObject> treeParts = flora.createInRange(TERRAIN_RANGE_LEFT, TERRAIN_RANGE_RIGHT);

        for (GameObject part : treeParts) {
            gameObjects().addGameObject(part, Layer.STATIC_OBJECTS);
        }
    }
}