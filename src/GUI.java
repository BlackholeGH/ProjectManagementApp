import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

/**
 * @author Rishaban and Daniel
 */
public class GUI extends JFrame implements ActionListener, KeyListener {

    private JTextArea projectDetails;
    private JTextArea projectNameDetails;
    private JTextField pName;
    private JTextField pProgress;
    private JTextField tSeq;
    private JTextField tSeqProg;
    private JTextField tDuration;
    private JTextField tSetup;
    private JScrollPane projectListPane;
    private JList projectList;
    //hello

    public void updateProjectList()
    {
        String selection = "";
        if(projectList != null) {
            selection = (String) projectList.getSelectedValue();
        }
        DefaultListModel<String> listValues = new DefaultListModel<>();
        for(int i = 0; i < ProjectKt.getStoredProjects().size(); i++)
        {
            Project tp = ProjectKt.getStoredProjects().get(i);
            listValues.addElement(tp.getProjectName());
        }
        projectList = new JList<>(listValues);
        if(listValues.contains(selection) && !selection.isEmpty()) { projectList.setSelectedValue(selection, true); }
        projectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        projectListPane.setViewportView(projectList);
    }

    public static void main(String[] args) {
        new GUI();
        Team.Companion.loadTeams();
        Task.Companion.loadTaskTemplates();
    }

    public GUI() {
        gui();
    }

