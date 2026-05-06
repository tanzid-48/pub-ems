package com.PUB;

import com.PUB.dao.DAOFactory;
import com.PUB.model.Employee;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardPanel {

    // ── Theme-aware colours ───────────────────────────────────────
    String BG()     { return Main.DARK ? "#06101e" : "#eef2f7"; }
    String CARD()   { return Main.DARK ? "#0a1628" : "#ffffff"; }
    String BORDER() { return Main.DARK ? "#111f38" : "#c5d8ec"; }
    String TXTP()   { return Main.DARK ? "#e2e8f0" : "#0d2137"; }
    String TXTS()   { return Main.DARK ? "#7a9cc0" : "#3a6080"; }
    String TXTM()   { return Main.DARK ? "#2a4a70" : "#8faabb"; }

    // ── Accent colours ────────────────────────────────────────────
    static final String TEAL   = "#00b4a6";
    static final String TEAL2  = "#00d4c4";
    static final String AMBER  = "#f5a623";
    static final String RED    = "#e74c3c";
    static final String GREEN  = "#27ae60";
    static final String BLUE   = "#2563eb";
    static final String PURPLE = "#7c3aed";

    // ── Simulated data ────────────────────────────────────────────
    static final String[] LEAVE_TYPES  = {"CASUAL","MEDICAL","ANNUAL","MATERNITY","STUDY"};
    static final int[]    LEAVE_COUNTS = {18, 9, 6, 3, 4};
    static final int[]    LEAVE_STATUS = {12, 22, 6};
    static final String[] ATT_MONTHS   = {"Nov","Dec","Jan","Feb","Mar","Apr"};
    static final int[][]  ATT_DATA     = {
        {720,45,30,25},{735,38,28,19},{748,42,22,18},
        {760,35,20,15},{752,40,25,13},{770,30,18,12}
    };
    static final String[][] BUSES = {
        {"PUB-01","Bogura City","07:30","45","ACTIVE"},
        {"PUB-02","Sherpur",    "07:00","40","ACTIVE"},
        {"PUB-03","Gabtali",    "07:15","40","ACTIVE"},
        {"PUB-04","Dhunat",     "06:45","35","ACTIVE"},
        {"PUB-05","Sariakandi", "06:30","35","INACTIVE"},
    };

    // ── State ─────────────────────────────────────────────────────
    private String filterType   = "All";
    private String filterDept   = "All";
    private String filterStatus = "All";
    private List<Employee> allEmps  = new ArrayList<>();
    private List<Employee> filtered = new ArrayList<>();

    // ── Canvas refs ───────────────────────────────────────────────
    private Canvas kpiCanvas,c1,c2,c3,c4,c5,c6,c7,c8,insightCanvas;

    // ── Build ─────────────────────────────────────────────────────
    public ScrollPane build() {
        try {
            allEmps  = new ArrayList<>(DAOFactory.emp().getAll());
            filtered = new ArrayList<>(allEmps);
        } catch (SQLException e) { e.printStackTrace(); }

        VBox root = new VBox(0);
        root.setStyle("-fx-background-color:" + BG() + ";");
        root.getChildren().addAll(
            buildFilterBar(),
            buildKpiBar(),
            buildChartsGrid(),
            buildInsightsPanel()
        );

        ScrollPane sp = new ScrollPane(root);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background:" + BG() + ";-fx-background-color:" + BG() + ";");
        return sp;
    }

    // ── Filter Bar ────────────────────────────────────────────────
    private HBox buildFilterBar() {
        HBox bar = new HBox(12);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(12, 20, 12, 20));
        bar.setStyle(
            "-fx-background-color:" + CARD() + ";" +
            "-fx-border-color:" + BORDER() + ";-fx-border-width:0 0 1 0;"
        );

        Label lbl = new Label("🔽  FILTERS");
        lbl.setStyle(
            "-fx-text-fill:" + TEAL + ";" +
            "-fx-font-size:12px;-fx-font-weight:bold;" +
            "-fx-font-family:'Consolas';"
        );

        ComboBox<String> typeBox   = fCombo("Emp Type",   new String[]{"All","FACULTY","STAFF","ADMIN"});
        ComboBox<String> deptBox   = fCombo("Department", new String[]{"All","CSE","EEE","Civil","BBA","IS","English","Law","Bangla","Education","Administration"});
        ComboBox<String> statusBox = fCombo("Status",     new String[]{"All","ACTIVE","INACTIVE","ON_LEAVE","RETIRED"});

        Button resetBtn = new Button("↺  Reset");
        resetBtn.setStyle(
            "-fx-background-color:rgba(239,68,68,0.12);" +
            "-fx-text-fill:" + RED + ";" +
            "-fx-font-size:12px;-fx-font-weight:bold;" +
            "-fx-padding:7 16;-fx-background-radius:8;-fx-cursor:hand;" +
            "-fx-border-color:rgba(239,68,68,0.3);-fx-border-radius:8;"
        );

        Label countLbl = new Label();
        countLbl.setStyle(
            "-fx-text-fill:" + TEAL + ";" +
            "-fx-font-size:13px;-fx-font-weight:bold;" +
            "-fx-font-family:'Consolas';"
        );
        updateCount(countLbl);

        typeBox.setOnAction(e   -> { filterType   = typeBox.getValue();   applyFilter(); updateCount(countLbl); });
        deptBox.setOnAction(e   -> { filterDept   = deptBox.getValue();   applyFilter(); updateCount(countLbl); });
        statusBox.setOnAction(e -> { filterStatus = statusBox.getValue(); applyFilter(); updateCount(countLbl); });
        resetBtn.setOnAction(e  -> {
            filterType="All"; filterDept="All"; filterStatus="All";
            typeBox.setValue("All"); deptBox.setValue("All"); statusBox.setValue("All");
            applyFilter(); updateCount(countLbl);
        });

        Pane spacer = new Pane(); HBox.setHgrow(spacer, Priority.ALWAYS);

        // Theme indicator
        Label themeTag = new Label(Main.DARK ? "🌙 Dark Mode" : "☀ Light Mode");
        themeTag.setStyle(
            "-fx-background-color:rgba(0,180,166,0.10);" +
            "-fx-text-fill:" + TEAL + ";" +
            "-fx-font-size:11px;-fx-padding:5 12;" +
            "-fx-background-radius:20;" +
            "-fx-border-color:rgba(0,180,166,0.25);-fx-border-radius:20;"
        );

        bar.getChildren().addAll(lbl, typeBox, deptBox, statusBox, resetBtn, spacer, themeTag, countLbl);
        return bar;
    }

    private void updateCount(Label l) {
        l.setText("  Showing " + filtered.size() + " / " + allEmps.size() + " employees");
    }

    private ComboBox<String> fCombo(String prompt, String[] items) {
        ComboBox<String> cb = new ComboBox<>();
        cb.getItems().addAll(items);
        cb.setValue("All");
        cb.setPromptText(prompt);
        cb.setStyle(
            "-fx-background-color:" + CARD() + ";" +
            "-fx-border-color:" + BORDER() + ";" +
            "-fx-border-radius:8;-fx-background-radius:8;" +
            "-fx-text-fill:" + TXTP() + ";" +
            "-fx-font-size:13px;-fx-min-width:145px;"
        );
        return cb;
    }

    private void applyFilter() {
        filtered = allEmps.stream()
            .filter(e -> filterType.equals("All")   || e.getEmpType().equals(filterType))
            .filter(e -> filterDept.equals("All")   || (e.getDeptName()!=null && e.getDeptName().contains(filterDept)))
            .filter(e -> filterStatus.equals("All") || e.getStatus().equals(filterStatus))
            .collect(Collectors.toList());
        redrawAll();
    }

    private void redrawAll() {
        draw(kpiCanvas,     this::drawKpi);
        draw(c1,            this::drawChart1);
        draw(c2,            this::drawChart2);
        draw(c3,            this::drawChart3);
        draw(c4,            this::drawChart4);
        draw(c5,            this::drawChart5);
        draw(c6,            this::drawChart6);
        draw(c7,            this::drawChart7);
        draw(c8,            this::drawChart8);
        draw(insightCanvas, this::drawInsights);
    }

    @FunctionalInterface interface Drawer { void draw(GraphicsContext g, double w, double h); }

    private void draw(Canvas cv, Drawer d) {
        if (cv != null) d.draw(cv.getGraphicsContext2D(), cv.getWidth(), cv.getHeight());
    }

    // ── KPI Bar ───────────────────────────────────────────────────
    private VBox buildKpiBar() {
        kpiCanvas = new Canvas(1400, 120);
        VBox wrap = new VBox(kpiCanvas);
        wrap.setPadding(new Insets(16, 20, 8, 20));
        wrap.setStyle("-fx-background-color:" + BG() + ";");
        wrap.widthProperty().addListener((o, old, nw) -> {
            kpiCanvas.setWidth(nw.doubleValue() - 40);
            drawKpi(kpiCanvas.getGraphicsContext2D(), kpiCanvas.getWidth(), kpiCanvas.getHeight());
        });
        drawKpi(kpiCanvas.getGraphicsContext2D(), kpiCanvas.getWidth(), kpiCanvas.getHeight());
        return wrap;
    }

    private void drawKpi(GraphicsContext g, double W, double H) {
        g.clearRect(0, 0, W, H);
        long total   = filtered.size();
        long faculty = filtered.stream().filter(e -> "FACULTY".equals(e.getEmpType())).count();
        long staff   = filtered.stream().filter(e -> !"FACULTY".equals(e.getEmpType())).count();
        long active  = filtered.stream().filter(e -> "ACTIVE".equals(e.getStatus())).count();
        long depts   = filtered.stream().map(Employee::getDeptName).filter(Objects::nonNull).distinct().count();
        long payroll = filtered.stream().mapToLong(e -> (long)e.getSalary()).sum();

        String[] icons  = {"👥","🎓","🏢","✅","🏛","💰"};
        String[] labels = {"Total Employees","Faculty Members","Staff & Admin","Active Employees","Departments","Monthly Payroll"};
        String[] values = {str(total),str(faculty),str(staff),str(active),str(depts),"৳"+fmt(payroll)};
        String[] colors = {TEAL,BLUE,PURPLE,GREEN,AMBER,TEAL};

        double gap  = 12;
        double cardW = (W - gap * 7) / 6.0;
        double cardH = H - 4;

        for (int i = 0; i < 6; i++) {
            double x = gap + i * (cardW + gap);

            // Card shadow
            g.setFill(Color.web("#000000", 0.15));
            g.fillRoundRect(x+2, 4, cardW, cardH, 12, 12);

            // Card bg
            g.setFill(Color.web(CARD()));
            rRect(g, x, 2, cardW, cardH, 12); g.fill();

            // Left accent
            g.setFill(Color.web(colors[i]));
            g.fillRoundRect(x, 2, 5, cardH, 4, 4);

            // Top teal line
            g.setFill(Color.web(colors[i], 0.15));
            g.fillRect(x+5, 2, cardW-5, 3);

            // Icon
            g.setFont(Font.font("System", 20));
            g.setFill(Color.web(colors[i], 0.8));
            g.fillText(icons[i], x + 15, 32);

            // Value
            g.setFont(Font.font("Consolas", FontWeight.BOLD, 26));
            g.setFill(Color.web(colors[i]));
            double vw = tw(g, values[i]);
            g.fillText(values[i], x + cardW/2 - vw/2, 70);

            // Label
            g.setFont(Font.font("System", 12));
            g.setFill(Color.web(TXTS()));
            double lw = tw(g, labels[i]);
            g.fillText(labels[i], x + cardW/2 - lw/2, 92);

            // Bottom border
            g.setStroke(Color.web(BORDER()));
            g.setLineWidth(1);
            g.strokeRoundRect(x, 2, cardW, cardH, 12, 12);
        }
    }

    // ── Charts Grid ───────────────────────────────────────────────
    private GridPane buildChartsGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(14); grid.setVgap(14);
        grid.setPadding(new Insets(6, 20, 14, 20));
        grid.setStyle("-fx-background-color:" + BG() + ";");
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(50); col.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(col, col);

        grid.add(cCard("📊  Headcount by Department",       mkChart(this::drawChart1, cv -> c1=cv, 240)), 0, 0);
        grid.add(cCard("🍩  Employee Type Distribution",    mkChart(this::drawChart2, cv -> c2=cv, 240)), 1, 0);
        grid.add(cCard("💵  Salary Distribution by Band",   mkChart(this::drawChart3, cv -> c3=cv, 240)), 0, 1);
        grid.add(cCard("🏦  Department Salary Spend",       mkChart(this::drawChart4, cv -> c4=cv, 240)), 1, 1);
        grid.add(cCard("📅  Hiring Timeline by Year",       mkChart(this::drawChart5, cv -> c5=cv, 220)), 0, 2);
        grid.add(cCard("📝  Leave Requests by Type",        mkChart(this::drawChart6, cv -> c6=cv, 230)), 1, 2);
        grid.add(cCard("📋  Attendance Breakdown (6 mo.)",  mkChart(this::drawChart7, cv -> c7=cv, 240)), 0, 3);
        grid.add(cCard("🚌  Bus Route Capacity & Status",   mkChart(this::drawChart8, cv -> c8=cv, 240)), 1, 3);
        return grid;
    }

    @FunctionalInterface interface Store { void store(Canvas c); }

    private VBox mkChart(Drawer d, Store s, double h) {
        Canvas cv = new Canvas(580, h);
        s.store(cv);
        d.draw(cv.getGraphicsContext2D(), 580, h);
        VBox vb = new VBox(cv);
        vb.widthProperty().addListener((o, old, nw) -> {
            cv.setWidth(nw.doubleValue());
            d.draw(cv.getGraphicsContext2D(), cv.getWidth(), cv.getHeight());
        });
        return vb;
    }

    private VBox cCard(String title, VBox content) {
        VBox card = new VBox(0);
        card.setStyle(
            "-fx-background-color:" + CARD() + ";" +
            "-fx-border-color:" + BORDER() + ";" +
            "-fx-border-radius:12;-fx-background-radius:12;" +
            "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.12),8,0,0,2);"
        );
        HBox hdr = new HBox();
        hdr.setPadding(new Insets(11, 14, 11, 14));
        hdr.setStyle(
            "-fx-background-color:" + (Main.DARK ? "rgba(37,99,235,0.12)" : "rgba(37,99,235,0.06)") + ";" +
            "-fx-border-color:" + BORDER() + ";-fx-border-width:0 0 1 0;" +
            "-fx-background-radius:12 12 0 0;"
        );
        Label lbl = new Label(title);
        lbl.setStyle(
            "-fx-text-fill:" + TXTP() + ";" +
            "-fx-font-size:14px;-fx-font-weight:bold;"
        );
        hdr.getChildren().add(lbl);
        VBox body = new VBox(content);
        body.setPadding(new Insets(10));
        card.getChildren().addAll(hdr, body);
        return card;
    }

    // ── Chart 1: Headcount by Department ─────────────────────────
    private void drawChart1(GraphicsContext g, double W, double H) {
        g.clearRect(0, 0, W, H);
        Map<String,Long> counts = filtered.stream()
            .collect(Collectors.groupingBy(
                e -> e.getDeptName()==null?"Unknown":e.getDeptName(),
                Collectors.counting()));
        List<Map.Entry<String,Long>> sorted = counts.entrySet().stream()
            .sorted((a,b)->Long.compare(b.getValue(),a.getValue()))
            .collect(Collectors.toList());
        if (sorted.isEmpty()) { noData(g,W,H); return; }

        double maxV = sorted.stream().mapToLong(Map.Entry::getValue).max().orElse(1);
        double labW=165, padL=labW+8, padR=55, padT=10, padB=20;
        double cW=W-padL-padR;
        double barH=Math.min(22,(H-padT-padB)/sorted.size()-4);
        double step=(H-padT-padB)/sorted.size();
        String[] cols={TEAL,BLUE,AMBER,PURPLE,GREEN,RED};

        for (int i=0; i<sorted.size(); i++) {
            String dept=sorted.get(i).getKey();
            long val=sorted.get(i).getValue();
            double y=padT+i*step+(step-barH)/2;
            double blen=val/maxV*cW;
            String col=cols[i%cols.length];

            // Track
            g.setFill(Color.web(BORDER(),0.5)); g.fillRoundRect(padL,y,cW,barH,5,5);
            // Bar
            g.setFill(Color.web(col,0.85)); g.fillRoundRect(padL,y,Math.max(blen,4),barH,5,5);
            // Dept name
            g.setFont(Font.font("System",12)); g.setFill(Color.web(TXTS()));
            String dl=dept.length()>22?dept.substring(0,21)+"…":dept;
            g.fillText(dl, 0, y+barH/2+4);
            // Value
            g.setFont(Font.font("Consolas",FontWeight.BOLD,12));
            g.setFill(Color.web(col));
            g.fillText(String.valueOf(val), padL+blen+6, y+barH/2+4);
        }
    }

    // ── Chart 2: Employee Type Donut ──────────────────────────────
    private void drawChart2(GraphicsContext g, double W, double H) {
        g.clearRect(0,0,W,H);
        if (filtered.isEmpty()) { noData(g,W,H); return; }
        long fac=filtered.stream().filter(e->"FACULTY".equals(e.getEmpType())).count();
        long stf=filtered.stream().filter(e->"STAFF".equals(e.getEmpType())).count();
        long adm=filtered.stream().filter(e->"ADMIN".equals(e.getEmpType())).count();
        long total=filtered.size();
        double[] vals={fac,stf,adm};
        String[] labels={"FACULTY","STAFF","ADMIN"};
        String[] cols={TEAL,AMBER,BLUE};
        double cx=W*0.35, cy=H/2.0;
        double R=Math.min(W*0.28,H/2.0-20), r=R*0.55;
        double start=-Math.PI/2;
        for (int i=0; i<3; i++) {
            if (vals[i]==0) continue;
            double sweep=2*Math.PI*vals[i]/total;
            // Glow
            g.setFill(Color.web(cols[i],0.12));
            g.fillArc(cx-R-4,cy-R-4,(R+4)*2,(R+4)*2,Math.toDegrees(-start),-Math.toDegrees(sweep),javafx.scene.shape.ArcType.ROUND);
            // Segment
            g.setFill(Color.web(cols[i],0.85));
            g.fillArc(cx-R,cy-R,R*2,R*2,Math.toDegrees(-start),-Math.toDegrees(sweep),javafx.scene.shape.ArcType.ROUND);
            start+=sweep;
        }
        // Hole
        g.setFill(Color.web(CARD())); g.fillOval(cx-r,cy-r,r*2,r*2);
        // Centre text
        g.setFont(Font.font("Consolas",FontWeight.BOLD,24)); g.setFill(Color.web(TXTP()));
        String ts=String.valueOf(total);
        g.fillText(ts, cx-tw(g,ts)/2, cy+8);
        g.setFont(Font.font("System",11)); g.setFill(Color.web(TXTS()));
        g.fillText("Total", cx-16, cy+24);
        // Legend
        double lx=W*0.67, ly=cy-48;
        for (int i=0; i<3; i++) {
            double pct=total>0?vals[i]*100.0/total:0;
            g.setFill(Color.web(cols[i],0.85)); g.fillRoundRect(lx,ly+i*42,13,13,4,4);
            g.setFont(Font.font("Consolas",FontWeight.BOLD,16)); g.setFill(Color.web(cols[i]));
            g.fillText(String.valueOf((long)vals[i]), lx+20, ly+i*42+12);
            g.setFont(Font.font("System",12)); g.setFill(Color.web(TXTS()));
            g.fillText(labels[i]+"  "+String.format("%.0f%%",pct), lx+20, ly+i*42+27);
        }
    }

    // ── Chart 3: Salary Bands ─────────────────────────────────────
    private void drawChart3(GraphicsContext g, double W, double H) {
        g.clearRect(0,0,W,H);
        String[] bands={"৳0–40K","৳40–60K","৳60–80K","৳80K+"};
        String[] cols={TEAL,AMBER,BLUE};
        int[][] data=new int[4][3];
        for (Employee e:filtered) {
            int ti="FACULTY".equals(e.getEmpType())?0:"STAFF".equals(e.getEmpType())?1:2;
            int bi=e.getSalary()<=40000?0:e.getSalary()<=60000?1:e.getSalary()<=80000?2:3;
            data[bi][ti]++;
        }
        int maxV=Arrays.stream(data).flatMapToInt(Arrays::stream).max().orElse(1);
        double padL=40,padR=20,padT=18,padB=38;
        double cW=W-padL-padR, cH=H-padT-padB;
        double gW=cW/4, bW=gW/4.5;
        // Grid lines
        for (int yi=0;yi<=4;yi++) {
            double yv=padT+cH-yi*cH/4.0;
            g.setStroke(Color.web(BORDER(),0.5)); g.setLineWidth(1); g.strokeLine(padL,yv,padL+cW,yv);
            g.setFont(Font.font("Consolas",11)); g.setFill(Color.web(TXTS()));
            g.fillText(String.valueOf(yi*maxV/4), 0, yv+4);
        }
        for (int b=0;b<4;b++) {
            double gx=padL+b*gW+gW*0.1;
            for (int t=0;t<3;t++) {
                if (data[b][t]==0) continue;
                double bh=data[b][t]*cH/(double)maxV;
                double bx=gx+t*(bW+2), by=padT+cH-bh;
                g.setFill(Color.web(cols[t],0.85)); g.fillRoundRect(bx,by,bW,bh,4,4);
                g.setFont(Font.font("Consolas",FontWeight.BOLD,11)); g.setFill(Color.web(cols[t]));
                g.fillText(String.valueOf(data[b][t]), bx+1, by-4);
            }
            g.setFont(Font.font("Consolas",12)); g.setFill(Color.web(TXTS()));
            g.fillText(bands[b], padL+b*gW+2, padT+cH+16);
        }
        String[] tl={"FACULTY","STAFF","ADMIN"};
        for (int t=0;t<3;t++) {
            g.setFill(Color.web(cols[t])); g.fillRoundRect(padL+t*115,H-15,11,11,3,3);
            g.setFont(Font.font("System",12)); g.setFill(Color.web(TXTS()));
            g.fillText(tl[t], padL+t*115+15, H-4);
        }
    }

    // ── Chart 4: Dept Salary Spend ────────────────────────────────
    private void drawChart4(GraphicsContext g, double W, double H) {
        g.clearRect(0,0,W,H);
        Map<String,Long> spend=filtered.stream()
            .collect(Collectors.groupingBy(
                e->e.getDeptName()==null?"Unknown":e.getDeptName(),
                Collectors.summingLong(e->(long)e.getSalary())));
        List<Map.Entry<String,Long>> sorted=spend.entrySet().stream()
            .sorted((a,b)->Long.compare(b.getValue(),a.getValue()))
            .collect(Collectors.toList());
        if (sorted.isEmpty()) { noData(g,W,H); return; }
        double maxV=sorted.stream().mapToLong(Map.Entry::getValue).max().orElse(1);
        double labW=165,padL=labW+8,padR=95,padT=8,padB=8;
        double cW=W-padL-padR;
        double barH=Math.min(20,(H-padT-padB)/sorted.size()-4);
        double step=(H-padT-padB)/sorted.size();
        String[] cols={TEAL,BLUE,AMBER,PURPLE,GREEN,RED};
        for (int i=0;i<sorted.size();i++) {
            String dept=sorted.get(i).getKey(); long val=sorted.get(i).getValue();
            double y=padT+i*step+(step-barH)/2;
            double blen=val/maxV*cW; String col=cols[i%cols.length];
            g.setFill(Color.web(BORDER(),0.5)); g.fillRoundRect(padL,y,cW,barH,5,5);
            g.setFill(Color.web(col,0.85)); g.fillRoundRect(padL,y,Math.max(blen,4),barH,5,5);
            g.setFont(Font.font("System",11)); g.setFill(Color.web(TXTS()));
            String dl=dept.length()>22?dept.substring(0,21)+"…":dept;
            g.fillText(dl, 0, y+barH/2+4);
            g.setFont(Font.font("Consolas",FontWeight.BOLD,11)); g.setFill(Color.web(col));
            g.fillText("৳"+fmt(val), padL+blen+6, y+barH/2+4);
        }
    }

    // ── Chart 5: Hiring Timeline ──────────────────────────────────
    private void drawChart5(GraphicsContext g, double W, double H) {
        g.clearRect(0,0,W,H);
        Map<Integer,Long> byYear=filtered.stream()
            .filter(e->e.getJoinDate()!=null&&!e.getJoinDate().isEmpty())
            .collect(Collectors.groupingBy(e->{
                try{return Integer.parseInt(e.getJoinDate().substring(0,4));}
                catch(Exception ex){return 2020;}
            },Collectors.counting()));
        if (byYear.isEmpty()) { noData(g,W,H); return; }
        int minY=byYear.keySet().stream().mapToInt(i->i).min().orElse(2010);
        int maxY=byYear.keySet().stream().mapToInt(i->i).max().orElse(2022);
        List<Integer> years=new ArrayList<>();
        for (int y=minY;y<=maxY;y++) years.add(y);
        double maxV=byYear.values().stream().mapToLong(l->l).max().orElse(1);
        double padL=30,padR=20,padT=18,padB=32;
        double cW=W-padL-padR, cH=H-padT-padB;
        double bW=cW/years.size()*0.6, step=cW/years.size();
        for (int yi=0;yi<=4;yi++) {
            double yv=padT+cH-yi*cH/4.0;
            g.setStroke(Color.web(BORDER(),0.5)); g.setLineWidth(1); g.strokeLine(padL,yv,padL+cW,yv);
            g.setFont(Font.font("Consolas",11)); g.setFill(Color.web(TXTS()));
            g.fillText(String.valueOf((int)(yi*maxV/4)),0,yv+4);
        }
        for (int i=0;i<years.size();i++) {
            int yr=years.get(i); long cnt=byYear.getOrDefault(yr,0L);
            double bh=cnt*cH/maxV, bx=padL+i*step+(step-bW)/2, by=padT+cH-bh;
            LinearGradient grad=new LinearGradient(0,0,0,1,true,CycleMethod.NO_CYCLE,
                new Stop(0,Color.web(TEAL,0.9)),new Stop(1,Color.web(BLUE,0.6)));
            g.setFill(grad); g.fillRoundRect(bx,by,bW,bh,5,5);
            if (cnt>0) {
                g.setFont(Font.font("Consolas",FontWeight.BOLD,11));
                g.setFill(Color.web(TEAL2)); g.fillText(String.valueOf(cnt),bx+bW/4,by-4);
            }
            g.setFont(Font.font("Consolas",11)); g.setFill(Color.web(TXTS()));
            g.fillText(String.valueOf(yr), bx-2, padT+cH+16);
        }
    }

    // ── Chart 6: Leave Pie ────────────────────────────────────────
    private void drawChart6(GraphicsContext g, double W, double H) {
        g.clearRect(0,0,W,H);
        int total=Arrays.stream(LEAVE_COUNTS).sum();
        String[] cols={TEAL,GREEN,AMBER,PURPLE,BLUE};
        double cy=H*0.42, cx=W*0.27, R=Math.min(W*0.20,H*0.37);
        double start=-Math.PI/2;
        for (int i=0;i<LEAVE_TYPES.length;i++) {
            double sweep=2*Math.PI*LEAVE_COUNTS[i]/total;
            g.setFill(Color.web(cols[i],0.85));
            g.fillArc(cx-R,cy-R,R*2,R*2,Math.toDegrees(-start),-Math.toDegrees(sweep),javafx.scene.shape.ArcType.ROUND);
            start+=sweep;
        }
        g.setFill(Color.web(CARD())); g.fillOval(cx-R*0.55,cy-R*0.55,R*1.1,R*1.1);
        g.setFont(Font.font("Consolas",FontWeight.BOLD,20)); g.setFill(Color.web(TXTP()));
        g.fillText(String.valueOf(total), cx-14, cy+7);
        g.setFont(Font.font("System",11)); g.setFill(Color.web(TXTS()));
        g.fillText("Total", cx-15, cy+22);
        double lx=W*0.54, ly=8;
        for (int i=0;i<LEAVE_TYPES.length;i++) {
            double pct=LEAVE_COUNTS[i]*100.0/total;
            g.setFill(Color.web(cols[i],0.85)); g.fillRoundRect(lx,ly+i*28,11,11,3,3);
            g.setFont(Font.font("System",12)); g.setFill(Color.web(TXTS()));
            g.fillText(LEAVE_TYPES[i], lx+16, ly+i*28+11);
            g.setFont(Font.font("Consolas",FontWeight.BOLD,11)); g.setFill(Color.web(cols[i]));
            g.fillText(String.format("%d (%.0f%%)",LEAVE_COUNTS[i],pct), lx+95, ly+i*28+11);
        }
        double ty=H*0.74;
        String[] stL={"PENDING","APPROVED","REJECTED"}; String[] stC={AMBER,GREEN,RED};
        double tw2=(W-30)/3.5;
        for (int i=0;i<3;i++) {
            double tx=8+i*(tw2+8);
            g.setFill(Color.web(stC[i],0.10)); g.fillRoundRect(tx,ty,tw2,40,8,8);
            g.setStroke(Color.web(stC[i],0.35)); g.setLineWidth(1); g.strokeRoundRect(tx,ty,tw2,40,8,8);
            g.setFont(Font.font("Consolas",FontWeight.BOLD,17)); g.setFill(Color.web(stC[i]));
            g.fillText(String.valueOf(LEAVE_STATUS[i]), tx+10, ty+25);
            g.setFont(Font.font("System",11)); g.setFill(Color.web(TXTS()));
            g.fillText(stL[i], tx+10, ty+38);
        }
    }

    // ── Chart 7: Attendance Stacked ───────────────────────────────
    private void drawChart7(GraphicsContext g, double W, double H) {
        g.clearRect(0,0,W,H);
        String[] catC={GREEN,RED,AMBER,PURPLE};
        double padL=44,padR=20,padT=18,padB=38;
        double cW=W-padL-padR, cH=H-padT-padB;
        double bW=cW/ATT_MONTHS.length*0.6, step=cW/ATT_MONTHS.length;
        int maxS=0;
        for (int[] row:ATT_DATA){int s=Arrays.stream(row).sum();if(s>maxS)maxS=s;}
        for (int yi=0;yi<=4;yi++) {
            double yv=padT+cH-yi*cH/4.0;
            g.setStroke(Color.web(BORDER(),0.5)); g.setLineWidth(1); g.strokeLine(padL,yv,padL+cW,yv);
            g.setFont(Font.font("Consolas",11)); g.setFill(Color.web(TXTS()));
            g.fillText(String.valueOf(yi*maxS/4),0,yv+4);
        }
        for (int m=0;m<ATT_MONTHS.length;m++) {
            double bx=padL+m*step+(step-bW)/2, yBase=padT+cH;
            for (int c=0;c<4;c++) {
                double bh=ATT_DATA[m][c]*cH/(double)maxS, by=yBase-bh;
                g.setFill(Color.web(catC[c],0.82)); g.fillRoundRect(bx,by,bW,bh,c==3?4:0,c==3?4:0);
                yBase=by;
            }
            g.setFont(Font.font("Consolas",11)); g.setFill(Color.web(TXTS()));
            g.fillText(ATT_MONTHS[m], bx+bW/4-2, padT+cH+16);
        }
        String[] cats={"PRESENT","ABSENT","LATE","HALF_DAY"};
        for (int c=0;c<4;c++) {
            g.setFill(Color.web(catC[c])); g.fillRoundRect(padL+c*105,H-15,11,11,3,3);
            g.setFont(Font.font("System",12)); g.setFill(Color.web(TXTS()));
            g.fillText(cats[c], padL+c*105+16, H-4);
        }
    }

    // ── Chart 8: Bus Capacity ─────────────────────────────────────
    private void drawChart8(GraphicsContext g, double W, double H) {
        g.clearRect(0,0,W,H);
        double labW=155,padL=labW+8,padR=90,padT=14,padB=30;
        double cW=W-padL-padR, barH=24, maxCap=50;
        double step=(H-padT-padB)/BUSES.length;
        for (int i=0;i<BUSES.length;i++) {
            String[] bus=BUSES[i];
            double y=padT+i*step+(step-barH)/2;
            double blen=Integer.parseInt(bus[3])/maxCap*cW;
            String col="ACTIVE".equals(bus[4])?TEAL:RED;
            g.setFill(Color.web(BORDER(),0.5)); g.fillRoundRect(padL,y,cW,barH,7,7);
            g.setFill(Color.web(col,0.8)); g.fillRoundRect(padL,y,blen,barH,7,7);
            g.setFont(Font.font("Consolas",FontWeight.BOLD,13)); g.setFill(Color.web(col));
            g.fillText(bus[0], 0, y+barH/2+5);
            g.setFont(Font.font("System",12)); g.setFill(Color.web(TXTS()));
            g.fillText(bus[1], 58, y+barH/2+5);
            g.setFont(Font.font("Consolas",FontWeight.BOLD,12)); g.setFill(Color.web(col));
            g.fillText(bus[3]+" seats", padL+blen+8, y+barH/2+5);
            String badge="ACTIVE".equals(bus[4])?"✅ ACTIVE  "+bus[2]:"🔴 INACTIVE";
            g.setFont(Font.font("System",11)); g.setFill(Color.web(col));
            g.fillText(badge, padL+blen+8, y+barH/2+19);
        }
        g.setFill(Color.web(TEAL)); g.fillRoundRect(padL,H-17,11,11,3,3);
        g.setFont(Font.font("System",12)); g.setFill(Color.web(TXTS())); g.fillText("ACTIVE",padL+15,H-6);
        g.setFill(Color.web(RED)); g.fillRoundRect(padL+90,H-17,11,11,3,3);
        g.fillText("INACTIVE",padL+106,H-6);
    }

    // ── Insights Panel ────────────────────────────────────────────
    private VBox buildInsightsPanel() {
        insightCanvas = new Canvas(1400, 180);
        VBox wrap = new VBox(insightCanvas);
        wrap.setPadding(new Insets(0, 20, 24, 20));
        wrap.setStyle("-fx-background-color:" + BG() + ";");
        wrap.widthProperty().addListener((o, old, nw) -> {
            insightCanvas.setWidth(nw.doubleValue() - 40);
            drawInsights(insightCanvas.getGraphicsContext2D(), insightCanvas.getWidth(), insightCanvas.getHeight());
        });
        drawInsights(insightCanvas.getGraphicsContext2D(), insightCanvas.getWidth(), insightCanvas.getHeight());
        return wrap;
    }

    private void drawInsights(GraphicsContext g, double W, double H) {
        g.clearRect(0,0,W,H);
        long total    = filtered.size();
        long below45  = filtered.stream().filter(e->e.getSalary()<=45000).count();
        long adminPay = filtered.stream().filter(e->"ADMIN".equals(e.getEmpType())).mapToLong(e->(long)e.getSalary()).sum();
        long allPay   = filtered.stream().mapToLong(e->(long)e.getSalary()).sum();
        long hired19p = filtered.stream().filter(e->{
            try{return Integer.parseInt(e.getJoinDate().substring(0,4))>=2019;}
            catch(Exception ex){return false;}
        }).count();
        long cseCount = filtered.stream().filter(e->e.getDeptName()!=null&&e.getDeptName().contains("Computer")).count();

        String[] prefixes={"🟢","🟡","🔴","🟢","🟡"};
        String[] cols2={GREEN,AMBER,RED,GREEN,AMBER};
        String[] texts={
            "CSE is the largest dept with "+cseCount+" members ("+
                (total>0?cseCount*100/total:0)+"% of filtered staff).",
            below45+" of "+total+" employees earn ৳45,000 or below — primarily junior lecturers.",
            "Bus PUB-05 (Sariakandi Route) is currently INACTIVE — staff commuting impact detected.",
            hired19p+" employees joined from 2019 onward — significant recent growth phase.",
            "3 Admin roles account for ৳3,70,000/month — "+
                (allPay>0?String.format("%.1f",adminPay*100.0/allPay):"0")+"% of shown payroll."
        };

        // Section header
        g.setFont(Font.font("Consolas",FontWeight.BOLD,12));
        g.setFill(Color.web(TEAL));
        g.fillText("⚡  AUTOMATED INSIGHTS", 0, 17);

        double cardW=(W-4*12)/5.0, cardH=H-28, cardY=24;
        for (int i=0;i<5;i++) {
            double cx=i*(cardW+12);
            // Shadow
            g.setFill(Color.web("#000000",0.10)); g.fillRoundRect(cx+2,cardY+2,cardW,cardH,10,10);
            // Card
            g.setFill(Color.web(CARD())); rRect(g,cx,cardY,cardW,cardH,10); g.fill();
            // Left border
            g.setFill(Color.web(cols2[i])); g.fillRoundRect(cx,cardY,5,cardH,4,4);
            // Top line
            g.setFill(Color.web(cols2[i],0.12)); g.fillRect(cx+5,cardY,cardW-5,3);
            // Icon
            g.setFont(Font.font("System",16)); g.setFill(Color.web(cols2[i]));
            g.fillText(prefixes[i], cx+12, cardY+24);
            // Text
            g.setFont(Font.font("System",12)); g.setFill(Color.web(TXTS()));
            wrapText(g, texts[i], cx+12, cardY+40, cardW-18, 16);
            // Border
            g.setStroke(Color.web(BORDER(),0.5)); g.setLineWidth(1);
            g.strokeRoundRect(cx,cardY,cardW,cardH,10,10);
        }
    }

    // ── Drawing Utils ─────────────────────────────────────────────
    private void rRect(GraphicsContext g, double x, double y, double w, double h, double r) {
        g.beginPath();
        g.moveTo(x+r,y); g.lineTo(x+w-r,y);
        g.arcTo(x+w,y,x+w,y+r,r); g.lineTo(x+w,y+h-r);
        g.arcTo(x+w,y+h,x+w-r,y+h,r); g.lineTo(x+r,y+h);
        g.arcTo(x,y+h,x,y+h-r,r); g.lineTo(x,y+r);
        g.arcTo(x,y,x+r,y,r); g.closePath();
    }

    private void noData(GraphicsContext g, double W, double H) {
        g.setFont(Font.font("System",14)); g.setFill(Color.web(TXTM()));
        String msg = "No data matches current filters";
        g.fillText(msg, W/2-tw(g,msg)/2, H/2);
    }

    private void wrapText(GraphicsContext g, String text, double x, double y, double maxW, double lineH) {
        String[] words=text.split(" ");
        StringBuilder line=new StringBuilder();
        double cy=y;
        for (String w:words) {
            String test=line.isEmpty()?w:line+" "+w;
            if (tw(g,test)>maxW) {
                g.fillText(line.toString(),x,cy); cy+=lineH;
                line=new StringBuilder(w);
            } else { line=new StringBuilder(test); }
        }
        if (!line.isEmpty()) g.fillText(line.toString(),x,cy);
    }

    private double tw(GraphicsContext g, String text) {
        Text t=new Text(text); t.setFont(g.getFont());
        return t.getBoundsInLocal().getWidth();
    }

    private String str(long v) { return String.valueOf(v); }
    private String fmt(long v) {
        if (v>=100000) return String.format("%.1fL",v/100000.0);
        if (v>=1000)   return String.format("%.0fK",v/1000.0);
        return String.valueOf(v);
    }
}