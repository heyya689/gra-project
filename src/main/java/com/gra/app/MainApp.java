package com.gra.app;

import com.gra.dao.*;
import com.gra.model.*;
import java.util.List;
import java.util.Scanner;

public class MainApp {
    private static Scanner scanner = new Scanner(System.in);
    private static UserDAO userDAO = new UserDAO();
    private static BiznesDAO biznesDAO = new BiznesDAO();
    private static RezervimDAO rezervimDAO = new RezervimDAO();
    private static VleresimDAO vleresimDAO = new VleresimDAO();
    private static KategoriDAO kategoriDAO = new KategoriDAO();

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  🏨 GRA - Online Reservation System  ");
        System.out.println("      (Version Complete)               ");
        System.out.println("========================================");

        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = getChoice();

            switch (choice) {
                case 1: manageUsers(); break;
                case 2: manageBusinesses(); break;
                case 3: manageReservations(); break;
                case 4: manageReviews(); break;
                case 5: manageCategories(); break;
                case 6: runDemo(); break;
                case 0: {
                    System.out.println("👋 Dalje nga aplikacioni...");
                    running = false; break;
                }
                default: System.out.println("⚠️ Zgjedhje e pavlefshme!"); break;
            }

            if (running && choice != 0) {
                System.out.println("\nShtyp Enter për të vazhduar...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n===== MENU KRYESORE =====");
        System.out.println("1. 👥 Menaxho Përdoruesit");
        System.out.println("2. 🏢 Menaxho Bizneset");
        System.out.println("3. 📅 Menaxho Rezervimet");
        System.out.println("4. ⭐ Menaxho Vlerësimet");
        System.out.println("5. 🏷️ Menaxho Kategoritë");
        System.out.println("6. 🎬 Demo i Sistemit");
        System.out.println("0. 🚪 Dil");
        System.out.print("Zgjedhja juaj: ");
    }

    private static int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void manageUsers() {
        boolean inUserMenu = true;
        while (inUserMenu) {
            System.out.println("\n===== MENU PËRDORUESIT =====");
            System.out.println("1. 📋 Listo të gjithë përdoruesit");
            System.out.println("2. ➕ Krijo përdorues të ri");
            System.out.println("3. ✏️ Përditëso përdorues");
            System.out.println("4. 🔍 Kërko përdorues me email");
            System.out.println("5. 🗑️ Fshi përdorues");
            System.out.println("6. 🔐 Testo Login");
            System.out.println("0. ↩️ Kthehu në menunë kryesore");
            System.out.print("Zgjedhja: ");

            int choice = getChoice();
            switch (choice) {
                case 1: listAllUsers(); break;
                case 2: createNewUser(); break;
                case 3: updateUser();break;
                case 4: searchUserByEmail(); break;
                case 5: deleteUser(); break;
                case 6: testLogin(); break;
                case 0: inUserMenu = false; break;
                default: System.out.println("Zgjedhje e pavlefshme!"); break;
            }
        }
    }

