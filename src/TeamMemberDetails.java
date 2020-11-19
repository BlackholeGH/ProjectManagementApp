import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class TeamMemberDetails {
    private JPanel TeamMemberAdder;
    private JTextField nameField;
    private JTextField idField;
    private JTextField roleField;
    private JButton acceptButton;
    private JButton cancelButton;
    private int teamMemberID = 0;
    private String teamMemberName = "Joe Bloggs";
    private TeamMember generatedTeamMember = null;
    private JDialog frame;

    public void show(JFrame parentFrame) {
        frame = new JDialog(parentFrame, "Team member dialog", true);
        frame.setLocationRelativeTo(parentFrame);
        frame.setContentPane(TeamMemberAdder);
        frame.pack();
        frame.setVisible(true);
    }

    public TeamMember get() { return generatedTeamMember; }

    public TeamMemberDetails() {
        nameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                teamMemberName = nameField.getText();
            }
        });
        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                nameField.postActionEvent();
            }
        });
        idField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                    teamMemberID = Integer.parseInt(idField.getText());
                }
                catch(NumberFormatException ee)
                {
                    JOptionPane.showMessageDialog(null, "Could not set new ID: Must be an integer.");
                }
            }
        });
        idField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                idField.postActionEvent();
            }
        });
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatedTeamMember = new TeamMember(teamMemberName, roleField.getText(), teamMemberID);
                frame.dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatedTeamMember = null;
                frame.dispose();
            }
        });
    }
}
