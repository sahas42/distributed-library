public class Sublibrary {
    private double latitude;
    private double longitude;
    private String name;
    private String loc_desc;

    public Sublibrary() {}
    public Sublibrary(double latitude, double longitude, String name, String loc_desc) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.loc_desc = loc_desc;
    }

    public String getAddress() {
        return name + "\n" + loc_desc + "\nCoordinates: " + latitude + ", " + longitude;
    }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLoc_desc() { return loc_desc; }
    public void setLoc_desc(String loc_desc) { this.loc_desc = loc_desc; }
}
