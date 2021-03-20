package app.context;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public abstract class AbstractContext extends JPanel{

    private JTabbedPane pane;
    private ArrayList<JPanel> context;
    private ArrayList<String> nameDefinition;
    private ArrayList<String> definitions;
    private JTextArea displayDefinition;
    public static List<String> listSection = getSections();



    AbstractContext(String property) {
        super(true);
        setLayout(new BorderLayout());
        nameDefinition=getNameDefinitions(property);
        definitions= getDescriptions(property);
        context=getContext();
        addDefinitionAndContext();
    }


    private ArrayList<String> getNameDefinitions(String property){
        try(FileReader reader = new FileReader("src/main/resources/definitions.properties", Charset.forName("cp1251"))) {
            Properties properties = new Properties();
            properties.load(reader);
            String prop = (String) properties.get(property);
            nameDefinition =new ArrayList<>(Arrays.asList(prop.split("[|]")));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return nameDefinition;
    }


    private ArrayList<String> getDescriptions(String property){
        try(FileReader reader = new FileReader("src/main/resources/descriptions.properties", Charset.forName("cp1251"))) {
            Properties properties = new Properties();
            properties.load(reader);
            String prop = (String) properties.get(property);
            definitions =new ArrayList<>(Arrays.asList(prop.split("[|]")));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return definitions;
    }



    /**
     * Обязательное переопределение каждым наследником
     * Должен вернуть лист панелей которые будут разбиты на табы
     *
     */

    ArrayList<JPanel> getContext(){
        throw new UnsupportedOperationException();
    }



    /**
     * Дифолтная компоновка для многостраничного таба
     * Необходимо передать лист JPanel который будет скомпонован CardLayout менеджером
     * southPanel содержит инструменты для перелистывания вкладок, информацию о количестве листов
     *
     */

    JPanel placeAsCard(ArrayList<JPanel> panels){
        JPanel mainPanel=new JPanel(new BorderLayout());
        //центральная панель
        JPanel cardPanel=new JPanel(new CardLayout());
        DefaultComboBoxModel<String> nameComboModel = new DefaultComboBoxModel<String>();
        for (int i = 0; i < panels.size(); i++) {   //заполнение модели и компоновка панелей
            String s = String.valueOf(i + 1);
            nameComboModel.addElement(s);
            JPanel panel = panels.get(i);
            panel.setName(s);
            cardPanel.add(panels.get(i),panel.getName());
        }
        JComboBox<String> comboBox = new JComboBox<String>(nameComboModel);
        CustomButtonForTurnPage left=new CustomButtonForTurnPage(CustomButtonForTurnPage.Direction.LEFT);
        left.setActionCommand("previous");
        CustomButtonForTurnPage right=new CustomButtonForTurnPage(CustomButtonForTurnPage.Direction.RIGHT);
        right.setActionCommand("next");

        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout layout = (CardLayout)(cardPanel.getLayout());
                if (e.getActionCommand().equals("comboBoxChanged")){
                    String selection = comboBox.getSelectedItem().toString();
                    layout.show(cardPanel, selection);
                }else {
                    if (e.getActionCommand().equals("previous")){
                        layout.previous(cardPanel);
                    }else if (e.getActionCommand().equals("next")){
                        layout.next(cardPanel);
                    }
                    //установка значения в combobox
                    for (Component component : cardPanel.getComponents()) {
                        if (component.isVisible()) {
                            comboBox.setSelectedItem(component.getName());
                        }
                    }

                    mainPanel.revalidate();
                    mainPanel.repaint();
                }
            }
        };
        comboBox.addActionListener(action);
        left.addActionListener(action);
        right.addActionListener(action);


        //панель перелистывания
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 40, 5);
        JPanel southPanel=new JPanel(flowLayout);
        southPanel.setBorder(BorderFactory.createEtchedBorder());
        southPanel.setBackground(new Color(214, 216, 205));
        southPanel.add(left);
        southPanel.add(right);
        southPanel.add(comboBox);


        //возвращаемая панель
        mainPanel.add(cardPanel,BorderLayout.CENTER);
        mainPanel.add(southPanel,BorderLayout.SOUTH);
        return mainPanel;
    }


    /**
     * Заполнение JTabbedPane полученными из файлов свойств данными
     *
     */

    private void addDefinitionAndContext() {
        pane = new JTabbedPane();
        displayDefinition =new JTextArea(definitions.get(0));
        displayDefinition.setFont(new Font("Serif", Font.PLAIN, 16));
        displayDefinition.setLineWrap(true);
        displayDefinition.setWrapStyleWord(true);
        displayDefinition.setEnabled(false);
        displayDefinition.setBorder(BorderFactory.createEmptyBorder(15,20,15,20));
        displayDefinition.setBackground(Color.LIGHT_GRAY);
        displayDefinition.setDisabledTextColor(Color.BLACK);

        for (int i = 0; i < nameDefinition.size(); i++) {
            pane.addTab(nameDefinition.get(i), context.get(i) );
        }

        pane.addChangeListener(e -> {
            displayDefinition.setText(definitions.get(pane.getSelectedIndex()));
        });

        add(pane,BorderLayout.CENTER);
        add(displayDefinition,BorderLayout.SOUTH);
    }

    /**
     * Возвращает список всех разделов
     *
     */

    private static ArrayList<String> getSections(){
        ArrayList<String> sections=null;
        try(FileReader reader = new FileReader("src/main/resources/definitions.properties", Charset.forName("cp1251"))) {
            Properties properties = new Properties();
            properties.load(reader);
            String prop = (String) properties.get("sectionnames");
            sections =new ArrayList<>(Arrays.asList(prop.split("[|]")));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return sections;
    }

    /**
     * Получение экземпляра реализующего AbstractContext
     *
     */

    public static AbstractContext getInstance(String sectionName){
        int i=0;
        if (listSection.contains(sectionName)){
            i = listSection.indexOf(sectionName);
        }

        switch (i){
            case 0: return new Layouts();
            case 1: return new SwingComponents();
            default: return new Layouts();
        }
    }

 }
