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

    public void show() {
        frame = new JFrame("Project editor");
        frame.setContentPane(projectEditorPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public ListModel<String> populateTaskSelector(Project popProject)
    {
        DefaultListModel<String> outModel = new DefaultListModel<String>();
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

    public ProjectEditor(Project preExistingProject) {
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
                AddProjectTask apt = new AddProjectTask();
                apt.show(frame, associatedProject, null);
                populateTaskSelector(associatedProject);
            }
        });
        editTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddProjectTask apt = new AddProjectTask();
                apt.show(frame, associatedProject, listEntryIdentifier.get(taskSelectList.getSelectedValue()));
                populateTaskSelector(associatedProject);
            }
        });
        deleteTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                associatedProject.removeTask(listEntryIdentifier.get(taskSelectList.getSelectedValue()));
                populateTaskSelector(associatedProject);
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
