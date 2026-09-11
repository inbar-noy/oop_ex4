package pepse.world.avatar;

/**
 * Observer interface for the avatar's location
 */
public interface AvatarLocationObserver {
    /**
     * Update the observer of the new avatar location
     * @param x X coordinate of the avatar
     */
    void updateAvatarLocation(float x);
}
