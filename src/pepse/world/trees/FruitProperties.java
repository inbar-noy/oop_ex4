package pepse.world.trees;

import danogl.util.Vector2;
import pepse.utils.ColorSupplier;
import pepse.world.Block;

import java.awt.Color;
import java.util.Random;

/**
 * Properties of a Fruit.
 * Constructed via its nested static Builder.
 */
public class FruitProperties {
    // ordinary fruit properties
    public static final Color BASE_COLOR = Color.RED;
    public static final int BASE_SIZE = Block.SIZE;
    public static final int BASE_ENERGY = 20;

    // large fruit properties
    private static final float LARGE_SIZE_MULTIPLIER = 1.5f;
    private static final int LARGE_ENERGY_BONUS = 10;

    // golden fruit properties
    private static final float GOLD_BLEND_RATIO = 0.8f;
    private static final int GOLDEN_ENERGY_BONUS = 2;

    // rotten fruit properties
    private static final int ROTTEN_ENERGY_PENALTY = 30;

    private final Color color;
    private final Vector2 dimensions;
    private final int energyGain;

    private FruitProperties(Color color, Vector2 dimensions, int energyGain) {
        this.color = color;
        this.dimensions = dimensions;
        this.energyGain = energyGain;
    }

    public Color getColor() { return color; }

    public Vector2 getDimensions() { return dimensions; }

    public int getEnergyGain() { return energyGain; }

    /**
     * Builder class for constructing configured FruitProperties instances.
     */
    public static class Builder {
        private boolean isLarge = false;
        private boolean isGolden = false;
        private boolean isRotten = false;

        public Builder setLarge(boolean isLarge) {
            this.isLarge = isLarge;
            return this;
        }

        public Builder setGolden(boolean isGolden) {
            this.isGolden = isGolden;
            return this;
        }

        public Builder setRotten(boolean isRotten) {
            this.isRotten = isRotten;
            return this;
        }

        /**
         * Helper to randomize attributes:
         * Large = 1/3
         * Golden = 1/4
         * Rotten 1/5
         * @param rand seeded random instance.
         * @return this builder.
         */
        public Builder randomizeAttributes(Random rand) {
            this.isLarge = rand.nextInt(3) == 0;
            this.isGolden = rand.nextInt(4) == 0;
            this.isRotten = rand.nextInt(5) == 0;
            return this;
        }

        /**
         * Builds and returns a FruitProperties instance.
         */
        public FruitProperties build() {
            Color calculatedColor = BASE_COLOR;
            float calculatedSize = BASE_SIZE;
            int calculatedEnergy = BASE_ENERGY;

            if (isLarge) {
                calculatedSize *= LARGE_SIZE_MULTIPLIER;
                calculatedEnergy += LARGE_ENERGY_BONUS;
            }

            if (isGolden) {
                calculatedColor = ColorSupplier.blendGold(calculatedColor, GOLD_BLEND_RATIO);
                calculatedEnergy *= GOLDEN_ENERGY_BONUS;
            }

            if (isRotten) {
                calculatedColor = calculatedColor.darker().darker().darker();
                calculatedEnergy -= ROTTEN_ENERGY_PENALTY;
            }

            return new FruitProperties(
                    calculatedColor,
                    Vector2.ONES.mult(calculatedSize),
                    calculatedEnergy
            );
        }
    }
}