class SpareScoringStrategy implements ScoringStrategy {
    @Override
    public String getScoreSymbol(int[] bowlerScores, int ballIndex) {
        return (ballIndex > 0 && bowlerScores[ballIndex] + bowlerScores[ballIndex - 1] == 10 && ballIndex % 2 == 1) ? "/" : null;
    }

    @Override
    public void computeScore(int[] curScore, int[][] cumulScores, int bowlIndex, int current, int i) {
        //        This ball was a the second of a spare.
        //Also, we're not on the current ball.
        //Add the next ball to the ith one in cumul.
        cumulScores[bowlIndex][i/2] += curScore[i+1] + curScore[i];
        if (i > 1) {
            //cumulScores[bowlIndex][i/2] += cumulScores[bowlIndex][i/2 -1];
        }
    }
}
