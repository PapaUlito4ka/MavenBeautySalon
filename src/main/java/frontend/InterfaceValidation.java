package frontend;

import backend.ClientTableData;
import backend.ServiceTableData;
import backend.WorkerTableData;
import exceptions.*;

import javax.swing.*;
import java.util.regex.Pattern;

public class InterfaceValidation {

    public static void checkFilterTextField(JTextField textField) throws BlankFilterTextFieldException
    {
        String pattern = "^\\s+$";
        if (textField.getText().length() == 0 ||
                textField.getText().matches(pattern)) {
            throw new BlankFilterTextFieldException();
        }
    }

    public static void checkSelectedTableRow(JTable table) throws TableRowNotSelectedException
    {
        if (table.getSelectedRow() == -1) {
            throw new TableRowNotSelectedException();
        }
    }
    public static void checkWorkerData(WorkerTableData data) throws Exception {
        checkStringField(data.name, "Name");
        checkStringField(data.surname, "Surname");
        checkStringField(data.spec, "Speciality");
    }
    public static void checkClientData(ClientTableData data) throws Exception {
        checkStringField(data.name, "Name");
        checkStringField(data.surname, "Surname");
    }
    public static void checkServiceData(ServiceTableData data) throws Exception {
        checkStringField(data.name, "Name");
        checkNumberField(data.price, "Price", 1);
    }

    public static void checkNumberField(String number, String name, int minValue) throws Exception
    {
        if (number.isEmpty()) throw new Exception(name + " must contain number");
        try {
            int num = Integer.parseInt(number);
            if (num < minValue) throw new Exception(name + " must be > " + Integer.toString(minValue));
        }
        catch (NumberFormatException exc) {
            throw new Exception(name + " must be a number");
        }
        catch (Exception exc) {
            throw new Exception(exc.getMessage());
        }
    }

    public static void checkStringField(String field, String name) throws Exception
    {
        if (field.isEmpty() || field.trim().isEmpty())
            throw new Exception(name + " must contain string");
    }
}
