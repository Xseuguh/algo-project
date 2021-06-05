package Display;

import Graph.Graph;
import loadData.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class Display extends JFrame implements ComponentListener {

    public static final int MENU_HEIGHT = 50;
    private DrawSubway drawSubway;
    private Menu menu;

    public Display(String title, Graph graph, Data data) {
        super();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(800, 500));
        this.setTitle(title);
        this.setLocationRelativeTo(null);
        this.addComponentListener(this);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        this.setUndecorated(true);

        this.menu = new Menu(data);
        this.add(menu, BorderLayout.NORTH);

        System.out.println(this.menu.getHeight());
        this.drawSubway = new DrawSubway(graph, data, new int[]{this.getWidth(), this.getHeight()});
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
