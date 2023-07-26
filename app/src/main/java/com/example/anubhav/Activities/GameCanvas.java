package com.example.anubhav.Activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import com.example.anubhav.Utils.Line;
import com.example.anubhav.Utils.Point;
import com.example.anubhav.Utils.Square;
import java.util.ArrayList;

public class GameCanvas extends View {
    private final int three =3;
    private int currentPlayer = 1;
    private int fullSizeOfOneCell;
    private float dotWidth, lineWidth;
    private boolean squareAdded = false;
    private final Paint dotColor, lineColor;
    private final Point[][] gridPattern = new Point[4][4];
    private final ArrayList<Line> lines;
    private final ArrayList<Square> squares;
    private CanvasListener listener;
    private final String[] dualColors = new String[2];

    public GameCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        lines = new ArrayList<>();
        squares = new ArrayList<>();

        dotColor = new Paint();
        dotColor.setColor(Color.parseColor("#494849"));  //grey dots on grid

        lineColor = new Paint();
        lineColor.setColor(Color.parseColor("#FF6160"));    //red - initial player
    }

    public void setListener(CanvasListener listener) {
        this.listener = listener;
    }

    public interface CanvasListener {

        void onSquareAdded(int player);

        void onGridCompleted();
    }

    public void setPlayers(String[] colors) {
        System.arraycopy(colors, 0, this.dualColors, 0, 2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Calculate the radius of each circle
        dotWidth = (float) (w * 0.02);

        // Calculate the width of the lines
        lineWidth = (float) (w * 0.015);
        lineColor.setStrokeWidth(lineWidth);

        // Calculate the size of each grid cell
        int gridSize = (int) (w - 2 * dotWidth);
        fullSizeOfOneCell = gridSize / three;

        // Generate the grid pattern
        for (int i = 0; i <= three; i++) {
            for (int j = 0; j <= three; j++) {
                gridPattern[i][j] = new Point(fullSizeOfOneCell * i, fullSizeOfOneCell * j);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the squares which have already been added to the list
        for (Square square : squares) {
            canvas.drawRect(square.rectF, square.paint);
        }

        // Draw the lines which have already been added to the list
        for (Line line : lines) {
            canvas.drawLine(line.getStartX() + dotWidth, line.getStartY() + dotWidth, line.getStopX() + dotWidth, line.getStopY() + dotWidth, line.getPaint());
        }

        Point point;
        // Draw the grid cells
        for (int i = 0; i <= three; i++) {
            for (int j = 0; j <= three; j++) {
                point = gridPattern[i][j];
                canvas.drawRect(point.getX(), point.getY(), point.getX() + (2 * dotWidth), point.getY() + (2 * dotWidth), dotColor);
            }
        }

        // Check if the grid is completed
        if (squares.size() == 9)
            listener.onGridCompleted();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        double oX = event.getX();       // detected touch X coordinate
        double oY = event.getY();       // detected touch Y coordinate
        double x = oX - dotWidth;       // adjusted touch X coordinate
        double y = oY - dotWidth;       // adjusted touch Y coordinate

        // Convert touch coordinates to grid indices
        double rx = x / fullSizeOfOneCell;  // this tells which cell from the left it is they should be less than 3 in decimal values
        double ry = y / fullSizeOfOneCell;  // this tells which cell from the top it is
        double rxS = Math.floor(rx);        // these are the base of that cell,
        double ryS = Math.floor(ry);        // so possible values can only be 0, 1 or 2
        double x1 = rx - rxS;               // determines the coordinate
        double y1 = ry - ryS;               // taking origin as the base of that cell

        if (event.getAction() == MotionEvent.ACTION_UP) {
            Line line = new Line(lineColor);

            // Check if the touch is within the valid grid range
            // x + y = 1 is the line though the diagonal of the cell
            if (rxS < three && ryS < three) {
                if (x1 >= y1) {
                    if (x1 + y1 >= 1) {  //RIGHT
                        line.setStartX((float) ((rxS + 1) * fullSizeOfOneCell));
                        line.setStartY((float) (ryS * fullSizeOfOneCell));
                        line.setStopX((float) ((rxS + 1) * fullSizeOfOneCell));
                        line.setStopY((float) ((ryS + 1) * fullSizeOfOneCell));
                        line.setOrientation(468);
                    }
                    else if (x1 + y1 < 1) {  //BOTTOM
                        line.setStartX((float) (rxS * fullSizeOfOneCell));
                        line.setStartY((float) (ryS * fullSizeOfOneCell));
                        line.setStopX((float) ((rxS + 1) * fullSizeOfOneCell));
                        line.setStopY((float) (ryS * fullSizeOfOneCell));
                        line.setOrientation(135);
                    }
                }
                else {
                    if (x1 + y1 >= 1) {  //TOP
                        line.setStartX((float) (rxS * fullSizeOfOneCell));
                        line.setStartY((float) ((ryS + 1) * fullSizeOfOneCell));
                        line.setStopX((float) ((rxS + 1) * fullSizeOfOneCell));
                        line.setStopY((float) ((ryS + 1) * fullSizeOfOneCell));
                        line.setOrientation(135);
                    }
                    else if (x1 + y1 < 1) {  //LEFT
                        line.setStartX((float) (rxS * fullSizeOfOneCell));
                        line.setStartY((float) (ryS * fullSizeOfOneCell));
                        line.setStopX((float) (rxS * fullSizeOfOneCell));
                        line.setStopY((float) ((ryS + 1) * fullSizeOfOneCell));
                        line.setOrientation(468);
                    }
                }

                boolean found = false;
                for (Line line1 : lines) {
                    if (line.equals(line1)) {
                        found = true;
                        break;
                    }
                }

                // Add the line to the list of lines if it doesn't already exist
                if (!found) {
                    if (!squareAdded)
                        nextPlayer();
                    line.setColor(dualColors[currentPlayer]);
                    lines.add(line);
                    checkForSquare(line);
                    postInvalidate();
                    return true;
                }
            }
        }
        return true;
    }


    private void checkForSquare(Line line) {
        Square s1, s2;
        boolean exists1 = false, exists2 = false;
        float startX = line.getStartX(), startY = line.getStartY(), stopX = line.getStopX(), stopY = line.getStopY();

        // X increases in right direction
        // Y increases in downward direction
        // Check for squares when the line orientation is 135 degrees ie horizontal line
        if (line.getOrientation() == 135) {
            Line tL = new Line(startX, startY - fullSizeOfOneCell, startX, startY);     //topLeft line
            Line tR = new Line(stopX, stopY - fullSizeOfOneCell, stopX, stopY);         // topRight Line
            Line tT = new Line(startX, startY - fullSizeOfOneCell, stopX, stopY - fullSizeOfOneCell);   //topTop line
            Line bL = new Line(startX, startY + fullSizeOfOneCell, startX, startY);     // bottomLeft line
            Line bR = new Line(stopX, stopY + fullSizeOfOneCell, stopX, stopY);         // bottomRight line
            Line bB = new Line(startX, startY + fullSizeOfOneCell, stopX, stopY + fullSizeOfOneCell);   // bottomBottom line

            // Check if the three lines forming a square exist in the list of lines
            for (Line line1 : lines) {
                if (tL.equals(line1)) {                 // basically, means that if the topLeft line of the horizontal line drawn rn already exists
                    for (Line line2 : lines) {
                        if (tR.equals(line2)) {         // if topRight also exits
                            for (Line line3 : lines) {
                                if (tT.equals(line3)) {    // and if topTop also exists
                                    exists1 = true;         // means the above square is to be drawn
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            // Create a new square if the first square exists
            if (exists1) {
                s1 = new Square(startX + dotWidth + lineWidth, startY - fullSizeOfOneCell + dotWidth + lineWidth, stopX + dotWidth - lineWidth, startY + dotWidth - lineWidth, dualColors[currentPlayer]);
                s1.lineIndex = lines.indexOf(line);
                squares.add(s1);
            }

            // Check if the three lines forming the second square exist in the list of lines
            for (Line line1 : lines) {
                if (bL.equals(line1)) {
                    for (Line line2 : lines) {
                        if (bR.equals(line2)) {
                            for (Line line3 : lines) {
                                if (bB.equals(line3)) {
                                    exists2 = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            // Create a new square if the second square exists
            if (exists2) {
                s2 = new Square(startX + dotWidth + lineWidth, startY + dotWidth + lineWidth, stopX + dotWidth - lineWidth, startY + fullSizeOfOneCell + dotWidth - lineWidth, dualColors[currentPlayer]);
                s2.lineIndex = lines.indexOf(line);
                squares.add(s2);
            }
        }
        // Check for squares when the line orientation is not 135 degrees
        else {  // ie if the line drawn was vertical, we need to check the left and right boxes
            Line lT = new Line(startX - fullSizeOfOneCell, startY, startX, startY);
            Line lL = new Line(startX - fullSizeOfOneCell, startY, stopX - fullSizeOfOneCell, stopY);
            Line lB = new Line(stopX - fullSizeOfOneCell, stopY, stopX, stopY);
            Line rT = new Line(startX, startY, startX + fullSizeOfOneCell, startY);
            Line rR = new Line(startX + fullSizeOfOneCell, startY, stopX + fullSizeOfOneCell, stopY);
            Line rB = new Line(stopX, stopY, stopX + fullSizeOfOneCell, stopY);

            // Check if the three lines forming a square exist in the list of lines
            for (Line line1 : lines) {
                if (lT.equals(line1)) {
                    for (Line line2 : lines) {
                        if (lL.equals(line2)) {
                            for (Line line3 : lines) {
                                if (lB.equals(line3)) {
                                    exists1 = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            // Create a new square if the first square exists
            if (exists1) {
                s1 = new Square(startX - fullSizeOfOneCell + dotWidth + lineWidth, startY + dotWidth + lineWidth, startX + dotWidth - lineWidth, stopY + dotWidth - lineWidth, dualColors[currentPlayer]);
                s1.lineIndex = lines.indexOf(line);
                squares.add(s1);
            }

            // Check if the three lines forming the second square exist in the list of lines
            for (Line line1 : lines) {
                if (rT.equals(line1)) {
                    for (Line line2 : lines) {
                        if (rR.equals(line2)) {
                            for (Line line3 : lines) {
                                if (rB.equals(line3)) {
                                    exists2 = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            // Create a new square if the second square exists
            if (exists2) {
                s2 = new Square(startX + dotWidth + lineWidth, startY + dotWidth + lineWidth, startX + fullSizeOfOneCell + dotWidth - lineWidth, stopY + dotWidth - lineWidth, dualColors[currentPlayer]);
                s2.lineIndex = lines.indexOf(line);
                squares.add(s2);
            }
        }

        // Check if at least one square exists
        if (exists1 | exists2) {
            squareAdded = true;
            listener.onSquareAdded(currentPlayer);
            if (exists1 && exists2)
                listener.onSquareAdded(currentPlayer);
        } else
            squareAdded = false;
    }

    private void nextPlayer() {
        currentPlayer = (currentPlayer +1) %2;
    }
}
