/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gamesnow;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Sujit T K
 */
public class Cards extends Application {

    private Pane pane_content = null;
    private ResourceLoader rl;
    private final double DBL_CARD_WIDTH = 120.0D, DBL_CARD_HEIGHT = 180.0D;
    private int intScorePlayerOne = 0, intScorePlayerTwo = 0, intCardsLeft = 6;
    private boolean blnPlayerOneActive = true;
    private ProgressBar pb = null;
    private Rectangle rGameSessionOver = null;
    private Text txtPlayerName1 = null, txtPlayerName2 = null;
    private FlipPane[] imgViewCard = new FlipPane[6];

    @Override
    public void start(Stage primaryStage) {
        rl = new ResourceLoader();
        pane_content = new Pane();
        pane_content.setBackground(new Background(new BackgroundFill(Color.web("#3A6E9B"), CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(pane_content, 1100, 500);
        //primaryStage.setMaximized(true);
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        this.getPlayerNames();
        //this.setupCards();   
        //this.showGameResult();
        primaryStage.setTitle("CARDS!!!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private class ProgressBar extends Pane {

        private Rectangle rPlayer1, rPlayer2;
        private Timeline t;
        private Text txtScorePlayer1, txtScorePlayer2;
        private final double BORDER_THICKNESS;

        public ProgressBar(final double WIDTH, final double HEIGHT, final double BORDER_THICKNESS, final String clrBorder, final String clrPlayer1, final String clrPlayer2) {
            this.setMinSize(WIDTH, HEIGHT);
            this.setMaxSize(WIDTH, HEIGHT);
            this.setPrefSize(WIDTH, HEIGHT);
            this.BORDER_THICKNESS = BORDER_THICKNESS;
            this.rPlayer1 = new Rectangle(BORDER_THICKNESS, BORDER_THICKNESS, (WIDTH / 2.0D) - BORDER_THICKNESS, HEIGHT - (2.0D * BORDER_THICKNESS));
            this.rPlayer2 = new Rectangle(BORDER_THICKNESS, BORDER_THICKNESS, WIDTH - (2.0D * BORDER_THICKNESS), HEIGHT - (2.0D * BORDER_THICKNESS));
            this.setBackground(new Background(new BackgroundFill(Color.web(clrBorder), CornerRadii.EMPTY, Insets.EMPTY)));
            this.rPlayer1.setFill(Color.web(clrPlayer1));
            this.rPlayer2.setFill(Color.web(clrPlayer2));
            this.t = new Timeline();
            this.txtScorePlayer1 = new Text("0");
            this.txtScorePlayer1.setLayoutX(30.0D);
            this.txtScorePlayer1.setLayoutY(25.0D);
            this.txtScorePlayer1.setFont(Font.font("Bell MT", FontWeight.BOLD, FontPosture.REGULAR, 20.0D));
            this.txtScorePlayer2 = new Text("0");
            this.txtScorePlayer2.setLayoutX(860.0D);
            this.txtScorePlayer2.setLayoutY(25.0D);
            this.txtScorePlayer2.setFont(Font.font("Bell MT", FontWeight.BOLD, FontPosture.REGULAR, 20.0D));
            this.getChildren().addAll(this.rPlayer2, this.rPlayer1, this.txtScorePlayer1, this.txtScorePlayer2);
            /*Bloom g = new Bloom();
            ColorAdjust ca = new ColorAdjust();
            this.setEffect(ca);
            Timeline temp = new Timeline();
            for(int i = 0; i < 240; i++) {
                temp.getKeyFrames().add(new KeyFrame(Duration.millis(i*(5)), new KeyValue(ca.saturationProperty(),i)));
            }
            temp.setAutoReverse(true);
            temp.setCycleCount(Timeline.INDEFINITE);
            temp.playFromStart();*/
        }
        
        private void reset() {
            this.txtScorePlayer1.setText("0");
            this.txtScorePlayer2.setText("0");
            this.rPlayer1.setWidth((this.getWidth() / 2.0D) - this.BORDER_THICKNESS);
        }

        private void update() {
            final double DBL_TOTAL_LENGTH = intScorePlayerOne + intScorePlayerTwo;
            final double DBL_LENGTH_PLAYER_1_BAR_CHANGE_TO = rPlayer2.getWidth() * (intScorePlayerOne / DBL_TOTAL_LENGTH);
            if (t.getStatus() == Timeline.Status.RUNNING) {
                t.stop();
                if(this.txtScorePlayer1.getScaleX() != 1.0D) {
                    this.txtScorePlayer1.setScaleX(1.0D);
                    this.txtScorePlayer1.setScaleY(1.0D);
                } else if (this.txtScorePlayer2.getScaleY() != 1.0D) {
                    this.txtScorePlayer2.setScaleX(1.0D);
                    this.txtScorePlayer2.setScaleY(1.0D);
                }
            }
            t.getKeyFrames().clear();
            if(!this.txtScorePlayer1.getText().equals(intScorePlayerOne+"")) {
                t.getKeyFrames().addAll(new KeyFrame(Duration.millis(300.0D), new KeyValue(this.txtScorePlayer1.scaleXProperty(),2.0D,Interpolator.EASE_IN)),
                                        new KeyFrame(Duration.millis(300.0D), new KeyValue(this.txtScorePlayer1.scaleYProperty(),2.0D,Interpolator.EASE_IN)),
                                        new KeyFrame(Duration.millis(400.0D), new KeyValue(this.txtScorePlayer1.textProperty(), "" + intScorePlayerOne)),
                                        new KeyFrame(Duration.millis(550.0D), new KeyValue(this.txtScorePlayer1.scaleXProperty(),2.0D,Interpolator.EASE_IN)),
                                        new KeyFrame(Duration.millis(550.0D), new KeyValue(this.txtScorePlayer1.scaleYProperty(),2.0D,Interpolator.EASE_IN)),
                                        new KeyFrame(Duration.millis(900.0D), new KeyValue(this.txtScorePlayer1.scaleXProperty(),1.0D,Interpolator.EASE_IN)),
                                        new KeyFrame(Duration.millis(900.0D), new KeyValue(this.txtScorePlayer1.scaleYProperty(),1.0D,Interpolator.EASE_IN)));
            } else {
                t.getKeyFrames().addAll(new KeyFrame(Duration.millis(300.0D), new KeyValue(this.txtScorePlayer2.scaleXProperty(),2.0D,Interpolator.EASE_IN)),
                                        new KeyFrame(Duration.millis(300.0D), new KeyValue(this.txtScorePlayer2.scaleYProperty(),2.0D,Interpolator.EASE_IN)),
                                        new KeyFrame(Duration.millis(400.0D), new KeyValue(this.txtScorePlayer2.textProperty(), "" + intScorePlayerTwo)),
                                        new KeyFrame(Duration.millis(550.0D), new KeyValue(this.txtScorePlayer2.scaleXProperty(),2.0D,Interpolator.EASE_IN)),
                                        new KeyFrame(Duration.millis(550.0D), new KeyValue(this.txtScorePlayer2.scaleYProperty(),2.0D,Interpolator.EASE_IN)),
                                        new KeyFrame(Duration.millis(900.0D), new KeyValue(this.txtScorePlayer2.scaleXProperty(),1.0D,Interpolator.EASE_IN)),
                                        new KeyFrame(Duration.millis(900.0D), new KeyValue(this.txtScorePlayer2.scaleYProperty(),1.0D,Interpolator.EASE_IN)));
            }
            t.getKeyFrames().add(new KeyFrame(Duration.millis(400.0D), new KeyValue(this.rPlayer1.widthProperty(), DBL_LENGTH_PLAYER_1_BAR_CHANGE_TO, Interpolator.EASE_OUT)));
            t.play();
        }
    }
    
    private void getPlayerNames() {
        this.txtPlayerName1 = new Text("Player 1");
        this.txtPlayerName2 = new Text("Player 2");
        this.txtPlayerName1.setLayoutX(120.0D);
        this.txtPlayerName1.setLayoutY(440.0D);
        this.txtPlayerName2.setLayoutX(940.0D);
        this.txtPlayerName2.setLayoutY(440.0D);
        Rectangle rect = new Rectangle(300.0D,100.0D,400.0D,300.0D);
        rect.setArcWidth(60.0D);
        rect.setArcHeight(60.0D);
        rect.setFill(Color.web("#07084F"));
        GaussianBlur gb = new GaussianBlur();
        gb.setRadius(20.0D);
        Text txtEnterDetails = new Text("DETAILS :");
        txtEnterDetails.setLayoutX(330.0D);
        txtEnterDetails.setLayoutY(140.0D);
        txtEnterDetails.setFont(Font.font("Stencil", FontWeight.NORMAL, FontPosture.REGULAR, 40.0D));
        txtEnterDetails.setUnderline(true);
        txtEnterDetails.setFill(Color.WHITESMOKE);
        //rect.setEffect(gb);
        Text txtPlayer1Name = new Text("Enter Player 1 Name :");
        txtPlayer1Name.setLayoutX(330.0D);
        txtPlayer1Name.setLayoutY(190.0D);
        txtPlayer1Name.setFill(Color.YELLOW);
        txtPlayer1Name.setFont(Font.font("Berlin Sans FB", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 25.0D));
        TextField tbPlayer1 = new TextField();
        tbPlayer1.setLayoutX(330.0D);
        tbPlayer1.setLayoutY(210.0D);
        tbPlayer1.setFont(Font.font("Berlin Sans FB", FontWeight.MEDIUM, FontPosture.REGULAR, 18.0D));
        tbPlayer1.setMinWidth(320.0D);
        tbPlayer1.setMaxWidth(320.0D);
        tbPlayer1.setPrefWidth(320.0D);
        tbPlayer1.textProperty().addListener((observableList, oldValue, newValue) -> {   
            newValue = newValue.replaceAll("[^a-zA-Z ]", "").stripLeading();
            tbPlayer1.setText(newValue);
            if (newValue.length() > 20) {
                tbPlayer1.setText(oldValue);
            }
        });
        Text txtPlayer2Name = new Text("Enter Player 2 Name :");
        txtPlayer2Name.setLayoutX(330.0D);
        txtPlayer2Name.setLayoutY(280.0D);
        txtPlayer2Name.setFill(Color.YELLOW);
        txtPlayer2Name.setFont(Font.font("Berlin Sans FB", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 25.0D));
        TextField tbPlayer2 = new TextField();
        tbPlayer2.setLayoutX(330.0D);
        tbPlayer2.setLayoutY(300.0D);
        tbPlayer2.setFont(Font.font("Berlin Sans FB", FontWeight.MEDIUM, FontPosture.REGULAR, 18.0D));
        tbPlayer2.setMinWidth(320.0D);
        tbPlayer2.setMaxWidth(320.0D);
        tbPlayer2.setPrefWidth(320.0D);
        tbPlayer2.textProperty().addListener((observableList, oldValue, newValue) -> {  
            newValue = newValue.replaceAll("[^a-zA-Z ]", "").stripLeading();
            tbPlayer2.setText(newValue);
            if (newValue.length() > 20) {
                tbPlayer2.setText(oldValue);
            }
        });
        Rectangle rectStart = new Rectangle(400.0D,344.0D,100.0D,40.0D);
        rectStart.setFill(Color.web("#19A74E"));
        rectStart.setArcWidth(10.0D);
        rectStart.setArcHeight(10.0D);
        Text txtStart = new Text("START");
        txtStart.setLayoutX(416.0D);
        txtStart.setLayoutY(372.0D);
        txtStart.setFill(Color.web("#0a0a0a"));
        txtStart.setMouseTransparent(true);
        txtStart.setFont(Font.font("Berlin Sans FB", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 25.0D));
        rectStart.setOnMouseEntered(eh->{
            rectStart.setFill(Color.web("#05B349"));
            txtStart.setScaleX(1.2D);
            txtStart.setScaleY(1.2D);
        });
        rectStart.setOnMouseExited(eh->{
            rectStart.setFill(Color.web("#19A74E"));
            txtStart.setScaleX(1.0D);
            txtStart.setScaleY(1.0D);
        });
        rectStart.setOnMousePressed(eh->{
            if(false) {
                
            } else {
                this.txtPlayerName1.setText(tbPlayer1.getText());
                this.txtPlayerName2.setText(tbPlayer2.getText());
                this.pane_content.getChildren().removeAll(rect,txtEnterDetails,txtPlayer1Name,tbPlayer1,txtPlayer2Name,tbPlayer2,rectStart,txtStart);
                this.pane_content.getChildren().addAll(this.txtPlayerName1,this.txtPlayerName2);
                this.setupCards();   
            }
        });
        this.pane_content.getChildren().addAll(rect,txtEnterDetails,txtPlayer1Name,tbPlayer1,txtPlayer2Name,tbPlayer2,rectStart,txtStart);
    }

    public void buildGameSessionOverRectangle(final double WIDTH, final double HEIGHT, final double ROUNDED_EDGE_RADIUS, final String strColor, final boolean visible) {
        this.rGameSessionOver = new Rectangle();
        this.rGameSessionOver.setWidth(WIDTH);
        this.rGameSessionOver.setHeight(HEIGHT);
        this.rGameSessionOver.setArcWidth(ROUNDED_EDGE_RADIUS);
        this.rGameSessionOver.setArcHeight(ROUNDED_EDGE_RADIUS);
        this.rGameSessionOver.setFill(Color.web(strColor));
        this.rGameSessionOver.setVisible(visible);
    }

    private void setupCards() {
        ArrayList<Integer> arr_cards_available = new ArrayList<Integer>(Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10));
        int[] int_card_number = new int[6];
        double X = 100.0D, Y = 100.0D;
        Timeline t = new Timeline();
        final int INT_CARD_FLIPPED = -101;
        for (int i = 0; i < 6; i++) {
            int random_card_number = arr_cards_available.get((int) (Math.random() * arr_cards_available.size()));
            int removed = arr_cards_available.remove(arr_cards_available.indexOf(random_card_number));
            int_card_number[i] = random_card_number;
            //System.out.println((i + 1) + "->c = " + random_card_number);
            if (Math.random() < 0.5D) {
                this.imgViewCard[i] = new FlipPane(this.rl.imgBackCard, this.rl.imgCardsDiamond[random_card_number - 2]);
            } else if (Math.random() > 0.5D) {
                this.imgViewCard[i] = new FlipPane(this.rl.imgBackCard, this.rl.imgCardsClover[random_card_number - 2]);
            } else if (Math.random() < 0.5D) {
                this.imgViewCard[i] = new FlipPane(this.rl.imgBackCard, this.rl.imgCardsSpade[random_card_number - 2]);
            } else {
                this.imgViewCard[i] = new FlipPane(this.rl.imgBackCard, this.rl.imgCardsHeart[random_card_number - 2]);
            }
            this.imgViewCard[i].setVisible(false);
            this.pane_content.getChildren().add(imgViewCard[i]);
            this.imgViewCard[i].setLayoutX(X);
            this.imgViewCard[i].setLayoutY(Y);
            t.getKeyFrames().addAll(new KeyFrame(Duration.millis(1), new KeyValue(this.imgViewCard[i].visibleProperty(), true)),
                    new KeyFrame(Duration.ZERO, new KeyValue(this.imgViewCard[i].layoutXProperty(), 100.0D)),
                    new KeyFrame(Duration.millis(800.0D), new KeyValue(this.imgViewCard[i].layoutXProperty(), X)));
            X += this.DBL_CARD_WIDTH + 20.0D;
            final int I = i;
            this.imgViewCard[i].setOnMouseClicked(eh -> {
                if (int_card_number[I] != INT_CARD_FLIPPED) {
                    this.imgViewCard[I].flip();
                    if (blnPlayerOneActive) {
                        intScorePlayerOne += int_card_number[I];
                        blnPlayerOneActive = false;
                    } else {
                        intScorePlayerTwo += int_card_number[I];
                        blnPlayerOneActive = true;
                    }
                    int_card_number[I] = INT_CARD_FLIPPED;
                    Timeline t_top = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(this.imgViewCard[I].scaleXProperty(), 1.0D)),
                            new KeyFrame(Duration.ZERO, new KeyValue(this.imgViewCard[I].scaleYProperty(), 1.0D)),
                            new KeyFrame(Duration.millis(200.0D), new KeyValue(this.imgViewCard[I].scaleXProperty(), 1.2D)),
                            new KeyFrame(Duration.millis(200.0D), new KeyValue(this.imgViewCard[I].scaleYProperty(), 1.2D)),
                            new KeyFrame(Duration.millis(400.0D), new KeyValue(this.imgViewCard[I].scaleXProperty(), 1.0D)),
                            new KeyFrame(Duration.millis(400.0D), new KeyValue(this.imgViewCard[I].scaleYProperty(), 1.0D)),
                            new KeyFrame(Duration.millis(400.0D), new KeyValue(this.imgViewCard[I].layoutYProperty(), 100.0D)),
                            new KeyFrame(Duration.millis(800.0D), new KeyValue(this.imgViewCard[I].layoutYProperty(), -this.DBL_CARD_HEIGHT)));
                    t_top.setOnFinished(e -> {
                        this.pane_content.getChildren().remove(this.imgViewCard[I]);
                        if (--intCardsLeft == 0) {
                            for(int j = 0; j < 6; j++) {
                                this.pane_content.getChildren().remove(this.imgViewCard[j]);
                                this.imgViewCard[j] = null;
                            }
                            this.showGameResult();
                        }
                    });
                    imgViewCard[I].tl_flip.setOnFinished(e -> {
                        t_top.setDelay(Duration.millis(400.0D));
                        t_top.play();
                        this.pb.update();
                    });
                }
            });
        }
        t.setDelay(Duration.millis(300.0D));
        t.play();
        this.pb = new ProgressBar(900.0D, 40.0D, 3.0D, "#000000", "#FCF541", "#FC22A6");
        this.pb.setLayoutX(100.0D);
        this.pb.setLayoutY(450.0D);
        this.pane_content.getChildren().add(pb);
    }
    
    private void resetGame() {
        this.intCardsLeft = 6;
        this.intScorePlayerOne = 0;
        this.intScorePlayerTwo = 0;
        this.pb.reset();
        this.setupCards();
    }
    
    private void showGameResult() {
        if(this.intScorePlayerOne != this.intScorePlayerTwo) {
            Text txtCongradulate = new Text("CONGRATULATIONS"), txtWinner = new Text((this.intScorePlayerOne > this.intScorePlayerTwo) ? this.txtPlayerName1.getText() : this.txtPlayerName2.getText());
            txtCongradulate.setLayoutX(430.0D);
            txtCongradulate.setLayoutY(200.0D);
            txtCongradulate.setVisible(false);
            txtCongradulate.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20.0D));
            txtWinner.setLayoutX((this.intScorePlayerOne > this.intScorePlayerTwo) ? this.txtPlayerName1.getLayoutX(): this.txtPlayerName2.getLayoutX());
            txtWinner.setLayoutY((this.intScorePlayerOne > this.intScorePlayerTwo) ? this.txtPlayerName1.getLayoutY() : this.txtPlayerName2.getLayoutY());
            txtWinner.setVisible(false);        
            this.buildGameSessionOverRectangle(400.0D, 230.0D, 46.0D, "#F9F48F", true);
            this.rGameSessionOver.setLayoutX(336.0D);
            this.rGameSessionOver.setLayoutY(-290.0D);
            Text txtScoreDifference = new Text("has won " + ((this.intScorePlayerOne <= this.intScorePlayerTwo)  ? this.txtPlayerName1.getText() : this.txtPlayerName2.getText()) 
                                                          + " by " + Math.abs(this.intScorePlayerOne-this.intScorePlayerTwo) + " points.");
            txtScoreDifference.setLayoutX(450.0D);
            txtScoreDifference.setLayoutY(290.0D);
            txtScoreDifference.setOpacity(0.0D);
            Timeline tl_txt_congradulations_breathe = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(txtCongradulate.scaleXProperty(), 1.0D)),
                    new KeyFrame(Duration.ZERO, new KeyValue(txtCongradulate.scaleYProperty(), 1.0D)),
                    new KeyFrame(Duration.millis(700.0D), new KeyValue(txtCongradulate.scaleXProperty(), 1.8D)),
                    new KeyFrame(Duration.millis(700.0D), new KeyValue(txtCongradulate.scaleYProperty(), 1.8D)));
            tl_txt_congradulations_breathe.setAutoReverse(true);
            tl_txt_congradulations_breathe.setCycleCount(6);
            Timeline tGameSessionPane = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(this.rGameSessionOver.layoutYProperty(), -190.0D)),
                    new KeyFrame(Duration.ZERO, new KeyValue(this.rGameSessionOver.opacityProperty(), 0.3D)),
                    new KeyFrame(Duration.millis(1200D), new KeyValue(this.rGameSessionOver.opacityProperty(), 1.0D)),
                    new KeyFrame(Duration.millis(1200D), new KeyValue(this.rGameSessionOver.layoutYProperty(), 160.0D, Interpolator.EASE_OUT)));        
            Rectangle rectPlayAgain = new Rectangle();
            rectPlayAgain.setWidth(160.0D);
            rectPlayAgain.setHeight(40.0D);
            rectPlayAgain.setArcWidth(20.0D);
            rectPlayAgain.setArcHeight(20.0D);
            rectPlayAgain.setLayoutX(455.0D);
            rectPlayAgain.setLayoutY(330.0D);
            rectPlayAgain.setFill(Color.web("#3100FC"));
            rectPlayAgain.setOpacity(0.0D);
            rectPlayAgain.setOnMouseEntered(e->{
                rectPlayAgain.setFill(Color.web("#920FFC"));
            });
            rectPlayAgain.setOnMouseExited(e->{
                rectPlayAgain.setFill(Color.web("#3100FC"));
            });
            rectPlayAgain.setId("NOT CLICKED");
            Text tPlayAgain = new Text("PLAY AGAIN");
            tPlayAgain.setLayoutX(480.0D);
            tPlayAgain.setLayoutY(355.0D);
            tPlayAgain.setFill(Color.web("#F4F3F2"));
            tPlayAgain.setFont(Font.font("Consolas", FontWeight.BOLD, FontPosture.REGULAR, 20.0D));
            tPlayAgain.setMouseTransparent(true);
            tPlayAgain.setOpacity(0.0D);
            rectPlayAgain.setOnMouseClicked(e->{
                if(rectPlayAgain.getId().equals("NOT CLICKED")) {
                    rectPlayAgain.setId("CLICKED");
                    Timeline t_remove = new Timeline(new KeyFrame(Duration.millis(800.0D), new KeyValue(txtCongradulate.opacityProperty(),0.0D)),
                                                     new KeyFrame(Duration.millis(800.0D), new KeyValue(tPlayAgain.opacityProperty(),0.0D)),
                                                     new KeyFrame(Duration.millis(800.0D), new KeyValue(txtScoreDifference.opacityProperty(),0.0D)),
                                                     new KeyFrame(Duration.millis(800.0D), new KeyValue(txtWinner.opacityProperty(),0.0D)),
                                                     new KeyFrame(Duration.millis(800.0D), new KeyValue(rectPlayAgain.opacityProperty(),0.0D)),
                                                     new KeyFrame(Duration.millis(800.0D), new KeyValue(this.rGameSessionOver.yProperty(),this.rGameSessionOver.getY())),
                                                     new KeyFrame(Duration.millis(1600.0D), new KeyValue(this.rGameSessionOver.yProperty(),-390.0D))
                    );
                    t_remove.setOnFinished(eh->{
                        this.pane_content.getChildren().removeAll(txtCongradulate,txtWinner,txtScoreDifference,rectPlayAgain,tPlayAgain,this.rGameSessionOver);
                        this.rGameSessionOver = null;
                        resetGame();
                    });
                    t_remove.play();
                }
            });
            Timeline tl_txt_winner_popup = new Timeline(/*new KeyFrame(Duration.ZERO, new KeyValue(txtWinner.layoutXProperty(), 
                    (this.intScorePlayerOne > this.intScorePlayerTwo) ? this.txtPlayerName1.getLayoutX(): this.txtPlayerName2.getLayoutX())),
                    new KeyFrame(Duration.ZERO, new KeyValue(txtWinner.layoutYProperty(), 
                    (this.intScorePlayerOne > this.intScorePlayerTwo) ? this.txtPlayerName1.getLayoutY() : this.txtPlayerName2.getLayoutY())),*/
                    new KeyFrame(Duration.millis(700.0D), new KeyValue(txtWinner.layoutXProperty(), 500.0D)),
                    new KeyFrame(Duration.millis(700.0D), new KeyValue(txtWinner.layoutYProperty(), 250.0D)),
                    new KeyFrame(Duration.millis(700.0D), new KeyValue(txtWinner.scaleXProperty(), 2.8D)),
                    new KeyFrame(Duration.millis(700.0D), new KeyValue(txtWinner.scaleYProperty(), 2.8D)),
                    new KeyFrame(Duration.millis(700.0D), new KeyValue(rectPlayAgain.opacityProperty(), 0.0D)),
                    new KeyFrame(Duration.millis(700.0D), new KeyValue(txtScoreDifference.opacityProperty(), 0.0D)),
                    new KeyFrame(Duration.millis(700.0D), new KeyValue(tPlayAgain.opacityProperty(), 0.0D)),
                    new KeyFrame(Duration.millis(1300.0D), new KeyValue(rectPlayAgain.opacityProperty(), 1.0D)),
                    new KeyFrame(Duration.millis(1300.0D), new KeyValue(txtScoreDifference.opacityProperty(), 1.0D)),
                    new KeyFrame(Duration.millis(1300.0D), new KeyValue(tPlayAgain.opacityProperty(), 1.0D))
            );
            tGameSessionPane.setOnFinished(e -> {
                txtCongradulate.setVisible(true);
                txtWinner.setVisible(true);
                tl_txt_congradulations_breathe.play();
                tl_txt_winner_popup.play();
            });
            tGameSessionPane.setDelay(Duration.millis(500.0D));
            tGameSessionPane.play();
            this.pane_content.getChildren().addAll(this.rGameSessionOver,txtCongradulate,txtWinner,txtScoreDifference,rectPlayAgain,tPlayAgain);        
        } else {
            Text txtDraw = new Text("It's a STALEMATE");
            txtDraw.setLayoutX(420.0D);
            txtDraw.setLayoutY(240.0D);
            txtDraw.setOpacity(0.0D);
            txtDraw.setWrappingWidth(250.0D);
            txtDraw.setTextAlignment(TextAlignment.CENTER);
            txtDraw.setFont(Font.font("Consolas", FontWeight.BOLD, FontPosture.REGULAR, 50.0D));
            Rectangle rectPlayAgain = new Rectangle();
            rectPlayAgain.setWidth(160.0D);
            rectPlayAgain.setHeight(40.0D);
            rectPlayAgain.setArcWidth(20.0D);
            rectPlayAgain.setArcHeight(20.0D);
            rectPlayAgain.setLayoutX(455.0D);
            rectPlayAgain.setLayoutY(330.0D);
            rectPlayAgain.setFill(Color.web("#3100FC"));
            rectPlayAgain.setOpacity(0.0D);
            rectPlayAgain.setOnMouseEntered(e->{
                rectPlayAgain.setFill(Color.web("#920FFC"));
            });
            rectPlayAgain.setOnMouseExited(e->{
                rectPlayAgain.setFill(Color.web("#3100FC"));
            });
            rectPlayAgain.setId("NOT CLICKED");
            Text tPlayAgain = new Text("PLAY AGAIN");
            tPlayAgain.setLayoutX(480.0D);
            tPlayAgain.setLayoutY(355.0D);
            tPlayAgain.setFill(Color.web("#F4F3F2"));
            tPlayAgain.setFont(Font.font("Consolas", FontWeight.BOLD, FontPosture.REGULAR, 20.0D));
            tPlayAgain.setMouseTransparent(true);
            tPlayAgain.setOpacity(0.0D);
            rectPlayAgain.setOnMouseClicked(e->{
                if(rectPlayAgain.getId().equals("NOT CLICKED")) {
                    rectPlayAgain.setId("CLICKED");
                    Timeline t_remove = new Timeline(new KeyFrame(Duration.millis(800.0D), new KeyValue(txtDraw.opacityProperty(),0.0D)),
                                                     new KeyFrame(Duration.millis(800.0D), new KeyValue(tPlayAgain.opacityProperty(),0.0D)),
                                                     new KeyFrame(Duration.millis(800.0D), new KeyValue(rectPlayAgain.opacityProperty(),0.0D)),
                                                     new KeyFrame(Duration.millis(800.0D), new KeyValue(this.rGameSessionOver.yProperty(),this.rGameSessionOver.getY())),
                                                     new KeyFrame(Duration.millis(1600.0D), new KeyValue(this.rGameSessionOver.yProperty(),-390.0D))
                    );
                    t_remove.setOnFinished(eh->{
                        this.pane_content.getChildren().removeAll(txtDraw,rectPlayAgain,tPlayAgain,this.rGameSessionOver);
                        this.rGameSessionOver = null;
                        resetGame();
                    });
                    t_remove.play();
                }
            });
            this.buildGameSessionOverRectangle(400.0D, 230.0D, 46.0D, "#F9F48F", true);
            this.rGameSessionOver.setLayoutX(336.0D);
            this.rGameSessionOver.setLayoutY(-290.0D);
            Timeline tGameSessionPane = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(this.rGameSessionOver.layoutYProperty(), -190.0D)),
                    new KeyFrame(Duration.ZERO, new KeyValue(this.rGameSessionOver.opacityProperty(), 0.3D)),
                    new KeyFrame(Duration.millis(1200D), new KeyValue(this.rGameSessionOver.opacityProperty(), 1.0D)),
                    new KeyFrame(Duration.millis(1200D), new KeyValue(this.rGameSessionOver.layoutYProperty(), 160.0D, Interpolator.EASE_OUT)),
                    new KeyFrame(Duration.millis(1200D), new KeyValue(tPlayAgain.opacityProperty(), 0.0D)),
                    new KeyFrame(Duration.millis(1200D), new KeyValue(rectPlayAgain.opacityProperty(), 0.0D)),
                    new KeyFrame(Duration.millis(1200D), new KeyValue(txtDraw.opacityProperty(), 0.0D)),
                    new KeyFrame(Duration.millis(2000D), new KeyValue(tPlayAgain.opacityProperty(), 1.0D, Interpolator.EASE_OUT)),
                    new KeyFrame(Duration.millis(2000D), new KeyValue(rectPlayAgain.opacityProperty(), 1.0D, Interpolator.EASE_OUT)),
                    new KeyFrame(Duration.millis(2000D), new KeyValue(txtDraw.opacityProperty(), 1.0D, Interpolator.EASE_OUT))
            );
            tGameSessionPane.play();
            this.pane_content.getChildren().addAll(this.rGameSessionOver,txtDraw,rectPlayAgain,tPlayAgain);    
        }
    }

    private class ResourceLoader {

        public final Image imgBackCard;
        private final Image[] imgCardsDiamond = new Image[9], imgCardsHeart = new Image[9], imgCardsSpade = new Image[9], imgCardsClover = new Image[9];

        public ResourceLoader() {
            imgBackCard = new Image(Cards.class.getResource("/res/cards/cardbackcover.png").toString(), DBL_CARD_WIDTH, DBL_CARD_HEIGHT, true, true);
            for (int i = 0; i < 9; i++) {
                this.imgCardsDiamond[i] = new Image(Cards.class.getResource("/res/cards/" + (i + 2) + "D.png").toString(), DBL_CARD_WIDTH, DBL_CARD_HEIGHT, true, true);
                this.imgCardsHeart[i] = new Image(Cards.class.getResource("/res/cards/" + (i + 2) + "H.png").toString(), DBL_CARD_WIDTH, DBL_CARD_HEIGHT, true, true);
                this.imgCardsSpade[i] = new Image(Cards.class.getResource("/res/cards/" + (i + 2) + "S.png").toString(), DBL_CARD_WIDTH, DBL_CARD_HEIGHT, true, true);
                this.imgCardsClover[i] = new Image(Cards.class.getResource("/res/cards/" + (i + 2) + "C.png").toString(), DBL_CARD_WIDTH, DBL_CARD_HEIGHT, true, true);
            }
        }
    }

    private class FlipPane extends StackPane {

        private final StackPane spFront, spBack;
        private SimpleDoubleProperty angleProperty = new SimpleDoubleProperty(Math.PI / 2.0D);
        private PerspectiveTransform transformProperty = new PerspectiveTransform();
        private SimpleBooleanProperty flippedProperty = new SimpleBooleanProperty(true);
        private Timeline tl_flip;
        private double DBL_ANIMATION_DURATION_MILLIS = 400.0D;

        public FlipPane(final Image imgFront, final Image imgBack) {
            this.spFront = new StackPane();
            this.spBack = new StackPane();
            this.spFront.setEffect(transformProperty);
            this.spFront.visibleProperty().bind(flippedProperty);
            this.spFront.getChildren().setAll(new ImageView(imgFront));
            this.spBack.setEffect(transformProperty);
            this.spBack.visibleProperty().bind(flippedProperty.not());
            this.spBack.getChildren().setAll(new ImageView(imgBack));
            this.getChildren().addAll(this.spFront, this.spBack);
            this.widthProperty().addListener(e -> {
                this.recalculateTransformation(angleProperty.doubleValue());
            });
            this.heightProperty().addListener(e -> {
                this.recalculateTransformation(angleProperty.doubleValue());
            });
            this.angleProperty = new SimpleDoubleProperty(Math.PI / 2.0D);
            this.angleProperty.addListener((ov, newValue, oldValue) -> {
                this.recalculateTransformation(newValue.doubleValue());
            });
            tl_flip = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(this.angleProperty, Math.PI / 2.0D)),
                    new KeyFrame(Duration.millis(this.DBL_ANIMATION_DURATION_MILLIS / 2.0D), new KeyValue(this.angleProperty, 0, Interpolator.EASE_IN)),
                    new KeyFrame(Duration.millis(this.DBL_ANIMATION_DURATION_MILLIS / 2), e -> {
                        this.flippedProperty.set(this.flippedProperty.not().get());
                    }),
                    new KeyFrame(Duration.millis(this.DBL_ANIMATION_DURATION_MILLIS / 2), new KeyValue(this.angleProperty, Math.PI)),
                    new KeyFrame(Duration.millis(this.DBL_ANIMATION_DURATION_MILLIS), new KeyValue(this.angleProperty, Math.PI / 2.0D, Interpolator.EASE_OUT))
            );
        }

        private void flip() {
            if (this.tl_flip.getStatus() != Timeline.Status.RUNNING) {
                this.tl_flip.setRate(flippedProperty.get() ? 1.0D : -1.0D);
                this.tl_flip.play();
            }
        }

        private void recalculateTransformation(final double anglePropertyProperty) {
            final double insetsTop = this.getInsets().getTop() * 2;
            final double insetsLeft = this.getInsets().getLeft() * 2;
            final double radius = this.widthProperty().subtract(insetsLeft).divide(2).doubleValue();
            final double height = this.heightProperty().subtract(insetsTop).doubleValue();
            final double back = height / 10;
            transformProperty.setUlx(radius - Math.sin(anglePropertyProperty) * radius);
            transformProperty.setUly(0 - Math.cos(anglePropertyProperty) * back);
            transformProperty.setUrx(radius + Math.sin(anglePropertyProperty) * radius);
            transformProperty.setUry(0 + Math.cos(anglePropertyProperty) * back);
            transformProperty.setLrx(radius + Math.sin(anglePropertyProperty) * radius);
            transformProperty.setLry(height - Math.cos(anglePropertyProperty) * back);
            transformProperty.setLlx(radius - Math.sin(anglePropertyProperty) * radius);
            transformProperty.setLly(height + Math.cos(anglePropertyProperty) * back);
        }
    }
}
