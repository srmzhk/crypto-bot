package org.srmzhk.cryptobot.model;
import lombok.Data;
import java.util.List;

@Data
public class User {
    int ID;
    String nickname;
    List<Favorite> favorites;
    boolean isConnected;
}
