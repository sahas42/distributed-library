import javax.swing.JOptionPane;

public class CreateAccountGUI extends javax.swing.JFrame {

    public CreateAccountGUI() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        popupMenu1 = new java.awt.PopupMenu();
        popupMenu2 = new java.awt.PopupMenu();
        TITLE = new javax.swing.JLabel();
        NameLabel = new javax.swing.JLabel();
        NmaeFeild = new javax.swing.JTextField();
        NameLabel1 = new javax.swing.JLabel();
        PhoneNumberFeild = new javax.swing.JTextField();
        EmailLabel = new javax.swing.JLabel();
        EmailFeild = new javax.swing.JTextField();
        UserIdLabel = new javax.swing.JLabel();
        PasswordLabel = new javax.swing.JLabel();
        UserIdFeild = new javax.swing.JTextField();
        PasswordField = new javax.swing.JPasswordField();
        CreateButton = new javax.swing.JButton();
        BackButton = new javax.swing.JButton();
        isAdminButton = new javax.swing.JRadioButton();
        isUserButton = new javax.swing.JRadioButton();
        buttonGroup = new javax.swing.ButtonGroup();
        buttonGroup.add(isAdminButton);
        buttonGroup.add(isUserButton);

        popupMenu1.setLabel("popupMenu1");
        popupMenu2.setLabel("popupMenu2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1001,594));
        setLayout(null);

        TITLE.setFont(new java.awt.Font("Sans Serif Collection",1,18));
        TITLE.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TITLE.setText("CREATE ACCOUNT");
        TITLE.setOpaque(true);
        add(TITLE);
        TITLE.setBounds(350,30,300,30);

        NameLabel.setFont(new java.awt.Font("Segoe UI",1,12));
        NameLabel.setText("FULL NAME");
        NameLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        add(NameLabel);
        NameLabel.setBounds(200,100,150,30);

        add(NmaeFeild);
        NmaeFeild.setBounds(370,100,300,30);

        NameLabel1.setFont(new java.awt.Font("Segoe UI",1,12));
        NameLabel1.setText("PHONE NUMBER");
        NameLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        add(NameLabel1);
        NameLabel1.setBounds(200,150,150,30);

        add(PhoneNumberFeild);
        PhoneNumberFeild.setBounds(370,150,300,30);

        EmailLabel.setFont(new java.awt.Font("Segoe UI",1,12));
        EmailLabel.setText("E-MAIL");
        EmailLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        add(EmailLabel);
        EmailLabel.setBounds(200,200,150,30);

        add(EmailFeild);
        EmailFeild.setBounds(370,200,300,30);

        UserIdLabel.setFont(new java.awt.Font("Segoe UI",1,12));
        UserIdLabel.setText("USER ID");
        UserIdLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        add(UserIdLabel);
        UserIdLabel.setBounds(200,250,150,30);

        PasswordLabel.setFont(new java.awt.Font("Segoe UI",1,12));
        PasswordLabel.setText("PASSWORD");
        PasswordLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        add(PasswordLabel);
        PasswordLabel.setBounds(200,300,150,30);

        add(UserIdFeild);
        UserIdFeild.setBounds(370,250,300,30);
        add(PasswordField);
        PasswordField.setBounds(370,300,300,30);

        CreateButton.setText("CREATE");
        CreateButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                createButtonActionPerformed(evt);
            }
        });
        add(CreateButton);
        CreateButton.setBounds(370,420,100,30);

        BackButton.setText("BACK");
        BackButton.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                backButtonActionPerformed(evt);
            }
        });
        add(BackButton);
        BackButton.setBounds(30,30,100,30);

        isAdminButton.setText("Admin");
        add(isAdminButton);
        isAdminButton.setBounds(350,350,70,30);

        isUserButton.setText("User");
        add(isUserButton);
        isUserButton.setBounds(450,350,70,30);

        pack();
        setSize(1000,600);
        setLocationRelativeTo(null);
    }

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt){
        String name = NmaeFeild.getText().trim();
        String userId = UserIdFeild.getText().trim();
        String phno = PhoneNumberFeild.getText().trim();
        String email = EmailFeild.getText().trim();
        String password = new String(PasswordField.getPassword()).trim();
        boolean isAdmin = isAdminButton.isSelected();

        if(name.equals("") || userId.equals("") || phno.equals("") ||
           email.equals("") || password.equals("") ||
           (!isAdminButton.isSelected() && !isUserButton.isSelected())) {
            JOptionPane.showMessageDialog(null, "Enter all details", "ERROR", JOptionPane.WARNING_MESSAGE);
            return;
        }

        long phone=0;
        try {
            phone = Long.parseLong(phno);
        } catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Phone number must be numeric", "ERROR", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if(userRepository.findById(userId) != null) {
            JOptionPane.showMessageDialog(null, "UserID already exists","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        PERSON newPerson = isAdmin
                ? new ADMIN(name, phone, email, userId, password)
                : new User(name, phone, email, userId, password);

        boolean success = userRepository.save(newPerson);
        if(success) {
            JOptionPane.showMessageDialog(null, 
                "Account created successfully!", "ACCOUNT CREATION", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, 
                "Failed to create account in DB", "ERROR", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt){
        MainGUI mg = new MainGUI();
        mg.setVisible(true);
        dispose();
    }

    private java.awt.PopupMenu popupMenu1, popupMenu2;
    private javax.swing.JLabel TITLE;
    private javax.swing.JLabel NameLabel, NameLabel1;
    private javax.swing.JTextField NmaeFeild, PhoneNumberFeild, EmailFeild, UserIdFeild;
    private javax.swing.JPasswordField PasswordField;
    private javax.swing.JLabel EmailLabel, UserIdLabel, PasswordLabel;
    private javax.swing.JButton CreateButton, BackButton;
    private javax.swing.JRadioButton isAdminButton, isUserButton;
    private javax.swing.ButtonGroup buttonGroup;
    private UserRepository userRepository = new UserRepository();
}
