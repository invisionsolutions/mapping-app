package app.invision.mapping.models;

/**
 * Created by hp on 7/4/2016.
 */
public class ChildItem{
    String photo;
    String socialTag;
    String coordinates;
    int number;
    String difficulty;
    String description;
    String subType;
    String specialReport;
    String type;
    String thumbnail;
    String coordinateType;
    String duration;
    String link;
    String webcam;
    String status;
    String video;
    String lookAt;
    String name;
    String parentName;

    public ChildItem(String photo, String socialTag, String coordinates, int number, String difficulty,
                     String description, String subType, String specialReport, String type, String thumbnail,
                     String coordinateType, String duration, String link, String webcam, String status,
                     String video, String lookAt, String name) {

        this.photo = photo;
        this.socialTag = socialTag;
        this.coordinates = coordinates;
        this.number = number;
        this.difficulty = difficulty;
        this.description = description;
        this.subType = subType;
        this.specialReport = specialReport;
        this.type = type;
        this.thumbnail = thumbnail;
        this.coordinateType = coordinateType;
        this.duration = duration;
        this.link = link;
        this.webcam = webcam;
        this.status = status;
        this.video = video;
        this.lookAt = lookAt;
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getSocialTag() {
        return socialTag;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public int getNumber() {
        return number;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getDescription() {
        return description;
    }

    public String getSubType() {
        return subType;
    }

    public String getSpecialReport() {
        return specialReport;
    }

    public String getType() {
        return type;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getCoordinateType() {
        return coordinateType;
    }

    public String getDuration() {
        return duration;
    }

    public String getLink() {
        return link;
    }

    public String getWebcam() {
        return webcam;
    }

    public String getStatus() {
        return status;
    }

    public String getVideo() {
        return video;
    }

    public String getLookAt() {
        return lookAt;
    }

    public String getName() {
        return name;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
