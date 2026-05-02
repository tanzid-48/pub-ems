package com.PUB;

import com.PUB.dao.DAOFactory;
import com.PUB.db.DB;
import com.PUB.model.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.animation.*;
import javafx.util.Duration;
import java.sql.SQLException;
import java.time.LocalDate;

public class Main extends Application {

    private StackPane  content;
    private Label      titleLbl, subLbl;
    private Button     activeNav;
    private BorderPane root;

    static final String BG     = "#06101e";
    static final String CARD   = "#0a1628";
    static final String BORDER = "#111f38";
    static final String BLUE   = "#2563eb";
    static final String TEAL   = "#0ea5e9";
    static final String GREEN  = "#10b981";
    static final String PURPLE = "#7c3aed";
    static final String AMBER  = "#f59e0b";
    static final String RED    = "#ef4444";
    static final String TXTP   = "#e2e8f0";
    static final String TXTS   = "#7a9cc0";
    static final String TXTM   = "#2a4a70";

    @Override
    public void start(Stage stage) {
        new SplashScreen().show(() -> Platform.runLater(() -> launch(stage)));
    }

    private void launch(Stage stage) {
        if (!new LoginWindow().show()) { Platform.exit(); return; }

        root = new BorderPane();
        root.setLeft(buildSidebar());
        root.setTop(buildTopbar());
        content = new StackPane();
        content.setStyle("-fx-background-color:" + BG + ";");
        root.setCenter(content);

        Scene scene = new Scene(root, 1300, 800);
        stage.setTitle("PUB EMS v6 — Pundra University of Science & Technology");
        stage.setScene(scene);
        stage.setMinWidth(1100);
        stage.setMinHeight(700);
        stage.setMaximized(true);
        stage.show();
        showDashboard();
    }

    // ── SIDEBAR ──────────────────────────────────────────────────
    private VBox buildSidebar() {
        VBox sb = new VBox();
        sb.setStyle("-fx-background-color:#070f1d;-fx-border-color:" + BORDER + ";-fx-border-width:0 1 0 0;");
        sb.setMinWidth(240); sb.setMaxWidth(240);

        // Logo
        VBox logoBox = new VBox(4);
        logoBox.setPadding(new Insets(16, 14, 14, 14));
        logoBox.setStyle("-fx-background-color:#050d1a;-fx-border-color:" + BORDER + ";-fx-border-width:0 0 1 0;");
        HBox lr = new HBox(12); lr.setAlignment(Pos.CENTER_LEFT);
        StackPane lc = new StackPane();
        Circle lcirc = new Circle(22);
        lcirc.setFill(new LinearGradient(0,0,1,1,true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#1e40af")),
            new Stop(1, Color.web("#0f2d6b"))));
        lcirc.setStroke(Color.web(BLUE)); lcirc.setStrokeWidth(1.5);
        lcirc.setEffect(new DropShadow(14, Color.web("#2563eb70")));
        Label lp = new Label("P");
        lp.setStyle("-fx-text-fill:white;-fx-font-size:15px;-fx-font-weight:bold;");
        lc.getChildren().addAll(lcirc, lp);
        VBox lt = new VBox(1);
        Label l1 = new Label("PUB EMS v6");
        l1.setStyle("-fx-text-fill:" + TXTP + ";-fx-font-size:13px;-fx-font-weight:bold;");
        Label l2 = new Label("Pundra University");
        l2.setStyle("-fx-text-fill:" + BLUE + ";-fx-font-size:9px;");
        Label l3 = new Label("Bogura, Bangladesh");
        l3.setStyle("-fx-text-fill:" + TXTM + ";-fx-font-size:8px;");
        lt.getChildren().addAll(l1, l2, l3);
        lr.getChildren().addAll(lc, lt);
        Label badge = new Label("Analytics Dashboard");
        badge.setStyle("-fx-background-color:rgba(0,180,166,0.15);-fx-text-fill:#00d4c4;" +
            "-fx-font-size:9px;-fx-padding:2 8;-fx-background-radius:10;" +
            "-fx-border-color:rgba(0,180,166,0.3);-fx-border-radius:10;");
        logoBox.getChildren().addAll(lr, badge);

        // Nav
        VBox nav = new VBox(2);
        nav.setPadding(new Insets(12, 6, 6, 6));
        VBox.setVgrow(nav, Priority.ALWAYS);
        nav.getChildren().addAll(
            navSec("OVERVIEW"),
            navBtn("📊", "Analytics Dashboard",  this::showDashboard),
            navSec("EMPLOYEES"),
            navBtn("🎓", "Faculty Members",      () -> showEmployees("FACULTY")),
            navBtn("🏢", "Staff & Admin",        () -> showEmployees("STAFF")),
            navBtn("👥", "All Employees",        () -> showEmployees("ALL")),
            navSec("MANAGEMENT"),
            navBtn("🏛",  "Departments",         this::showDepartments),
            navBtn("💰",  "Salary & Payroll",    this::showSalary),
            navBtn("📋",  "Attendance",          this::showAttendance),
            navBtn("📝",  "Leave Requests",      this::showLeave),
            navBtn("🚌",  "Bus Schedule",        this::showBus)
        );

        // User footer
        HBox uf = new HBox(10);
        uf.setAlignment(Pos.CENTER_LEFT);
        uf.setPadding(new Insets(12));
        uf.setStyle("-fx-background-color:#050d1a;-fx-border-color:" + BORDER + ";-fx-border-width:1 0 0 0;");
        StackPane av = new StackPane();
        Rectangle avbg = new Rectangle(34, 34);
        avbg.setArcWidth(10); avbg.setArcHeight(10);
        avbg.setFill(new LinearGradient(0,0,1,1,true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web(BLUE)),
            new Stop(1, Color.web(PURPLE))));
        Label avl = new Label("AD");
        avl.setStyle("-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:10px;");
        av.getChildren().addAll(avbg, avl);
        VBox ui = new VBox(2);
        Label un = new Label("Administrator");
        un.setStyle("-fx-text-fill:" + TXTP + ";-fx-font-size:11px;-fx-font-weight:600;");
        Label ur = new Label("SUPER ADMIN");
        ur.setStyle("-fx-text-fill:" + TXTM + ";-fx-font-size:8px;-fx-font-family:'Consolas';");
        ui.getChildren().addAll(un, ur);
        HBox.setHgrow(ui, Priority.ALWAYS);
        Circle dot = new Circle(4, Color.web(GREEN));
        uf.getChildren().addAll(av, ui, dot);

