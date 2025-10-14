package com.village.web;

import fi.iki.elonen.NanoHTTPD;
import com.village.repository.InMemoryRepository;
import com.village.service.*;
import com.village.model.*;

import java.io.IOException;
import java.util.*;

public class WebServer extends NanoHTTPD {
    private final InMemoryRepository repo;
    private final VehicleService vehicleService;
    private final BookingService bookingService;
    private final FareService fareService;
    private final ReportGenerator reportGenerator;

    public WebServer(int port,
                     InMemoryRepository repo,
                     VehicleService vs,
                     BookingService bs,
                     FareService fs,
                     ReportGenerator rg) {
        super(port);
        this.repo = repo;
        this.vehicleService = vs;
        this.bookingService = bs;
        this.fareService = fs;
        this.reportGenerator = rg;
    }

    @Override
    public Response serve(IHTTPSession session) {
        try {
            String uri = session.getUri();
            Method method = session.getMethod();

            if (uri.equals("/")) {
                return htmlResponse(indexPage());
            } else if (uri.equals("/vehicles")) {
                return htmlResponse(vehiclesPage());
            } else if (uri.equals("/addVehicle") && method == Method.POST) {
                Map<String, String> files = new HashMap<>();
                session.parseBody(files);
                Map<String, String> params = session.getParms();

                String driver = params.getOrDefault("driver", "unknown");
                String plate = params.getOrDefault("plate", "TN-00-XX-0000");
                int cap = Integer.parseInt(params.getOrDefault("capacity", "4"));
                String type = params.getOrDefault("type", "AUTO").toUpperCase();

                Vehicle v = new Vehicle(UUID.randomUUID().toString(), driver, plate, cap, VehicleType.valueOf(type));
                vehicleService.addVehicle(v);

                return htmlResponse(successPage("Vehicle added successfully!", "/vehicles"));
            } else if (uri.equals("/passengers")) {
                return htmlResponse(passengersPage());
            } else if (uri.equals("/routes")) {
                return htmlResponse(routesPage());
            } else if (uri.equals("/book") && method == Method.POST) {
                Map<String, String> files = new HashMap<>();
                session.parseBody(files);
                Map<String, String> params = session.getParms();

                String vehicleId = params.get("vehicleId");
                String passengerName = params.get("passengerName");
                String passengerPhone = params.get("passengerPhone");
                String routeId = params.get("routeId");

                Passenger p = bookingService.registerPassenger(passengerName, passengerPhone);
                Booking b = bookingService.createBooking(vehicleId, p.id(), routeId);

                double fare = fareService.calculateFare(
                        repo.findRoute(routeId)
                                .orElse(new SimpleRoute("Unknown", 1))
                                .getDistanceKm(),
                        repo.findVehicle(vehicleId)
                                .map(Vehicle::getVehicleType)
                                .map(Enum::name)
                                .orElse("AUTO")
                );

                fareService.recordFare(b.getId(), fare);
                return htmlResponse(successPage("Booking successful!", "/"));
            } else if (uri.equals("/report")) {
                return newFixedLengthResponse(Response.Status.OK, "text/plain", reportGenerator.generateSummaryReport());
            }

            return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Page Not Found");

        } catch (IOException | ResponseException e) {
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", "Error: " + e.getMessage());
        }
    }

