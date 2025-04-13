package com.example.foto_blur;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {
    ImageView original = new ImageView();
    ImageView result = new ImageView();
    ComboBox<String> choice = new ComboBox<>();

    @Override
    public void start(Stage stage) throws IOException {
        VBox root = new VBox();
        HBox h1 = new HBox();
        HBox h2 = new HBox();

        original.setFitHeight(500);
        original.setFitWidth(300);
        result.setFitHeight(500);
        result.setFitWidth(300);

        Button img = new Button("Выбрать изображение");
        choice.getItems().addAll("Инверсия", "Блюр", "Черно-белое");
        choice.setValue("Инверсия");

        Button processing = new Button("Обработка");
        Button save = new Button("Сохранить");

        root.getChildren().addAll(h1, h2);
        h1.getChildren().addAll(img, choice, processing, save);
        h2.getChildren().addAll(original, result);

        final Image[] loadedImage = {null};

        img.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                loadedImage[0] = new Image(file.toURI().toString());
                original.setImage(loadedImage[0]);
            }
        });

        processing.setOnAction(e -> {
            if (loadedImage[0] != null) {
                Image resultImage = null;
                switch (choice.getValue()) {
                    case "Инверсия":
                        resultImage = applyInversion(loadedImage[0]);
                        break;
                    case "Блюр":
                        resultImage = applyBlur(loadedImage[0]);
                        break;
                    case "Черно-белое":
                        resultImage = applyGrayscale(loadedImage[0]);
                        break;
                }
                result.setImage(resultImage);
            }
        });

        Scene scene = new Scene(root, 700, 600);
        stage.setTitle("Фото обработка");
        stage.setScene(scene);
        stage.show();
    }

    public static Image applyInversion(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        PixelReader reader = image.getPixelReader();
        WritableImage newImage = new WritableImage(width, height);
        PixelWriter writer = newImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                Color inverted = Color.color(1.0 - color.getRed(),
                        1.0 - color.getGreen(),
                        1.0 - color.getBlue());
                writer.setColor(x, y, inverted);
            }
        }
        return newImage;
    }

    public static Image applyBlur(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        PixelReader reader = image.getPixelReader();
        WritableImage blurred = new WritableImage(width, height);
        PixelWriter writer = blurred.getPixelWriter();

        int[] dx = {-1, 0, 1};
        int[] dy = {-1, 0, 1};

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                double r = 0, g = 0, b = 0;
                for (int i : dx) {
                    for (int j : dy) {
                        Color color = reader.getColor(x + i, y + j);
                        r += color.getRed();
                        g += color.getGreen();
                        b += color.getBlue();
                    }
                }
                int count = 9;
                writer.setColor(x, y, Color.color(r / count, g / count, b / count));
            }
        }

        return blurred;
    }

    public static Image applyGrayscale(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        PixelReader reader = image.getPixelReader();
        WritableImage grayImage = new WritableImage(width, height);
        PixelWriter writer = grayImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                double gray = 0.3 * color.getRed() + 0.59 * color.getGreen() + 0.11 * color.getBlue();
                writer.setColor(x, y, Color.color(gray, gray, gray));
            }
        }

        return grayImage;
    }

    public static void main(String[] args) {
        launch();
    }
}