    private static void listAllUsers() {
        try {
            System.out.println("\n=== LISTA E PËRDORUESVE ===");
            List<User> users = userDAO.findAll();

            if (users.isEmpty()) {
                System.out.println("Nuk ka përdorues në sistem.");
            } else {
                System.out.printf("%-5s %-20s %-25s %-15s\n", "ID", "Emri", "Email", "Data e Krijimit");
                System.out.println("-----------------------------------------------------------");

                for (User user : users) {
                    System.out.printf("%-5d %-20s %-25s %-15s\n",
                            user.getUserId(),
                            user.getName(),
                            user.getEmail(),
                            user.getCreatedAt() != null ?
                                    user.getCreatedAt().toLocalDate().toString() : "N/A");
                }
                System.out.println("-----------------------------------------------------------");
                System.out.println("Total: " + users.size() + " përdorues");
            }
        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createNewUser() {
        try {
            System.out.println("\n=== KRIJIMI I PËRDORUESIT TË RI ===");

            System.out.print("Emri: ");
            String name = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            System.out.print("Telefon (opsional): ");
            String phone = scanner.nextLine();

            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setPhone(phone);

            userDAO.save(newUser);

            System.out.println("✅ Përdoruesi u krijua me sukses! ID: " + newUser.getUserId());
        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void manageBusinesses() {
        boolean inBiznesMenu = true;
        while (inBiznesMenu) {
            System.out.println("\n===== MENU BIZNESET =====");
            System.out.println("1. 📋 Listo të gjitha bizneset");
            System.out.println("2. ➕ Krijo biznes të ri");
            System.out.println("3. 🔍 Kërko biznese sipas kategorisë");
            System.out.println("4. 📊 Shiko statistikat e biznesit");
            System.out.println("0. ↩️ Kthehu në menunë kryesore");
            System.out.print("Zgjedhja: ");

            int choice = getChoice();
            switch (choice) {
                case 1: listAllBusinesses(); break;
                case 2: createNewBusiness(); break;
                case 3: searchBusinessByCategory(); break;
                case 4: showBusinessStats(); break;
                case 0: inBiznesMenu = false; break;
                default: System.out.println("Zgjedhje e pavlefshme!"); break;
            }
        }
    }

    private static void listAllBusinesses() {
        try {
            System.out.println("\n=== LISTA E BIZNESEVE ===");
            List<Biznes> businesses = biznesDAO.findAll();

            if (businesses.isEmpty()) {
                System.out.println("Nuk ka biznese në sistem.");
            } else {
                System.out.printf("%-5s %-25s %-15s %-12s\n", "ID", "Emri", "Kategoria", "NIPT");
                System.out.println("-----------------------------------------------------------");

                for (Biznes biznes : businesses) {
                    System.out.printf("%-5d %-25s %-15s %-12s\n",
                            biznes.getBiznesId(),
                            biznes.getEmri(),
                            biznes.getKategori() != null ? biznes.getKategori() : "N/A",
                            biznes.getNipt());
                }
                System.out.println("-----------------------------------------------------------");
                System.out.println("Total: " + businesses.size() + " biznese");
            }
        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createNewBusiness() {
        try {
            System.out.println("\n=== KRIJIMI I BIZNESIT TË RI ===");

            System.out.print("Emri i biznesit: ");
            String emri = scanner.nextLine();

            System.out.print("NIPT: ");
            String nipt = scanner.nextLine();

            System.out.print("Kategoria: ");
            String kategori = scanner.nextLine();

            System.out.print("Pershkrim (opsional): ");
            String pershkrim = scanner.nextLine();

            System.out.print("License (opsional): ");
            String license = scanner.nextLine();

            System.out.print("Telefon: ");
            String telefon = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            Biznes newBiznes = new Biznes();
            newBiznes.setEmri(emri);
            newBiznes.setNipt(nipt);
            newBiznes.setKategori(kategori);
            newBiznes.setPershkrim(pershkrim);
            newBiznes.setLicense(license);
            newBiznes.setTelefon(telefon);
            newBiznes.setEmail(email);

            biznesDAO.save(newBiznes);

            System.out.println("✅ Biznesi u krijua me sukses! ID: " + newBiznes.getBiznesId());
        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void manageReservations() {
        boolean inReservationMenu = true;
        while (inReservationMenu) {
            System.out.println("\n===== MENU REZERVIMET =====");
            System.out.println("1. 📋 Listo të gjitha rezervimet");
            System.out.println("2. ➕ Krijo rezervim të ri");
            System.out.println("3. ✅ Konfirmo rezervim");
            System.out.println("4. ❌ Anullo rezervim");
            System.out.println("5. 🔍 Shiko rezervimet e përdoruesit");
            System.out.println("0. ↩️ Kthehu në menunë kryesore");
            System.out.print("Zgjedhja: ");

            int choice = getChoice();
            switch (choice) {
                case 1: listAllReservations(); break;
                case 2: createReservation(); break;
                case 3: confirmReservation(); break;
                case 4: cancelReservation(); break;
                case 5: findReservationsByUser(); break;
                case 0: inReservationMenu = false; break;
                default: System.out.println("Zgjedhje e pavlefshme!"); break;
            }
        }
    }

    private static void listAllReservations() {
        try {
            System.out.println("\n=== LISTA E REZERVIMEVE ===");
            List<Rezervim> reservations = rezervimDAO.findAll();

            if (reservations.isEmpty()) {
                System.out.println("Nuk ka rezervime në sistem.");
            } else {
                System.out.printf("%-5s %-15s %-20s %-20s %-8s %-10s\n",
                        "ID", "User ID", "Biznes ID", "Data", "Persona", "Statusi");
                System.out.println("-------------------------------------------------------------------");

                for (Rezervim res : reservations) {
                    System.out.printf("%-5d %-15d %-20d %-20s %-8d %-10s\n",
                            res.getRezervimId(),
                            res.getUser().getUserId(),
                            res.getBiznes().getBiznesId(),
                            res.getData() != null ?
                                    res.getData().toLocalDate().toString() : "N/A",
                            res.getNumriPersonave(),
                            res.getStatus());
                }
                System.out.println("Total: " + reservations.size() + " rezervime");
            }
        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void manageReviews() {
        boolean inReviewMenu = true;
        while (inReviewMenu) {
            System.out.println("\n===== MENU VLERËSIMET =====");
            System.out.println("1. 📋 Listo të gjitha vlerësimet");
            System.out.println("2. ➕ Krijo vlerësim të ri");
            System.out.println("3. ✅ Mirato vlerësim");
            System.out.println("4. 🔍 Shiko vlerësimet e biznesit");
            System.out.println("0. ↩️ Kthehu në menunë kryesore");
            System.out.print("Zgjedhja: ");

            int choice = getChoice();
            switch (choice) {
                case 1: listAllReviews(); break;
                case 2: createReview(); break;
                case 3: approveReview(); break;
                case 4: findReviewsByBusiness(); break;
                case 0: inReviewMenu = false; break;
                default: System.out.println("Zgjedhje e pavlefshme!"); break;
            }
        }
    }

    private static void listAllReviews() {
        try {
            System.out.println("\n=== LISTA E VLERËSIMEVE ===");
            List<Vleresim> reviews = vleresimDAO.findAll();

            if (reviews.isEmpty()) {
                System.out.println("Nuk ka vlerësime në sistem.");
            } else {
                System.out.printf("%-5s %-20s %-25s %-7s %-10s\n",
                        "ID", "Përdorues", "Biznes", "Rating", "Status");
                System.out.println("-------------------------------------------------------------------");

                for (Vleresim review : reviews) {
                    System.out.printf("%-5d %-20s %-25s %-7d %-10s\n",
                            review.getVleresimId(),
                            review.getUser().getName(),
                            review.getBiznes().getEmri(),
                            review.getRating(),
                            review.isApproved() ? "✅ Miratuar" : "⏳ Në pritje");
                }
                System.out.println("Total: " + reviews.size() + " vlerësime");
            }
        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void manageCategories() {
        boolean inCategoryMenu = true;
        while (inCategoryMenu) {
            System.out.println("\n===== MENU KATEGORITË =====");
            System.out.println("1. 📋 Listo të gjitha kategoritë");
            System.out.println("2. ➕ Krijo kategori të re");
            System.out.println("3. 🔍 Shiko bizneset sipas kategorisë");
            System.out.println("0. ↩️ Kthehu në menunë kryesore");
            System.out.print("Zgjedhja: ");

            int choice = getChoice();
            switch (choice) {
                case 1: listAllCategories(); break;
                case 2: createCategory(); break;
                case 3: findBusinessesByCategory(); break;
                case 0: inCategoryMenu = false; break;
                default: System.out.println("Zgjedhje e pavlefshme!"); break;
            }
        }
    }

    private static void listAllCategories() {
        try {
            System.out.println("\n=== LISTA E KATEGORIVE ===");
            List<Kategori> categories = kategoriDAO.findAll();

            if (categories.isEmpty()) {
                System.out.println("Nuk ka kategori në sistem.");
            } else {
                System.out.printf("%-5s %-20s %-30s %-10s\n",
                        "ID", "Emri", "Pershkrim", "Biznese");
                System.out.println("-------------------------------------------------------------------");

                for (Kategori kategori : categories) {
                    System.out.printf("%-5d %-20s %-30s %-10d\n",
                            kategori.getKategoriId(),
                            kategori.getDisplayName(),
                            kategori.getPershkrim() != null ?
                                    (kategori.getPershkrim().length() > 25 ?
                                            kategori.getPershkrim().substring(0, 22) + "..." :
                                            kategori.getPershkrim()) : "N/A",
                            kategori.getBiznesCount());
                }
                System.out.println("Total: " + categories.size() + " kategori");
            }
        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runDemo() {
        System.out.println("\n🎬 DEMO I SISTEMIT GRA");
        System.out.println("======================");

        try {
            // 1. Krijo disa objekte demo
            System.out.println("\n1. 🆕 Krijimi i objekteve demo...");

            // Krijo përdorues
            User demoUser = new User();
            demoUser.setName("Demo User");
            demoUser.setEmail("demo@gra.com");
            demoUser.setPassword("demopass123");

            // Krijo biznes
            Biznes demoBiznes = new Biznes();
            demoBiznes.setEmri("Demo Restaurant");
            demoBiznes.setNipt("L12345678D");
            demoBiznes.setKategori("RESTAURANT");
            demoBiznes.setPershkrim("Një restorant i shkëlqyer për demo");

            // Krijo lokacion
            Lokacion demoLocation = new Lokacion();
            demoLocation.setQyteti("Tirana");
            demoLocation.setAdresa("Rruga e Demo");
            demoLocation.setLatitude(41.3275);
            demoLocation.setLongitude(19.8187);

            // Krijo inventar
            Inventari demoInventar = new Inventari();
            demoInventar.setEmerProdukt("Tavolina për 4 persona");
            demoInventar.setSasi(10);
            demoInventar.setCmimi(0.0);

            // Krijo rezervim
            Rezervim demoRezervim = new Rezervim();
            demoRezervim.setUser(demoUser);
            demoRezervim.setBiznes(demoBiznes);
            demoRezervim.setData(java.time.LocalDateTime.now().plusDays(1));
            demoRezervim.setNumriPersonave(4);

            // Krijo pagesë
            Pagesat demoPagesa = new Pagesat();
            demoPagesa.setShuma(50.0);
            demoPagesa.setMetoda("CARD");

            // Krijo vlerësim
            Vleresim demoVleresim = new Vleresim();
            demoVleresim.setUser(demoUser);
            demoVleresim.setBiznes(demoBiznes);
            demoVleresim.setRating(5);
            demoVleresim.setKoment("Shërbim i shkëlqyer! Rekomandoj!");

            // Krijo kategori
            Kategori demoKategori = new Kategori();
            demoKategori.setEmri("DEMO");
            demoKategori.setIkona("🎯");
            demoKategori.setPershkrim("Kategori demo për testim");

            System.out.println("✅ Objektet demo u krijuan me sukses!");

            // 2. Demonstro funksionalitete
            System.out.println("\n2. 🧪 Testimi i funksionaliteteve...");

            // Testo login
            System.out.print("   🔐 Testo login: ");
            boolean loginSuccess = demoUser.login("demo@gra.com", "demopass123");
            System.out.println(loginSuccess ? "✅ Sukses" : "❌ Dështoi");

            // Testo inventarin
            System.out.print("   📦 Testo inventar: ");
            demoInventar.decreaseStock(1);
            System.out.println("Stock i mbetur: " + demoInventar.getSasi());

            // Testo rezervimin
            System.out.print("   📅 Testo rezervim: ");
            demoRezervim.create();
            System.out.println("Statusi: " + demoRezervim.getStatus());

            // Testo pagesën
            System.out.print("   💳 Testo pagesë: ");
            boolean paymentProcessed = demoPagesa.processPayment();
            System.out.println(paymentProcessed ? "✅ Pagesa u procesua" : "❌ Dështoi");

            // Testo vlerësimin
            System.out.print("   ⭐ Testo vlerësim: ");
            demoVleresim.submit();
            System.out.println("Rating: " + demoVleresim.getRating());

            // 3. Shfaq informacion
            System.out.println("\n3. 📊 Informacioni i sistemit demo:");
            System.out.println("   👤 Përdorues: " + demoUser.getName() + " (" + demoUser.getEmail() + ")");
            System.out.println("   🏢 Biznes: " + demoBiznes.getEmri() + " - " + demoBiznes.getKategori());
            System.out.println("   📍 Lokacion: " + demoLocation.getQyteti() + ", " + demoLocation.getAdresa());
            System.out.println("   📅 Rezervim për: " + demoRezervim.getNumriPersonave() + " persona");
            System.out.println("   💰 Pagesa: " + demoPagesa.getShuma() + "€ - " + demoPagesa.getStatus());
            System.out.println("   ⭐ Vlerësim: " + demoVleresim.getRating() + "/5");
            System.out.println("   🏷️ Kategori: " + demoKategori.getDisplayName());

            System.out.println("\n🎉 Demo u përfundua me sukses!");

        } catch (Exception e) {
            System.err.println("❌ Gabim gjatë demos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void updateUser() {
        try {
            System.out.print("\nShkruaj ID-në e përdoruesit për të përditësuar: ");
            int userId = Integer.parseInt(scanner.nextLine());

            User user = userDAO.findById(userId);
            if (user == null) {
                System.out.println("❌ Përdoruesi nuk u gjet!");
                return;
            }

            System.out.println("Përdoruesi aktual: " + user.getName() + " (" + user.getEmail() + ")");

            System.out.print("Emri i ri (lëre bosh për të mos ndryshuar): ");
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) {
                user.setName(newName);
            }

            System.out.print("Email i ri (lëre bosh për të mos ndryshuar): ");
            String newEmail = scanner.nextLine();
            if (!newEmail.isEmpty()) {
                user.setEmail(newEmail);
            }

            System.out.print("Password i ri (lëre bosh për të mos ndryshuar): ");
            String newPassword = scanner.nextLine();
            if (!newPassword.isEmpty()) {
                user.setPassword(newPassword);
            }

            userDAO.update(user);
            System.out.println("✅ Profili u përditësua me sukses!");

        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void searchUserByEmail() {
        try {
            System.out.print("\nShkruaj email-in: ");
            String email = scanner.nextLine();

            User user = userDAO.findByEmail(email);
            if (user == null) {
                System.out.println("❌ Nuk u gjet përdorues me këtë email.");
            } else {
                System.out.println("\n✅ PËRDORUESI U GJET:");
                System.out.println("ID: " + user.getUserId());
                System.out.println("Emri: " + user.getName());
                System.out.println("Email: " + user.getEmail());
                System.out.println("Telefon: " + (user.getPhone() != null ? user.getPhone() : "N/A"));
                System.out.println("Krijuar më: " +
                        (user.getCreatedAt() != null ? user.getCreatedAt().toLocalDate() : "N/A"));
            }
        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void deleteUser() {
        try {
            System.out.print("\nShkruaj ID-në e përdoruesit për të fshirë: ");
            int userId = Integer.parseInt(scanner.nextLine());

            System.out.print("Jeni i sigurt? (shkruaj 'PO' për të vazhduar): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("PO")) {
                userDAO.delete(userId);
                System.out.println("✅ Përdoruesi u fshi me sukses!");
            } else {
                System.out.println("❌ Operacioni u anullua.");
            }
        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testLogin() {
        try {
            System.out.print("\nEmail: ");
            String email = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            User user = userDAO.findByEmail(email);
            if (user == null) {
                System.out.println("❌ Përdoruesi nuk ekziston!");
                return;
            }

            if (user.login(email, password)) {
                System.out.println("✅ Login i suksesshëm!");
                System.out.println("👤 Përshëndetje, " + user.getName() + "!");
            } else {
                System.out.println("❌ Password i gabuar!");
            }

        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void searchBusinessByCategory() {
        try {
            System.out.print("\nShkruaj kategorinë: ");
            String category = scanner.nextLine();

            List<Biznes> businesses = biznesDAO.findByCategory(category);

            if (businesses.isEmpty()) {
                System.out.println("Nuk u gjet asnjë biznes me kategorinë: " + category);
            } else {
                System.out.println("\n=== BIZNESET E KATEGORISË " + category + " ===");
                System.out.printf("%-5s %-25s %-15s %-12s\n", "ID", "Emri", "NIPT", "Telefon");
                System.out.println("-----------------------------------------------------------");

                for (Biznes biznes : businesses) {
                    System.out.printf("%-5d %-25s %-15s %-12s\n",
                            biznes.getBiznesId(),
                            biznes.getEmri(),
                            biznes.getNipt(),
                            biznes.getTelefon() != null ? biznes.getTelefon() : "N/A");
                }
                System.out.println("Gjetur: " + businesses.size() + " biznese");
            }
        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void showBusinessStats() {
        try {
            System.out.print("\nShkruaj ID-në e biznesit: ");
            int businessId = Integer.parseInt(scanner.nextLine());

            Biznes biznes = biznesDAO.findById(businessId);
            if (biznes == null) {
                System.out.println("❌ Biznesi nuk u gjet!");
                return;
            }

            System.out.println("\n=== STATISTIKAT E BIZNESIT ===");
            System.out.println("Emri: " + biznes.getEmri());
            System.out.println("NIPT: " + biznes.getNipt());
            System.out.println("Kategoria: " + biznes.getKategori());
            System.out.println("Email: " + biznes.getEmail());
            System.out.println("Telefon: " + biznes.getTelefon());
            System.out.println("Krijuar më: " +
                    (biznes.getCreatedAt() != null ? biznes.getCreatedAt().toLocalDate() : "N/A"));

            // Stats shtesë (nëse janë implementuar)
            System.out.println("\n📊 Statistikat:");
            System.out.println("- Rezervimet: " + biznes.getRezervimet().size());
            System.out.println("- Vlerësimet: " + biznes.getVleresimet().size());
            System.out.println("- Inventari: " + biznes.getInventari().size());
            System.out.println("- Imazhe: " + biznes.getImazhet().size());

        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createReservation() {
        try {
            System.out.println("\n=== KRIJIMI I REZERVIMIT ===");

            // Listo përdoruesit
            List<User> users = userDAO.findAll();
            if (users.isEmpty()) {
                System.out.println("Nuk ka përdorues në sistem. Së pari krijoni një përdorues.");
                return;
            }

            System.out.println("Përdoruesit e disponueshëm:");
            for (User user : users) {
                System.out.println(user.getUserId() + ". " + user.getName() + " (" + user.getEmail() + ")");
            }

            System.out.print("\nZgjidh ID-në e përdoruesit: ");
            int userId = Integer.parseInt(scanner.nextLine());

            // Listo bizneset
            List<Biznes> businesses = biznesDAO.findAll();
            if (businesses.isEmpty()) {
                System.out.println("Nuk ka biznese në sistem. Së pari krijoni një biznes.");
                return;
            }

            System.out.println("\nBizneset e disponueshme:");
            for (Biznes biznes : businesses) {
                System.out.println(biznes.getBiznesId() + ". " + biznes.getEmri() +
                        " (" + (biznes.getKategori() != null ? biznes.getKategori() : "N/A") + ")");
            }

            System.out.print("\nZgjidh ID-në e biznesit: ");
            int businessId = Integer.parseInt(scanner.nextLine());

            System.out.print("Data dhe ora e rezervimit (YYYY-MM-DD HH:MM): ");
            String dateTime = scanner.nextLine() + ":00";

            System.out.print("Numri i personave: ");
            int numberOfPeople = Integer.parseInt(scanner.nextLine());

            System.out.print("Shënime (opsional): ");
            String notes = scanner.nextLine();

            // Krijo rezervimin
            Rezervim reservation = new Rezervim();

            User user = userDAO.findById(userId);
            Biznes biznes = biznesDAO.findById(businessId);

            if (user == null || biznes == null) {
                System.out.println("❌ Përdoruesi ose biznesi nuk u gjet!");
                return;
            }

            reservation.setUser(user);
            reservation.setBiznes(biznes);
            reservation.setData(java.time.LocalDateTime.parse(dateTime.replace(" ", "T")));
            reservation.setNumriPersonave(numberOfPeople);
            reservation.setShenime(notes);
            reservation.setStatus("PENDING");

            rezervimDAO.save(reservation);

            System.out.println("✅ Rezervimi u krijua me sukses!");
            System.out.println("Rezervimi ID: " + reservation.getRezervimId());

        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void confirmReservation() {
        try {
            System.out.print("\nShkruaj ID-në e rezervimit për të konfirmuar: ");
            int reservationId = Integer.parseInt(scanner.nextLine());

            Rezervim reservation = rezervimDAO.findById(reservationId);
            if (reservation == null) {
                System.out.println("❌ Rezervimi nuk u gjet!");
                return;
            }

            if (reservation.getStatus().equals("PENDING")) {
                reservation.confirm();
                rezervimDAO.update(reservation);
                System.out.println("✅ Rezervimi u konfirmua me sukses!");
            } else {
                System.out.println("❌ Rezervimi nuk është në status PENDING. Statusi aktual: " + reservation.getStatus());
            }

        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void cancelReservation() {
        try {
            System.out.print("\nShkruaj ID-në e rezervimit për të anulluar: ");
            int reservationId = Integer.parseInt(scanner.nextLine());

            Rezervim reservation = rezervimDAO.findById(reservationId);
            if (reservation == null) {
                System.out.println("❌ Rezervimi nuk u gjet!");
                return;
            }

            if (reservation.canBeCancelled()) {
                reservation.cancel();
                rezervimDAO.update(reservation);
                System.out.println("✅ Rezervimi u anullua me sukses!");
            } else {
                System.out.println("❌ Rezervimi nuk mund të anullohet. Statusi aktual: " + reservation.getStatus());
            }

        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void findReservationsByUser() {
        try {
            System.out.print("\nShkruaj ID-në e përdoruesit: ");
            int userId = Integer.parseInt(scanner.nextLine());

            List<Rezervim> reservations = rezervimDAO.findByUserId(userId);

            if (reservations.isEmpty()) {
                System.out.println("Nuk ka rezervime për këtë përdorues.");
            } else {
                System.out.println("\n=== REZERVIMET E PËRDORUESIT ===");
                System.out.printf("%-5s %-20s %-20s %-8s %-10s\n",
                        "ID", "Biznes", "Data", "Persona", "Statusi");
                System.out.println("-------------------------------------------------------------------");

                for (Rezervim res : reservations) {
                    System.out.printf("%-5d %-20s %-20s %-8d %-10s\n",
                            res.getRezervimId(),
                            res.getBiznes().getEmri(),
                            res.getData() != null ?
                                    res.getData().toLocalDate().toString() : "N/A",
                            res.getNumriPersonave(),
                            res.getStatus());
                }
                System.out.println("Total: " + reservations.size() + " rezervime");
            }
        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createReview() {
        try {
            System.out.println("\n=== KRIJIMI I VLERËSIMIT ===");

            // Listo përdoruesit
            List<User> users = userDAO.findAll();
            if (users.isEmpty()) {
                System.out.println("Nuk ka përdorues në sistem.");
                return;
            }

            // Listo bizneset
            List<Biznes> businesses = biznesDAO.findAll();
            if (businesses.isEmpty()) {
                System.out.println("Nuk ka biznese në sistem.");
                return;
            }

            System.out.print("Shkruaj ID-në e përdoruesit: ");
            int userId = Integer.parseInt(scanner.nextLine());

            System.out.print("Shkruaj ID-në e biznesit: ");
            int businessId = Integer.parseInt(scanner.nextLine());

            System.out.print("Rating (1-5): ");
            int rating = Integer.parseInt(scanner.nextLine());

            if (rating < 1 || rating > 5) {
                System.out.println("❌ Rating duhet të jetë ndërmjet 1 dhe 5!");
                return;
            }

            System.out.print("Koment: ");
            String comment = scanner.nextLine();

            User user = userDAO.findById(userId);
            Biznes biznes = biznesDAO.findById(businessId);

            if (user == null || biznes == null) {
                System.out.println("❌ Përdoruesi ose biznesi nuk u gjet!");
                return;
            }

            Vleresim review = new Vleresim();
            review.setUser(user);
            review.setBiznes(biznes);
            review.setRating(rating);
            review.setKoment(comment);
            review.setApproved(false); // Në fillim nuk është i miratuar

            vleresimDAO.save(review);

            System.out.println("✅ Vlerësimi u dërgua për miratim!");
            System.out.println("Vlerësimi ID: " + review.getVleresimId());

        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void approveReview() {
        try {
            System.out.print("\nShkruaj ID-në e vlerësimit për të miratuar: ");
            int reviewId = Integer.parseInt(scanner.nextLine());

            Vleresim review = vleresimDAO.findById(reviewId);
            if (review == null) {
                System.out.println("❌ Vlerësimi nuk u gjet!");
                return;
            }

            if (!review.isApproved()) {
                review.setApproved(true);
                vleresimDAO.update(review);
                System.out.println("✅ Vlerësimi u miratua me sukses!");
            } else {
                System.out.println("ℹ️ Vlerësimi është tashmë i miratuar.");
            }

        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void findReviewsByBusiness() {
        try {
            System.out.print("\nShkruaj ID-në e biznesit: ");
            int businessId = Integer.parseInt(scanner.nextLine());

            List<Vleresim> reviews = vleresimDAO.findByBusinessId(businessId);

            if (reviews.isEmpty()) {
                System.out.println("Nuk ka vlerësime për këtë biznes.");
            } else {
                System.out.println("\n=== VLERËSIMET E BIZNESIT ===");
                System.out.printf("%-5s %-20s %-7s %-40s %-10s\n",
                        "ID", "Përdorues", "Rating", "Koment", "Status");
                System.out.println("-------------------------------------------------------------------");

                for (Vleresim review : reviews) {
                    String shortComment = review.getKoment();
                    if (shortComment.length() > 35) {
                        shortComment = shortComment.substring(0, 32) + "...";
                    }

                    System.out.printf("%-5d %-20s %-7d %-40s %-10s\n",
                            review.getVleresimId(),
                            review.getUser().getName(),
                            review.getRating(),
                            shortComment,
                            review.isApproved() ? "✅" : "⏳");
                }

                // Llogarit rating mesatar
                double avgRating = 0;
                if (!reviews.isEmpty()) {
                    for (Vleresim r : reviews) {
                        avgRating += r.getRating();
                    }
                    avgRating /= reviews.size();
                }

                System.out.println("\n📊 Statistikat:");
                System.out.println("- Total vlerësime: " + reviews.size());
                System.out.println("- Rating mesatar: " + String.format("%.1f/5", avgRating));
                System.out.println("- Vlerësime të miratuara: " +
                        reviews.stream().filter(Vleresim::isApproved).count());
            }
        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createCategory() {
        try {
            System.out.print("\nEmri i kategorisë: ");
            String name = scanner.nextLine();

            System.out.print("Ikona (emoji, opsionale): ");
            String icon = scanner.nextLine();

            System.out.print("Pershkrim (opsionale): ");
            String description = scanner.nextLine();

            Kategori kategori = new Kategori();
            kategori.setEmri(name);
            kategori.setIkona(icon);
            kategori.setPershkrim(description);

            kategoriDAO.save(kategori);

            System.out.println("✅ Kategoria u krijua me sukses! ID: " + kategori.getKategoriId());

        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void findBusinessesByCategory() {
        try {
            System.out.print("\nShkruaj ID-në e kategorisë: ");
            int categoryId = Integer.parseInt(scanner.nextLine());

            Kategori kategori = kategoriDAO.findById(categoryId);
            if (kategori == null) {
                System.out.println("❌ Kategoria nuk u gjet!");
                return;
            }

            List<Biznes> businesses = kategori.getBizneset();

            if (businesses.isEmpty()) {
                System.out.println("Nuk ka biznese në këtë kategori.");
            } else {
                System.out.println("\n=== BIZNESET E KATEGORISË: " + kategori.getDisplayName() + " ===");
                System.out.printf("%-5s %-25s %-15s %-20s\n", "ID", "Emri", "NIPT", "Email");
                System.out.println("-------------------------------------------------------------------");

                for (Biznes biznes : businesses) {
                    System.out.printf("%-5d %-25s %-15s %-20s\n",
                            biznes.getBiznesId(),
                            biznes.getEmri(),
                            biznes.getNipt(),
                            biznes.getEmail() != null ? biznes.getEmail() : "N/A");
                }
                System.out.println("Total: " + businesses.size() + " biznese");
            }
        } catch (Exception e) {
            System.err.println("Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }
}