    // --- Helper for styled HTML response ---
    private Response htmlResponse(String content) {
        String html = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Rural Transport Manager</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
                <style>
                    body { background-color: #f8f9fa; }
                    .navbar { background-color: #007bff; }
                    .navbar a { color: white !important; }
                    .card { margin-top: 20px; border-radius: 10px; }
                    .footer { text-align: center; padding: 10px; margin-top: 30px; color: #666; }
                </style>
            </head>
            <body>
                <nav class="navbar navbar-expand-lg navbar-dark">
                    <div class="container-fluid">
                        <a class="navbar-brand" href="/">Rural Transport</a>
                        <div class="collapse navbar-collapse">
                            <ul class="navbar-nav">
                                <li class="nav-item"><a class="nav-link" href="/vehicles">Vehicles</a></li>
                                <li class="nav-item"><a class="nav-link" href="/passengers">Passengers</a></li>
                                <li class="nav-item"><a class="nav-link" href="/routes">Routes</a></li>
                                <li class="nav-item"><a class="nav-link" href="/report">Reports</a></li>
                            </ul>
                        </div>
                    </div>
                </nav>
                <div class="container">%s</div>
                <div class="footer">© 2025 Rural Transport Manager</div>
            </body>
            </html>
        """.formatted(content);
        return newFixedLengthResponse(Response.Status.OK, "text/html", html);
    }

    private String indexPage() {
        return """
            <div class='text-center mt-5'>
                <h1 class='mb-4'>Welcome to Rural Transport Manager</h1>
                <p class='lead'>Manage vehicles, passengers, bookings, and routes for your rural transport system.</p>
                <div class='d-grid gap-2 d-sm-flex justify-content-sm-center mt-4'>
                    <a href='/vehicles' class='btn btn-primary btn-lg'>Manage Vehicles</a>
                    <a href='/routes' class='btn btn-success btn-lg'>View Routes</a>
                    <a href='/report' class='btn btn-secondary btn-lg'>Reports</a>
                </div>
            </div>
        """;
    }

    private String successPage(String message, String backUrl) {
        return """
            <div class='card shadow-sm p-4 text-center'>
                <h3 class='text-success mb-3'>✅ %s</h3>
                <a href='%s' class='btn btn-outline-primary'>Back</a>
            </div>
        """.formatted(message, backUrl);
    }

    private String vehiclesPage() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class='row'><div class='col-md-8'><div class='card p-3 shadow-sm'><h3>Vehicles</h3>");
        sb.append("<table class='table table-striped'><thead><tr><th>Type</th><th>Driver</th><th>Plate</th><th>Capacity</th></tr></thead><tbody>");
        for (Vehicle v : repo.findAllVehicles()) {
            sb.append("<tr><td>").append(v.getVehicleType()).append("</td><td>")
                    .append(v.getDriverName()).append("</td><td>")
                    .append(v.getPlateNumber()).append("</td><td>")
                    .append(v.getCapacity()).append("</td></tr>");
        }
        sb.append("</tbody></table></div></div>");
        sb.append("<div class='col-md-4'><div class='card p-3 shadow-sm'><h4>Add Vehicle</h4>");
        sb.append("""
            <form method='post' action='/addVehicle'>
                <div class='mb-2'><input class='form-control' name='driver' placeholder='Driver name'></div>
                <div class='mb-2'><input class='form-control' name='plate' placeholder='Plate number'></div>
                <div class='mb-2'><input class='form-control' name='capacity' placeholder='Capacity' value='4'></div>
                <div class='mb-2'>
                    <select class='form-select' name='type'>
                        <option>BUS</option><option>AUTO</option><option>CAB</option>
                    </select>
                </div>
                <button class='btn btn-primary w-100'>Add</button>
            </form>
        """);
        sb.append("</div></div></div>");
        sb.append("<div class='card p-3 mt-4'><h4>Book a Vehicle</h4>").append(bookForm()).append("</div>");
        return sb.toString();
    }

    private String bookForm() {
        StringBuilder sb = new StringBuilder();
        sb.append("<form method='post' action='/book' class='row g-2'>");
        sb.append("<div class='col-md-4'><input class='form-control' name='passengerName' placeholder='Passenger name'></div>");
        sb.append("<div class='col-md-4'><input class='form-control' name='passengerPhone' placeholder='Phone number'></div>");
        sb.append("<div class='col-md-2'><select class='form-select' name='vehicleId'>");
        for (Vehicle v : repo.findAllVehicles()) {
            sb.append("<option value='").append(v.getId()).append("'>").append(v.getDriverName()).append(" (").append(v.getVehicleType()).append(")").append("</option>");
        }
        sb.append("</select></div>");
        sb.append("<div class='col-md-2'><select class='form-select' name='routeId'>");
        for (Route r : repo.findAllRoutes()) {
            sb.append("<option value='").append(r.getId()).append("'>").append(r.getName()).append("</option>");
        }
        sb.append("</select></div>");
        sb.append("<div class='col-md-12'><button class='btn btn-success mt-2'>Book</button></div>");
        sb.append("</form>");
        return sb.toString();
    }

    private String passengersPage() {
        StringBuilder sb = new StringBuilder("<div class='card p-3 shadow-sm'><h3>Passengers</h3><ul class='list-group'>");
        for (Passenger p : repo.findAllPassengers()) {
            sb.append("<li class='list-group-item'>")
                    .append("<strong>").append(p.name()).append("</strong> - ").append(p.phone())
                    .append("</li>");
        }
        sb.append("</ul></div>");
        return sb.toString();
    }

    private String routesPage() {
        StringBuilder sb = new StringBuilder("<div class='card p-3 shadow-sm'><h3>Available Routes</h3><ul class='list-group'>");
        for (Route r : repo.findAllRoutes()) {
            sb.append("<li class='list-group-item'>")
                    .append("<strong>").append(r.getName()).append("</strong> - ")
                    .append(r.getDistanceKm()).append(" km</li>");
        }
        sb.append("</ul></div>");
        return sb.toString();
    }
}
