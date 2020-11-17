import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
    //hello

    public static void main(String[] args) {
        new GUI();
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

        JMenuBar menuBar = new JMenuBar();
        JMenu taskOptions = new JMenu();
        JMenu projectOptions = new JMenu();
        JMenu teamOptions = new JMenu();

        JMenu options = new JMenu();
        JLabel title = new JLabel();

        //Menu bar options
        setJMenuBar(menuBar);

        //projectOptions = new JMenu("Project Options");
        //projectOptions.add(makeMenuItem("Add Project", "addProject"));
        //projectOptions.addSeparator();
        //projectOptions.add(makeMenuItem("Remove Project", "removeProject"));
        //projectOptions.addSeparator();
        //projectOptions.add(makeMenuItem("Edit Project Details", "editDetails"));
        //menuBar.add(projectOptions);

        teamOptions = new JMenu("Team Options");
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
        add(projectDetails);

        //Scrollbar
        JScrollPane sp = new JScrollPane(projectDetails);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(sp);
        setVisible(true);

        JFrame projectNames = new JFrame();
        projectNames.setTitle("Project Details");
        projectNames.setSize(500,500);
        projectNames.setLayout(new BorderLayout());
        projectNames.setLocation(122,270);

        JLabel projectTitle = new JLabel();
        projectTitle = new JLabel("Project Names:");
        projectTitle.setFont(new Font("Arial", Font.BOLD, 30));
        projectTitle.setHorizontalAlignment(JLabel.CENTER);
        projectNames.add(projectTitle, BorderLayout.NORTH);
        
        projectNameDetails = new JTextArea();
        projectNameDetails.setEditable(false);
        projectNames.add(projectNameDetails);

        JScrollPane sp2 = new JScrollPane(projectNameDetails);
        sp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        projectNames.add(sp2);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(new JButton("Clear Window"));
        add(panel, BorderLayout.SOUTH);


        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton addProject = new JButton("Add Project");
        addProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JFrame addProject = new JFrame();
                JPanel panel = new JPanel();
                JLabel addProjectTitle = new JLabel();
                addProjectTitle = new JLabel("Add Project");
                addProjectTitle.setFont(new Font("Arial", Font.BOLD, 15));
                addProjectTitle.setHorizontalAlignment(JLabel.CENTER);
                addProject.add(addProjectTitle, BorderLayout.NORTH);

                addProject.setTitle("Add Project");
                addProject.setSize(500, 380);
                addProject.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                addProject.setResizable(false);
                addProject.setLocationRelativeTo(null);

                JLabel projectName = new JLabel("Project Name:");
                pName = new JTextField(40);
                panel.add(projectName);
                panel.add(pName);

                JLabel projectProgress = new JLabel("Project Progress(%):");
                pProgress = new JTextField(40);
                panel.add(projectProgress);
                panel.add(pProgress);

                JLabel taskSequence = new JLabel("Task Sequence: ");
                tSeq = new JTextField(40);
                panel.add(taskSequence);
                panel.add(tSeq);

                JLabel taskSequenceProgress = new JLabel("Task Sequence Progress(%): ");
                tSeqProg = new JTextField(40);
                panel.add(taskSequenceProgress);
                panel.add(tSeqProg);

                JLabel taskDuration = new JLabel("Task Duration (mins):");
                tDuration = new JTextField(40);
                panel.add(taskDuration);
                panel.add(tDuration);

                JLabel teamSetup = new JLabel("Team Name:");
                tSetup = new JTextField(40);
                panel.add(teamSetup);
                panel.add(tSetup);

                panel.add(makeButton("Submit", "submitButton"));

                addProject.add(panel);
                addProject.setVisible(true);
            }
        });
        bottomPanel.add(addProject);

        JButton editProject = new JButton("Edit Project Details");
        bottomPanel.add(editProject);
        projectNames.add(bottomPanel, BorderLayout.SOUTH);

        projectNames.setVisible(true);
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

        if ("Remove Project".equals(e.getActionCommand())) {

        }

        if ("editDetails".equals(e.getActionCommand())) {

        }

        if ("editTeam".equals(e.getActionCommand())) {

        }

        if ("removeTeam".equals(e.getActionCommand())) {

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
