package gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class CustomTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private static final String[] columnNames = new String[]{"Regras", "Pesos"};
	protected List<Regra> Regras;
	protected Class<?>[] types = new Class[]{String.class, Double.class};

	public CustomTableModel(List<Regra> Regras)
	{
		this.Regras = Regras;
		fireTableDataChanged();
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		return columnNames[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return types[columnIndex];
	}

	@Override
	public boolean isCellEditable(int row, int columnIndex)
	{
		if(columnIndex == 1) {
			return true;
		}
		return false;
	}

	@Override
	public Object getValueAt(int row, int column)
	{
		if(row < 0 || row >= Regras.size()) return null;
		Regra obj = Regras.get(row);
		switch(column) {
		case 0: return obj.getName();
		case 1: return obj.getPeso();
		default: return null;
		}

	}

	public void setValueAt(double value, int row, int col) {
		Regras.get(row).setPeso(value);
		fireTableCellUpdated(row, col);
		fireTableDataChanged();
	}

	@Override
	public int getRowCount()
	{
		return Regras.size();
	}

	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}
}