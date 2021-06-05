package Display;

import Graph.*;
import loadData.Data;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class Menu extends JPanel {

    private JComboBox<String> startingStationName, endingStationName;
    private GridLayout layout;

    public Menu(Data data) {
        super();

        this.startingStationName = new JComboBox<>();
        this.startingStationName.addItem("--Select the departure station");

        this.endingStationName = new JComboBox<>();
        this.endingStationName.addItem("--Select the ending station");

        fillComboBoxWithStationNames(data.getStationNames());

        layout = new GridLayout(2, 3);

        this.setSize(JFrame.MAXIMIZED_HORIZ, 50);
        this.setLayout(layout);
        this.add(new JLabel("Start from:"));
        this.add(new JLabel("End to:"));
        this.add(startingStationName);
        this.add(endingStationName);
    }

    private void fillComboBoxWithStationNames(Set<String> stations) {
        for (String station : stations) {
            this.startingStationName.addItem(station);
            this.endingStationName.addItem(station);
        }
    }
}
