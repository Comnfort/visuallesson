package app.context;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

public class CustomButtonForTurnPage extends JButton {

    private final int ARR_SIZE = 20;
    private boolean direction;
    private Dimension size = new Dimension(50,25);

    CustomButtonForTurnPage(boolean direction) {

        this.direction=direction;
        setFocusPainted(false);
        setPreferredSize(size);
        setContentAreaFilled(false);


        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setForeground(Color.ORANGE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setForeground(Color.BLACK);
            }
        });
    }

    void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g1.create();
        g.setStroke(new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_NORMALIZE);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        double dx = x2 - x1;
        double dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        g.drawLine(0, 0, len-ARR_SIZE, 0);
        g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                new int[] {0, -ARR_SIZE+12, ARR_SIZE-12, 0}, 4);
    }

    //отрисовка содержания
    protected void paintComponent(Graphics g)
    {
        if (direction){
            drawArrow(g, size.width, size.height/2, 0, size.height/2);
        }else {
            drawArrow(g, 0, size.height/2, size.width, size.height/2);
        }

        super.paintComponent(g);
    }

    //отрисовка границ
    protected void paintBorder(Graphics g)
    {
//         Graphics2D g2 = (Graphics2D) g.create();
//         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                 RenderingHints.VALUE_ANTIALIAS_ON);
//         g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
//                RenderingHints.VALUE_STROKE_NORMALIZE);
//         g2.setStroke(new BasicStroke(1));
//         g2.setColor(Color.DARK_GRAY.darker());
//         g2.drawOval(0, 0, getSize().width-1,getSize().height-1);
    }

    //границы в которых кнопка будет срабатывать
//    public boolean contains(int x, int y)
//    {
//        Ellipse2D.Float shape = new Ellipse2D.Float(0, 0,getWidth(), getHeight());
//        return shape.contains(x, y);
//    }

    static class Direction{

        static boolean LEFT=true;
        static boolean RIGHT=false;
    }

}
