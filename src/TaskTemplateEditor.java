import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TaskTemplateEditor {
    private JPanel taskTemplatePanel;
    private JTextField taskNameField;
    private JTextArea taskDescriptionArea;
    private JTextField weekField;
    private JTextField dayField;
    private JTextField hourField;
    private JTextField minuteField;
    private JTextField secondField;
    private JButton saveTaskButton;
    private JButton exitButton;
    protected int weekValue = 0;
    protected int dayValue = 0;
    protected int hourValue = 0;
    protected int minuteValue = 0;
    protected int secondValue = 0;
    protected String taskName = "Untitled task";
    private JDialog frame;
    private Boolean localTaskEdit = false;

    public void show(JFrame parent, Boolean trueShow) {
        frame = new JDialog(parent, "Task template editor");
        frame.setContentPane(taskTemplatePanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setModal(true);
        frame.pack();
        frame.setLocationByPlatform(true);
        if(trueShow) { frame.setVisible(true); }
    }

    public void show(JDialog parent) {
        frame = new JDialog(parent, "Task template editor");
        frame.setContentPane(taskTemplatePanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setModal(true);
        frame.pack();
        frame.setLocationByPlatform(true);
    }

    public void showAndIngestTask(JDialog parent, Task inTask) {
        show(parent);
        ingestTask(inTask);
        frame.setVisible(true);
    }

    public void showAndIngestTask(JFrame parent, Task inTask)
    {
        show(parent, false);
        ingestTask(inTask);
        frame.setVisible(true);
    }

    public Task generateTask()
    {
        return new Task(taskName, taskDescriptionArea.getText(), getTimeInSeconds());
    }

    public void ingestTask(Task inTask)
    {
        taskName = inTask.getTaskName();
        taskNameField.setText(inTask.getTaskName());
        taskDescriptionArea.setText(inTask.getTaskDescription());
        int[] timeVals = Task.Companion.getLengthAsTimeArray(inTask.getTaskLength());
        secondValue = timeVals[0];
        secondField.setText(timeVals[0] + "");
        minuteValue = timeVals[1];
        minuteField.setText(timeVals[1] + "");
        hourValue = timeVals[2];
        hourField.setText(timeVals[2] + "");
        dayValue = timeVals[3];
        dayField.setText(timeVals[3] + "");
        weekValue = timeVals[4];
        weekField.setText(timeVals[4] + "");
    }

    public long getTimeInSeconds()
    {
        return secondValue + (60 * minuteValue) + (3600 * hourValue) + (86400 * dayValue) + (604800 * weekValue);
    }

    public TaskTemplateEditor(Boolean editLocalTask) {
        localTaskEdit = editLocalTask;
        weekField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int localWeekVal = 0;
                Boolean failCondition = false;
                try {
                    localWeekVal = Integer.parseInt(((JTextField)e.getSource()).getText());
                }
                catch(NumberFormatException ex)
                {
                    failCondition = true;
                }
                if(localWeekVal < 0) { failCondition = true; }
                if(failCondition)
                {
                    JOptionPane.showMessageDialog(null, "Invalid value: Must be a positive integer.");
                    ((JTextField)e.getSource()).setText(weekValue + "");
                }
                else
                {
                    weekValue = localWeekVal;
                }
            }
        });
        weekField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }
            @Override
            public void focusLost(FocusEvent e) {
                weekField.postActionEvent();
            }
        });
        dayField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int localDayVal = 0;
                Boolean failCondition = false;
                try {
                    localDayVal = Integer.parseInt(((JTextField)e.getSource()).getText());
                }
                catch(NumberFormatException ex)
                {
                    failCondition = true;
                }
                if(localDayVal < 0) { failCondition = true; }
                if(failCondition)
                {
                    JOptionPane.showMessageDialog(null, "Invalid value: Must be a positive integer.");
                    ((JTextField)e.getSource()).setText(dayValue + "");
                }
                else
                {
                    int remainder = localDayVal % 7;
                    int nonRemainder = localDayVal - remainder;
                    dayValue = remainder;
                    if(nonRemainder > 0)
                    {
                        ((JTextField)e.getSource()).setText(dayValue + "");
                        weekField.setText(weekValue + (nonRemainder / 7) + "");
                        weekField.postActionEvent();
                    }
                }
            }
        });
        dayField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }
            @Override
            public void focusLost(FocusEvent e) {
                dayField.postActionEvent();
            }
        });
        hourField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int localHourVal = 0;
                Boolean failCondition = false;
                try {
                    localHourVal = Integer.parseInt(((JTextField)e.getSource()).getText());
                }
                catch(NumberFormatException ex)
                {
                    failCondition = true;
                }
                if(localHourVal < 0) { failCondition = true; }
                if(failCondition)
                {
                    JOptionPane.showMessageDialog(null, "Invalid value: Must be a positive integer.");
                    ((JTextField)e.getSource()).setText(hourValue + "");
                }
                else
                {
                    int remainder = localHourVal % 24;
                    int nonRemainder = localHourVal - remainder;
                    hourValue = remainder;
                    if(nonRemainder > 0)
                    {
                        ((JTextField)e.getSource()).setText(hourValue + "");
                        dayField.setText(dayValue + (nonRemainder / 24) + "");
                        dayField.postActionEvent();
                    }
                }
            }
        });
        hourField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }
            @Override
            public void focusLost(FocusEvent e) {
                hourField.postActionEvent();
            }
        });
        minuteField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int localMinuteVal = 0;
                Boolean failCondition = false;
                try {
                    localMinuteVal = Integer.parseInt(((JTextField)e.getSource()).getText());
                }
                catch(NumberFormatException ex)
                {
                    failCondition = true;
                }
                if(localMinuteVal < 0) { failCondition = true; }
                if(failCondition)
                {
                    JOptionPane.showMessageDialog(null, "Invalid value: Must be a positive integer.");
                    ((JTextField)e.getSource()).setText(minuteValue + "");
                }
                else
                {
                    int remainder = localMinuteVal % 60;
                    int nonRemainder = localMinuteVal - remainder;
                    minuteValue = remainder;
                    if(nonRemainder > 0)
                    {
                        ((JTextField)e.getSource()).setText(minuteValue + "");
                        hourField.setText(hourValue + (nonRemainder / 60) + "");
                        hourField.postActionEvent();
                    }
                }
            }
        });
        minuteField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }
            @Override
            public void focusLost(FocusEvent e) {
                minuteField.postActionEvent();
            }
        });
        secondField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int localSecondVal = 0;
                Boolean failCondition = false;
                try {
                    localSecondVal = Integer.parseInt(((JTextField)e.getSource()).getText());
                }
                catch(NumberFormatException ex)
                {
                    failCondition = true;
                }
                if(localSecondVal < 0) { failCondition = true; }
                if(failCondition)
                {
                    JOptionPane.showMessageDialog(null, "Invalid value: Must be a positive integer.");
                    ((JTextField)e.getSource()).setText(secondValue + "");
                }
                else
                {
                    int remainder = localSecondVal % 60;
                    int nonRemainder = localSecondVal - remainder;
                    secondValue = remainder;
                    if(nonRemainder > 0)
                    {
                        ((JTextField)e.getSource()).setText(secondValue + "");
                        minuteField.setText(minuteValue + (nonRemainder / 60) + "");
                        minuteField.postActionEvent();
                    }
                }
            }
        });
        secondField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }
            @Override
            public void focusLost(FocusEvent e) {
                secondField.postActionEvent();
            }
        });
        taskNameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taskName = ((JTextField)e.getSource()).getText();
            }
        });
        taskNameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }
            @Override
            public void focusLost(FocusEvent e) {
                taskNameField.postActionEvent();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        saveTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!localTaskEdit)
                {
                    Task genTask = generateTask();
                    if(!TaskKt.getTemplateTasks().containsKey(genTask.getTaskName()))
                    {
                        TaskKt.getTemplateTasks().put(genTask.getTaskName(), genTask);
                        Task.Companion.saveTaskTemplates();
                    }
                }
                JOptionPane.showMessageDialog(null, "Task information saved.");
            }
        });
    }
    public JButton getTaskButton()
    {
        return saveTaskButton;
    }
}
