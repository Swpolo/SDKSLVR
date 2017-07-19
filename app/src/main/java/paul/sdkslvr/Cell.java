package paul.sdkslvr;

class Cell {
    private int value;
    private int[] prob;

    Cell()
    {
        init();
    }

    private void init() {
        prob = new int[10];
        reset();
    }

    void reset()
    {
        value = 0;
        for(int i = 1; i <= 9; i++)
        {
            prob[i] = 1;
        }
        prob[0] = 9;
    }

    int getProb(int index)
    {
        return prob[index];
    }

    void removePossibleValue(int index)
    {
        prob[index] = 0;
        calculateProb();
    }

    void setValue(int index)
    {
        value = index;
        if (index == 0) {
            reset();
        }
        else {
            for (int i = 0; i <= 9; i++) {
                prob[i] = 0;
            }
        }
    }

    int getValue() {
        return value;
    }

    private void calculateProb(){
        int nProb = 0;
        for(int i = 1; i <= 9; i++) {
            if(prob[i] != 0) {
                nProb++;
            }
        }
        prob[0] = nProb;
    }
}
