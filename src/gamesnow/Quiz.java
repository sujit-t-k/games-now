/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gamesnow;

import java.util.ArrayList;
import java.util.Collections;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
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
public class Quiz extends Application {
    private Pane pane_content = null;
    private ArrayList<QA> technical_questions = this.assignTechnicalQuestions();
    private ArrayList<QA> non_technical_questions = this.assignNonTechnicalQuestions();//this.assignFunQuestions();//this.assignNonTechnicalQuestions();
    private ProgressBar pb = null;
    private int intScore = 0;
    private final ResourcesHub RH = new ResourcesHub();
    private final boolean BLNPLAYSFX = true;
    
    @Override
    public void start(Stage primaryStage) {
        this.pane_content = new Pane();//#9F0F4A
        this.pane_content.setBackground(new Background(new BackgroundFill(Color.web("#0f0f0f"), CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(this.pane_content, 1100, 530);
        //primaryStage.setMaximized(true);
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("QUIZ!!!");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        //displayQuizDetails();
        //displayQA();
        //showTechnicalQuiz();
        this.buildQuestionPane();
    }
    
    private void showResult() {
        Text lblQNo = new Text("Q. No.");
        lblQNo.setLayoutX(100.0D+15.0D);
        lblQNo.setLayoutY(85.0D);
        lblQNo.setFill(Color.web("#ffffff"));
        Text lblStatus = new Text("Status");
        lblStatus.setLayoutX(200.0D);
        lblStatus.setLayoutY(85.0D);
        lblStatus.setFill(Color.web("#ffffff"));
        Text lblDuration= new Text("Time taken");
        lblDuration.setLayoutX(290.0D);
        lblDuration.setLayoutY(85.0D);
        lblDuration.setFill(Color.web("#ffffff"));
        double yLoc = 120.0D;
        Line l_top = new Line(90.0D,60.0D,365.0D,60.0D), l_bottom = new Line(90.0D,500.0D,365.0D,500.0D), l_left = new Line(90.0D,60.0D,90.0D,500.0D), 
                l_right = new Line(365.0D,60.0D,365.0D,500.0D), l_sep1 = new Line(185.0D,60.0D,185.0D,500.0D), l_sep2 = new Line(265.0D,60.0D,265.0D,500.0D);
        l_top.setStroke(Color.AQUA); l_bottom.setStroke(Color.AQUA); l_left.setStroke(Color.AQUA); l_right.setStroke(Color.AQUA); l_sep1.setStroke(Color.BURLYWOOD); l_sep2.setStroke(Color.BURLYWOOD);
        Timeline t_result = new Timeline();
        double dblTimeDiffer = 0.0D;
        for(int i = 0; i < 10; i++) {
            Line l = new Line(90.0D,yLoc-25.0D,365.0D,yLoc-25.0D);
            l.setStroke(Color.BROWN);
            Text txtQuestionNo = new Text((i+1)+"");
            txtQuestionNo.setLayoutX(110.0D+15.0D);
            txtQuestionNo.setLayoutY(yLoc);
            txtQuestionNo.setFill(Color.web("#ffffff"));
            txtQuestionNo.setStrokeWidth(1.0D);
            ImageView imgViewStatus = new ImageView((this.dblTimeTaken[i] > 0.0D) ? this.RH.imgCorrect : (this.dblTimeTaken[i] == 0.0D) ? this.RH.imgTimeOut : this.RH.imgWrong);
            imgViewStatus.setX(205.0D);
            imgViewStatus.setY(yLoc-17.0D);
            imgViewStatus.setScaleX(0.3D);
            imgViewStatus.setScaleY(0.3D);
            t_result.getKeyFrames().addAll(new KeyFrame(Duration.millis(700.0D+dblTimeDiffer), new KeyValue(imgViewStatus.scaleXProperty(),1.3D)),
                                           new KeyFrame(Duration.millis(700.0D+dblTimeDiffer), new KeyValue(imgViewStatus.scaleYProperty(),1.3D)),
                                           new KeyFrame(Duration.millis(1000.0D+dblTimeDiffer), new KeyValue(imgViewStatus.scaleXProperty(),1.0D)),
                                           new KeyFrame(Duration.millis(1000.0D+dblTimeDiffer), new KeyValue(imgViewStatus.scaleYProperty(),1.0D)));
            Text txtTimeTaken = new Text((this.dblTimeTaken[i] < 0.0D) ? String.format("%.2f",(-this.dblTimeTaken[i]/1000.0D)) + "s" : ((this.dblTimeTaken[i] > 0.0D)) ? String.format("%.2f",(this.dblTimeTaken[i]/1000.0D)) + "s" : "-");
            txtTimeTaken.setLayoutX(315.0D);
            txtTimeTaken.setLayoutY(yLoc);
            txtTimeTaken.setFill(Color.web("#ffffff"));
            this.pane_content.getChildren().addAll(txtQuestionNo,txtTimeTaken,imgViewStatus,l);
            yLoc += 40.0D;
            dblTimeDiffer += 150.0D;
        }
        Line l_top_result = new Line(450.0D,100.0D,850.0D,100.0D), l_bottom_result = new Line(450.0D,300.0D,850.0D,300.0D), l_left_result = new Line(450.0D,100.0D,450.0D,300.0D), l_right_result = new Line(850.0D,100.0D,850.0D,300.0D);
        l_top_result.setStroke(Color.GOLD); l_bottom_result.setStroke(Color.GOLD); l_left_result.setStroke(Color.GOLD); l_right_result.setStroke(Color.GOLD);
        Text txtNoOfAttempts = new Text("No. of Questions Attempted :");
        txtNoOfAttempts.setLayoutX(460.0D);
        txtNoOfAttempts.setLayoutY(140.0D);
        txtNoOfAttempts.setFill(Color.BLUEVIOLET);
        txtNoOfAttempts.setTextAlignment(TextAlignment.CENTER);
        txtNoOfAttempts.setWrappingWidth(133.0D);
        txtNoOfAttempts.setFont(Font.font("Arial Black", FontWeight.BOLD, FontPosture.REGULAR, 20));
        Text txtNoOfCorrect = new Text("No. of Correct Answers :");
        txtNoOfCorrect.setLayoutX(596.0D);
        txtNoOfCorrect.setLayoutY(140.0D);
        txtNoOfCorrect.setFill(Color.BLUEVIOLET);
        txtNoOfCorrect.setTextAlignment(TextAlignment.CENTER);
        txtNoOfCorrect.setWrappingWidth(133.0D);
        txtNoOfCorrect.setFont(Font.font("Arial Black", FontWeight.BOLD, FontPosture.REGULAR, 20));
        Text txtNoOfWrong = new Text("No. of Wrong Answers :");
        txtNoOfWrong.setLayoutX(720.0D);
        txtNoOfWrong.setLayoutY(140.0D);
        txtNoOfWrong.setFill(Color.BLUEVIOLET);
        txtNoOfWrong.setTextAlignment(TextAlignment.CENTER);
        txtNoOfWrong.setWrappingWidth(133.0D);
        txtNoOfWrong.setFont(Font.font("Arial Black", FontWeight.BOLD, FontPosture.REGULAR, 20));
        Text txtOutOfTen = new Text("/10");
        txtOutOfTen.setLayoutX(485.0D);
        txtOutOfTen.setLayoutY(280.0D);
        txtOutOfTen.setFill(Color.BLUEVIOLET);
        txtOutOfTen.setTextAlignment(TextAlignment.CENTER);
        txtOutOfTen.setWrappingWidth(133.0D);
        txtOutOfTen.setFont(Font.font("Arial Black", FontWeight.BOLD, FontPosture.REGULAR, 20));
        Line l_div_1 = new Line(600.0D,100.0D,600.0D,300.0D), l_div_2 = new Line(724.0D,100.0D,724.0D,300.0D), l_div_3 = new Line(450.0D,220.0D,850.0D,220.0D);
        l_div_1.setStroke(Color.GOLD); l_div_2.setStroke(Color.GOLD); l_div_3.setStroke(Color.GOLD);
        NumericalLabel lblTotalQuestionsAttempted = new NumericalLabel();
        lblTotalQuestionsAttempted.setFont(Font.font("Arial Narrow", FontWeight.LIGHT, FontPosture.REGULAR, 46.0D));
        lblTotalQuestionsAttempted.setLayoutX(485.0D);
        lblTotalQuestionsAttempted.setLayoutY(236.0D);
        lblTotalQuestionsAttempted.setText("-");
        lblTotalQuestionsAttempted.setScaleX(1.2D);
        lblTotalQuestionsAttempted.setTextFill(Color.web("#26FCF8"));
        NumericalLabel lblCorrectAnswersNumber = new NumericalLabel();
        lblCorrectAnswersNumber.setFont(Font.font("Arial Narrow", FontWeight.LIGHT, FontPosture.REGULAR, 46.0D));
        lblCorrectAnswersNumber.setLayoutX(640.0D);
        lblCorrectAnswersNumber.setLayoutY(236.0D);
        lblCorrectAnswersNumber.setText("-");
        lblCorrectAnswersNumber.setScaleX(1.2D);
        lblCorrectAnswersNumber.setTextFill(Color.web("#B5E61D"));
        NumericalLabel lblWrongAnswersNumber = new NumericalLabel();
        lblWrongAnswersNumber.setFont(Font.font("Arial Narrow", FontWeight.LIGHT, FontPosture.REGULAR, 46.0D));
        lblWrongAnswersNumber.setLayoutX(766.0D);
        lblWrongAnswersNumber.setLayoutY(236.0D);
        lblWrongAnswersNumber.setText("-");
        lblWrongAnswersNumber.setScaleX(1.2D);
        lblWrongAnswersNumber.setTextFill(Color.web("#ED1C24"));
        Line l_top_time = new Line(870.0D,100.0D,1070.0D,100.0D), l_bottom_time = new Line(870.0D,300.0D,1070.0D,300.0D), l_left_time = new Line(870.0D,100.0D,870.0D,300.0D), l_right_time = new Line(1070.0D,100.0D,1070.0D,300.0D);
        l_top_time.setStroke(Color.DARKMAGENTA); l_bottom_time.setStroke(Color.DARKMAGENTA); l_left_time.setStroke(Color.DARKMAGENTA); l_right_time.setStroke(Color.DARKMAGENTA);
        Text txtTimeTaken = new Text("Time taken :\n\n\n\n\nseconds");
        txtTimeTaken.setLayoutX(870.0D);
        txtTimeTaken.setLayoutY(140.0D);
        txtTimeTaken.setFill(Color.SILVER);
        txtTimeTaken.setTextAlignment(TextAlignment.CENTER);
        txtTimeTaken.setWrappingWidth(200.0D);
        txtTimeTaken.setFont(Font.font("Arial Black", FontWeight.BOLD, FontPosture.REGULAR, 20));
        NumericalLabel lblTimeTaken = new NumericalLabel();
        lblTimeTaken.setFont(Font.font("Arial Narrow", FontWeight.LIGHT, FontPosture.REGULAR, 52.0D));
        lblTimeTaken.setLayoutX(886.0D);
        lblTimeTaken.setLayoutY(170.0D);
        lblTimeTaken.setScaleX(1.2D);
        lblTimeTaken.setPrefWidth(200.0D/1.2D);
        //lblTimeTaken.setBackground(new Background(new BackgroundFill(Color.web("#493732"), CornerRadii.EMPTY, Insets.EMPTY)));
        lblTimeTaken.setAlignment(Pos.CENTER);
        lblTimeTaken.setText("-");
        lblTimeTaken.setTextFill(Color.YELLOW);
        Text txtScore = new Text("YOUR\nSCORE");
        txtScore.setLayoutX(450.0D);
        txtScore.setLayoutY(380.0D);
        txtScore.setFill(Color.web("#3F8A0F"));
        txtScore.setTextAlignment(TextAlignment.CENTER);
        txtScore.setWrappingWidth(250.0D);
        txtScore.setFont(Font.font("Arial Black", FontWeight.BOLD, FontPosture.REGULAR, 50));
        NumericalLabel lblScore = new NumericalLabel();
        lblScore.setFont(Font.font("Arial Narrow", FontWeight.LIGHT, FontPosture.REGULAR, 66.0D));
        lblScore.setLayoutX(716.0D);
        lblScore.setLayoutY(370.0D);
        lblScore.setScaleX(1.2D);
        lblScore.setText("-");
        lblScore.setTextFill(Color.web("#3F8A0F"));
        t_result.setOnFinished(eh->{
            short total_attempted = 0, correct_answers = 0, wrong_answers = 0;
            double total_time_taken = 0.0D;
            for(double d : this.dblTimeTaken) {
                if(d!=0) {
                    ++total_attempted;
                    if(d>0.0D) {
                        total_time_taken += d;
                        ++correct_answers;
                    } else {
                        total_time_taken -= d;
                        wrong_answers++;
                    }                    
                }
            }
            lblTotalQuestionsAttempted.setValue(total_attempted, 800.0D, 0);
            lblCorrectAnswersNumber.setValue(correct_answers, 800.0D, 0);
            lblWrongAnswersNumber.setValue(wrong_answers, 800.0D, 0);
            final double DBL_TIME_TAKEN = total_time_taken;
            lblWrongAnswersNumber.t.setOnFinished(e->{
                lblTimeTaken.t.setDelay(Duration.millis(600.0D));
                lblTimeTaken.setValue(DBL_TIME_TAKEN/1000.0D, 1600.0D, 2);
                lblTimeTaken.t.setOnFinished(ehs->{
                    lblScore.t.setDelay(Duration.millis(600.0D));
                    lblScore.setValue((this.dbl_score*this.DBL_SCORE_MULTIPLIER), 1600.0D, 2);
                });
            });
        });
        this.pane_content.getChildren().addAll(lblQNo,lblStatus,lblDuration,l_top,l_bottom,l_left,l_right,l_sep1,l_sep2,l_top_result,l_bottom_result,l_left_result,l_right_result,
                 txtNoOfAttempts,txtNoOfCorrect,txtNoOfWrong,txtOutOfTen,l_div_1,l_div_2,l_div_3,lblTotalQuestionsAttempted,lblCorrectAnswersNumber,lblWrongAnswersNumber,l_top_time,
                 l_bottom_time,l_left_time,l_right_time,txtTimeTaken,lblTimeTaken,txtScore,lblScore);
        t_result.playFromStart();
    }
    
    private final Button getButton(final String bgColorButton, final String dropShadowColor, final Double dblXLocation, final double dblYLocation) {        
        Rectangle rect = new Rectangle(0.0D,0.0D,400.0D,80.0D);
        rect.setArcHeight(40.0D);
        rect.setArcWidth(40.0D);
        Button btn = new Button();
        btn.setWrapText(true);
        btn.setMinSize(400.0D, 80.0D);
        btn.setMaxSize(400.0D, 80.0D);
        btn.setPrefSize(400.0D, 80.0D);
        btn.setLayoutX(dblXLocation);
        btn.setLayoutY(dblYLocation);
        btn.setShape(rect);
        DropShadow ds = new DropShadow();
        ds.setOffsetX(8.0D);
        ds.setOffsetY(8.0D);
        ds.setColor(Color.web(dropShadowColor));
        ds.setBlurType(BlurType.GAUSSIAN);
        btn.setBackground(new Background(new BackgroundFill(Color.web(bgColorButton), CornerRadii.EMPTY, Insets.EMPTY)));
        btn.setFont(Font.font("Arial Black", FontWeight.BOLD, FontPosture.REGULAR, 26));
        btn.addEventHandler(MouseEvent.MOUSE_ENTERED, eh-> {
            btn.setFont(Font.font("Arial Black", FontWeight.BOLD, FontPosture.REGULAR, 32));
            btn.setEffect(ds);
        });
        btn.addEventHandler(MouseEvent.MOUSE_EXITED, eh-> {
            btn.setFont(Font.font("Arial Black", FontWeight.BOLD, FontPosture.REGULAR, 26));
            btn.setEffect(null);
        });
        return btn;
    }
    private final String[] strBtnBackgrounds = new String[]{"#aa42ff","#aa42ff","#aa42ff","#aa42ff"};//{"#09ebe7","#eb9409","#ff42bd","#aa42ff"};
    private Button[] btnChoice;
    private double[] dblTimeTaken = new double[10];
    private Text txtQuestion,txtTitle;
    private int q_number = 0;
    private double dbl_score = 0.0D;
    private final double DBL_HALF_TRANSITION_TIME_MILLIS = 1500.0D, DBL_SCORE_MULTIPLIER = 1.0D/96.0D;
    private ArrayList<Integer> arrChoiceIndices = new ArrayList<Integer>();
    private void showNextQuestion() {
        pb.tl_count_down.stop();
        ++q_number;
        Collections.shuffle(arrChoiceIndices);
        Timeline tlQuestionTransition = new Timeline();
        for(int i = 0; i < 4; i++) {
            final int INDEX = i;
            btnChoice[INDEX].setDisable(true);
            btnChoice[INDEX].setText((q_number<6) ? technical_questions.get(q_number-1).strChoices[arrChoiceIndices.get(INDEX)] : non_technical_questions.get(q_number-6).strChoices[arrChoiceIndices.get(INDEX)]);
            tlQuestionTransition.getKeyFrames().add(new KeyFrame(Duration.millis(DBL_HALF_TRANSITION_TIME_MILLIS), new KeyValue(btnChoice[INDEX].opacityProperty(),1.0D)));
            btnChoice[INDEX].setOnMouseClicked(eh->{
                this.dblTimeTaken[q_number-1] = pb.tl_count_down.getCurrentTime().toMillis();
                pb.tl_count_down.stop();
                if((q_number < 6) ? btnChoice[INDEX].getText().equals(technical_questions.get(q_number-1).answer_key) : btnChoice[INDEX].getText().equals(non_technical_questions.get(q_number-6).answer_key)) {
                    this.dbl_score += 30000.0D - this.dblTimeTaken[q_number-1];
                    if(BLNPLAYSFX) RH.playSuccessSound();
                    btnChoice[INDEX].setBackground(new Background(new BackgroundFill(Color.web("#B2FF2A"), CornerRadii.EMPTY, Insets.EMPTY)));
                    for(int j = 0; j < 4; j++) {
                        btnChoice[j].setDisable(true);
                        if(INDEX!=j) {
                            btnChoice[j].setBackground(new Background(new BackgroundFill(Color.web("#C40900"), CornerRadii.EMPTY, Insets.EMPTY)));
                        }
                    }
                } else {
                    this.dblTimeTaken[q_number-1] *= -1.0D;
                    if(BLNPLAYSFX) RH.playFailureSound();
                    for(int j = 0; j < 4; j++) {
                        btnChoice[j].setDisable(true);
                        btnChoice[j].setBackground(new Background(new BackgroundFill(Color.web((btnChoice[j].getText().equals(q_number<6 ? technical_questions.get(q_number-1).answer_key : non_technical_questions.get(q_number-6).answer_key)) ? "#B2FF2A" : "#C40900"), CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                    //btnChoice[INDEX].setBackground(new Background(new BackgroundFill(Color.web("#FFD016"), CornerRadii.EMPTY, Insets.EMPTY)));
                }
                fadeCurrentQuestion();
            });
        }
        pb.resetTimer();
        tlQuestionTransition.getKeyFrames().addAll(new KeyFrame(Duration.millis(DBL_HALF_TRANSITION_TIME_MILLIS), new KeyValue(txtQuestion.opacityProperty(),1.0D)),
                                                   new KeyFrame(Duration.millis(DBL_HALF_TRANSITION_TIME_MILLIS), new KeyValue(pb.opacityProperty(),1.0D)));
        txtQuestion.setText(q_number + ". " + ((q_number < 6) ? technical_questions.get(q_number-1).strQuestion : non_technical_questions.get(q_number-6).strQuestion));
        tlQuestionTransition.setOnFinished(eh->{
            for(int i = 0; i < 4; i++) btnChoice[i].setDisable(false);
            pb.startCountDown();
        });
        pb.tl_count_down.setOnFinished(eh->{
            //if(BLNPLAYSFX) To play timeup soundfx
            for(int j = 0; j < 4; j++) {
                btnChoice[j].setDisable(true);
                btnChoice[j].setBackground(new Background(new BackgroundFill(Color.web((btnChoice[j].getText().equals(q_number<6 ? technical_questions.get(q_number-1).answer_key : non_technical_questions.get(q_number-6).answer_key)) ? "#B2FF2A" : "#C40900"), CornerRadii.EMPTY, Insets.EMPTY)));
            }
            fadeCurrentQuestion();
        });
        tlQuestionTransition.playFromStart();
    }
    private void fadeCurrentQuestion() {
        Timeline tlQuestionFade = new Timeline(new KeyFrame(Duration.millis(DBL_HALF_TRANSITION_TIME_MILLIS), new KeyValue(txtQuestion.opacityProperty(),0.0D)),
                                               new KeyFrame(Duration.millis(DBL_HALF_TRANSITION_TIME_MILLIS), new KeyValue(pb.opacityProperty(),0.0D)));
        for(int i = 0; i < 4; i++) tlQuestionFade.getKeyFrames().add(new KeyFrame(Duration.millis(DBL_HALF_TRANSITION_TIME_MILLIS), new KeyValue(btnChoice[i].opacityProperty(),0.0D)));
        tlQuestionFade.setOnFinished(eh->{
            if(q_number==5) txtTitle.setText("PART - II : Non-Technical Quiz Round");
            if(q_number<10) {
                for(int i = 0; i < 4; i++) btnChoice[i].setBackground(new Background(new BackgroundFill(Color.web(strBtnBackgrounds[i]), CornerRadii.EMPTY, Insets.EMPTY)));
                showNextQuestion();
            } else {
                Timeline tl_show_result = new Timeline(new KeyFrame(Duration.millis(DBL_HALF_TRANSITION_TIME_MILLIS), new KeyValue(txtTitle.opacityProperty(),0.0D)));
                tl_show_result.setOnFinished(e->{
                    pane_content.getChildren().removeAll(txtTitle,pb,txtQuestion,btnChoice[0],btnChoice[1],btnChoice[2],btnChoice[3]);
                    showResult();
                });
                tl_show_result.playFromStart();
            }
        });
        tlQuestionFade.playFromStart();
    }
    private void buildQuestionPane() {
        txtTitle = new Text("PART - I : Technical Quiz Round");
        txtTitle.setFont(Font.font("Arial Black", FontWeight.BOLD, FontPosture.REGULAR, 32));
        txtTitle.setUnderline(true);
        txtTitle.setFill(Color.web("#B9E30D"));
        txtTitle.setLayoutX(34.0D);
        txtTitle.setLayoutY(34.0D);        
        pb = new ProgressBar(40.0D,400.0D,new Background(new BackgroundFill(Color.web("#26eb0c"), CornerRadii.EMPTY, Insets.EMPTY)),Color.web("#08085e"));
        pb.setLayoutX(1000.0D);
        pb.setLayoutY(40.0D);
        txtQuestion = new Text();
        txtQuestion.setFont(Font.font("Arial Black", FontWeight.BOLD, FontPosture.REGULAR, 32));
        txtQuestion.setFill(Color.web("#FF9911"));
        txtQuestion.setLayoutX(46.0D);
        txtQuestion.setLayoutY(120.0D);
        txtQuestion.setWrappingWidth(900.0D);
        btnChoice = new Button[4];
        btnChoice[0] = getButton(strBtnBackgrounds[0],"#080d70",30.0D,240.0D);
        btnChoice[1] = getButton(strBtnBackgrounds[1],"#700a08",450.0D,240.0D);
        btnChoice[2] = getButton(strBtnBackgrounds[2],"#70033f",30.0D,340.0D);        
        btnChoice[3] = getButton(strBtnBackgrounds[3],"#510370",450.0D,340.0D);
        Collections.shuffle(technical_questions);
        Collections.shuffle(non_technical_questions);
        for(int i = 0; i < 4; i++) arrChoiceIndices.add(i);
        pane_content.getChildren().addAll(txtTitle,pb,txtQuestion,btnChoice[0],btnChoice[1],btnChoice[2],btnChoice[3]);
        showNextQuestion();
    }
            
    private class ProgressBar extends Pane {
        private final double WIDTH, HEIGHT;
        private Rectangle rectEmpty;
        private Timeline tl_count_down = new Timeline();
        public ProgressBar(final double WIDTH, final double HEIGHT, final Background progress, final Paint background) {
            this.WIDTH = WIDTH;
            this.HEIGHT = HEIGHT-4.0D;
            this.setMaxSize(WIDTH, HEIGHT);
            this.setMinSize(WIDTH, HEIGHT);
            this.setPrefSize(WIDTH, HEIGHT);
            this.setBackground(progress);
            rectEmpty = new Rectangle(2.0D,2.0D,WIDTH-4.0D,0.0D);
            rectEmpty.setFill(background);
            this.getChildren().add(rectEmpty);
        }
        public void resetTimer() {
            rectEmpty.setHeight(0.0D);
            this.setBackground(new Background(new BackgroundFill(Color.web("#26eb0c"), CornerRadii.EMPTY, Insets.EMPTY)));
        }
        public void startCountDown() {
            tl_count_down.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(rectEmpty.heightProperty(),0.0D)),
                    new KeyFrame(Duration.ZERO, new KeyValue(this.backgroundProperty(),new Background(new BackgroundFill(Color.web("#26eb0c"), CornerRadii.EMPTY, Insets.EMPTY)))),
                    new KeyFrame(Duration.seconds(12.0D), new KeyValue(this.backgroundProperty(),new Background(new BackgroundFill(Color.web("#f2ea07"), CornerRadii.EMPTY, Insets.EMPTY)))),
                    new KeyFrame(Duration.seconds(25.0D), new KeyValue(this.backgroundProperty(),new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)))),
                    new KeyFrame(Duration.seconds(30.0D), new KeyValue(rectEmpty.heightProperty(),HEIGHT)));
            tl_count_down.play();
        }
    }
    
    private void displayQuizDetails() {
        Pane paneTechnicalDetails = new Pane();
        paneTechnicalDetails.setMinSize(300.0D, 300.0D);
        paneTechnicalDetails.setMaxSize(300.0D, 300.0D);
        paneTechnicalDetails.setPrefSize(300.0D, 300.0D);
        paneTechnicalDetails.setBackground(new Background(new BackgroundFill(Color.web("#B5E61D"), CornerRadii.EMPTY, Insets.EMPTY)));
        paneTechnicalDetails.setLayoutX(225.0D);
        paneTechnicalDetails.setLayoutY(100.0D);
        Rectangle rectBorder = new Rectangle(0.0D,0.0D,300.0d,300.0D);
        rectBorder.setFill(Color.TRANSPARENT);
        rectBorder.setStroke(Color.BLACK);
        rectBorder.setStrokeWidth(4.0D);
        Text t_parta = new Text("PART - A");
        t_parta.setFont(Font.font("Century Gothic", FontWeight.BOLD, FontPosture.REGULAR, 24));
        t_parta.setLayoutX(110.0D);
        t_parta.setLayoutY(30.0D);
        Text t_tr = new Text("Technical Round");
        t_tr.setFont(Font.font("Century Gothic", FontWeight.BOLD, FontPosture.REGULAR, 20));
        t_tr.setFill(Color.web("#198338"));
        t_tr.setLayoutX(70.0D);
        t_tr.setLayoutY(70.0D);
        Text t_nq = new Text("Number of questions : 5");
        t_nq.setFont(Font.font("Century Gothic", FontWeight.BOLD, FontPosture.REGULAR, 22));
        t_nq.setFill(Color.web("#880015"));
        t_nq.setLayoutX(20.0D);
        t_nq.setLayoutY(120.0D);
        Text t = new Text("Duration : 2min 30 sec");
        t.setFont(Font.font("Century Gothic", FontWeight.BOLD, FontPosture.REGULAR, 22));
        t.setLayoutX(36.0D);
        t.setLayoutY(160.0D);
        Text ts = new Text("(30 sec for each question)");
        ts.setFont(Font.font("Century Gothic", FontWeight.BOLD, FontPosture.REGULAR, 16));
        ts.setLayoutX(46.0D);
        ts.setLayoutY(180.0D);
        Text tscore = new Text("TOTAL SCORE : 15");
        tscore.setFont(Font.font("Century Gothic", FontWeight.BOLD, FontPosture.REGULAR, 22));
        tscore.setLayoutX(54.0D);
        tscore.setLayoutY(220.0D);
        paneTechnicalDetails.getChildren().addAll(t_parta,t_tr,rectBorder,t_nq,t,ts,tscore);
        Pane paneNonTechnicalDetails = new Pane();
        paneNonTechnicalDetails.setMinSize(300.0D, 300.0D);
        paneNonTechnicalDetails.setMaxSize(300.0D, 300.0D);
        paneNonTechnicalDetails.setPrefSize(300.0D, 300.0D);
        paneNonTechnicalDetails.setBackground(new Background(new BackgroundFill(Color.web("#00A2E8"), CornerRadii.EMPTY, Insets.EMPTY)));
        paneNonTechnicalDetails.setLayoutX(555.0D);
        paneNonTechnicalDetails.setLayoutY(100.0D);
        Rectangle rectBorder2 = new Rectangle(0.0D,0.0D,300.0d,300.0D);
        rectBorder2.setFill(Color.TRANSPARENT);
        rectBorder2.setStroke(Color.BLACK);
        rectBorder2.setStrokeWidth(4.0D);
        Text t_parta2 = new Text("PART - B");
        t_parta2.setFont(Font.font("Century Gothic", FontWeight.BOLD, FontPosture.REGULAR, 24));
        t_parta2.setLayoutX(110.0D);
        t_parta2.setLayoutY(30.0D);
        Text t_tr2 = new Text("Non Technical Round");
        t_tr2.setFont(Font.font("Century Gothic", FontWeight.BOLD, FontPosture.REGULAR, 20));
        t_tr2.setFill(Color.web("#198338"));
        t_tr2.setLayoutX(70.0D);
        t_tr2.setLayoutY(70.0D);
        Text t_nq2 = new Text("Number of questions : 5");
        t_nq2.setFont(Font.font("Century Gothic", FontWeight.BOLD, FontPosture.REGULAR, 22));
        t_nq2.setFill(Color.web("#880015"));
        t_nq2.setLayoutX(30.0D);
        t_nq2.setLayoutY(120.0D);
        Text t2 = new Text("Duration : 1min");
        t2.setFont(Font.font("Century Gothic", FontWeight.BOLD, FontPosture.REGULAR, 22));
        t2.setLayoutX(36.0D);
        t2.setLayoutY(160.0D);
        Text ts2 = new Text("(12 sec for each question)");
        ts2.setFont(Font.font("Century Gothic", FontWeight.BOLD, FontPosture.REGULAR, 16));
        ts2.setLayoutX(46.0D);
        ts2.setLayoutY(180.0D);
        Text tscore2 = new Text("TOTAL SCORE : 10");
        tscore2.setFont(Font.font("Century Gothic", FontWeight.BOLD, FontPosture.REGULAR, 22));
        tscore2.setLayoutX(54.0D);
        tscore2.setLayoutY(220.0D);
        paneNonTechnicalDetails.getChildren().addAll(t_parta2,t_tr2,rectBorder2,t_nq2,t2,ts2,tscore2);
        Rectangle rectTakeQuiz = new Rectangle(0.0D,0.0D,100.0D,50.0D);
        rectTakeQuiz.setArcWidth(10.0D);
        rectTakeQuiz.setArcHeight(10.0D);
        rectTakeQuiz.setStrokeWidth(4.0D);
        rectTakeQuiz.setLayoutX(500.0D);
        rectTakeQuiz.setLayoutY(440.0D);
        rectTakeQuiz.setStroke(Color.BLACK);
        rectTakeQuiz.setStrokeWidth(2.0D);
        rectTakeQuiz.setFill(Color.web("#A81AE0"));
        Text txtTakeQuiz = new Text("TAKE QUIZ");
        txtTakeQuiz.setFont(Font.font("Century Gothic", FontWeight.BOLD, FontPosture.REGULAR, 16));
        txtTakeQuiz.setLayoutX(510.0D);
        txtTakeQuiz.setLayoutY(470.0D);
        txtTakeQuiz.setFill(Color.web("#000000"));
        txtTakeQuiz.setMouseTransparent(true);
        rectTakeQuiz.setOnMouseEntered(e->{
            rectTakeQuiz.setFill(Color.web("#2EE024"));
            txtTakeQuiz.setFill(Color.web("#8A1607"));
        });
        rectTakeQuiz.setOnMouseExited(e->{
            rectTakeQuiz.setFill(Color.web("#A81AE0"));
            txtTakeQuiz.setFill(Color.web("#000000"));
        });
        rectTakeQuiz.setId("NOT CLICKED");
        rectTakeQuiz.setOnMouseClicked(e-> {
            if(rectTakeQuiz.getId().equals("NOT CLICKED")) {
                rectTakeQuiz.setId("CLICKED");
                Timeline tl = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(paneTechnicalDetails.layoutYProperty(),paneTechnicalDetails.getLayoutY())),
                                           new KeyFrame(Duration.millis(340.0D), new KeyValue(paneTechnicalDetails.layoutYProperty(),paneTechnicalDetails.getLayoutY() + 20.0D)),
                                           new KeyFrame(Duration.millis(1200.0D), new KeyValue(paneTechnicalDetails.layoutYProperty(),-paneTechnicalDetails.getHeight())),
                                           new KeyFrame(Duration.ZERO, new KeyValue(paneNonTechnicalDetails.layoutYProperty(),paneNonTechnicalDetails.getLayoutY())),
                                           new KeyFrame(Duration.millis(340.0D), new KeyValue(paneNonTechnicalDetails.layoutYProperty(),paneNonTechnicalDetails.getLayoutY() + 20.0D)),
                                           new KeyFrame(Duration.millis(1200.0D), new KeyValue(paneNonTechnicalDetails.layoutYProperty(),-paneNonTechnicalDetails.getHeight())),
                                           new KeyFrame(Duration.ZERO, new KeyValue(rectTakeQuiz.layoutYProperty(),rectTakeQuiz.getLayoutY())),
                                           new KeyFrame(Duration.millis(340.0D), new KeyValue(rectTakeQuiz.layoutYProperty(),rectTakeQuiz.getLayoutY() - 20.0D)),
                                           new KeyFrame(Duration.millis(1200.0D), new KeyValue(rectTakeQuiz.layoutYProperty(),rectTakeQuiz.getLayoutY() + 80.0D)),
                                           new KeyFrame(Duration.ZERO, new KeyValue(txtTakeQuiz.layoutYProperty(),txtTakeQuiz.getLayoutY())),
                                           new KeyFrame(Duration.millis(340.0D), new KeyValue(txtTakeQuiz.layoutYProperty(),txtTakeQuiz.getLayoutY() - 20.0D)),
                                           new KeyFrame(Duration.millis(1200.0D), new KeyValue(txtTakeQuiz.layoutYProperty(),txtTakeQuiz.getLayoutY() + 80.0D)));
                tl.play();
                tl.setOnFinished(eh->{
                    pane_content.getChildren().removeAll(paneTechnicalDetails,paneNonTechnicalDetails,rectTakeQuiz,txtTakeQuiz);
                });
            }
        });
        pane_content.getChildren().addAll(paneTechnicalDetails,paneNonTechnicalDetails,rectTakeQuiz,txtTakeQuiz);
    }
    
    private void displayQA() {
        for(QA q : technical_questions) {
            System.out.println("Q : " + q.strQuestion);
            for(int i = 0; i < 4; i++) {
                System.out.println(((char) (i+65)) + ") " + q.strChoices[i]);
            }
            System.out.println("Correct Answer = " + q.answer_key);
            System.out.println("-----------------------------------------");
        }
        for(QA q : non_technical_questions) {
            System.out.println("Q : " + q.strQuestion);
            for(int i = 0; i < 4; i++) {
                System.out.println(((char) (i+65)) + ") " + q.strChoices[i]);
            }
            System.out.println("Correct Answer = " + q.answer_key);
            System.out.println("-----------------------------------------");
        }
    }
    
    public class NumericalLabel extends Label {
        
        private double VALUE = 0L, OLD_VALUE = 0L;
        Timeline t = new Timeline();
        
        public final void setValue(final double Value, final int decimal_count) {
            OLD_VALUE = this.VALUE;
            this.VALUE = Value;
            if (t.getStatus() == Timeline.Status.RUNNING) {
                t.stop();
            }
            this.setText(String.format("%."+decimal_count+"f",VALUE));
        }
        
        public final void setValue(final double Value, final double milliseconds, final int decimal_count) {
            if (milliseconds == 0) {
                setValue(Value, decimal_count);
                return;
            } else if (milliseconds < 0) {
                System.err.println("Duration \'milliseconds\' should be always positive.");
                return;
            }
            if (t.getStatus() == Timeline.Status.RUNNING) {
                t.stop();
                this.setText(String.valueOf(VALUE));
            }
            OLD_VALUE = this.VALUE;
            this.VALUE = Value;
            t.getKeyFrames().clear();
            final double DIFFERENCE = this.VALUE - this.OLD_VALUE;
            for (double duration_ms = 0; duration_ms < milliseconds; duration_ms+=20) {
                t.getKeyFrames().add(new KeyFrame(Duration.millis(duration_ms), new KeyValue(this.textProperty(), String.format("%."+decimal_count+"f", (OLD_VALUE+(DIFFERENCE*(duration_ms/milliseconds)))))));
            }
            t.getKeyFrames().add(new KeyFrame(Duration.millis(milliseconds), new KeyValue(this.textProperty(), String.format("%."+decimal_count+"f", VALUE))));
            t.play();
        }
        
        public double getCurrentValue() {
            return VALUE;
        }
        
        public double getPreviousValue() {
            return OLD_VALUE;
        }
    }
    
    private class ResourcesHub {
        public final MediaPlayer[] sfx_correct, sfx_wrong;        
        private final Image imgCorrect, imgWrong, imgTimeOut;        
        public void playSuccessSound() {
            int random_index = (int)(Math.random()*8);
            if(sfx_correct[random_index].getStatus() == MediaPlayer.Status.PLAYING) {
                sfx_correct[random_index].stop();
            }
            sfx_correct[random_index].seek(Duration.ZERO);
            sfx_correct[random_index].play();
        }
        public void playFailureSound() {
            int random_index = (int)(Math.random()*7);
            if(sfx_wrong[random_index].getStatus() == MediaPlayer.Status.PLAYING) {
                sfx_wrong[random_index].stop();
            }
            sfx_wrong[random_index].seek(Duration.ZERO);
            sfx_wrong[random_index].play();
        }
        public ResourcesHub() {
            sfx_correct = new MediaPlayer[8];
            for (int i = 0; i < 8; i++) {
                String strResourcePath = "/res/quiz/sfx/correct" + (i+1) + ".mp3";
                Media sfx_m = new Media(getClass().getResource(strResourcePath).toString());
                sfx_correct[i] = new MediaPlayer(sfx_m);
            }
            sfx_wrong = new MediaPlayer[7];
            for (int i = 0; i < 7; i++) {
                String strResourcePath = "/res/quiz/sfx/wrong" + (i+1) + ".mp3";
                Media sfx_m = new Media(getClass().getResource(strResourcePath).toString());
                sfx_wrong[i] = new MediaPlayer(sfx_m);
            }
            this.imgCorrect = new Image(Cards.class.getResource("/res/quiz/img/correct.png").toString(), 25.0D, 25.0D, true, true);
            this.imgWrong = new Image(Cards.class.getResource("/res/quiz/img/wrong2.png").toString(), 25.0D, 25.0D, true, true);
            this.imgTimeOut = new Image(Cards.class.getResource("/res/quiz/img/time_out.png").toString(), 25.0D, 25.0D, true, true);
        }
    }
    
    private class QA {
        private final String strQuestion;
        private final String[] strChoices;
        private final String answer_key;
        
        public QA(final String question, final String[] strchoices, final String answer_index) {
            this.strQuestion = question;
            this.strChoices = strchoices;
            this.answer_key = answer_index;
        }
    }
    /*********************************************************************************************************/
    /*private ArrayList<QA> assignTechnicalQuestions() {
        ArrayList<QA> questionList = new ArrayList<QA>();
        questionList.add(new QA("What is the extension of Python source file?", new String[]{".p",".py",".python","None of the above"},".py"));
        questionList.add(new QA("Find the odd one out.", new String[]{"C","Java","C++","CSS"},"CSS"));
        questionList.add(new QA("Which is the most secured and statically typed language?", new String[]{"Python","Rust","Java","C"},"Java"));
        questionList.add(new QA("Which is the fastest language?", new String[]{"C++","Java","Scala","Kotlin"},"C++"));
        questionList.add(new QA("What is the official language recommended by Google for Android App Development?", new String[]{"C#","Java","Groovy","Kotlin"},"Kotlin"));
        return questionList;
    }*/
    
    private ArrayList<QA> assignTechnicalQuestions() {
        ArrayList<QA> questionList = new ArrayList<QA>();
        questionList.add(new QA("What is the extension of Python source file?", new String[]{".p",".py",".python","None of the above"},".py"));
        questionList.add(new QA("Find the odd one out.", new String[]{"C","Java","C++","CSS"},"CSS"));
        questionList.add(new QA("Which is the most secured and statically typed language?", new String[]{"Python","Rust","Java","C"},"Java"));
        questionList.add(new QA("Which is the fastest language?", new String[]{"C++","Java","Scala","Kotlin"},"C++"));
        questionList.add(new QA("What is the official language recommended by Google for Android App Development?", new String[]{"C#","Java","Groovy","Kotlin"},"Kotlin"));
        return questionList;
    }

    private ArrayList<QA> assignNonTechnicalQuestions() {
        ArrayList<QA> questionList = new ArrayList<QA>();
        questionList.add(new QA("What is the capital of France?", new String[]{"Paris","London","Berlin","Normandy"},"Paris"));
        questionList.add(new QA("Find the odd one out.", new String[]{"Estonia","Latvia","Lithuania","Poland"},"Poland"));
        questionList.add(new QA("Where does the city FrankFort situated?", new String[]{"England","Germany","Denmark","None of the above"},"Germany"));
        questionList.add(new QA("Which country has most number of lakes?", new String[]{"Canada","Russia","Finland","Norway"},"Finland"));
        questionList.add(new QA("Which country once had Nuclear weapon but not now?", new String[]{"Poland","South Africa","Iraq","Czech Republic"},"South Africa"));
        return questionList;
    }
}
