class DefaultScoringStrategy implements ScoringStrategy {
    @Override
    public String getScoreSymbol(int[] bowlerScores, int ballIndex) {
        return Integer.toString(bowlerScores[ballIndex]);
    }
}
