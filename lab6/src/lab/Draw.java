package lab;

import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class Draw extends JFrame {
  /**
   * Create a new Frame to show the process. (done)
   */
  private static final long serialVersionUID = 1L;
  private Panel panel1;
  private JTextField textField;
  private JTextField textField1;
  private JTextField textField2;
  private JTextField textField3;
  private JTextField textField4;
  private JTextField textField5;
  private Builder builder = null;
  private BuilderRainy builderRainy = null;

  /**
   * create a frame.
   */
  public Draw() {

    setBounds(100, 100, 1700, 700);
    getContentPane().setLayout(null);

    panel1 = new Panel();
    panel1.setLayout(null);
    panel1.setBounds(1400, 0, 300, 700);
    getContentPane().add(panel1);
    
    textField = new JTextField();
    textField.setText("5");
    textField.setBounds(170, 130, 86, 24);
    panel1.add(textField);
    textField.setColumns(10);

    textField1 = new JTextField();
    textField1.setText("20");
    textField1.setColumns(10);
    textField1.setBounds(170, 180, 86, 24);
    panel1.add(textField1);

    textField2 = new JTextField();
    textField2.setText("3");
    textField2.setColumns(10);
    textField2.setBounds(170, 230, 86, 24);
    panel1.add(textField2);

    textField3 = new JTextField();
    textField3.setText("25");
    textField3.setColumns(10);
    textField3.setBounds(170, 280, 86, 24);
    panel1.add(textField3);

    textField4 = new JTextField();
    textField4.setText("500");
    textField4.setColumns(10);
    textField4.setBounds(170, 330, 86, 24);
    panel1.add(textField4);

    textField5 = new JTextField();
    textField5.setText("10");
    textField5.setColumns(10);
    textField5.setBounds(170, 380, 86, 24);
    panel1.add(textField5);

    JButton btnNewButton = new JButton("Move");
    btnNewButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        int ladderNumber = 0;
        int ladderLength = 0;
        int timeInteval = 0;
          int oneTimeMkyNumber = 0;
          int mkyNumber = 0;
          int maxVelocity = 0;
        try {
          ladderNumber = Integer.valueOf(textField.getText());
          ladderLength = Integer.valueOf(textField1.getText());
          timeInteval = Integer.valueOf(textField2.getText());
          oneTimeMkyNumber = Integer.valueOf(textField3.getText());
          mkyNumber = Integer.valueOf(textField4.getText());
          maxVelocity = Integer.valueOf(textField5.getText());
            
        } catch (Exception e) {
          e.printStackTrace();
        }
        builder = new Builder(ladderNumber, ladderLength, timeInteval,
              oneTimeMkyNumber, mkyNumber, maxVelocity);
        builderRainy = null;
          builder.start();
      }
    });
    btnNewButton.setBounds(14, 529, 121, 45);
    btnNewButton.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 23));
    panel1.add(btnNewButton);

    JLabel lblNewLabel = new JLabel("Monkey");
    lblNewLabel.setFont(new Font("Arial", Font.PLAIN, 40));
    lblNewLabel.setBounds(14, 33, 244, 60);
    panel1.add(lblNewLabel);

    

    JLabel label1 = new JLabel("梯子数");
    label1.setFont(new Font("黑体", Font.BOLD, 25));
    label1.setBounds(14, 130, 135, 35);
    panel1.add(label1);

    JLabel label2 = new JLabel("梯子长");
    label2.setFont(new Font("黑体", Font.BOLD, 25));
    label2.setBounds(14, 180, 135, 35);
    panel1.add(label2);

    JLabel label3 = new JLabel("时间间隔");
    label3.setFont(new Font("黑体", Font.BOLD, 25));
    label3.setBounds(14, 230, 135, 35);
    panel1.add(label3);

    JLabel label4 = new JLabel("单次个数");
    label4.setFont(new Font("黑体", Font.BOLD, 25));
    label4.setBounds(14, 280, 135, 35);
    panel1.add(label4);

    JLabel label5 = new JLabel("总数");
    label5.setFont(new Font("黑体", Font.BOLD, 25));
    label5.setBounds(14, 330, 135, 35);
    panel1.add(label5);

    JLabel label6 = new JLabel("最大速度");
    label6.setFont(new Font("黑体", Font.BOLD, 25));
    label6.setBounds(14, 380, 135, 35);
    panel1.add(label6);
    
    JTextArea pathname = new JTextArea();
    pathname.setText(".\\src\\lab6\\txt\\Competition_1.txt");
    pathname.setBounds(14, 428, 244, 85);
    panel1.add(pathname);
    
    JButton btnFile = new JButton("File");
    btnFile.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        builderRainy = new BuilderRainy(pathname.getText());
        builder = null;
        builderRainy.start();
      }
    });
    btnFile.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 23));
    btnFile.setBounds(151, 529, 121, 45);
    panel1.add(btnFile);

    JTextPane textPane = new JTextPane();
    textPane.setEditable(true);
    textPane.setBounds(0, 0, 1400, 700);
    getContentPane().add(textPane);
    textPane.setFont(new Font("等线", Font.BOLD, 30));
    panel1.setVisible(true);
    this.setVisible(true);
    update(textPane);
  }

  private void update(JTextPane textPane) {
    while (true) {
      if (this.isVisible() == false) {
        System.exit(0);
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (builder != null) {
        textPane.setText("");
        textPane.setText(builder.getInfoString());
        if (builder.isStopped()) {
          textPane.setText("");
          String temp = String.format("%.3f", builder.getMkyNumber() / (double) builder.getTime());
          String temp1 = String.format("%.3f", builder.getIndex());
          String tempString = builder.getInfoString() 
              + "\n\n程序结束,总用时:" + builder.getTime() + "秒\n" + "公平指数: "
              + Double.valueOf(temp1) + "\n" + "吞吐率:" + Double.valueOf(temp);
          textPane.setText(tempString);
          ;
        }
      }
      
      if (builderRainy != null) {
        textPane.setText("");
        textPane.setText(builderRainy.getInfoString());
        if (builderRainy.isStopped()) {
          textPane.setText("");
          String temp = String.format("%.3f", (builderRainy.getLine().size() - 2) 
              / (double) builderRainy.getTime());
          String temp1 = String.format("%.3f", builderRainy.getIndex());
          String tempString = builderRainy.getInfoString() + "\n\n程序结束,总用时:" 
                  + builderRainy.getTime() + "秒\n" + "公平指数: "
              + Double.valueOf(temp1) + "\n" + "吞吐率:" + Double.valueOf(temp);
          textPane.setText(tempString);
          ;
        }
      }
    }
  }
}