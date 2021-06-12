package Model;
import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import View.MazeDisplayer;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.control.Alert;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel {
    private Maze maze;
    private int playerRow;
    private int playerCol;
    private Solution solution;
    private MazeDisplayer mazeDisplayer;
    private Server serverGen;

    public MyModel() {
        mazeDisplayer=new MazeDisplayer();


    }

    @Override
    public void generateMaze(int rows, int cols) {
        serverGen=new Server(5400,1000,new ServerStrategyGenerateMaze());
        serverGen.start();
        CommunicateWithServer_MazeGenerating(rows,cols);
        setChanged();
        notifyObservers("maze generated");
        // start position:
        movePlayer(0, 0);
        serverGen.stop();


    }

    private void CommunicateWithServer_MazeGenerating(int rows,int cols) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                        @Override
                        public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                            try {
                                ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                                ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                                toServer.flush();
                                int[] mazeDimensions = new int[]{rows, cols};
                                toServer.writeObject(mazeDimensions); //send mazedimensions to server
                                toServer.flush();
                                byte[] compressedMaze = (byte[])
                                        fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                                InputStream is = new MyDecompressorInputStream(new
                                        ByteArrayInputStream(compressedMaze));
                                byte[] decompressedMaze = new byte[rows*cols+12 /*CHANGE
SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressedmaze -
                                        is.read(decompressedMaze); //Fill decompressedMazewith bytes
                                   maze = new Maze(decompressedMaze);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Maze getMaze() {
        return maze;
    }

    @Override
    public void updatePlayerLocation(MovementDirection direction) {
        switch (direction) {
            case UP -> {
                if (playerRow > 0)
                    if(maze.getArray()[playerRow-1][playerCol]!=1)
                        movePlayer(playerRow - 1, playerCol);
            }
            case DOWN -> {
                if (playerRow < maze.getArray().length - 1)
                    if(maze.getArray()[playerRow+1][playerCol]!=1)
                        movePlayer(playerRow + 1, playerCol);
            }
            case LEFT -> {
                if (playerCol > 0)
                    if(maze.getArray()[playerRow][playerCol-1]!=1)
                        movePlayer(playerRow, playerCol - 1);
            }
            case RIGHT -> {
                if (playerCol < maze.getArray()[0].length - 1)
                    if(maze.getArray()[playerRow][playerCol+1]!=1)
                        movePlayer(playerRow, playerCol + 1);
            }
        }

    }

    private void movePlayer(int row, int col){
        this.playerRow = row;
        this.playerCol = col;
        setChanged();
        notifyObservers("player moved");
        int endRow=maze.getGoalPosition().getRowIndex();
        int endColumn=maze.getGoalPosition().getColumnIndex();
        if(row==endRow && col==endColumn){
            Alert youWin=new Alert(Alert.AlertType.INFORMATION);
            youWin.setContentText("You Win!!!");
            this.playerRow=0;
            this.playerCol=0;
            youWin.show();
            mazeDisplayer.drawMaze(maze);


        }
    }

    @Override
    public int getPlayerRow() {
        return playerRow;
    }

    @Override
    public int getPlayerCol() {
        return playerCol;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void solveMaze() {
        //solve the maze
        //solution = new Solution();
        setChanged();
        notifyObservers("maze solved");
    }

    @Override
    public Solution getSolution() {
        return solution;
    }
}
