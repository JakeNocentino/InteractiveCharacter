package hw3;

import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Random;

/**
 * Creates an application that displays a criminal that is locked up for a crime.
 *
 * The user has eight different buttons to choose different face parts to change. There are
 * checkboxes for putting the prisoner behind the bars and for taking the prisoners hat off.
 * There are two radio button options for either having a harmonica play or not play. A slider is
 * included to change the widths of the cell bars and a menu button is included to give the user
 * choices of the background that they want selected. The slider and background button are two
 * javaFX features I added that we did not explain in class. You can also click on the prisoners
 * shirt to change it to a random color.
 *
 * For the required transformation and visual effect, I made it so you can click on the prison
 * cell bars to rotate them 360 degrees. If you click on one multiple times, it may get stuck in
 * an odd position so I fixed that with the default button. For the visual effect, the background
 * button allows the user to change the effect of the background.
 *
 * For the "advanced feature", I included the harmonica audio and the background, which both are
 * considered media.
 *
 * @author Jake Nocentino
 * @version Created on 4/17/18
 */
public class JailCharacter extends Application {

    private final String[] QUOTES = {"I listed JavaScript as my favorite language.", "I changed my " +
            "major to Digital Forensics.", "I use spaces instead of tabs."};
    private final Image JAIL_CELL = new Image("hw3/JailAudio&Image/Jail_Image.jpg");
    private AudioClip harmonica;

    private Color skin = new Color(1, 0.8745, 0.7686, 1);
    private Color barColor = new Color(0.7529, 0.7529, 0.7529, 0.6);

    private final int NUM_OPTIONS = 3;

    private final String richBlue = "-fx-background-color: \n" +
            "        #000000,\n" +
            "        linear-gradient(#7ebcea, #2f4b8f),\n" +
            "        linear-gradient(#426ab7, #263e75),\n" +
            "        linear-gradient(#395cab, #223768);\n" +
            "    -fx-background-insets: 0,1,2,3;\n" +
            "    -fx-background-radius: 3,2,2,2;\n" +
            "    -fx-padding: 12 30 12 30;\n" +
            "    -fx-text-fill: white;\n" +
            "    -fx-font-size: 12px;";

    private final String backgroundColor = "-fx-background-color: linear-gradient(to right, #e6e6e6," +
            " #999999, #e6e6e6);";

    private int bodyCounter = 0;
    private int eyeCounter = 0;
    private int eyebrowCounter = 0;
    private int noseCounter = 0;
    private int mouthCounter = 0;
    private int quoteCounter = 0;
    private int headCounter = 0;

    private DropShadow ds = new DropShadow();

    private Button[] leftButtons = {new Button("Eyes"), new Button("Eyebrows"), new Button("Nose"),
            new Button("Mouth")};

    private Arc nose;
    private Arc body;
    private Rectangle neck;
    private Ellipse head;
    private Ellipse leftEye;
    private Circle eyeColorL;
    private Circle pupilL;
    private Line eyebrowL;
    private Ellipse rightEye;
    private Circle eyeColorR;
    private Circle pupilR;
    private Line eyebrowR;
    private Arc mouth;
    private Label quote = new Label(QUOTES[0]);

    private Rectangle hatBase = new Rectangle(255, 50, 143, 40);;
    private Arc hatTop = new Arc(326, 50, 71, 25, 0, 180);

    private Rectangle[] bars = new Rectangle[5];

    private CheckBox behindBars = new CheckBox("Behind Bars");
    CheckBox hat = new CheckBox("Hat");

    Pane center = new Pane();

