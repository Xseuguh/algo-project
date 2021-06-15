package Display;

import Graph.Vertex;
import loadData.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.function.Consumer;

public class ScrollingMenu extends JPanel {

    private JComboBox<String> scrollMenuList;

    public ScrollingMenu(Data data, Consumer<Vertex> update, String defaultValue, String title) {
        super();

        this.scrollMenuList = new JComboBox<>();
        this.scrollMenuList.addItem(defaultValue);

        for (String value : data.getStationNames()) {
            this.scrollMenuList.addItem(value);
        }

        this.scrollMenuList.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String stationName = (String) e.getItem();
                Vertex station = data.getVertexFromStationName(stationName);
                update.accept(station);
            }
        });

        this.add(new JLabel(title));
        this.add(scrollMenuList);

        this.setBackground(Color.WHITE);
    }

    public void setSelectedItem(String stationName) {
        this.scrollMenuList.setSelectedItem(stationName);
    }
}
