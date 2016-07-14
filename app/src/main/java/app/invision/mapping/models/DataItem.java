package app.invision.mapping.models;

/**
 * Created by hp on 6/23/2016.
 */

public class DataItem {

    private class Location {
        float lat;
        float lng;
        float alt;

        public Location(float lat, float lng, float alt) {
            this.lat = lat;
            this.lng = lng;
            this.alt = alt;
        }

        public float getLat() {
            return lat;
        }

        public void setLat(float lat) {
            this.lat = lat;
        }

        public float getLng() {
            return lng;
        }

        public void setLng(float lng) {
            this.lng = lng;
        }

        public float getAlt() {
            return alt;
        }

        public void setAlt(float alt) {
            this.alt = alt;
        }
    }

    long mapID = 1000000100000l;
    String clientName =  "Joe Bloe";
    String sVer = "1.00";
    String lVer = "1.00";
    String itemsVer = "1.00";
    String description = "Here goes a description.";
    String mapsSFile = "10000001000000_S.mpk";
    Location[] locations = {
            new Location((float) 45.1098, (float) -128.2398,(float) 0),
            new Location((float) 43.1098, (float) -129.2398,(float) 0)
    };
    String locationsJSONArray;
    String overviewTopo = "10000001000000_overview_topo.png";
    String mapsLUpUTC = "14598856081.00";
    String province = "Ontario";
    String mapsLFile = "10000001000000_L.mpk";
    String country = "Canada";
    String mapsSUpUTC = "14598856081.00";
    String name = "A Special Map";
    String overviewSatellite = "10000001000000_overview_satellite.png";
    String town = "Toronto";
    String itemsUpUTC = "14598856081.00";

    public DataItem(long mapID, String clientName, String sVer, String lVer, String itemsVer,
                    String description, String mapsSFile, String locationsJSONArray,
                    String overviewTopo, String mapsLUpUTC, String province, String mapsLFile, String country,
                    String mapsSUpUTC, String name, String overviewSatellite, String town, String itemsUpUTC) {

        this.mapID = mapID;
        this.clientName = clientName;
        this.sVer = sVer;
        this.lVer = lVer;
        this.itemsVer = itemsVer;
        this.description = description;
        this.mapsSFile = mapsSFile;
        this.locationsJSONArray = locationsJSONArray;
        this.overviewTopo = overviewTopo;
        this.mapsLUpUTC = mapsLUpUTC;
        this.province = province;
        this.mapsLFile = mapsLFile;
        this.country = country;
        this.mapsSUpUTC = mapsSUpUTC;
        this.name = name;
        this.overviewSatellite = overviewSatellite;
        this.town = town;
        this.itemsUpUTC = itemsUpUTC;
    }

    public long getMapID() {
        return mapID;
    }

    public String getClientName() {
        return clientName;
    }

    public String getsVer() {
        return sVer;
    }

    public String getlVer() {
        return lVer;
    }

    public String getItemsVer() {
        return itemsVer;
    }

    public String getDescription() {
        return description;
    }

    public String getMapsSFile() {
        return mapsSFile;
    }

    public Location[] getLocations() {
        return locations;
    }

    public String getMapsLUpUTC() {
        return mapsLUpUTC;
    }

    public String getProvince() {
        return province;
    }

    public String getMapsLFile() {
        return mapsLFile;
    }

    public String getLocationsJSONArray() {
        return locationsJSONArray;
    }

    public String getOverviewTopo() {
        return overviewTopo;
    }

    public String getOverviewSatellite() {
        return overviewSatellite;
    }

    public String getCountry() {
        return country;
    }

    public String getMapsSUpUTC() {
        return mapsSUpUTC;
    }

    public String getName() {
        return name;
    }

    public String getTown() {
        return town;
    }

    public String getItemsUpUTC() {
        return itemsUpUTC;
    }
}
