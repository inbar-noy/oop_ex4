package pepse;

import java.awt.*;

import static pepse.world.Sky.sky;

public class PepseGameManager {

    public static final String GROUND_TAG = "ground";
    public static final String TRUNK_TAG = "trunk";
    public static final String LEAF_TAG = "leaf";

    public static final Color BROWN = new Color(100, 50, 20);
    public static final Color GREEN = new Color(50, 200, 30);

    public static void main(String[] args) {
        System.out.println("Pepse");
        sky();
    }
}
