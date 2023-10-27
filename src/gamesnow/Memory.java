/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gamesnow;

import java.util.ArrayList;
import java.util.Collections;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
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
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Sujit T K
 */
public class Memory extends Application {
    private Pane pane_base;
    private final ResourceCenter RC = new ResourceCenter();
    private final double DBL_WINDOW_WIDTH = 1100.0D, DBL_WINDOW_HEIGHT = 530.0D,  DBL_CARD_WIDTH = 60.0D, DBL_CARD_HEIGHT = 110.0D;
    private Values V;
    private FlipPane[] fp;
    private boolean blnMouseActionAllowed = false;
    private Timeline tl_handle_card_anim;
    private char charStatus;
    private Text lblTimeElapsed;
    
    @Override
    public void start(Stage stage) {
        this.pane_base = new Pane();
        this.V = new Values();
        this.V.initialize(4,6);
        this.fp = new FlipPane[V.total];
        this.charStatus = this.V.CH_IDLE;
        this.tl_handle_card_anim = new Timeline(new KeyFrame(Duration.millis(FlipPane.DBL_ANIMATION_DURATION_MILLIS+100.0D), e->{
            if (charStatus == V.CH_FLIP_SECOND_ANIMATION) {
                if (this.V.cardValue[this.V.previousCardIndex] == this.V.cardValue[this.V.currentCardIndex]) {
                    final double DBL_REMOVE_ANIM_DURATION_MILLIS = 600.0D;
                    Timeline tl_remove_paired_cards = new Timeline(new KeyFrame(Duration.millis(DBL_REMOVE_ANIM_DURATION_MILLIS), new KeyValue(this.fp[this.V.previousCardIndex].scaleXProperty(),0.0D)),
                    new KeyFrame(Duration.millis(DBL_REMOVE_ANIM_DURATION_MILLIS), new KeyValue(this.fp[this.V.previousCardIndex].scaleYProperty(),0.0D)),
                    new KeyFrame(Duration.millis(DBL_REMOVE_ANIM_DURATION_MILLIS), new KeyValue(this.fp[this.V.currentCardIndex].scaleXProperty(),0.0D)),
                    new KeyFrame(Duration.millis(DBL_REMOVE_ANIM_DURATION_MILLIS), new KeyValue(this.fp[this.V.currentCardIndex].scaleYProperty(),0.0D)));
                    tl_remove_paired_cards.setOnFinished(eh->{
                        this.pane_base.getChildren().removeAll(this.fp[this.V.previousCardIndex],this.fp[this.V.currentCardIndex]);
                         blnMouseActionAllowed = true;
                        V.previousCardIndex = V.INT_NOT_DEFINED;
                        V.currentCardIndex = V.INT_NOT_DEFINED;
                    });
                    tl_remove_paired_cards.playFromStart();
                } else {
                    charStatus = V.CH_WRONG_FLIP_REVERSED;
                    fp[V.currentCardIndex].flip();
                    fp[V.previousCardIndex].flip();
                    this.tl_handle_card_anim.playFromStart();
                }
            } else if (charStatus == V.CH_WRONG_FLIP_REVERSED) {
                this.charStatus = this.V.CH_IDLE;
                blnMouseActionAllowed = true;
                V.previousCardIndex = V.INT_NOT_DEFINED;
                V.currentCardIndex = V.INT_NOT_DEFINED;
            }
        }));
        this.pane_base.setBackground(new Background(new BackgroundFill(Color.web("#0f0f0f"), CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(this.pane_base, DBL_WINDOW_WIDTH, DBL_WINDOW_HEIGHT);
        stage.setTitle("MeMorY GaMe");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();
        this.showSinglePlayerPlayGround();
    }
    private void showSinglePlayerPlayGround() {
        Rectangle rPlayArea = new Rectangle(Constants.DBL_PLAYAREA_X_LOC,Constants.DBL_PLAYAREA_Y_LOC,Constants.DBL_PLAYAREA_WIDTH,Constants.DBL_PLAYAREA_HEIGHT);
        rPlayArea.setFill(Color.web("#4E9AC2"));
        Rectangle rInfo = new Rectangle(Constants.DBL_INFOAREA_X_LOC,Constants.DBL_INFOAREA_Y_LOC,Constants.DBL_INFOAREA_WIDTH,Constants.DBL_INFOAREA_HEIGHT);
        rInfo.setFill(Color.web("#4EC29A"));
        Rectangle rBottom = new Rectangle(Constants.DBL_BOTTOMAREA_X_LOC,Constants.DBL_BOTTOMAREA_Y_LOC,Constants.DBL_BOTTOMAREA_WIDTH,Constants.DBL_BOTTOMAREA_HEIGHT);
        rBottom.setFill(Color.web("#FF8A0D"));
        lblTimeElapsed = new Text();
        Text txtTimeElapsed = new Text("TIME TAKEN");
        txtTimeElapsed.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 140));
        this.pane_base.getChildren().addAll(rPlayArea,rInfo,rBottom);
        final double dBL_X_START_LOC = 120.0D, DBL_Y_START_LOC = 100.0D;
        double dblXLoc = dBL_X_START_LOC, dblYLoc = DBL_Y_START_LOC;
        ArrayList<Integer> arr = new ArrayList<Integer>();
        for(int i = 0; i < 12; i++) {
            arr.add(i);
            arr.add(i);
        }
        Collections.shuffle(arr);
        Timeline tl_spread_out_cards = new Timeline();
        final double DBL_H_GAP = 30.0D, DBL_V_GAP = 10.0D, DBL_X_CENTER = dBL_X_START_LOC + ((this.DBL_CARD_WIDTH*this.V.columns)+(DBL_H_GAP*(this.V.columns-1)))/2.0D,
                DBL_Y_CENTER = DBL_Y_START_LOC + ((this.DBL_CARD_HEIGHT*this.V.rows)+(DBL_V_GAP*(this.V.rows-1)))/2.0D, DBL_DURATION_ANIM_MILLIS = 900.0D;
        for(int i = 0; i < V.total; i++) {
            final int INDEX = i;
            fp[i] = new FlipPane(this.RC.imgCardBackCover,this.RC.imgs[arr.get(INDEX)+14]);
            /*fp[i].setLayoutX(dblXLoc);
            fp[i].setLayoutY(dblYLoc);*/
            fp[i].setLayoutX(DBL_X_CENTER);
            fp[i].setLayoutY(DBL_Y_CENTER);
            tl_spread_out_cards.getKeyFrames().addAll(new KeyFrame(Duration.millis(DBL_DURATION_ANIM_MILLIS), new KeyValue(this.fp[INDEX].layoutXProperty(),dblXLoc)),
                        new KeyFrame(Duration.millis(DBL_DURATION_ANIM_MILLIS), new KeyValue(this.fp[INDEX].layoutYProperty(),dblYLoc)));
            fp[i].setPrefSize(DBL_CARD_WIDTH,DBL_CARD_HEIGHT);
            this.V.cardValue[INDEX] = arr.get(INDEX);
            fp[i].setOnMouseClicked(eh->{
                if (blnMouseActionAllowed) {
                    V.previousCardIndex = V.currentCardIndex;
                    V.currentCardIndex = INDEX;
                    //System.out.println("prev = " + V.previousCardIndex + "\ncurr = " + V.currentCardIndex + "\n------------");
                    if(V.previousCardIndex != V.INT_NOT_DEFINED && V.currentCardIndex != V.INT_NOT_DEFINED) {
                        if(V.previousCardIndex != V.currentCardIndex) {
                            this.blnMouseActionAllowed = false;
                            this.charStatus = V.CH_FLIP_SECOND_ANIMATION;
                            this.tl_handle_card_anim.playFromStart();
                        } else {
                            V.previousCardIndex = V.INT_NOT_DEFINED;
                            V.currentCardIndex = V.INT_NOT_DEFINED;
                        }
                    }
                    fp[INDEX].flip();
                }
            });
            if((i+1) % V.columns != 0) {
                dblXLoc += DBL_CARD_WIDTH + DBL_H_GAP;
            } else {
                dblXLoc = dBL_X_START_LOC;
                dblYLoc += DBL_CARD_HEIGHT + DBL_V_GAP;
            }
            this.pane_base.getChildren().add(fp[i]);
        }
        tl_spread_out_cards.setOnFinished(e->{
            Timeline t_flip = new Timeline();
            for (int i = 0; i < V.total; i++) {
                final int I = i;
                t_flip.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, fun -> {
                    this.fp[I].flip();
                }),
                new KeyFrame(Duration.millis(FlipPane.DBL_ANIMATION_DURATION_MILLIS * 3.5D), fun -> {
                   this.fp[I].flip();
                }));
                t_flip.setOnFinished(completed -> {

                });
            }
            t_flip.setOnFinished(eh -> {          
                blnMouseActionAllowed = true;
            });
            t_flip.playFromStart();
        });
        tl_spread_out_cards.setDelay(Duration.millis(1200.0D));
        tl_spread_out_cards.playFromStart();
    }
    private class Values {
        private final int INT_NOT_DEFINED = -1;
        private int rows, columns, total, previousCardIndex = this.INT_NOT_DEFINED, currentCardIndex = this.INT_NOT_DEFINED;
        private int[] cardValue;
        private final char CH_FLIP_SECOND_ANIMATION = 'C', CH_WRONG_FLIP_REVERSED = 'D', CH_IDLE = 'E';
        public final void initialize(final int row, final int col) {
            this.rows = row;
            this.columns = col;
            this.total = row*col;
            this.cardValue = new int[total];
        }
    }
    private final class Constants {
        private static final double DBL_SCREEN_WIDTH = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width,
                DBL_SCREEN_HEIGHT = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height,
                DBL_PLAYAREA_WIDTH = DBL_SCREEN_WIDTH*0.7D, DBL_PLAYAREA_HEIGHT = DBL_SCREEN_HEIGHT*0.7D,
                DBL_PLAYAREA_X_LOC = DBL_SCREEN_WIDTH*0.05D, DBL_PLAYAREA_Y_LOC = DBL_SCREEN_HEIGHT*0.1D,
                DBL_INFOAREA_WIDTH = DBL_SCREEN_WIDTH*0.20D, DBL_INFOAREA_HEIGHT = DBL_SCREEN_HEIGHT*0.7D,
                DBL_INFOAREA_X_LOC = DBL_SCREEN_WIDTH*0.77D, DBL_INFOAREA_Y_LOC = DBL_SCREEN_HEIGHT*0.1D,
                DBL_BOTTOMAREA_WIDTH = DBL_SCREEN_WIDTH*0.92D, DBL_BOTTOMAREA_HEIGHT = DBL_SCREEN_HEIGHT*0.12D,
                DBL_BOTTOMAREA_X_LOC = DBL_SCREEN_WIDTH*0.05D, DBL_BOTTOMAREA_Y_LOC = DBL_SCREEN_HEIGHT*0.83D;
    }
    private class FlipPane extends StackPane {

        private final StackPane spFront, spBack;
        private SimpleDoubleProperty angleProperty = new SimpleDoubleProperty(Math.PI / 2.0D);
        private PerspectiveTransform transformProperty = new PerspectiveTransform();
        private SimpleBooleanProperty flippedProperty = new SimpleBooleanProperty(true);
        private Timeline tl_flip;
        private static double DBL_ANIMATION_DURATION_MILLIS = 400.0D;

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
    private final class ResourceCenter {
        private final Image[] imgs;
        private final Image imgCardBackCover;
        public ResourceCenter() {
            this.imgCardBackCover = new Image(Cards.class.getResource("/res/memory/c3.png").toString(), DBL_CARD_WIDTH, DBL_CARD_HEIGHT, true, true);
            imgs = new Image[28];
            for(int i = 0; i < 28; i++) {
                imgs[i] = new Image(Cards.class.getResource("/res/memory/" + i + ".png").toString(), DBL_CARD_WIDTH, DBL_CARD_HEIGHT, true, true);
            }
        }
    }
}
