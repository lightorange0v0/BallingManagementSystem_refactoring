class StrikeScoringStrategy implements ScoringStrategy {
    @Override
    public String getScoreSymbol(int[] bowlerScores, int ballIndex) {
        return (bowlerScores[ballIndex] == 10 && (ballIndex % 2 == 0 || ballIndex == 19)) ? "X" : null;
    }


    @Override
    public void computeScore(int[] curScore, int[][] cumulScores, int bowlIndex, int current, int i) {
        //your original Strike related logic here.
        int strikeballs = 0;
        //This ball is the first ball, and was a strike.
        //If we can get 2 balls after it, good add them to cumul.
        if (curScore[i+2] != -1) {
            strikeballs = 1;
            if(curScore[i+3] != -1) {
                //Still got em.
                strikeballs = 2;
            } else if(curScore[i+4] != -1) {
                //Ok, got it.
                strikeballs = 2;
            }
        }
        if (strikeballs == 2){
            //Add up the strike.
            //Add the next two balls to the current cumulscore.
            cumulScores[bowlIndex][i/2] += 10;
            if(curScore[i+1] != -1) {
                cumulScores[bowlIndex][i/2] += curScore[i+1] + cumulScores[bowlIndex][(i/2)-1];
                if (curScore[i+2] != -1){
                    if( curScore[i+2] != -2){
                        cumulScores[bowlIndex][(i/2)] += curScore[i+2];
                    }
                } else {
                    if( curScore[i+3] != -2){
                        cumulScores[bowlIndex][(i/2)] += curScore[i+3];
                    }
                }
            } else {
                if ( i/2 > 0 ){
                    cumulScores[bowlIndex][i/2] += curScore[i+2] + cumulScores[bowlIndex][(i/2)-1];
                } else {
                    cumulScores[bowlIndex][i/2] += curScore[i+2];
                }
                if (curScore[i+3] != -1){
                    if( curScore[i+3] != -2){
                        cumulScores[bowlIndex][(i/2)] += curScore[i+3];
                    }
                } else {
                    cumulScores[bowlIndex][(i/2)] += curScore[i+4];
                }
            }
        }
    }

}