    //GUI's main content
    public final void gui() {
        setTitle("Project Management System");
        setMinimumSize(new Dimension(1200, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel commonPanel = new JPanel(new BorderLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));

        JMenuBar menuBar = new JMenuBar();
        JMenu taskOptions = new JMenu();
        JMenu projectOptions = new JMenu();
        JMenu teamOptions = new JMenu();

        JMenu options = new JMenu();
        JLabel title = new JLabel();

        //Menu bar options
        setJMenuBar(menuBar);

        projectOptions = new JMenu("Project Options");
        projectOptions.add(makeMenuItem("Add Project", "addProject"));
        projectOptions.addSeparator();
        projectOptions.add(makeMenuItem("Remove Project", "removeProject"));
        projectOptions.addSeparator();
        projectOptions.add(makeMenuItem("Edit Project Details", "editProject"));
        menuBar.add(projectOptions);

        teamOptions = new JMenu("Team Options");
        teamOptions.add(makeMenuItem("Add Team", "addTeam"));
        teamOptions.addSeparator();
        teamOptions.add(makeMenuItem("Edit Team", "editTeam"));
        teamOptions.addSeparator();
        teamOptions.add(makeMenuItem("Remove Team", "removeTeam"));
        menuBar.add(teamOptions);

        taskOptions = new JMenu("Task Options");
        taskOptions.add(makeMenuItem("Create task template", "addTask"));
        taskOptions.addSeparator();
        taskOptions.add(makeMenuItem("Delete task template", "removeTask"));
        taskOptions.addSeparator();
        taskOptions.add(makeMenuItem("Edit task template", "editTask"));
        menuBar.add(taskOptions);

        options = new JMenu("Options");
        options.add(makeMenuItem("Help", "Help"));
        options.addSeparator();
        options.add(makeMenuItem("Clear Window", "CW"));
        options.addSeparator();
        options.add(makeMenuItem("Exit", "Exit"));

        menuBar.add(options);

        title = new JLabel("Project Management System Database:");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setHorizontalAlignment(JLabel.CENTER);
        commonPanel.add(title, BorderLayout.NORTH);

        //Text area that stores project and task info
        projectDetails = new JTextArea();
        projectDetails.setEditable(false);
        JScrollPane sp = new JScrollPane(projectDetails);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        centerPanel.add(sp);
        commonPanel.add(centerPanel, BorderLayout.CENTER);

        JLabel projectList = new JLabel("Select Project:");
        projectList.setFont(new Font("Arial", Font.BOLD, 18));
        projectList.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(projectList);

        projectListPane = new JScrollPane();
        updateProjectList();
        leftPanel.add(projectListPane);

        JPanel projectEditButtonPanel = new JPanel(new FlowLayout());

        JButton addProject = new JButton("Add Project");
        addProject.setActionCommand("addProject");
        addProject.addActionListener(this);
        addProject.setPreferredSize(new Dimension(120, 30));
        projectEditButtonPanel.add(addProject);

        JButton editProject = new JButton("Edit Project Details");
        editProject.setActionCommand("editDetails");
        editProject.addActionListener(this);
        editProject.setPreferredSize(new Dimension(120, 30));
        projectEditButtonPanel.add(editProject);
        //projectEditButtonPanel.add(, BorderLayout.SOUTH);

        leftPanel.add(projectEditButtonPanel);

        leftPanel.setPreferredSize(new Dimension(300, 500));

        commonPanel.add(leftPanel, BorderLayout.WEST);

        JLabel teamTitle = new JLabel();
        teamTitle = new JLabel("Team Details:");
        teamTitle.setFont(new Font("Arial", Font.BOLD, 18));
        teamTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(teamTitle);

        // Team Details displays a table of two columns, team names and number of team members

        JTable teamTable;

        String[] teamColumns = {"Team Names", "Number of Team Members"};

        //Defined a multidimensional array to store values at a specific
        //destination
        Object[][] data = {
                {"",""}

        };

        teamTable = new JTable(data, teamColumns);
        //Sets the size of the table
        teamTable.setPreferredScrollableViewportSize(new Dimension(300, 360));
        JScrollPane jsp = new JScrollPane(teamTable);
        jsp.setBounds(teamTable.getBounds());

        rightPanel.add(jsp);
        rightPanel.setPreferredSize(new Dimension(300, 500));

        commonPanel.add(rightPanel, BorderLayout.EAST);

        add(commonPanel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("Help".equals(e.getActionCommand())) {
            JOptionPane.showMessageDialog(null, "");
        }

        if ("CW".equals(e.getActionCommand())) {
            projectDetails.setText("");
        }

        if ("Exit".equals(e.getActionCommand())) {
            System.exit(0);
        }

        if ("exitTeams".equals(e.getActionCommand())) {

        }

        if ("submitButton".equals(e.getActionCommand())) {
            projectNameDetails.append("Project Name: " + pName.getText() + "\n");
            projectNameDetails.append("Project Progress(%): " + pProgress.getText() + "%" + "\n");
            projectNameDetails.append("\n");

            //projectDetails.append("Project Name: " + pName.getText() + "\n");
            //projectDetails.append("Project Progress(%): " + pProgress.getText() + "%" + "\n");
            projectDetails.append("Task Sequence: " + tSeq.getText() + "\n");
            projectDetails.append("Task Sequence Progress(%): " + tSeqProg.getText() + "%" + "\n");
            projectDetails.append("Task Duration (mins): " + tDuration.getText() + " mins" + "\n");
            projectDetails.append("Team Name: " + tSetup.getText() + "\n");
            projectDetails.append("\n");

            pName.setText("");
            pProgress.setText("");
            tSeq.setText("");
            tSeqProg.setText("");
            tDuration.setText("");
            tSetup.setText("");
        }

        if ("addProject".equals(e.getActionCommand())) {
            ProjectEditor pe = new ProjectEditor(null, this);
            pe.show();
        }

        if ("editProject".equals(e.getActionCommand())) {
            Hashtable<String, Project> projectIdentifiers = new Hashtable<>();
            for(int i = 0; i < ProjectKt.getStoredProjects().size(); i++)
            {
                Project thisProject = ProjectKt.getStoredProjects().get(i);
                projectIdentifiers.putIfAbsent(thisProject.getProjectName(), thisProject);
            }
            String[] selectors = projectIdentifiers.keySet().toArray(new String[0]);
            if(selectors.length == 0)
            {
                JOptionPane.showMessageDialog(null, "There are no projects to edit.");
            }
            else {
                String selection = (String) JOptionPane.showInputDialog(null, "Choose a project to edit.", "Selection dialog", JOptionPane.QUESTION_MESSAGE, null, selectors, selectors[0]);
                if(selection != null && !selection.isEmpty()) {
                    ProjectEditor pe = new ProjectEditor(projectIdentifiers.get(selection), this);
                    pe.show();
                }
            }
        }

        if("editDetails".equals(e.getActionCommand()))
        {
            if(projectList != null) {
                Hashtable<String, Project> projectIdentifiers = new Hashtable<>();
                for (int i = 0; i < ProjectKt.getStoredProjects().size(); i++) {
                    Project thisProject = ProjectKt.getStoredProjects().get(i);
                    projectIdentifiers.putIfAbsent(thisProject.getProjectName(), thisProject);
                }
                String selVal = (String) projectList.getSelectedValue();
                if(selVal != null && projectIdentifiers.containsKey(selVal))
                {
                    ProjectEditor pe = new ProjectEditor(projectIdentifiers.get(selVal), this);
                    pe.show();
                }
            }
        }

        if ("removeProject".equals(e.getActionCommand())) {
            Hashtable<String, Project> projectIdentifiers = new Hashtable<>();
            for(int i = 0; i < ProjectKt.getStoredProjects().size(); i++)
            {
                Project thisProject = ProjectKt.getStoredProjects().get(i);
                projectIdentifiers.putIfAbsent(thisProject.getProjectName(), thisProject);
            }
            String[] selectors = projectIdentifiers.keySet().toArray(new String[0]);
            if(selectors.length == 0)
            {
                JOptionPane.showMessageDialog(null, "There are no projects to delete.");
            }
            else {
                String selection = (String) JOptionPane.showInputDialog(null, "Choose a project to delete.", "Selection dialog", JOptionPane.QUESTION_MESSAGE, null, selectors, selectors[0]);
                ProjectKt.getStoredProjects().remove(selection);
                updateProjectList();
            }
        }

        if ("addTeam".equals(e.getActionCommand())) {
            TeamEditor te = new TeamEditor();
            te.showTeamEditor(null);
        }

        if ("editTeam".equals(e.getActionCommand())) {
            Team[] teamR  = TeamKt.getTeamsMap().values().toArray(new Team[TeamKt.getTeamsMap().size()]);
            String[] teamNames = new String[teamR.length];
            for(int i = 0; i < teamR.length; i++)
            {
                teamNames[i] = teamR[i].getTeamName();
            }
            if(teamNames.length == 0)
            {
                JOptionPane.showMessageDialog(null, "There are no teams to edit.");
            }
            else {
                String selection = (String) JOptionPane.showInputDialog(null, "Choose a team to edit.", "Selection dialog", JOptionPane.QUESTION_MESSAGE, null, teamNames, teamNames[0]);
                int truei = -1;
                for(int i = 0; i < teamR.length; i++)
                {
                    if(teamNames[i].equals(selection)) {
                        truei = i;
                        break;
                    }
                }
                if(truei >= 0) {
                    Team editTeam = teamR[truei];
                    TeamEditor te = new TeamEditor();
                    te.showTeamEditor(editTeam);
                }
            }
        }
        if ("removeTeam".equals(e.getActionCommand())) {
            Team[] teamR  = TeamKt.getTeamsMap().values().toArray(new Team[TeamKt.getTeamsMap().size()]);
            String[] teamNames = new String[teamR.length];
            for(int i = 0; i < teamR.length; i++)
            {
                teamNames[i] = teamR[i].getTeamName();
            }
            if(teamNames.length == 0)
            {
                JOptionPane.showMessageDialog(null, "There are no teams to delete.");
            }
            else {
                String selection = (String) JOptionPane.showInputDialog(null, "Choose a team to delete.", "Selection dialog", JOptionPane.QUESTION_MESSAGE, null, teamNames, teamNames[0]);
                int truei = -1;
                for(int i = 0; i < teamR.length; i++)
                {
                    if(teamNames[i].equals(selection)) {
                        truei = i;
                        break;
                    }
                }
                if(truei >= 0) {
                    Team editTeam = teamR[truei];
                    TeamKt.getTeamsMap().remove(editTeam.getTeamID());
                    Team.Companion.saveTeams();
                }
            }
        }

        if ("addTask".equals(e.getActionCommand())) {
            TaskTemplateEditor tte = new TaskTemplateEditor(false);
            tte.show(this);
        }
        if ("editTask".equals(e.getActionCommand())) {
            String[] taskTemplates = TaskKt.getTemplateTasks().keySet().toArray(new String[TaskKt.getTemplateTasks().size()]);
            if(taskTemplates.length == 0)
            {
                JOptionPane.showMessageDialog(null, "There are no task templates to edit.");
            }
            else {
                String selection = (String) JOptionPane.showInputDialog(null, "Choose a task template to edit.", "Selection dialog", JOptionPane.QUESTION_MESSAGE, null, taskTemplates, taskTemplates[0]);
                TaskTemplateEditor tte = new TaskTemplateEditor(true);
                tte.getTaskButton().addActionListener(new ActionListener() {
                    String tSelection = selection;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        TaskKt.getTemplateTasks().remove(tSelection);
                        Task newSaveTask = tte.generateTask();
                        TaskKt.getTemplateTasks().put(newSaveTask.getTaskName(), newSaveTask);
                        tSelection = newSaveTask.getTaskName();
                        Task.Companion.saveTaskTemplates();
                    }
                });
                tte.show(this);
                tte.ingestTask(TaskKt.getTemplateTasks().get(selection));
            }
        }
        if("removeTask".equals(e.getActionCommand()))
        {
            String[] taskTemplates = TaskKt.getTemplateTasks().keySet().toArray(new String[TaskKt.getTemplateTasks().size()]);
            if(taskTemplates.length == 0)
            {
                JOptionPane.showMessageDialog(null, "There are no task templates to remove.");
            }
            else {
                String selection = (String) JOptionPane.showInputDialog(null, "Choose a task template to delete.", "Selection dialog", JOptionPane.QUESTION_MESSAGE, null, taskTemplates, taskTemplates[0]);
                TaskKt.getTemplateTasks().remove(selection);
                Task.Companion.saveTaskTemplates();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @param txt
     * @param actionCommand
     * @return
     */

    //Method to create menu option and action command
    protected JMenuItem makeMenuItem(
            String txt,
            String actionCommand) {

        JMenuItem menuItem = new JMenuItem();
        menuItem.setText(txt);
        menuItem.setActionCommand(actionCommand);
        menuItem.addActionListener(this);

        return menuItem;
    }

    //Method to create button and action command
    protected JButton makeButton(
            String txt,
            String actionCommand) {

        JButton button = new JButton();
        button.setText(txt);
        button.setActionCommand(actionCommand);
        button.addActionListener(this);

        return button;
    }
}
