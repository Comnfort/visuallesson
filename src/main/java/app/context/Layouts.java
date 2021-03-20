package app.context;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;

 class Layouts extends AbstractContext {

    public Layouts() {
        super("layouts");
    }


    public ArrayList<JPanel> getContext(){

        return new ArrayList<>(Arrays.asList(createBorderLayout(), createFlowLayout(), createGridLayout(),
                createBoxLayout(), createGridBagLayout(), createCardLayout(),
                createGroupLayout(),createOverlayLayout(), createSpringLayout()));
    }


    private JPanel createBorderLayout(){
        JPanel borderLayout = new JPanel(new BorderLayout());
        JButton button1 = new JButton("CENTER");
        JLabel label1= new JLabel("WEST");
        JLabel label2 = new JLabel("EAST");
        JButton button2 = new JButton("NORTH");
        JButton button3 = new JButton("SOUTH");
        borderLayout.add(button1,BorderLayout.CENTER);
        borderLayout.add(button2,BorderLayout.NORTH);
        borderLayout.add(button3,BorderLayout.SOUTH);
        borderLayout.add(label1,BorderLayout.WEST);
        borderLayout.add(label2,BorderLayout.EAST);
        return borderLayout;
    }

    private JPanel createFlowLayout(){
        JPanel flowLayout = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton button1 = new JButton("first");
        button1.setPreferredSize(new Dimension(800,100));
        JButton button2 = new JButton("Second");
        button2.setPreferredSize(new Dimension(200,200));
        JButton button3 = new JButton("third");
        button3.setPreferredSize(new Dimension(200,150));
        JButton button4 = new JButton("fourth");
        button4.setPreferredSize(new Dimension(800,100));
        flowLayout.add(button1);
        flowLayout.add(button2);
        flowLayout.add(button3);
        flowLayout.add(button4);
        return flowLayout;

    }
    private JPanel createGridLayout(){
        JPanel gridLayout = new JPanel(new GridLayout(8,8,20,5));
        gridLayout.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        for (int i = 0; i < 64; i++) {
            JButton button = new JButton(String.valueOf(i));
            if (i%2==0){button.setBackground(Color.orange);}
            else {button.setBackground(Color.white);}
            gridLayout.add(button);
        }
        return gridLayout;
    }


    private JPanel createBoxLayout(){
        JPanel boxLayout = new JPanel(new BorderLayout());
        class ScrollPanel extends JPanel implements Scrollable
        {
            @Override
            public Dimension getPreferredScrollableViewportSize() {return null; }

            @Override
            public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
                return 0;
            }

            @Override
            public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
                return 0;
            }

            @Override
            public boolean getScrollableTracksViewportWidth() {
                return true;
            }

            @Override
            public boolean getScrollableTracksViewportHeight() {
                return false;
            }
        }

        ScrollPanel wrap=new ScrollPanel();
        wrap.setLayout(new BoxLayout(wrap,BoxLayout.Y_AXIS));
        JScrollPane pane=new JScrollPane(wrap);
        pane.getVerticalScrollBar().setUnitIncrement(16);
        pane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        pane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );

        boxLayout.setBorder(new EmptyBorder(20,20,20,20));
        wrap.setBorder(new EmptyBorder(20,20,20,20));
        boxLayout.add(pane);
        ArrayList<String> definitions=new ArrayList<>();
        try(FileReader reader = new FileReader("src/main/resources/descriptions.properties", Charset.forName("cp1251"))) {
            Properties properties = new Properties();
            properties.load(reader);
            String prop = (String) properties.get("layouts");
            definitions =new ArrayList<>(Arrays.asList(prop.split("[|]")));
        } catch(Exception e) {
            e.printStackTrace();
        }

        for (String definition : definitions) {
            JTextArea text = new JTextArea(definition);
            text.setFont(new Font("Serif", Font.PLAIN, 14));
            text.setBorder(new EmptyBorder(10, 25, 10, 25));
            text.setLineWrap(true);
            text.setWrapStyleWord(true);
            text.setEnabled(false);
            text.setDisabledTextColor(Color.BLACK);
            wrap.add(text);
            wrap.add(Box.createVerticalStrut(10));
        }
        //отмотает скролл в начало
        SwingUtilities.invokeLater(() -> {
//                pane.getVerticalScrollBar().setValue(10);
            pane.getViewport().setViewPosition( new Point(0, 0) );
        });

        return boxLayout;
    }


    private JPanel createGridBagLayout(){
        JPanel gridBagLayout = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        // По умолчанию натуральная высота, максимальная ширина
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5;
        constraints.gridy   = 0  ;  // нулевая ячейка таблицы по вертикали
        constraints.gridx = 0;      // нулевая ячейка таблицы по горизонтали

        JButton button = new JButton("first");
        gridBagLayout.add(button,constraints);

        JButton button2 = new JButton("Second");
        constraints.gridx = 1;
        gridBagLayout.add(button2,constraints);

        JButton button3 = new JButton("third");
        constraints.ipadx=45;
        constraints.gridx = 2;
        gridBagLayout.add(button3,constraints);

        JButton button4 = new JButton("fourth");
        constraints.gridy = 1;
        constraints.gridx = 0;
        constraints.ipady     = 45;   // кнопка высокая
        constraints.weightx   = 0.0;
        constraints.gridwidth = 2;    // размер кнопки в две ячейки
        gridBagLayout.add(button4,constraints);

        JButton button5 = new JButton("fifth");
        constraints.ipady     = 0;    // установить первоначальный размер кнопки
        constraints.weighty   = 1.0;
        // установить кнопку в конец окна
        constraints.anchor = GridBagConstraints.PAGE_END;
        constraints.insets    = new Insets(5, 0, 25, 25);
        constraints.gridwidth = 2;    // размер кнопки в 2 ячейки
        constraints.gridx     = 1;    // первая ячейка таблицы по горизонтали
        constraints.gridy     = 2;    // вторая ячейка по вертикали
        gridBagLayout.add(button5,constraints);
        return gridBagLayout;
    }


    private JPanel createCardLayout(){
        JPanel cardLayout = new JPanel();
        cardLayout.setLayout(new BorderLayout());
        JPanel panel1=new JPanel();
        JButton button1 = new JButton("first");
        button1.setPreferredSize(new Dimension(200,100));
        JButton button2 = new JButton("Second");
        button2.setPreferredSize(new Dimension(200,100));
        JButton button3 = new JButton("third");
        button3.setPreferredSize(new Dimension(200,100));
        panel1.add(button1);
        panel1.add(button2);
        panel1.add(button3);
        panel1.setBackground(new Color(45, 241, 137, 128));
        JPanel panel2=new JPanel();
        JButton button11 = new JButton("вектор");
        JButton button22 = new JButton("второго");
        JButton button33 = new JButton("направления");
        panel2.add(button11);
        panel2.add(button22);
        panel2.add(button33);
        panel2.setBackground(new Color(67, 0, 12, 103));
        cardLayout.add(placeAsCard(new ArrayList<>(List.of(panel1,panel2))));
        return cardLayout;
    }

    private JPanel createGroupLayout(){
        JPanel groupLayout = new JPanel();
        // Список компонентов формы
        JLabel      label           = new JLabel("Поиск строки :");
        JTextField  textField       = new JTextField();
        JCheckBox   cbCaseSensitive = new JCheckBox("Учет регистра");
        JCheckBox   cbWholeWords    = new JCheckBox("Целое слово"  );
        JCheckBox   cbBackward      = new JCheckBox("Поиск назад"  );
        JButton     btnFind         = new JButton("Найти"   );
        JButton     btnCancel       = new JButton("Отменить");

        //удаление пустой рамки
        cbCaseSensitive.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbWholeWords   .setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbBackward     .setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Определение менеджера расположения
        GroupLayout layout = new GroupLayout(groupLayout);
        groupLayout.setLayout(layout);
        layout.setAutoCreateGaps(true); //внутренние отступы между элементами
        layout.setAutoCreateContainerGaps(true); //отступы контейнера
        //LEADING выравниванию по левому краю, TRAILING выравнивание по правому краю в измерении по горизонтали
        //в горизонтальной разбиваем по столбцам
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(label)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(textField)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(LEADING)
                                        .addComponent(cbCaseSensitive)
                                        .addComponent(cbBackward))
                                .addGroup(layout.createParallelGroup(LEADING)
                                        .addComponent(cbWholeWords))))
                .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(btnFind)
                        .addComponent(btnCancel))
        );

        layout.linkSize(SwingConstants.HORIZONTAL, btnFind, btnCancel); //зафиксировать размер кнопок

        //в вертикальной выравниваем строки столбцов
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(label)
                        .addComponent(textField)
                        .addComponent(btnFind))
                .addGroup(layout.createParallelGroup(LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(cbCaseSensitive)
                                        .addComponent(cbWholeWords))
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(cbBackward)))
                        .addComponent(btnCancel))
        );
        return groupLayout;
    }


    private JPanel createOverlayLayout(){
        JPanel overlayLayout = new JPanel();
        overlayLayout.setLayout( new OverlayLayout(overlayLayout) );
        overlayLayout.setBackground( Color.ORANGE );

        JPanel red = new JPanel();
        red.setBackground( Color.RED );
        red.setPreferredSize( new Dimension(250, 250) );
        red.setMaximumSize( red.getPreferredSize() );
        red.setMinimumSize( red.getPreferredSize() );
        overlayLayout.add( red );

        JPanel green = new JPanel();
        green.setBackground( Color.GREEN );
        green.setPreferredSize( new Dimension(400, 400) );
        green.setMaximumSize( green.getPreferredSize() );
        green.setMinimumSize( green.getPreferredSize() );
        green.setAlignmentX(0.5f);
        green.setAlignmentY(0.5f);
        overlayLayout.add( green );
        overlayLayout.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                Point point = e.getPoint();
                if (SwingUtilities.isLeftMouseButton(e)){ red.setLocation(point);
                }else if (SwingUtilities.isRightMouseButton(e)){green.setLocation(point);}
            }
        });
        return overlayLayout;
    }


    private JPanel createSpringLayout() {
        JPanel springLayout = new JPanel();
        SpringLayout layout = new SpringLayout();
        springLayout.setLayout(layout);
        JLabel label = new JLabel("Ввод");
        JTextField field = new JTextField(15);
        JButton button=new JButton("Найти");

        springLayout.add(label);
        springLayout.add(field);
        springLayout.add(button);
        layout.putConstraint(SpringLayout.WEST, label, 10, SpringLayout.WEST, springLayout);
        layout.putConstraint(SpringLayout.NORTH, label, 25, SpringLayout.NORTH, springLayout);
        layout.putConstraint(SpringLayout.NORTH, field, 25, SpringLayout.NORTH, springLayout);
        layout.putConstraint(SpringLayout.WEST, field, 20, SpringLayout.EAST, label);
        layout.putConstraint(SpringLayout.WEST, button, 20, SpringLayout.EAST, field);
        layout.putConstraint(SpringLayout.BASELINE, button, 0, SpringLayout.BASELINE, label);
        return springLayout;
    }

}
