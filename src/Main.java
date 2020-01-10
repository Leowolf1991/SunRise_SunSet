import java.util.Calendar;
public class Main {
    private double dLngHour;
    private double dSunRise;
    private double dSunSet;
    private double dAnomalyRise;
    private double dAnomalySet;
    private double dLongtitudeRise;
    private double dLongtitudeSet;
    private double dRArise;
    private double dRAset;
    private double dSinDecrise;
    private double dCosDicrise;
    private double dSinset;
    private double dCosset;
    private int iN;
    private final double zenith = 90.83333D;

    public Main() {

    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        Main main = new Main();
        System.out.println("Сегодня день: " + calendar.get(Calendar.DATE) + " Месяц: " + calendar.get(Calendar.MONTH) + " Год : " + calendar.get(Calendar.YEAR));
        main.iN = main.DayofYear();
        main.dLngHour = main.LongtiToHour();

        main.TimeSun(main.dLngHour, main.iN);
        main.dAnomalyRise = main.SunAnomaly(main.dSunRise);
        main.dAnomalySet = main.SunAnomaly(main.dSunSet);
        main.dLongtitudeRise = main.TrueLongtitude(main.dAnomalyRise);
        main.dLongtitudeSet = main.TrueLongtitude(main.dAnomalySet);
        main.dRArise = main.RightAcsension(main.dLongtitudeRise);
        main.dRAset = main.RightAcsension(main.dLongtitudeSet);
        main.dSinDecrise = 0.39782D * Math.sin(main.DegToRoad(main.dLongtitudeRise));
        main.dCosDicrise = Math.cos(Math.asin(main.dSinDecrise));
        main.dSinset = 0.39782D * Math.sin(main.DegToRoad(main.dLongtitudeSet));
        main.dCosset = Math.cos(Math.asin(main.dSinset));
    }

    private int DayofYear() {
        Calendar calendar = Calendar.getInstance();
        double dN1 = Math.floor((double)(275 * (calendar.get(Calendar.MONTH) + 1) / 9));
        double dN2 = Math.floor((double)((calendar.get(Calendar.MONTH) + 10) / 12));
        double dN3 = 1.0D + Math.floor((double)calendar.get(Calendar.YEAR) - 4.0D * (Math.floor((double)(calendar.get(Calendar.YEAR) / 4)) + 2.0D) / 3.0D);
        this.iN = (int)(dN1 - dN2 * dN3 + calendar.get(Calendar.DATE) - 30.0D);
        return this.iN;
    }

    private double LongtiToHour() {
        Coordinate coordinate = new Coordinate(53.55D, 34.23D);
        this.dLngHour = coordinate.getLongtitude() / 15.0D;
        return this.dLngHour;
    }

    private void TimeSun(double dLngHour, int iN) {
        this.dSunRise = (double)iN + (6.0D - dLngHour) / 24.0D;
        this.dSunSet = (double)iN + (18.0D - dLngHour) / 24.0D;
    }

    private double SunAnomaly(double dTimeSun) {
        double dAnomaly = 0.9856D * dTimeSun - 3.289D;
        return dAnomaly;
    }

    private double TrueLongtitude(double dAnomaly) {
        double L = dAnomaly + 1.916D * Math.sin(this.DegToRoad(dAnomaly)) + 0.02D * Math.sin(this.DegToRoad(2.0D * dAnomaly)) + 282.634D;
        if (L >= 360.0D) {
            L -= 360.0D;
        } else if (L < 0.0D) {
            L += 360.0D;
        }

        return L;
    }

    private double DegToRoad(double dValue) {
        return 3.141592653589793D * dValue / 180.0D;
    }

    private double RightAcsension(double L) {
        double RA = 180.0D * Math.atan(0.91764D * Math.tan(L * 3.141592653589793D / 180.0D)) / 3.141592653589793D;
        double LQuadrant = 90.0D * Math.floor(L / 90.0D);
        double RQuadrant = 90.0D * Math.floor(RA / 90.0D);
        RA += LQuadrant - RQuadrant;
        return RA / 15.0D;
    }

    private double LocalAngles(double zenit, double latitude, double dSinDecrise, double dCosDicrise)
    {
        double cosH = (Math.cos(DegToRoad(zenit)) - dSinDecrise * Math.sin(DegToRoad(latitude)))/(dCosDicrise * Math.cos(DegToRoad(latitude)));
        return  cosH;
    }
}
