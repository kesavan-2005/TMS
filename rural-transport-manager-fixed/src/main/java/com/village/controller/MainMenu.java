package com.village.controller;

import com.village.repository.InMemoryRepository;
import com.village.service.*;
import com.village.model.*;
import com.village.web.WebServer;
import fi.iki.elonen.NanoHTTPD;

import java.util.Scanner;

public class MainMenu {
    public static void main(String[] args) throws Exception {
        InMemoryRepository repo = new InMemoryRepository();
        VehicleService vehicleService = new VehicleServiceImpl(repo);
        BookingService bookingService = new BookingServiceImpl(repo);
        FareService fareService = new FareServiceImpl(repo);
        ReportGenerator reportGenerator = new ReportGeneratorImpl(repo);

        // Add some demo data
        Vehicle v1 = new Vehicle(java.util.UUID.randomUUID().toString(), "Raju", "TN-01-AB-1234", 40, com.village.model.VehicleType.BUS);
        Vehicle v2 = new Vehicle(java.util.UUID.randomUUID().toString(), "Kumar", "TN-02-CD-5678", 4, com.village.model.VehicleType.AUTO);
        vehicleService.addVehicle(v1);
        vehicleService.addVehicle(v2);

        // Add default routes
        bookingService.addRoute("VillageA-VillageB", 12.5);
        bookingService.addRoute("VillageB-VillageC", 8.0);

        // Start web server
        WebServer server = new WebServer(8080, repo, vehicleService, bookingService, fareService, reportGenerator);
        server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("Web server started at http://localhost:8080/");

        // Simple console menu
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Rural Transport Manager ---");
            System.out.println("1. List vehicles");
            System.out.println("2. Add vehicle");
            System.out.println("3. List routes");
            System.out.println("4. Show web URL");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> vehicleService.listVehicles().forEach(System.out::println);
                case "2" -> {
                    System.out.print("Driver name: "); String dn = sc.nextLine();
                    System.out.print("Plate (eg TN-01-AB-1234): "); String plate = sc.nextLine();
                    System.out.print("Capacity: "); int cap = Integer.parseInt(sc.nextLine());
                    System.out.print("Type (BUS/AUTO/CAB): "); String t = sc.nextLine();
                    VehicleType vt = VehicleType.valueOf(t.toUpperCase());
                    Vehicle v = new Vehicle(java.util.UUID.randomUUID().toString(), dn, plate, cap, vt);
                    vehicleService.addVehicle(v);
                    System.out.println("Added: " + v);
                }
                case "3" -> bookingService.listRoutes().forEach(System.out::println);
                case "4" -> System.out.println("Web server is running at http://localhost:8080/"); 
                case "5" -> { System.out.println("Shutting down..."); server.stop(); System.exit(0); }
                default -> System.out.println("Invalid choice");
            }
        }
    }
}
