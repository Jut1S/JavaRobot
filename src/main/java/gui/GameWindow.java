package gui;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import javax.swing.JPanel;

import State.AbstractWindow;
import model.RobotsLogic;

public class GameWindow extends AbstractWindow implements PropertyChangeListener
{

    private final RobotsLogic logic;

    public GameWindow(RobotsLogic logic) {
        super();

        this.logic = logic;

        logic.startTimer();

        setTitle("Игровое окно");
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);

        GameVisualizer gameVisualizer = new GameVisualizer(logic);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(gameVisualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    public void dispose() {
        super.dispose();

        logic.stopTimer();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if("changeLocale".equals(evt.getPropertyName())){
            ResourceBundle bundle = (ResourceBundle)evt.getNewValue();
            setTitle(bundle.getString("NewGameWindow"));
        }
    }
}