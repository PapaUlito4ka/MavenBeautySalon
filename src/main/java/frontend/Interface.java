package frontend;

import backend.*;
import exceptions.*;
import models.Worker;
import net.sf.jasperreports.engine.JRException;
import org.javatuples.Pair;
import reports.Reporting;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

enum ImgPath {
    DATES_TABLE("./src/main/java/frontend/images/list-icon.png"),
    WORKERS_TABLE("./src/main/java/frontend/images/list-icon.png"),
    CLIENTS_TABLE("./src/main/java/frontend/images/list-icon.png"),
    SERVICES_TABLE("./src/main/java/frontend/images/list-icon.png"),
    PLUS("./src/main/java/frontend/images/plus-icon.png"),
    EDIT("./src/main/java/frontend/images/edit-icon.png"),
    DELETE("./src/main/java/frontend/images/delete-icon.png"),
    SEARCH("./src/main/java/frontend/images/search-icon.png"),
    PDF("./src/main/java/frontend/images/pdf-icon.png");
    private String path;
    ImgPath(String path){
        this.path = path;
    }
    public String getPath(){ return path; }
}

class MultiLineTableCellRenderer extends JList<String> implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof String[]) {
            setListData((String[]) value);
        }

        if (isSelected) {
            setBackground(UIManager.getColor("Table.selectionBackground"));
            setForeground(Color.WHITE);
        } else {
            setBackground(UIManager.getColor("Table.background"));
            setForeground(Color.BLACK);
        }
        return this;
    }
}

enum Table {
    RESERVE_DATES,
    WORKERS,
    CLIENTS,
    SERVICES
}

public class Interface {
    private final JFrame window;
    private final Backend backend;
    private final int PIC_W = 30;
    private final int PIC_H = 30;
    private final int rowHeight = 17;

    private JButton reserveDatesButton;
    private JButton workersButton;
    private JButton clientsButton;
    private JButton servicesButton;

    private JScrollPane scrollPane;
    private Object[][] tableData;
    private JTable table;

    public Interface(JFrame window) {
        this.window = window;
        backend = new Backend();
        buildInterface(Table.RESERVE_DATES);
    }

    private void buildInterface(Table table) {
        if (table == Table.RESERVE_DATES) {
            buildToolbar(Table.RESERVE_DATES);
            buildReserveDatesTable(backend.getReserveDatesTable());
            buildReserveDatesFilterPanel();
        }
        else if (table == Table.WORKERS) {
            buildToolbar(Table.WORKERS);
            buildWorkersTable(backend.getWorkersTable());
            buildWorkersFilterPanel();
        }
        else if (table == Table.CLIENTS) {
            buildToolbar(Table.CLIENTS);
            buildClientsTable(backend.getClientsTable());
            buildClientsFilterPanel();
        }
        else if (table == Table.SERVICES) {
            buildToolbar(Table.SERVICES);
            buildServicesTable(backend.getServicesTable());
            buildServicesFilterPanel();
        }
    }

