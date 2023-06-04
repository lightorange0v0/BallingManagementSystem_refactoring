class DefaultScoringStrategy implements ScoringStrategy {
    @Override
    public String getScoreSymbol(int[] bowlerScores, int ballIndex) {
        return Integer.toString(bowlerScores[ballIndex]);
    }
    @Override
    public void computeScore(int[] curScore, int[][] cumulScores, int bowlIndex, int current, int i) {
        //We're dealing with a normal throw, add it and be on our way.
        if( i%2 == 0 && i < 18){
            if ( i/2 == 0 ) {
                //First frame, first ball.  Set his cumul score to the first ball
                if(curScore[i] != -2){
                    cumulScores[bowlIndex][i/2] += curScore[i];
                }
            } else if (i/2 != 9){
                //add his last frame's cumul to this ball, make it this frame's cumul.
                if(curScore[i] != -2){
                    cumulScores[bowlIndex][i/2] += cumulScores[bowlIndex][i/2 - 1] + curScore[i];
                } else {
                    cumulScores[bowlIndex][i/2] += cumulScores[bowlIndex][i/2 - 1];
                }
            }
        } else if (i < 18){
            if(curScore[i] != -1 && i > 2){
                if(curScore[i] != -2){
                    cumulScores[bowlIndex][i/2] += curScore[i];
                }
            }
        }
        if (i/2 == 9){
            if (i == 18){
                cumulScores[bowlIndex][9] += cumulScores[bowlIndex][8];
            }
            if(curScore[i] != -2){
                cumulScores[bowlIndex][9] += curScore[i];
            }
        } else if (i/2 == 10) {
            if(curScore[i] != -2){
                cumulScores[bowlIndex][9] += curScore[i];
            }
        }
    }

}
