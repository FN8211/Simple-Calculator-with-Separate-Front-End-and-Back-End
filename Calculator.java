package start;

import com.mysql.jdbc.Driver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.math.BigDecimal;

public class Calculator extends JFrame implements ActionListener {
    //
    private JPanel jp_north = new JPanel();       //initialize the panel on the northern of window
    private JTextField input_text = new JTextField();       //setting texts on northern panel as input
    private JButton c_Btn = new JButton("C");    //initialize the "clear" button
    private JPanel jp_center = new JPanel();       //initialize the panel on the central of window
    String content = "";
    private final String url = "jdbc:mysql://       :3306/demo";
    private final String username = "demo_user";
    private final String password = " ";
    //for safety reason, please contact author to obtain url and password
    //QQ:1243378880

    public Calculator() throws HeadlessException {
        //
        this.init();
        this.addNorthComponent();
        this.addCenterButton();
    }


    public void init() {
        //initialize the window
        this.setTitle(Const.TITLE);                          //setting title of window
        this.setSize(Const.FRAME_W, Const.FRAME_H);          //setting size of window
        this.setLayout(new BorderLayout());
        this.setResizable(false);                     //fix the size of window
        this.setLocation(Const.FRAME_X, Const.FRAME_Y);        //setting location of window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //closing the program when exit the window
    }

