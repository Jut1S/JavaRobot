package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import javax.swing.JPanel;

import State.AbstractWindow;
import log.BoundedLogWindowSource;
import log.LogChangeListener;
import log.LogEntry;



public class LogWindow extends AbstractWindow implements LogChangeListener
{
    private final BoundedLogWindowSource m_logSource;
    private final TextArea m_logContent;

    public LogWindow(BoundedLogWindowSource logSource) {
        super();

        setTitle("Окно логов");
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);

        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }
}