package com.rs.tools;

import com.rs.cache.Cache;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.utils.Logger;
import components.Configuration;
import components.frame.AppFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;

/**
 * Created by Peng on 5.2.2016.
 */
public class ItemDefEditor extends AppFrame implements ActionListener {

    static float version = 0.2f;

    String[] columnNames = {"Name", "Id", "Stab att", "Slash att", "Crush att", "Magic att",
            "Range att", "Stab def", "Slash def", "Crush def", "Magic def",
            "Range def", "Sum Def", "Melee abs", "Magic abs", "Range abs",
            "Str bonus", "Range str", "Pray bonus", "Magic dmg boost (%)"};
    Object[][] data = {{"Item name", 00, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};

    JTable dataTable = new JTable(new DefaultTableModel(data,columnNames) {
        Class[] types = { String.class, Integer.class, Short.class, Short.class, Short.class,
                Short.class, Short.class, Short.class, Short.class, Short.class, Short.class,
                Short.class, Short.class, Short.class, Short.class, Short.class, Short.class,
                Short.class, Short.class, Short.class};

        @Override
        public Class getColumnClass(int columnIndex) {
            return this.types[columnIndex];
        }
    });
    JMenuItem open = new JMenuItem("Open file"), pack = new JMenuItem("Repack"), addRow = new JMenuItem("Add row"), deleteRow = new JMenuItem("Delete row");

    public ItemDefEditor() {
        try {
            Cache.init();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cache not found");
        }

        menu.add(open);
        menu.add(pack);
        menu.add(addRow);
        menu.add(deleteRow);

        open.addActionListener(this);
        pack.addActionListener(this);
        addRow.addActionListener(this);
        deleteRow.addActionListener(this);

        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                menu.setLocation(getX(), getY() + 25);
            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }

        });

        bar.icon.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                menu.setVisible(!menu.isVisible());
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        gamePane.add(scrollPane);
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        dataTable.setFillsViewportHeight(true);
        dataTable.getColumn("Name").setMinWidth(100);
        dataTable.getColumn("Id").setMinWidth(25);
        dataTable.setAutoCreateRowSorter(true);
        setVisible(true);
    }

    JPopupMenu menu = new JPopupMenu();

    public static void main(String[] args) {
        Configuration.TITLE = "Item Definitions editor V" + version;
        Configuration.frameSize = new Dimension(300, 600);
        ItemDefEditor editor = new ItemDefEditor();
        editor.setLocationRelativeTo(null);
    }

    public void repack() {
        File file = getChosenFile();
        backupOld(file);
        if (saveItemBonuses(file))
            removeBackup(file);
        else
            backup(file);
    }

    public void backupOld(File f1) {
        File file = new File(f1 + "");
        if (!file.exists())
            return;
        file.renameTo(new File(file + ".temp"));
        System.out.println(file + ".temp");
    }

    public void backup(File f1) {
        File file = new File(f1 + ".temp");
        System.out.println(file + ".temp");
        if (!file.exists())
            return;
        file.renameTo(f1);
    }

    public void removeBackup(File f1) {
        File file = new File(f1 + ".temp");
        if (!file.exists())
            return;
        file.delete();
    }

    public boolean saveItemBonuses(File file) {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(new FileOutputStream(file.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            //this will never happen
            e.printStackTrace();
        }
        try {

            for (int index = 0; index < dataTable.getRowCount(); index++) {
                assert out != null;
                try {
                    if (dataTable.getValueAt(index, 1) instanceof String)
                        out.writeShort(Integer.parseInt((String) dataTable.getValueAt(index, 1)));
                    else
                        out.writeShort((int) dataTable.getValueAt(index, 1));

                    for (int i = 2; i < 20; i++) {
                        if (dataTable.getValueAt(index, i) instanceof String)
                            out.writeShort(Short.parseShort((String) dataTable.getValueAt(index, i)));
                        else
                            out.writeShort((Short) dataTable.getValueAt(index, i));
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this, "Invalid number located at row " + index);
                    out.flush();
                    out.close();
                    return false;
                }

            }
            JOptionPane.showMessageDialog(this, "Done Packing item bonuses...");
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void openFile() {
        File file = getChosenFile();
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "File not found!");
            return;
        } else {
            loadItemBonuses(file);
        }
    }

    public void updateDataTable() {
        short[] bonuses;
        ItemDefinitions defs;
        int index = 0;
        data = new Object[itemBonuses.size()][];
        for (int id : itemBonuses.keySet()) {
            bonuses = itemBonuses.get(id);
            if (bonuses == null)
                continue;
            defs = ItemDefinitions.getItemDefinitions(id);
            data[index] = new Object[]{defs != null ? defs.getName() : "Null", id, bonuses[0], bonuses[1], bonuses[2], bonuses[3], bonuses[4],
                    bonuses[5], bonuses[6], bonuses[7], bonuses[8], bonuses[9], bonuses[10], bonuses[11],
                    bonuses[12], bonuses[13], bonuses[14], bonuses[15], bonuses[16], bonuses[17]};
            index++;
        }
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        for (int i = 0; i < model.getRowCount(); i++)
            model.removeRow(i);
        for (int i = 0; i < itemBonuses.size(); i++) {
            if (data[i] == null)
                continue;
            model.addRow(data[i]);
        }
    }

    private HashMap<Integer, short[]> itemBonuses;

    private void loadItemBonuses(File file) {
        try {
            RandomAccessFile in = new RandomAccessFile(file.getAbsolutePath(), "r");
            FileChannel channel = in.getChannel();
            ByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            itemBonuses = new HashMap<>(buffer.remaining() / 38);
            while (buffer.hasRemaining()) {
                int itemId = buffer.getShort() & 0xffff;
                short[] bonuses = new short[18];
                for (int index = 0; index < bonuses.length; index++)
                    bonuses[index] = buffer.getShort();
                itemBonuses.put(itemId, bonuses);
            }
            channel.close();
            in.close();
        } catch (Throwable e) {
            Logger.handle(e);
        }
        updateDataTable();
    }

    public File getChosenFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(this);
        return fileChooser.getSelectedFile();
    }

    public void insertRow() {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.insertRow(0, new Object[]{"New item", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    }

    public void deleteRow() {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        while (dataTable.getSelectedRows().length != 0) {
            model.removeRow(dataTable.getSelectedRows()[0]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JMenuItem) {
            JMenuItem item = (JMenuItem) e.getSource();
            menu.setVisible(false);
            switch (item.getText()) {
                case "Open file":
                    openFile();
                    break;
                case "Repack":
                    repack();
                    break;
                case "Add row":
                    insertRow();
                    break;
                case "Delete row":
                    deleteRow();
                    break;
                default:
                    System.out.println(item.getText() + " clicked.");
            }
        }
    }
}
