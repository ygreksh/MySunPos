package com.example.ygrek.mysunpos;

import java.util.Calendar;

/**
 * Created by ygrek on 04.01.2018.
 */

public class Astronomy {
    public static double Deg2Rad = 0.01745329251994329576923690768489;
    public static double Rad2Deg = 1 / Deg2Rad;
    public static double sunazimuth;
    public static double sunaltitude;
    public static double moonazimuth;
    public static double moonelevation;
    //public static int localTimeZone;
    // Широта и долгота на входе в градусах, азимут и высота на выходе в радианах.
    public static void CalculateMoonPosition(Calendar dateTime, double latitude, double longitude)
    {
        latitude = Deg2Rad * latitude;

        //UT - всемирное время в часах, момент расчета
        //dateTime = dateTime.ToUniversalTime();
        //double ut = dateTime.TimeOfDay.TotalHours;
        double ut = dateTime.get(Calendar.HOUR_OF_DAY);
        //double year = dateTime.Year;
        double year = dateTime.get(Calendar.YEAR);
        //double mon = dateTime.Month;
        double mon = dateTime.get(Calendar.MONTH);
        //double day = dateTime.Day;
        double day = dateTime.get(Calendar.DAY_OF_MONTH);

        //Вычисление модифицированной  юлианской даты на начало суток
        double var1 = 10000 * year + 100 * mon + day;
        if (mon <= 2)
        {
            mon = mon + 12;
            year = year - 1;
        }
        double var2;
        if (var1 <= 15821004)
        {
            var2 = -2 + Math.floor((year + 4716) / 4) - 1179;
        }
        else
        {
            var2 = Math.floor(year / 400) - Math.floor(year / 100) + Math.floor(year / 4);
        }
        double var3 = 365 * year - 679004;

        // MJD - Модифицированная Юлианская дата
        double mjd = var3 + var2 + Math.floor(306001 * (mon + 1) / 10000) + day + ut / 24;
        double t0 = (mjd - 51544.5) / 36525; // мод.юл.дата на начало суток в юлианских столетиях

        // Вычисление эклиптических координат Луны и вектора (X, Y, Z) в эклиптической системе координат.
        // эклиптическая долгота
        double eklLon = 218.32 + 481267.883 * t0 + 6.29 * Math.sin(Deg2Rad * (134.9 + 477198.85 * t0))
                - 1.27 * Math.sin(Deg2Rad * (259.2 - 413335.38 * t0))
                + 0.66 * Math.sin(Deg2Rad * (235.7 + 890534.23 * t0))
                + 0.21 * Math.sin(Deg2Rad * (269.9 + 954397.7 * t0))
                - 0.19 * Math.sin(Deg2Rad * (357.5 + 35999.05 * t0))
                - 0.11 * Math.sin(Deg2Rad * (186.6 + 966404.05 * t0));
        eklLon = Deg2Rad * eklLon;

        // эклиптическая широта
        double eklLat = 5.13 * Math.sin(Deg2Rad * (93.3 + 483202.03 * t0))
                + 0.28 * Math.sin(Deg2Rad * (228.2 + 960400.87 * t0))
                - 0.28 * Math.sin(Deg2Rad * (318.3 + 6003.18 * t0))
                - 0.17 * Math.sin(Deg2Rad * (217.6 - 407332.2 * t0));
        eklLat = Deg2Rad * eklLat;

        // Горизонтальный паралакс
        double p = 0.9508 + 0.0518 * Math.cos(Deg2Rad * (134.9 + 477198.85 * t0))
                + 0.0095 * Math.cos(Deg2Rad * (259.2 - 413335.38 * t0))
                + 0.0078 * Math.cos(Deg2Rad * (235.7 + 890534.23 * t0))
                + 0.0028 * Math.cos(Deg2Rad * (269.9 + 954397.7 * t0));

        // Растояние до Луны в радиусах Земли
        double r = 1 / Math.sin(Deg2Rad * p);
    final double ER = 6378.136; // км радиус земли
        double rast = ER * r; // растояние до Луны
        double x = rast * Math.cos(eklLon) * Math.cos(eklLat);
        double y = rast * Math.sin(eklLon) * Math.cos(eklLat);
        double z = rast * Math.sin(eklLat);

        // Вычисление вектора Луны (X1,Y1,Z1) в экваториальной системе координат.
        double e = (84381.488 - 46.815 * t0 - 0.00059 * t0 * t0 + 0.001813 * t0 * t0 * t0) / 3600;
        e = Deg2Rad * e;
        double x1 = x;
        double y1 = y * Math.cos(e) - z * Math.sin(e);
        double z1 = y * Math.sin(e) + z * Math.cos(e);

        // Вычисление среднего звездного времени на дату MD и время UT.
        t0 = ((int)mjd - 51544.5) / 36525;
    final double A1 = 24110.54841;
    final double A2 = 8640184.812;
    final double A3 = 0.093104;
    final double A4 = 0.0000062;
        double s0 = A1 + A2 * t0 + A3 * t0 * t0 - A4 * t0 * t0 * t0; // звездное время в Гринвиче на начало суток в секундах
    final double W = 1.002737909350795;
        double ss = s0 + ut * 86400 * W / 24; // звездное время в секундах
        double s = ss / 240; // звездное время в градусах
        double sm = s + longitude; // Местное звезное время

        // Вычисление геоцентрического вектора наблюдателя (Xn, Yn, Zn) на момент звездного времени S.
        double h = 0; // высота точки наблюдения над уровнем моря
    final double RZ = 6378.14; // Радиус земли, км.
    final double F = 1 / 298.26; // Сжатие Земли
        double c = 1 / (Math.sqrt(1 + F * (F - 2) * Math.sin(latitude) * Math.sin(latitude)));
        double w = c * (1 - F) * (1 - F);

        double xn = (RZ * c + h) * Math.cos(Deg2Rad * s) * Math.cos(latitude);
        double yn = (RZ * c + h) * Math.sin(Deg2Rad * s) * Math.cos(latitude);
        double zn = (RZ * w + h) * Math.sin(latitude);

        // Вычисление топоцентрического вектора луны
        double xt = x1 - xn;
        double yt = y1 - yn;
        double zt = z1 - zn;

        // Вычисление топоцентрическое прямое восхождение и склонение
        // RA_L - прямое восхождение Луны  на нужный момент времени
        // DEC_L - склонение Луны на нужный момент времени
        double raL = Math.atan2(yt, xt) * Rad2Deg;
        double decL = Math.atan2(zt, Math.sqrt(xt * xt + yt * yt));

        // Вычисление Азимута и Угла места лун
        double th = sm - raL; //часовой угол
        double zh =
                Math.acos(Math.sin(latitude) * Math.sin(decL) + Math.cos(latitude) * Math.cos(decL) * Math.cos(Deg2Rad * th));
        // косинус зенитного угла
        moonelevation = Math.PI / 2 - zh; // угол места

        moonazimuth = Math.atan2(
                Math.sin(Deg2Rad * th) * Math.cos(decL) * Math.cos(latitude),
                Math.sin(Deg2Rad * h) * Math.sin(latitude) - Math.sin(decL)) + Math.PI;
    }