    /**
     * Initialize the audio clip.
     */
    public JailCharacter() {
        URL resource = getClass().getResource("JailAudio&Image/Harmonica.wav");
        harmonica = new AudioClip(resource.toString());
    }
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);

        hatBase.setFill(Color.ORANGE);
        hatTop.setFill(Color.ORANGE);

        body = new Arc(330, 438, 160, 130, 20, 140);
        body.setType(ArcType.OPEN);
        body.setFill(Color.ORANGE);

        ImageView image = new ImageView(JAIL_CELL);
        ImageView sepiaCopy = new ImageView(JAIL_CELL);
        ImageView glowCopy = new ImageView(JAIL_CELL);

        // Create the prison bars and the event handler for clicking a bar
        for (int i = 0; i < bars.length; i++) {
            int n = 30 + i * 130;
            bars[i] = new Rectangle(30 + n, 0, 60, 394);
            bars[i].setFill(barColor);

            final int X = i;
            bars[i].setOnMousePressed(event -> {
                RotateTransition rt = new RotateTransition(Duration.millis(500), bars[X]);
                rt.setCycleCount(2);
                rt.setByAngle(360);
                rt.setAutoReverse(true);
                rt.play();
            });
        }

        center.getChildren().addAll(image, createBody(), createPrisonNum(), createNeck(), createHead(),
                createLeftEye(), createEyeColorL(), createPupilL(), createLeftEyebrow(),
                createRightEye(), createEyeColorR(), createPupilR(), createRightEyebrow(),
                createNose(), createMouth(), hatBase, hatTop, bars[0], bars[1],
                bars[2], bars[3], bars[4]);

        // Create and style the bottom section of the application
        BorderPane bottom = new BorderPane();

        FlowPane checkPane = new FlowPane();
        hat.setSelected(true);
        behindBars.setSelected(true);
        checkPane.getChildren().addAll(behindBars, hat);
        checkPane.setAlignment(Pos.CENTER);
        checkPane.setHgap(50);

        Slider barWidth = new Slider(0, 120, 60);
        barWidth.setShowTickLabels(true);
        barWidth.setMajorTickUnit(10);
        barWidth.setBlockIncrement(1);
        barWidth.setMinSize(600, 50);
        barWidth.setPadding(new Insets(10, 0, 10, 0));

        BorderPane realSliderPane = new BorderPane();

        FlowPane sliderPane = new FlowPane();
        sliderPane.setAlignment(Pos.CENTER);
        sliderPane.getChildren().add(barWidth);
        FlowPane sliderText = new FlowPane();
        Text bar = new Text("Slider to change the width of the bar");
        sliderText.setAlignment(Pos.CENTER);
        sliderText.getChildren().add(bar);

        realSliderPane.setTop(sliderPane);
        realSliderPane.setCenter(bar);

        // Create the background menu
        MenuButton backgroundMenu = new MenuButton();
        backgroundMenu.setText("Background\nMenu");
        backgroundMenu.setStyle(richBlue);
        backgroundMenu.setOnMouseClicked(event -> backgroundMenu.setCursor(Cursor.HAND));
        backgroundMenu.setOnMouseReleased(event -> backgroundMenu.setCursor(Cursor.DEFAULT));
        MenuItem sepiaTone = new MenuItem("Sepia Tone Effect");
        MenuItem glow = new MenuItem("Glow Effect");
        MenuItem regular = new MenuItem("Regular");
        backgroundMenu.getItems().addAll(sepiaTone, glow, regular);
        backgroundMenu.setPopupSide(Side.RIGHT);
        backgroundMenu.setPadding(new Insets(10, 10, 10, 10));

        // Create the event handler for the sepia tone menu option
        sepiaTone.setOnAction(event -> {
            SepiaTone st = new SepiaTone();
            st.setLevel(0.7);
            sepiaCopy.setEffect(st);
            if (center.getChildren().contains(image)) {
                center.getChildren().remove(image);
            }
            if (center.getChildren().contains(glowCopy)) {
                center.getChildren().remove(glowCopy);
            }
            if (center.getChildren().contains(sepiaCopy)) {
                center.getChildren().remove(sepiaCopy);
            }
            center.getChildren().add(sepiaCopy);
            checkIfBehindBars();
        });

        // Create the event handler for the glow tone menu option
        glow.setOnAction(event -> {
            glowCopy.setEffect(new Glow(0.8));
            if (center.getChildren().contains(image)) {
                center.getChildren().remove(image);
            }
            if (center.getChildren().contains(sepiaCopy)) {
                center.getChildren().remove(sepiaCopy);
            }
            if (center.getChildren().contains(glowCopy)) {
                center.getChildren().remove(glowCopy);
            }
            center.getChildren().add(glowCopy);
            checkIfBehindBars();
        });

        // Create the event handler for the regular tone menu option
        regular.setOnAction(event -> {
            if (center.getChildren().contains(glowCopy)) {
                center.getChildren().remove(glowCopy);
            }
            if (center.getChildren().contains(sepiaCopy)) {
                center.getChildren().remove(sepiaCopy);
            }
            if (center.getChildren().contains(image)) {
                center.getChildren().remove(image);
            }
            center.getChildren().add(image);
            checkIfBehindBars();
        });

        // Create the radio buttons for the harmonica
        VBox rButtons = new VBox();
        rButtons.setSpacing(20);
        RadioButton harmonicaOn = new RadioButton("Harmonica On");
        RadioButton harmonicaOff = new RadioButton("Harmonica Off");
        harmonicaOff.setTranslateX(1);
        harmonicaOff.setSelected(true);
        ToggleGroup tg = new ToggleGroup();
        tg.getToggles().addAll(harmonicaOn, harmonicaOff);
        rButtons.getChildren().addAll(harmonicaOn, harmonicaOff);

        // Create the event handlers for the radio buttons
        harmonicaOn.setOnAction(event -> {
            harmonica.setCycleCount(AudioClip.INDEFINITE);
            harmonica.play();
        });

        harmonicaOff.setOnAction(event -> harmonica.stop());

        bottom.setCenter(checkPane);
        bottom.setTop(realSliderPane);
        bottom.setRight(backgroundMenu);
        bottom.setLeft(rButtons);
        bottom.setPadding(new Insets(10, 10, 10, 10));

        // Create the event handler for changing the color of the prison uniform and hat
        body.setOnMouseClicked(event -> {
            Random rand = new Random();
            Double r = rand.nextDouble();
            Double g = rand.nextDouble();
            Double b = rand.nextDouble();
            Color randColor = new Color(r, g, b, 1);

            body.setFill(randColor);
            hatBase.setFill(randColor);
            hatTop.setFill(randColor);
        });

        // Create the event handlers for the checkboxes
        hat.setOnAction(event -> checkIfBehindBars());
        behindBars.setOnAction(event -> checkIfBehindBars());

        // Create the event handler for the slider
        barWidth.setOnMouseDragged(event -> {
            double x = barWidth.getValue();
            for (int i = 0; i < bars.length; i++) {
                bars[i].setWidth(x);
            }
        });

        // Create and style the left side of the application
        VBox left = new VBox();
        for (int i = 0; i < leftButtons.length; i++) {
            styleButton(leftButtons[i], richBlue);
        }

        // Create the event handler for the eye button
        leftButtons[0].setOnAction(event -> {
            center.getChildren().removeAll(leftEye, eyeColorL, pupilL, rightEye,
                    eyeColorR, pupilR);
            eyeCounter++;
            if (eyeCounter == NUM_OPTIONS) {
                eyeCounter = 0;
            }
            center.getChildren().addAll(createLeftEye(), createEyeColorL(), createPupilL(),
                    createRightEye(), createEyeColorR(), createPupilR());
            checkIfBehindBars();
        });

        // Create the event handler for the nose button
        leftButtons[2].setOnAction(event -> {
            center.getChildren().remove(nose);
            noseCounter++;
            if (noseCounter == NUM_OPTIONS) {
                noseCounter = 0;
            }
            center.getChildren().add(createNose());
            checkIfBehindBars();
        });

        // Create the event handler for the eyebrows button
        leftButtons[1].setOnAction(event -> {
            center.getChildren().removeAll(eyebrowL, eyebrowR);
            eyebrowCounter++;
            if (eyebrowCounter == NUM_OPTIONS) {
                eyebrowCounter = 0;
            }
            center.getChildren().addAll(createLeftEyebrow(), createRightEyebrow());
            checkIfBehindBars();
        });

        // Create the event handler for the mouth button
        leftButtons[3].setOnAction(event -> {
            center.getChildren().remove(mouth);
            mouthCounter++;
            if (mouthCounter == NUM_OPTIONS) {
                mouthCounter = 0;
            }
            center.getChildren().add(createMouth());
            checkIfBehindBars();
        });

        left.getChildren().addAll(leftButtons);
        left.setSpacing(40);
        left.setAlignment(Pos.CENTER);
        left.setPadding(new Insets(10));

        // Create and style the top of the application
        VBox top = new VBox();

        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));

        Label quoteIntro = new Label("I'm in jail because...");
        styleQuote(quoteIntro);
        styleQuote(quote);

        top.getChildren().addAll(quoteIntro, quote);
        top.setSpacing(20);
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(10));

        // Create and style the right side of the application
        VBox right = new VBox();
        Button headButton = new Button("Head");
        styleButton(headButton, richBlue);
        Button quoteButton = new Button("Quote");
        styleButton(quoteButton, richBlue);
        Button randomButton = new Button("Randomize");
        styleButton(randomButton, richBlue);
        Button defaultButton = new Button("Default");
        styleButton(defaultButton, richBlue);

        // Create the event handler for the random button
        randomButton.setOnAction(event -> {
            Random rand = new Random();
            headCounter = rand.nextInt(NUM_OPTIONS);
            mouthCounter = rand.nextInt(NUM_OPTIONS);
            eyeCounter = rand.nextInt(NUM_OPTIONS);
            noseCounter = rand.nextInt(NUM_OPTIONS);
            eyebrowCounter = rand.nextInt(NUM_OPTIONS);

            Double r = rand.nextDouble();
            Double g = rand.nextDouble();
            Double b = rand.nextDouble();
            Color randColor = new Color(r, g, b, 1);
            body.setFill(randColor);
            hatBase.setFill(randColor);
            hatTop.setFill(randColor);

            checkIfBehindBars();
        });

        // Create the event handlers for the default button
        defaultButton.setOnAction(event -> {
            headCounter = 0;
            mouthCounter = 0;
            eyeCounter = 0;
            noseCounter = 0;
            eyebrowCounter = 0;

            body.setFill(Color.ORANGE);
            hatBase.setFill(Color.ORANGE);
            hatTop.setFill(Color.ORANGE);

            for (int i = 0; i < bars.length; i++) {
                center.getChildren().removeAll(bars);
                int n = 30 + i * 130;
                bars[i] = new Rectangle(30 + n, 0, 60, 394);
                bars[i].setFill(barColor);
                center.getChildren().addAll(bars);
                final int X = i;

                bars[i].setOnMousePressed(event1 -> {
                    RotateTransition rt = new RotateTransition(Duration.millis(500), bars[X]);
                    rt.setCycleCount(2);
                    rt.setByAngle(360);
                    rt.setAutoReverse(true);
                    rt.play();
                });
            }

            checkIfBehindBars();
        });

        right.setAlignment(Pos.CENTER);
        right.setSpacing(40);
        right.setPadding(new Insets(10));

        right.getChildren().addAll(headButton, quoteButton, randomButton, defaultButton);

        // Create the event handler for the quote button
        quoteButton.setOnAction(event -> {
            quoteCounter++;
            top.getChildren().remove(quote);
            if (quoteCounter == NUM_OPTIONS) {
                quoteCounter = 0;
            }
            quote = new Label(QUOTES[quoteCounter]);
            styleQuote(quote);
            top.getChildren().add(quote);
        });

        // Create the event handler for the head button
        headButton.setOnAction(event -> {
            center.getChildren().remove(head);
            headCounter++;
            if (headCounter == NUM_OPTIONS) {
                headCounter = 0;
            }
            center.getChildren().add(createHead());
            checkIfBehindBars();
        });

        root.setCenter(center);
        root.setBottom(bottom);
        root.setLeft(left);
        root.setTop(top);
        root.setRight(right);
        root.setStyle(backgroundColor);

        primaryStage.setTitle("Bloomsburg Prison");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * This method is very important to the application. Because of the prison bars and the option
     * of placing the person in front of or behind the bars, this method determines what to do
     * when a new shape is added or removed. For example, if I clicked on the nose button to change
     * the nose and the behind bars checkbox was checked, this method would determine where to place
     * the nose. This method is called basically every time a shape is changed.
     */
    private void checkIfBehindBars() {
        if (behindBars.isSelected() && hat.isSelected()) {
            center.getChildren().removeAll(body, createPrisonNum(), neck, head, leftEye, eyeColorL,
                    pupilL, eyebrowL, rightEye, eyeColorR, pupilR, eyebrowR, nose, mouth, hatTop,
                    hatBase, bars[0], bars[1], bars[2], bars[3], bars[4]);
            center.getChildren().addAll(body, createPrisonNum(), createNeck(), createHead(),
                    createLeftEye(), createEyeColorL(), createPupilL(), createLeftEyebrow(),
                    createRightEye(), createEyeColorR(), createPupilR(), createRightEyebrow(),
                    createNose(), createMouth(),hatBase, hatTop, bars[0], bars[1], bars[2],
                    bars[3], bars[4]);
        }
        if (behindBars.isSelected() && !hat.isSelected()) {
            center.getChildren().removeAll(body, createPrisonNum(), neck, head, leftEye,
                    eyeColorL, pupilL, eyebrowL, rightEye, eyeColorR, pupilR, eyebrowR, nose,
                    mouth, bars[0], bars[1], bars[2], bars[3], bars[4], hatBase, hatTop);
            center.getChildren().addAll(body, createPrisonNum(), createNeck(),
                    createHead(), createLeftEye(), createEyeColorL(), createPupilL(),
                    createLeftEyebrow(), createRightEye(), createEyeColorR(), createPupilR(),
                    createRightEyebrow(), createNose(), createMouth(), bars[0], bars[1], bars[2],
                    bars[3], bars[4]);
        }
        if (hat.isSelected() && !behindBars.isSelected()) {
            center.getChildren().removeAll(body, createPrisonNum(), neck,
                    head, leftEye, eyeColorL, pupilL, eyebrowL, rightEye, eyeColorR, pupilR,
                    eyebrowR, nose, mouth, hatBase, hatTop, bars[0], bars[1], bars[2], bars[3],
                    bars[4]);
            center.getChildren().addAll(bars[0], bars[1], bars[2], bars[3], bars[4], body,
                    createPrisonNum(), createNeck(), createHead(), createLeftEye(),
                    createEyeColorL(), createPupilL(), createLeftEyebrow(), createRightEye(),
                    createEyeColorR(), createPupilR(), createRightEyebrow(), createNose(),
                    createMouth(), hatBase, hatTop);
        }
        if (!hat.isSelected() && !behindBars.isSelected()) {
            center.getChildren().removeAll(body, hatBase, hatTop, bars[0], bars[1], bars[2],
                    bars[3], bars[4], createPrisonNum(), neck, head, leftEye, eyeColorL,
                    pupilL, eyebrowL, rightEye, eyeColorR, pupilR, eyebrowR, nose, mouth);
            center.getChildren().addAll(bars[0], bars[1], bars[2], bars[3], bars[4],
                    body, createPrisonNum(), createNeck(), createHead(),
                    createLeftEye(), createEyeColorL(), createPupilL(), createLeftEyebrow(),
                    createRightEye(), createEyeColorR(), createPupilR(), createRightEyebrow(),
                    createNose(), createMouth());
        }
    }

    /**
     * Creates and returns the prisoner number.
     */
    private Text createPrisonNum() {
        Text num = new Text("24601");
        Font font = Font.font("Monospaced", FontWeight.BOLD, 20);
        num.setFont(font);
        num.setFill(Color.BLACK);
        num.setY(370);
        num.setX(370);
        return num;
    }

    /**
     * Creates and returns the body depending on what the body counter equals.
     */
    private Arc createBody() {
        body = new Arc(330, 438, 160, 130, 20, 140);
        body.setType(ArcType.OPEN);
        if (bodyCounter == 0) {
            body.setFill(Color.ORANGE);
        }
        return body;
    }

    /**
     * Creates and returns the neck.
     */
    private Rectangle createNeck() {
        neck = new Rectangle(295, 280, 70, 40);
        neck.setFill(skin);
        return neck;
    }

    /**
     * Creates and returns the head depending on what the head counter equals.
     */
    private Ellipse createHead() {
        if (headCounter == 0) {
            head = new Ellipse(327, 180, 100, 130);
        }
        if (headCounter == 1) {
            head = new Ellipse(327, 180, 160, 120);
        }
        if (headCounter == 2) {
            head = new Ellipse(327, 180, 70, 130);
        }
        head.setFill(skin);
        head.setStroke(Color.BLACK);
        head.setStrokeWidth(1);
        return head;
    }

    /**
     * Creates and returns the left eye depending on what the eye counter equals.
     */
    private Ellipse createLeftEye() {
        if (eyeCounter == 0) {
            leftEye = new Ellipse(290, 150, 20, 15);
        }
        if (eyeCounter == 1) {
            leftEye = new Ellipse(290, 150, 15, 20);
        }
        if (eyeCounter == 2) {
            leftEye = new Ellipse(290, 150, 20, 15);
            leftEye.setRotate(45);
        }
        leftEye.setFill(Color.WHITE);
        return leftEye;
    }

    /**
     * Creates and returns the left eye color depending on what the eye counter equals.
     */
    private Circle createEyeColorL() {
        if (eyeCounter == 0) {
            eyeColorL = new Circle(290, 150, 13, Color.ALICEBLUE);
        }
        if (eyeCounter == 1) {
            eyeColorL = new Circle(290, 150, 13, Color.LIGHTGREEN);
        }
        if (eyeCounter == 2) {
            eyeColorL = new Circle(290, 150, 13, Color.MISTYROSE);
        }
        return eyeColorL;
    }

    /**
     * Creates and returns the left eye pupil depending on what the eye counter equals.
     */
    private Circle createPupilL() {
        if (eyeCounter == 0) {
            pupilL = new Circle(292, 152, 5, Color.BLACK);
        }
        if (eyeCounter == 1) {
            pupilL = new Circle(296, 152, 5, Color.BLACK);
        }
        if (eyeCounter == 2) {
            pupilL = new Circle(295, 155, 5, Color.BLACK);
        }
        return pupilL;
    }

    /**
     * Creates and returns the left eyebrow depending on what the left eyebrow counter equals.
     */
    private Line createLeftEyebrow() {
        if (eyebrowCounter == 0) {
            eyebrowL = new Line(270, 110, 310, 130);
        }
        if (eyebrowCounter == 1) {
            eyebrowL = new Line(260, 130, 300, 115);
        }
        if (eyebrowCounter == 2) {
            eyebrowL = new Line(270, 125, 360, 125);
        }
        eyebrowL.setStroke(Color.BLACK);
        eyebrowL.setStrokeWidth(5);
        return eyebrowL;
    }

    /**
     * Creates and returns the right eye depending on what the eye counter equals.
     */
    private Ellipse createRightEye() {
        if (eyeCounter == 0) {
            rightEye = new Ellipse(370, 150, 20, 15);
        }
        if (eyeCounter == 1) {
            rightEye = new Ellipse(370, 150, 15, 20);
        }
        if (eyeCounter == 2) {
            rightEye = new Ellipse(370, 150, 20, 15);
            rightEye.setRotate(-45);
        }
        rightEye.setFill(Color.WHITE);
        return rightEye;
    }

    /**
     * Creates and returns the right eye color depending on what the eye counter equals.
     */
    private Circle createEyeColorR() {
        if (eyeCounter == 0) {
            eyeColorR = new Circle(370, 150, 13, Color.ALICEBLUE);
        }
        if (eyeCounter == 1) {
            eyeColorR = new Circle(370, 150, 13, Color.LIGHTGREEN);
        }
        if (eyeCounter == 2) {
            eyeColorR = new Circle(370, 150, 13, Color.MISTYROSE);
        }
        return eyeColorR;
    }

    /**
     * Creates and returns the right eye pupil depending on what the eye counter equals.
     */
    private Circle createPupilR() {
        if (eyeCounter == 0) {
            pupilR = new Circle(372, 152, 5, Color.BLACK);
        }
        if (eyeCounter == 1) {
            pupilR = new Circle(376, 152, 5, Color.BLACK);
        }
        if (eyeCounter == 2) {
            pupilR = new Circle(368, 156, 5, Color.BLACK);
        }
        return pupilR;
    }

    /**
     * Creates and returns the right eyebrow depending on what the right eyebrow counter equals.
     */
    private Line createRightEyebrow() {
        if (eyebrowCounter == 0) {
            eyebrowR = new Line(350, 130, 390, 110);
        }
        if (eyebrowCounter == 1) {
            eyebrowR = new Line(358, 115, 395, 130);
        }
        if (eyebrowCounter == 2) {
            eyebrowR = new Line(360, 125, 390, 125);
        }
        eyebrowR.setStroke(Color.BLACK);
        eyebrowR.setStrokeWidth(5);
        return eyebrowR;
    }

    /**
     * Creates and returns a nose depending on what the nose counter equals.
     */
    private Arc createNose() {
        if (noseCounter == 0) {
            nose = new Arc(340, 200, 20, 10, 270, 180);
        }
        if (noseCounter == 1) {
            nose = new Arc(340, 200, 50, 20, 270, 180);
        }
        if (noseCounter == 2) {
            nose = new Arc(340, 190, 10, 30, 270, 180);
        }
        nose.setType(ArcType.OPEN);
        nose.setStroke(Color.BLACK);
        nose.setStrokeWidth(2);
        nose.setFill(skin);
        return nose;
    }

    /**
     * Creates and returns a mouth depending on what the mouth counter equals.
     */
    private Arc createMouth() {
        if (mouthCounter == 0) {
            mouth = new Arc(320, 270, 50, 20, 20, 110);
        }
        if (mouthCounter == 1) {
            mouth = new Arc(320, 250, 10, 5, 170, 200);
        }
        if (mouthCounter == 2) {
            mouth = new Arc(330, 240, 40, 20, 190, 150);
        }
        mouth.setStroke(Color.BLACK);
        mouth.setStrokeWidth(2);
        mouth.setFill(skin);
        return mouth;
    }

    /**
     * This method is used to style a button. The user can select the button and the style he or
     * she wants.
     */
    private Button styleButton(Button button, String style) {
        button.setStyle(style);
        button.setOnMousePressed(event -> button.setCursor(Cursor.HAND));
        button.setOnMouseReleased(event -> button.setCursor(Cursor.DEFAULT));
        return button;
    }

    /**
     * Styles the quotes that are used at the top of the application.
     */
    private Label styleQuote(Label label) {
        label.setEffect(ds);
        label.setCache(true);
        label.setFont(Font.font(null, FontWeight.BOLD, 32));
        return label;
    }

    public static void main(String[] args) {
        launch(args);
    }
}