package com.esri.arcgisruntime.toolkit.examples;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.view.GeoView;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.SceneView;
import com.esri.arcgisruntime.toolkit.Compass;
import com.esri.arcgisruntime.toolkit.FloorFilter;
import com.esri.arcgisruntime.toolkit.examples.model.Example;
import com.esri.arcgisruntime.toolkit.examples.model.ExampleContainer;
import com.esri.arcgisruntime.toolkit.examples.utils.ExampleUtils;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FloorFilterExample extends Application implements Example {

    private final MapView mapView = new MapView();
    private final List<Tab> tabs = new ArrayList<>();
    private final VBox settings;
    private FloorFilter floorFilter;
    private BorderPane borderPane;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        StackPane stackPane = new StackPane();
        Scene scene = new Scene(stackPane);

        ExampleContainer exampleContainer = new ExampleContainer(scene);
        exampleContainer.setExample(new FloorFilterExample());
        stackPane.getChildren().add(exampleContainer);

        scene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());

        // set title, size, and add scene to stage
        primaryStage.setTitle(getExampleName() + " - ArcGIS Runtime for Java Toolkit");
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setWidth(screenBounds.getWidth() * 0.75);
        primaryStage.setHeight(screenBounds.getHeight() * .75);

        primaryStage.setScene(scene);
        primaryStage.show();

        // TODO: set api key for individual runs - doesn't use the gradle file!
    }

    public FloorFilterExample() {

        // configure mapview tab
        ArcGISMap map = new ArcGISMap("https://www.arcgis.com/home/item.html?id=f133a698536f44c8884ad81f80b6cfc7");
        mapView.setMap(map);
        borderPane = new BorderPane();
        floorFilter = new FloorFilter(mapView);
        floorFilter.minHeightProperty().bind(borderPane.heightProperty());
        borderPane.setLeft(floorFilter);
        borderPane.setCenter(mapView);
        Tab mapViewTab = ExampleUtils.createTab(borderPane, "Map");

        tabs.add(mapViewTab);

        // configure settings options
        settings = configureSettings();
    }

    private VBox configureSettings() {
        ArrayList<Node> requiredSettings = new ArrayList<>();

        // automatic selection mode
        VBox autoSelectVBox = new VBox(5);
        Label autoSelectLabel = new Label("Automatic selection mode:");
        ComboBox<FloorFilter.AutomaticSelectionMode> autoSelectComboBox = new ComboBox<>();
        autoSelectComboBox.getItems().addAll(FloorFilter.AutomaticSelectionMode.ALWAYS, FloorFilter.AutomaticSelectionMode.ALWAYS_NON_CLEARING, FloorFilter.AutomaticSelectionMode.NEVER);
        autoSelectComboBox.getSelectionModel().selectedItemProperty().addListener((obvs, ov, nv) -> {
            floorFilter.setAutomaticSelectionMode(nv);
        });
        autoSelectComboBox.getSelectionModel().select(FloorFilter.AutomaticSelectionMode.ALWAYS);
        autoSelectVBox.getChildren().addAll(autoSelectLabel, autoSelectComboBox);
        requiredSettings.add(autoSelectVBox);

        // Layout settings
        TitledPane layoutTitledPane = new TitledPane();
        layoutTitledPane.setExpanded(false);
        layoutTitledPane.setText("Layout settings");
        VBox layoutVBox = new VBox(5);
        layoutTitledPane.setContent(layoutVBox);
        // resize
        Label sizeLabel = new Label("Resize:");
        Slider sizeSlider = new Slider(120, 500, 220);
        sizeSlider.setShowTickLabels(true);
        sizeSlider.setMajorTickUnit(500);
        sizeSlider.valueProperty().addListener((obvs, ov, nv) -> {
            floorFilter.setPrefSize(nv.doubleValue(), nv.doubleValue());
        });
        layoutVBox.getChildren().addAll(sizeLabel, sizeSlider);
        // position
        Label positionLabel = new Label("Re-position:");
        ComboBox<String> positionComboBox = new ComboBox<>();
        positionComboBox.getItems().addAll("Left", "Right");
        positionComboBox.getSelectionModel().selectedItemProperty().addListener((obvs, ov, nv) -> {
            if(nv.equals("Left")) {
                borderPane.setRight(null);
                borderPane.setLeft(floorFilter);
            } else {
                borderPane.setLeft(null);
                borderPane.setRight(floorFilter);
            }
        });
        positionComboBox.getSelectionModel().select("Left");
        layoutVBox.getChildren().addAll(positionLabel, positionComboBox);
        requiredSettings.add(layoutTitledPane);

        return ExampleUtils.createSettings(getExampleName(), requiredSettings);
    }

    @Override
    public String getExampleName() {return "Floor Filter";}

    @Override
    public VBox getSettings() {
        return settings;
    }

    @Override
    public List<Tab> getTabs() {
        return tabs;
    }

    @Override
    public String getDescription() {
        return "Shows sites and facilities, and enables toggling the visibility of levels on floor aware maps and scenes.";
    }

    @Override
    public List<GeoView> getGeoViews() {
        return List.of(mapView);
    }

    @Override
    public void stop() {
        mapView.dispose();
    }
}
