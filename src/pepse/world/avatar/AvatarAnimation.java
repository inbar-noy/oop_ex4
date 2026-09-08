package pepse.world.avatar;

import danogl.gui.ImageReader;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;

/**
 * Manages the animations and horizontal orientation for the Avatar class.
 * This class loads and stores the cyclic AnimationRenderable objects for the
 * different avatar states (idle, jump, and run) and provides helper methods to flip
 * the avatar's rendering horizontally depending on movement direction.
 */
public class AvatarAnimation {

    /** Duration in seconds that each animation frame is displayed. */
    private static final double TIME_PER_FRAME = 0.2;

    /** File paths for the idle state animation frames. */
    private static final String[] IDLE_IMAGE_PATHS = {
            "assets/idle_0.png", "assets/idle_1.png",
            "assets/idle_2.png", "assets/idle_3.png"
    };

    /** File paths for the jump/airborne state animation frames. */
    private static final String[] JUMP_IMAGE_PATHS = {
            "assets/jump_0.png", "assets/jump_1.png",
            "assets/jump_2.png", "assets/jump_3.png"
    };

    /** File paths for the running state animation frames. */
    private static final String[] RUN_IMAGE_PATHS = {
            "assets/run_0.png", "assets/run_1.png",
            "assets/run_2.png", "assets/run_3.png",
            "assets/run_4.png", "assets/run_5.png"
    };

    private final AnimationRenderable idleAnimation;
    private final AnimationRenderable jumpAnimation;
    private final AnimationRenderable runAnimation;

    /**
     * Constructs an AvatarAnimation instance and preloads all animation sequences.
     * @param imageReader the ImageReader used to read asset image files.
     */
    public AvatarAnimation(ImageReader imageReader) {
        this.idleAnimation = new AnimationRenderable(
                IDLE_IMAGE_PATHS, imageReader, true, TIME_PER_FRAME
        );
        this.jumpAnimation = new AnimationRenderable(
                JUMP_IMAGE_PATHS, imageReader, true, TIME_PER_FRAME
        );
        this.runAnimation = new AnimationRenderable(
                RUN_IMAGE_PATHS, imageReader, true, TIME_PER_FRAME
        );
    }

    /**
     * Returns the renderable animation for the idle state.
     * @return Renderable representing the idle animation.
     */
    public Renderable getIdleAnimation() {
        return idleAnimation;
    }

    /**
     * Returns the renderable animation for the jumping/airborne state.
     * @return Renderable representing the jump animation.
     */
    public Renderable getJumpAnimation() {
        return jumpAnimation;
    }

    /**
     * Returns the renderable animation for the running state.
     * @return Renderable representing the run animation.
     */
    public Renderable getRunAnimation() {
        return runAnimation;
    }

    /**
     * Configures the avatar's renderer to face left by flipping the image horizontally.
     * @param avatar the Avatar instance to orient.
     */
    public void faceLeft(Avatar avatar) {
        avatar.renderer().setIsFlippedHorizontally(true);
    }

    /**
     * Configures the avatar's renderer to face right by restoring default horizontal orientation.
     * @param avatar the Avatar instance to orient.
     */
    public void faceRight(Avatar avatar) {
        avatar.renderer().setIsFlippedHorizontally(false);
    }
}