import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

public class TeamEditor {
    private JTextField nameField;
    private JTextField idField;
    private JButton saveTeamButton;
    private JButton exitButton;
    private JButton addTeamMemberButton;
    private JButton deleteTeamMemberButton;
    private JPanel teamEditorPanel;
    private JTable teamMemberTable;
    private JScrollPane tableScrollPane;
    private Team teamToMod = null;
    private String teamName = "Default team name";
    private int teamID = -1;
    private ArrayList<TeamMember> teamMembers = new ArrayList<>();
    private JFrame frame;

    private void updateTeamMemberTable()
    {
        String[][] data = new String[teamMembers.size()][2];
        for(int i = 0; i < teamMembers.size(); i++)
        {
            data[i][0] = teamMembers.get(i).getName();
            data[i][1] = teamMembers.get(i).getRole();
        }
        DefaultTableModel model = new DefaultTableModel(data, new String[] { "Team member", "Role"});
        teamMemberTable = new JTable(model);
        tableScrollPane.setViewportView(teamMemberTable);
    }

    public void showTeamEditor(Team preExistingTeam) {
        frame = new JFrame("TeamEditor");
        frame.setContentPane(teamEditorPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationByPlatform(true);
        if(preExistingTeam != null)
        {
            teamToMod = preExistingTeam;
            teamName = teamToMod.getTeamName();
            teamID = teamToMod.getTeamID();
            teamMembers = new ArrayList<TeamMember>(teamToMod.getTeamMembers());
            nameField.setText(teamName);
            idField.setText(teamID + "");
            updateTeamMemberTable();
        }
        else
        {
            DefaultTableModel model = new DefaultTableModel(new String[0][2], new String[] { "Team member", "Role"});
            teamMemberTable.setModel(model);
        }
        frame.pack();
        frame.setVisible(true);
    }

    private Team teamifyThis()
    {
        Team out = new Team(teamName, teamID);
        for(int i = 0; i < teamMembers.size(); i++) {
            TeamMember tm = teamMembers.get(i);
            out.getTeamMembers().add(tm.getIdNumber(), tm);
        }
        return out;
    }

    public TeamEditor() {

        nameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teamName = nameField.getText();
            }
        });
        idField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int newID = -1;
                try
                {
                    newID = Integer.parseInt(idField.getText());
                    if(newID < 0) { throw new NumberFormatException(); }
                    if(TeamKt.getTeamsMap().containsKey(newID))
                    {
                        JOptionPane.showMessageDialog(null, "Could not set new ID: ID is already in use!");
                    }
                    else
                    {
                        teamID = newID;
                    }
                }
                catch(NumberFormatException ee)
                {
                    JOptionPane.showMessageDialog(null, "Could not set new ID: Must be a positive integer.");
                }
            }
        });
        idField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                idField.postActionEvent();
            }
        });
        addTeamMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TeamMemberDetails tmd = new TeamMemberDetails();
                tmd.show(frame);
                if(tmd.get() != null)
                {
                    teamMembers.add(tmd.get());
                    updateTeamMemberTable();
                }
            }
        });
        deleteTeamMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] teamMemberNames = new String[teamMembers.size()];
                for(int i = 0; i < teamMembers.size(); i++)
                {
                    teamMemberNames[i] = teamMembers.get(i).getName();
                }
                if(teamMemberNames.length == 0)
                {
                    JOptionPane.showMessageDialog(null, "There are no team members to delete.");
                }
                else
                {
                    String selection = (String) JOptionPane.showInputDialog(null, "Choose a team member to delete.", "Selection dialog", JOptionPane.QUESTION_MESSAGE, null, teamMemberNames, teamMemberNames[0]);
                    ArrayList<TeamMember> remTMs = new ArrayList<>();
                    for(int i = 0; i < teamMembers.size(); i++)
                    {
                        if(teamMembers.get(i).getName().equals(selection)) { remTMs.add(teamMembers.get(i)); }
                    }
                    for(int i = 0; i < remTMs.size(); i++)
                    {
                        teamMembers.remove(remTMs.get(i));
                    }
                    updateTeamMemberTable();
                }
            }
        });
        saveTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(teamToMod != null)
                {
                    TeamKt.getTeamsMap().remove(teamToMod.getTeamID());
                }
                teamToMod = teamifyThis();
                TeamKt.getTeamsMap().put(teamToMod.getTeamID(), teamToMod);
                Team.Companion.saveTeams();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                nameField.postActionEvent();
            }
        });
    }
}
