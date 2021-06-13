package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;

import java.io.IOException;

public class MainMenu2Controller {
    public void setViewModel(MyViewModel myViewModel) {
    }

    public void SendToAbout(ActionEvent actionEvent) throws IOException {
        Main.toAbout();
    }

    public void sendToMaze(ActionEvent actionEvent) throws IOException {
        Main.toTheMaze();
    }

    public void goToHelp(ActionEvent actionEvent) throws IOException {
        Main.toHelp();
    }
}
