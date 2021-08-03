package agh.cs;

import javax.swing.*;

/* rozszerzenie przycisku o zawarcie informacji o stanie mapy oraz automatyczne ustawianie tekstu jaki się na przycisku
pojawia
 */

public class MyButton extends JButton {
    private int state;
    private String text;

    public MyButton(int state,String text){
        this.state=state;
        this.text=text;
        this.setText(text);
    }
}
