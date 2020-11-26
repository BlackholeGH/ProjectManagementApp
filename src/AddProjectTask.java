import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

public class AddProjectTask {
    private JPanel addProjectTaskPanel;
    private JButton selectTemplateTaskButton;
    private JButton editTaskDetailsButton;
    private JButton cancelButton;
    private JButton addTaskButton;
    private JTextPane taskDetailsPane;
    private JComboBox selectPrecedingTaskCBox;
    private JComboBox taskTeamComboBox;
    private JTextField taskProgressField;
    private HashMap<String, Task> projectTaskLookup = new HashMap<>();
    private HashMap<String, Team> teamsLookup = new HashMap<>();
    private Task workingAssociatedTask = new Task("Undefined task name", "Undefined task description", 0);
    private Task precedingTask = null;
    JDialog frame;
    Project taskProject;
    Task lastSavedTask = null;

    public void updateTextPane()
    {
        taskDetailsPane.setText(workingAssociatedTask.displaySummary());
        taskProgressField.setText(String.format("%.2f", workingAssociatedTask.getTaskProgress()));
    }

    public void show(JFrame parentFrame, Project project, Task editableTask) {
        taskProject = project;
        frame = new JDialog(parentFrame, "Add task dialog", true);
        frame.setContentPane(addProjectTaskPanel);
        frame.pack();
        projectTaskLookup.put("No preceding task", null);
        for(int i = 0; i < taskProject.getProjectTasks().size(); i++)
        {
            Task t = taskProject.getProjectTasks().get(i);
            projectTaskLookup.put(t.getTaskName() + " (" + t.getTaskID() + ")", t);
        }
        String[] precTaskSelectStrings = projectTaskLookup.keySet().toArray(new String[0]);
        ComboBoxModel<String> cbm = new DefaultComboBoxModel<String>(precTaskSelectStrings);
        selectPrecedingTaskCBox.setModel(cbm);
        selectPrecedingTaskCBox.setSelectedItem("No preceding task");
        for(int i = 0; i < TeamKt.getTeamsMap().size(); i++)
        {
            Team t = (Team)TeamKt.getTeamsMap().values().toArray()[i];
            teamsLookup.put(t.getTeamName() + " (" + t.getTeamID() + ")", t);
        }
        String[] teamSelectStrings = teamsLookup.keySet().toArray(new String[0]);
        ComboBoxModel<String> cbm2 = new DefaultComboBoxModel<String>(teamSelectStrings);
        taskTeamComboBox.setModel(cbm2);
        if(editableTask != null)
        {
            lastSavedTask = editableTask;
            workingAssociatedTask = new Task(lastSavedTask.getTaskName(), lastSavedTask.getTaskDescription(), lastSavedTask.getTaskLength());
            Team taskTeamLast = lastSavedTask.getTaskTeam();
            workingAssociatedTask.setTaskTeam(taskTeamLast);
            workingAssociatedTask.setTaskProgress(lastSavedTask.getTaskProgress());
            for(int i = 0; i < lastSavedTask.getFollowingTasks().size(); i++)
            {
                workingAssociatedTask.getFollowingTasks().add(lastSavedTask.getFollowingTasks().get(i));
            }
            int teamCBIndex = -1;
            for(int i = 0; i < taskTeamComboBox.getItemCount(); i++)
            {
                if(taskTeamLast != null && taskTeamComboBox.getItemAt(i).equals(taskTeamLast.getTeamName() + " (" + taskTeamLast.getTeamID() + ")"))
                {
                    teamCBIndex = i;
                    break;
                }
            }
            taskTeamComboBox.setSelectedItem(teamCBIndex);
            for(int i = 0; i < projectTaskLookup.keySet().size(); i++)
            {
                String key = projectTaskLookup.keySet().toArray(new String[0])[i];
                if(projectTaskLookup.get(key) != null && projectTaskLookup.get(key).getFollowingTasks().contains(lastSavedTask))
                {
                    selectPrecedingTaskCBox.setSelectedItem(key);
                }
            }
            updateTextPane();
        }
        frame.setVisible(true);
    }

