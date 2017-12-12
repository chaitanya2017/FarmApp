package monitor;

/*
* This class represents an image
*/
public class Image {

    //Fields
    private String path;
    private String imagetakendate;  

    /*
    * constructor
    */
    public Image() {

    }

    //Property accessors
    public Image(String path, String imagetakendate) {
        this.path = path;
        this.imagetakendate = imagetakendate;
    }
    
    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    public String getImagetakendate() {
        return this.imagetakendate;
    }

    public void setImagetakendate(String imagetakendate) {
        this.imagetakendate = imagetakendate;
    }
}
