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
        JMenuItem miUkoncit = new JMenuItem();
        miUkoncit.setText("Ukoncit");
        miUkoncit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int input = JOptionPane.showConfirmDialog(null, "Do you want to save changes tp current file? " + fileManager.getSelectedFile());
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

        JMenuItem miOProgramu = new JMenuItem("O programu");
        miOProgramu.addActionListener((e)->{
            JOptionPane.showMessageDialog(mainPanel, "O programu\nJednoduchy simulator e-shopu.",
                    "0 programu", JOptionPane.INFORMATION_MESSAGE);
        });

        JMenuItem miUlozJson = new JMenuItem("Ulož CSV");
        miUlozJson.addActionListener((e) -> {
            showSaveDialog();
        });

        JMenuItem miNactiJson = new JMenuItem("Načti CSV");
        miNactiJson.addActionListener((e) -> {
            showReadDialog();
        });

        JMenu mnSoubor = new JMenu();
        mnSoubor.setText("Soubor");
        mnSoubor.add(miUlozJson);
        mnSoubor.add(miUkoncit);
        mnSoubor.add(miNactiJson);

        JMenu mnNapoveda = new JMenu("Napoveda");
        mnNapoveda.add(miOProgramu);

        JMenuBar nabidka = new JMenuBar();
        nabidka.add(mnSoubor);
        nabidka.add(mnNapoveda);

        window.setJMenuBar(nabidka);
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
                String soubor = dialog.getSelectedFile().getPath();
                fileManager.write(soubor + ".csv", itemsAdapter.getListOfItems());
            }
        } catch (Exception exp) {
            JOptionPane.showMessageDialog(mainPanel,
                    "Při ukládání do CSV formátu nastala: "
                            + exp.getLocalizedMessage(), "Chyba ukládání",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void createPanel() {
        JTable tabulkaSkladu;

        tabulkaSkladu = new JTable();
        tabulkaSkladu.setModel(itemsAdapter);
        tabulkaSkladu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabulkaSkladu.setFillsViewportHeight(true);

        JScrollPane spTabulka = new JScrollPane(tabulkaSkladu);

        JButton btPridej = new JButton("Přidat");
        JButton btSmaz = new JButton("Smazat");
        JButton btSmazVse = new JButton("Smazat vše");

        JPanel pnTlacitka = new JPanel();
        pnTlacitka.setLayout(new GridLayout(0, 1));
        pnTlacitka.add(btPridej);
        pnTlacitka.add(btSmaz);
        pnTlacitka.add(btSmazVse);

        btSmaz.setEnabled(false);

        tabulkaSkladu.getSelectionModel().addListSelectionListener((e) -> {
            int radek = tabulkaSkladu.getSelectedRow();
            if(radek < 0) {
                btSmaz.setEnabled(false);
            } else {
                btSmaz.setEnabled(true);
            }
        });

        btPridej.addActionListener((e)-> {
            itemsAdapter.addItem(new Item("-", 0, 0));
        });

        btSmaz.addActionListener((e) -> {
            int radek = tabulkaSkladu.getSelectedRow();
            System.out.println(radek);
            itemsAdapter.removeItem(radek);
        });

        btSmazVse.addActionListener((e) -> {
            int confrimDialog = JOptionPane.showConfirmDialog(null, "Smažete všechny položky v tabulce!\nOpravdu chcete pokračovat?", "Varování!" ,JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(confrimDialog == JOptionPane.YES_OPTION) {
                itemsAdapter.clear();
            }
        });

        JPanel panelSkladu = new JPanel();
        panelSkladu.setLayout(new BorderLayout());
        panelSkladu.add(spTabulka, BorderLayout.CENTER);
        panelSkladu.add(pnTlacitka, BorderLayout.SOUTH);

        window.add(panelSkladu);
    }
}
