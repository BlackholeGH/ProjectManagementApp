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
    private JFrame teamDetails;
    //hello

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
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel rightPanel = new JPanel();
        JPanel leftPanel = new JPanel();

        JFrame teamDetails = new JFrame();

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
        add(title, BorderLayout.NORTH);

        //Text area that stores project and task info
        projectDetails = new JTextArea();
        projectDetails.setEditable(false);
        rightPanel.add(projectDetails);
        add(rightPanel,BorderLayout.EAST);

        JLabel projectList = new JLabel("Select Project:");
        projectList.setFont(new Font("Arial", Font.BOLD, 15));
        leftPanel.add(projectList);

        //Dummy list of values stored inside an arraylist to check if JList is working.

        DefaultListModel<String> listValues = new DefaultListModel<>();

        listValues.addElement("Project 1");
        listValues.addElement("Project 2");
        listValues.addElement("Project 3");
        listValues.addElement("Project 4");
        listValues.addElement("Project 5");
        listValues.addElement("Project 6");
        listValues.addElement("Project 7");
        listValues.addElement("Project 8");
        listValues.addElement("Project 9");
        listValues.addElement("Project 10");

        JList<String> projectsList = new JList<>(listValues);
        projectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        leftPanel.add(new JScrollPane(projectsList));
        add(leftPanel, BorderLayout.WEST);


        //String projects[] = {"Project 1", "Project 2", "Project 3", "Project 4", "Project 5"};
        //JList projectsList = new JList(projects);
        //projectsList.setSelectedIndex(0);
        //projectsList.addListSelectionListener();
        //leftPanel.add(projectsList);
        //add(leftPanel, BorderLayout.WEST);

        //Scrollbar
        JScrollPane sp = new JScrollPane(projectDetails);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(sp);
        setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton addProject = new JButton("Add Project");
        addProject.setActionCommand("addProject");
        addProject.addActionListener(this);
        panel.add(addProject);

        JButton editProject = new JButton("Edit Project Details");
        editProject.setActionCommand("editProject");
        editProject.addActionListener(this);
        panel.add(editProject);

        JButton clearWindow = new JButton("Clear Window");
        clearWindow.setActionCommand("CW");
        clearWindow.addActionListener(this);
        panel.add(clearWindow);

        add(panel, BorderLayout.SOUTH);

        //JFrame projectNames = new JFrame();
        //projectNames.setTitle("Project Details");
        //projectNames.setSize(500,500);
        //projectNames.setLayout(new BorderLayout());
        //projectNames.setLocation(122,270);

        //JLabel projectTitle = new JLabel();
        //projectTitle = new JLabel("Project Names:");
        //projectTitle.setFont(new Font("Arial", Font.BOLD, 30));
        //projectTitle.setHorizontalAlignment(JLabel.CENTER);
        //projectNames.add(projectTitle, BorderLayout.NORTH);
        
        //projectNameDetails = new JTextArea();
        //projectNameDetails.setEditable(false);
        //projectNames.add(projectNameDetails);

        //JScrollPane sp2 = new JScrollPane(projectNameDetails);
        //sp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //projectNames.add(sp2);


        teamDetails.setTitle("Team Details");
        teamDetails.setSize(550,500);
        teamDetails.setLayout(new BorderLayout());
        teamDetails.setLocation(1298,270);

        JLabel teamTitle = new JLabel();
        teamTitle = new JLabel("Team Details:");
        teamTitle.setFont(new Font("Arial", Font.BOLD, 30));
        teamTitle.setHorizontalAlignment(JLabel.CENTER);
        teamDetails.add(teamTitle, BorderLayout.NORTH);

        // Team Details displays a table of two columns, team names and number of team members

        JPanel teamPanel = new JPanel();
        teamDetails.add(teamPanel);
        JTable teamTable;

        String[] teamColumns = {"Team Names", "Number of Team Members"};

        //Defined a multidimensional array to store values at a specific
        //destination
        Object[][] data = {
                {"",""}

        };

        teamTable = new JTable(data, teamColumns);
        //Sets the size of the table
        teamTable.setPreferredScrollableViewportSize(new Dimension(600, 360));
        //Ensures the table is set to true and visible to GUI
        teamTable.setFillsViewportHeight(true);
        JScrollPane jsp = new JScrollPane(teamTable);
        jsp.setBounds(teamTable.getBounds());

        teamPanel.add(jsp);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton exitTeams = new JButton("Exit Team Details");
        exitTeams.setActionCommand("exitTeams");
        exitTeams.addActionListener(this);
        bottomPanel.add(exitTeams);
        teamDetails.add(bottomPanel, BorderLayout.SOUTH);

        teamDetails.setVisible(true);

        //JPanel bottomPanel2 = new JPanel();
        //bottomPanel2.setLayout(new FlowLayout(FlowLayout.CENTER));
        //JButton addProject = new JButton("Add Project");
        //addProject.setActionCommand("addProject");
        //Sorry Rishi, I had to switch our your panel here for my own project GUI form to make the system work right
        //addProject.addActionListener(this);
        //bottomPanel2.add(addProject);

        //JButton editProject = new JButton("Edit Project Details");
        //editProject.setActionCommand("editProject");
        //editProject.addActionListener(this);
        //bottomPanel2.add(editProject);
        //projectNames.add(bottomPanel2, BorderLayout.SOUTH);

        //projectNames.setVisible(true);
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
            ProjectEditor pe = new ProjectEditor(null);
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
                ProjectEditor pe = new ProjectEditor(projectIdentifiers.get(selection));
                pe.show();
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
            }
        }

        if ("editDetails".equals(e.getActionCommand())) {

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
            tte.show();
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
                tte.show();
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
