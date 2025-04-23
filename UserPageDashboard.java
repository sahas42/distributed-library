import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserPageDashboard extends JFrame {

    private final User              loggedIn;
    private final BookRepository    bookRepo         = new BookRepository();
    private final BorrowedBookRepository borrowedRepo = new BorrowedBookRepository();
    private final AutoReturnService autoReturnService = new AutoReturnService();

    private CardLayout cardLayout;
    private JPanel     contentPanel;

    public UserPageDashboard(User loggedIn) {
        this.loggedIn = loggedIn;
        setTitle("User Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(createSidebar(), BorderLayout.WEST);

        cardLayout   = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createHomePanel(),          "Home");
        contentPanel.add(createProfilePanel(),       "Profile");
        contentPanel.add(createBorrowBooksPanel(),   "Borrow Books");
        contentPanel.add(createBorrowedBooksPanel(), "Borrowed Books");
        contentPanel.add(createDemandBooksPanel(),   "Demand Back");
        contentPanel.add(createDonatedBooksPanel(),  "Donated Books");
        contentPanel.add(createDonateBooksPanel(),   "Donate Books");
        contentPanel.add(createPasswordPanel(),      "Change Password");

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(new Color(44, 62, 80));
        side.setPreferredSize(new Dimension(200, getHeight()));

        String[] items = {
            "Home",
            "Profile",
            "Borrow Books",
            "Borrowed Books",
            "Demand Back",
            "Donated Books",
            "Donate Books",
            "Change Password",
            "Logout"
        };

        for (String item : items) {
            JButton btn = new JButton(item);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(200, 40));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(52, 73, 94));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btn.addActionListener(e -> {
                if ("Logout".equals(item)) {
                    new MainGUI().setVisible(true);
                    dispose();
                } else {
                    autoReturnService.processOverdues();
                    refreshPanel(item);
                    cardLayout.show(contentPanel, item);
                }
            });
            side.add(Box.createVerticalStrut(10));
            side.add(btn);
        }
        return side;
    }

    private void refreshPanel(String cardName) {
        switch (cardName) {
            case "Borrow Books"   -> replaceCard(cardName, createBorrowBooksPanel());
            case "Borrowed Books" -> replaceCard(cardName, createBorrowedBooksPanel());
            case "Donated Books"  -> replaceCard(cardName, createDonatedBooksPanel());
            case "Demand Back"    -> replaceCard(cardName, createDemandBooksPanel());
            default -> {}
        }
    }
    private void replaceCard(String cardName, JPanel freshPanel) {
        contentPanel.add(freshPanel, cardName);
    }

    private JPanel createHomePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(245, 246, 250));
        JLabel lbl = new JLabel("Welcome, " + loggedIn.getName() + "!", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    private JPanel createProfilePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel info = new JLabel("<html>"
                + "Name: "   + loggedIn.getName()        + "<br>"
                + "User Id: "+ loggedIn.getID()          + "<br>"
                + "Email: "  + loggedIn.getEmail()       + "<br>"
                + "Phone: "  + loggedIn.getPhoneNumber() + "</html>");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        info.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.add(info, BorderLayout.NORTH);
        return p;
    }

    private JPanel createBorrowBooksPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel title = new JLabel("Borrow a Book");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));

        /* search bar */
        JPanel search = new JPanel(new FlowLayout());
        JTextField tf = new JTextField(15);
        JButton   go = new JButton("Search");
        search.add(new JLabel("Title:"));
        search.add(tf);
        search.add(go);

        /* table */
        String[] cols = {"Book ID","Name","Author","Owner"};
        JTable   tbl  = new JTable();
        JScrollPane jsp = new JScrollPane(tbl);
        Runnable loadTable = () -> {
            var list = bookRepo.findAvailableBooksByKeyword(tf.getText().trim());
            Object[][] data = new Object[list.size()][4];
            for (int i=0;i<list.size();i++){
                BOOK b = list.get(i);
                data[i][0]=b.getBookId();
                data[i][1]=b.getName();
                data[i][2]=b.getAuthor();
                data[i][3]=b.getDonatedby();
            }
            tbl.setModel(new javax.swing.table.DefaultTableModel(data, cols));
        };
        loadTable.run();
        go.addActionListener(e -> loadTable.run());

        /* borrow button */
        JButton borrow = new JButton("Borrow Selected");
        borrow.addActionListener(e -> {
            int r = tbl.getSelectedRow();
            if (r==-1){ JOptionPane.showMessageDialog(this,"Select a row"); return; }
            int bookId = (Integer) tbl.getValueAt(r,0);
            BOOK sel = bookRepo.findById(bookId);
            if (sel==null) return;
            if (loggedIn.borrowBook(sel)) {
                bookRepo.update(sel);
                borrowedRepo.saveTransaction(loggedIn.getID(), String.valueOf(bookId),"LibraryBook");
                autoReturnService.processOverdues();
                refreshPanel("Borrow Books");
                refreshPanel("Borrowed Books");
            }
        });

        p.add(title,  BorderLayout.NORTH);
        p.add(search, BorderLayout.BEFORE_FIRST_LINE);
        p.add(jsp,    BorderLayout.CENTER);
        p.add(borrow, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createBorrowedBooksPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel title = new JLabel("Your Borrowed Books");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));

        List<BOOK> borrowed = bookRepo.findBorrowedByUser(loggedIn.getID());
        String[] cols = {"Book ID","Name","Author","Status"};
        Object[][] data = new Object[borrowed.size()][4];
        for(int i=0;i<borrowed.size();i++){
            BOOK b = borrowed.get(i);
            data[i][0]=b.getBookId();
            data[i][1]=b.getName();
            data[i][2]=b.getAuthor();
            data[i][3]=b.getBookedStatus();
        }
        JTable tbl = new JTable(data, cols);
        JScrollPane jsp = new JScrollPane(tbl);

        p.add(title, BorderLayout.NORTH);
        p.add(jsp,   BorderLayout.CENTER);
        return p;
    }

    /* -------------  DEMAND BACK ----------------------- */
    private JPanel createDemandBooksPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel t = new JLabel("Demand Book Back");
        t.setFont(new Font("Segoe UI", Font.BOLD, 18));
        t.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));

        JPanel search = new JPanel(new FlowLayout());
        JTextField tf = new JTextField(15);
        JButton sb   = new JButton("Search");
        search.add(new JLabel("Title:"));
        search.add(tf);
        search.add(sb);

        JTable tbl = new JTable();
        JScrollPane jsp = new JScrollPane(tbl);
        Runnable reload = () -> {
            var owned = bookRepo.findOwnedBooksByKeyword(loggedIn.getName(), tf.getText().trim());
            var show  = owned.stream().filter(b->!b.getBookedStatus().equalsIgnoreCase("withOwner")).toList();
            Object[][] dat = new Object[show.size()][3];
            for(int i=0;i<show.size();i++){
                BOOK b = show.get(i);
                dat[i][0]=b.getBookId();
                dat[i][1]=b.getName();
                dat[i][2]=b.getBookedStatus();
            }
            tbl.setModel(new javax.swing.table.DefaultTableModel(dat,new String[]{"Book ID","Name","Status"}));
        };
        reload.run();
        sb.addActionListener(e->reload.run());

        JButton demand = new JButton("Demand Selected");
        demand.addActionListener(e -> {
            int row = tbl.getSelectedRow();
            if (row==-1){ JOptionPane.showMessageDialog(this,"Select a row"); return; }
            int bookId = (Integer) tbl.getValueAt(row,0);
            BOOK sel = bookRepo.findById(bookId);
            if (sel==null) return;

            if (loggedIn.demandBookBack(sel)) {
                bookRepo.update(sel);
                autoReturnService.processOverdues();
                refreshPanel("Demand Back");
                refreshPanel("Donated Books");
            }
        });

        p.add(t,       BorderLayout.NORTH);
        p.add(search,  BorderLayout.BEFORE_FIRST_LINE);
        p.add(jsp,     BorderLayout.CENTER);
        p.add(demand,  BorderLayout.SOUTH);
        return p;
    }

    /* -------------  DONATED LIST ---------------------- */
    private JPanel createDonatedBooksPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("Your Donated (Owned) Books");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));

        ArrayList<BOOK> list = bookRepo.findByDonar(loggedIn.getName());
        Object[][] data = new Object[list.size()][3];
        for(int i=0;i<list.size();i++){
            BOOK b=list.get(i);
            data[i][0]=b.getBookId();
            data[i][1]=b.getName();
            data[i][2]=b.getBookedStatus();
        }
        JTable tbl = new JTable(data, new String[]{"Book ID","Name","Status"});
        p.add(lbl,BorderLayout.NORTH);
        p.add(new JScrollPane(tbl),BorderLayout.CENTER);
        return p;
    }

    //------------  DONATE PANEL ---------------------
    private JPanel createDonateBooksPanel() {
        JPanel p=new JPanel();
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(20,50,20,50));
        p.setBackground(Color.WHITE);

        JLabel title=new JLabel("Donate Book");
        title.setFont(new Font("Segoe UI",Font.BOLD,22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel form=new JPanel(new GridLayout(4,2,10,10));
        form.setBackground(Color.WHITE);
        JTextField nameF=new JTextField(15);
        JTextField authF=new JTextField(15);
        JTextField subF =new JTextField(15);
        JTextField locF =new JTextField(15);
        form.add(new JLabel("Book Name:"));  form.add(nameF);
        form.add(new JLabel("Author:"));     form.add(authF);
        form.add(new JLabel("Sublibrary ID:")); form.add(subF);
        form.add(new JLabel("Location:"));   form.add(locF);

        JButton donate=new JButton("Donate");
        donate.setAlignmentX(Component.CENTER_ALIGNMENT);
        donate.addActionListener(e->{
            try{
                int subId=Integer.parseInt(subF.getText().trim());
                String bn=nameF.getText().trim();
                if(bn.isEmpty()){ JOptionPane.showMessageDialog(this,"Name required"); return; }
                BOOK b=new BOOK(0,bn,"available",null,subId,null,"donated",
                                authF.getText().trim(),loggedIn.getName());
                b.setLocation(locF.getText().trim());
                if(bookRepo.save(b)){
                    JOptionPane.showMessageDialog(this,"Book donated!");
                    autoReturnService.processOverdues();
                    refreshPanel("Donated Books");
                }
            }catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(this,"Sublibrary ID must be integer");
            }
        });

        p.add(title); p.add(Box.createVerticalStrut(20));
        p.add(form);  p.add(Box.createVerticalStrut(20));
        p.add(donate);
        return p;
    }

    // -------------  PASSWORD PANEL -----------------
    private JPanel createPasswordPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(40,100,40,100));
        p.setBackground(Color.WHITE);

        JLabel lbl=new JLabel("Change Password");
        lbl.setFont(new Font("Segoe UI",Font.BOLD,18));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField oldF=new JPasswordField(20);
        JPasswordField newF=new JPasswordField(20);
        JPasswordField conF=new JPasswordField(20);
        JButton change=new JButton("Change Password");
        change.setAlignmentX(Component.CENTER_ALIGNMENT);
        change.addActionListener(e->{
            String o=new String(oldF.getPassword());
            String n=new String(newF.getPassword());
            String c=new String(conF.getPassword());
            if(!n.equals(c)){ JOptionPane.showMessageDialog(this,"New passwords differ"); return; }
            if(loggedIn.changePassword(o,n)){
                loggedIn.setPassword(n,loggedIn.getID());
                JOptionPane.showMessageDialog(this,"Password changed");
            }else JOptionPane.showMessageDialog(this,"Old password wrong");
        });

        p.add(lbl); p.add(Box.createVerticalStrut(20));
        p.add(new JLabel("Old Password:")); p.add(oldF);
        p.add(new JLabel("New Password:")); p.add(newF);
        p.add(new JLabel("Confirm Password:")); p.add(conF);
        p.add(Box.createVerticalStrut(20)); p.add(change);
        return p;
    }
}