    private void buildToolbar(Table tableType) {
        reserveDatesButton = new JButton(loadImageIcon(ImgPath.DATES_TABLE.getPath(), PIC_W, PIC_H));
        reserveDatesButton.setToolTipText("Show reservations");
        workersButton = new JButton(loadImageIcon(ImgPath.WORKERS_TABLE.getPath(), PIC_W, PIC_H));
        workersButton.setToolTipText("Show workers");
        clientsButton = new JButton(loadImageIcon(ImgPath.CLIENTS_TABLE.getPath(), PIC_W, PIC_H));
        clientsButton.setToolTipText("Show clients");
        servicesButton = new JButton(loadImageIcon(ImgPath.SERVICES_TABLE.getPath(), PIC_W, PIC_H));
        servicesButton.setToolTipText("Show services");
        JButton reserveButton = new JButton(loadImageIcon(ImgPath.PLUS.getPath(), PIC_W, PIC_H));
        reserveButton.setToolTipText("Reserve");
        JButton editButton = new JButton(loadImageIcon(ImgPath.EDIT.getPath(), PIC_W, PIC_H));
        editButton.setToolTipText("Edit record");
        JButton deleteButton = new JButton(loadImageIcon(ImgPath.DELETE.getPath(), PIC_W, PIC_H));
        deleteButton.setToolTipText("Delete record");
        JButton pdfButton = new JButton(loadImageIcon(ImgPath.PDF.getPath(), PIC_W, PIC_H));
        pdfButton.setToolTipText("Create pdf report");

        reserveDatesButton.addActionListener(e -> {
            window.getContentPane().removeAll();
            buildToolbar(Table.RESERVE_DATES);
            buildReserveDatesTable(backend.getReserveDatesTable());
            buildReserveDatesFilterPanel();
            window.getContentPane().revalidate();
            window.getContentPane().repaint();
        });
        workersButton.addActionListener(e -> {
            window.getContentPane().removeAll();
            buildToolbar(Table.WORKERS);
            buildWorkersTable(backend.getWorkersTable());
            buildWorkersFilterPanel();
            window.getContentPane().revalidate();
            window.getContentPane().repaint();
        });
        clientsButton.addActionListener(e -> {
            window.getContentPane().removeAll();
            buildToolbar(Table.CLIENTS);
            buildClientsTable(backend.getClientsTable());
            buildClientsFilterPanel();
            window.getContentPane().revalidate();
            window.getContentPane().repaint();
        });
        servicesButton.addActionListener(e -> {
            window.getContentPane().removeAll();
            buildToolbar(Table.SERVICES);
            buildServicesTable(backend.getServicesTable());
            buildServicesFilterPanel();
            window.getContentPane().revalidate();
            window.getContentPane().repaint();
        });

        if (tableType == Table.RESERVE_DATES) {
            reserveButton.addActionListener(e -> newReserveDatePane());
            editButton.addActionListener(e -> {
                try {
                    InterfaceValidation.checkSelectedTableRow(table);
                    editReserveDatePane();
                }
                catch (TableRowNotSelectedException exc) {
                    JOptionPane.showMessageDialog(window, exc.getMessage());
                }
            });
            deleteButton.addActionListener(e -> {
                try {
                    InterfaceValidation.checkSelectedTableRow(table);
                    deleteReserveDatePane();
                }
                catch (TableRowNotSelectedException exc) {
                    JOptionPane.showMessageDialog(window, exc.getMessage());
                }
            });
        }
        else if (tableType == Table.WORKERS) {
            reserveButton.addActionListener(e -> newWorkerPane());
            editButton.addActionListener(e -> {
                try {
                    InterfaceValidation.checkSelectedTableRow(table);
                    editWorkerPane();
                }
                catch (TableRowNotSelectedException exc) {
                    JOptionPane.showMessageDialog(window, exc.getMessage());
                }
            });
            deleteButton.addActionListener(e -> {
                try {
                    InterfaceValidation.checkSelectedTableRow(table);
                    deleteWorkerPane();
                }
                catch (TableRowNotSelectedException exc) {
                    JOptionPane.showMessageDialog(window, exc.getMessage());
                }
            });
        }
        else if (tableType == Table.CLIENTS) {
            reserveButton.addActionListener(e -> newClientPane());
            editButton.addActionListener(e -> {
                try {
                    InterfaceValidation.checkSelectedTableRow(table);
                    editClientPane();
                }
                catch (TableRowNotSelectedException exc) {
                    JOptionPane.showMessageDialog(window, exc.getMessage());
                }
            });
            deleteButton.addActionListener(e -> {
                try {
                    InterfaceValidation.checkSelectedTableRow(table);
                    deleteClientPane();
                }
                catch (TableRowNotSelectedException exc) {
                    JOptionPane.showMessageDialog(window, exc.getMessage());
                }
            });
        }
        else if (tableType == Table.SERVICES) {
            reserveButton.addActionListener(e -> newServicePane());
            editButton.addActionListener(e -> {
                try {
                    InterfaceValidation.checkSelectedTableRow(table);
                    editServicePane();
                }
                catch (TableRowNotSelectedException exc) {
                    JOptionPane.showMessageDialog(window, exc.getMessage());
                }
            });
            deleteButton.addActionListener(e -> {
                try {
                    InterfaceValidation.checkSelectedTableRow(table);
                    deleteServicePane();
                }
                catch (TableRowNotSelectedException exc) {
                    JOptionPane.showMessageDialog(window, exc.getMessage());
                }
            });
        }

        pdfButton.addActionListener(e -> {
            JPanel panel = new JPanel();
            JRadioButton week = new JRadioButton("week");
            week.setSelected(true);
            JRadioButton month = new JRadioButton("month");
            ButtonGroup bg = new ButtonGroup();
            bg.add(week);
            bg.add(month);
            panel.add(week);
            panel.add(month);
            int option = JOptionPane.showConfirmDialog(null, panel,"Select period", JOptionPane.OK_CANCEL_OPTION);
            if (option == 2 || option == -1) return;
            Runnable task = () -> {
                boolean isLastMonthSelected = month.isSelected();
                try {
                    new Reporting(isLastMonthSelected).makePdf();
                    JOptionPane.showMessageDialog(window, "Successfully created!");
                } catch (FileNotFoundException | JRException exc) {
                    JOptionPane.showMessageDialog(window, exc.getMessage());
                }
            };
            Thread thread = new Thread(task);
            thread.start();
        });

        JToolBar toolBar = new JToolBar("ToolBar");
        toolBar.add(reserveDatesButton);
        toolBar.add(workersButton);
        toolBar.add(clientsButton);
        toolBar.add(servicesButton);
        toolBar.add(reserveButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        toolBar.add(pdfButton);
        toolBar.setFloatable(false);

        window.add(toolBar, BorderLayout.NORTH);
    }

    private void buildReserveDatesTable(ArrayList<ReserveDateTableData> data) {
        String [] cols = new String[] {"Date", "From", "To", "Client", "Worker", "Service", "Price"};
        String [][] dataString = new String[data.size()][7];
        tableData = new Object[data.size()][8];
        for (int i = 0; i < data.size(); i++) {
            dataString[i][0] = data.get(i).date;
            dataString[i][1] = data.get(i).fromTime;
            dataString[i][2] = data.get(i).toTime;
            dataString[i][3] = data.get(i).client;
            dataString[i][4] = data.get(i).worker;
            dataString[i][5] = data.get(i).service;
            dataString[i][6] = data.get(i).price;
            tableData[i] = data.get(i).toArray();
        }
        DefaultTableModel model = new DefaultTableModel(dataString, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setGridColor(Color.BLACK);
        scrollPane = new JScrollPane(table);
        window.add(scrollPane, BorderLayout.CENTER);
    }

    private void buildWorkersTable(ArrayList<WorkerTableData> data) {
        String [] cols = new String[] {"Name", "Surname", "Speciality", "Services"};
        Object [][] dataString = new Object[data.size()][4];
        tableData = new Object[data.size()][5];
        for (int i = 0; i < data.size(); i++) {
            dataString[i][0] = data.get(i).name;
            dataString[i][1] = data.get(i).surname;
            dataString[i][2] = data.get(i).spec;
            dataString[i][3] = data.get(i).services;
            tableData[i] = data.get(i).toArray();
        }
        DefaultTableModel model = new DefaultTableModel(dataString, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setGridColor(Color.BLACK);
        for (int i = 0; i < data.size(); i++) {
            int height = data.get(i).services.length == 0 ? rowHeight : data.get(i).services.length * rowHeight;
            table.setRowHeight(i, height);
        }
        MultiLineTableCellRenderer renderer = new MultiLineTableCellRenderer();
        table.getColumnModel().getColumn(3).setCellRenderer(renderer);
        scrollPane = new JScrollPane(table);
        window.add(scrollPane, BorderLayout.CENTER);
    }

    private void buildClientsTable(ArrayList<ClientTableData> data) {
        String [] cols = new String[] {"Name", "Surname", "Reserve Dates", "Services"};
        Object [][] dataString = new Object[data.size()][4];
        tableData = new Object[data.size()][5];
        for (int i = 0; i < data.size(); i++) {
            dataString[i][0] = data.get(i).name;
            dataString[i][1] = data.get(i).surname;
            dataString[i][2] = data.get(i).reserveDates;
            dataString[i][3] = data.get(i).services;
            tableData[i] = data.get(i).toArray();
        }
        DefaultTableModel model = new DefaultTableModel(dataString, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setGridColor(Color.BLACK);
        for (int i = 0; i < data.size(); i++) {
            int height = data.get(i).reserveDates.length == 0 ? rowHeight : data.get(i).reserveDates.length * rowHeight;
            table.setRowHeight(i, height);
        }
        MultiLineTableCellRenderer renderer = new MultiLineTableCellRenderer();
        table.getColumnModel().getColumn(2).setCellRenderer(renderer);
        table.getColumnModel().getColumn(3).setCellRenderer(renderer);
        scrollPane = new JScrollPane(table);
        window.add(scrollPane, BorderLayout.CENTER);
    }

    private void buildServicesTable(ArrayList<ServiceTableData> data) {
        String [] cols = new String[] { "Name", "Price", "Workers" };
        Object [][] dataString = new Object[data.size()][3];
        tableData = new Object[data.size()][4];
        for (int i = 0; i < data.size(); i++) {
            dataString[i][0] = data.get(i).name;
            dataString[i][1] = data.get(i).price;
            dataString[i][2] = data.get(i).workers;
            tableData[i] = data.get(i).toArray();
        }
        DefaultTableModel model = new DefaultTableModel(dataString, cols) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setGridColor(Color.BLACK);
        for (int i = 0; i < data.size(); i++) {
            int height = data.get(i).workers.length == 0 ? rowHeight : data.get(i).workers.length * rowHeight;
            table.setRowHeight(i, height);
        }
        MultiLineTableCellRenderer renderer = new MultiLineTableCellRenderer();
        table.getColumnModel().getColumn(2).setCellRenderer(renderer);
        scrollPane = new JScrollPane(table);
        window.add(scrollPane, BorderLayout.CENTER);
    }

    private void buildReserveDatesFilterPanel() {
        JPanel filtersPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JPanel layer1Panel = new JPanel(new GridLayout(4, 1));
        JComboBox<String> dateFrom;
        JComboBox<String> dateTo;
        if (tableData.length != 0) {
            dateFrom = new JComboBox<>(
                    backend.generateDates((String) tableData[tableData.length - 1][1], (String) tableData[0][1]).toArray(String[]::new)
            );
            dateFrom.setSelectedIndex(dateFrom.getItemCount()-1);
            dateTo = new JComboBox<>(
                    backend.generateDates((String) tableData[tableData.length - 1][1], (String) tableData[0][1]).toArray(String[]::new)
            );
        }
        else {
            dateFrom = new JComboBox<>(new String[] { LocalDate.now().toString() });
            dateTo = new JComboBox<>(new String[] { LocalDate.now().toString() });
        }
        layer1Panel.add(new Label("Date from"));
        layer1Panel.add(dateFrom);
        layer1Panel.add(new Label("Date to"));
        layer1Panel.add(dateTo);

        JPanel layer2Panel = new JPanel(new GridLayout(4, 1));
        JComboBox<String> timeFrom = new JComboBox<>(
                backend.generateReserveTime().stream().map(LocalTime::toString).toArray(String[]::new)
        );
        JComboBox<String> timeTo = new JComboBox<>(
                backend.generateReserveTime().stream().map(LocalTime::toString).toArray(String[]::new)
        );
        timeTo.setSelectedIndex(timeTo.getItemCount()-1);
        layer2Panel.add(new Label("Time from"));
        layer2Panel.add(timeFrom);
        layer2Panel.add(new Label("Time to"));
        layer2Panel.add(timeTo);

        JPanel layer3Panel = new JPanel(new GridLayout(4, 1));
        JTextField clientText = new JTextField("", 10);
        JTextField workerText = new JTextField("", 10);
        layer3Panel.add(new Label("Client"));
        layer3Panel.add(clientText);
        layer3Panel.add(new JLabel("Worker"));
        layer3Panel.add(workerText);

        JPanel layer4Panel = new JPanel(new GridLayout(4, 1));
        JTextField priceFrom = new JTextField("", 10);
        JTextField priceTo = new JTextField("", 10);
        layer4Panel.add(new Label("Price from"));
        layer4Panel.add(priceFrom);
        layer4Panel.add(new Label("Price to"));
        layer4Panel.add(priceTo);

        JPanel layer5Panel = new JPanel(new GridLayout(1, 1));
        JButton filterButton = new JButton(loadImageIcon(ImgPath.SEARCH.getPath(), PIC_W, PIC_H));
        filterButton.addActionListener(e -> {
            try {
                ReserveDateFilter settings = new ReserveDateFilter(
                        (String) dateFrom.getSelectedItem(),
                        (String) dateTo.getSelectedItem(),
                        (String) timeFrom.getSelectedItem(),
                        (String) timeTo.getSelectedItem(),
                        clientText.getText(),
                        workerText.getText(),
                        priceFrom.getText(),
                        priceTo.getText()
                );
                filterReserveDatesTable(settings);
            }
            catch (Exception exc) {
                JOptionPane.showMessageDialog(window, exc.getMessage());
            }
        });
        layer5Panel.add(filterButton);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 4;
        c.gridheight = 1;
        filtersPanel.add(layer5Panel, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = 1;
        c.gridy = 0;
        c.gridx = 0;
        c.gridheight = 3;
        c.gridwidth = 1;
        filtersPanel.add(layer1Panel, c);
        c.gridx = 1;
        filtersPanel.add(layer2Panel, c);
        c.gridx = 2;
        filtersPanel.add(layer3Panel, c);
        c.gridx = 3;
        filtersPanel.add(layer4Panel, c);

        window.add(filtersPanel, BorderLayout.SOUTH);
    }

    private void buildWorkersFilterPanel() {
        JPanel filtersPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JPanel layer1Panel = new JPanel(new GridLayout(4, 1));
        JTextField nameText = new JTextField("", 10);
        layer1Panel.add(new Label("Name"));
        layer1Panel.add(nameText);
        layer1Panel.add(new Label(""));
        layer1Panel.add(new Label(""));

        JPanel layer2Panel = new JPanel(new GridLayout(4, 1));
        JTextField surnameText = new JTextField("", 10);
        layer2Panel.add(new Label("Surname"));
        layer2Panel.add(surnameText);
        layer2Panel.add(new Label(""));
        layer2Panel.add(new Label(""));

        JPanel layer3Panel = new JPanel(new GridLayout(4, 1));
        JTextField specText = new JTextField("", 10);
        layer3Panel.add(new Label("Speciality"));
        layer3Panel.add(specText);
        layer3Panel.add(new Label(""));
        layer3Panel.add(new Label(""));

        JPanel layer4Panel = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        JList<String> servicesList = new JList<>(
                backend.getUniqueServices().toArray(String[]::new)
        );
        servicesList.setVisibleRowCount(4);
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = 1;
        c.ipadx = 50;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        layer4Panel.add(new Label("Services"), c);
        c.gridy = 1;
        c.gridheight = 3;
        c.gridwidth = 1;
        layer4Panel.add(new JScrollPane(servicesList), c);

        JPanel layer5Panel = new JPanel(new GridLayout(1, 1));
        JButton filterButton = new JButton(loadImageIcon(ImgPath.SEARCH.getPath(), PIC_W, PIC_H));
        filterButton.addActionListener(e -> {
            try {
                WorkerFilter settings = new WorkerFilter(
                        nameText.getText(),
                        surnameText.getText(),
                        specText.getText(),
                        servicesList.getSelectedValuesList().toArray(String[]::new)
                );
                filterWorkersTable(settings);
            }
            catch (Exception exc) {
                JOptionPane.showMessageDialog(window, exc.getMessage());
            }
        });
        layer5Panel.add(filterButton);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 4;
        c.gridheight = 1;
        filtersPanel.add(layer5Panel, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = 1;
        c.gridy = 0;
        c.gridx = 0;
        c.gridheight = 3;
        c.gridwidth = 1;
        filtersPanel.add(layer1Panel, c);
        c.gridx = 1;
        filtersPanel.add(layer2Panel, c);
        c.gridx = 2;
        filtersPanel.add(layer3Panel, c);
        c.gridx = 3;
        filtersPanel.add(layer4Panel, c);

        window.add(filtersPanel, BorderLayout.SOUTH);
    }

    private void buildClientsFilterPanel() {
        JPanel filtersPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JPanel layer1Panel = new JPanel(new GridLayout(4, 1));
        JTextField nameText = new JTextField("", 10);
        layer1Panel.add(new Label("Name"));
        layer1Panel.add(nameText);
        layer1Panel.add(new Label(""));
        layer1Panel.add(new Label(""));

        JPanel layer2Panel = new JPanel(new GridLayout(4, 1));
        JTextField surnameText = new JTextField("", 10);
        layer2Panel.add(new Label("Surname"));
        layer2Panel.add(surnameText);
        layer2Panel.add(new Label(""));
        layer2Panel.add(new Label(""));

        JPanel layer3Panel = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        JList<String> reserveDatesList = new JList<>(
                backend.getUniqueReserveDates().toArray(String[]::new)
        );
        reserveDatesList.setVisibleRowCount(4);
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = 1;
        c.ipadx = 50;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        layer3Panel.add(new Label("Reserve dates"), c);
        c.gridy = 1;
        c.gridheight = 3;
        c.gridwidth = 1;
        layer3Panel.add(new JScrollPane(reserveDatesList), c);

        JPanel layer4Panel = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        JList<String> servicesList = new JList<>(
                backend.getUniqueServices().toArray(String[]::new)
        );
        servicesList.setVisibleRowCount(4);
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = 1;
        c.ipadx = 50;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        layer4Panel.add(new Label("Services"), c);
        c.gridy = 1;
        c.gridheight = 3;
        c.gridwidth = 1;
        layer4Panel.add(new JScrollPane(servicesList), c);

        JPanel layer5Panel = new JPanel(new GridLayout(1, 1));
        JButton filterButton = new JButton(loadImageIcon(ImgPath.SEARCH.getPath(), PIC_W, PIC_H));
        filterButton.addActionListener(e -> {
            try {
                ClientFilter settings = new ClientFilter(
                        nameText.getText(),
                        surnameText.getText(),
                        reserveDatesList.getSelectedValuesList().toArray(String[]::new),
                        servicesList.getSelectedValuesList().toArray(String[]::new)
                );
                filterClientsTable(settings);
            }
            catch (Exception exc) {
                JOptionPane.showMessageDialog(window, exc.getMessage());
            }
        });
        layer5Panel.add(filterButton);
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 4;
        c.gridheight = 1;
        filtersPanel.add(layer5Panel, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = 1;
        c.gridy = 0;
        c.gridx = 0;
        c.gridheight = 3;
        c.gridwidth = 1;
        filtersPanel.add(layer1Panel, c);
        c.gridx = 1;
        filtersPanel.add(layer2Panel, c);
        c.gridx = 2;
        filtersPanel.add(layer3Panel, c);
        c.gridx = 3;
        filtersPanel.add(layer4Panel, c);

        window.add(filtersPanel, BorderLayout.SOUTH);
    }

    private void buildServicesFilterPanel() {
        JPanel filtersPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JPanel layer1Panel = new JPanel(new GridLayout(4, 1));
        JTextField nameText = new JTextField("", 10);
        layer1Panel.add(new Label("Name"));
        layer1Panel.add(nameText);
        layer1Panel.add(new Label(""));
        layer1Panel.add(new Label(""));

        JPanel layer2Panel = new JPanel(new GridLayout(4, 1));
        JTextField priceFrom = new JTextField("", 10);
        JTextField priceTo = new JTextField("", 10);
        layer2Panel.add(new Label("Price from"));
        layer2Panel.add(priceFrom);
        layer2Panel.add(new Label("Price to"));
        layer2Panel.add(priceTo);

        JPanel layer3Panel = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        JList<String> workersList = new JList<>(
                backend.getUniqueWorkers().toArray(String[]::new)
        );
        workersList.setVisibleRowCount(4);
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = 1;
        c.ipadx = 50;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 1;
        c.gridwidth = 1;
        layer3Panel.add(new Label("Reserve dates"), c);
        c.gridy = 1;
        c.gridheight = 3;
        c.gridwidth = 1;
        layer3Panel.add(new JScrollPane(workersList), c);

        JPanel layer4Panel = new JPanel(new GridLayout(1, 1));
        JButton filterButton = new JButton(loadImageIcon(ImgPath.SEARCH.getPath(), PIC_W, PIC_H));
        filterButton.addActionListener(e -> {
            try {
                ServiceFilter settings = new ServiceFilter(
                        nameText.getText(),
                        priceFrom.getText(),
                        priceTo.getText(),
                        workersList.getSelectedValuesList().toArray(String[]::new)
                );
                filterServicesTable(settings);
            }
            catch (Exception exc) {
                JOptionPane.showMessageDialog(window, exc.getMessage());
            }
        });
        layer4Panel.add(filterButton);
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 3;
        c.gridheight = 1;
        filtersPanel.add(layer4Panel, c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = 1;
        c.gridy = 0;
        c.gridx = 0;
        c.gridheight = 3;
        c.gridwidth = 1;
        filtersPanel.add(layer1Panel, c);
        c.gridx = 1;
        filtersPanel.add(layer2Panel, c);
        c.gridx = 2;
        filtersPanel.add(layer3Panel, c);

        window.add(filtersPanel, BorderLayout.SOUTH);
    }

    private int createDeletePane() {
        JLabel messageLabel = new JLabel("Are you sure you want to delete selected records?");
        return JOptionPane.showConfirmDialog(null, messageLabel,"Delete records", JOptionPane.OK_CANCEL_OPTION);
    }

    private Pair<Integer, ReserveDateTableData> createReserveDatePane(ReserveDateTableData data) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel dateLabel = new JLabel("Select date");
        JComboBox<String> dateBox = new JComboBox<>(
                backend.generateDates(LocalDate.now(), LocalDate.now().plusMonths(1)).toArray(String[]::new)
        );
        dateBox.setSelectedItem(data.date);
        JLabel serviceLabel = new JLabel("Select service");
        JComboBox<String> serviceBox = new JComboBox<>(
                backend.getUniqueServices().toArray(String[]::new)
        );
        serviceBox.setSelectedItem(data.service);
        JLabel workerLabel = new JLabel("Select worker");
        JComboBox<String> workerBox = new JComboBox<>(
                backend.getWorkersService((String) serviceBox.getSelectedItem()).toArray(String[]::new)
        );
        workerBox.setSelectedItem(data.worker);
        JLabel timeLabel = new JLabel("Select time");
        JComboBox<String> timeBox = new JComboBox<>(
                backend.getUnreservedTime((String) dateBox.getSelectedItem(),
                        (String) workerBox.getSelectedItem(), data.fromTime).toArray(String[]::new)
        );
        timeBox.setSelectedItem(data.fromTime);
        JLabel clientLabel = new JLabel("Select client");
        JComboBox<String> clientBox = new JComboBox<>(
                backend.getUniqueClients().toArray(String[]::new)
        );
        clientBox.setSelectedItem(data.client);
        JLabel priceLabel = new JLabel("Price");
        JTextField priceText = new JTextField(backend.getServicePrice((String) serviceBox.getSelectedItem()), 10);
        priceText.setEditable(false);

        serviceBox.addActionListener(e -> {
            workerBox.setModel(new DefaultComboBoxModel<>(
                    backend.getWorkersService((String) serviceBox.getSelectedItem()).toArray(String[]::new)
            ));
            timeBox.setModel(new DefaultComboBoxModel<>(
                    backend.getUnreservedTime((String) dateBox.getSelectedItem(),
                            (String) workerBox.getSelectedItem(), data.fromTime).toArray(String[]::new)
            ));
            priceText.setText(backend.getServicePrice((String) serviceBox.getSelectedItem()));
        });
        workerBox.addActionListener(e -> {
            timeBox.setModel(new DefaultComboBoxModel<>(
                    backend.getUnreservedTime((String) dateBox.getSelectedItem(),
                            (String) workerBox.getSelectedItem(), data.fromTime).toArray(String[]::new)
            ));
        });
        dateBox.addActionListener(e -> {
            timeBox.setModel(new DefaultComboBoxModel<>(
                    backend.getUnreservedTime((String) dateBox.getSelectedItem(),
                            (String) workerBox.getSelectedItem(), data.fromTime).toArray(String[]::new)
            ));
        });

        panel.add(dateLabel);
        panel.add(dateBox);
        panel.add(serviceLabel);
        panel.add(serviceBox);
        panel.add(workerLabel);
        panel.add(workerBox);
        panel.add(timeLabel);
        panel.add(timeBox);
        panel.add(clientLabel);
        panel.add(clientBox);
        panel.add(priceLabel);
        panel.add(priceText);
        int option = JOptionPane.showConfirmDialog(null, panel, "Reserve date", JOptionPane.OK_CANCEL_OPTION);
        option = option == -1 ? 2 : option;
        if (option == 2) return new Pair<>(
                option,
                data
        );

        String id = data.id;
        String date = (String) dateBox.getSelectedItem();
        String fromTime = (String) timeBox.getSelectedItem();
        String toTime = LocalTime.parse(fromTime).plusHours(1).toString();
        String client = (String) clientBox.getSelectedItem();
        String worker = (String) workerBox.getSelectedItem();
        String service = (String) serviceBox.getSelectedItem();
        String price = priceText.getText();

        ReserveDateTableData newData = new ReserveDateTableData(
                date,
                fromTime,
                toTime,
                client,
                worker,
                service,
                price,
                id
        );

        return new Pair<>(
                option,
                newData
        );
    }

    private void newReserveDatePane() {
        Pair<Integer, ReserveDateTableData> data = createReserveDatePane(new ReserveDateTableData());
        if (data.getValue0() == 2) return;
        try {
            backend.saveReserveDate(data.getValue1());
            reserveDatesButton.doClick();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window, e.getMessage());
        }
    }
    private void editReserveDatePane() {
        int row = table.getSelectedRow();
        ReserveDateTableData rowData = new ReserveDateTableData(
                (String) tableData[row][1],
                (String) tableData[row][2],
                (String) tableData[row][3],
                (String) tableData[row][4],
                (String) tableData[row][5],
                (String) tableData[row][6],
                (String) tableData[row][7],
                (String) tableData[row][0]
        );
        Pair<Integer, ReserveDateTableData> data = createReserveDatePane(rowData);
        if (data.getValue0() == 2 ||
            data.getValue1().equals(rowData)) return;
        try {
            backend.editReserveDate(data.getValue1());
            reserveDatesButton.doClick();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window, e.getMessage());
        }
    }
    private void deleteReserveDatePane() {
        int option = createDeletePane();
        if (option == 2) return;
        int[] rows = table.getSelectedRows();
        for (int i = 0; i < rows.length; i++) rows[i] = Integer.parseInt((String)tableData[rows[i]][0]);
        try {
            backend.deleteReserveDate(rows);
            reserveDatesButton.doClick();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window, e.getMessage());
        }
    }

    private Pair<Integer, WorkerTableData> createWorkerPane(WorkerTableData data) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel nameLabel = new JLabel("Enter name");
        JTextField nameText = new JTextField(data.name, 10);
        JLabel surnameLabel = new JLabel("Enter surname");
        JTextField surnameText = new JTextField(data.surname, 10);
        JLabel specLabel = new JLabel("Enter speciality");
        JTextField specText = new JTextField(data.spec, 10);
        JLabel servicesLabel = new JLabel("Select services");
        JPanel listPanel = new JPanel(new BorderLayout());
        String [] services = backend.getUniqueServices().toArray(String[]::new);
        ArrayList<Integer> selectedServices = new ArrayList<>();
        for (int i = 0; i < services.length; i++) {
            for (int j = 0; j < data.services.length; j++) {
                if (services[i].equals(data.services[j]))
                    selectedServices.add(i);
            }
        }
        JList<String> servicesList = new JList<>(services);
        servicesList.setSelectedIndices(selectedServices.stream().mapToInt(i -> i).toArray());
        servicesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listPanel.add(new JScrollPane(servicesList));

        panel.add(nameLabel);
        panel.add(nameText);
        panel.add(surnameLabel);
        panel.add(surnameText);
        panel.add(specLabel);
        panel.add(specText);
        panel.add(servicesLabel);
        panel.add(listPanel);

        int option = JOptionPane.showConfirmDialog(null, panel,"Worker", JOptionPane.OK_CANCEL_OPTION);
        option = option == -1 ? 2 : option;
        if (option == 2) return new Pair<>(
                option,
                data
        );

        String id = data.id;
        String name = nameText.getText();
        String surname = surnameText.getText();
        String spec = specText.getText();
        String[] services_ = servicesList.getSelectedValuesList().toArray(String[]::new);

        WorkerTableData newData = new WorkerTableData(
                name,
                surname,
                spec,
                services_,
                id
        );

        try {
            InterfaceValidation.checkWorkerData(newData);
        }
        catch (Exception exc) {
            JOptionPane.showMessageDialog(window, exc.getMessage());
            return new Pair<>(
                    2,
                    data
            );
        }
        return new Pair<>(
                option,
                newData
        );
    }

    private void newWorkerPane() {
        Pair<Integer, WorkerTableData> data = createWorkerPane(new WorkerTableData());
        if (data.getValue0() == 2) return;
        try {
            backend.saveWorker(data.getValue1());
            workersButton.doClick();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window, e.getMessage());
        }
    }
    private void editWorkerPane() {
        int row = table.getSelectedRow();
        WorkerTableData rowData = new WorkerTableData(
                (String) tableData[row][1],
                (String) tableData[row][2],
                (String) tableData[row][3],
                (String[]) tableData[row][4],
                (String) tableData[row][0]
        );
        Pair<Integer, WorkerTableData> data = createWorkerPane(rowData);
        if (data.getValue0() == 2 ||
                data.getValue1().equals(rowData)) return;
        try {
            backend.editWorker(data.getValue1());
            workersButton.doClick();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window, e.getMessage());
        }
    }
    private void deleteWorkerPane() {
        int option = createDeletePane();
        if (option == 2) return;
        int[] rows = table.getSelectedRows();
        for (int i = 0; i < rows.length; i++) rows[i] = Integer.parseInt((String)tableData[rows[i]][0]);
        try {
            backend.deleteWorker(rows);
            workersButton.doClick();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window, e.getMessage());
        }
    }

