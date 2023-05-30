class FoulScoringStrategy implements ScoringStrategy {
    @Override
    public String getScoreSymbol(int[] bowlerScores, int ballIndex) {
        return (bowlerScores[ballIndex] == -2) ? "F" : null;
    }
}
