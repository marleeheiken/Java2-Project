package com.bushnell;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public static String DBName = "jdbc:sqlite:/Users/marleeheiken/Documents/GitHub/Java2-Project/VR-Factory.db";

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
  }
  