    private Pair<Integer, ClientTableData> createClientPane(ClientTableData data) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel nameLabel = new JLabel("Enter name");
        JTextField nameText = new JTextField(data.name, 10);
        JLabel surnameLabel = new JLabel("Enter surname");
        JTextField surnameText = new JTextField(data.surname, 10);

        panel.add(nameLabel);
        panel.add(nameText);
        panel.add(surnameLabel);
        panel.add(surnameText);

        int option = JOptionPane.showConfirmDialog(null, panel,"Client", JOptionPane.OK_CANCEL_OPTION);
        option = option == -1 ? 2 : option;
        if (option == 2) return new Pair<>(
                option,
                data
        );

        String id = data.id;
        String name = nameText.getText();
        String surname = surnameText.getText();
        String[] reserveDates = new String[]{};
        String[] services = new String[]{};
        ClientTableData newData = new ClientTableData(
                name,
                surname,
                reserveDates,
                services,
                id
        );
        try {
            InterfaceValidation.checkClientData(newData);
        }
        catch (Exception exc) {
            JOptionPane.showMessageDialog(window, exc.getMessage());
            return new Pair<>(
                    2,
                    data
            );
        }
        return new Pair<>(
                option,
                newData
        );
    }

    private void newClientPane() {
        Pair<Integer, ClientTableData> data = createClientPane(new ClientTableData());
        if (data.getValue0() == 2) return;
        try {
            backend.saveClient(data.getValue1());
            clientsButton.doClick();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window, e.getMessage());
        }
    }
    private void editClientPane() {
        int row = table.getSelectedRow();
        ClientTableData rowData = new ClientTableData(
                (String) tableData[row][1],
                (String) tableData[row][2],
                (String[]) tableData[row][3],
                (String[]) tableData[row][4],
                (String) tableData[row][0]
        );
        Pair<Integer, ClientTableData> data = createClientPane(rowData);
        if (data.getValue0() == 2 ||
                data.getValue1().equals(rowData)) return;
        try {
            backend.editClient(data.getValue1());
            clientsButton.doClick();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window, e.getMessage());
        }
    }
    private void deleteClientPane() {
        int option = createDeletePane();
        if (option == 2) return;
        int[] rows = table.getSelectedRows();
        for (int i = 0; i < rows.length; i++) rows[i] = Integer.parseInt((String)tableData[rows[i]][0]);
        try {
            backend.deleteClient(rows);
            clientsButton.doClick();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window, e.getMessage());
        }
    }

    private Pair<Integer, ServiceTableData> createServicePane(ServiceTableData data) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel nameLabel = new JLabel("Enter name");
        JTextField nameText = new JTextField(data.name, 10);
        JLabel priceLabel = new JLabel("Enter price");
        JTextField priceText = new JTextField(data.price, 10);
        JLabel servicesLabel = new JLabel("Select workers");
        JPanel listPanel = new JPanel(new BorderLayout());
        String [] workers = backend.getUniqueWorkers().toArray(String[]::new);
        ArrayList<Integer> selectedWorkers = new ArrayList<>();
        for (int i = 0; i < workers.length; i++) {
            for (int j = 0; j < data.workers.length; j++) {
                if (workers[i].equals(data.workers[j]))
                    selectedWorkers.add(i);
            }
        }
        JList<String> workersList = new JList<>(workers);
        workersList.setSelectedIndices(selectedWorkers.stream().mapToInt(i -> i).toArray());
        workersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listPanel.add(new JScrollPane(workersList));

        panel.add(nameLabel);
        panel.add(nameText);
        panel.add(priceLabel);
        panel.add(priceText);
        panel.add(servicesLabel);
        panel.add(listPanel);

        int option = JOptionPane.showConfirmDialog(null, panel,"Services", JOptionPane.OK_CANCEL_OPTION);
        option = option == -1 ? 2 : option;
        if (option == 2) return new Pair<>(
                option,
                data
        );
        String id = data.id;
        String name = nameText.getText();
        String price = priceText.getText();
        String[] workers_ = workersList.getSelectedValuesList().toArray(String[]::new);

        ServiceTableData newData = new ServiceTableData(
                name,
                price,
                workers_,
                id
        );
        try {
            InterfaceValidation.checkServiceData(newData);
        }
        catch (Exception exc) {
            JOptionPane.showMessageDialog(window, exc.getMessage());
            return new Pair<>(
                    2,
                    data
            );
        }
        return new Pair<>(
                option,
                newData
        );
    }

    private void newServicePane() {
        Pair<Integer, ServiceTableData> data = createServicePane(new ServiceTableData());
        if (data.getValue0() == 2) return;
        try {
            backend.saveService(data.getValue1());
            servicesButton.doClick();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window, e.getMessage());
        }
    }
    private void editServicePane() {
        int row = table.getSelectedRow();
        ServiceTableData rowData = new ServiceTableData(
                (String) tableData[row][1],
                (String) tableData[row][2],
                (String[]) tableData[row][3],
                (String) tableData[row][0]
        );
        Pair<Integer, ServiceTableData> data = createServicePane(rowData);
        if (data.getValue0() == 2 ||
            data.getValue1().equals(rowData)) return;
        try {
            backend.editService(data.getValue1());
            servicesButton.doClick();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window, e.getMessage());
        }
    }
    private void deleteServicePane() {
        int option = createDeletePane();
        if (option == 2) return;
        int[] rows = table.getSelectedRows();
        for (int i = 0; i < rows.length; i++) rows[i] = Integer.parseInt((String)tableData[rows[i]][0]);
        try {
            backend.deleteService(rows);
            clientsButton.doClick();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(window, e.getMessage());
        }
    }

    private void filterReserveDatesTable(ReserveDateFilter settings) {
        window.getContentPane().removeAll();
        buildToolbar(Table.RESERVE_DATES);
        buildReserveDatesTable(settings.getFilteredRecords());
        buildReserveDatesFilterPanel();
        window.getContentPane().revalidate();
        window.getContentPane().repaint();
    }
    private void filterWorkersTable(WorkerFilter settings) {
        window.getContentPane().removeAll();
        buildToolbar(Table.WORKERS);
        buildWorkersTable(settings.getFilteredRecords());
        buildWorkersFilterPanel();
        window.getContentPane().revalidate();
        window.getContentPane().repaint();
    }
    private void filterClientsTable(ClientFilter settings) {
        window.getContentPane().removeAll();
        buildToolbar(Table.CLIENTS);
        buildClientsTable(settings.getFilteredRecords());
        buildClientsFilterPanel();
        window.getContentPane().revalidate();
        window.getContentPane().repaint();
    }
    private void filterServicesTable(ServiceFilter settings) {
        window.getContentPane().removeAll();
        buildToolbar(Table.SERVICES);
        buildServicesTable(settings.getFilteredRecords());
        buildServicesFilterPanel();
        window.getContentPane().revalidate();
        window.getContentPane().repaint();
    }

    private ImageIcon loadImageIcon(String filepath, int w, int h) {
        return new ImageIcon(new ImageIcon(filepath).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
    }
}
