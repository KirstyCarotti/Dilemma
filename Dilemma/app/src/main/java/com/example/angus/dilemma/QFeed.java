package com.example.angus.dilemma;

class QFeed {

    private int index;
    private Question[] qList;
    //profile / userID

    //will take in user profile so that appropriate questions are shown
    //eg. dont show own q, don't show answered, filter by tags etc.
    public QFeed(/*int userID*/){
        //testPop();
        //populate();
    }

    private void populate(){
        //add 10 questions to qList
        //remaining questions to front
        //reset index to 0
        //call "pull()" method until qList.length == 10
    }

    public Question next(){
        //get next question from qList
        // if(i>=8) populate();
        //return thing;
       /* Question q = qList[index];
        if (index>=8){
            testPop();
        }else index++;

        return q;*/
       return new Question(0, "Text from QFeed", "Left Answer ", "Right Answer ");
    }

    private /*Question*/ void pull(){
        //creates Question object from DB
    }

    public void testPop(){
        for (int i=0; i<=10;i++){
            //qList[i]=new Question(i,"Question "+i,"Left "+i,"Right "+i);
            //TO:DO fix
        }
        index = 0;
    }

    //TO:DO add catagory filtered

}