        sb.getChildren().addAll(logoBox, nav, uf);
        return sb;
    }

    private Label navSec(String t) {
        Label l = new Label(t);
        l.setStyle("-fx-text-fill:" + TXTM + ";-fx-font-size:9px;" +
            "-fx-font-family:'Consolas';-fx-font-weight:bold;-fx-padding:10 8 3 8;");
        return l;
    }

    private Button navBtn(String icon, String label, Runnable action) {
        Button btn = new Button(icon + "  " + label);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle(navStyle(false));
        btn.setOnAction(e -> {
            if (activeNav != null) activeNav.setStyle(navStyle(false));
            btn.setStyle(navStyle(true));
            activeNav = btn;
            action.run();
        });
        return btn;
    }

    private String navStyle(boolean on) {
        return on
            ? "-fx-background-color:rgba(37,99,235,0.18);-fx-text-fill:#ffffff;" +
              "-fx-font-size:12px;-fx-font-weight:600;-fx-padding:9 12;" +
              "-fx-background-radius:9;-fx-border-color:" + BLUE +
              ";-fx-border-width:0 0 0 3;-fx-border-radius:0;-fx-cursor:hand;"
            : "-fx-background-color:transparent;-fx-text-fill:" + TXTS +
              ";-fx-font-size:12px;-fx-padding:9 12;-fx-background-radius:9;" +
              "-fx-border-color:transparent;-fx-border-width:0 0 0 3;-fx-cursor:hand;";
    }

    // ── TOPBAR ───────────────────────────────────────────────────
    private HBox buildTopbar() {
        HBox bar = new HBox(14);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(0, 24, 0, 24));
        bar.setMinHeight(56); bar.setMaxHeight(56);
        bar.setStyle("-fx-background-color:#050d1a;-fx-border-color:" + BORDER + ";-fx-border-width:0 0 1 0;");
        HBox tb = new HBox(10); tb.setAlignment(Pos.CENTER_LEFT);
        titleLbl = new Label("Analytics Dashboard");
        titleLbl.setStyle("-fx-text-fill:" + TXTP + ";-fx-font-size:15px;-fx-font-weight:bold;");
        subLbl = new Label("// PUB EMS v6");
        subLbl.setStyle("-fx-text-fill:" + TXTM + ";-fx-font-size:11px;-fx-font-family:'Consolas';");
        tb.getChildren().addAll(titleLbl, subLbl);
        HBox.setHgrow(tb, Priority.ALWAYS);
        Label date = new Label("📅  " + LocalDate.now());
        date.setStyle("-fx-text-fill:" + TXTM + ";-fx-font-size:11px;");
        Label univ = new Label("  |  Pundra University, Bogura");
        univ.setStyle("-fx-text-fill:" + TXTM + ";-fx-font-size:11px;");
        Label analytBadge = new Label("📊 Analytics v6");
        analytBadge.setStyle("-fx-background-color:rgba(0,180,166,0.12);-fx-text-fill:#00b4a6;" +
            "-fx-font-size:10px;-fx-font-weight:bold;-fx-padding:4 12;" +
            "-fx-background-radius:20;-fx-border-color:rgba(0,180,166,0.3);-fx-border-radius:20;");
        bar.getChildren().addAll(tb, analytBadge, date, univ);
        return bar;
    }

    private void setTitle(String t, String s) {
        titleLbl.setText(t); subLbl.setText(s);
    }

    // ── SCREENS ──────────────────────────────────────────────────
    private void showDashboard() {
        setTitle("Analytics Dashboard", "// KPI · Charts · Insights");
        show(new DashboardPanel().build());
    }

    private void showEmployees(String type) {
        String title = switch(type) {
            case "FACULTY" -> "Faculty Members";
            case "STAFF"   -> "Staff & Admin";
            default        -> "All Employees";
        };
        setTitle(title, "// Directory");
        VBox screen = new VBox(16);
        screen.setPadding(new Insets(22, 26, 22, 26));
        screen.setStyle("-fx-background-color:" + BG + ";");

        TextField search = new TextField();
        search.setPromptText("🔍  Search name, ID, designation...");
        search.setStyle(inp()); search.setMinWidth(300);
        HBox.setHgrow(search, Priority.ALWAYS);
        Button addBtn = pBtn("+ Add Employee");
        HBox toolbar = new HBox(10, search, addBtn);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        TableView<Employee> table = empTable();
        try {
            table.setItems(type.equals("ALL")
                ? DAOFactory.emp().getAll()
                : DAOFactory.emp().getByType(type));
        } catch (SQLException e) { err(e.getMessage()); }

        search.textProperty().addListener((o, old, val) -> {
            try {
                table.setItems(val.isBlank()
                    ? (type.equals("ALL") ? DAOFactory.emp().getAll() : DAOFactory.emp().getByType(type))
                    : DAOFactory.emp().search(val));
            } catch (SQLException ex) { err(ex.getMessage()); }
        });
        addBtn.setOnAction(e -> showEmpForm(null, table, type));

        VBox card = tCard(title + " Directory", toolbar, table);
        VBox.setVgrow(card, Priority.ALWAYS);
        screen.getChildren().add(card);
        show(scroll(screen));
    }

    @SuppressWarnings("unchecked")
    private TableView<Employee> empTable() {
        TableView<Employee> t = new TableView<>();
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        styleTable(t);
        t.getColumns().addAll(
            tc("EMP ID",      "empId",       90,  true),
            tc("Name",        "name",        200, false),
            tc("Designation", "designation", 150, false),
            tc("Department",  "deptName",    155, false),
            tc("Phone",       "phone",       115, true),
            tc("Join Date",   "joinDate",    100, true),
            statusCol(), salaryCol(), actionCol(t)
        );
        return t;
    }

    private <T> TableColumn<T, String> tc(String hdr, String prop, int w, boolean mono) {
        TableColumn<T, String> c = new TableColumn<>(hdr);
        c.setCellValueFactory(new PropertyValueFactory<>(prop));
        c.setMinWidth(w);
        c.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String v, boolean empty) {
                super.updateItem(v, empty); setText(empty ? null : v);
                setStyle("-fx-text-fill:" + TXTP + ";-fx-padding:10 12;" +
                    (mono ? "-fx-font-family:'Consolas';-fx-font-size:12px;" : "-fx-font-size:13px;"));
            }
        });
        return c;
    }

    private TableColumn<Employee, Void> actionCol(TableView<Employee> table) {
        TableColumn<Employee, Void> col = new TableColumn<>("ACTIONS");
        col.setMinWidth(145); col.setMaxWidth(165);
        col.setCellFactory(c -> new TableCell<>() {
            final Button edit = aBtn("✎ Edit", BLUE);
            final Button del  = aBtn("✕ Del",  RED);
            final HBox   box  = new HBox(6, edit, del);
            { box.setAlignment(Pos.CENTER); }
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                if (empty) { setGraphic(null); return; }
                Employee emp = getTableView().getItems().get(getIndex());
                edit.setOnAction(ev -> showEmpForm(emp, table, emp.getEmpType()));
                del.setOnAction(ev -> {
                    if (confirm("Delete " + emp.getName() + "?")) {
                        try {
                            DAOFactory.emp().delete(emp.getId());
                            table.getItems().remove(emp);
                        } catch (SQLException ex) { err(ex.getMessage()); }
                    }
                });
                setGraphic(box);
            }
        });
        return col;
    }

    private void showEmpForm(Employee ex, TableView<Employee> table, String type) {
        Stage dlg = dlg(ex == null ? "Add Employee" : "Edit Employee", 580, 580);
        VBox form = new VBox(14);
        form.setPadding(new Insets(22));
        form.setStyle("-fx-background-color:#070e1c;");
        Label title = new Label(ex == null ? "+ Add New Employee" : "✎  Edit Employee");
        title.setStyle("-fx-text-fill:" + BLUE + ";-fx-font-size:14px;-fx-font-weight:bold;");
        GridPane grid = new GridPane(); grid.setHgap(14); grid.setVgap(11);

        TextField empIdF = fi("PUB-FAC-001", ex==null?"":nv(ex.getEmpId()));
        TextField nameF  = fi("Full Name",   ex==null?"":nv(ex.getName()));
        TextField emailF = fi("Email",       ex==null?"":nv(ex.getEmail()));
        TextField phoneF = fi("Phone",       ex==null?"":nv(ex.getPhone()));
        TextField nidF   = fi("NID",         ex==null?"":nv(ex.getNid()));
        TextField desigF = fi("Designation", ex==null?"":nv(ex.getDesignation()));
        TextField joinF  = fi("YYYY-MM-DD",  ex==null?"":nv(ex.getJoinDate()));
        TextField salF   = fi("Salary BDT",  ex==null?"0":String.valueOf((long)ex.getSalary()));
        TextField bloodF = fi("Blood Group", ex==null?"":nv(ex.getBlood()));
        TextField addrF  = fi("Address",     ex==null?"":nv(ex.getAddress()));
        ComboBox<String> typeC   = cbo(new String[]{"FACULTY","STAFF","ADMIN"}, ex==null?type:ex.getEmpType());
        ComboBox<String> statusC = cbo(new String[]{"ACTIVE","INACTIVE","ON_LEAVE","RETIRED"}, ex==null?"ACTIVE":ex.getStatus());
        ComboBox<Department> deptC = new ComboBox<>();
        deptC.setStyle(inp() + "-fx-min-width:260px;");
        try {
            deptC.setItems(DAOFactory.dept().getAll());
            if (ex != null) deptC.getItems().stream()
                .filter(d -> d.getId() == ex.getDeptId()).findFirst().ifPresent(deptC::setValue);
        } catch (SQLException e) { err(e.getMessage()); }

        int r = 0;
        row(grid,r++,"EMP ID",     empIdF); row(grid,r++,"Name",       nameF);
        row(grid,r++,"Email",      emailF); row(grid,r++,"Phone",      phoneF);
        row(grid,r++,"NID",        nidF);   row(grid,r++,"Designation",desigF);
        row(grid,r++,"Type",       typeC);  row(grid,r++,"Department", deptC);
        row(grid,r++,"Status",     statusC);row(grid,r++,"Join Date",  joinF);
        row(grid,r++,"Salary",     salF);   row(grid,r++,"Blood",      bloodF);
        row(grid,r++,"Address",    addrF);

        Button save   = pBtn(ex==null?"✓  Save":"✓  Update");
        Button cancel = gBtn("Cancel"); cancel.setOnAction(e -> dlg.close());
        save.setOnAction(e -> {
            try {
                Employee emp = ex==null ? new Employee() : ex;
                emp.setEmpId(empIdF.getText()); emp.setName(nameF.getText());
                emp.setEmail(emailF.getText());  emp.setPhone(phoneF.getText());
                emp.setNid(nidF.getText());      emp.setDesignation(desigF.getText());
                emp.setEmpType(typeC.getValue()); emp.setStatus(statusC.getValue());
                emp.setJoinDate(joinF.getText());
                emp.setSalary(salF.getText().isBlank()?0:Double.parseDouble(salF.getText()));
                emp.setBlood(bloodF.getText());  emp.setAddress(addrF.getText());
                if (deptC.getValue()!=null) emp.setDeptId(deptC.getValue().getId());
                if (ex==null) DAOFactory.emp().add(emp); else DAOFactory.emp().update(emp);
                table.setItems(type.equals("ALL")
                    ? DAOFactory.emp().getAll() : DAOFactory.emp().getByType(type));
                dlg.close();
            } catch (SQLException ex2) { err(ex2.getMessage()); }
        });

        ScrollPane sp = new ScrollPane(grid); sp.setFitToWidth(true);
        sp.setStyle("-fx-background:transparent;-fx-background-color:transparent;");
        form.getChildren().addAll(title, sp, new HBox(10, save, cancel));
        dlg.setScene(new Scene(form, 580, 580)); dlg.showAndWait();
    }

    private void showDepartments() {
        setTitle("Departments", "// Academic Departments");
        VBox screen = new VBox(16); screen.setPadding(new Insets(22,26,22,26));
        screen.setStyle("-fx-background-color:" + BG + ";");
        Button addBtn = pBtn("+ Add Department");
        HBox toolbar = new HBox(addBtn); toolbar.setAlignment(Pos.CENTER_RIGHT);
        TableView<Department> t = new TableView<>();
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); styleTable(t);

        TableColumn<Department,Void> act = new TableColumn<>("ACTIONS"); act.setMaxWidth(150);
        act.setCellFactory(c -> new TableCell<>() {
            final Button e2 = aBtn("✎ Edit",BLUE); final Button d = aBtn("✕ Del",RED);
            final HBox box = new HBox(6,e2,d); { box.setAlignment(Pos.CENTER); }
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v,empty); if(empty){setGraphic(null);return;}
                Department dept = getTableView().getItems().get(getIndex());
                e2.setOnAction(ev -> showDeptForm(dept,t));
                d.setOnAction(ev -> { if(confirm("Delete "+dept.getName()+"?")){
                    try{DAOFactory.dept().delete(dept.getId());t.getItems().remove(dept);}
                    catch(SQLException ex){err(ex.getMessage());}}});
                setGraphic(box);
            }
        });
        TableColumn<Department,Integer> cnt = new TableColumn<>("STAFF"); cnt.setMaxWidth(80);
        cnt.setCellValueFactory(new PropertyValueFactory<>("empCount"));
        cnt.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(Integer v, boolean empty) {
                super.updateItem(v,empty); setText(empty||v==null?null:v+" staff");
                setStyle("-fx-text-fill:"+BLUE+";-fx-font-family:'Consolas';-fx-padding:10 12;");
            }
        });
        t.getColumns().addAll(
            tc("Code","code",80,true), tc("Department","name",220,false),
            tc("Faculty","faculty",180,false), tc("Head","headName",160,false), cnt, act);
        try { t.setItems(DAOFactory.dept().getAll()); } catch(SQLException e){err(e.getMessage());}
        addBtn.setOnAction(e -> showDeptForm(null, t));
        VBox card = tCard("All Departments", toolbar, t);
        VBox.setVgrow(t,Priority.ALWAYS); VBox.setVgrow(card,Priority.ALWAYS);
        screen.getChildren().add(card); show(scroll(screen));
    }

    private void showDeptForm(Department ex, TableView<Department> t) {
        Stage dlg = dlg(ex==null?"Add Department":"Edit Department", 440, 280);
        VBox form = new VBox(14); form.setPadding(new Insets(22));
        form.setStyle("-fx-background-color:#070e1c;");
        Label title = new Label(ex==null?"+ Add Department":"✎  Edit Department");
        title.setStyle("-fx-text-fill:"+BLUE+";-fx-font-size:14px;-fx-font-weight:bold;");
        GridPane grid = new GridPane(); grid.setHgap(14); grid.setVgap(11);
        TextField nF = fi("Department Name", ex==null?"":nv(ex.getName()));
        TextField cF = fi("Code e.g. CSE",   ex==null?"":nv(ex.getCode()));
        TextField fF = fi("Faculty/School",  ex==null?"":nv(ex.getFaculty()));
        TextField hF = fi("Head of Dept",    ex==null?"":nv(ex.getHeadName()));
        row(grid,0,"Name",nF); row(grid,1,"Code",cF);
        row(grid,2,"Faculty",fF); row(grid,3,"Head",hF);
        Button save = pBtn("✓  Save"); Button cancel = gBtn("Cancel");
        cancel.setOnAction(e -> dlg.close());
        save.setOnAction(e -> {
            try {
                Department d = ex==null?new Department():ex;
                d.setName(nF.getText()); d.setCode(cF.getText());
                d.setFaculty(fF.getText()); d.setHeadName(hF.getText());
                if(ex==null) DAOFactory.dept().add(d); else DAOFactory.dept().update(d);
                t.setItems(DAOFactory.dept().getAll()); dlg.close();
            } catch(SQLException ex2){err(ex2.getMessage());}
        });
        form.getChildren().addAll(title, grid, new HBox(10,save,cancel));
        dlg.setScene(new Scene(form,440,280)); dlg.showAndWait();
    }

    private void showSalary() {
        setTitle("Salary & Payroll","// BDT");
        VBox screen = new VBox(16); screen.setPadding(new Insets(22,26,22,26));
        screen.setStyle("-fx-background-color:"+BG+";");
        TableView<Employee> t = new TableView<>();
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); styleTable(t);
        TableColumn<Employee,Void> upd = new TableColumn<>("UPDATE"); upd.setMinWidth(120);
        upd.setCellFactory(c -> new TableCell<>() {
            final Button btn = aBtn("✎ Update", BLUE);
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v,empty); if(empty){setGraphic(null);return;}
                Employee emp = getTableView().getItems().get(getIndex());
                btn.setOnAction(e -> {
                    TextInputDialog d = new TextInputDialog(String.valueOf((long)emp.getSalary()));
                    d.setTitle("Update Salary"); d.setHeaderText(emp.getName());
                    d.setContentText("New Salary (BDT):");
                    d.showAndWait().ifPresent(val -> {
                        try { emp.setSalary(Double.parseDouble(val));
                            DAOFactory.emp().update(emp); getTableView().refresh();
                        } catch(Exception ex){err(ex.getMessage());}
                    });
                });
                setGraphic(btn);
            }
        });
        t.getColumns().addAll(
            tc("EMP ID","empId",100,true), tc("Name","name",200,false),
            tc("Designation","designation",160,false), tc("Department","deptName",160,false),
            salaryCol(), upd);
        try{t.setItems(DAOFactory.emp().getAll());}catch(SQLException e){err(e.getMessage());}
        VBox card = tCard("Salary Overview", new HBox(), t);
        VBox.setVgrow(t,Priority.ALWAYS); VBox.setVgrow(card,Priority.ALWAYS);
        screen.getChildren().add(card); show(scroll(screen));
    }

    private void showAttendance() {
        setTitle("Attendance","// Daily — "+LocalDate.now());
        VBox screen = new VBox(16); screen.setPadding(new Insets(22,26,22,26));
        screen.setStyle("-fx-background-color:"+BG+";");
        HBox bar = new HBox(14); bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(14,18,14,18));
        bar.setStyle("-fx-background-color:"+CARD+";-fx-border-color:"+BORDER+
            ";-fx-border-radius:10;-fx-background-radius:10;");
        Label dl = new Label("📅  Date: "+LocalDate.now()+"   |   Mark daily attendance");
        dl.setStyle("-fx-text-fill:"+TXTP+";-fx-font-size:13px;-fx-font-weight:bold;");
        HBox.setHgrow(dl, Priority.ALWAYS); bar.getChildren().add(dl);
        TableView<Employee> t = new TableView<>();
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); styleTable(t);
        TableColumn<Employee,Void> attCol = new TableColumn<>("MARK ATTENDANCE");
        attCol.setMinWidth(320);
        attCol.setCellFactory(c -> new TableCell<>() {
            final Button p2 = attBtn("✓ Present",GREEN);
            final Button ab = attBtn("✕ Absent", RED);
            final Button lt = attBtn("⏱ Late",   AMBER);
            final Button hd = attBtn("½ Half",   PURPLE);
            final HBox box = new HBox(6,p2,ab,lt,hd);
            { box.setAlignment(Pos.CENTER_LEFT); }
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v,empty); if(empty){setGraphic(null);return;}
                Button[] btns = {p2,ab,lt,hd};
                for(Button b:btns) b.setOnAction(e -> {
                    for(Button x:btns) x.setOpacity(0.4); b.setOpacity(1.0);
                });
                setGraphic(box);
            }
        });
        t.getColumns().addAll(
            tc("EMP ID","empId",90,true), tc("Name","name",200,false),
            tc("Designation","designation",150,false),
            tc("Department","deptName",150,false), attCol);
        try{t.setItems(DAOFactory.emp().getAll());}catch(SQLException e){err(e.getMessage());}
        VBox card = tCard("Daily Attendance — "+LocalDate.now(), new HBox(), t);
        VBox.setVgrow(t,Priority.ALWAYS); VBox.setVgrow(card,Priority.ALWAYS);
        screen.getChildren().addAll(bar, card); show(scroll(screen));
    }

    private Button attBtn(String text, String color) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:rgba("+hexRgb(color)+",0.15);-fx-text-fill:"+color+
            ";-fx-font-size:11px;-fx-padding:5 9;-fx-background-radius:6;-fx-cursor:hand;");
        b.setOpacity(0.4); return b;
    }

    private void showLeave() {
        setTitle("Leave Requests","// Approval");
        VBox screen = new VBox(16); screen.setPadding(new Insets(22,26,22,26));
        screen.setStyle("-fx-background-color:"+BG+";");
        Button addBtn = pBtn("+ New Request");
        HBox toolbar = new HBox(addBtn); toolbar.setAlignment(Pos.CENTER_RIGHT);
        TableView<LeaveRequest> t = new TableView<>();
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); styleTable(t);

        TableColumn<LeaveRequest,String> stCol = new TableColumn<>("STATUS"); stCol.setMaxWidth(120);
        stCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        stCol.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String v, boolean empty) {
                super.updateItem(v,empty); if(empty||v==null){setGraphic(null);return;}
                Label b = new Label(v);
                b.setStyle(switch(v) {
                    case "APPROVED" -> "-fx-background-color:rgba(16,185,129,0.15);-fx-text-fill:"+GREEN+";-fx-background-radius:20;-fx-padding:3 10;-fx-font-size:11px;";
                    case "REJECTED" -> "-fx-background-color:rgba(239,68,68,0.15);-fx-text-fill:"+RED+";-fx-background-radius:20;-fx-padding:3 10;-fx-font-size:11px;";
                    default         -> "-fx-background-color:rgba(245,158,11,0.15);-fx-text-fill:"+AMBER+";-fx-background-radius:20;-fx-padding:3 10;-fx-font-size:11px;";
                });
                setGraphic(b); setText(null);
            }
        });

        TableColumn<LeaveRequest,Void> act = new TableColumn<>("ACTIONS"); act.setMinWidth(200);
        act.setCellFactory(c -> new TableCell<>() {
            final Button ap = aBtn("✓ Approve",GREEN);
            final Button rj = aBtn("✕ Reject", RED);
            final Button dl = aBtn("Del",       TXTM);
            final HBox box = new HBox(6,ap,rj,dl); { box.setAlignment(Pos.CENTER); }
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v,empty); if(empty){setGraphic(null);return;}
                LeaveRequest lr = getTableView().getItems().get(getIndex());
                ap.setOnAction(e -> { try{DAOFactory.leave().updateStatus(lr.getId(),"APPROVED"); t.setItems(DAOFactory.leave().getAll());}catch(SQLException ex){err(ex.getMessage());}});
                rj.setOnAction(e -> { try{DAOFactory.leave().updateStatus(lr.getId(),"REJECTED"); t.setItems(DAOFactory.leave().getAll());}catch(SQLException ex){err(ex.getMessage());}});
                dl.setOnAction(e -> { if(confirm("Delete request?")){try{DAOFactory.leave().delete(lr.getId());t.getItems().remove(lr);}catch(SQLException ex){err(ex.getMessage());}}});
                setGraphic(box);
            }
        });

        TableColumn<LeaveRequest,Integer> daysCol = new TableColumn<>("DAYS"); daysCol.setMaxWidth(70);
        daysCol.setCellValueFactory(new PropertyValueFactory<>("days"));
        daysCol.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(Integer v, boolean empty) {
                super.updateItem(v,empty); setText(empty||v==null?null:v+"d");
                setStyle("-fx-text-fill:"+AMBER+";-fx-font-family:'Consolas';-fx-padding:10 12;");
            }
        });

        t.getColumns().addAll(
            tc("Employee","empName",160,false), tc("Type","leaveType",100,true),
            tc("From","fromDate",100,true), tc("To","toDate",100,true),
            daysCol, stCol, act);
        try{t.setItems(DAOFactory.leave().getAll());}catch(SQLException e){err(e.getMessage());}
        addBtn.setOnAction(e -> showLeaveForm(t));
        VBox card = tCard("Leave Requests", toolbar, t);
        VBox.setVgrow(t,Priority.ALWAYS); VBox.setVgrow(card,Priority.ALWAYS);
        screen.getChildren().add(card); show(scroll(screen));
    }

    private void showLeaveForm(TableView<LeaveRequest> t) {
        Stage dlg = dlg("New Leave Request",460,370);
        VBox form = new VBox(14); form.setPadding(new Insets(22));
        form.setStyle("-fx-background-color:#070e1c;");
        Label title = new Label("+ New Leave Request");
        title.setStyle("-fx-text-fill:"+BLUE+";-fx-font-size:14px;-fx-font-weight:bold;");
        GridPane grid = new GridPane(); grid.setHgap(14); grid.setVgap(11);
        ComboBox<String> empC = new ComboBox<>(); empC.setStyle(inp()+"-fx-min-width:260px;");
        try{DAOFactory.emp().getAll().forEach(e->empC.getItems().add(e.getId()+" - "+e.getName()));}
        catch(SQLException e){err(e.getMessage());}
        ComboBox<String> typeC = cbo(new String[]{"CASUAL","MEDICAL","ANNUAL","MATERNITY","STUDY"},"CASUAL");
        TextField fromF = fi("YYYY-MM-DD",""); TextField toF = fi("YYYY-MM-DD","");
        TextField daysF = fi("Days","");       TextField reasonF = fi("Reason","");
        row(grid,0,"Employee",empC); row(grid,1,"Leave Type",typeC);
        row(grid,2,"From",fromF);    row(grid,3,"To",toF);
        row(grid,4,"Days",daysF);    row(grid,5,"Reason",reasonF);
        Button save = pBtn("✓  Submit"); Button cancel = gBtn("Cancel");
        cancel.setOnAction(e -> dlg.close());
        save.setOnAction(e -> {
            try {
                LeaveRequest lr = new LeaveRequest();
                if(empC.getValue()!=null) lr.setEmployeeId(Integer.parseInt(empC.getValue().split(" - ")[0]));
                lr.setLeaveType(typeC.getValue()); lr.setFromDate(fromF.getText());
                lr.setToDate(toF.getText());
                lr.setDays(daysF.getText().isBlank()?1:Integer.parseInt(daysF.getText()));
                lr.setReason(reasonF.getText());
                DAOFactory.leave().add(lr); t.setItems(DAOFactory.leave().getAll()); dlg.close();
            } catch(SQLException ex){err(ex.getMessage());}
        });
        form.getChildren().addAll(title, grid, new HBox(10,save,cancel));
        dlg.setScene(new Scene(form,460,380)); dlg.showAndWait();
    }

    private void showBus() {
        setTitle("Bus Schedule","// PUB Transport");
        VBox screen = new VBox(16); screen.setPadding(new Insets(22,26,22,26));
        screen.setStyle("-fx-background-color:"+BG+";");
        Button addBtn = pBtn("+ Add Route");
        HBox toolbar = new HBox(addBtn); toolbar.setAlignment(Pos.CENTER_RIGHT);
        TableView<BusSchedule> t = new TableView<>();
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); styleTable(t);

        TableColumn<BusSchedule,String> stCol = new TableColumn<>("STATUS"); stCol.setMaxWidth(100);
        stCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        stCol.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String v, boolean empty) {
                super.updateItem(v,empty); if(empty||v==null){setGraphic(null);return;}
                Label b = new Label(v);
                b.setStyle("ACTIVE".equals(v)
                    ? "-fx-background-color:rgba(16,185,129,0.15);-fx-text-fill:"+GREEN+";-fx-background-radius:20;-fx-padding:3 10;-fx-font-size:11px;"
                    : "-fx-background-color:rgba(239,68,68,0.15);-fx-text-fill:"+RED+";-fx-background-radius:20;-fx-padding:3 10;-fx-font-size:11px;");
                setGraphic(b); setText(null);
            }
        });

        TableColumn<BusSchedule,Integer> capCol = new TableColumn<>("SEATS"); capCol.setMaxWidth(80);
        capCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        capCol.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(Integer v, boolean empty) {
                super.updateItem(v,empty); setText(empty||v==null?null:v+"");
                setStyle("-fx-text-fill:"+PURPLE+";-fx-font-family:'Consolas';-fx-padding:10 12;");
            }
        });

        TableColumn<BusSchedule,Void> act = new TableColumn<>("ACTIONS"); act.setMaxWidth(140);
        act.setCellFactory(c -> new TableCell<>() {
            final Button e2 = aBtn("✎ Edit",BLUE); final Button d = aBtn("✕ Del",RED);
            final HBox box = new HBox(6,e2,d); { box.setAlignment(Pos.CENTER); }
            @Override protected void updateItem(Void v, boolean empty) {
                super.updateItem(v,empty); if(empty){setGraphic(null);return;}
                BusSchedule bus = getTableView().getItems().get(getIndex());
                e2.setOnAction(ev -> showBusForm(bus,t));
                d.setOnAction(ev -> { if(confirm("Delete route "+bus.getRouteName()+"?")){
                    try{DAOFactory.bus().delete(bus.getId());t.getItems().remove(bus);}
                    catch(SQLException ex){err(ex.getMessage());}}});
                setGraphic(box);
            }
        });

        t.getColumns().addAll(
            tc("Bus No","busNo",80,true), tc("Route","routeName",175,false),
            tc("From","fromLoc",130,false), tc("Depart","departTime",90,true),
            tc("Arrive","arriveTime",90,true), tc("Days","days",80,false),
            capCol, tc("Driver","driverName",130,false), stCol, act);
        try{t.setItems(DAOFactory.bus().getAll());}catch(SQLException e){err(e.getMessage());}
        addBtn.setOnAction(e -> showBusForm(null,t));
        VBox card = tCard("PUB Bus Schedule", toolbar, t);
        VBox.setVgrow(t,Priority.ALWAYS); VBox.setVgrow(card,Priority.ALWAYS);
        screen.getChildren().add(card); show(scroll(screen));
    }

    private void showBusForm(BusSchedule ex, TableView<BusSchedule> t) {
        Stage dlg = dlg(ex==null?"Add Route":"Edit Route",460,480);
        VBox form = new VBox(14); form.setPadding(new Insets(22));
        form.setStyle("-fx-background-color:#070e1c;");
        Label title = new Label("🚌 "+(ex==null?"Add Bus Route":"Edit Bus Route"));
        title.setStyle("-fx-text-fill:"+BLUE+";-fx-font-size:14px;-fx-font-weight:bold;");
        GridPane grid = new GridPane(); grid.setHgap(14); grid.setVgap(11);
        TextField busNoF = fi("PUB-01",     ex==null?"":nv(ex.getBusNo()));
        TextField routeF = fi("Route Name", ex==null?"":nv(ex.getRouteName()));
        TextField fromF  = fi("From",       ex==null?"":nv(ex.getFromLoc()));
        TextField toF    = fi("PUB Campus", ex==null?"PUB Campus":nv(ex.getToLoc()));
        TextField depF   = fi("HH:MM:SS",   ex==null?"":nv(ex.getDepartTime()));
        TextField arrF   = fi("08:00:00",   ex==null?"08:00:00":nv(ex.getArriveTime()));
        TextField daysF  = fi("Sun-Thu",    ex==null?"Sun-Thu":nv(ex.getDays()));
        TextField capF   = fi("40",         ex==null?"40":String.valueOf(ex.getCapacity()));
        TextField drvF   = fi("Driver",     ex==null?"":nv(ex.getDriverName()));
        TextField dphF   = fi("Phone",      ex==null?"":nv(ex.getDriverPhone()));
        ComboBox<String> stC = cbo(new String[]{"ACTIVE","INACTIVE"}, ex==null?"ACTIVE":ex.getStatus());
        int r=0;
        row(grid,r++,"Bus No",busNoF);  row(grid,r++,"Route",routeF);
        row(grid,r++,"From",fromF);     row(grid,r++,"To",toF);
        row(grid,r++,"Departure",depF); row(grid,r++,"Arrival",arrF);
        row(grid,r++,"Days",daysF);     row(grid,r++,"Capacity",capF);
        row(grid,r++,"Driver",drvF);    row(grid,r++,"Phone",dphF);
        row(grid,r++,"Status",stC);
        Button save = pBtn("✓  Save"); Button cancel = gBtn("Cancel");
        cancel.setOnAction(e -> dlg.close());
        save.setOnAction(e -> {
            try {
                BusSchedule b = ex==null?new BusSchedule():ex;
                b.setBusNo(busNoF.getText()); b.setRouteName(routeF.getText());
                b.setFromLoc(fromF.getText()); b.setToLoc(toF.getText());
                b.setDepartTime(depF.getText()); b.setArriveTime(arrF.getText());
                b.setDays(daysF.getText());
                b.setCapacity(capF.getText().isBlank()?40:Integer.parseInt(capF.getText()));
                b.setDriverName(drvF.getText()); b.setDriverPhone(dphF.getText());
                b.setStatus(stC.getValue());
                if(ex==null) DAOFactory.bus().add(b); else DAOFactory.bus().update(b);
                t.setItems(DAOFactory.bus().getAll()); dlg.close();
            } catch(SQLException ex2){err(ex2.getMessage());}
        });
        ScrollPane sp = new ScrollPane(grid); sp.setFitToWidth(true);
        sp.setStyle("-fx-background:transparent;-fx-background-color:transparent;");
        form.getChildren().addAll(title, sp, new HBox(10,save,cancel));
        dlg.setScene(new Scene(form,460,490)); dlg.showAndWait();
    }

    // ── TABLE HELPERS ────────────────────────────────────────────
    private TableColumn<Employee,String> statusCol() {
        TableColumn<Employee,String> col = new TableColumn<>("STATUS"); col.setMaxWidth(110);
        col.setCellValueFactory(new PropertyValueFactory<>("status"));
        col.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(String v, boolean empty) {
                super.updateItem(v,empty); if(empty||v==null){setGraphic(null);return;}
                Label b = new Label(v);
                b.setStyle(switch(v) {
                    case "ACTIVE"   -> "-fx-background-color:rgba(16,185,129,0.15);-fx-text-fill:"+GREEN+";-fx-background-radius:20;-fx-padding:3 9;-fx-font-size:10px;";
                    case "RETIRED"  -> "-fx-background-color:rgba(124,58,237,0.15);-fx-text-fill:"+PURPLE+";-fx-background-radius:20;-fx-padding:3 9;-fx-font-size:10px;";
                    case "INACTIVE" -> "-fx-background-color:rgba(239,68,68,0.15);-fx-text-fill:"+RED+";-fx-background-radius:20;-fx-padding:3 9;-fx-font-size:10px;";
                    default         -> "-fx-background-color:rgba(245,158,11,0.15);-fx-text-fill:"+AMBER+";-fx-background-radius:20;-fx-padding:3 9;-fx-font-size:10px;";
                });
                setGraphic(b); setText(null);
            }
        });
        return col;
    }

    private TableColumn<Employee,Double> salaryCol() {
        TableColumn<Employee,Double> col = new TableColumn<>("SALARY (BDT)"); col.setMinWidth(130);
        col.setCellValueFactory(new PropertyValueFactory<>("salary"));
        col.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(Double v, boolean empty) {
                super.updateItem(v,empty); if(empty||v==null){setText(null);return;}
                setText(String.format("৳%,.0f", v));
                setStyle(v>=80000
                    ? "-fx-text-fill:"+TEAL+";-fx-font-family:'Consolas';-fx-font-weight:bold;-fx-padding:10 12;"
                    : v>=50000
                    ? "-fx-text-fill:"+AMBER+";-fx-font-family:'Consolas';-fx-padding:10 12;"
                    : "-fx-text-fill:"+TXTS+";-fx-font-family:'Consolas';-fx-padding:10 12;");
            }
        });
        return col;
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    private void styleTable(TableView t) {
        t.setStyle("-fx-background-color:transparent;-fx-border-color:transparent;");
        t.setRowFactory(tv -> {
            TableRow row = new TableRow();
            String base = "-fx-background-color:transparent;-fx-border-color:transparent transparent "
                + BORDER + " transparent;-fx-border-width:0 0 1 0;";
            row.setStyle(base);
            row.setOnMouseEntered(e -> { if(!row.isEmpty()) row.setStyle("-fx-background-color:rgba(37,99,235,0.06);"); });
            row.setOnMouseExited(e  -> row.setStyle(base));
            return row;
        });
    }

    private VBox tCard(String title, HBox toolbar, TableView<?> table) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color:"+CARD+";-fx-border-color:"+BORDER+
            ";-fx-border-radius:12;-fx-background-radius:12;");
        HBox hdr = new HBox(12); hdr.setPadding(new Insets(13,16,13,16));
        hdr.setAlignment(Pos.CENTER_LEFT);
        hdr.setStyle("-fx-background-color:rgba(37,99,235,0.14);-fx-border-color:"+BORDER+
            ";-fx-border-width:0 0 1 0;-fx-background-radius:12 12 0 0;");
        Label tl = new Label(title);
        tl.setStyle("-fx-text-fill:#ffffff;-fx-font-size:14px;-fx-font-weight:bold;");
        HBox.setHgrow(tl, Priority.ALWAYS);
        hdr.getChildren().addAll(tl, toolbar);
        card.getChildren().addAll(hdr, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return card;
    }

    // ── UI HELPERS ───────────────────────────────────────────────
    private Button aBtn(String text, String color) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:rgba("+hexRgb(color)+",0.14);-fx-text-fill:"+color+
            ";-fx-font-size:11px;-fx-font-weight:bold;-fx-padding:5 11;" +
            "-fx-background-radius:7;-fx-border-color:rgba("+hexRgb(color)+",0.4);" +
            "-fx-border-radius:7;-fx-cursor:hand;");
        return b;
    }

    private Button pBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:linear-gradient(to right,#1d4ed8,#2563eb);" +
            "-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:13px;" +
            "-fx-padding:9 18;-fx-background-radius:9;-fx-cursor:hand;" +
            "-fx-effect:dropshadow(gaussian,rgba(37,99,235,0.4),12,0,0,3);");
        return b;
    }

    private Button gBtn(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:rgba(255,255,255,0.05);-fx-text-fill:"+TXTS+
            ";-fx-font-size:13px;-fx-padding:9 18;-fx-background-radius:9;" +
            "-fx-border-color:"+BORDER+";-fx-border-radius:9;-fx-cursor:hand;");
        return b;
    }

    private TextField fi(String prompt, String val) {
        TextField tf = new TextField(val); tf.setPromptText(prompt); tf.setStyle(inp()); return tf;
    }

    private ComboBox<String> cbo(String[] items, String val) {
        ComboBox<String> cb = new ComboBox<>(FXCollections.observableArrayList(items));
        cb.setValue(val); cb.setStyle(inp()+"-fx-min-width:260px;"); return cb;
    }

    private void row(GridPane g, int row, String lbl, Control field) {
        Label l = new Label(lbl);
        l.setStyle("-fx-text-fill:"+TXTS+";-fx-font-size:11px;"); l.setMinWidth(110);
        g.add(l,0,row); g.add(field,1,row); GridPane.setHgrow(field, Priority.ALWAYS);
    }

    private String inp() {
        return "-fx-background-color:#0a1628;-fx-border-color:"+BORDER+
            ";-fx-border-radius:9;-fx-background-radius:9;-fx-text-fill:"+TXTP+
            ";-fx-prompt-text-fill:"+TXTM+";-fx-padding:9 13;-fx-font-size:13px;";
    }

    private Stage dlg(String title, int w, int h) {
        Stage s = new Stage(); s.initModality(Modality.APPLICATION_MODAL);
        s.setTitle(title); s.setResizable(false); return s;
    }

    private ScrollPane scroll(VBox c) {
        ScrollPane sp = new ScrollPane(c); sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color:"+BG+";-fx-background:"+BG+";"); return sp;
    }

    private void show(Node n) {
        n.setOpacity(0); content.getChildren().setAll(n);
        FadeTransition ft = new FadeTransition(Duration.millis(200), n);
        ft.setFromValue(0); ft.setToValue(1); ft.play();
    }

    private boolean confirm(String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText(msg); a.setContentText("This cannot be undone.");
        return a.showAndWait().map(r -> r == ButtonType.OK).orElse(false);
    }

    private void err(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText("Error"); a.setContentText(msg); a.showAndWait();
    }

    private String nv(String s) { return s==null?"":s; }

    private String hexRgb(String hex) {
        int r = Integer.parseInt(hex.substring(1,3),16);
        int g = Integer.parseInt(hex.substring(3,5),16);
        int b = Integer.parseInt(hex.substring(5,7),16);
        return r+","+g+","+b;
    }

    @Override public void stop() { DB.close(); }
    public static void main(String[] args) { launch(args); }
}