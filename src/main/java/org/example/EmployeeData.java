package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeData {

    // Method to establish and return a database connection
    public Connection getConnection() throws SQLException {
        // Update the URL, username, and password based on your database settings
        return DatabaseConnection.getConnection();
    }

    // Method to create a new employee in the database
    public void createEmployee(Employee employee) throws SQLException {
        String query = "INSERT INTO employee (name, position, salary, hire_date) VALUES (?, ?, ?, ?) RETURNING id";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getPosition());
            preparedStatement.setDouble(3, employee.getSalary());
            preparedStatement.setDate(4, new java.sql.Date(employee.getHireDate().getTime()));

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int generatedId = resultSet.getInt("id");
                employee.setId(generatedId);
            }
        }
    }


    // Method to get an employee by ID from the database
    public Employee getEmployeeById(int id) throws SQLException {
        String query = "SELECT * FROM employee WHERE id = ?";
        Employee employee = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                employee = new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getDouble("salary"),
                        rs.getDate("hire_date") // getDate automatically handles conversion from SQL date to Java Date
                );
            }
        }

        return employee;
    }

    // Method to get all employees from the database
    public List<Employee> getAllEmployees() throws SQLException {
        String query = "SELECT * FROM employee";
        List<Employee> employees = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getDouble("salary"),
                        rs.getDate("hire_date") // getDate automatically handles conversion from SQL date to Java Date
                );
                employees.add(employee);
            }
        }

        return employees;
    }

    // Method to update an employee's details in the database
    public void updateEmployee(Employee employee) throws SQLException {
        String query = "UPDATE employee SET name = ?, position = ?, salary = ?, hire_date = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getPosition());
            stmt.setDouble(3, employee.getSalary());

            // Convert java.util.Date to java.sql.Date
            stmt.setDate(4, new java.sql.Date(employee.getHireDate().getTime()));

            stmt.setInt(5, employee.getId());

            stmt.executeUpdate();
        }
    }

    // Method to delete an employee by ID from the database
    public void deleteEmployee(int id) throws SQLException {
        String query = "DELETE FROM employee WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
