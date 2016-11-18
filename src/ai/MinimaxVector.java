package ai;

public class MinimaxVector  {
    public double[] Scores;

    public MinimaxVector() {
        Scores = new double[4];
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MinimaxVector) {
            MinimaxVector v = (MinimaxVector) o;
            boolean equal = v.Scores.length == Scores.length;
            for (int i = 0; i < Scores.length && equal; i++) {
                equal = equal && v.Scores[i] == Scores[i];
            }
            return equal;
        }
        return false;
    }
}
