import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AdminPageDashboard extends JFrame {

    private final ADMIN admin;
    private final BookRepository bookRepo = new BookRepository();
    private final AutoReturnService autoReturnService = new AutoReturnService();

    private CardLayout cardLayout;
    private JPanel     contentPanel;

    public AdminPageDashboard(ADMIN admin) {
        this.admin = admin;

        setTitle("Admin Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);

        cardLayout   = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createHomePanel(),        "Home");
        contentPanel.add(createManageUsersPanel(), "Manage Users");
        contentPanel.add(createAddBookPanel(),     "Add Book");
        contentPanel.add(createRemoveBookPanel(),  "Remove Book");
        contentPanel.add(createViewBooksPanel(),   "View Books");
        contentPanel.add(createAddSublibraryPanel(),"Add Sublibrary");

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(new Color(44, 62, 80));
        side.setPreferredSize(new Dimension(200, getHeight()));

        String[] items = {
            "Home",
            "Manage Users",
            "Add Book",
            "Remove Book",
            "View Books",
            "Add Sublibrary",
            "Logout"
        };

        for (String item : items) {
            JButton btn = new JButton(item);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(200,40));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(52, 73, 94));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            btn.addActionListener(e -> {
                if ("Logout".equals(item)) {
                    new MainGUI().setVisible(true);
                    dispose();
                } else {
                    autoReturnService.processOverdues();
                    refreshPanel(item);
                    cardLayout.show(contentPanel,item);
                }
            });
            side.add(Box.createVerticalStrut(10));
            side.add(btn);
        }
        return side;
    }

    private void refreshPanel(String name){
        if("View Books".equals(name)) replaceCard(name,createViewBooksPanel());
    }
    private void replaceCard(String cardName,JPanel fresh){
        contentPanel.add(fresh,cardName);
    }

    /* ----------------  Home  ----------------------- */
    private JPanel createHomePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(245,246,250));
        JLabel lbl=new JLabel("Welcome, Admin "+admin.getName()+"!",SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI",Font.BOLD,22));
        p.add(lbl,BorderLayout.CENTER);
        return p;
    }

    /* ----------------  Manage Users ---------------- */
    private JPanel createManageUsersPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel lbl=new JLabel("Manage Users",SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI",Font.BOLD,18));

        JPanel bottom=new JPanel();
        JTextField userIdF=new JTextField(15);
        JButton viewBtn=new JButton("View User Details");
        viewBtn.addActionListener(e->{
            String id=userIdF.getText().trim();
            if(id.isEmpty()){ JOptionPane.showMessageDialog(this,"Enter ID"); return;}
            JOptionPane.showMessageDialog(this, admin.viewUserDetails(id));
        });
        bottom.add(new JLabel("User ID:")); bottom.add(userIdF); bottom.add(viewBtn);

        p.add(lbl,BorderLayout.NORTH);
        p.add(bottom,BorderLayout.CENTER);
        return p;
    }

    /* ---------------  Add Book ---------------------- */
    private JPanel createAddBookPanel() {
        JPanel p=new JPanel();
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(40,100,40,100));
        p.setBackground(Color.WHITE);

        JLabel title=new JLabel("Add Book");
        title.setFont(new Font("Segoe UI",Font.BOLD,18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField idF=new JTextField(15);
        JTextField nameF=new JTextField(15);
        JTextField locF=new JTextField(15);
        JTextField subF=new JTextField(15);

        JButton add=new JButton("Add Book");
        add.setAlignmentX(Component.CENTER_ALIGNMENT);
        add.addActionListener(e->{
            try{
                int id=Integer.parseInt(idF.getText().trim());
                int sub=Integer.parseInt(subF.getText().trim());
                BOOK b=new BOOK(id,nameF.getText().trim(),"available",
                                null,sub,null,"Normal","Unknown","Admin");
                b.setLocation(locF.getText().trim());
                if(admin.addBook(b)){
                    JOptionPane.showMessageDialog(this,"Added!");
                    autoReturnService.processOverdues();
                    refreshPanel("View Books");
                }
            }catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(this,"IDs must be numeric");
            }
        });

        p.add(title); p.add(Box.createVerticalStrut(20));
        p.add(new JLabel("Book ID:"));      p.add(idF);
        p.add(new JLabel("Book Name:"));    p.add(nameF);
        p.add(new JLabel("Location:"));     p.add(locF);
        p.add(new JLabel("Sublibrary ID:"));//
        p.add(subF); p.add(Box.createVerticalStrut(20)); p.add(add);
        return p;
    }

    /* --------------  Remove Book ------------------- */
    private JPanel createRemoveBookPanel() {
        JPanel p=new JPanel();
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(40,100,40,100));
        p.setBackground(Color.WHITE);

        JLabel t=new JLabel("Remove Book");
        t.setFont(new Font("Segoe UI",Font.BOLD,18));
        t.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField idF=new JTextField(15);
        JButton rem=new JButton("Remove Book");
        rem.setAlignmentX(Component.CENTER_ALIGNMENT);
        rem.addActionListener(e->{
            try{
                int id=Integer.parseInt(idF.getText().trim());
                if(admin.removeBook(id)){
                    JOptionPane.showMessageDialog(this,"Removed!");
                    autoReturnService.processOverdues();
                    refreshPanel("View Books");
                }
            }catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(this,"Book ID must be integer");
            }
        });

        p.add(t); p.add(Box.createVerticalStrut(20));
        p.add(new JLabel("Book ID:")); p.add(idF);
        p.add(Box.createVerticalStrut(20)); p.add(rem);
        return p;
    }

    /* ----------------  View Books -------- */
    private JPanel createViewBooksPanel() {
        JPanel p=new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel lbl=new JLabel("All Books in System");
        lbl.setFont(new Font("Segoe UI",Font.BOLD,18));
        lbl.setBorder(BorderFactory.createEmptyBorder(20,20,10,0));

        List<BOOK> list = admin.ViewBooks();
        Object[][] data=new Object[list.size()][6];
        for(int i=0;i<list.size();i++){
            BOOK b=list.get(i);
            data[i][0]=b.getBookId();
            data[i][1]=b.getName();
            data[i][2]=b.getBookedStatus();
            data[i][3]=b.getSubLibraryID();
            data[i][4]=b.getAuthor();
            data[i][5]=b.getDonatedby();
        }
        JTable tbl=new JTable(data,new String[]{"ID","Name","Status","Sublib","Author","Owner"});
        p.add(lbl,BorderLayout.NORTH);
        p.add(new JScrollPane(tbl),BorderLayout.CENTER);
        return p;
    }

    /* -------------  Add Sublibrary -------------- */
    private JPanel createAddSublibraryPanel() {
        JPanel p=new JPanel();
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(40,100,40,100));
        p.setBackground(Color.WHITE);

        JLabel t=new JLabel("Add Sublibrary");
        t.setFont(new Font("Segoe UI",Font.BOLD,18));
        t.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField nameF=new JTextField(15);
        JTextField latF =new JTextField(15);
        JTextField lonF =new JTextField(15);
        JTextField descF=new JTextField(15);

        JButton add=new JButton("Add Sublibrary");
        add.setAlignmentX(Component.CENTER_ALIGNMENT);
        add.addActionListener(e->{
            try{
                double lat=Double.parseDouble(latF.getText().trim());
                double lon=Double.parseDouble(lonF.getText().trim());
                Sublibrary sub=new Sublibrary(lat,lon,nameF.getText().trim(),descF.getText().trim());
                if(admin.addSubLibrary(sub)){
                    JOptionPane.showMessageDialog(this,"Sublibrary added!");
                }
            }catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(this,"Lat/Lon must be numeric");
            }
        });

        p.add(t); p.add(Box.createVerticalStrut(20));
        p.add(new JLabel("Name:")); p.add(nameF);
        p.add(new JLabel("Latitude:")); p.add(latF);
        p.add(new JLabel("Longitude:")); p.add(lonF);
        p.add(new JLabel("Description:")); p.add(descF);
        p.add(Box.createVerticalStrut(20)); p.add(add);
        return p;
    }
}
