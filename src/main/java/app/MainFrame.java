package app;

import app.context.AbstractContext;

import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame {

    public static void main(String[] args) {
        MainFrame instance = MainFrame.getInstance();
    }

    private double width=Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private double height=Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private JPanel choicePanel;
    private AbstractContext centralPanel;

    JPanel getCentralPanel() {
        return centralPanel;
    }

    void setCentralPanel(AbstractContext centralPanel) {
        this.centralPanel = centralPanel;
    }

    private MainFrame(){
        SwingUtilities.invokeLater(() -> {
            setTitle("Theory");
            setPreferredSize(new Dimension((int)(width*0.8), (int)(height*0.8)));
            setMaximumSize(this.getPreferredSize());
            setMinimumSize(this.getPreferredSize());
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            centralPanel=AbstractContext.getInstance("");
            choicePanel= new SelectionPanel(this);
            add(choicePanel,BorderLayout.WEST);
            add(centralPanel,BorderLayout.CENTER);
            setVisible(true);
        });
    }





    //получение окна через внутренний класс с ленивой инициализацией
    public static MainFrame getInstance(){
        return SingltonFrame.INSTANCE;
    }
    private static class SingltonFrame {
        private static final MainFrame INSTANCE =new MainFrame();
    }

}
