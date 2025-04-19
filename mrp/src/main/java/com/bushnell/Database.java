package com.bushnell;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Database {

    public static String DBName = "jdbc:sqlite:/Users/marleeheiken/Documents/GitHub/Java2-Project/VR-Factory.db";

    public static boolean setDBDirectory(String directory) {
        try {
          Path fullPath = Paths.get(directory, "VR-Factory.db");
          DBName = "jdbc:sqlite:" + fullPath.toString();  
          //System.out.println("setting DB path to: " + DBName);
          return true;
        } catch(Exception e) {
          return false;
        }
    }

    public static boolean checkConnection() {
        try {
          Class.forName("org.sqlite.JDBC");
          //System.out.println("connected to " + DBName);
          return true;
      }
      catch(Exception e)
      {
          e.printStackTrace(System.err);
          return false;  
      }
    }
  
    public static String[] getSkuList() {
        try
        (
          Connection connection = DriverManager.getConnection(DBName);
          Statement statement = connection.createStatement();
        )
        {
          ResultSet rs = statement.executeQuery("select sku from part");
          List<String> skuList = new ArrayList<>();
          while(rs.next()) {
            skuList.add(rs.getString("sku"));
          }
          String[] skuArray = skuList.toArray(new String[0]);
          return skuArray;          
        }
        catch(SQLException e)
        {
          e.printStackTrace(System.err);
          return new String[0];
        }
    }

    public static String[] getSkuSubList() {
      try (
          Connection connection = DriverManager.getConnection(DBName);
          Statement statement = connection.createStatement();
      ) {
          // Change the query to use LIKE 'SUB%' to get only SKUs starting with SUB
          ResultSet rs = statement.executeQuery("select sku from part where sku like 'SUB%'");
          List<String> skuList = new ArrayList<>();
          while(rs.next()) {
              skuList.add(rs.getString("sku"));
          }
          String[] skuArray = skuList.toArray(new String[0]);
          return skuArray;          
      }
      catch(SQLException e) {
          e.printStackTrace(System.err);
          return new String[0];
      }
  }
  
    public static Part getSkuData(String sku) {
        Part result = new Part();
        try
        (
          Connection connection = DriverManager.getConnection(DBName);
          Statement statement = connection.createStatement();
        )
        {
          ResultSet rs = statement.executeQuery("select * from part where sku = \"" + sku + "\"");
          while(rs.next()) {
            result.sku = rs.getString("sku");
            result.description = rs.getString("description");
            result.price = rs.getDouble("price");
            result.stock = rs.getInt("stock");
          }
          return result;          
        }
        catch(SQLException e)
        {
          e.printStackTrace(System.err);
          result.sku = "";
          result.description = "";
          result.price = 0.0;
          result.stock = 0;
          return result;
        }
    }
    
    public static boolean updatePrice(String sku, double newPrice) {
        try
        (
            Connection connection = DriverManager.getConnection(DBName);
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE part SET price = ? WHERE sku = ?");
        )
        {
            statement.setDouble(1, newPrice);
            statement.setString(2, sku);
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
        catch(SQLException e)
        {
            e.printStackTrace(System.err);
            return false;
        } 
    }

    public static boolean updateStock(String sku, int newStock) {
        try
        (
            Connection connection = DriverManager.getConnection(DBName);
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE part SET stock = ? WHERE sku = ?");
        )
        {
            statement.setInt(1, newStock);
            statement.setString(2, sku);
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
        catch(SQLException e)
        {
            e.printStackTrace(System.err);
            return false;
        }
    }

    public static boolean updatePart(String sku, double newPrice, int newStock) {
        try
        (
            Connection connection = DriverManager.getConnection(DBName);
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE part SET price = ?, stock = ? WHERE sku = ?");
        )
        {
            statement.setDouble(1, newPrice);
            statement.setInt(2, newStock);
            statement.setString(3, sku);
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        }
        catch(SQLException e)
        {
            e.printStackTrace(System.err);
            return false;
        }
    }
    
    public static List<Part> getAllSkuData() {
      List<Part> result = new ArrayList<>();
      try (
          Connection connection = DriverManager.getConnection(DBName);
          Statement statement = connection.createStatement();
      ) {
          ResultSet rs = statement.executeQuery("select * from part");
          while(rs.next()) {
              Part part = new Part();
              part.sku = rs.getString("sku");
              part.description = rs.getString("description");
              part.price = rs.getDouble("price");
              part.stock = rs.getInt("stock");
              result.add(part);
          }
          return result;          
      }
      catch(SQLException e) {
          e.printStackTrace(System.err);
          return result;
      }
  }  

    public static Map<String, Integer> getSubcomponents(String parentSku) {
        Map<String, Integer> components = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DBName);
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT sku, quantity FROM bom WHERE parent_sku = ?")) {
            stmt.setString(1, parentSku);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                components.put(rs.getString("sku"), rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return components;
    }
    
    public static boolean performBundle(String parentSku) {
        Map<String, Integer> components = getSubcomponents(parentSku);
        
        // First verify we can still bundle
        for (Map.Entry<String, Integer> entry : components.entrySet()) {
            Part child = getSkuData(entry.getKey());
            if (child == null || child.stock < entry.getValue()) {
                return false;
            }
        }
        
        try (Connection conn = DriverManager.getConnection(DBName)) {
            conn.setAutoCommit(false);
            
            // Update child components
            try (PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE part SET stock = stock - ? WHERE sku = ?")) {
                for (Map.Entry<String, Integer> entry : components.entrySet()) {
                    stmt.setInt(1, entry.getValue());
                    stmt.setString(2, entry.getKey());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
            
            // Update parent component
            try (PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE part SET stock = stock + 1 WHERE sku = ?")) {
                stmt.setString(1, parentSku);
                stmt.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isBundlePossible(String sku) {
        Map<String, Integer> components = Database.getSubcomponents(sku);
        for (Map.Entry<String, Integer> entry : components.entrySet()) {
            Part part = Database.getSkuData(entry.getKey());
            if (part.stock < entry.getValue()) {
                return false; // Not enough stock for at least one component
            }
        }
        return true; 
    }
    

}

  