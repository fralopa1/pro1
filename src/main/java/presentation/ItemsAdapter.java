package presentation;

import model.Item;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends AbstractTableModel {

    private static final int COLUMN_COUNT = 3;

    private static final int NAME = 0;
    private static final int PRICE = 1;
    private static final int AMOUNT = 2;

    private List<Item> listOfItems = new ArrayList<>();

    public List<Item> getListOfItems() {
        return listOfItems;
    }

    public Item getItem(int pos){
        return listOfItems.get(pos);
    }

    @Override
    public int getRowCount() {
        return listOfItems.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case NAME -> "Name";
            case PRICE -> "Price";
            case AMOUNT -> "Amount";
            default -> "";
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Item zbozi = listOfItems.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return zbozi.getName();
            case 1:
                return zbozi.getPrice();
            case 2:
                return zbozi.getCount();
            default:
                throw new IllegalArgumentException("Nespravny sloupec tabulky skladu.");
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object hodnota, int rowIndex, int columnIndex) {
        Item zbozi = listOfItems.get(rowIndex);

        try {
            switch (columnIndex) {
                case NAME:
                    zbozi.setName(hodnota.toString());
                    break;
                case PRICE:
                    zbozi.setPrice(Float.parseFloat(hodnota.toString()));
                    break;
                case AMOUNT:
                    zbozi.setCount(Integer.parseInt(hodnota.toString()));
                    break;
                default:
                    throw new IllegalArgumentException("AAAAAAAAAAAA WHAT IS GOING ON");
            }
        } catch (Exception e) {

        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void addItem(Item item) {
        listOfItems.add(item);
        fireTableChanged(null);
    }

    public void loadList(List<Item> items){
        listOfItems.clear();
        listOfItems.addAll(items);
        fireTableChanged(null);
    }

    public void removeItem(int index) {
        if (index >= 0 && index < listOfItems.size()) {
            listOfItems.remove(index);
            fireTableRowsDeleted(index, index);
        }
    }
    public void clear() {
        listOfItems.clear();
        fireTableChanged(null);
    }
}
