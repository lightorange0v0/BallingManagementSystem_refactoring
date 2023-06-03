class SpareScoringStrategy implements ScoringStrategy {
    @Override
    public String getScoreSymbol(int[] bowlerScores, int ballIndex) {
        return (ballIndex > 0 && bowlerScores[ballIndex] + bowlerScores[ballIndex - 1] == 10 && ballIndex % 2 == 1) ? "/" : null;
    }
}
