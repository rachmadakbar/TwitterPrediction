
import java.util.ArrayList;
import com.mysql.jdbc.Connection;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rachmad
 */
public class Location {

    String name, type, way, id;
    String firstNode1, firstNode2;
    double[] first1, center1, last1, first2, center2, last2;
    double[] point;
    ArrayList<double[]> node1, node2;
    Statement statement;
    String query;

    public Location(String name, String type) throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String userName = "root";
            String password = "";
            String url = "jdbc:mysql://localhost/tweet_street";
            Connection connection = (Connection) DriverManager.getConnection(url, userName, password);
            statement = connection.createStatement();
            this.name = name;
            this.type = type;
            if (type.equals("primary") || type.equals("secondary")) {
                node1 = new ArrayList<double[]>();
                node2 = new ArrayList<double[]>();
                getExtendedDescription(name);
                getIntersectionNode();                
            } else {
                point = new double[2];
                getPoint(name, type);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Location.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double[] getNode(String id) {
        double[] temp = new double[2];
        query = "SELECT * FROM `street_node` WHERE `id` =" + id;
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                temp[0] = Double.parseDouble(resultSet.getObject(3).toString());
                temp[1] = Double.parseDouble(resultSet.getObject(4).toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Location.class.getName()).log(Level.SEVERE, null, ex);
        }
        return temp;
    }

    private void getExtendedDescription(String name) {
        try {
            query = "SELECT * FROM `street_description` WHERE `street_name` = '" + name + "'";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                id = resultSet.getObject(1).toString();
                way = resultSet.getObject(4).toString();
            }
            query = "SELECT * FROM `extended_description` WHERE `id_street` = '" + id + "'";
            resultSet = statement.executeQuery(query);
            if (way.equals("1")) {
                String[] temp = new String[3];
                while (resultSet.next()) {
                    temp[0] =  resultSet.getObject(4).toString();
                    temp[1] = resultSet.getObject(5).toString();
                    temp[2] = resultSet.getObject(6).toString();
                }
                firstNode1 = temp[0];
                first1 = getNode(firstNode1);
                center1 = getNode(temp[1]);
                last1 = getNode(temp[2]);
            } else {
                String[] temp = new String[6];
                while (resultSet.next()) {
                    if (resultSet.getObject(3).toString().equals("1")) {
                        temp[0] =  resultSet.getObject(4).toString();
                        temp[1] = resultSet.getObject(5).toString();
                        temp[2] = resultSet.getObject(6).toString();
                    } else if (resultSet.getObject(3).toString().equals("2")) {
                        temp[3] =  resultSet.getObject(4).toString();
                        temp[4] = resultSet.getObject(5).toString();
                        temp[5] = resultSet.getObject(6).toString();
                    }
                }
                
                firstNode1 = temp[0];
                first1 = getNode(firstNode1);
                center1 = getNode(temp[1]);
                last1 = getNode(temp[2]);
                firstNode2 = temp[3];
                first2 = getNode(firstNode2);
                center2 = getNode(temp[4]);
                last2 = getNode(temp[5]);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LocationEstimator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getPoint(String name, String type) {
        if (type.equals("mall")) {
            query = "SELECT * FROM `mall` WHERE `mall_name` = '" + name + "'";
        } else {
            query = "SELECT * FROM `kecamatan` WHERE `name` = '" + name + "'";
        }
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                point[0] = Double.parseDouble(resultSet.getObject(3).toString());
                point[1] = Double.parseDouble(resultSet.getObject(4).toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(Location.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getIntersectionNode() {
        try {
            String ttemp;
            if (way.equals("1")) {
                query = "SELECT distinct id_node FROM street_path WHERE id_street =" + id;
                ResultSet resultSet = statement.executeQuery(query);
                ArrayList<String> temp = new ArrayList();
                while (resultSet.next()) {
                    ttemp = resultSet.getObject(1).toString();
                    if(!firstNode1.equals(ttemp))                    
                    temp.add(ttemp);
                }
                for(String s : temp)
                    node1.add(getNode(s));
            } else {
                query = "SELECT distinct a.id_node FROM path_1 a, intersection b WHERE a.id_node = b.id_node AND a.id_street ="+id;
                ResultSet resultSet = statement.executeQuery(query);
                ArrayList<String> temp = new ArrayList();
                
                while (resultSet.next()) {
                     ttemp = resultSet.getObject(1).toString();
                    if(!firstNode1.equals(ttemp))                    
                    temp.add(ttemp);
                }
                for(String s : temp)
                    node1.add(getNode(s));
                
                query = "SELECT distinct a.id_node FROM path_2 a, intersection b WHERE a.id_node = b.id_node AND a.id_street ="+id;
                ResultSet resultSet2 = statement.executeQuery(query);
                
                temp = new ArrayList();
                while (resultSet2.next()) {
                    ttemp = resultSet2.getObject(1).toString();
                    if(!firstNode2.equals(ttemp))                    
                    temp.add(ttemp);
                }
                for(String s : temp)
                    node2.add(getNode(s));
            }
        } catch (SQLException ex) {
            Logger.getLogger(LocationEstimator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