    public AddProjectTask(ProjectEditor projEd) {
        AddProjectTask apt = this;
        selectPrecedingTaskCBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                precedingTask = projectTaskLookup.get(selectPrecedingTaskCBox.getSelectedItem());
            }
        });
        selectTemplateTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] taskTemplates = TaskKt.getTemplateTasks().keySet().toArray(new String[TaskKt.getTemplateTasks().size()]);
                if(taskTemplates.length == 0)
                {
                    JOptionPane.showMessageDialog(null, "There are no task templates to use.");
                }
                else {
                    String selection = (String) JOptionPane.showInputDialog(null, "Choose a task template clone.", "Selection dialog", JOptionPane.QUESTION_MESSAGE, null, taskTemplates, taskTemplates[0]);
                    Task temp = TaskKt.getTemplateTasks().get(selection);
                    Task realTemp = new Task(temp.getTaskName(), temp.getTaskDescription(), temp.getTaskLength());
                    realTemp.setTaskProgress(workingAssociatedTask.getTaskProgress());
                    for(int i = 0; i < workingAssociatedTask.getFollowingTasks().size(); i++)
                    {
                        realTemp.getFollowingTasks().add(workingAssociatedTask.getFollowingTasks().get(i));
                    }
                    workingAssociatedTask = realTemp;
                    updateTextPane();
                }
            }
        });
        editTaskDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TaskTemplateEditor tte = new TaskTemplateEditor(true);
                tte.getTaskButton().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Task temp2 = tte.generateTask();
                        temp2.setTaskTeam(workingAssociatedTask.getTaskTeam());
                        temp2.setTaskProgress(workingAssociatedTask.getTaskProgress());
                        for(int i = 0; i < workingAssociatedTask.getFollowingTasks().size(); i++)
                        {
                            temp2.getFollowingTasks().add(workingAssociatedTask.getFollowingTasks().get(i));
                        }
                        workingAssociatedTask = temp2;
                        apt.updateTextPane();
                    }
                });
                tte.showAndIngestTask(frame, workingAssociatedTask);
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Task formerPrecedingTask = null;
                if(lastSavedTask != null) {
                    for(int i = 0; i < taskProject.getProjectTasks().size(); i++)
                    {
                        if(taskProject.getProjectTasks().get(i).getFollowingTasks().contains(lastSavedTask))
                        {
                            formerPrecedingTask = taskProject.getProjectTasks().get(i);
                            break;
                        }
                    }
                    taskProject.removeTask(lastSavedTask, false);
                    if(formerPrecedingTask != null) { formerPrecedingTask.getFollowingTasks().remove(lastSavedTask); }
                }
                lastSavedTask = new Task(workingAssociatedTask.getTaskName(), workingAssociatedTask.getTaskDescription(), workingAssociatedTask.getTaskLength());
                if(workingAssociatedTask.getTaskTeam() == null)
                {
                    workingAssociatedTask.setTaskTeam(teamsLookup.get(taskTeamComboBox.getSelectedItem()));
                }
                lastSavedTask.setTaskTeam(workingAssociatedTask.getTaskTeam());
                lastSavedTask.setTaskProgress(workingAssociatedTask.getTaskProgress());
                for(int i = 0; i < workingAssociatedTask.getFollowingTasks().size(); i++)
                {
                    lastSavedTask.getFollowingTasks().add(workingAssociatedTask.getFollowingTasks().get(i));
                }
                taskProject.addTask(lastSavedTask, lastSavedTask.getTaskTeam(), precedingTask);
                projEd.populateTaskSelector(taskProject);
                projEd.updateRemainingLength();
                frame.dispose();
            }
        });
        taskTeamComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                workingAssociatedTask.setTaskTeam(teamsLookup.get(taskTeamComboBox.getSelectedItem()));
            }
        });
        taskProgressField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float prog = Float.parseFloat(taskProgressField.getText()) / 100f;
                workingAssociatedTask.setTaskProgress(prog);
            }
        });
        taskProgressField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                taskProgressField.postActionEvent();
            }
        });
    }
}
