package pepse.world.avatar;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.util.Vector2;
import pepse.PepseGameManager;

import java.awt.*;

public class Avatar extends GameObject {
    protected static final float VELOCITY_X = 400;
    protected static final float VELOCITY_Y = -650;

    private static final float GRAVITY = 600;
    private static final Vector2 AVATAR_DIMENSIONS = Vector2.ONES.mult(50);
    private static final int ENERGY_MIN = 0;
    private static final int ENERGY_MAX = 100;

    private UserInputListener inputListener;
    private final AvatarAnimation avatarAnimation;
    private int energy;
    private AvatarState curState;

    // ~~~~~~~~~~~~~~
    //   CONSTRUCTOR
    // ~~~~~~~~~~~~~~
    /**
     * Constructs a new Avatar instance.
     * @param topLeftCorner the starting top-left position.
     * @param inputListener listener for keyboard inputs.
     * @param imageReader   image reader.
     */
    public Avatar(Vector2 topLeftCorner,
                  UserInputListener inputListener,
                  ImageReader imageReader) {
        super(topLeftCorner, AVATAR_DIMENSIONS, null);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);

        this.inputListener = inputListener;
        this.avatarAnimation = new AvatarAnimation(imageReader);
        this.energy = ENERGY_MAX;
        this.setTag(PepseGameManager.AVATAR_TAG);

        // Initialize with the default (idle) state
        changeState(new IdleState());
    }

    // ~~~~~~~~~~~
    //   GETTERS
    // ~~~~~~~~~~~
    /**
     * Returns the player's current energy.
     */
    public int getEnergy() { return energy; }

    /**
     * Returns the user input listener.
     */
    public UserInputListener getInputListener() { return inputListener; }

    /**
     * Returns the avatar's animation controller.
     */
    public AvatarAnimation getAvatarAnimation() { return avatarAnimation; }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~
    //   CLASS FUNCTIONALITIES
    // ~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Updates the avatar's energy, clamping between MIN and MAX.
     * @param num the amount of energy to add (if positive) or subtract (if negative).
     */
    public void updateEnergy(int num) {
        this.energy = Math.max(ENERGY_MIN, Math.min(ENERGY_MAX, this.energy + num));
    }

    /**
     * Changes the current state of the avatar.
     * @param newState the new state to transition to.
     */
    public void changeState(AvatarState newState) {
        if(curState != null) {
            curState.exit(this);
        }
        curState = newState;
        curState.enter(this);
    }

    private boolean isSurfaceObj(GameObject other) {
        return other.getTag().equals(PepseGameManager.GROUND_TAG) ||
                other.getTag().equals(PepseGameManager.TRUNK_TAG);
    }


    // ~~~~~~~~~~~~~
    //   OVERRIDES
    // ~~~~~~~~~~~~~
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        AvatarState nextState = curState.tick(this);
        if(nextState != null && nextState != curState) {
            changeState(nextState);
        }
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        if(getVelocity().y() > 0 && isSurfaceObj(other)) {
            this.transform().setVelocityY(0);
        }

    }

}
