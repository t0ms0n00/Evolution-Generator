package agh.cs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;

public class WriteToFile implements ActionListener {

    private JFrame frameSave = new JFrame();
    private MyButton map1 = new MyButton(1,"Mapa 1");
    private MyButton map2 = new MyButton(2,"Mapa 2");
    private MyButton map12 = new MyButton(12,"Mapy 1 i 2");
    private MapVisualiser vis1;
    private MapVisualiser vis2;

    /* konstruktor dla okna do wyboru sposobu zapisu*/

    public WriteToFile(MapVisualiser vis1, MapVisualiser vis2){
        this.vis1=vis1;
        this.vis2=vis2;

        frameSave.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frameSave.setVisible(true);
        frameSave.setBounds(0,0,400,400);
        frameSave.setLayout(new GridLayout(2,1,5,5));
        frameSave.setResizable(false);

        JLabel label=new JLabel();
        label.setFont(new Font("TimesRoman", Font.BOLD | Font.ITALIC, 25));
        label.setText("Którą mapę zapisać w pliku?");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel buttonPanel=new JPanel();
        buttonPanel.setLayout(new GridLayout(1,3,5,5));

        map1.addActionListener(this);
        map2.addActionListener(this);
        map12.addActionListener(this);

        buttonPanel.add(map1);
        buttonPanel.add(map12);
        buttonPanel.add(map2);

        frameSave.add(label);
        frameSave.add(buttonPanel);
    }

    /* procedura zapisująca mapę do pliku */

    public void write(String a){
        try {
            FileWriter myWriter = new FileWriter("results.txt");
            myWriter.write(a);
            myWriter.close();
        }
        catch (IOException e) {
            System.out.println("Wystapil blad");
            e.printStackTrace();
        }
    }

    /* obsługa przycisków, wybierających mapę do zapisu w pliku */

    @Override
    public void actionPerformed(ActionEvent e) {
        if (map1.equals(e.getSource())){
            this.write("Mapa 1:\n"+vis1.getStats());
            this.frameSave.dispatchEvent(new WindowEvent(frameSave, WindowEvent.WINDOW_CLOSING));
        }
        else if (map2.equals(e.getSource())){
            this.write("Mapa 2:\n"+vis2.getStats());
            this.frameSave.dispatchEvent(new WindowEvent(frameSave, WindowEvent.WINDOW_CLOSING));
        }
        else{
            this.write("Mapa 1:\n"+vis1.getStats()+"Mapa 2:\n"+vis2.getStats());
            this.frameSave.dispatchEvent(new WindowEvent(frameSave, WindowEvent.WINDOW_CLOSING));
        }
    }
}
