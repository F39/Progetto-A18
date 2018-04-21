package ArtificialAgent;

import GameLogic.Connect4Game;

import java.util.ArrayList;

public class ArtificialLev2 extends ArtificialLev1 {
    ArrayList<Integer> saveAvoid;
    public ArtificialLev2(Connect4Game game) {
        super(game);
        saveAvoid=new ArrayList<>();
    }

    @Override
    public int getInTheWay() {
        int col1=super.getInTheWay();
        if(col1==-1){
            saveAvoid.clear();
            saveAvoid.addAll(avoid);
            int col2= superScan(mirror.getLastR(), mirror.getLastC(), 1, 2);
            avoid.clear();
            avoid.addAll(saveAvoid);
            if(avoid.contains(col2))
                return -1;
            return col2;
        }
        else
            return col1;
    }
}