package paul.sdkslvr;

import java.util.ArrayList;

import static paul.sdkslvr.MainActivity.sdkGrid;


class SdkCopy {

    private ArrayList<Grid> storage;
    private ArrayList<Integer> stoInt;

    SdkCopy(){
        storage = new ArrayList<>();
        stoInt = new ArrayList<>();
    }

    int sdkAttempt() {
        int returnCode = 0;

        // there is a guessable cell
        if(findImpossibleCell() == 0){
            int[] possibleCell;
            possibleCell = findAvailableCell();
            possibleCell[2] = findPossibleIndex(possibleCell[0], possibleCell[1]);
            storeSdk(possibleCell[0], possibleCell[1], possibleCell[2]);
        }
        // there is no guessable cell
        else if (storage.size() > 0){
            loadSdk();
        }
        else {
            returnCode = 1;
        }
        return returnCode;
    }

    private int findImpossibleCell(){
        int impossibleCell = 0;
        for(int c = 0; c < 9; c++){
            for(int r = 0; r < 9; r++){
                if((sdkGrid.getCell(c,r) == 0) && (sdkGrid.getProb(c,r,0) == 0)) {
                    impossibleCell = 1;
                }
            }
        }
        return impossibleCell;
    }

    private int[] findAvailableCell() {
        int[] availableCell = {0,0,10};
        for(int c = 0; c < 9; c++){
            for(int r = 0; r < 9; r++){
                if(sdkGrid.getCell(c, r) == 0 && sdkGrid.getProb(c,r,0) < availableCell[2]){
                    availableCell[0] = c;
                    availableCell[1] = r;
                    availableCell[2] = sdkGrid.getProb(c,r,0);
                }
            }
        }
        return availableCell;
    }

    private int findPossibleIndex(int column, int row) {
        int index = 0;
        for(int i = 1; i < 9; i++){
            if(sdkGrid.getProb(column, row, i) != 0) {
                index = i;
                i = 10;
            }
        }
        return index;
    }

    private void storeSdk(int column, int row, int value) {
        sdkGrid.removeProb(column, row, value);
        storage.add(new Grid(sdkGrid));
        sdkGrid.setCell(column, row, value);
        stoInt.add(storage.size());
    }

    private void loadSdk() {
        sdkGrid = storage.get(storage.size()-1);
        storage.remove(storage.size() - 1);
    }
}
