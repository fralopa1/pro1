package presentation;

import data.FileManager;
import model.Item;
import data.Mapper;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ShopApp {

    private static JFrame window;
    private static JPanel mainPanel;

    private static FileManager fileManager = new FileManager();
    static ItemsAdapter itemsAdapter = new ItemsAdapter();

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            createMainWindow();
        });
    }

    private static void createMainWindow() {
        List<List<String>> list = fileManager.read();

        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("Obchod (" + fileManager.getSelectedFile() + ")");
        window.setLocationByPlatform(true);
        window.pack();
        window.setSize(500, 500);

        itemsAdapter.loadList(Mapper.toItemModel(list));

        window.setVisible(true);

        createComponents();
        createPanel();
    }

    private static void createComponents() {
        createActionBar();
        mainPanel = new JPanel();
        createActionBar();

    }

    private static void createActionBar(){
        JMenuItem exit = new JMenuItem();
        exit.setText("Ukoncit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int input = JOptionPane.showConfirmDialog(null, "Chcete uložit změny do aktuálního souboru? " + fileManager.getSelectedFile());
                switch (input) {
                    case 0: {
                        try {
                            fileManager.saveCurrentFile(itemsAdapter.getListOfItems());
                        } catch (Exception exp) {
                            JOptionPane.showMessageDialog(mainPanel,
                                    "Při ukládání do CSV formátu nastala: "
                                            + exp.getLocalizedMessage(), "Chyba ukládání",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    case 1: System.exit(0);
                    default:
                }
            }
        });

        JMenuItem aboutProgram = new JMenuItem("O programu");
        aboutProgram.addActionListener((e)->{
            JOptionPane.showMessageDialog(mainPanel, "O programu\nJednoduchy simulator e-shopu.",
                    "0 programu", JOptionPane.INFORMATION_MESSAGE);
        });

        JMenuItem saveCsv = new JMenuItem("Ulož CSV");
        saveCsv.addActionListener((e) -> {
            showSaveDialog();
        });

        JMenuItem loadCsv = new JMenuItem("Načti CSV");
        loadCsv.addActionListener((e) -> {
            showReadDialog();
        });

        JMenu mnFile = new JMenu();
        mnFile.setText("Soubor");
        mnFile.add(saveCsv);
        mnFile.add(exit);
        mnFile.add(loadCsv);

        JMenu mnHelp = new JMenu("Napoveda");
        mnHelp.add(aboutProgram);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(mnFile);
        menuBar.add(mnHelp);

        window.setJMenuBar(menuBar);
    }

    private static void showReadDialog() {
        try {
            JFileChooser dialog = new JFileChooser(".");
            dialog.setFileFilter(new FileNameExtensionFilter("*.csv", "csv"));
            if (dialog.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
                String file = dialog.getSelectedFile().getPath();
                List<List<String>> dataString = fileManager.read(file);
                List<Item> list = Mapper.toItemModel(dataString);
                itemsAdapter.loadList(list);
                window.setTitle("Obchod (" + fileManager.getSelectedFile() + ")");
            }
        } catch (Exception exp) {
            JOptionPane.showMessageDialog(mainPanel,
                    "Při načítání do CSV formátu nastala: "
                            + exp.getLocalizedMessage(), "Chyba načítání",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void showSaveDialog() {
        try {
            JFileChooser dialog = new JFileChooser(".");
            dialog.setFileFilter(new FileNameExtensionFilter("*.csv", "csv"));
            if (dialog.showSaveDialog(window) == JFileChooser.APPROVE_OPTION) {
                String file = dialog.getSelectedFile().getPath();
                fileManager.write(file + ".csv", itemsAdapter.getListOfItems());
            }
        } catch (Exception exp) {
            JOptionPane.showMessageDialog(mainPanel,
                    "Při ukládání do CSV formátu nastala: "
                            + exp.getLocalizedMessage(), "Chyba ukládání",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void createPanel() {
        JTable storeTable;

        storeTable = new JTable();
        storeTable.setModel(itemsAdapter);
        storeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        storeTable.setFillsViewportHeight(true);

        JScrollPane spTable = new JScrollPane(storeTable);

        JButton btAdd = new JButton("Přidat");
        JButton btDelete = new JButton("Smazat");
        JButton btDeleteAll = new JButton("Smazat vše");

        JPanel pnButtons = new JPanel();
        pnButtons.setLayout(new GridLayout(0, 1));
        pnButtons.add(btAdd);
        pnButtons.add( btDelete);
        pnButtons.add(btDeleteAll);

         btDelete.setEnabled(false);

        storeTable.getSelectionModel().addListSelectionListener((e) -> {
            int radek = storeTable.getSelectedRow();
            if(radek < 0) {
                 btDelete.setEnabled(false);
            } else {
                 btDelete.setEnabled(true);
            }
        });

        btAdd.addActionListener((e)-> {
            itemsAdapter.addItem(new Item("-", 0, 0));
        });

         btDelete.addActionListener((e) -> {
            int line = storeTable.getSelectedRow();
            System.out.println(line);
            itemsAdapter.removeItem(line);
        });

        btDeleteAll.addActionListener((e) -> {
            int confrimDialog = JOptionPane.showConfirmDialog(null, "Smažete všechny položky v tabulce!\nOpravdu chcete pokračovat?", "Varování!" ,JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(confrimDialog == JOptionPane.YES_OPTION) {
                itemsAdapter.clear();
            }
        });

        JPanel storePanel = new JPanel();
        storePanel.setLayout(new BorderLayout());
        storePanel.add(spTable, BorderLayout.CENTER);
        storePanel.add(pnButtons, BorderLayout.SOUTH);

        window.add(storePanel);
    }
}
