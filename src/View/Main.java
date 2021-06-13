package View;

import Model.IModel;
import Model.MyModel;
import Server.Configurations;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    public static MyModel myModel;
    public static MyViewModel myViewModel;
    public static Stage MainMenu;
    public static Stage AlertStage;
    public static Stage gameStage;
    public static Scene MainMenuScene;
    public static Scene HelpScene;
    public static Scene AboutScene;
    public static Scene AlertScene;
    public static Scene OptionsScene;
    public static Scene WinScene;
    public static Scene GameScene;

    public static MainMenu2Controller MainMenuController;
    public static MyViewController myViewController;
    public static HelpController helpController;


    public static MediaPlayer MainMenuMusic;
    public static MediaPlayer MazeMusic;
    public static MediaPlayer WinningMusic;

    public Main() {

    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        Main.MainMenu = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainMenu2.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root, 1000, 700));
        MainMenu.show();
        MainMenu.setResizable(false);

        IModel model = new MyModel();
        this.myViewModel = new MyViewModel(model);
        MainMenu2Controller view = fxmlLoader.getController();
        view.setViewModel(this.myViewModel);
        model.assignObserver(myViewModel);


        File musicPath=new File("resources/Music/powerpuffgirls.mp3");
        MainMenuMusic=new MediaPlayer(new Media(musicPath.toURI().toString()));
        MainMenuMusic.play();


        FXMLLoader aboutFXMLLoader=new FXMLLoader(getClass().getResource("About.fxml"));
        Parent aboutRoot=aboutFXMLLoader.load();
        primaryStage.setTitle("About");
        AboutScene=new Scene(aboutRoot,1000,700);

    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void toAbout() throws IOException{
        MainMenuMusic.stop();
        MainMenu.setScene(AboutScene);
    }

    public static void toMainScene() throws IOException{
        FXMLLoader GenerateMazeWindow=new FXMLLoader(Main.class.getResource("MyView.fxml"));
        Parent root=GenerateMazeWindow.load();
        IModel model = new MyModel();
        //MyViewModel myViewModel = new MyViewModel(model);
        Scene scene=new Scene(root,1000,700);
        Main.MainMenu.setScene(scene);
        Main.MainMenu.setTitle("Main");
        MainMenuMusic.play();
        IModel model1 = new MyModel();
        MyViewModel myViewModel1 = new MyViewModel(model1);
        MyViewController view = GenerateMazeWindow.getController();
        view.setViewModel(myViewModel1);
    }

    public static void PropetiesAlert() throws IOException{
        //MainMenuMusic.stop();
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        Configurations conf=Configurations.getInstance();
        alert.setContentText("The Thread size is: "+conf.getThreadSize()+"\n"+"The Generating Algorithm is: "+conf.getMazeGenerator()+"" +
                "\n"+"The Solving Algorithm is: "+conf.getSolutionType());
        alert.show();
    }

    public static void toMainMenu() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainMenu2.fxml"));
        Parent root = fxmlLoader.load();
        MainMenuScene=new Scene(root,1000,700);

        MainMenu.setScene(MainMenuScene);
    }

    public static void toTheMaze() throws IOException{
        MainMenuMusic.stop();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        GameScene=new Scene(root,1000,700);
        myViewController = fxmlLoader.getController();
        myViewController.setViewModel(myViewModel);

        MainMenu.setScene(GameScene);

    }

    public static void toHelp() throws IOException{
        MainMenuMusic.stop();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Help.fxml"));
        Parent root = fxmlLoader.load();
        HelpScene=new Scene(root,1000,700);
        helpController = fxmlLoader.getController();

        MainMenu.setScene(HelpScene);

    }

}
