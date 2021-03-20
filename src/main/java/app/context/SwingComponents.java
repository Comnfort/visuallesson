package app.context;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

class SwingComponents extends AbstractContext {

    public SwingComponents() {
        super("swingcomponents");
    }


    @Override
    ArrayList<JPanel> getContext() {
        return new ArrayList<>(Arrays.asList(
                createJTree()));
    }


    JPanel createJTree(){
        JPanel treePanel = new JPanel(new BorderLayout());
        JPanel treePanel2 = new JPanel(new BorderLayout());

        //данные для первого дерева
        DefaultMutableTreeNode root=new DefaultMutableTreeNode();
        root.setUserObject("Корневая запись");
        DefaultMutableTreeNode node1=new DefaultMutableTreeNode("Напитки");
        DefaultMutableTreeNode subnode1 = new DefaultMutableTreeNode("Чай");
        subnode1.setAllowsChildren(false);
        node1.add(subnode1);
        node1.add(new DefaultMutableTreeNode("Кофе"));
        DefaultMutableTreeNode node2=new DefaultMutableTreeNode("Сладости");
        node2.add(new DefaultMutableTreeNode("Мороженое"));
        node2.add(new DefaultMutableTreeNode("Зефир"));
        root.add(node1);
        root.add(node2);



        List<String> paths =new ArrayList<>();

        //заполнение листа обходом текущей директории вглубь
        try {
//            Files.walkFileTree(Paths.get(System.getProperty("user.dir")), new SimpleFileVisitor<Path>(){
            Files.walkFileTree(Paths.get("src"), new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    paths.add(file.toString());
                    return super.visitFile(file, attrs);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Заполнение дерева путями к файлам
         *
         */

        DefaultMutableTreeNode root2=new DefaultMutableTreeNode("currentDir");
        DefaultMutableTreeNode currentNode;
        List<Set<String>> setList=new ArrayList<>();
        for (int i = 0; i < paths.size(); i++) {
            //получаем массив каталогов
            String[] s = paths.get(i).split(Pattern.quote("\\"));
//            System.out.println(Arrays.asList(s));
            currentNode=root2;
            for (int j = 0; j < s.length; j++) {
                try {
                    //получаем вертикальное множество
                    Set<String> set = setList.get(j);
                    //если множество уже существует, проверяем на содержимость каталога
                    if (set.contains(s[j])){
                        //поиск дочернего каталога с таким же названием
                        for(int k=0; k < currentNode.getChildCount(); k++) {
                            if(currentNode.getChildAt(k).toString().equals(s[j])) {
                                currentNode = (DefaultMutableTreeNode) currentNode.getChildAt(k);
                                break;
                            }
                        }
                        continue;}
                    set.add(s[j]);
                }catch(IndexOutOfBoundsException e){
                    //множества не существует, создаём и добавляем новое
                    HashSet<String> newSet = new HashSet<>();
                    newSet.add(s[j]);
                    setList.add( j, newSet);
                }

                    DefaultMutableTreeNode tempNode=new DefaultMutableTreeNode(s[j]);
                    currentNode.add(tempNode);
                    currentNode=tempNode;
            }
        }


        //модель в которую передаём скомпонованные ноды
        DefaultTreeModel model=new DefaultTreeModel(root2){
            // Функция получения корневого узла дерева
            @Override
            public Object getRoot() {
                return root;
            }

            // Функция получения количество потомков узла
            @Override
            public int getChildCount(Object node){
                return ((TreeNode)node).getChildCount();
            }

            // Функция получения потомка узла по порядковому номеру
            @Override
            public Object getChild(Object node, int index){
                return ((TreeNode)node).getChildAt(index);
            }

            // Функция получения порядкового номера потомка
            @Override
            public int getIndexOfChild(Object node, Object child){
                if(node == null || child == null)
                    return -1;
                return ((TreeNode)node).getIndex((TreeNode)child);
            }

            // Функция определения, является ли узел листом
            @Override
            public boolean isLeaf(Object node){
                if(asksAllowsChildren)
                    return !((TreeNode)node).getAllowsChildren();
                return ((TreeNode)node).isLeaf();
            }

            // Функция вызывается при изменении значения некоторого узла
            @Override
            public void valueForPathChanged(TreePath path, Object value) {
                MutableTreeNode   aNode = (MutableTreeNode)path.getLastPathComponent();
                aNode.setUserObject(value);
                nodeChanged(aNode);
            }
        };



        DefaultTreeCellRenderer renderer=new DefaultTreeCellRenderer();  //отображение
        renderer.setFont(new Font("serif",Font.BOLD,20));
        renderer.setBackground(new Color(12, 109, 89));
        renderer.setBorderSelectionColor(new Color(3, 24, 19, 243));
        renderer.setForeground(Color.YELLOW);


        JTree tree = new JTree(model);
        tree.setCellRenderer(renderer);
//        tree.addTreeSelectionListener(e -> {   //что делать при выборе компонента
//
//            TreePath tp = tree.getSelectionPath();
//        });

        treePanel.add(new JScrollPane(tree),BorderLayout.CENTER);


        DefaultTreeModel model2=new DefaultTreeModel(root);
        JTree tree2 = new JTree(model2);
        treePanel2.add(new JScrollPane(tree2),BorderLayout.CENTER);

        //добавление поиска
        JTextField textArea=new JTextField(30);
        JButton button=new JButton("найти");
        button.setFocusPainted(false);
        button.addActionListener(e -> {
            String text = textArea.getText();
            if (!text.equals("")){
                //заполняю лист нодами
                List<DefaultMutableTreeNode> searchNodes = new ArrayList<DefaultMutableTreeNode>();
                Enumeration<?> enumeration = root2.preorderEnumeration();
                while(enumeration.hasMoreElements()) {
                    searchNodes.add((DefaultMutableTreeNode)enumeration.nextElement());
                }
                int index=0;
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
                DefaultMutableTreeNode foundNode=null;

                //находим текущий нод в списке, чтобы начать поиск с текущего нода
                if( selectedNode != null ) {
                    for(int i = 0; i < searchNodes.size(); i++) {
                        if( searchNodes.get(i) == selectedNode ) {
                            index = i;
                            break;
                        }
                    }
                }

                //начинаем поиск с текущего нода
                for(int i = index+1; i < searchNodes.size(); i++) {
                    if(searchNodes.get(i).toString().toLowerCase().contains(text.toLowerCase())) {
                        foundNode = searchNodes.get(i);
                        break;
                    }
                }

                //если ненашли ищем с начала списка
                if( foundNode == null ) {
                    for(int i = 0; i <= index; i++) {
                        if(searchNodes.get(i).toString().toLowerCase().contains(text.toLowerCase())) {
                            foundNode = searchNodes.get(i);
                            break;
                        }
                    }
                }

                if (foundNode!=null){
                    TreePath path = new TreePath(foundNode.getPath());
                    tree.setSelectionPath(path);
                    tree.scrollPathToVisible(path);
                }
            }
        });
        JPanel panelForSearch=new JPanel();
        panelForSearch.add(textArea);
        panelForSearch.add(button);
        treePanel.add(panelForSearch,BorderLayout.NORTH);


        JPanel panelForCard=new JPanel(new BorderLayout());
        panelForCard.add(placeAsCard(new ArrayList<>(List.of(treePanel,treePanel2))));
        return panelForCard;
    }


    JPanel createJList(){
        JPanel listPanel = new JPanel(new BorderLayout());



        return listPanel;
    }

    JPanel createJTable(){
        JPanel tablePanel = new JPanel(new BorderLayout());



        return tablePanel;
    }

}
