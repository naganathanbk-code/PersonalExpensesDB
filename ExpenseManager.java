import java.sql.*;
import java.util.*;

public class ExpenseManager {

    public static Connection getConnection() {
        Connection con = null;
        String url = "jdbc:mysql://localhost:3306/expense_db"; 
        String user = "root";   // your MySQL username
        String pass = "";       // your MySQL password
        
        try {
            con = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
        }
        return con;
    }

    static Scanner sc = new Scanner(System.in);

    // ---------------------- MAIN MENU ----------------------
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=========== EXPENSE MANAGER ===========");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Update Expense");
            System.out.println("4. Delete Expense");
            System.out.println("5. Category-wise Total");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1: addExpense(); break;
                case 2: viewExpenses(); break;
                case 3: updateExpense(); break;
                case 4: deleteExpense(); break;
                case 5: categoryTotal(); break;
                case 6: System.exit(0);
                default: System.out.println("Invalid Choice!");
            }
        }
    }

    // ---------------------- CREATE ----------------------
    public static void addExpense() {
        try {
            Connection con = getConnection();
            String sql = "INSERT INTO expenses (name, amount, category, date) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(sql);

            System.out.print("Enter Expense Name: ");
            String name = sc.next();

            System.out.print("Enter Amount: ");
            double amount = sc.nextDouble();

            System.out.print("Enter Category: ");
            String category = sc.next();

            System.out.print("Enter Date (YYYY-MM-DD): ");
            String date = sc.next();

            pst.setString(1, name);
            pst.setDouble(2, amount);
            pst.setString(3, category);
            pst.setString(4, date);

            pst.executeUpdate();
            System.out.println("Expense Added Successfully!");

            con.close();
        } catch (Exception e) {
            System.out.println("Error Adding Expense: " + e.getMessage());
        }
    }

    // ---------------------- READ ----------------------
    public static void viewExpenses() {
        try {
            Connection con = getConnection();
            String sql = "SELECT * FROM expenses";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            System.out.println("\nID | Name | Amount | Category | Date");
            System.out.println("---------------------------------------------");

            while (rs.next()) {
                System.out.println(
                    rs.getInt("id") + " | " +
                    rs.getString("name") + " | " +
                    rs.getDouble("amount") + " | " +
                    rs.getString("category") + " | " +
                    rs.getDate("date")
                );
            }

            con.close();
        } catch (Exception e) {
            System.out.println("Error Fetching Data: " + e.getMessage());
        }
    }

    // ---------------------- UPDATE ----------------------
    public static void updateExpense() {
        try {
            Connection con = getConnection();

            System.out.print("Enter Expense ID to Update: ");
            int id = sc.nextInt();

            System.out.print("Enter New Expense Name: ");
            String name = sc.next();

            System.out.print("Enter New Amount: ");
            double amount = sc.nextDouble();

            System.out.print("Enter New Category: ");
            String category = sc.next();

            String sql = "UPDATE expenses SET name=?, amount=?, category=? WHERE id=?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, name);
            pst.setDouble(2, amount);
            pst.setString(3, category);
            pst.setInt(4, id);

            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Expense Updated Successfully!");
            else
                System.out.println("ID Not Found!");

            con.close();
        } catch (Exception e) {
            System.out.println("Error Updating Expense: " + e.getMessage());
        }
    }

    // ---------------------- DELETE ----------------------
    public static void deleteExpense() {
        try {
            Connection con = getConnection();

            System.out.print("Enter Expense ID to Delete: ");
            int id = sc.nextInt();

            String sql = "DELETE FROM expenses WHERE id=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);

            int rows = pst.executeUpdate();

            if (rows > 0)
                System.out.println("Expense Deleted Successfully!");
            else
                System.out.println("ID Not Found!");

            con.close();
        } catch (Exception e) {
            System.out.println("Error Deleting Expense: " + e.getMessage());
        }
    }

    // ---------------- CATEGORY TOTAL ----------------
    public static void categoryTotal() {
        try {
            Connection con = getConnection();

            System.out.print("Enter Category: ");
            String category = sc.next();

            String sql = "SELECT SUM(amount) FROM expenses WHERE category=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, category);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                double total = rs.getDouble(1);
                System.out.println("Total for category '" + category + "' = Rs. " + total);
            }

            con.close();
        } catch (Exception e) {
            System.out.println("Error Fetching Total: " + e.getMessage());
        }
    }
}