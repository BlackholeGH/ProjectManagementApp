import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private HashMap<String, Task> projectTaskLookup = new HashMap<>();
    private HashMap<String, Team> teamsLookup = new HashMap<>();
    private Task workingAssociatedTask = new Task("Undefined task name", "Undefined task description", 0);
    private Task precedingTask = null;
    JDialog frame;
    Project taskProject;
    Task lastSavedTask = null;

    private void updateTextPane()
    {
        taskDetailsPane.setText(workingAssociatedTask.displaySummary());
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
        for(int i = 0; i < TeamKt.getTeamsMap().size(); i++)
        {
            Team t = (Team)TeamKt.getTeamsMap().values().toArray()[i];
            teamsLookup.put(t.getTeamName() + " (" + t.getTeamID() + ")", t);
        }
        String[] teamSelectStrings = teamsLookup.keySet().toArray(new String[0]);
        ComboBoxModel<String> cbm2 = new DefaultComboBoxModel<String>(teamSelectStrings);
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
                if(taskTeamComboBox.getItemAt(i).equals(taskTeamLast.getTeamName() + " (" + taskTeamLast.getTeamID() + ")"))
                {
                    teamCBIndex = i;
                    break;
                }
            }
            taskTeamComboBox.setSelectedItem(teamCBIndex);
            for(int i = 0; i < projectTaskLookup.values().size(); i++)
            {
                String key = projectTaskLookup.keySet().toArray(new String[0])[i];
                if(projectTaskLookup.get(key).getFollowingTasks().contains(lastSavedTask))
                {
                    selectPrecedingTaskCBox.setSelectedItem(key);
                }
            }
            updateTextPane();
        }
        frame.setVisible(true);
    }

    public AddProjectTask() {
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
                    workingAssociatedTask = new Task(temp.getTaskName(), temp.getTaskDescription(), temp.getTaskLength());
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
                        workingAssociatedTask = temp2;
                    }
                });
                tte.show(frame);
                tte.ingestTask(workingAssociatedTask);
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
                    taskProject.removeTask(lastSavedTask);
                }
                lastSavedTask = new Task(workingAssociatedTask.getTaskName(), workingAssociatedTask.getTaskDescription(), workingAssociatedTask.getTaskLength());
                lastSavedTask.setTaskTeam(workingAssociatedTask.getTaskTeam());
                lastSavedTask.setTaskProgress(workingAssociatedTask.getTaskProgress());
                for(int i = 0; i < workingAssociatedTask.getFollowingTasks().size(); i++)
                {
                    lastSavedTask.getFollowingTasks().add(workingAssociatedTask.getFollowingTasks().get(i));
                }
                taskProject.addTask(lastSavedTask, lastSavedTask.getTaskTeam(), precedingTask);
                if(formerPrecedingTask != null)
                {
                    for(int i = 0; i < lastSavedTask.getFollowingTasks().size(); i++)
                    {
                        if(formerPrecedingTask.getFollowingTasks().contains(lastSavedTask.getFollowingTasks().get(i)))
                        {
                            formerPrecedingTask.getFollowingTasks().remove(lastSavedTask.getFollowingTasks().get(i));
                        }
                    }
                }
            }
        });
        taskTeamComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                workingAssociatedTask.setTaskTeam(teamsLookup.get(taskTeamComboBox.getSelectedItem()));
            }
        });
    }
}
