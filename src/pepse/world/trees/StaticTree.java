package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Represents a complete tree structure composed of a trunk and a treetop containing the leaves
 * and fruits.
 */
public class StaticTree {

    private static final Color BROWN = new Color(100, 50, 20);
    private static final int MEASURE_UNIT = Block.SIZE;
    private static final int TREE_EDGE = 8 * MEASURE_UNIT;
    private static final int TREE_RADIUS = TREE_EDGE / 2;
    private final ArrayList<GameObject> treeObjects = new ArrayList<>();

    /**
     * Constructs a complete StaticTree at the specified location.
     * Initializes the trunk with the given height and creates a centered treetop
     * populated with leaves and fruits generated according to the provided random instance.
     * @param topTrunkLeftCorner the top-left coordinate of the tree trunk.
     * @param trunkHeight        the height of the trunk expressed in number of blocks.
     * @param rand               the Random instance used for treetop generation.
     * @param energyCallback     a callback to modify avatar energy when fruits are consumed.
     */
    public StaticTree(Vector2 topTrunkLeftCorner, int trunkHeight, Random rand,
                      Consumer<Integer> energyCallback) {

        // TRUNK CONSTRUCTION
        Vector2 trunkDims = new Vector2(MEASURE_UNIT, MEASURE_UNIT * trunkHeight);
        RectangleRenderable trunkRenderable = new RectangleRenderable(BROWN);
        TreeTrunk treeTrunk = new TreeTrunk(topTrunkLeftCorner, trunkDims, trunkRenderable);

        // TREETOP CONSTRUCTION
        int treetopX = (int) (topTrunkLeftCorner.x() - TREE_RADIUS + (MEASURE_UNIT / 2f));
        int treetopY = (int) (topTrunkLeftCorner.y() - (TREE_RADIUS * 1.5));
        Vector2 topTreetop = new Vector2(treetopX, treetopY);
        Treetop treetop = new Treetop(topTreetop, rand, energyCallback);
        ArrayList<GameObject> leaves = treetop.getLeaves();

        // ADDITION TO TREE OBJECTS
        treeObjects.add(treeTrunk);
        treeObjects.addAll(leaves);
    }

    /**
     * Returns the list of all GameObject instances (trunk, leaves, fruits) that compose this tree.
     * @return an ArrayList of game objects belonging to this tree.
     */
    public ArrayList<GameObject> getTree() { return this.treeObjects; }

}
