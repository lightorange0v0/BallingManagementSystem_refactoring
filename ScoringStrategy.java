interface ScoringStrategy { // 심볼 표시와 점수 계산 인터페이스
    String getScoreSymbol(int[] bowlerScores, int ballIndex);

    void computeScore(int[] curScore, int[][] cumulScores, int bowlIndex, int current, int i);
}
