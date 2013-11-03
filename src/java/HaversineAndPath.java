
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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

    Location street,from, to;
    double[] point1, point2;
    String print;

    public HaversineAndPath(String[] from, String[] to, int mode) {
        try {
            this.from = new Location(from[0], from[1]);
            if (mode == 1) {
                point1 = this.from.first1;
                point2 = this.from.last1;
            } else if (mode == 2) {
                this.to = new Location(to[0], to[1]);
                point1 = new double[2];
                point2 = new double[2];
            }

        } catch (SQLException ex) {
            Logger.getLogger(HaversineAndPath.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public HaversineAndPath(String[] street, String[] from, String[] to) {
        try {
            this.street = new Location(street[0], street[1]);
            this.street.getIntersectionNode();
            this.from = new Location(from[0], from[1]);
            this.to = new Location(to[0], to[1]);

        } catch (SQLException ex) {
            Logger.getLogger(HaversineAndPath.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double getDistance(double[] f, double[] t) {
        double result = 0.0;
        try {
            String urlStr = "http://yournavigation.org/api/dev/route.php?"
                    + "flat=" + f[0] + "&flon=" + f[1] + "&tlat=" + t[0] + "&tlon=" + t[1] + "&v=motorcar&fast=1&instructions=1";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() != 200) {
                throw new IOException(conn.getResponseMessage());
            }

            // Buffer the result into a string
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line = "";
            for (int i = 0; i < 6; i++) {
                line = rd.readLine();
            }
            result = Double.parseDouble(line.replace("<distance>", "").replace("</distance>", ""));
            rd.close();
            conn.disconnect();

        } catch (MalformedURLException ex) {
            Logger.getLogger(HaversineAndPath.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HaversineAndPath.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    private void setPoint(double[] a, double[] b) {
        point1 = a;
        point2 = b;
    }

    public void getPoint3(){
        
    }
    
    public void getPoint2() {
        if (this.from.way != null) {
            if (this.from.way.equals("1")) {
                if (this.to.way != null) {
                    if (this.to.way.equals("1")) {
                        double d1 = min3(from.first1, to.first1, to.last1);
                        double d2 = min3(from.last1, to.first1, to.last1);
                        if (d1 < d2) {
                            setPoint(from.center1, from.first1);
                        } else {
                            setPoint(from.center1, from.last1);
                        }
                    } else {
                        double d1 = min3(from.first1, to.first1, to.last1);
                        double d2 = min3(from.first1, to.first2, to.last2);
                        double d3 = min3(from.last1, to.first1, to.last1);
                        double d4 = min3(from.last1, to.first2, to.last2);
                        if (d1 > d2) {
                            d1 = d2;
                        }
                        if (d3 > d4) {
                            d3 = d4;
                        }
                        if (d1 < d3) {
                            setPoint(from.center1, from.first1);
                        } else {
                            setPoint(from.center1, from.last1);
                        }
                    }
                } else {
                    double d1 = getDistance(from.first1, to.point);
                    double d2 = getDistance(from.last1, to.point);
                    if (d1 < d2) {
                        setPoint(from.center1, from.first1);
                    } else {
                        setPoint(from.center1, from.last1);
                    }
                }
            } else {
                if (this.to.way != null) {
                    if (this.to.way.equals("1")) {
                        double d1 = min3(from.last1, to.first1, to.last1);
                        double d2 = min3(from.last2, to.first1, to.last1);
                        if (d1 < d2) {
                            setPoint(from.center1, from.last1);
                        } else {
                            setPoint(from.center2, from.last2);
                        }
                    } else {
                        double d1 = min3(from.last1, to.first1, to.last1);
                        double d2 = min3(from.last1, to.first2, to.last2);
                        double d3 = min3(from.last2, to.first1, to.last1);
                        double d4 = min3(from.last2, to.first2, to.last2);
                        if (d1 > d2) {
                            d1 = d2;
                        }
                        if (d3 > d4) {
                            d3 = d4;
                        }
                        if (d1 < d3) {
                            setPoint(from.center1, from.last1);
                        } else {
                            setPoint(from.center2, from.last2);
                        }
                    }
                } else {
                    double d1 = getDistance(from.last1, to.point);
                    double d2 = getDistance(from.last2, to.point);
                    if (d1 >= d2) {
                        setPoint(from.center1, from.last1);
                    } else {
                        setPoint(from.center2, from.last2);
                    }
                }
            }
        } else {
            if (this.to.way != null) {
                if (this.to.way.equals("1")) {
                    setPoint(this.from.point, this.to.first1);
                }else {
                    double d1 = getDistance(this.from.point, this.to.first1);
                    double d2 = getDistance(this.from.point, this.to.first2);
                    if(d1 < d2){
                        setPoint(this.from.point, this.to.first1);
                    }else{
                        setPoint(this.from.point, this.to.first2);
                    }
                }
            }else{
                setPoint(this.from.point, this.to.point);
            }
        }
    }

    public double min3(double[] f, double[] t1, double[] t2) {
        double d1 = getDistance(f, t1);
        double d2 = getDistance(f, t2);
        if (d1 >= d2) {
            return d2;
        } else {
            return d1;
        }
    }

    public void getPoint() {
        double min, temp;
        min = Double.MAX_VALUE;
        if (this.from.way != null) {
            if (this.from.way.equals("1")) {
                if (this.to.way != null) {
                    for (double[] d : from.node1) {
                        temp = minDist(d, to.node1);
                        if (min > temp) {
                            min = temp;
                            setPoint(from.center1, d);
                        }
                    }
                    if (this.to.way.equals("2")) {
                        for (double[] d : from.node1) {
                            temp = minDist(d, to.node2);
                            if (min > temp) {
                                min = temp;
                                setPoint(from.center1, d);
                            }
                        }
                    }

                } else {
                    ArrayList<double[]> p = new ArrayList<double[]>();
                    p.add(this.to.point);
                    for (double[] d : from.node1) {
                        temp = minDist(d, p);
                        if (min > temp) {
                            min = temp;
                            setPoint(d, d);
                        }
                    }
                }
            } else {
                if (this.to.way != null) {

                    for (double[] d : from.node1) {
                        temp = minDist(d, to.node1);
                        if (min > temp) {
                            min = temp;
                            setPoint(d, d);
                        }
                    }

                    for (double[] d : from.node2) {
                        temp = minDist(d, to.node1);
                        if (min > temp) {
                            min = temp;
                            setPoint(d, d);
                        }
                    }

                    if (this.to.way.equals("2")) {
                        for (double[] d : from.node1) {
                            temp = minDist(d, to.node2);
                            if (min > temp) {
                                min = temp;
                                setPoint(d, d);
                            }
                        }

                        for (double[] d : from.node2) {
                            temp = minDist(d, to.node2);
                            if (min > temp) {
                                min = temp;
                                setPoint(d, d);
                            }
                        }
                    }
                } else {
                    ArrayList<double[]> p = new ArrayList<double[]>();
                    p.add(this.to.point);

                    for (double[] d : from.node1) {
                        temp = minDist(d, p);
                        if (min > temp) {
                            min = temp;
                            setPoint(d, d);
                        }
                    }

                    for (double[] d : from.node2) {
                        temp = minDist(d, p);
                        if (min > temp) {
                            min = temp;
                            setPoint(d, d);
                        }
                    }
                }
            }
        } else {
            setPoint(from.point, to.first1);
            if (this.to.way != null) {
                min = haversine(this.from.point[0], this.from.point[1], this.to.first1[0], this.to.first1[1]);
                temp = haversine(this.from.point[0], this.from.point[1], this.to.center1[0], this.to.center1[1]);
                if (min > temp) {
                    min = temp;
                    setPoint(from.point, to.center1);
                }
                temp = haversine(this.from.point[0], this.from.point[1], this.to.last1[0], this.to.last1[1]);
                if (min > temp) {
                    setPoint(from.point, to.last1);
                }
            } else {
                setPoint(from.point, to.point);
            }
        }
    }

    private double minDist(double[] point, ArrayList<double[]> list) {

        double result = Double.MAX_VALUE;
        double temp;

        for (double[] d : list) {
            temp = getDistance(point, d);
            if (result > temp) {
                result = temp;
            }
        }
        return result;
    }

    private double haversine(double lat1, double lng1, double lat2, double lng2) {
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
