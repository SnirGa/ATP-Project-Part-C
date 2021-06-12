package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Canvas {
    private Maze maze;
    private Solution solution;

    // player position:
    private int playerRow = 0;
    private int playerCol = 0;
    // wall and player images:
    private StringProperty imageFileNameWall = new SimpleStringProperty();
    private StringProperty imageFileNamePlayer = new SimpleStringProperty();

    double canvasWidth;
    double canvasHeight;

    public MazeDisplayer() {
        this.widthProperty().addListener((e) ->{
            this.draw();
        });
        this.heightProperty().addListener((e) ->{
            this.draw();
        });
        GraphicsContext GC = this.getGraphicsContext2D();
        GC.clearRect(0.0D,0.0D,this.canvasWidth,canvasHeight);
    }

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public void setPlayerPosition(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
        draw();
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
        draw();
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public String imageFileNameWallProperty() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public String imageFileNamePlayerProperty() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    public void drawMaze(Maze maze) {
        this.maze = maze;
        draw();
    }

    private void draw() {
        if(maze != null){
            canvasHeight = getHeight();
            canvasWidth = getWidth();
            int rows = maze.getArray().length;
            int cols = maze.getArray()[0].length;
            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;
            Image mojo=new Image("/Images/mojoGoal.png");



            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);

            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
            if(solution != null)
                drawSolution(graphicsContext, cellHeight, cellWidth);
            drawPlayer(graphicsContext, cellHeight, cellWidth);
            Position goalPos=maze.getGoalPosition();
            graphicsContext.drawImage(mojo,goalPos.getColumnIndex()*cellWidth,goalPos.getRowIndex()*cellHeight,cellWidth,cellHeight);
        }
    }

    private void drawSolution(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        Image playerImage=new Image(Main.myViewModel.getCharPicPath());
        Image mojo=new Image("/Images/mojoGoal.png");

        for(int i=0;i<solution.getSolutionPath().size();i++){
            int row,col;
            row=((MazeState)solution.getSolutionPath().get(i)).getPosition().getRowIndex();
            col=((MazeState)solution.getSolutionPath().get(i)).getPosition().getColumnIndex();
            graphicsContext.drawImage(i<solution.getSolutionPath().size()-1?playerImage:mojo,(double) col*cellWidth,(double) row*cellHeight,cellWidth,cellHeight);

        }
    }

    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.RED);

        Image wallImage = null;
        try{
            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no wall image file");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(maze.getArray()[i][j] == 1){
                    //if it is a wall:
                    double x = j * cellWidth;
                    double y = i * cellHeight;
                    if(wallImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                }
            }
        }
    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.GREEN);

        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if(playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }

    public void zoom(){
        this.setOnScroll((e)->{
            if (e.isControlDown()) {
                double MouseChange=e.getDeltaY();
                double Const=1.03D;
                if (MouseChange <0.0D ){
                    Const=0.97D;
                }
                this.setScaleY(this.getScaleY()*Const);
                this.setScaleX(this.getScaleX()*Const);
                e.consume();
            }
        });
    }
}
