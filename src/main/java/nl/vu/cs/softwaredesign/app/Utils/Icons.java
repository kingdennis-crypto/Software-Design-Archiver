package nl.vu.cs.softwaredesign.app.Utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class Icons {
    public static ImageView createJavaFXIcon(String iconName) {
        Image iconImage = new Image(
                Objects.requireNonNull(Icons.class.getResourceAsStream("/nl/vu/cs/softwaredesign/icons/" + iconName)));

        ImageView iconImageView = new ImageView(iconImage);
        iconImageView.setFitWidth(16);
        iconImageView.setFitHeight(16);

        return iconImageView;
    }
}
