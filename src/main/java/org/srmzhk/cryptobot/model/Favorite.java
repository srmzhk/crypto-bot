package org.srmzhk.cryptobot.model;
import lombok.Data;

import java.util.Date;

@Data
public class Favorite {
    int ID;
    int userID;
    CryptoCurrency cryptoCurrency;
    Date addedDate;
    double addedPrice;
    double priceChangePercent;
    double percentForNotify;
}
