package org.firstinspires.ftc.teamcode.libs;


public class ColorSensorCalibration {
    public int absentRed = 0;
    public int absentGreen = 0;
    public int absentBlue = 0;
    
    public int presentRed = 0;
    public int presentGreen = 0;
    public int presentBlue = 0;
    
    public ColorSensorCalibration() {
        
    }
    
    public String serialize() {
        String csv = "";
        csv += "absentRed:" + absentRed + ",";
        csv += "absentGreen:" + absentGreen + ",";
        csv += "absentBlue:" + absentBlue + ",";
        csv += "presentRed:" + presentRed + ",";
        csv += "presentGreen:" + presentGreen + ",";
        csv += "presentBlue:" + presentBlue;
        return csv;
    }
    
    public void deserialize(String calibration) {
        String[] elements = calibration.split(",");
        
        for (int i = 0; i < 6; i++) {
            String[] element = elements[i].split(":");
            
            switch (element[0]) {
                case "absentRed":
                    absentRed = Integer.parseInt(element[1]);
                    break;
                case "absentGreen":
                    absentGreen = Integer.parseInt(element[1]);
                    break;
                case "absentBlue":
                    absentBlue = Integer.parseInt(element[1]);
                    break;
                case "presentRed":
                    presentRed = Integer.parseInt(element[1]);
                    break;
                case "presentGreen":
                    presentGreen = Integer.parseInt(element[1]);
                    break;
                case "presentBlue":
                    presentBlue = Integer.parseInt(element[1]);
                    break;
            }
        }
    }
    
    public void setAbsentRGB(int red, int green, int blue) {
        absentRed = red;
        absentGreen = green;
        absentBlue = blue;
    }
    
    public void setPresentRGB(int red, int green, int blue) {
        presentRed = red;
        presentGreen = green;
        presentBlue = blue;
    }
    
    public boolean isPresent(int red, int green, int blue) {
        double absentXDerivative = absentRed - red;
        double absentYDerivative = absentGreen - green;
        double absentZDerivative = absentBlue - blue;
        double absentDistance = Math.sqrt((double)(absentXDerivative * absentXDerivative + absentYDerivative * absentYDerivative + absentZDerivative * absentZDerivative));
        
        double presentXDerivative = presentRed - red;
        double presentYDerivative = presentGreen - green;
        double presentZDerivative = presentBlue - blue;
        double presentDistance = Math.sqrt((double)(presentXDerivative * presentXDerivative + presentYDerivative * presentYDerivative + presentZDerivative * presentZDerivative));
        
        return presentDistance < absentDistance;
    }
}