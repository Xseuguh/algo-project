package Display;

import Graph.Graph;
import loadData.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class Display extends JFrame implements ComponentListener {

    public static final int MENU_HEIGHT = 100;
    private DrawSubway drawSubway;
    private Menu menu;

    public Display(String title, Graph graph, Data data) {
        super();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1000, 500));
        this.setTitle(title);
        this.setLocationRelativeTo(null);
        this.addComponentListener(this);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.drawSubway = new DrawSubway(graph, new int[]{this.getWidth(), this.getHeight()});
        this.menu = new Menu(data, graph, drawSubway);
        this.drawSubway.setMenu(menu);


        this.add(menu, BorderLayout.NORTH);
        this.add(drawSubway);

        this.setVisible(true);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        int height = this.getHeight();
        int width = this.getWidth();
        this.drawSubway.setSize(new int[]{width, height});
        this.drawSubway.refresh();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}
