
import java.sql.SQLException;
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
public class HaversineAndPath {

    Location from,to;
    double[] point1, point2;
    
    public HaversineAndPath(String[] from, String[] to){
        try {
            this.from = new Location(from[0], from[1]);
            this.to = new Location(to[0], to[1]);
            point1 = new double[2];
            point2 = new double[2];
            //getPoint();
        } catch (SQLException ex) {
            Logger.getLogger(HaversineAndPath.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void getPoint(){
        if(this.from.way != null && this.from.way.equals("1") && this.to.way != null && this.to.way.equals("1")){
            int mini = 1;
            double min,temp;
            min = distFrom(this.from.first1[0], this.from.first1[1], this.to.first1[0], this.to.first1[1]);
            temp = distFrom(this.from.first1[0], this.from.first1[1], this.to.center1[0], this.to.center1[1]);
            if(min>temp) min = temp;
            temp = distFrom(this.from.first1[0], this.from.first1[1], this.to.last1[0], this.to.last1[1]);
            if(min>temp) min = temp;
            temp = distFrom(this.from.center1[0], this.from.center1[1], this.to.first1[0], this.to.first1[1]);
            if(min>temp){
                min = temp;
                mini = 2;
            }
            temp = distFrom(this.from.center1[0], this.from.center1[1], this.to.center1[0], this.to.center1[1]);
            if(min>temp) {
                min = temp;
                mini = 2;
            }
            temp = distFrom(this.from.center1[0], this.from.center1[1], this.to.last1[0], this.to.last1[1]);
            if(min>temp) {
                min = temp;
                mini = 2;
            }
            temp = distFrom(this.from.last1[0], this.from.last1[1], this.to.first1[0], this.to.first1[1]);
            if(min>temp){
                min = temp;
                mini = 3;
            }
            temp = distFrom(this.from.last1[0], this.from.last1[1], this.to.center1[0], this.to.center1[1]);
            if(min>temp) {
                min = temp;
                mini = 3;
            }
            temp = distFrom(this.from.last1[0], this.from.last1[1], this.to.last1[0], this.to.last1[1]);
            if(min>temp) {
                min = temp;
                mini = 3;
            }
            if(mini==1){
                point1 = this.from.center1;
                point2 = this.from.first1;
            }else if(mini==2){
                point1 = this.from.first1;
                point2 = this.from.center1;
            }else {
                point1 = this.from.center1;
                point2 = this.from.last1;
            }
        }
    }
    
    private double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist;
    }
    

}
