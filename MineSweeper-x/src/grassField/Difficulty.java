package grassField;

class Difficulty {
    private double mineProbability = 0;

    public double getMineProbability() {
        return mineProbability;
    }

    public void setMineProbability(final double mineProbability) {
        if (mineProbability < 0 || mineProbability > 0.90) {
            throw new IllegalArgumentException("Mine probability must be between 0 and 0.90.");
        }
        this.mineProbability = mineProbability;
    }

    public Difficulty(double mineProbability) {
        this.setMineProbability(mineProbability);
    }

    public static Difficulty getEasy() {
        return new Difficulty(0.12);
    }

    public static Difficulty getMedium() {
        return new Difficulty(0.15);
    }

    public static Difficulty getHard() {
        return new Difficulty(0.17);
    }

}
