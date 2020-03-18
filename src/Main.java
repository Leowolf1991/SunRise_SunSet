import java.util.Calendar;
public class Main {

    private final double zenith = 90.83333D;
    //Coordinate coordinate = new Coordinate(53.55D, 34.23D);
    Coordinate coordinate = new Coordinate(55.75, 37.62);
    public Main() {

    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        Main main = new Main();
        System.out.println("Сегодня день: " + calendar.get(Calendar.DATE) + " Месяц: " + calendar.get(Calendar.MONTH) + " Год : " + calendar.get(Calendar.YEAR));
         int n = main.DayofYear();
        double lngHour = main.coordinate.getLantitude()/15;
        double trise = n + ((6-lngHour)/24);
        double tset = n + ((18-lngHour)/24);
        double Mrise = main.SunM(trise);
        double Mset = main.SunM(tset);

        double Lrise = main.TrueL(Mrise);
        double Lset = main.TrueL(Mset);

        double RArise = main.RightAscension(Lrise);
        double RAset = main.RightAscension(Lset);

        double sinDecrise = 0.39782 * Math.sin(main.DegToRad(Lrise));
        double cosDecrise = Math.cos(Math.asin(sinDecrise));

        double sinDecset = 0.39782 * Math.sin(main.DegToRad(Lset));
        double cosDecset = Math.cos(Math.asin(sinDecset));

        double cosHrise = main.LocalAngle(main.zenith,main.coordinate.getLantitude(),sinDecrise,cosDecrise);
        if(cosHrise>1)
            System.out.println("Нет восхода солнца");
        else
            if(cosHrise<-1)
                System.out.println("Нет захода солнца");
        else{
            double cosHset = main.LocalAngle(main.zenith,main.coordinate.getLantitude(),sinDecset,cosDecset);
            double Hrise = (360 - 180*Math.acos(cosHrise)/Math.PI)/15;
            double Hset = (180*Math.acos(cosHset)/Math.PI)/15;
            double Trise = Hrise + RArise - (0.06571*trise) - 6.622;
            double Tset = Hset + RAset - (0.06571*tset) - 6.622;
            double UTrise = main.ToUT(Trise, lngHour);
            double UTset = main.ToUT(Tset, lngHour);
            double UTrisehour = Math.floor(UTrise);
            double UTriseminutes = Math.floor( (UTrise - UTrisehour) * 60 );
            double UTsethour = Math.floor(UTrise);
            double UTsetminutes = Math.floor( (UTset - UTsethour) * 60 );
            double UTrisetotalminutes = UTrisehour * 60 + UTriseminutes + 3 *60;
            double UTsettotalminutes = UTsethour * 60 + UTsetminutes + 3 * 60;

                if (UTrisetotalminutes > 24*60)
                    UTrisetotalminutes -= 24*60;
                if (UTrisetotalminutes < 0)
                    UTrisetotalminutes += 24*60;
                UTrisehour = Math.floor(UTrisetotalminutes/60);
                UTriseminutes = UTrisetotalminutes%60;

                if (UTsettotalminutes > 24*60)
                    UTsettotalminutes -= 24*60;
                if (UTsettotalminutes < 0)
                    UTsettotalminutes += 24*60;

                UTsethour = Math.floor(UTsettotalminutes/60);
                UTsetminutes = UTsettotalminutes%60;

                System.out.println("Time rise: " + (int)UTrisehour + " : " + (int)UTriseminutes);
                System.out.println("Time set: " + (int)UTsethour + " : " + (int)UTsetminutes);
            }
    }

    private int DayofYear() {
        Calendar calendar = Calendar.getInstance();
        double dN1 = Math.floor((double)(275 * (calendar.get(Calendar.MONTH) + 1) / 9));
        double dN2 = Math.floor((double)((calendar.get(Calendar.MONTH) + 10) / 12));
        double dN3 = 1.0D + Math.floor((double)calendar.get(Calendar.YEAR) - 4.0D * (Math.floor((double)(calendar.get(Calendar.YEAR) / 4)) + 2.0D) / 3.0D);
        int n = (int)(dN1 - dN2 * dN3 + calendar.get(Calendar.DATE) - 30.0D);
        return n;
    }

    private double DegToRad(double input)
    {
        return Math.PI * input/180;
    }

    private double SunM( double input)
    {
        return (0.9856 * input)-3.289;
    }

    private double TrueL(double M)
    {
        double L = M + (1.916*Math.sin(DegToRad(M)))+(0.020*Math.sin(DegToRad(2*M))) + 282.634;
        if (L >= 360)
            L -= 360;
        else if (L < 0)
            L += 360;
        return L;
    }

    private double RightAscension(double L)
    {
        double RA = 180 *Math.atan(0.91764*Math.tan(L*Math.PI/180)) / Math.PI;
        double LQuadrant = 90*Math.floor(L/90);
        double RQuadrant = 90*Math.floor(RA/90);
        RA = RA + (LQuadrant - RQuadrant);
        return RA/15;
    }

    private double LocalAngle(double zenith, double latitude, double sinDec, double cosDec)
    {
        double cosH = (Math.cos(DegToRad(zenith))-sinDec*Math.sin(DegToRad(latitude)))/(cosDec*Math.cos(DegToRad(latitude)));
        return cosH;
    }

    private double ToUT(double T,double lngHour)
    {
        double UT = T - lngHour;
        if (UT < 0)
            UT += 24;
        else if (UT >= 24)
            UT -= 24;
        return UT;
    }
}
