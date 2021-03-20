package app;

import app.context.AbstractContext;

import javax.swing.*;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

class SelectionPanel extends JPanel {
    private JLabel headLabel;
    private JPanel thisPanel;
    private boolean isHide;
    private int currentSection;
    private List<String> listSection;

   SelectionPanel(MainFrame frame) {
       thisPanel=this;
       isHide=false;
       currentSection=0;
       setLayout(new BorderLayout());
       listSection = AbstractContext.listSection;
       JList<String> wordList = new JList(listSection.toArray());
       wordList.setVisibleRowCount(15);
       Font font=new Font(Font.SERIF,Font.PLAIN,16);
       wordList.setFont(font);
       wordList.setLayoutOrientation(JList.VERTICAL);
       wordList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       wordList.addListSelectionListener(e -> {
           JList v= (JList) e.getSource();
           if (e.getValueIsAdjusting()){
               int selectedIndex = v.getSelectedIndex();
               if (currentSection==selectedIndex) return;
               Object selectedValue = v.getSelectedValue();
               System.out.println(selectedIndex+" : "+selectedValue);

               frame.remove(frame.getCentralPanel());
               frame.setCentralPanel(AbstractContext.getInstance(listSection.get(selectedIndex)));
               frame.add(frame.getCentralPanel(),BorderLayout.CENTER);
               currentSection=selectedIndex;
               frame.revalidate();
               frame.repaint();
           }
       });


       wordList.setCellRenderer(new DefaultListCellRenderer(){
           @Override
           protected void paintComponent(Graphics g) {
               super.paintComponent(g);
               g.drawRect(0,0,getWidth(),getHeight());
           }
       });



       //Настройка разделителя
       JSplitPane splitPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,wordList,null);
       SplitPaneUI spui = splitPane.getUI();
       if (spui instanceof BasicSplitPaneUI) {
           ((BasicSplitPaneUI) spui).getDivider().addMouseListener(new MouseAdapter() {
               @Override
               public void mouseClicked(MouseEvent event) {
                   if (event.getClickCount() == 2) {
                       if (isHide){
                           add(headLabel,BorderLayout.NORTH);
                           splitPane.setLeftComponent(wordList);
                           thisPanel.revalidate();
                           thisPanel.repaint();
                           isHide=false;
                       }else {
                           splitPane.setLeftComponent(null);
                           thisPanel.remove(headLabel);
                           thisPanel.revalidate();
                           thisPanel.repaint();
                           isHide=true;
                       }
                   }
               }
           });
       }

       headLabel =new JLabel("Разделы: ");
       headLabel.setFont(new Font(Font.SERIF,Font.PLAIN,16));
       add(headLabel,BorderLayout.NORTH);
       add(splitPane,BorderLayout.CENTER);

       setBorder(BorderFactory.createEmptyBorder(5,5,5,0));

   }







}
