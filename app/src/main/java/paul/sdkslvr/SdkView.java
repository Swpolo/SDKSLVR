package paul.sdkslvr;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import static android.content.ContentValues.TAG;

public class SdkView extends View {
    private int focusedColumn;
    private int focusedRow;

    private int[][] cellValue = new int[9][9];
    private int[][] cellUserSet = new int[9][9];

    // visual
    private int squareSize;

    private int gridSize_r;
    int cellSize_r = 27;
    int outBorderSize_r = 2;
    int inBorderSize_r = 1;

    private int gridSize;
    int cellSize;
    int outBorderSize;
    int inBorderSize;

    boolean f_inflated;
    boolean f_init;

    Paint paint = new Paint();

    public SdkView(Context context) {
        super(context);
    }

    public SdkView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public SdkView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        if(!f_init){
            init();
        }
    }

    private void init() {
        gridSize_r = 9 * cellSize_r + 2 * outBorderSize_r + 8 * inBorderSize_r;

        int widthMode = this.getLayoutParams().width;
        int width  = this.getMeasuredWidth();
        int heightMode = this.getLayoutParams().height;
        int height = this.getMeasuredHeight();

        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) this.getLayoutParams();

        if(widthMode == ViewGroup.LayoutParams.MATCH_PARENT){
            squareSize = width;
        }
        else if(heightMode == ViewGroup.LayoutParams.MATCH_PARENT) {
            squareSize = height;
        }
        else if(widthMode == ViewGroup.LayoutParams.WRAP_CONTENT && heightMode == ViewGroup.LayoutParams.WRAP_CONTENT) {
            // TODO : Add margin and padding;
            squareSize = gridSize_r;
        }
        else if(widthMode != ViewGroup.LayoutParams.WRAP_CONTENT && heightMode != ViewGroup.LayoutParams.WRAP_CONTENT) {
            squareSize = (width < height ? width : height);
        }
        else if(widthMode != ViewGroup.LayoutParams.WRAP_CONTENT) {
            squareSize = width;
        }
        else /* if(heightMode != View.Group.LayoutParams.WRAP_CONTENT) */ {
            squareSize = height;
        }

        if(squareSize == height) {
            gridSize = squareSize - mlp.topMargin - mlp.bottomMargin - getPaddingTop() - getPaddingBottom();
        }
        else if(squareSize == width) {
            gridSize = squareSize - mlp.leftMargin - mlp.rightMargin - getPaddingLeft() - getPaddingRight();
        }
        else {
            gridSize = squareSize;
        }

        if(gridSize < this.getSuggestedMinimumWidth()) {
            gridSize = this.getSuggestedMinimumWidth();
        }

        float gridRatio = (float)gridSize / (float)gridSize_r;
        cellSize = Math.round((float)cellSize_r * gridRatio);
        outBorderSize = Math.round((float)outBorderSize_r * gridRatio);
        inBorderSize = Math.round((float)inBorderSize_r * gridRatio);

        int gridTotal = cellSize * 9 + outBorderSize * 2 + inBorderSize * 8;
        while(gridSize < gridTotal){
                cellSize--;
            gridTotal = cellSize * 9 + outBorderSize * 2 + inBorderSize * 8;
        }
        gridSize = gridTotal;

        ViewGroup.LayoutParams lp = this.getLayoutParams();

        lp.width = lp.height = gridSize;

        requestLayout();

        reset();
        f_init = true;
    }

    public void reset()
    {
        for(int c = 0; c < 9; c++){
            for(int r = 0; r < 9; r++){
                cellValue[c][r]     = 0;
                cellUserSet[c][r]   = 0;
            }
        }
        focusedColumn = 0;
        focusedRow = 0;
    }

    @Override
    public void onDraw(Canvas canvas) {
        int curRow = 0;

        if(f_inflated && !f_init){
            init();
        }
        f_inflated = true;

        // DRAW 9x9 tiny cells
        paint.setColor(Color.LTGRAY);
        for(int i = outBorderSize + cellSize; i < gridSize - outBorderSize; i = i + cellSize + inBorderSize) {
            curRow = curRow + 1;
            if ((curRow == 3) || (curRow == 6)) {
                continue;
            }
            canvas.drawRect(outBorderSize, i, gridSize - outBorderSize, i + inBorderSize, paint);
            canvas.drawRect(i, outBorderSize, i + inBorderSize, gridSize - outBorderSize, paint);
        }

        // DRAW 3x3 blocks separator
        paint.setColor(Color.DKGRAY);

        curRow = 0;
        for(int i = outBorderSize + cellSize; i < gridSize - outBorderSize; i = i + cellSize + inBorderSize) {
            curRow = curRow + 1;
            if (!((curRow == 3) || (curRow == 6))) {
                continue;
            }
            canvas.drawRect(outBorderSize, i, gridSize - outBorderSize, i + inBorderSize, paint);
            canvas.drawRect(i, outBorderSize, i + inBorderSize, gridSize - outBorderSize, paint);
        }

        // DRAW grid border
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, outBorderSize, gridSize, paint);
        canvas.drawRect(0, 0, gridSize, outBorderSize, paint);
        canvas.drawRect(0, gridSize - outBorderSize, gridSize, gridSize, paint);
        canvas.drawRect(gridSize - outBorderSize, 0, gridSize, gridSize, paint);

        // DRAW current focused cell
        if ((focusedColumn != 0) && (focusedRow != 0)) {
            paint.setColor(Color.YELLOW);
            canvas.drawRect(getPosPxStart(focusedColumn), getPosPxStart(focusedRow), getPosPxEnd(focusedColumn), getPosPxEnd(focusedRow), paint);
        }

        // DRAW cells value
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(cellSize/2);
        for(int c = 0; c < 9; c++) {
            for(int r = 0; r < 9; r++) {
                paint.setColor(cellUserSet[c][r] == 0 ? Color.RED : Color.BLACK);
                if (cellValue[c][r] != 0) {
                    float posX = (float) getPosPxMiddle(c + 1);
                    float posY = (float) getPosPxMiddle(r + 1) + (cellSize / 4);
                    canvas.drawText(Integer.toString(cellValue[c][r]), posX, posY, paint);
                }
            }
        }
    }

    public void focusedCell(int x, int y){
        focusedColumn = getPosNum(x);
        focusedRow = getPosNum(y);
    }

    private int getPosNum(int px) {
        int pos = 0;
        for(int i = outBorderSize; i < gridSize - outBorderSize; i = i + cellSize + inBorderSize) {
            if (px <= i) {
                break;
            }
            pos++;
        }
        return pos;
    }

    private int getPosPxStart(int num) {
        return (outBorderSize + ((num - 1) * inBorderSize) + cellSize * (num - 1));
    }

    private int getPosPxEnd(int num) {
        return (outBorderSize + ((num - 1) * inBorderSize) +  cellSize * num);
    }

    private int getPosPxMiddle(int num) {
        return ((getPosPxStart(num) + getPosPxEnd(num)) / 2);
    }

    public void setCell(int newValue) {
        if ((focusedColumn != 0) && (focusedRow != 0)) {
            cellValue[focusedColumn - 1][focusedRow - 1]    = newValue;
            cellUserSet[focusedColumn - 1][focusedRow - 1]  = 1;
        }
    }

    public void setGrid(int cells[][]){
        for (int c = 0; c < 9; c++) {
            for(int r = 0; r < 9; r++) {
                cellValue[c][r] = cells[c][r];
            }
        }
    }

    void setGrid(String input) {
        input = input.replaceAll("[^\\d]", "");
        if(input.length() != 9*9){
            Log.d(TAG, "INPUT TO SMALL");
            return;
        }

        for (int column = 0; column < 9; column++) {
            for (int row = 0; row < 9; row++) {
                // TODO ROW AND COLUMN ARE STRING REVERTED
                cellValue[row][column] = Character.getNumericValue(input.charAt(column * 9 + row));
            }
        }
    }


    public void setCellUserSet(){
        for (int c = 0; c < 9; c++) {
            for (int r = 0; r < 9; r++) {
                cellUserSet[c][r] = ((cellValue[c][r] == 0) ? 0 : 1);
            }
        }
    }

    public int getCell(int column, int row) {
        return cellValue[column][row];
    }

    public int[][] getGrid() {
        return cellValue;
    }
}
