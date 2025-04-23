import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainGUI extends JFrame {

    public MainGUI() {
        initComponents();
    }

    private void initComponents() {
        jPanel2 = new JPanel();
        jLabel1 = new JLabel();
        jTextField1 = new JTextField();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jButton1 = new JButton();
        jPasswordField1 = new JPasswordField();
        jButton2 = new JButton();
        jLabel4 = new JLabel();
        jPanel1 = new JPanel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 500));
        getContentPane().setLayout(null);

        jPanel2.setBackground(new Color(255,255,255));
        jPanel2.setLayout(null);

        jLabel1.setFont(new Font("Segoe UI", 1, 14));
        jLabel1.setText("USER ID");
        jPanel2.add(jLabel1);
        jLabel1.setBounds(20, 80, 130, 30);

        jTextField1.setBorder(BorderFactory.createEtchedBorder(new Color(44,62,80), null));
        jPanel2.add(jTextField1);
        jTextField1.setBounds(20,112,380,40);

        jLabel2.setFont(new Font("Segoe UI",1,18));
        jLabel2.setForeground(new Color(0,51,51));
        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel2.setText("LOGIN");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(140,30,130,40);

        jLabel3.setFont(new Font("Segoe UI",1,14));
        jLabel3.setText("PASSWORD");
        jPanel2.add(jLabel3);
        jLabel3.setBounds(20,170,100,30);

        jButton1.setBackground(new Color(44,62,80));
        jButton1.setFont(new Font("Segoe UI",1,14));
        jButton1.setForeground(new Color(255,255,255));
        jButton1.setText("LOGIN");
        jButton1.setBorder(BorderFactory.createSoftBevelBorder(0));
        jButton1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                loginBtnActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);
        jButton1.setBounds(160,283,120,30);

        jPasswordField1.setBorder(BorderFactory.createEtchedBorder(new Color(44,62,80),null));
        jPanel2.add(jPasswordField1);
        jPasswordField1.setBounds(20,200,380,40);

        jButton2.setBackground(new Color(44,62,80));
        jButton2.setForeground(new Color(255,255,255));
        jButton2.setText("SIGN UP");
        jButton2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                signUpBtnActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2);
        jButton2.setBounds(30,390,120,20);

        jLabel4.setText("DON'T HAVE ACCOUNT");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(30,370,126,10);

        getContentPane().add(jPanel2);
        jPanel2.setBounds(350,0,450,500);

        jPanel1.setBackground(new Color(44,62,80));
        getContentPane().add(jPanel1);
        jPanel1.setBounds(0,0,350,500);

        pack();
        setLocationRelativeTo(null);
    }

    private void loginBtnActionPerformed(ActionEvent evt) {
        try {
            userId = jTextField1.getText().trim();
            password = new String(jPasswordField1.getPassword()).trim();
            PERSON person = userRepository.findById(userId);

            if(userId.equals("") || password.equals("")) {
                JOptionPane.showMessageDialog(null, 
                    "Enter all details", 
                    "ERROR", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(person != null && person.validateCredentials(userId, password)) {
                if(person instanceof ADMIN) {
                    AdminPageDashboard adminDash = new AdminPageDashboard((ADMIN)person);
                    adminDash.setVisible(true);
                } else {
                    UserPageDashboard userDash = new UserPageDashboard((User)person);
                    userDash.setVisible(true);
                }
                dispose();
            } else if (person != null) {
                JOptionPane.showMessageDialog(null,
                    "INCORRECT PASSWORD",
                    "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                    "ACCOUNT DOESN'T EXIST\nCREATE ACCOUNT",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null,
                "Unexpected error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void signUpBtnActionPerformed(ActionEvent evt) {
        CreateAccountGUI cgui = new CreateAccountGUI();
        cgui.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new MainGUI().setVisible(true));
    }

    private JPanel jPanel1, jPanel2;
    private JLabel jLabel1, jLabel2, jLabel3, jLabel4;
    private JTextField jTextField1;
    private JPasswordField jPasswordField1;
    private JButton jButton1, jButton2;
    private String userId, password;
    private UserRepository userRepository = new UserRepository();
}
