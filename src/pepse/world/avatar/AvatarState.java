package pepse.world.avatar;

/**
 * Represents a state in the State Pattern lifecycle for the Avatar.
 * This interface also encapsulates shared constants related to energy costs, gains, thresholds,
 * and standard state velocities.
 */
public interface AvatarState {

    /** Horizontal velocity when the avatar is stationary. */
    int IDLE_VELOCITY = 0;

    /** Energy gained per frame/update while idle on the ground. */
    int IDLE_ENERGY_GAIN = 1;

    /** Energy consumed per frame/update while running on the ground. */
    int RUN_ENERGY_GAIN = -2;

    /** One-time energy cost deducted when initiating a regular jump. */
    int JUMP_ENERGY_GAIN = -20;

    /** One-time energy cost deducted when performing an airborne double jump. */
    int DOUBLE_JUMP_ENERGY_GAIN = -50;

    /** Minimum energy threshold required to perform a standard ground jump. */
    int JUMP_ENERGY_DEMAND = 20;

    /** Minimum energy threshold required to perform a double jump. */
    int DOUBLE_JUMP_ENERGY_DEMAND = 50;

    /** Downward vertical velocity threshold (positive Y) considered as active falling. */
    float FALL_THRESHOLD = 50f;

    /** Minimum energy required to initiate or sustain running. */
    int MIN_ENERGY_TO_RUN = 5;

    /**
     * Executes actions upon entering this state (e.g., setting animations, applying impulses).
     * @param avatar the Avatar transitioning into this state.
     */
    void enter(Avatar avatar);

    /**
     * Updates the avatar's logic for this state on each frame and determines state transitions.
     * @param avatar the Avatar being updated.
     * @return the next AvatarState to transition into, or this if remaining in the current state.
     */
    AvatarState tick(Avatar avatar);

    /**
     * Executes cleanup or exit logic when leaving this state.
     * @param avatar the Avatar transitioning out of this state.
     */
    void exit(Avatar avatar);
}