    // Широта и долгота на входе в градусах, азимут и высота на выходе в радианах.
    public static void CalculateSunPosition(Calendar dateTime, double latitude, double longitude, int timeZone)
    {
        // Convert to UTC
        //dateTime = dateTime.ToUniversalTime();

        //добавление часового пояса
        dateTime.add(Calendar.HOUR, -timeZone);
        // Number of days from J2000.0.
        double julianDate = 367 * dateTime.get(Calendar.YEAR) - (int)((7.0 / 4.0) * (dateTime.get(Calendar.YEAR) + (int)((dateTime.get(Calendar.MONTH) + 9.0) / 12.0)))
                + (int)((275.0 * dateTime.get(Calendar.MONTH)) / 9.0) + dateTime.get(Calendar.DAY_OF_MONTH) - 730531.5;

        double julianCenturies = julianDate / 36525.0;

        // Sidereal Time
        double siderealTimeHours = 6.6974 + 2400.0513 * julianCenturies;

        double siderealTimeUT = siderealTimeHours + (366.2422 / 365.2422) * dateTime.get(Calendar.HOUR_OF_DAY);

        double siderealTime = siderealTimeUT * 15 + longitude;

        // Refine to number of days (fractional) to specific time.
        //julianDate += dateTime.TimeOfDay.TotalHours / 24.0;
        julianDate += dateTime.get(Calendar.HOUR_OF_DAY) / 24.0;
        julianCenturies = julianDate / 36525.0;

        // Solar Coordinates
        double meanLongitude = CorrectAngle(Deg2Rad * (280.466 + 36000.77 * julianCenturies));

        double meanAnomaly = CorrectAngle(Deg2Rad * (357.529 + 35999.05 * julianCenturies));

        double equationOfCenter = Deg2Rad
                * ((1.915 - 0.005 * julianCenturies) * Math.sin(meanAnomaly) + 0.02 * Math.sin(2 * meanAnomaly));

        double elipticalLongitude = CorrectAngle(meanLongitude + equationOfCenter);

        double obliquity = (23.439 - 0.013 * julianCenturies) * Deg2Rad;

        // Right Ascension
        double rightAscension = Math.atan2(Math.cos(obliquity) * Math.sin(elipticalLongitude), Math.cos(elipticalLongitude));

        double declination = Math.asin(Math.sin(rightAscension) * Math.sin(obliquity));

        // Horizontal Coordinates
        double hourAngle = CorrectAngle(siderealTime * Deg2Rad) - rightAscension;

        if (hourAngle > Math.PI)
        {
            hourAngle -= 2 * Math.PI;
        }

        sunaltitude =
                Math.asin(
                        Math.sin(latitude * Deg2Rad) * Math.sin(declination)
                                + Math.cos(latitude * Deg2Rad) * Math.cos(declination) * Math.cos(hourAngle));

        // Nominator and denominator for calculating Azimuth
        // angle. Needed to test which quadrant the angle is in.
        double aziNom = -Math.sin(hourAngle);
        double aziDenom = Math.tan(declination) * Math.cos(latitude * Deg2Rad)
                - Math.sin(latitude * Deg2Rad) * Math.cos(hourAngle);

        sunazimuth = Math.atan(aziNom / aziDenom);

        if (aziDenom < 0) // In 2nd or 3rd quadrant
        {
            sunazimuth += Math.PI;
        }
        else if (aziNom < 0) // In 4th quadrant
        {
            sunazimuth += 2 * Math.PI;
        }
    }

    private static double CorrectAngle(double angleInRadians)
    {
        if (angleInRadians < 0)
        {
            return 2 * Math.PI - (Math.abs(angleInRadians) % (2 * Math.PI));
        }
        if (angleInRadians > 2 * Math.PI)
        {
            return angleInRadians % (2 * Math.PI);
        }
        return angleInRadians;
    }
}
