package burp;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * @author LinChen
 */

public class MainUI extends JPanel {

    private JTable table;

    private DefaultTableModel model = new DefaultTableModel() {
        public Class<?> getColumnClass(int column) {
            switch (column) {
                case 0:
                    return Integer.class;
                case 1:
                    return Boolean.class;
                case 2:
                    return Boolean.class;
                default:
                    return String.class;
            }
        }

        public boolean isCellEditable(int row, int column) {
            if (column > 0)
                return true;
            return false;
        }
    };

    public MainUI(Targets targets) {
        initComponents(targets);
    }

    private void initComponents(final Targets targets) {

        // ======== this ========
        setLayout(new GridBagLayout());
        ((GridBagLayout) getLayout()).columnWidths = new int[] { 0, 0, 0 };
        ((GridBagLayout) getLayout()).rowHeights = new int[] { 0, 0, 0, 0, 0 };
        ((GridBagLayout) getLayout()).columnWeights = new double[] { 0.0, 1.0, 1.0E-4 };
        ((GridBagLayout) getLayout()).rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 1.0E-4 };

        // ---- FilterSuffix ----
        JLabel label1 = new JLabel();
        label1.setText("过滤后缀：");
        add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(15, 5, 3, 2), 0, 0));
        label1.setBounds(new Rectangle(new Point(0, 0), label1.getPreferredSize()));
        final JTextField text1 = new JTextField();
        text1.setText(targets.excludeSuffix);
        add(text1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(15, 5, 3, 2), 0, 0));
        // text1.setBounds(70,50,865,30);
        text1.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(FocusEvent e) {
                targets.updateExcludeSuffix(text1.getText());
            }

            @Override
            public void focusGained(FocusEvent e) {
            }
        });
        text1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                targets.updateExcludeSuffix(text1.getText());
            }

        });

        // ---- TargetAdd ----
        JButton TargetAdd = new JButton();
        TargetAdd.setText("Add");
        TargetAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String text = JOptionPane.showInputDialog(null, "", "请输入目标URL", JOptionPane.PLAIN_MESSAGE);
                if (!text.isEmpty()) {
                    model.addRow(targets.add(text));
                    model = (DefaultTableModel) table.getModel();
                }
            }
        });
        add(TargetAdd, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(15, 5, 3, 2), 0, 0));

        // ---- Remove ----
        JButton TargetRemove = new JButton();
        TargetRemove.setText("Remove");
        TargetRemove.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int pos = 0;
                for (int row : table.getSelectedRows()) {
                    model.removeRow(table.convertRowIndexToModel(row - pos++));
                }
                model = (DefaultTableModel) table.getModel();
            }
        });
        add(TargetRemove, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 5, 3, 2), 0, 0));

        // ---- Load ----
        JButton TargetLoad = new JButton();
        TargetLoad.setText("Load");
        TargetLoad.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jFileChooser.setFileFilter(new FileNameExtensionFilter("TXT（每行一条目标）", "txt"));
                if (jFileChooser.showOpenDialog(new JLabel()) == JFileChooser.APPROVE_OPTION) {
                    File file = new File(jFileChooser.getSelectedFile().toString());
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            model.addRow(targets.add(line));
                        }
                        reader.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                model = (DefaultTableModel) table.getModel();
            }
        });
        add(TargetLoad, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(15, 5, 3, 2), 0, 0));

        // ---- table ----
        table = new JTable();
        table.setShowVerticalLines(false);
        table.setVerifyInputWhenFocusTarget(false);
        table.setUpdateSelectionOnSort(false);
        table.setShowHorizontalLines(false);
        table.setModel(new DefaultTableModel());
        table.setSurrendersFocusOnKeystroke(true);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        add(scrollPane, new GridBagConstraints(1, 1, 1, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(15, 5, 5, 5), 0, 0));

        // JFormDesigner - End of component initialization //GEN-END:initComponents
        table.setModel(model);
        model.setDataVector(targets.getArray(), new String[] { "#", "检测", "漏洞", "目标" });
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                model = (DefaultTableModel) table.getModel();
            }
        });
        table.setRowSorter(new TableRowSorter<>(model));
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(80);
        table.getColumnModel().getColumn(1).setMaxWidth(80);
        table.getColumnModel().getColumn(2).setMaxWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
    }

}
