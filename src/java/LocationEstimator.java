
import com.mysql.jdbc.Connection;
import java.sql.*;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Rachmad-
 */
public class LocationEstimator {

    String token[];
    HashSet<String> primaryStreet;
    HashSet<String> secondaryStreet;
    HashSet<String> mall;
    HashSet<String> kecamatan;
    
    

    public LocationEstimator() {
        primaryStreet = new HashSet<String>();
        secondaryStreet = new HashSet<String>();
        mall = new HashSet<String>();
        kecamatan = new HashSet<String>();
    }

    public void clear(){
        primaryStreet = new HashSet<String>();
        secondaryStreet = new HashSet<String>();
        mall = new HashSet<String>();
        kecamatan = new HashSet<String>();
    }
    
    public String estimate(String input) {
        clear();
        token = input.split(" ");
        
        //cek jalan primary
        for (String token1 : token) {
            match(primaryStreet, Character.toUpperCase(token1.charAt(0)) + token1.substring(1), "primary");
        }
        
        //cek jalan secondary
        for (String token1 : token) {
            match(secondaryStreet, Character.toUpperCase(token1.charAt(0)) + token1.substring(1), "secondary");
        }
        
        //cek mall
        for (String token1 : token) {
            match(mall, Character.toUpperCase(token1.charAt(0)) + token1.substring(1), "mall");
        }

        //cek kecamatan
        for (String token1 : token) {
            match(kecamatan, Character.toUpperCase(token1.charAt(0)) + token1.substring(1), "kecamatan");
        }
        
        String primaryResult =  "";
        String secondaryResult =  "";
        String mallResult =  "";
        String kecamatanResult =  "";
        
        String result = "";
        
        double primaryMax = 0.0;
        double secondaryMax = 0.0;
        double mallMax = 0.0;
        double kecamatanMax = 0.0;
        double curr;
        
        for (String s : primaryStreet) {
            curr = LevenshteinDistance.similarity(input.replace("Raya", "").replace("Jalan", "").trim(), s.replace("Raya", "").replace("Jalan", "").trim());
            if(primaryMax < curr){
                primaryMax = curr;
                primaryResult = s;
            }
        }
        
        for (String s : secondaryStreet) {
            curr = LevenshteinDistance.similarity(input.replace("Raya", "").replace("Jalan", "").trim(), s.replace("Raya", "").replace("Jalan", "").trim());
            if(secondaryMax < curr){
                secondaryMax = curr;
                secondaryResult = s;
            }
        }
        
        for (String s : mall) {
            curr = LevenshteinDistance.similarity(input, s);
            if(mallMax < curr){
                mallMax = curr;
                mallResult = s;
            }
        }
        
        for (String s : kecamatan) {
            curr = LevenshteinDistance.similarity(input, s);
            if(kecamatanMax < curr){
                kecamatanMax = curr;
                kecamatanResult = s;
            }
        }
        
        if(primaryMax >= 0.5 && primaryMax*0.495049505 >= secondaryMax*0.267326733 && 
                primaryMax*0.495049505 >= mallMax*0.089108911 &&  
                primaryMax*0.495049505 >= kecamatanMax * 0.148514851){
            result = primaryResult;
        }else if(secondaryMax >= 0.5 && secondaryMax*0.267326733 >= primaryMax*0.495049505 &&
                secondaryMax*0.267326733 >= mallMax*0.089108911 &&
                secondaryMax*0.267326733 >= kecamatanMax * 0.148514851){
            result = secondaryResult;
        }else if(mallMax >= 0.5 && mallMax*0.089108911 >= primaryMax*0.495049505 &&
                mallMax*0.089108911 >= secondaryMax*0.267326733 && 
                mallMax*0.089108911 >= kecamatanMax * 0.148514851){
            result = mallResult;
        }else if(kecamatanMax >= 0.5) result = kecamatanResult;

        return result;
    }

    private void match(HashSet<String> set, String input, String type) {
        try {
            String query;
            if(type.equals("mall"))
                query = "SELECT * FROM mall where mall_name LIKE '%" + input + "%'";
            else if(type.equals("kecamatan")){
                query = "SELECT * FROM kecamatan where name LIKE '%" + input + "%'";
            }else {
                query = "SELECT * FROM street_description where street_name LIKE '%"+input+"%' AND type = '"+type+"'";
            }
            Class.forName("com.mysql.jdbc.Driver");
            String userName = "root";
            String password = "";
            String url = "jdbc:mysql://localhost/tweet_street";
            Connection connection = (Connection) DriverManager.getConnection(url, userName, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            String temp;
            while (resultSet.next()) {
                temp = resultSet.getObject(2).toString();
                set.add(temp);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LocationEstimator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(LocationEstimator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
