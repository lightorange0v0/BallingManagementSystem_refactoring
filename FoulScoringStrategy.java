class FoulScoringStrategy implements ScoringStrategy {
    @Override
    public String getScoreSymbol(int[] bowlerScores, int ballIndex) {
        return (bowlerScores[ballIndex] == -2) ? "F" : null;
    }

    @Override
    public void computeScore(int[] curScore, int[][] cumulScores, int bowlIndex, int current, int i) {
        // foul은 점수 계산에 영향이 없음
    }
}
