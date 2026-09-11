package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;

import java.awt.*;

/**
 * Creates the sky
 */
public class Sky {
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");

    /**
     * Create the sky
     * @param windowDimensions Dimensions of the window
     * @return Sky GameObject
     */
    public static GameObject create(Vector2 windowDimensions) {
        GameObject sky = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(PepseGameManager.SKY_TAG);
        return sky;
    }
}
