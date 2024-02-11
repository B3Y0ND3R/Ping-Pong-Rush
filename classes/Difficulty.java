package gameV;

public enum Difficulty {
    EASY(1, 6),     
    MEDIUM(2, 8),
    HARD(3, 12);

    private final int speedMultiplier;
    private final int maxBallSpeed;

    Difficulty(int speedMultiplier, int maxBallSpeed) {
        this.speedMultiplier = speedMultiplier;
        this.maxBallSpeed = maxBallSpeed;
    }

    public int getSpeedMultiplier() {
        return speedMultiplier;
    }

    public int getMaxBallSpeed() {
        return maxBallSpeed;
    }
}
