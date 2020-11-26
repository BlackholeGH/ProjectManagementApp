import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.DecimalFormat;
import java.util.Hashtable;

public class ProjectEditor {
    private JList taskSelectList;
    private JButton addTaskButton;
    private JButton deleteTaskButton;
    private JButton editTaskButton;
    private JButton saveProjectButton;
    private JButton exitProjectDesignerButton;
    private JLabel projectLengthString;
    private JScrollPane taskScrollPane;
    private JPanel projectEditorPanel;
    private JTextField projectNameField;
    private Hashtable<String, Task> listEntryIdentifier;
    private Project lastSavedProject;
    private Project associatedProject;
    private JFrame frame;
    private Long lastTimeR = -1L;

    public void updateRemainingLength()
    {
        Long timeR = associatedProject.getRemainingTime();
        String readableTime = Task.Companion.getLengthAsReadableTime(timeR);
        projectLengthString.setText("Project time remaining: " + readableTime);
        if(timeR > lastTimeR && lastTimeR >= 0)
        {
            JOptionPane.showMessageDialog(null, "A change was made to this project that increased its projected time remaining per the critical path. The new projected time for completion is: " + readableTime);
        }
        lastTimeR = timeR;
    }

    public void show() {
        frame = new JFrame("Project editor");
        frame.setContentPane(projectEditorPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        updateRemainingLength();
        frame.setVisible(true);
    }

    public ListModel<String> populateTaskSelector(Project popProject)
    {
        DefaultListModel<String> outModel = new DefaultListModel<String>();
        if(listEntryIdentifier == null) { listEntryIdentifier = new Hashtable<>(); }
        listEntryIdentifier.clear();
        DecimalFormat df = new DecimalFormat("###.##");
        for(int i = 0; i < popProject.getProjectTasks().size(); i++)
        {
            Task thisTask = popProject.getProjectTasks().get(i);
            String taskDescriptor = thisTask.getTaskName() + " (" + Task.Companion.getLengthAsReadableTime(thisTask.getTaskLength()) + " total, " + df.format(thisTask.getTaskProgress() * 100) + "% complete)";
            outModel.add(i, taskDescriptor);
            listEntryIdentifier.put(taskDescriptor, thisTask);
        }
        return outModel;
    }

    public ProjectEditor(Project preExistingProject, GUI myGUI) {
        ProjectEditor prEd = this;
        if(preExistingProject != null)
        {
            lastSavedProject = preExistingProject;
            associatedProject = preExistingProject.clone();
            taskSelectList.setModel(populateTaskSelector(associatedProject));
            projectNameField.setText(associatedProject.getProjectName());
        }
        else
        {
            associatedProject = new Project("Default project name");
        }
        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddProjectTask apt = new AddProjectTask(prEd);
                apt.show(frame, associatedProject, null);
                taskSelectList.setModel(populateTaskSelector(associatedProject));
            }
        });
        editTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String etIndex = (String)taskSelectList.getSelectedValue();
                if(etIndex != null && !etIndex.isEmpty()) {
                    Task editTask = listEntryIdentifier.get(etIndex);
                    if(editTask != null) {
                        AddProjectTask apt = new AddProjectTask(prEd);
                        apt.show(frame, associatedProject, editTask);
                        taskSelectList.setModel(populateTaskSelector(associatedProject));
                    }
                }
            }
        });
        deleteTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                associatedProject.removeTask(listEntryIdentifier.get(taskSelectList.getSelectedValue()), true);
                taskSelectList.setModel(populateTaskSelector(associatedProject));
                updateRemainingLength();
            }
        });
        saveProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(lastSavedProject != null) {
                    ProjectKt.getStoredProjects().remove(lastSavedProject);
                }
                ProjectKt.getStoredProjects().add(associatedProject);
                lastSavedProject = associatedProject;
                associatedProject = associatedProject.clone();
                Project.Companion.saveProjects();
                myGUI.updateProjectList();
                myGUI.updateDisplay();
                taskSelectList.setModel(populateTaskSelector(associatedProject));
                JOptionPane.showMessageDialog(null,"Project information saved.");
            }
        });
        exitProjectDesignerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        projectNameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                associatedProject.setProjectName(projectNameField.getText());
            }
        });
        projectNameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                projectNameField.postActionEvent();
            }
        });
    }
}
