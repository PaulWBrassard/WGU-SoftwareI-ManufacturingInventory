package manufacturinginventory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** Class ManufacturingInventory is an Application
 * that gives a GUI for managing an inventory of parts
 * and products.
 *
 * @author Paul Brassard
 */
public class ManufacturingInventory extends Application {

    private Button partsSearch, partsAdd, partsModify, partsDelete,
            productSearch, productAdd, productModify, productDelete,
            exit, save, cancel, add, delete;
    private TableView partsTV, productTV, associatedPartsTV;
    private Inventory inventory;
    private ObservableList<Part> parts, associatedParts;
    private ObservableList<Product> products;
    private TextField partsSearchBox, productSearchBox, idTF, nameTF,
            inventoryLevelTF, priceTF, maxTF, minTF, companyNameTF, machineIDTF;
    private Label appLabel, partsLabel, productLabel, idLabel, nameLabel,
            inventoryLabel, priceLabel, maxLabel, minLabel, screenLabel,
            companyNameLabel, machineIDLabel;
    private StackPane root;
    private VBox mainScreen, productScreenLeft, productScreenRight;
    private HBox partsAndProductViewer, partsButtons, productButtons,
            hbExitButton, hbSaveAndCancel, hbMaxMin, radios, productScreen,
            hbSearch, hbAdd, hbDelete;
    private GridPane gpParts, gpProducts, PartScreen, descriptionPane;
    private RadioButton inHouse, outsourced;
    private ToggleGroup radioGroup;
    private Stage primaryStage;
    private static int partID = 0, productID = 0;
    private Alert validationAlert, confirmationAlert;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        root = new StackPane();
        Scene scene = new Scene(root, 970, 425);
        scene.getStylesheets().add(ManufacturingInventory.class.getResource("darkTheme.css").toExternalForm());
        primaryStage.setTitle("Manufacturing Inventory");
        primaryStage.setScene(scene);
        primaryStage.show();
        instantiateVariables();
        prepareMainScreen();
        root.getChildren().add(mainScreen);
        prepareEventHandling();
        loadInventory();
    }

    //Instantiate Variables in order of Main Screen, Part Screen, then Product Screen.
    private void instantiateVariables() {
        //Instantiate Variables used for the MainScreen
        partsSearch = new Button("Search");
        productSearch = new Button("Search");
        partsAdd = new Button("Add");
        productAdd = new Button("Add");
        partsModify = new Button("Modify");
        productModify = new Button("Modify");
        partsDelete = new Button("Delete");
        productDelete = new Button("Delete");
        exit = new Button("Exit");
        parts = FXCollections.observableArrayList();
        partsTV = new TableView(parts);
        partsTV.setMinWidth(400);
        TableColumn partIDColumn = new TableColumn("Part ID");
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("partID"));
        partIDColumn.setMinWidth(100);
        TableColumn partNameColumn = new TableColumn("Part Name");
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partNameColumn.setMinWidth(100);
        TableColumn invLevelColumn = new TableColumn("Inventory Level");
        invLevelColumn.setCellValueFactory(new PropertyValueFactory<>("instock"));
        invLevelColumn.setMinWidth(100);
        TableColumn costColumn = new TableColumn("Price/Cost per Unit");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        costColumn.setMinWidth(100);
        partsTV.setItems(parts);
        partsTV.getColumns().addAll(partIDColumn, partNameColumn,
                invLevelColumn, costColumn);
        partsTV.getSortOrder().add(partIDColumn);
        products = FXCollections.observableArrayList();
        productTV = new TableView(products);
        productTV.setMinWidth(400);
        TableColumn productIDColumn = new TableColumn("Product ID");
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productIDColumn.setMinWidth(100);
        TableColumn productNameColumn = new TableColumn("Product Name");
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productNameColumn.setMinWidth(100);
        TableColumn invColumn = new TableColumn("Inventory Level");
        invColumn.setCellValueFactory(new PropertyValueFactory<>("instock"));
        invColumn.setMinWidth(100);
        TableColumn priceColumn = new TableColumn("Price per Unit");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setMinWidth(100);
        productTV.setItems(products);
        productTV.getColumns().addAll(productIDColumn, productNameColumn,
                invColumn, priceColumn);
        productTV.getSortOrder().add(productIDColumn);
        partsSearchBox = new TextField();
        productSearchBox = new TextField();
        appLabel = new Label("Inventory Management System");
        appLabel.setId("appLabel");
        partsLabel = new Label("Parts");
        partsLabel.setId("partsLabel");
        productLabel = new Label("Products");
        productLabel.setId("productLabel");
        partsButtons = new HBox(10);
        partsButtons.setPadding(new Insets(0, 25, 0, 0));
        partsButtons.setAlignment(Pos.BOTTOM_RIGHT);
        productButtons = new HBox(10);
        productButtons.setAlignment(Pos.BOTTOM_RIGHT);
        productButtons.setPadding(new Insets(0, 25, 0, 0));
        gpParts = new GridPane();
        gpParts.setAlignment(Pos.CENTER);
        gpParts.setId("gpParts");
        gpParts.setHgap(10);
        gpParts.setVgap(10);
        gpParts.setPadding(new Insets(25, 0, 25, 0));
        gpProducts = new GridPane();
        gpProducts.setId("gpProducts");
        gpProducts.setAlignment(Pos.CENTER);
        gpProducts.setHgap(10);
        gpProducts.setVgap(10);
        gpProducts.setPadding(new Insets(25, 0, 25, 0));
        partsAndProductViewer = new HBox();
        hbExitButton = new HBox();
        hbExitButton.setAlignment(Pos.BOTTOM_RIGHT);
        hbExitButton.setPadding(new Insets(0, 25, 0, 0));
        mainScreen = new VBox(20);
        mainScreen.setPadding(new Insets(25, 25, 25, 25));

        //Instantiate Variables used for the PartScreen
        validationAlert = new Alert(AlertType.INFORMATION);
        confirmationAlert = new Alert(AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        screenLabel = new Label("Add Part");
        screenLabel.setId("screenLabel");
        screenLabel.setMinWidth(100); //To keep formatting uniform in GridPanes
        radioGroup = new ToggleGroup();
        inHouse = new RadioButton("In-House");
        inHouse.setToggleGroup(radioGroup);
        outsourced = new RadioButton("Outsourced");
        outsourced.setToggleGroup(radioGroup);
        outsourced.setSelected(true);
        idLabel = new Label("ID");
        nameLabel = new Label("Name");
        inventoryLabel = new Label("Inv");
        priceLabel = new Label("Price/Cost");
        priceLabel.setMinWidth(100); //To keep formatting uniform in GridPanes
        maxLabel = new Label("Max");
        minLabel = new Label("Min");
        minLabel.setMinWidth(35);
        companyNameLabel = new Label("Company Name");
        machineIDLabel = new Label("Machine ID");
        idTF = new TextField();
        idTF.setEditable(false);
        idTF.setId("idTF");
        nameTF = new TextField();
        inventoryLevelTF = new TextField("0");
        priceTF = new TextField();
        maxTF = new TextField();
        maxTF.setMaxWidth(75);
        minTF = new TextField();
        minTF.setMaxWidth(75);
        companyNameTF = new TextField();
        machineIDTF = new TextField();
        save = new Button("Save");
        cancel = new Button("Cancel");
        hbSaveAndCancel = new HBox(10);
        hbSaveAndCancel.setAlignment(Pos.BOTTOM_RIGHT);
        hbSaveAndCancel.getChildren().addAll(save, cancel);
        hbMaxMin = new HBox(10);
        hbMaxMin.getChildren().addAll(maxTF, minLabel, minTF);
        radios = new HBox(10);
        radios.getChildren().addAll(inHouse, outsourced);
        PartScreen = new GridPane();
        PartScreen.setHgap(10);
        PartScreen.setVgap(10);
        PartScreen.setPadding(new Insets(25, 25, 25, 50));
        //descriptionPane is used in Parts and Products Screens
        descriptionPane = new GridPane();
        descriptionPane.setHgap(10);
        descriptionPane.setVgap(10);
        descriptionPane.add(idLabel, 0, 1);
        descriptionPane.add(idTF, 1, 1);
        descriptionPane.add(nameLabel, 0, 2);
        descriptionPane.add(nameTF, 1, 2);
        descriptionPane.add(inventoryLabel, 0, 3);
        descriptionPane.add(inventoryLevelTF, 1, 3);
        descriptionPane.add(priceLabel, 0, 4);
        descriptionPane.add(priceTF, 1, 4);
        descriptionPane.add(maxLabel, 0, 5);
        descriptionPane.add(hbMaxMin, 1, 5, 4, 1);
        //descriptionPane.setPadding(new Insets(25,25,25,25));

        //Instantiate Variables used for the productScreen
        productScreen = new HBox(10);
        productScreen.setPadding(new Insets(25, 25, 25, 25));
        add = new Button("Add");
        add.setAlignment(Pos.BOTTOM_RIGHT);
        delete = new Button("Delete");
        delete.setAlignment(Pos.BOTTOM_RIGHT);
        hbSearch = new HBox(10);
        hbSearch.setAlignment(Pos.CENTER);
        hbAdd = new HBox(10);
        hbAdd.setAlignment(Pos.BOTTOM_RIGHT);
        hbAdd.getChildren().add(add);
        hbDelete = new HBox(10);
        hbDelete.setAlignment(Pos.BOTTOM_RIGHT);
        hbDelete.getChildren().add(delete);
        productScreenLeft = new VBox(10);
        productScreenRight = new VBox(10);
        associatedParts = FXCollections.observableArrayList();
        associatedPartsTV = new TableView(associatedParts);
        associatedPartsTV.setMinWidth(400);
        TableColumn partIDColumn2 = new TableColumn("Part ID");
        partIDColumn2.setCellValueFactory(new PropertyValueFactory<>("partID"));
        partIDColumn2.setMinWidth(100);
        TableColumn partNameColumn2 = new TableColumn("Part Name");
        partNameColumn2.setCellValueFactory(new PropertyValueFactory<>("name"));
        partNameColumn2.setMinWidth(100);
        TableColumn invLevelColumn2 = new TableColumn("Inventory Level");
        invLevelColumn2.setCellValueFactory(new PropertyValueFactory<>("instock"));
        invLevelColumn2.setMinWidth(100);
        TableColumn costColumn2 = new TableColumn("Price/Cost per Unit");
        costColumn2.setCellValueFactory(new PropertyValueFactory<>("price"));
        costColumn2.setMinWidth(100);
        associatedPartsTV.setItems(associatedParts);
        associatedPartsTV.getColumns().addAll(partIDColumn2, partNameColumn2,
                invLevelColumn2, costColumn2);
    }

    //Add the functionality to all buttons in the program.
    private void prepareEventHandling() {
        //Exit button for ending the program. Confirms, saves Inventory, then exits.
        exit.setOnAction(event -> {
            confirmationAlert.setContentText("Are you sure you want to exit the program?");
            confirmationAlert.showAndWait();
            if (confirmationAlert.getResult() == ButtonType.NO) {
                return;
            }
            saveInventory();
            Platform.exit();
        });
        //Cancel button used for disregarding product or part add/modify.
        cancel.setOnAction(event -> {
            confirmationAlert.setContentText("Are you sure you want to cancel?");
            confirmationAlert.showAndWait();
            if (confirmationAlert.getResult() == ButtonType.NO) {
                return;
            }
            associatedParts.clear();
            returnToMainScreen();
            clearTFs();
        });
        //Add button used for getting to Add Part Screen.
        partsAdd.setOnAction(event -> {
            outsourced.setSelected(true);
            screenLabel.setText("Add Part");
            idTF.setText("Auto Gen - Disabled");
            preparePartsScreen();
            root.getChildren().add(PartScreen);
            root.getChildren().remove(mainScreen);
            primaryStage.setWidth(400);
            primaryStage.setHeight(375);
        });
        //Modify button used for getting to Modify Part Screen.
        partsModify.setOnAction(event -> {
            if (partsTV.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            screenLabel.setText("Modify Part");
            preparePartsScreen();
            root.getChildren().add(PartScreen);
            root.getChildren().remove(mainScreen);
            primaryStage.setWidth(400);
            primaryStage.setHeight(375);
            Part part = (Part) partsTV.getSelectionModel().getSelectedItem();
            idTF.setText(String.valueOf(part.getPartID()));
            nameTF.setText(part.getName());
            inventoryLevelTF.setText(String.valueOf(part.getInstock()));
            priceTF.setText(String.valueOf(part.getPrice()));
            maxTF.setText(String.valueOf(part.getMax()));
            minTF.setText(String.valueOf(part.getMin()));
            if (part instanceof Outsourced) {
                companyNameTF.setText(((Outsourced) part).getCompanyName());
                outsourced.setSelected(true);
                outsourcedSelected();
            } else {
                machineIDTF.setText(String.valueOf(((Inhouse) part).getPartID()));
                inHouse.setSelected(true);
                inHouseSelected();
            }
        });
        //Add button used for getting to Add Product Screen.
        productAdd.setOnAction(event -> {
            screenLabel.setText("Add Product");
            idTF.setText("Auto Gen - Disabled");
            prepareProductScreen();
            root.getChildren().add(productScreen);
            root.getChildren().remove(mainScreen);
            primaryStage.setWidth(775);
        });
        //Modify button used for getting to Modify Product Screen.
        productModify.setOnAction(event -> {
            if (productTV.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            screenLabel.setText("Modify Product");
            prepareProductScreen();
            root.getChildren().add(productScreen);
            root.getChildren().remove(mainScreen);
            primaryStage.setWidth(775);
            Product product = (Product) productTV.getSelectionModel().getSelectedItem();
            idTF.setText(String.valueOf(product.getProductID()));
            nameTF.setText(product.getName());
            inventoryLevelTF.setText(String.valueOf(product.getInstock()));
            priceTF.setText(Double.toString(product.getPrice()));
            maxTF.setText(String.valueOf(product.getMax()));
            minTF.setText(String.valueOf(product.getMin()));
            for (int i = 0; i < product.getAssociatedPartsSize(); i++) {
                associatedParts.add(product.lookupAssociatedPart(i));
            }
        });
        //Outsourced radio button used in part creation.
        outsourced.setOnAction(event -> {
            outsourcedSelected();
        });
        //Inhouse radio button used in part creation.
        inHouse.setOnAction(event -> {
            inHouseSelected();
        });
        //Save button used to save details of a part or product.
        save.setOnAction(event -> {
            //Inform user that all fields are mandatory.
            if (nameTF.getText().equals("") || priceTF.getText().equals("") || "".equals(inventoryLevelTF.getText()) || "".equals(maxTF.getText())
                    || minTF.getText().equals("")) {
                validationAlert.setContentText("All input fields are required!");
                validationAlert.showAndWait();
                return;
            }
            //Inform user that a part needs category info.
            if (screenLabel.getText().contains("Part")
                    && (machineIDTF.getText().equals("") && companyNameTF.getText().equals(""))) {
                validationAlert.setContentText("Category info is required!");
                validationAlert.showAndWait();
                return;
            }
            //Inform user if Max < Min.
            if (Integer.valueOf(maxTF.getText()) < Integer.valueOf(minTF.getText())) {
                validationAlert.setContentText("Max value must be larger than Min value!");
                validationAlert.showAndWait();
                return;
            }
            //Inform user if inventory is outside min-max range.
            if (Integer.valueOf(inventoryLevelTF.getText()) > Integer.valueOf(maxTF.getText())
                    || Integer.valueOf(inventoryLevelTF.getText()) < Integer.valueOf(minTF.getText())) {
                validationAlert.setContentText("Inventory is not within min-max range!");
                validationAlert.showAndWait();
                return;
            }
            if (screenLabel.getText().equals("Add Part")) {
                addPart();
            }
            if (screenLabel.getText().equals("Modify Part")) {
                modifyPart();
            }
            //Inform user if product price is less than cost of the parts.
            double sum = 0;
            for (int i = 0; i < associatedParts.size(); i++) {
                sum += associatedParts.get(i).getPrice();
            }
            if (Double.valueOf(priceTF.getText()) < sum) {
                validationAlert.setContentText("Product price must be greater than sum of its parts!");
                validationAlert.showAndWait();
                return;
            }
            //Inform user if product does not have any parts yet.
            if (screenLabel.getText().contains("Product")){
                if (associatedParts.size() < 1) {
                    validationAlert.setContentText("Products must have at least one part");
                    validationAlert.showAndWait();
                    return;
                }
            }
            if (screenLabel.getText().equals("Add Product")) {
                addProduct();
            }
            if (screenLabel.getText().equals("Modify Product")) {
                modifyProduct();
            }
            clearTFs();
            returnToMainScreen();
        });
        //Delete button used for removing parts from a product.
        delete.setOnAction(event -> {
            confirmationAlert.setContentText("Are you sure you want to delete this part?");
            confirmationAlert.showAndWait();
            if (confirmationAlert.getResult() == ButtonType.NO) {
                return;
            }
            Part part = (Part) associatedPartsTV.getSelectionModel().getSelectedItem();
            associatedParts.remove(part);
        });
        //Delete button used for removing parts from the inventory.
        partsDelete.setOnAction(event -> {
            if (partsTV.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            confirmationAlert.setContentText("Are you sure you want to delete this part?");
            confirmationAlert.showAndWait();
            if (confirmationAlert.getResult() == ButtonType.NO) {
                return;
            }
            Part part = (Part) partsTV.getSelectionModel().getSelectedItem();
            parts.remove(part);
        });
        //Delete button used for removing products from the inventory.
        productDelete.setOnAction(event -> {
            if (productTV.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            confirmationAlert.setContentText("Are you sure you want to delete this product?");
            confirmationAlert.showAndWait();
            if (confirmationAlert.getResult() == ButtonType.NO) {
                return;
            }
            Product product = (Product) productTV.getSelectionModel().getSelectedItem();
            products.remove(product);
        });
        //Search button used for finding a part in inventory.
        partsSearch.setOnAction(event -> {
            for (Part p : parts) {
                if (p.getName().equalsIgnoreCase(partsSearchBox.getText())) {
                    partsTV.scrollTo(p);
                    partsTV.getSelectionModel().select(p);
                    break;
                }
            }
        });
        //Search button used for finding a product in inventory.
        productSearch.setOnAction(event -> {
            for (Product p : products) {
                if (p.getName().equalsIgnoreCase(productSearchBox.getText())) {
                    productTV.scrollTo(p);
                    productTV.getSelectionModel().select(p);
                    break;
                }
            }
        });
        //Add button used for adding a part to a product.
        add.setOnAction(event -> {
            Part selection = (Part) partsTV.getSelectionModel().getSelectedItem();
            if (selection != null) {
                associatedParts.add(selection);
            }
        });
    }

    //Show Machine ID Label and TextField not Company Name, part is Inhouse.
    private void inHouseSelected() {
        companyNameTF.clear(); //Inhouse parts don't list company Name.
        PartScreen.getChildren().remove(companyNameLabel);
        PartScreen.getChildren().remove(companyNameTF);
        PartScreen.add(machineIDLabel, 0, 6);
        PartScreen.add(machineIDTF, 1, 6);
        //Adding and removing hbSaveAndCancel preserves tabbing order
        PartScreen.getChildren().remove(hbSaveAndCancel);
        PartScreen.add(hbSaveAndCancel, 0, 7, 5, 1);
    }

    //Show Company Name Label and TextField not machine ID, part is Outsourced.
    private void outsourcedSelected() {
        machineIDTF.clear(); //Outsourced parts don't have a machine ID.
        PartScreen.getChildren().remove(machineIDLabel);
        PartScreen.getChildren().remove(machineIDTF);
        PartScreen.add(companyNameLabel, 0, 6);
        PartScreen.add(companyNameTF, 1, 6);
        //Adding and removing hbSaveAndCancel preserves tabbing order.
        PartScreen.getChildren().remove(hbSaveAndCancel);
        PartScreen.add(hbSaveAndCancel, 0, 7, 5, 1);
    }

    //Connects appropriate nodes to the Main Screen, clearing each node parent first to avoid conflicts
    private void prepareMainScreen() {
        partsButtons.getChildren().clear();
        partsButtons.getChildren().addAll(partsAdd, partsModify, partsDelete);
        productButtons.getChildren().clear();
        productButtons.getChildren().addAll(productAdd, productModify, productDelete);
        gpParts.getChildren().clear();
        gpParts.add(partsLabel, 0, 0);
        gpParts.add(partsSearch, 3, 0);
        gpParts.add(partsSearchBox, 4, 0);
        gpParts.add(partsTV, 0, 1, 5, 4);
        gpParts.add(partsButtons, 3, 6, 3, 1);
        gpProducts.getChildren().clear();
        gpProducts.add(productLabel, 0, 0);
        gpProducts.add(productSearch, 3, 0);
        gpProducts.add(productSearchBox, 4, 0);
        gpProducts.add(productTV, 0, 1, 5, 4);
        gpProducts.add(productButtons, 3, 6, 3, 1);
        partsAndProductViewer.getChildren().clear();
        partsAndProductViewer.getChildren().add(gpParts);
        partsAndProductViewer.getChildren().add(gpProducts);
        hbExitButton.getChildren().clear();
        hbExitButton.getChildren().add(exit);
        mainScreen.getChildren().clear();
        mainScreen.getChildren().add(appLabel);
        mainScreen.getChildren().add(partsAndProductViewer);
        mainScreen.getChildren().add(hbExitButton);
    }

    //Connects appropriate nodes to the Parts Screen, clearing each node parent first to avoid conflicts
    private void preparePartsScreen() {
        PartScreen.getChildren().clear();
        PartScreen.add(screenLabel, 0, 0);
        PartScreen.add(radios, 1, 0, 4, 1);
        PartScreen.add(descriptionPane, 0, 1, 5, 5);
        PartScreen.add(companyNameLabel, 0, 6);
        PartScreen.add(companyNameTF, 1, 6);
        PartScreen.add(hbSaveAndCancel, 0, 7, 5, 1);
    }

    //Connects appropriate nodes to the Product Screen, clearing each node parent first to avoid conflicts
    private void prepareProductScreen() {
        productScreenRight.getChildren().clear();
        hbSearch.getChildren().addAll(partsSearch, partsSearchBox);
        productScreenRight.getChildren().add(hbSearch);
        productScreenRight.getChildren().add(partsTV);
        productScreenRight.getChildren().add(hbAdd);
        productScreenRight.getChildren().add(associatedPartsTV);
        productScreenRight.getChildren().add(hbDelete);
        productScreenRight.getChildren().add(hbSaveAndCancel);
        productScreenLeft.getChildren().clear();
        productScreenLeft.getChildren().add(screenLabel);
        productScreenLeft.getChildren().add(descriptionPane);
        productScreen.getChildren().clear();
        productScreen.getChildren().add(productScreenLeft);
        productScreen.getChildren().add(productScreenRight);
    }

    //Make Main Screen visible to user.
    private void returnToMainScreen() {
        root.getChildren().clear();
        prepareMainScreen();
        root.getChildren().add(mainScreen);
        primaryStage.setWidth(970);
        primaryStage.setHeight(425);
    }

    //Create a part based on the TextField inputs and add to parts list.
    private void addPart() {
        Part newPart;
        if (outsourced.isSelected()) {
            newPart = new Outsourced();
            ((Outsourced) newPart).setCompanyName(companyNameTF.getText());
        } else {

            newPart = new Inhouse();
            //Inform user if machine ID entered is not a number.
            try {
                ((Inhouse) newPart).setMachineID(Integer.parseInt(machineIDTF.getText()));
            } catch (NumberFormatException e) {
                validationAlert.setContentText("Machine ID must be a number");
                validationAlert.showAndWait();
                return;
            }
        }
        newPart.setPartID(++partID);
        newPart.setName(nameTF.getText());
        newPart.setInstock(Integer.parseInt(inventoryLevelTF.getText()));
        newPart.setPrice(Double.parseDouble(priceTF.getText()));
        newPart.setMax(Integer.parseInt(maxTF.getText()));
        newPart.setMin(Integer.parseInt(minTF.getText()));
        parts.add(newPart);
    }

    //Modify a part by removing the old version and adding new version.
    private void modifyPart() {
        Part part = (Part) partsTV.getSelectionModel().getSelectedItem();
        parts.remove(part);
        part.setName(nameTF.getText());
        part.setInstock(Integer.valueOf(inventoryLevelTF.getText()));
        part.setPrice(Double.valueOf(priceTF.getText()));
        part.setMax(Integer.valueOf(maxTF.getText()));
        part.setMin(Integer.valueOf(minTF.getText()));
        if (part instanceof Outsourced) {
            ((Outsourced) part).setCompanyName(companyNameTF.getText());
        } else {
            ((Inhouse) part).setMachineID(Integer.valueOf(machineIDTF.getText()));
        }
        parts.add(part);
        partsTV.sort();
        partsTV.refresh();
    }

    //Create a product based on the TextField inputs and add to product list.
    private void addProduct() {
        String name = nameTF.getText();
        Double price = Double.parseDouble(priceTF.getText());
        Product newProduct = new Product(name, price);
        newProduct.setProductID(++productID);
        newProduct.setInstock(Integer.parseInt(inventoryLevelTF.getText()));
        newProduct.setMax(Integer.parseInt(maxTF.getText()));
        newProduct.setMin(Integer.parseInt(minTF.getText()));
        associatedParts.forEach((p) -> {
            newProduct.addAssociatedPart(p);
        });
        products.add(newProduct);
        associatedParts.clear(); //clearing associatedParts ensures old values don't persist to other product requests.
    }

    //Modify a product by removing the old version and adding new version.
    private void modifyProduct() {
        Product product = (Product) productTV.getSelectionModel().getSelectedItem();
        products.remove(product);
        product.setName(nameTF.getText());
        product.setInstock(Integer.valueOf(inventoryLevelTF.getText()));
        product.setPrice(Double.valueOf(priceTF.getText()));
        product.setMax(Integer.valueOf(maxTF.getText()));
        product.setMin(Integer.valueOf(minTF.getText()));
        product.clearAssociatedParts();
        for (int i = 0; i < associatedParts.size(); i++) {
            product.addAssociatedPart(associatedParts.get(i));
        }
        products.add(product);
        productTV.sort();
        productTV.refresh();
        associatedParts.clear(); //clearing associatedParts ensures old values don't persist to other product requests.
    }

    //Empty all Text Fields
    private void clearTFs() {
        nameTF.clear();
        inventoryLevelTF.setText("0");
        priceTF.clear();
        maxTF.clear();
        minTF.clear();
        companyNameTF.clear();
        machineIDTF.clear();
    }

    //Add all parts and products to inventory and then output to file
    private void saveInventory() {
        inventory = new Inventory();
        parts.forEach(p -> {
            inventory.addPart(p);
        });
        products.forEach(p -> {
            inventory.addProduct(p);
        });
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Inventory.inv"))) {
            oos.writeObject(inventory);
            oos.close();
            System.out.println("Inventory successfully saved.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Read all parts and products from Inventory file and load them into the program
    private void loadInventory() {
        Inventory inv;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Inventory.inv"))) {
            inv = (Inventory) ois.readObject();
            ois.close();
            int i = 0;
            try {
                Product p = inv.lookupProduct(i);
                while (p != null) {
                    products.add(p);
                    i++;
                    p = inv.lookupProduct(i);
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Finished loading products.");
            }
            i = 0;
            try {
                Part part = inv.lookupPart(i);
                while (part != null) {
                    parts.add(part);
                    i++;
                    part = inv.lookupPart(i);
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Finished loading parts.");
            }
        } catch (IOException e) {
            System.out.println("No Inventory to load.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("Cannot add parts or products.");
        }
    }
}
