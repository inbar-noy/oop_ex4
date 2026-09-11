package pepse.world.avatar;

import java.awt.event.KeyEvent;

/**
 * Represents the state when the avatar is running.
 */
public class RunState implements AvatarState {

    @Override
    public void enter(Avatar avatar) {
        avatar.renderer().setRenderable(avatar.getAvatarAnimation().getRunAnimation());
        avatar.snapToSurface();
    }

    @Override
    public AvatarState tick(Avatar avatar) {
        boolean left = avatar.getInputListener().isKeyPressed(KeyEvent.VK_LEFT);
        boolean right = avatar.getInputListener().isKeyPressed(KeyEvent.VK_RIGHT);
        boolean space = avatar.getInputListener().isKeyPressed(KeyEvent.VK_SPACE);

        // IN AIR CASE:
        if(!avatar.isOnSurface() && avatar.getVelocity().y() > FALL_THRESHOLD) {
            return new JumpState(false);
        }

        // ON THE GROUND CASES:
        // jump initiated mid-running:
        if(space && avatar.getEnergy() >= JUMP_ENERGY_DEMAND) {
            return new JumpState(true);
        }

        // stopped running case:
        if(!(left ^ right) || avatar.getEnergy() < MIN_ENERGY_TO_RUN) {
            return new IdleState();
        }

        // DEFAULT CASE:
        // update animation if needed and consume energy:
        if(left) {
            avatar.transform().setVelocityX(-Avatar.VELOCITY_X);
            avatar.getAvatarAnimation().faceLeft(avatar);
        }
        else if(right) {
            avatar.transform().setVelocityX(Avatar.VELOCITY_X);
            avatar.getAvatarAnimation().faceRight(avatar);
        }
        avatar.updateEnergy(RUN_ENERGY_GAIN);
        return this;
    }

    @Override
    public void exit(Avatar avatar) {}
}
