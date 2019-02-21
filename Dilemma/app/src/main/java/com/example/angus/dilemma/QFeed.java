package com.example.angus.dilemma;

class QFeed {

    Stack sFirst, sSecond;
    //profile / userID
    static int index;

    //will take in user profile so that appropriate questions are shown
    //eg. dont show own q, don't show answered, filter by tags etc.
    public QFeed(/*int userID*/){
        sFirst = new Stack();
        sSecond = new Stack();
        index = 0;
        populate();
        populate();
    }

    private void populate(){

        sFirst = sSecond;
        sSecond = new Stack();
        while(!sSecond.isFull())
            sSecond.push(pull());
        //sFirst.pointer=0;

    }

    public Question next(){

        Question q = sFirst.pop();
        if (q != null) return q;
        else{
            populate();
            return next();
        }
    }

    private Question pull(){
        //creates Question object from DB

        //filler code until db created
        index++;

        //replace code with query on each line after comment
        return new Question(

                //Question ID
                index,

                //Question Text
                "Question no."+index,

                //Left text
                "Left no."+index,

                //Right Text
                "Right no."+index
        );

    }


    //TO:DO add catagory filtered

}