    public void addNorthComponent() {
        //adding elements on northern panel
        this.input_text.setPreferredSize(new Dimension(230, 30));
        jp_north.add(input_text);                       //adding display window into panel
        this.c_Btn.setForeground(Color.RED);            //setting color of "clear" button as red
        jp_north.add(c_Btn);                            //adding "clear" button into panel
        c_Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input_text.setText("");
                content = "";
            }
        });
        this.add(jp_north, BorderLayout.NORTH);        //putting panel on the northern side of window
    }

    public void addCenterButton() {
        //adding elements on central panel
        String btn_text = "123+ 456- 789* 0.=/ ";        //initializing the texts display on buttons
        String regex = "[\\+\\-\\*\\/\\.\\=\\%]";       //initializing the operators display on buttons
        this.jp_center.setLayout(new GridLayout(5, 5));          //adding buttons on central panel as formation of 4 * 4
        for (int i = 0; i < 25; i++) {
            String temp = "";
            if (i < 19 && (i - 4) % 5 != 0) {
                temp = btn_text.substring(i, i + 1);                  //extracting texts that would be added into buttons later
            } else if (i == 4) {
                temp = "pi";
            } else if (i == 9) {
                temp = "%";
            } else if (i == 14) {
                temp = "ans";
            } else if (i == 19) {
                temp = "history";
            }
            else if(i == 20){
                temp = "sin";
            }
            else if(i == 21){
                temp = "cos";
            }
            else if(i == 22){
                temp = "tan";
            }
            else if(i == 23){
                temp = "sqrt";
            }
            else if(i == 24){
                temp = "e";
            }
            JButton btn = new JButton();
            btn.setText(temp);                                           //setting texts in buttons
            if (temp.matches(regex) || temp.equals("pi") || temp.equals("ans") || temp.equals("history")
                  ||temp.matches("sin") || temp.matches("cos") || temp.matches("tan")
                    || temp.matches("sqrt") || temp.matches("e")) {     //if we are adding operators
                btn.setFont(new Font("粗体", Font.BOLD, 16));    //setting texts as bold
                btn.setForeground(Color.RED);                  //setting colors of operators as red
            }
            btn.addActionListener(this);                    //receiving the buttons that was pressed
            jp_center.add(btn);                                //adding buttons to central panel
        }
        this.add(jp_center, BorderLayout.CENTER);              //adding panel at the center of window
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setVisible(true);
    }

    private String firstInput = null;
    private String operator = null;

    @Override
    public void actionPerformed(ActionEvent e) {
        //calculating part of this program
        String clickStr = e.getActionCommand();            //receiving button been pressed
        if (".0123456789".indexOf(clickStr) != -1) {                          //if pressed numbers
            this.input_text.setText(input_text.getText() + clickStr);       //show buttons been pressed in order
            this.input_text.setHorizontalAlignment(JTextField.RIGHT);       //setting displaying texts on the right side of window
            content += clickStr;
        } else if (clickStr.matches("[\\+\\-\\*\\/\\%]") || clickStr.matches("pi")
             || clickStr.matches("sin") || clickStr.matches("cos") || clickStr.matches("tan")
             || clickStr.matches("sqrt") || clickStr.matches("e"))
        {           //if pressed operators
            operator = clickStr;
            firstInput = this.input_text.getText();
            this.input_text.setText("");                                     //clear the display window
            content += clickStr;
            if(clickStr.equals("e")){
                content += "^";
            }
        } else if (clickStr.equals("=")) {
            content += clickStr;
            Double a = 0.0;
            Double b = 0.0;
            if (firstInput.matches("-?\\d+(\\.\\d+)?")){
                a = Double.valueOf(firstInput);
            }
            if (this.input_text.getText().matches("-?\\d+(\\.\\d+)?")) {
                b = Double.valueOf(this.input_text.getText());
            }
            Double result = null;
            switch (operator) {               //different operations during calculating
                case "+":
                    result = a + b;
                    break;
                case "-":
                    result = a - b;
                    break;
                case "*":
                    result = a * b;
                    break;
                case "/":
                    if (b != 0) {
                        result = a / b;
                    }
                    break;
                case "%":
                    if (b != 0) {
                        result = a % b;
                    }
                    break;
                    //the following result of beneath operator is only conserved two decimal places
                case "pi":
                    BigDecimal   PIresult   =   new   BigDecimal(a * Math.PI);
                    result  =   PIresult.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                    break;
                case "sin":
                    BigDecimal   SINresult   =   new   BigDecimal(Math.sin(Math.toRadians(b)));
                    result = SINresult.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                    break;
                case "cos":
                    BigDecimal   COSresult   =   new   BigDecimal(Math.cos(Math.toRadians(b)));
                    result = COSresult.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                    break;
                case "tan":
                    BigDecimal   TANresult   =   new   BigDecimal(Math.tan(Math.toRadians(b)));
                    result = TANresult.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                    break;
                case "sqrt":
                    BigDecimal   SQRTresult   =   new   BigDecimal(Math.sqrt(b));
                    result = SQRTresult.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                    break;
                case "e":
                    BigDecimal   Eresult   =   new   BigDecimal(Math.exp(b));
                    result = Eresult.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                    break;
            }
            content += result;
            this.input_text.setText(result.toString());                    //change result of calculation to a string
            // and show on display window

            try {
                DriverManager.deregisterDriver(new Driver());
                Connection connection = DriverManager.getConnection(url, username, password);    //create connection to MySQL database
                String historyContent = "'" + content + "'";           //conserve the previous operation
                int count = 1;
                String sql = "";                              //initialize sql sentence
                Statement statement = connection.createStatement();
                sql = "update history set content = " + result + " where id = " + count;
                statement.executeUpdate(sql);                 //execute sql sentence to update the latest result on first row in database
                count++;
                sql = "select * from history";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);             //obtain messages in database and conserve in rs
                rs.next();                                         //skip the first row for it only conserve the result of latest result but not operations
                sql = "update history set content = " + historyContent + " where id = " + count;           //update the previous operations to following rows in database
                while (rs.next()) {                         //if there are exist next row in database
                    historyContent = "'" + rs.getString("content") + "'";          //remember the current row for it will be rewrote by following code
                    statement.executeUpdate(sql);
                    count++;
                    sql = "update history set content = " + historyContent + " where id = " + count;
                }
                //release obtained resources
                statement.close();
                stmt.close();
                connection.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if (clickStr.equals("ans")) {
            try {
                DriverManager.deregisterDriver(new com.mysql.jdbc.Driver());
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();
                String sql = "select * from history";
                ResultSet rs = statement.executeQuery(sql);
                Statement stmt = connection.createStatement();
                rs.next();
                String prevResult = rs.getString("content");     //obtain the first row in content column of database
                this.input_text.setText(prevResult);                        //print the message obtained in display window
                rs.close();
                stmt.close();
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        else if(clickStr.equals("history")) {
            String[] historyContent = new String[10];     //initialize a string to conserve the information from database
            //1.注册驱动
            try {
                DriverManager.deregisterDriver(new Driver());
                Connection conn = DriverManager.getConnection(url, username, password);
                Statement statement = conn.createStatement();
                String sql = "select * from history";
                ResultSet rs = statement.executeQuery(sql);
                Statement stmt = conn.createStatement();
                int count = 0;
                rs.next();                                //skip to second row in database
                while (rs.next()) {
                    historyContent[count] = rs.getString("content");          //receive message from database and conserve them in String array for later examining
                    count++;
                }
                rs.close();
                stmt.close();
                conn.close();

            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            //create another window in java to display history operations
            JFrame jframe = new JFrame();
            jframe.setTitle("History Content");
            JPanel panel = new JPanel();
            JTextField history[]= new JTextField[10];
            for(int i = 0; i < 10; i++) {
                history[i] = new JTextField();
                history[i].setEditable(false);
                history[i].setHorizontalAlignment(JTextField.CENTER);
                history[i].setPreferredSize(new Dimension(200,100));
                history[i].setText(historyContent[i]);                  //set the blocks in window to represent the information retain in String array
                Font font=new Font("",Font.BOLD,35);
                history[i].setFont(font);
                panel.add(history[i],BorderLayout.NORTH);
            }
            jframe.add(panel);
            jframe.setBounds(600, 200, 500, 600);
            jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jframe.setVisible(true);
        }
    }
}
