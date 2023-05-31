class StrikeScoringStrategy implements ScoringStrategy {
    @Override
    public String getScoreSymbol(int[] bowlerScores, int ballIndex) {
        return (bowlerScores[ballIndex] == 10 && (ballIndex % 2 == 0 || ballIndex == 19)) ? "X" : null;
    }
}
