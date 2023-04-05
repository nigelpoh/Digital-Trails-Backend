package helpers.misc;

public class Miscellaneous {
  public static Double getDistanceKM(Double lat_s, Double lng_s, Double lat_d, Double lng_d){
    return Math.sqrt(Math.pow(lat_d - lat_s, 2) + Math.pow(lng_d - lng_s, 2)) * (Math.PI / 180) * 6371.01;
  }
}
