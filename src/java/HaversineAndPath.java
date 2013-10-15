
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

    Location from, to;
    double[] point1, point2;

    public HaversineAndPath(String[] from, String[] to) {
        try {
            this.from = new Location(from[0], from[1]);
            this.to = new Location(to[0], to[1]);
            point1 = new double[2];
            point2 = new double[2];
        } catch (SQLException ex) {
            Logger.getLogger(HaversineAndPath.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private void setPoint(double[] a, double[] b){
        point1 = a;
        point2 = b;
    }
    
    public void getPoint() {
        double min, temp;
        if (this.from.way != null) {
            setPoint(from.first1,from.center1);
            if (this.from.way.equals("1")) {
                if (this.to.way != null) {
                    min = minDist(this.from.first1, this.to.first1, this.to.center1, this.to.last1);
                    temp = minDist(this.from.last1, this.to.first1, this.to.center1, this.to.last1);
                    if (min > temp) {
                        setPoint(from.center1,from.last1);
                    }
                } else {
                    min = distFrom(this.from.first1[0], this.from.first1[1], this.to.point[0], this.to.point[1]);
                    temp = distFrom(this.from.last1[0], this.from.last1[1], this.to.point[0], this.to.point[1]);
                    if (min > temp) {
                       setPoint(from.center1,from.last1);
                    }
                }
            }else{
                if (this.to.way != null) {
                    min = minDist(this.from.center1, this.to.first1, this.to.center1, this.to.last1);
                    temp = minDist(this.from.last1, this.to.first1, this.to.center1, this.to.last1);
                    if (min > temp) {
                        setPoint(from.center1,from.last1);
                        min = temp;
                    }
                    temp = minDist(this.from.center2, this.to.first1, this.to.center1, this.to.last1);
                    if (min > temp) {
                        setPoint(from.first2,from.center2);
                        min = temp;
                    }
                    temp = minDist(this.from.last2, this.to.first1, this.to.center1, this.to.last1);
                    if (min > temp) {
                        setPoint(from.center2,from.last2);
                    }
                }else{
                    min = distFrom(this.from.first1[0], this.from.first1[1], this.to.point[0], this.to.point[1]);
                    temp = distFrom(this.from.last1[0], this.from.last1[1], this.to.point[0], this.to.point[1]);
                    if (min > temp) {
                       setPoint(from.center1,from.last1);
                       min = temp;
                    }                    
                    temp = distFrom(this.from.first2[0], this.from.first2[1], this.to.point[0], this.to.point[1]);
                    if (min > temp) {
                       setPoint(from.first2,from.center2);
                       min = temp;
                    }
                    temp = distFrom(this.from.center2[0], this.from.center2[1], this.to.point[0], this.to.point[1]);
                    if (min > temp) {
                       setPoint(from.first2,from.center2);
                       min = temp;
                    }
                    temp = distFrom(this.from.last2[0], this.from.last2[1], this.to.point[0], this.to.point[1]);
                    if (min > temp) {
                       setPoint(from.center2,from.last2);
                    }
                }
            }
        }else{
            setPoint(from.point,to.first1);
            if (this.to.way != null) {
                min = distFrom(this.from.point[0], this.from.point[1], this.to.first1[0], this.to.first1[1]);
                temp = distFrom(this.from.point[0], this.from.point[1], this.to.center1[0], this.to.center1[1]);
                if(min>temp){
                    min = temp;
                    setPoint(from.point,to.center1);
                }
                temp = distFrom(this.from.point[0], this.from.point[1], this.to.last1[0], this.to.last1[1]);
                if(min>temp){
                    setPoint(from.point,to.last1);
                }
            }else{
                setPoint(from.point,to.point);
            }
        }
    }

    private double minDist(double[] from, double[] to1, double[] to2, double[] to3) {
        double result = distFrom(from[0], from[1], to1[0], to1[1]);
        double temp = distFrom(from[0], from[1], to2[0], to2[1]);
        if (result > temp) {
            result = temp;
        }
        temp = distFrom(from[0], from[1], to3[0], to3[1]);
        if (result > temp) {
            result = temp;
        }
        return result;
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
